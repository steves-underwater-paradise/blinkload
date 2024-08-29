package io.github.steveplays28.blinkload.mixin.client.main;

import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.Main;
import net.minecraft.util.crash.CrashMemoryReserve;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Main.class)
public class ClientMainMixin {
	@Inject(method = "main", at = @At(value = "HEAD"))
	private static void blinkload$loadCachedDataAsyncAndInitializeBootstrapOffThread(String[] args, CallbackInfo ci) {
		BlinkLoadCache.initialize();
		ClientLifecycleEvent.CLIENT_MAIN_STARTING.invoker().onClientMainStarting();
	}

	@Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;initCrashReport()V"))
	private static void blinkload$optimiseCrashReportInitialization() {
		CrashMemoryReserve.reserveMemory();
	}
}
