package com.efimchick.eventregistrar;


/**
 * Event registrar.
 * Supports registering events and counting them.
 */
interface EventRegistrar {

    /**
     * Performs registering of Event.
     * @param event event to be registered. Currently useless - no API to get events, only to count them.
     */
    void registerEvent(Event event);

    /**
     * Returns amount of registered events.
     * @return amount of registered events.
     */
    int countRegisteredEvents();
    
}
