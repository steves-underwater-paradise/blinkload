package io.github.steveplays28.blinkload.util;

import com.google.common.hash.Hashing;
import io.github.steveplays28.blinkload.BlinkLoad;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
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
    private static final @NotNull File CACHED_HASH_FILE = new File(String.format("%s/mod_list_hash", CacheUtil.getCachePath()));
    // TODO: Move into a config file
    private static final @NotNull String[] filterArray = {"generated_"};

    public static @NotNull String getModList() {
        List<ModContainer> mods = new ArrayList<>(FabricLoader.getInstance().getAllMods());
        // Alphabetically sort the mod list
        mods.sort((mod1, mod2) -> mod1.toString().compareToIgnoreCase(mod2.toString()));

        @NotNull StringBuilder modNames = new StringBuilder();
        for (@NotNull ModContainer mod : mods) {
            if (Arrays.stream(filterArray).anyMatch(mod.toString()::startsWith)) {
                BlinkLoad.LOGGER.info("Mod: {} Contains a filtered prefix.", mod);
                continue;
            }

            if (!modNames.isEmpty()) {
                modNames.append(", ");
            }
            modNames.append(mod);
        }

        return modNames.toString();
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
            File parentDir = CACHED_HASH_FILE.getParentFile();
            parentDir.mkdirs();

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
