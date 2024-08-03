package io.github.steveplays28.blinkload.mixin.client;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import io.github.steveplays28.blinkload.util.AtlasTextureUtils;
import io.github.steveplays28.blinkload.util.AtlasTextureJson;
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

import java.util.ArrayList;
import java.util.List;
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

            // Category/region of the sprite
            var atlasIdentifier = atlasTexture.getValue().getAtlasId();
            // Actual sprite identifier
            var spriteIdentifier = atlasTexture.getKey();
            var x = atlasTexture.getValue().getX();
            var y = atlasTexture.getValue().getY();
            var width = atlasTexture.getValue().getContents().getWidth();
            var height = atlasTexture.getValue().getContents().getHeight();

			// {"atlas_texture_id":{"namespace":"minecraft","path":"textures/atlas/blocks.png"},"sprite_texture_id":{"namespace":"minecraft","path":"block/jukebox_top"},"width":16,"height":16,"x":448,"y":240}
            AtlasTextureJson sprite = new AtlasTextureJson(atlasIdentifier, spriteIdentifier, width, height, x, y);

			var gson = new Gson().newBuilder().setFieldNamingPolicy(
					FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
			String json = gson.toJson(sprite);
			BlinkLoadCache.cacheData(json);

			/*
            // atlas_texture_namespace/atlas_texture_path/texture_region_namespace/x_y_width_height_SEPARATOR_texture_region_path.png
            var textureFolderPath = atlasTexturesPath.resolve(String.format("%s/%s/%s/", atlasIdentifier.getNamespace(), atlasIdentifier.getPath(), spriteIdentifier.getNamespace()));
			textureFolderPath.toFile().mkdirs();

			var textureFilePath = textureFolderPath.resolve(String.format("%s_%s_%s_%s_SEPARATOR_%s.png", x, y, width, height, spriteIdentifier.getPath()));
			try {
				((SpriteContentsAccessor) atlasTexture.getValue().getContents()).getImage().writeTo(textureFilePath);
			} catch (IOException e) {
				BlinkLoad.LOGGER.error("Exception thrown while saving texture: ", e);
            }
			 */
        }
	}
}
