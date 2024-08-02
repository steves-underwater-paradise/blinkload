package io.github.steveplays28.blinkload.util.forge;

import io.github.steveplays28.blinkload.util.ModUtil;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Implements {@link ModUtil}.
 */
@SuppressWarnings("unused")
public class ModUtilImpl {
	/**
	 * Checks if a mod is present during loading.
	 */
	public static boolean isModPresent(String id) {
		return LoadingModList.get().getModFileById(id) != null;
	}

	/**
	 * Gets the game directory.
	 */
	public static @NotNull Path getGameDirectory() {
		return FMLPaths.GAMEDIR.get();
	}
}
