package io.github.steveplays28.blinkload.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public interface ClientLifecycleEvent {
	/**
	 * @see ClientMainStarting
	 */
	Event<ClientMainStarting> CLIENT_MAIN_STARTING = EventFactory.createArrayBacked(
		ClientMainStarting.class,
		callbacks -> () -> {
			for (ClientMainStarting callback : callbacks) {
				callback.onClientMainStarting();
			}
		}
	);

	/**
	 * @see ClientResourceReloadStarting
	 */
	Event<ClientResourceReloadStarting> CLIENT_RESOURCE_RELOAD_STARTING = EventFactory.createArrayBacked(
		ClientResourceReloadStarting.class,
		callbacks -> () -> {
			for (ClientResourceReloadStarting callback : callbacks) {
				callback.onClientResourceReloadStarting();
			}
		}
	);

	/**
	 * @see ClientResourceReloadFinished
	 */
	Event<ClientResourceReloadFinished> CLIENT_RESOURCE_RELOAD_FINISHED = EventFactory.createArrayBacked(
		ClientResourceReloadFinished.class,
		callbacks -> () -> {
			for (ClientResourceReloadFinished callback : callbacks) {
				callback.onClientResourceReloadFinished();
			}
		}
	);

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
