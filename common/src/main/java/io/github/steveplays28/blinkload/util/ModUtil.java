package io.github.steveplays28.blinkload.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("unused")
public abstract class ModUtil {
	/**
	 * Checks if a mod is present during loading.
	 */
	@ExpectPlatform
	public static boolean isModPresent(String id) {
		throw new AssertionError("Platform implementation expected.");
	}

	/**
	 * Gets the game directory.
	 */
	@ExpectPlatform
	public static @NotNull Path getGameDirectory() {
		throw new AssertionError("Platform implementation expected.");
	}

	@ExpectPlatform
	public static @NotNull List<String> getModListNames() {
		throw new AssertionError("Platform implementation expected.");
	}
}
