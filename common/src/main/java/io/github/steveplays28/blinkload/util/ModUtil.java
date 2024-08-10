package io.github.steveplays28.blinkload.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

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

	public static @NotNull Path getConfigDirectory() {
		return getGameDirectory().resolve("config");
	}
}
