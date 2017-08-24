package com.efimchick.eventregistrar;

import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ZeroMemoryDurationTimedEventRegistrarTest {

    private final EventRegistrar ZeroMemoryDurationEventRegistrar = new TimeBasedEventRegistrar(Duration.of(0, SECONDS));

    @Test
    public void registerEvent() throws Exception {
        ZeroMemoryDurationEventRegistrar.registerEvent(new Event() {});
    }

    @Test
    public void registerAndCountSingleEvent() throws Exception {
        ZeroMemoryDurationEventRegistrar.registerEvent(new Event() {});
        TimeUnit.MILLISECONDS.sleep(1);
        assertThat(ZeroMemoryDurationEventRegistrar.countRegisteredEvents(), is(0));
    }

    @Test
    public void registerAndCountMultipleEvents() throws Exception {
        final int amount = (int) (5 + Math.random() * 10);
        for (int i = 0; i < amount; i++) {
            ZeroMemoryDurationEventRegistrar.registerEvent(new Event() {});
        }
        TimeUnit.MILLISECONDS.sleep(1);
        assertThat(ZeroMemoryDurationEventRegistrar.countRegisteredEvents(), is(0));
    }
}
