package io.github.steveplays28.blinkload.util;

import net.fabricmc.loader.api.FabricLoader;
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
	public static boolean isModPresent(String id) {
		return FabricLoader.getInstance().isModLoaded(id);
	}

	/**
	 * Gets the game directory.
	 */
	public static @NotNull Path getGameDirectory() {
		return FabricLoader.getInstance().getGameDir().toAbsolutePath();
	}

	public static @NotNull List<String> getModListNames() {
		@NotNull List<String> modListNames = new ArrayList<>();
		for (@NotNull var mod : new ArrayList<>(FabricLoader.getInstance().getAllMods())) {
			modListNames.add(mod.toString());
		}
		return modListNames;
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
