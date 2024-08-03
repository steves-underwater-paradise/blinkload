package io.github.steveplays28.blinkload.client.cache;

import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.event.ClientLifecycleEvent;
import io.github.steveplays28.blinkload.util.AtlasTextureUtils;
import io.github.steveplays28.blinkload.util.JsonUtil;
import io.github.steveplays28.blinkload.util.StitchResult;
import net.minecraft.client.texture.NativeImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlinkLoadCache {
	private static final @NotNull File CACHED_DATA_FILE = new File(String.format("%s/atlas_textures_cache.json", AtlasTextureUtils.getCachePath()));
	private static final @NotNull Map<StitchResult, List<NativeImage>> CACHED_DATA = new ConcurrentHashMap<>();

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

	public static @NotNull Map<StitchResult, List<NativeImage>> getCachedData() {
		// Read JSON from a file
		try (Reader reader = new FileReader(CACHED_DATA_FILE)) {
			// convert the JSON data to a Java object
			var stitchResults = JsonUtil.getGson().fromJson(reader, StitchResult[].class);
			for (@NotNull var stitchResult : stitchResults) {
				// TODO: Read all NativeImages
				@NotNull List<NativeImage> nativeImages = new ArrayList<>();
				@NotNull var nativeImage = NativeImage.read(new FileInputStream(Path.of(String.format("%s/%s.png", AtlasTextureUtils.getCachePath(), stitchResult.getUUID())).toFile()));
				nativeImages.add(nativeImage);
				CACHED_DATA.put(stitchResult, nativeImages);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return CACHED_DATA;
	}

	public static void cacheData(@NotNull StitchResult stitchResult, @NotNull List<NativeImage> nativeImage) {
		CACHED_DATA.put(stitchResult, nativeImage);
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
			file.write(String.format("[%s]", String.join(",\n", JsonUtil.getGson().toJson(CACHED_DATA))));
			file.close();

			for (@Nullable var cachedData : CACHED_DATA.entrySet()) {
				if (cachedData == null) {
					continue;
				}

				@Nullable var nativeImages = cachedData.getValue();
				if (nativeImages == null) {
					continue;
				}

                for (@NotNull var nativeImage : nativeImages) {
					nativeImage.writeTo(Path.of(String.format("%s/%s.png", AtlasTextureUtils.getCachePath(), cachedData.getKey().getUUID())));
				}
			}
		} catch (IOException e) {
			BlinkLoad.LOGGER.error("Exception thrown while writing cache data to file ({}): {}", e, CACHED_DATA_FILE);
        }
    }
}
