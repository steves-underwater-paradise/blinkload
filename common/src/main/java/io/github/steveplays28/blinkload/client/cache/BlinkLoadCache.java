package io.github.steveplays28.blinkload.client.cache;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.CacheUtil;
import io.github.steveplays28.blinkload.util.resource.json.JsonUtil;
import io.github.steveplays28.blinkload.util.resource.json.StitchResult;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BlinkLoadCache {
	private static final @NotNull File CACHED_DATA_FILE = new File(
			String.format("%s/atlas_textures_cache.json", CacheUtil.getCachePath()));

	private static @Nullable Map<Identifier, StitchResult> CACHED_DATA = null;

	private static boolean hasFirstResourceReloadFinished = false;

	public static void initialize() {
		ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_FINISHED.register(BlinkLoadCache::onFirstResourceReload);
	}

	public static boolean hasFirstResourceReloadFinished() {
		return hasFirstResourceReloadFinished;
	}

	public static boolean isUpToDate() {
		// TODO: Compare the mod list's hash
		return Files.exists(CACHED_DATA_FILE.toPath());
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	public static @NotNull Map<Identifier, StitchResult> getCachedData() {
		if (CACHED_DATA == null) {
			var startTime = System.nanoTime();

			CACHED_DATA = new ConcurrentHashMap<>();
			// Read JSON from the cached data file
			try (@NotNull Reader reader = new FileReader(CACHED_DATA_FILE)) {
				// Convert the JSON data to a Java object
				@NotNull var stitchResults = JsonUtil.getGson().fromJson(reader, StitchResult[].class);
				for (int stitchResultIndex = 0; stitchResultIndex < stitchResults.length; stitchResultIndex++) {
					@NotNull var stitchResult = stitchResults[stitchResultIndex];
					CACHED_DATA.put(stitchResult.getAtlasTextureId(), stitchResult);
				}

				BlinkLoad.LOGGER.info(
						"Loaded atlas textures from cache (took {}ms).",
						TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
				);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return CACHED_DATA;
	}

	public static void cacheData(@NotNull StitchResult stitchResult) {
		getCachedData().put(stitchResult.getAtlasTextureId(), stitchResult);
	}

	private static void onFirstResourceReload() {
		hasFirstResourceReloadFinished = true;
		// TODO: Don't write the cache every time the game starts in the render thread
		BlinkLoad.LOGGER.info("Atlas creation finished. Caching data to JSON.");
		writeCacheDataToFile();
	}

	private static void writeCacheDataToFile() {
		try {
			// TODO: Add error handling
			CACHED_DATA_FILE.getParentFile().mkdirs();

			@NotNull var file = new FileWriter(CACHED_DATA_FILE);
			file.write(JsonUtil.getGson().toJson(getCachedData().values()));
			file.close();
		} catch (IOException e) {
			BlinkLoad.LOGGER.error("Exception thrown while writing cache data to file ({}): {}", e, CACHED_DATA_FILE);
		}
	}
}
