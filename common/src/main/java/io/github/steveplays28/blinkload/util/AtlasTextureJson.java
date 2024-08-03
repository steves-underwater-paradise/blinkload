package io.github.steveplays28.blinkload.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AtlasTextureJson {
    private @Nullable Identifier atlasTextureId;
    private @Nullable Identifier spriteTextureId;
    private int width;
    private int height;
    private int x;
    private int y;

    public AtlasTextureJson(@Nullable Identifier atlasTextureId, @Nullable Identifier spriteTextureId, int width, int height, int x, int y) {
        this.atlasTextureId = atlasTextureId;
        this.spriteTextureId = spriteTextureId;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
}
