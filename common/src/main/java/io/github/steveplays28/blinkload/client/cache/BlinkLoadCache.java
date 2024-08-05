package io.github.steveplays28.blinkload.client.cache;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.CacheUtil;
import io.github.steveplays28.blinkload.util.ThreadUtil;
import io.github.steveplays28.blinkload.util.resource.json.AtlasTextureIdentifier;
import io.github.steveplays28.blinkload.util.resource.json.JsonUtil;
import io.github.steveplays28.blinkload.util.resource.json.StitchResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public class BlinkLoadCache {
	private static final @NotNull File CACHED_DATA_FILE = new File(
			String.format("%s/atlas_textures_cache.json", CacheUtil.getCachePath()));

	private static @Nullable CompletableFuture<Void> cachedDataCompletableFuture = null;
	private static @Nullable Map<AtlasTextureIdentifier, StitchResult> cachedData = null;
	private static @Nullable Boolean isUpToDate = null;

	public static void initialize() {
		ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_FINISHED.register(BlinkLoadCache::onClientResourceReloadFinished);
	}

	public static boolean isUpToDate() {
		if (isUpToDate == null) {
			// TODO: Compare the mod list's hash
			isUpToDate = Files.exists(CACHED_DATA_FILE.toPath());
		}

		return isUpToDate;
	}

	public static @NotNull Map<AtlasTextureIdentifier, StitchResult> getCachedData() {
		if (cachedData == null) {
			loadCachedDataAsync().join();
		}

		return cachedData;
	}

	public static @NotNull CompletableFuture<Void> loadCachedDataAsync() {
		if (cachedDataCompletableFuture == null) {
			cachedDataCompletableFuture = CompletableFuture.runAsync(
					BlinkLoadCache::loadCachedData, ThreadUtil.getAtlasTextureLoaderThreadPoolExecutor());
		}

		return cachedDataCompletableFuture;
	}

	public static void cacheData(@NotNull StitchResult stitchResult) {
		getCachedData().put(new AtlasTextureIdentifier(stitchResult.getAtlasTextureId(), stitchResult.getMipLevel()), stitchResult);
	}

	private static void onClientResourceReloadFinished() {
		writeCacheDataToFile();
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	private static void loadCachedData() {
		var startTime = System.nanoTime();

		cachedData = new ConcurrentHashMap<>();
		// Read JSON from the cached data file
		try (@NotNull Reader reader = new FileReader(CACHED_DATA_FILE)) {
			// Convert the JSON data to a Java object
			@NotNull var stitchResults = JsonUtil.getGson().fromJson(reader, StitchResult[].class);
			for (int stitchResultIndex = 0; stitchResultIndex < stitchResults.length; stitchResultIndex++) {
				@NotNull var stitchResult = stitchResults[stitchResultIndex];
				cachedData.put(new AtlasTextureIdentifier(stitchResult.getAtlasTextureId(), stitchResult.getMipLevel()), stitchResult);
			}

			BlinkLoad.LOGGER.info(
					"Loaded atlas textures from cache (took {}ms).",
					TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void writeCacheDataToFile() {
		if (isUpToDate()) {
			return;
		}

		try {
			BlinkLoad.LOGGER.info("Atlas creation finished, writing cache data to file ({}).", CACHED_DATA_FILE);

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
