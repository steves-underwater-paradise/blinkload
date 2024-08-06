package io.github.steveplays28.blinkload.mixin;

import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public interface RegistryMixin {
	@Inject(method = "getOrThrow", at = @At(value = "HEAD"))
	private void blinkload$waitForBootstrapInitializationToFinish(RegistryKey<?> key, @NotNull CallbackInfoReturnable<?> cir) {
		ClientLifecycleEvent.BEFORE_BOOTSTRAP_FINISH_INITIALIZATION.invoker().onBeforeBootstrapFinishInitialization();
	}
}
