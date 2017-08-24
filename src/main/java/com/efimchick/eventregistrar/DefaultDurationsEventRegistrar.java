package com.efimchick.eventregistrar;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


/**
 * Event Registrar.
 * Supports registering events and counting events for last minute, hour or day with corresponding methods.
 */
public class DefaultDurationsEventRegistrar extends CombinedEventRegistrar{
    public DefaultDurationsEventRegistrar() {
        super(
                new TimeBasedEventRegistrar(Duration.of(1, ChronoUnit.MINUTES)),
                new TimeBasedEventRegistrar(Duration.of(1, ChronoUnit.HOURS)),
                new TimeBasedEventRegistrar(Duration.of(1, ChronoUnit.DAYS))
        );
    }

    /**
     * Performs registering of Event.
     * @param event event to be registered. Currently useless - no API to get events, only their count.
     */
    @Override
    public void registerEvent(Event event) {
        super.registerEvent(event);
    }

    /**
     * Returns amount of registered events for last minute.
     *
     * @return amount of registered events for last minute.
     */
    public int countRegisteredEventsForLastMinute(){
        return countRegisteredEvents(0);
    };

    /**
     * Returns amount of registered events for last hour.
     *
     * @return amount of registered events for last hour.
     */
    public int countRegisteredEventsForLastHour(){
        return countRegisteredEvents(1);
    };

    /**
     * Returns amount of registered events for last day.
     *
     * @return amount of registered events for last day.
     */
    public int countRegisteredEventsForLastDay(){
        return countRegisteredEvents(2);
    };
}
