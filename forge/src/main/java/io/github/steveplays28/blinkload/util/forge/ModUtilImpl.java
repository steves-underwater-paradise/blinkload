package io.github.steveplays28.blinkload.util.forge;

import io.github.steveplays28.blinkload.util.ModUtil;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
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
		return LoadingModList.get().getModFileById(id) != null;
	}

	/**
	 * Gets the game directory.
	 */
	public static @NotNull Path getGameDirectory() {
		return FMLPaths.GAMEDIR.get();
	}

	public static @NotNull List<String> getModListNames() {
		@NotNull List<String> modListNames = new ArrayList<>();
		for (@NotNull var mod : FMLLoader.getLoadingModList().getMods()) {
			modListNames.add(mod.toString());
		}
		return modListNames;
	}
}
