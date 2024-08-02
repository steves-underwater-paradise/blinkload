package io.github.steveplays28.blinkload.fabric.client;

import io.github.steveplays28.blinkload.client.BlinkLoadClient;
import net.fabricmc.api.ClientModInitializer;

public class BlinkLoadClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlinkLoadClient.initialize();
	}
}
