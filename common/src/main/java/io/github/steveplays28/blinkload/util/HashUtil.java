package io.github.steveplays28.blinkload.util;

import io.github.steveplays28.blinkload.BlinkLoad;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.List;

public class HashUtil {

    static List<ModContainer> mods = new ArrayList<>(FabricLoader.getInstance().getAllMods());
    static StringBuilder modNames = new StringBuilder();

    public static String getModList(){
        // Alphabetically sort the mod list
        mods.sort((mod1, mod2) -> mod1.toString().compareToIgnoreCase(mod2.toString()));

        // Append mod names into single string
        for (ModContainer mod : mods) {
            if (!modNames.isEmpty()) {
                modNames.append(", ");
            }
            modNames.append(mod);
        }

        return modNames.toString();
    }

    public static String getHashedList(String modList) {
        // Actually hash the mod list here
    }
}
