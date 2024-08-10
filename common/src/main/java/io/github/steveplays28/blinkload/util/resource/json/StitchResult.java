package io.github.steveplays28.blinkload.util.resource.json;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "unused"})
public class StitchResult implements Comparable<StitchResult> {
	private @Nullable Identifier atlasTextureId;
	private int width;
	private int height;
	private int mipLevel;
	private @Nullable AtlasTextureRegion[] atlasTextureRegions;

	public StitchResult(@Nullable Identifier atlasTextureId, int width, int height, int mipLevel, @Nullable AtlasTextureRegion[] atlasTextureRegions) {
		this.atlasTextureId = atlasTextureId;
		this.width = width;
		this.height = height;
		this.mipLevel = mipLevel;
		this.atlasTextureRegions = atlasTextureRegions;
	}

	public @Nullable Identifier getAtlasTextureId() {
		return atlasTextureId;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getMipLevel() {
		return mipLevel;
	}

	public @Nullable AtlasTextureRegion[] getAtlasTextureRegions() {
		return atlasTextureRegions;
	}

	@Override
	public int compareTo(@NotNull StitchResult stitchResult) {
		return Integer.compare(getAtlasTextureRegions().length, stitchResult.getAtlasTextureRegions().length);
	}

	public static class AtlasTextureRegion {
		private @Nullable Identifier atlasTextureRegionId;
		private @Nullable Sprite sprite;
		private int width;
		private int height;
		private int x;
		private int y;

		public AtlasTextureRegion(@Nullable Identifier atlasTextureId, @Nullable Sprite sprite, int width, int height, int x, int y) {
			this.atlasTextureRegionId = atlasTextureId;
			this.sprite = sprite;
			this.width = width;
			this.height = height;
			this.x = x;
			this.y = y;
		}

		public @Nullable Identifier getAtlasTextureRegionId() {
			return atlasTextureRegionId;
		}

		public @Nullable Sprite getSprite() {
			return sprite;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public static class Sprite {
			private @Nullable Identifier identifier;
			private @Nullable NativeImage nativeImage;

			public Sprite(@Nullable Identifier identifier, @Nullable NativeImage nativeImage) {
				this.identifier = identifier;
				this.nativeImage = nativeImage;
			}

			public @Nullable Identifier getIdentifier() {
				return identifier;
			}

			public @Nullable NativeImage getNativeImage() {
				return nativeImage;
			}
		}
	}
}
