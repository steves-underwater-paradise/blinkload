package io.github.steveplays28.blinkload.mixin.client;

import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import io.github.steveplays28.blinkload.mixin.client.accessor.SpriteContentsAccessor;
import io.github.steveplays28.blinkload.util.StitchResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
@Mixin(SpriteLoader.class)
public class SpriteLoaderMixin {
	/**
	 * Loads the atlas textures cache, if it exists and is up-to-date.
	 */
	@Inject(method = "stitch", at = @At(value = "HEAD"), cancellable = true)
	private void blinkload$loadAtlasTexturesCache(List<SpriteContents> sprites, int mipLevel, Executor executor, @NotNull CallbackInfoReturnable<SpriteLoader.StitchResult> cir) {
		if (!BlinkLoadCache.isUpToDate()) {
			return;
		}

		@NotNull Map<Identifier, Sprite> atlasTextureRegions = new HashMap<>();
		// TODO: Get cached data index based on the worker thread index
		var stitchResult = BlinkLoadCache.getCachedData().keySet().iterator().next();
		int atlasTextureRegionIndex = 0;
		for (var cachedData : BlinkLoadCache.getCachedData().entrySet()) {
			var atlasTextureRegion = cachedData.getKey().getAtlasTextureRegions()[atlasTextureRegionIndex];
			var atlasTextureRegionId = atlasTextureRegion.getAtlasTextureRegionId();
			var nativeImage = cachedData.getValue().get(atlasTextureRegionIndex);
			atlasTextureRegions.put(atlasTextureRegionId, new Sprite(atlasTextureRegionId, new SpriteContents(atlasTextureRegion.getSpriteId(), new SpriteDimensions(atlasTextureRegion.getWidth(), atlasTextureRegion.getHeight()), nativeImage, AnimationResourceMetadata.EMPTY), stitchResult.getWidth(), stitchResult.getHeight(), atlasTextureRegion.getX(), atlasTextureRegion.getY()));
			atlasTextureRegionIndex++;
		}

		cir.setReturnValue(new SpriteLoader.StitchResult(stitchResult.getWidth(), stitchResult.getHeight(), stitchResult.getMipLevel(), atlasTextureRegions.get(MissingSprite.getMissingSpriteId()), atlasTextureRegions, CompletableFuture.completedFuture(null)));
	}

	/**
	 * Saves the atlas textures to the cache.
	 */
	@Inject(method = "stitch", at = @At(value = "RETURN"))
	private void blinkload$saveAtlasTextures(List<SpriteContents> sprites, int mipLevel, Executor executor, @NotNull CallbackInfoReturnable<SpriteLoader.StitchResult> cir) {
		@NotNull var stitchResult = cir.getReturnValue();
		var stitchResultAtlasTextureRegions = stitchResult.regions();
		@NotNull List<StitchResult.AtlasTextureRegion> atlasTextureRegions = new ArrayList<>();
		@NotNull List<NativeImage> atlasTextureRegionNativeImages = new ArrayList<>();
        for (@Nullable var stitchResultAtlasTextureRegion : stitchResultAtlasTextureRegions.entrySet()) {
			if (stitchResultAtlasTextureRegion == null) {
				continue;
			}

            // Category/region of the sprite
            var atlasIdentifier = stitchResultAtlasTextureRegion.getValue().getAtlasId();
            // Actual sprite identifier
            var spriteIdentifier = stitchResultAtlasTextureRegion.getKey();
            var x = stitchResultAtlasTextureRegion.getValue().getX();
            var y = stitchResultAtlasTextureRegion.getValue().getY();
            var width = stitchResultAtlasTextureRegion.getValue().getContents().getWidth();
            var height = stitchResultAtlasTextureRegion.getValue().getContents().getHeight();
			atlasTextureRegions.add(new StitchResult.AtlasTextureRegion(atlasIdentifier, spriteIdentifier, width, height, x, y));
			atlasTextureRegionNativeImages.add(((SpriteContentsAccessor) stitchResultAtlasTextureRegion.getValue().getContents()).getImage());
        }

		BlinkLoadCache.cacheData(new StitchResult(stitchResult.width(), stitchResult.height(), stitchResult.mipLevel(), atlasTextureRegions.toArray(StitchResult.AtlasTextureRegion[]::new)), atlasTextureRegionNativeImages);
	}
}
