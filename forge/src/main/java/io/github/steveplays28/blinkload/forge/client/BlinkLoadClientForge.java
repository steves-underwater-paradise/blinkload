package io.github.steveplays28.blinkload.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BlinkLoadClientForge {
	public static void onInitializeClient() {
		BlinkLoadClient.initialize();
	}
}
