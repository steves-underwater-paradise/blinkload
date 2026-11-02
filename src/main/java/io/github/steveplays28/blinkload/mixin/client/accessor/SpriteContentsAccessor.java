package io.github.steveplays28.blinkload.mixin.client.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(SpriteContents.class)
public interface SpriteContentsAccessor {
	@Accessor
	NativeImage getImage();

	@Accessor
	NativeImage[] getMipmapLevelsImages();

	@Accessor
	void setMipmapLevelsImages(NativeImage[] mipmapLevelsImages);
}
