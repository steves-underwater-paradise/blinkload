package io.github.steveplays28.blinkload.client.launch.pre;

import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;

public class BlinkLoadClientPreLaunch {
	/**
	 * Runs the entrypoint. Ran before the game has bootstrapped.
	 */
	public static void onPreLaunch() {
		BlinkLoadCache.loadCachedDataAsync();
	}
}
