package io.github.steveplays28.blinkload.mixin.client.texture;

import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import io.github.steveplays28.blinkload.mixin.client.accessor.SpriteContentsAccessor;
import io.github.steveplays28.blinkload.util.resource.json.AtlasTextureIdentifier;
import io.github.steveplays28.blinkload.util.resource.json.StitchResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
@Mixin(SpriteLoader.class)
public class SpriteLoaderMixin {
	/**
	 * Loads the atlas textures cache, if it exists and is up-to-date.
	 */
	@SuppressWarnings("ForLoopReplaceableByForEach")
	@Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;ILjava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "HEAD"), cancellable = true)
	private void blinkload$loadAtlasTexturesFromCache(@NotNull ResourceManager resourceManager, @NotNull Identifier atlasTextureId, int mipLevel, @NotNull Executor executor, @NotNull CallbackInfoReturnable<CompletableFuture<SpriteLoader.StitchResult>> cir) {
		if (!BlinkLoadCache.isUpToDate()) {
			return;
		}

		@NotNull Map<Identifier, Sprite> atlasTextureRegions = new HashMap<>();
		@Nullable var stitchResult = BlinkLoadCache.getCachedData().get(new AtlasTextureIdentifier(atlasTextureId, mipLevel));
		if (stitchResult == null) {
			return;
		}

		cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
			var stitchResultAtlasTextureRegions = stitchResult.getAtlasTextureRegions();
			for (int stitchResultAtlasTextureRegionIndex = 0; stitchResultAtlasTextureRegionIndex < stitchResultAtlasTextureRegions.length; stitchResultAtlasTextureRegionIndex++) {
				@NotNull var atlasTextureRegion = stitchResultAtlasTextureRegions[stitchResultAtlasTextureRegionIndex];
				@Nullable var atlasTextureRegionId = atlasTextureRegion.getAtlasTextureRegionId();
				if (atlasTextureRegionId == null) {
					continue;
				}

				@Nullable var sprite = atlasTextureRegion.getSprite();
				if (sprite == null) {
					continue;
				}

				@Nullable var spriteId = sprite.getIdentifier();
				@Nullable var spriteMipmapLevel0Image = sprite.getMipmapLevel0Image();
				@Nullable var spriteMipmapLevelsImages = sprite.getMipmapLevelsImages();
				if (spriteId == null || spriteMipmapLevel0Image == null || spriteMipmapLevelsImages == null) {
					continue;
				}

				@NotNull var spriteContents = new SpriteContents(spriteId,
						new SpriteDimensions(atlasTextureRegion.getWidth(), atlasTextureRegion.getHeight()), spriteMipmapLevel0Image,
						AnimationResourceMetadata.EMPTY
				);
				((SpriteContentsAccessor) spriteContents).setMipmapLevelsImages(spriteMipmapLevelsImages);
				atlasTextureRegions.put(spriteId, new Sprite(
						atlasTextureRegionId, spriteContents, stitchResult.getWidth(), stitchResult.getHeight(), atlasTextureRegion.getX(),
						atlasTextureRegion.getY()
				));
			}

			return new SpriteLoader.StitchResult(stitchResult.getWidth(), stitchResult.getHeight(), stitchResult.getMipLevel(),
					atlasTextureRegions.get(MissingSprite.getMissingSpriteId()), atlasTextureRegions,
					CompletableFuture.completedFuture(null)
			);
		}, executor));
	}

	/**
	 * Saves the atlas textures to the cache.
	 */
	@Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;ILjava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "RETURN"))
	private void blinkload$saveAtlasTextures(@NotNull ResourceManager resourceManager, @NotNull Identifier atlasTextureId, int mipLevel, @NotNull Executor executor, @NotNull CallbackInfoReturnable<CompletableFuture<SpriteLoader.StitchResult>> cir) {
		cir.getReturnValue().thenAccept(incompleteStitchResult -> incompleteStitchResult.whenComplete().thenAccept(stitchResult -> {
			if (BlinkLoadCache.isUpToDate()) {
				return;
			}

			var stitchResultAtlasTextureRegions = stitchResult.regions();
			@NotNull List<StitchResult.AtlasTextureRegion> atlasTextureRegions = new ArrayList<>();
			for (@Nullable var stitchResultAtlasTextureRegion : stitchResultAtlasTextureRegions.entrySet()) {
				if (stitchResultAtlasTextureRegion == null) {
					continue;
				}

				// Category/region of the sprite
				var atlasIdentifier = stitchResultAtlasTextureRegion.getValue().getAtlasId();
				// Actual sprite identifier
				var spriteIdentifier = stitchResultAtlasTextureRegion.getKey();
				var spriteContents = ((SpriteContentsAccessor) stitchResultAtlasTextureRegion.getValue().getContents());
				var x = stitchResultAtlasTextureRegion.getValue().getX();
				var y = stitchResultAtlasTextureRegion.getValue().getY();
				var width = stitchResultAtlasTextureRegion.getValue().getContents().getWidth();
				var height = stitchResultAtlasTextureRegion.getValue().getContents().getHeight();
				atlasTextureRegions.add(new StitchResult.AtlasTextureRegion(atlasIdentifier,
						new StitchResult.AtlasTextureRegion.Sprite(
								spriteIdentifier, spriteContents.getImage(),
								spriteContents.getMipmapLevelsImages()
						), width, height, x, y
				));
			}

			BlinkLoadCache.cacheData(
					new StitchResult(atlasTextureId, stitchResult.width(), stitchResult.height(), stitchResult.mipLevel(),
							atlasTextureRegions.toArray(StitchResult.AtlasTextureRegion[]::new)
					));
		}));
	}
}
