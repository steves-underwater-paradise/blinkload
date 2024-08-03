package io.github.steveplays28.blinkload.client.cache;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.AtlasTextureUtils;
import io.github.steveplays28.blinkload.util.resource.json.JsonUtil;
import io.github.steveplays28.blinkload.util.resource.json.StitchResult;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class BlinkLoadCache {
	private static final @NotNull File CACHED_DATA_FILE = new File(
			String.format("%s/atlas_textures_cache.json", AtlasTextureUtils.getCachePath()));
	private static final @NotNull Set<StitchResult> CACHED_DATA = new ConcurrentSkipListSet<>();

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

	public static @NotNull Set<StitchResult> getCachedData() {
		// Read JSON from a file
		try (@NotNull Reader reader = new FileReader(CACHED_DATA_FILE)) {
			// Convert the JSON data to a Java object
			var stitchResult = JsonUtil.getGson().fromJson(reader, StitchResult[].class);
			CACHED_DATA.addAll(Arrays.asList(stitchResult));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return CACHED_DATA;
	}

	public static void cacheData(@NotNull StitchResult stitchResult) {
		CACHED_DATA.add(stitchResult);
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
			file.write(JsonUtil.getGson().toJson(CACHED_DATA.toArray(StitchResult[]::new)));
			file.close();
		} catch (IOException e) {
			BlinkLoad.LOGGER.error("Exception thrown while writing cache data to file ({}): {}", e, CACHED_DATA_FILE);
		}
	}
}
