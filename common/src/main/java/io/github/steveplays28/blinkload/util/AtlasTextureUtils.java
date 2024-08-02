package io.github.steveplays28.blinkload.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static io.github.steveplays28.blinkload.BlinkLoad.MOD_ID;

public class AtlasTextureUtils {
	private static final @NotNull String ATLAS_TEXTURES_FOLDER_NAME = "atlas_textures";

	public static @NotNull Path getAtlasTexturesPath() {
		return ModUtil.getGameDirectory().resolve(String.format(".%s/%s/", MOD_ID, ATLAS_TEXTURES_FOLDER_NAME));
	}
}
