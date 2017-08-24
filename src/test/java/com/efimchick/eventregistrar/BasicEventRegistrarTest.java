package com.efimchick.eventregistrar;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BasicEventRegistrarTest {

    private final EventRegistrar eventRegistrar = new BasicEventRegistrar();

    @Test
    public void registerEvent() throws Exception {
        eventRegistrar.registerEvent(new Event() {});
    }

    @Test
    public void registerAndCountSingleEvent() throws Exception {
        eventRegistrar.registerEvent(new Event() {});
        assertThat(eventRegistrar.countRegisteredEvents(), is(1));
    }

    @Test
    public void registerAndCountMultipleEvents() throws Exception {
        final int amount = (int) (5 + Math.random() * 10);
        for (int i = 0; i < amount; i++) {
            eventRegistrar.registerEvent(new Event() {});
        }
        assertThat(eventRegistrar.countRegisteredEvents(), is(amount));
    }
}
