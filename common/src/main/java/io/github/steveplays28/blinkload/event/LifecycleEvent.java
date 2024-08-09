package io.github.steveplays28.blinkload.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface LifecycleEvent {
	/**
	 * @see BeforeBootstrapFinishInitialization
	 */
	Event<BeforeBootstrapFinishInitialization> BEFORE_BOOTSTRAP_FINISH_INITIALIZATION = EventFactory.createLoop();

	@FunctionalInterface
	interface BeforeBootstrapFinishInitialization {
		/**
		 * Invoked when the {@link net.minecraft.Bootstrap} is accessed for the first time and needs to finish initialization.
		 */
		void onBeforeBootstrapFinishInitialization();
	}
}
