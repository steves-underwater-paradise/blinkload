package io.github.steveplays28.blinkload.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.github.steveplays28.blinkload.BlinkLoad;
import io.github.steveplays28.blinkload.client.cache.BlinkLoadCache;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class HashUtil {

    private static final @NotNull File CACHED_HASH_FILE = new File(
            String.format("%s/hash.txt", CacheUtil.getCachePath()));

    static String[] filterArray = {"generated_"};

    public static String getModList(){
        List<ModContainer> mods = new ArrayList<>(FabricLoader.getInstance().getAllMods());
        StringBuilder modNames = new StringBuilder();

        // Alphabetically sort the mod list
        mods.sort((mod1, mod2) -> mod1.toString().compareToIgnoreCase(mod2.toString()));

        // Append mod names into single string
        for (ModContainer mod : mods) {

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

    public static String getHashedList(String modList) {
        BlinkLoad.LOGGER.info(modList);
        int seed = 1; // Fixed seed for consistent hashing

        // Initialize the Murmur3 hash function with the fixed seed
        HashFunction hashFunction = Hashing.murmur3_128(seed);

        // Convert the input string to bytes using UTF-8 encoding
        byte[] inputBytes = modList.getBytes(StandardCharsets.UTF_8);

        // Print the input bytes to ensure consistency
        System.out.println("Input bytes: " + Arrays.toString(inputBytes));

        // Hash the string input using MurmurHash3 with the fixed seed
        return hashFunction.hashString(modList, StandardCharsets.UTF_8).toString();
    }

    // Method to save the hash to the file
    public static void saveHash(String hash) {
        try {
            // Ensure the parent directory exists
            File parentDir = CACHED_HASH_FILE.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Write the hash to the file
            Files.write(CACHED_HASH_FILE.toPath(), hash.getBytes());
        } catch (IOException e) {
            // Handle the exception internally
            System.err.println("Error saving hash: " + e.getMessage());
        }
    }

    // Method to load the hash from the file
    public static String loadHash() {
        try {
            Path filePath = CACHED_HASH_FILE.toPath();
            if (Files.exists(filePath)) {
                return new String(Files.readAllBytes(filePath));
            }
        } catch (IOException e) {
            // Handle the exception internally
            System.err.println("Error loading hash: " + e.getMessage());
        }
        return null; // Return null if an error occurs or the file doesn't exist
    }

    public static boolean compareHashes(String currentHash) {
        String cachedHash = loadHash();

        assert cachedHash != null;
        if (cachedHash.equals(currentHash)) {
            BlinkLoad.LOGGER.info("HASHES MATCH! Cache: {} | Current: {}", cachedHash, currentHash);
            BlinkLoadCache.isUpToDate = true;
            return true;
        } else {
            BlinkLoad.LOGGER.info("HASHES DON'T MATCH! Cache: {} | Current: {}", cachedHash, currentHash);
            return false;
        }
    }
}
