package io.github.steveplays28.blinkload.client.cache;

import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.CacheUtil;
import io.github.steveplays28.blinkload.util.HashUtil;
import io.github.steveplays28.blinkload.util.ThreadUtil;
import io.github.steveplays28.blinkload.util.resource.json.AtlasTextureIdentifier;
import io.github.steveplays28.blinkload.util.resource.json.JsonUtil;
import io.github.steveplays28.blinkload.util.resource.json.StitchResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static io.github.steveplays28.blinkload.BlinkLoad.MOD_ID;

@Environment(EnvType.CLIENT)
public class BlinkLoadCache {
	private static final Logger LOGGER = LoggerFactory.getLogger(String.format("%s/cache", MOD_ID));
	private static final @NotNull File CACHED_DATA_FILE = new File(
			String.format("%s/atlas_textures_cache.json", CacheUtil.getCachePath()));
	private static final @NotNull String MOD_LIST_HASH = HashUtil.calculateHash(HashUtil.getModList());

	private static @Nullable CompletableFuture<Map<AtlasTextureIdentifier, StitchResult>> cachedDataCompletableFuture = null;
	private static @Nullable Map<AtlasTextureIdentifier, StitchResult> cachedData = null;
	private static boolean hasClientStarted = false;
	private static @Nullable Boolean isUpToDate = null;

	public static void initialize() {
		ClientLifecycleEvent.CLIENT_MAIN_STARTING.register(BlinkLoadCache::loadCachedDataAsync);
		dev.architectury.event.events.client.ClientLifecycleEvent.CLIENT_STARTED.register(instance -> hasClientStarted = true);
		ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_STARTING.register(BlinkLoadCache::invalidateCache);
		ClientLifecycleEvent.CLIENT_RESOURCE_RELOAD_FINISHED.register(BlinkLoadCache::writeCacheDataToFileAsync);
	}

	public static boolean isUpToDate() {
		if (isUpToDate == null) {
			isUpToDate = Files.exists(CACHED_DATA_FILE.toPath()) && HashUtil.compareHashes(MOD_LIST_HASH);
		}

		return isUpToDate;
	}

	public static void invalidateCache() {
		if (!hasClientStarted) {
			return;
		}

		isUpToDate = false;
	}

	public static @NotNull Map<AtlasTextureIdentifier, StitchResult> getCachedData() {
		if (cachedData == null) {
			loadCachedDataAsync().join();
		}

		return cachedData;
	}

	public static void cacheData(@NotNull StitchResult stitchResult) {
		getCachedData().put(new AtlasTextureIdentifier(stitchResult.getAtlasTextureId(), stitchResult.getMipLevel()), stitchResult);
	}

	private static @NotNull CompletableFuture<Map<AtlasTextureIdentifier, StitchResult>> loadCachedDataAsync() {
		if (cachedDataCompletableFuture == null) {
			cachedDataCompletableFuture = CompletableFuture.supplyAsync(
					BlinkLoadCache::loadCachedData, ThreadUtil.getAtlasTextureIOThreadPoolExecutor()
			).whenCompleteAsync(
					(cachedData, throwable) -> {
						if (throwable != null) {
							LOGGER.error(
									"Exception thrown while trying to load the atlas texture cache: ",
									ExceptionUtils.getRootCause(throwable)
							);
						}

						BlinkLoadCache.cachedData = cachedData;
					}
			);
		}

		return cachedDataCompletableFuture;
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	private static @NotNull Map<AtlasTextureIdentifier, StitchResult> loadCachedData() {
		if (!isUpToDate()) {
			return new ConcurrentHashMap<>();
		}

		var startTime = System.nanoTime();
		@NotNull Map<AtlasTextureIdentifier, StitchResult> cachedData = new ConcurrentHashMap<>();
		// Read JSON from the cached data file
		try (@NotNull Reader reader = new FileReader(CACHED_DATA_FILE)) {
			// Convert the JSON data to a Java object
			@NotNull var stitchResults = JsonUtil.getGson().fromJson(reader, StitchResult[].class);
			for (int stitchResultIndex = 0; stitchResultIndex < stitchResults.length; stitchResultIndex++) {
				@NotNull var stitchResult = stitchResults[stitchResultIndex];
				cachedData.put(new AtlasTextureIdentifier(stitchResult.getAtlasTextureId(), stitchResult.getMipLevel()), stitchResult);
			}

			LOGGER.info(
					"Loaded atlas textures from cache (took {}ms).",
					TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return cachedData;
	}

	private static void writeCacheDataToFileAsync() {
		CompletableFuture.runAsync(BlinkLoadCache::writeCacheDataToFile, ThreadUtil.getAtlasTextureIOThreadPoolExecutor());
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static void writeCacheDataToFile() {
		if (isUpToDate()) {
			return;
		}

		try {
			LOGGER.info("Atlas creation finished, writing cache data to file ({}).", CACHED_DATA_FILE);
			@NotNull var cacheDirectory = CACHED_DATA_FILE.getParentFile();
			FileUtils.deleteDirectory(cacheDirectory);
			cacheDirectory.mkdirs();
			HashUtil.saveHash(MOD_LIST_HASH);

			@NotNull var file = new FileWriter(CACHED_DATA_FILE);
			file.write(JsonUtil.getGson().toJson(getCachedData().values()));
			file.close();
		} catch (IOException e) {
			LOGGER.error("Exception thrown while writing cache data to file ({}): {}", e, CACHED_DATA_FILE);
		}
	}
}
