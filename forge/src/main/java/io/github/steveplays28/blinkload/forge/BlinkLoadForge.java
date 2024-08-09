package io.github.steveplays28.blinkload.forge;

import io.github.steveplays28.blinkload.BlinkLoad;
import net.minecraftforge.fml.common.Mod;

@Mod(BlinkLoad.MOD_ID)
public class BlinkLoadForge {
	public BlinkLoadForge() {
		BlinkLoad.initialize();
	}
}
