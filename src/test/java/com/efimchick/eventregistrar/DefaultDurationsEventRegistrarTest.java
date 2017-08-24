package com.efimchick.eventregistrar;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;

public class DefaultDurationsEventRegistrarTest {

    private final DefaultDurationsEventRegistrar registar = new DefaultDurationsEventRegistrar();

    @Test
    public void registerEvent() throws Exception {

        final Field membersField = registar.getClass().getSuperclass().getDeclaredField("members");
        membersField.setAccessible(true);
        final EventRegistrar[] members = (EventRegistrar[]) membersField.get(registar);

        members[0] = new TimeBasedEventRegistrar(Duration.of(0, ChronoUnit.MILLIS));
        members[1] = new TimeBasedEventRegistrar(Duration.of(100, ChronoUnit.MILLIS));
        members[2] = new TimeBasedEventRegistrar(Duration.of(1000, ChronoUnit.MILLIS));

        registar.registerEvent(new Event() {});
        TimeUnit.MILLISECONDS.sleep(1);

        Assert.assertThat(registar.countRegisteredEventsForLastMinute(), is(0));
        Assert.assertThat(registar.countRegisteredEventsForLastHour(), is(1));
        Assert.assertThat(registar.countRegisteredEventsForLastDay(), is(1));

        TimeUnit.MILLISECONDS.sleep(200);

        Assert.assertThat(registar.countRegisteredEventsForLastMinute(), is(0));
        Assert.assertThat(registar.countRegisteredEventsForLastHour(), is(0));
        Assert.assertThat(registar.countRegisteredEventsForLastDay(), is(1));

    }

}