package io.github.steveplays28.blinkload.client.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

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
