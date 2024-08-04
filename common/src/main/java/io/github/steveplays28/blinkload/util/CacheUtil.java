package io.github.steveplays28.blinkload.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static io.github.steveplays28.blinkload.BlinkLoad.MOD_ID;

public class CacheUtil {
	public static @NotNull Path getCachePath() {
		return ModUtil.getGameDirectory().resolve(String.format(".%s/", MOD_ID));
	}
}
