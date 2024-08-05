package io.github.steveplays28.blinkload.client.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ClientLifecycleEvent {
	/**
	 * @see ClientStarted
	 */
	Event<ClientStarted> CLIENT_RESOURCE_RELOAD_FINISHED = EventFactory.createLoop();

	@FunctionalInterface
	interface ClientStarted {
		/**
		 * Invoked when the client has finished resource reloading.
		 */
		void onClientResourceReloadFinished();
	}
}
