package io.github.steveplays28.blinkload.mixin.client;

import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;reload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReload;"))
	private void blinkload$invokeBeforeResourceReloadEvent(boolean force, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_STARTING.invoker().onClientResourceReloadStarting();

	}

	@Inject(method = "reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "RETURN"))
	private void blinkload$invokeAfterResourceReloadEvent(boolean force, @NotNull CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		cir.getReturnValue().whenComplete(
				(unused, throwable) -> ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_FINISHED.invoker().onClientResourceReloadFinished());
	}
}
