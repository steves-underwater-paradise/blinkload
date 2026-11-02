package io.github.steveplays28.blinkload.mixin.client.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

@Environment(EnvType.CLIENT)
@Mixin(NativeImage.class)
public interface NativeImageInvoker {
	@Invoker("write")
	boolean invokeWrite(WritableByteChannel channel) throws IOException;
}
