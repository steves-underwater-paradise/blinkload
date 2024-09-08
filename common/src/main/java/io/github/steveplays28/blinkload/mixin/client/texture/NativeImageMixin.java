package io.github.steveplays28.blinkload.mixin.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.channels.WritableByteChannel;

@Environment(EnvType.CLIENT)
@Mixin(NativeImage.class)
public abstract class NativeImageMixin {
	@Shadow
	private long pointer;

	@Inject(method = "write", at = @At(value = "HEAD"), cancellable = true)
	private void blinkload$checkIfPointerIsAllocated(WritableByteChannel channel, @NotNull CallbackInfoReturnable<Boolean> cir) {
		if (this.pointer == 0L) {
			cir.setReturnValue(true);
		}
	}
}
