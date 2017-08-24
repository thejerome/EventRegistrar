package com.efimchick.eventregistrar;

class BasicEventRegistrar implements EventRegistrar {

    int i = 0;

    @Override
    public void registerEvent(Event event) {
        i++;
    }

    @Override
    public int countRegisteredEvents() {
        return i;
    }
}
