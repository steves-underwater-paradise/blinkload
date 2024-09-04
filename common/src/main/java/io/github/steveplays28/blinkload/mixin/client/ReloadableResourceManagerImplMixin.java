package io.github.steveplays28.blinkload.mixin.client;

import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
@Mixin(ReloadableResourceManagerImpl.class)
public class ReloadableResourceManagerImplMixin {
	@Inject(method = "reload", at = @At(value = "HEAD"))
	private void blinkload$invokeBeforeResourceReloadEvent(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReload> cir) {
		ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_STARTING.invoker().onClientResourceReloadStarting();
	}

	@Inject(method = "reload", at = @At(value = "RETURN"))
	private void blinkload$invokeAfterResourceReloadEvent(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, @NotNull CallbackInfoReturnable<ResourceReload> cir) {
		cir.getReturnValue().whenComplete().whenComplete(
				(unused, throwable) -> ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_FINISHED.invoker().onClientResourceReloadFinished());
	}
}
