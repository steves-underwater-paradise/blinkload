package io.github.steveplays28.blinkload.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static io.github.steveplays28.blinkload.BlinkLoad.MOD_ID;

@SuppressWarnings("unused")
public abstract class ModUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(String.format("%s/mod_util", MOD_ID));

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

	public static @NotNull List<String> getEnabledResourcePackNames() {
		@NotNull List<String> enabledResourcePackNames = new ArrayList<>();
		try (@NotNull var bufferedFileReader = new BufferedReader(new FileReader(getGameDirectory().resolve("options.txt").toFile()))) {
			bufferedFileReader.lines().forEach(line -> {
				if (!line.contains("resourcePacks")) {
					return;
				}

				@NotNull var rawEnabledResourcePackNamesOption = line.split("\\[");
				if (rawEnabledResourcePackNamesOption.length < 2) {
					return;
				}

				@NotNull var rawEnabledResourcePackNames = rawEnabledResourcePackNamesOption[1].replace("]", "");
				enabledResourcePackNames.addAll(List.of(rawEnabledResourcePackNames.split(",")));
			});
		} catch (IOException e) {
			LOGGER.error("Exception occurred while getting enabled resource pack names: ", e);
		}

		return enabledResourcePackNames;
	}
}
