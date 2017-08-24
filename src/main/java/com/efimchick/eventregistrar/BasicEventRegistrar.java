package com.efimchick.eventregistrar;

class BasicEventRegistrar implements EventRegistrar {

    private int i = 0;

    @Override
    public void registerEvent(Event event) {
        i++;
    }

    @Override
    public int countRegisteredEvents() {
        return i;
    }
}
