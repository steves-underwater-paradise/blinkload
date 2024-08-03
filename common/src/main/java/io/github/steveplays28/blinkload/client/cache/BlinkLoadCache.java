package io.github.steveplays28.blinkload.client.cache;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.AtlasTextureUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class BlinkLoadCache {
	private static final @NotNull File CACHED_DATA_FILE = new File(String.format("%s/atlas_textures_cache.json", AtlasTextureUtils.getCachePath()));
	private static final @NotNull Set<String> CACHED_DATA = new ConcurrentSkipListSet<>();

	private static boolean hasFirstResourceReloadFinished = false;

	public static void initialize() {
		ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_FINISHED.register(BlinkLoadCache::onFirstResourceReload);
	}

	public static boolean hasFirstResourceReloadFinished() {
		return hasFirstResourceReloadFinished;
	}

	public static boolean isUpToDate() {
		// TODO: Compare the mod list's hash
		return true;
	}

	public static void cacheData(@NotNull String data) {
		CACHED_DATA.add(data);
	}

	private static void onFirstResourceReload() {
		hasFirstResourceReloadFinished = true;
		BlinkLoad.LOGGER.info("Atlas creation finished. Caching data to JSON.");
		writeCacheDataToFile();
	}

	private static void writeCacheDataToFile() {
		try {
			// TODO: Add error handling
			CACHED_DATA_FILE.getParentFile().mkdirs();

			@NotNull var file = new FileWriter(CACHED_DATA_FILE);
			file.write(String.format("[%s]", String.join(",\n", CACHED_DATA)));
			file.close();
		} catch (IOException e) {
			BlinkLoad.LOGGER.error("Exception thrown while writing cache data to file ({}): {}", e, CACHED_DATA_FILE);
        }
    }
}
