package io.github.steveplays28.blinkload.mixin.client.main;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.ThreadUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Bootstrap;
import net.minecraft.block.Blocks;
import net.minecraft.client.main.Main;
import net.minecraft.client.util.telemetry.GameLoadTimeEvent;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.crash.CrashMemoryReserve;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
@Mixin(Main.class)
public class ClientMainMixin {
	@Unique
	private static @Nullable CompletableFuture<Void> blinkload$bootstrapInitializationCompletableFuture = null;

	@Inject(method = "main", at = @At(value = "HEAD"))
	private static void blinkload$loadCachedDataAsyncAndInitializeBootstrapOffThread(String[] args, CallbackInfo ci) {
		BlinkLoadCache.initialize();
		ClientLifecycleEvent.CLIENT_MAIN_STARTING.invoker().onClientMainStarting();

		ClientLifecycleEvent.BEFORE_BOOTSTRAP_FINISH_INITIALIZATION.register(
				ClientMainMixin::blinkload$onBeforeBoostrapFinishInitialization);
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

	@Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/telemetry/GameLoadTimeEvent;setBootstrapTime(J)V"))
	private static void blinkload$cancelSetBootstrapTime(@NotNull GameLoadTimeEvent instance, long bootstrapTime) {
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
		GameLoadTimeEvent.INSTANCE.setBootstrapTime(Bootstrap.LOAD_TIME.get());
		Bootstrap.logMissing();
		BlinkLoad.LOGGER.info(
				"Waited for bootstrapping to finish (took {}ms)",
				TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
		);
	}
}
