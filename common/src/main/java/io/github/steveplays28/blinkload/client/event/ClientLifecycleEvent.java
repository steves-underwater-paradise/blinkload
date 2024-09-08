package io.github.steveplays28.blinkload.client.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ClientLifecycleEvent {
	/**
	 * @see ClientMainStarting
	 */
	Event<ClientMainStarting> CLIENT_MAIN_STARTING = EventFactory.createLoop();
	/**
	 * @see ClientResourceReloadStarting
	 */
	Event<ClientResourceReloadStarting> CLIENT_RESOURCE_RELOAD_STARTING = EventFactory.createLoop();
	/**
	 * @see ClientResourceReloadFinished
	 */
	Event<ClientResourceReloadFinished> CLIENT_RESOURCE_RELOAD_FINISHED = EventFactory.createLoop();

	@FunctionalInterface
	interface ClientMainStarting {
		/**
		 * Invoked when the client starts.
		 */
		void onClientMainStarting();
	}

	@FunctionalInterface
	interface ClientResourceReloadFinished {
		/**
		 * Invoked when the client has finished resource reloading.
		 */
		void onClientResourceReloadFinished();
	}

	@FunctionalInterface
	interface ClientResourceReloadStarting {
		/**
		 * Invoked when the client is starting resource reloading.
		 */
		void onClientResourceReloadStarting();
	}
}
