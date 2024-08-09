package io.github.steveplays28.blinkload.mixin.server;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.event.LifecycleEvent;
import io.github.steveplays28.blinkload.util.ThreadUtil;
import net.minecraft.Bootstrap;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.server.Main;
import net.minecraft.util.crash.CrashMemoryReserve;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Mixin(Main.class)
public class ServerMainMixin {
	@Unique
	private static @Nullable CompletableFuture<Void> blinkload$bootstrapInitializationCompletableFuture = null;

	@Inject(method = "main", at = @At(value = "HEAD"))
	private static void blinkload$initializeBootstrapOffThread(String[] args, CallbackInfo ci) {
		LifecycleEvent.BEFORE_BOOTSTRAP_FINISH_INITIALIZATION.register(
				ServerMainMixin::blinkload$onBeforeBoostrapFinishInitialization);
		blinkload$bootstrapInitializationCompletableFuture = CompletableFuture.runAsync(
				Bootstrap::initialize, ThreadUtil.getBootstrapInitializerThreadPoolExecutor());
	}

	@Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;initCrashReport()V"))
	private static void blinkload$optimiseCrashReportInitialization() {
		CrashMemoryReserve.reserveMemory();
	}

	@Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/Bootstrap;initialize()V"))
	private static void blinkload$cancelBootstrapInitializationOnTheMainThread() {
		// NO-OP
	}

	@Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/Bootstrap;logMissing()V"))
	private static void blinkload$cancelBootstrapLogMissing() {
		// NO-OP
	}

	@Unique
	private static void blinkload$onBeforeBoostrapFinishInitialization() {
		if (blinkload$bootstrapInitializationCompletableFuture == null) {
			return;
		}

		var startTime = System.nanoTime();
		blinkload$bootstrapInitializationCompletableFuture.join();
		blinkload$bootstrapInitializationCompletableFuture = null;
		Blocks.refreshShapeCache();
		ItemGroups.collect();
		Bootstrap.logMissing();
		BlinkLoad.LOGGER.info(
				"Waited for bootstrap initialization to finish (took {}ms)",
				TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
		);
	}
}
