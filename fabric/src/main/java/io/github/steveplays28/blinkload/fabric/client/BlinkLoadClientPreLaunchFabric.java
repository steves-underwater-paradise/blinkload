package io.github.steveplays28.blinkload.fabric.client;

import io.github.steveplays28.blinkload.client.launch.pre.BlinkLoadClientPreLaunch;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class BlinkLoadClientPreLaunchFabric implements PreLaunchEntrypoint {
	/**
	 * Runs the entrypoint. Ran before the game has bootstrapped.
	 */
	@Override
	public void onPreLaunch() {
		BlinkLoadClientPreLaunch.onPreLaunch();
	}
}
