package io.github.steveplays28.blinkload.fabric;

import io.github.steveplays28.blinkload.BlinkLoad;
import net.fabricmc.api.ModInitializer;

public class BlinkLoadFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BlinkLoad.initialize();
    }
}
