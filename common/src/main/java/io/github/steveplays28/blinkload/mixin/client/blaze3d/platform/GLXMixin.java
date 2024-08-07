package io.github.steveplays28.blinkload.mixin.client.blaze3d.platform;

import com.mojang.blaze3d.platform.GLX;
import io.github.steveplays28.blinkload.util.ThreadUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.GlDebug;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
@Mixin(GLX.class)
public class GLXMixin {
	@Shadow
	private static String cpuInfo;

	@Inject(method = "_init", at = @At(value = "NEW", target = "()Loshi/SystemInfo;"), cancellable = true)
	private static void blinkload$optimizeSystemInfoFetch(int debugVerbosity, boolean debugSync, @NotNull CallbackInfo ci) {
		CompletableFuture.supplyAsync(() -> {
			CentralProcessor centralProcessor = new SystemInfo().getHardware().getProcessor();
			return String.format(
					Locale.ROOT, "%dx %s", centralProcessor.getLogicalProcessorCount(),
					centralProcessor.getProcessorIdentifier().getName()
			).replaceAll("\\s+", " ");
		}, ThreadUtil.getGLXInitializerThreadPoolExecutor()).whenComplete((string, throwable) -> cpuInfo = string);

		if (debugVerbosity > 0) {
			GlDebug.enableDebug(debugVerbosity, debugSync);
		}

		ci.cancel();
	}
}
