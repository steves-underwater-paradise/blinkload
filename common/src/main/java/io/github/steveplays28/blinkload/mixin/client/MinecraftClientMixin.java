package io.github.steveplays28.blinkload.mixin.client;

import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "onInitFinished", at = @At(value = "HEAD"))
	private void blinkload$invokeAfterResourceReloadEvent(RealmsClient realms, @NotNull ResourceReload reload, RunArgs.QuickPlay quickPlay, @NotNull CallbackInfo ci) {
		reload.whenComplete().thenAccept(
				o -> ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_FINISHED.invoker().onClientResourceReloadFinished());
	}
}
