package io.github.steveplays28.blinkload.client;

import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BlinkLoadClient {
	public static void initialize() {
		BlinkLoadCache.initialize();
	}
}
