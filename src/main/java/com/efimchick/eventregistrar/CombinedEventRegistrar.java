package com.efimchick.eventregistrar;

import java.util.Arrays;
import java.util.Objects;

class CombinedEventRegistrar {

    private final EventRegistrar[] members;

    CombinedEventRegistrar(EventRegistrar... members) {
        Objects.requireNonNull(members);
        this.members = Arrays.copyOf(members, members.length);
    }

    /**
     * Performs registering of Event.
     * @param event event to be registered. Currently useless - no API to get events, only their count.
     */
    void registerEvent(Event event) {
        for (EventRegistrar member : members) {
            member.registerEvent(event);
        }
    }

    /**
     * Returns amount of registered events for member registrar of index.
     *
     * @param memberIndex index to identify member registrar.
     * @return amount of registered events for member registrar of index.
     *
     * @throws IllegalArgumentException if no member for index.
     */
    int countRegisteredEvents(int memberIndex){
        if(memberIndex < 0 || memberIndex > members.length - 1)
            throw new IllegalArgumentException("No member for index " + memberIndex);

        return members[memberIndex].countRegisteredEvents();
    }





}
