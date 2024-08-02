package io.github.steveplays28.blinkload.mixin.client;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import io.github.steveplays28.blinkload.mixin.client.accessor.SpriteContentsAccessor;
import io.github.steveplays28.blinkload.util.AtlasTextureUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
@Mixin(SpriteLoader.class)
public class SpriteLoaderMixin {
	@Shadow
	@Final
	private int width;
	@Shadow
	@Final
	private int height;

//	/**
//	 * Loads the atlas textures cache, if it exists and is up-to-date.
//	 */
//	@Inject(method = "stitch", at = @At(value = "HEAD"), cancellable = true)
//	private void blinkload$loadAtlasTexturesCache(List<SpriteContents> sprites, int mipLevel, Executor executor, @NotNull CallbackInfoReturnable<SpriteLoader.StitchResult> cir) {
//		if (!BlinkLoadCache.isUpToDate()) {
//			return;
//		}
//
//		@Nullable File[] listOfFiles = AtlasTextureUtils.getAtlasTexturesPath().toFile().listFiles();
//		if (listOfFiles == null || listOfFiles.length == 0) {
//			return;
//		}
//
//		Map<Identifier, Sprite> atlasTextures = new HashMap<>();
//		for (@Nullable var file : listOfFiles) {
//			if (file == null) {
//				continue;
//			}
//
//			try {
//				if (!file.isFile()) {
//					continue;
//				}
//
//				var image = NativeImage.read(new FileInputStream(file));
//				// TODO: Save and load the atlas texture Identifier
//				var spriteContents = new SpriteContents(new Identifier(file.getName()), new SpriteDimensions(image.getWidth(), image.getHeight()), image, AnimationResourceMetadata.EMPTY);
//
//				// TODO: Save and load from namespace folders
//				atlasTextures.put(new Identifier(file.getName()), new Sprite(new Identifier(file.getName()), spriteContents, ));
//			} catch (IOException e) {
//				// TODO
//			}
//		}
//
//		int width = Math.max(textureStitcher.getWidth(), this.width);
//		int height = Math.max(textureStitcher.getHeight(), this.height);
//		cir.setReturnValue(new SpriteLoader.StitchResult(width, height, ));
//	}

	/**
	 * Saves the atlas textures to the cache.
	 */
	@Inject(method = "stitch", at = @At(value = "RETURN"))
	private void blinkload$saveAtlasTextures(List<SpriteContents> sprites, int mipLevel, Executor executor, @NotNull CallbackInfoReturnable<SpriteLoader.StitchResult> cir) {
		@NotNull var atlasTexturesPath = AtlasTextureUtils.getAtlasTexturesPath();
		if (!atlasTexturesPath.toFile().mkdirs()) {
			BlinkLoad.LOGGER.error("Error occurred while creating directories for path: {}", atlasTexturesPath);
		}

		@NotNull var atlasTextures = cir.getReturnValue().regions();
		for (@Nullable var atlasTexture : atlasTextures.entrySet()) {
			if (atlasTexture == null) {
				continue;
			}

			try {
				// TODO: Save X
				// TODO: Save Y
				// TODO: Save atlas texture Identifier
				// TODO: Save atlas texture width
				// TODO: Save atlas texture height
				((SpriteContentsAccessor) atlasTexture.getValue().getContents()).getImage().writeTo(
						atlasTexturesPath.resolve(String.format("%s.png", atlasTexture.getKey().toUnderscoreSeparatedString())));
			} catch (IOException e) {
				BlinkLoad.LOGGER.error("Exception thrown while saving atlas textures: ", e);
			}
		}
	}
}
