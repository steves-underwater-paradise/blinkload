package io.github.steveplays28.blinkload.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "unused"})
public class StitchResult {
    private @Nullable UUID uuid;
    private int width;
    private int height;
    private int mipLevel;
    private @Nullable AtlasTextureRegion[] atlasTextureRegions;

    public StitchResult(int width, int height, int mipLevel, @Nullable AtlasTextureRegion[] atlasTextureRegions) {
        this.width = width;
        this.height = height;
        this.mipLevel = mipLevel;
        this.atlasTextureRegions = atlasTextureRegions;

        this.uuid = UUID.randomUUID();
    }

    public @Nullable UUID getUUID() {
        return uuid;
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

    public static class AtlasTextureRegion {
        private @Nullable Identifier atlasTextureRegionId;
        private @Nullable Identifier spriteId;
        private int width;
        private int height;
        private int x;
        private int y;

        public AtlasTextureRegion(@Nullable Identifier atlasTextureId, @Nullable Identifier spriteId, int width, int height, int x, int y) {
            this.atlasTextureRegionId = atlasTextureId;
            this.spriteId = spriteId;
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
        }

        public @Nullable Identifier getAtlasTextureRegionId() {
            return atlasTextureRegionId;
        }

        public @Nullable Identifier getSpriteId() {
            return spriteId;
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
    }
}
