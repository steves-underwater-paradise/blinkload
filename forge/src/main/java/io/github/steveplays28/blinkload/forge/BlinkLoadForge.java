package io.github.steveplays28.blinkload.forge;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.forge.client.BlinkLoadClientForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(BlinkLoad.MOD_ID)
public class BlinkLoadForge {
	public BlinkLoadForge() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			BlinkLoadClientForge.onInitializeClient();
		}

		BlinkLoad.initialize();
	}
}
