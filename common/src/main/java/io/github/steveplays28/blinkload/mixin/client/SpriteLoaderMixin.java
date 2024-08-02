package io.github.steveplays28.blinkload.mixin.client;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.mixin.client.accessor.SpriteContentsAccessor;
import io.github.steveplays28.blinkload.util.ModUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import static io.github.steveplays28.blinkload.BlinkLoad.MOD_ID;
import static io.github.steveplays28.blinkload.util.AtlasTextureUtils.ATLAS_TEXTURES_FOLDER_NAME;

@Environment(EnvType.CLIENT)
@Mixin(SpriteLoader.class)
public class SpriteLoaderMixin {
	@Inject(method = "stitch", at = @At(value = "RETURN"))
	private void blinkload$saveAtlasTextures(List<SpriteContents> sprites, int mipLevel, Executor executor, @NotNull CallbackInfoReturnable<SpriteLoader.StitchResult> cir) {
		@NotNull var atlasTexturesPath = ModUtil.getGameDirectory().resolve(String.format(".%s/%s/", MOD_ID, ATLAS_TEXTURES_FOLDER_NAME));
		if (!atlasTexturesPath.toFile().mkdirs()) {
			BlinkLoad.LOGGER.error("Error occurred while creating directories for path: {}", atlasTexturesPath);
		}

		@NotNull var atlasTextures = cir.getReturnValue().regions();
		for (@Nullable var atlasTexture : atlasTextures.entrySet()) {
			if (atlasTexture == null) {
				continue;
			}

			try {
				((SpriteContentsAccessor) atlasTexture.getValue().getContents()).getImage().writeTo(
						atlasTexturesPath.resolve(String.format("%s.png", atlasTexture.getKey().toUnderscoreSeparatedString())));
			} catch (IOException e) {
				BlinkLoad.LOGGER.error("Exception thrown while saving atlas textures: ", e);
			}
		}
	}
}
