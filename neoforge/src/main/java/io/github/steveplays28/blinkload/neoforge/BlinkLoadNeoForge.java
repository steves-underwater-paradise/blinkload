package io.github.steveplays28.blinkload.forge;

import io.github.steveplays28.blinkload.BlinkLoad;
import net.neoforged.fml.common.Mod;

@Mod(BlinkLoad.MOD_ID)
public class BlinkLoadNeoForge {
	public BlinkLoadNeoForge() {
		BlinkLoad.initialize();
	}
}
