package io.github.steveplays28.blinkload.mixin.client.texture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(NativeImage.class)
public abstract class NativeImageMixin {
	@WrapOperation(method = "write", at = @At(value = "INVOKE", target = "Lorg/lwjgl/stb/STBImageWrite;nstbi_write_png_to_func(JJIIIJI)I"))
	private int blinkload$writePngToFuncSafely(long func, long context, int w, int h, int comp, long data, int stride_in_bytes, Operation<Integer> original) {
		if (data == 0L) {
			return 0;
		}

		return original.call(func, context, w, h, comp, data, stride_in_bytes);
	}
}
