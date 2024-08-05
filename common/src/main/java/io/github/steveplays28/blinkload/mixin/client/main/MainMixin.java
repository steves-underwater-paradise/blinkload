package io.github.steveplays28.blinkload.mixin.client.main;

import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Main.class)
public class MainMixin {
	@Inject(method = "main", at = @At(value = "HEAD"))
	private static void blinkload$loadCachedDataAsync(String[] args, CallbackInfo ci) {
		BlinkLoadCache.initialize();
		ClientLifecycleEvent.CLIENT_MAIN_STARTING.invoker().onClientMainStarting();
	}
}
