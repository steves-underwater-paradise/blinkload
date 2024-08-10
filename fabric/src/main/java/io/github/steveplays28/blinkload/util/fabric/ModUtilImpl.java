package io.github.steveplays28.blinkload.util.fabric;

import io.github.steveplays28.blinkload.util.ModUtil;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements {@link ModUtil}.
 */
@SuppressWarnings("unused")
public class ModUtilImpl {
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
}
