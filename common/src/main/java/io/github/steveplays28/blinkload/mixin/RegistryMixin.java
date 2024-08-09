package io.github.steveplays28.blinkload.mixin;

import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.ThreadUtil;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public interface RegistryMixin {
	@Inject(method = "getOrThrow", at = @At(value = "HEAD"))
	private void blinkload$waitForBootstrapInitializationToFinishBeforeGetting(CallbackInfoReturnable<?> cir) {
		if (Thread.currentThread().getName().contains(ThreadUtil.BOOTSTRAPPER_THREAD_POOL_NAME)) {
			return;
		}

		ClientLifecycleEvent.BEFORE_BOOTSTRAP_FINISH_INITIALIZATION.invoker().onBeforeBootstrapFinishInitialization();
	}

	@Inject(method = {"register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;", "register(Lnet/minecraft/registry/Registry;ILjava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", "registerReference(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;"}, at = @At(value = "HEAD"))
	private static void blinkload$waitForBootstrapInitializationToFinishBeforeRegistering(CallbackInfoReturnable<?> cir) {
		if (Thread.currentThread().getName().contains(ThreadUtil.BOOTSTRAPPER_THREAD_POOL_NAME)) {
			return;
		}

		ClientLifecycleEvent.BEFORE_BOOTSTRAP_FINISH_INITIALIZATION.invoker().onBeforeBootstrapFinishInitialization();
	}
}
