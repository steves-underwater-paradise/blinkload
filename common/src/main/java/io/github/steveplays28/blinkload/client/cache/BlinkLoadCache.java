package io.github.steveplays28.blinkload.client.cache;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

public class BlinkLoadCache {
	private static boolean hasFirstResourceReloadFinished = false;

	public static void initialize() {
		ClientLifecycleEvent.CLIENT_STARTED.register(BlinkLoadCache::onFirstResourceReload);
	}

	public static boolean isUpToDate() {
		// TODO: Compare the mod list's hash
		return true;
	}

	public static boolean hasFirstResourceReloadFinished() {
		return hasFirstResourceReloadFinished;
	}

	private static void onFirstResourceReload(@NotNull MinecraftClient client) {
		hasFirstResourceReloadFinished = true;
	}
}
