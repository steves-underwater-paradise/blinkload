package io.github.steveplays28.blinkload.mixin.client.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(Sprite.class)
public interface SpriteInvoker {
	@Invoker("<init>")
	static Sprite create(Identifier atlasId, SpriteContents contents, int maxLevel, int width, int height, int x, int y) {
		throw new AssertionError();
	}
}
