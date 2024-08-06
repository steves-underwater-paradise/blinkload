package io.github.steveplays28.blinkload.mixin;

import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.ThreadUtil;
import net.minecraft.Bootstrap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class BootstrapMixin {
	@Unique
	private static boolean blinkload$hasFinished = false;

	@Inject(method = "ensureBootstrapped", at = @At(value = "HEAD"))
	private static void blinkload$finishBootstrappingIfRequired(@NotNull CallbackInfo ci) {
		if (blinkload$hasFinished || Thread.currentThread().getName().contains(ThreadUtil.BOOTSTRAPPER_THREAD_POOL_NAME)) {
			return;
		}

		ClientLifecycleEvent.BEFORE_BOOTSTRAP_FINISH_INITIALIZATION.invoker().onBeforeBootstrapFinishInitialization();
		blinkload$hasFinished = true;
	}

	@Redirect(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroups;collect()V"))
	private static void blinkload$cancelItemGroupsCollect() {
		// NO-OP
	}
}
