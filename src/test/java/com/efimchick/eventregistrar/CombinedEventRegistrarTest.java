package com.efimchick.eventregistrar;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;

public class CombinedEventRegistrarTest {

    private final CombinedEventRegistrar combinedEventRegistrar = new CombinedEventRegistrar(
            new TimeBasedEventRegistrar(Duration.of(0, ChronoUnit.MILLIS)),
            new TimeBasedEventRegistrar(Duration.of(500, ChronoUnit.MILLIS)),
            new TimeBasedEventRegistrar(Duration.of(1000, ChronoUnit.MILLIS))
    );

    @Test
    public void registerEvent() throws Exception {
        combinedEventRegistrar.registerEvent(new Event() {});
        TimeUnit.MILLISECONDS.sleep(1);

        Assert.assertThat(combinedEventRegistrar.countRegisteredEvents(0), is(0));
        Assert.assertThat(combinedEventRegistrar.countRegisteredEvents(1), is(1));
        Assert.assertThat(combinedEventRegistrar.countRegisteredEvents(2), is(1));

        TimeUnit.MILLISECONDS.sleep(500);

        Assert.assertThat(combinedEventRegistrar.countRegisteredEvents(0), is(0));
        Assert.assertThat(combinedEventRegistrar.countRegisteredEvents(1), is(0));
        Assert.assertThat(combinedEventRegistrar.countRegisteredEvents(2), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongMemberIndex3() throws Exception {
        combinedEventRegistrar.countRegisteredEvents(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongMemberIndexNegative() throws Exception {
        combinedEventRegistrar.countRegisteredEvents(-1);
    }

}