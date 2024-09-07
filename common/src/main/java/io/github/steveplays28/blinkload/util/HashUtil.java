package io.github.steveplays28.blinkload.util;

import com.google.common.hash.Hashing;
import io.github.steveplays28.blinkload.BlinkLoad;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HashUtil {
	private static final @NotNull File CACHED_HASH_FILE = new File(
			String.format("%s/mod_and_enabled_resource_pack_list_hash", CacheUtil.getCachePath()));
	// TODO: Move into a config file
	private static final @NotNull String[] nameFilterSubstrings = {"generated"};

	public static @NotNull String getModAndEnabledResourcePackListCommaSeparated() {
		@NotNull var modAndResourcePackNames = ModUtil.getModListNames();
		modAndResourcePackNames.addAll(MinecraftClient.getInstance().getResourcePackManager().getEnabledNames());
		// Alphabetically sort the mod/resource pack list
		modAndResourcePackNames.sort(String::compareToIgnoreCase);

		@NotNull List<String> filteredNames = new ArrayList<>();
		for (@NotNull String name : modAndResourcePackNames) {
			if (Arrays.stream(nameFilterSubstrings).noneMatch(name::contains)) {
				continue;
			}

			modAndResourcePackNames.remove(name);
			filteredNames.add(name);
		}

		BlinkLoad.LOGGER.info("Mods/resource packs containing a filtered substring: {}", StringUtils.join(filteredNames, ", "));
		return StringUtils.join(modAndResourcePackNames, ", ");
	}

	/**
	 * Hashes the input {@link String} using {@link Hashing#murmur3_128}, with {@code 1} as the seed.
	 *
	 * @param input The input {@link String}.
	 * @return The hashed output {@link String}.
	 */
	public static @NotNull String calculateHash(@NotNull String input) {
		return Hashing.murmur3_128(1).hashString(input, StandardCharsets.UTF_8).toString();
	}

	// Method to save the hash to the file
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void saveHash(@NotNull String hash) {
		try {
			// Ensure the parent directory exists
			CACHED_HASH_FILE.getParentFile().mkdirs();

			// Write the hash to the file
			Files.write(CACHED_HASH_FILE.toPath(), hash.getBytes());
		} catch (IOException e) {
			BlinkLoad.LOGGER.error("Exception occurred while saving mod list hash:", e);
		}
	}

	// Method to load the hash from the file
	public static @Nullable String loadHash() {
		try {
			@NotNull var filePath = CACHED_HASH_FILE.toPath();
			if (!Files.exists(filePath)) {
				return null;
			}

			return new String(Files.readAllBytes(filePath));
		} catch (IOException e) {
			BlinkLoad.LOGGER.error("Exception occurred while loading mod list hash:", e);
		}
		return null;
	}

	public static boolean compareHashes(String currentHash) {
		@Nullable String cachedHash = loadHash();
		if (cachedHash == null) {
			BlinkLoad.LOGGER.info("Mod list hash file does not exist.");
		}

		if (cachedHash != null && cachedHash.equals(currentHash)) {
			BlinkLoad.LOGGER.info("Mod list hash ({}) matches with cache ({}). Continuing loading.", currentHash, cachedHash);
			return true;
		} else {
			BlinkLoad.LOGGER.info("Mod list hash ({}) doesn't match with cache ({}).", currentHash, cachedHash);
			return false;
		}
	}
}
