package com.efimchick.eventregistrar;

import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SecondMemoryDurationTimedEventRegistrarTest {

    private final EventRegistrar tenthOfSecondMemoryDurationEventRegistrar = new TimeBasedEventRegistrar(Duration.of(1, SECONDS));

    @Test
    public void registerEvent() throws Exception {
        tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
    }

    @Test
    public void registerAndCountSingleEvent() throws Exception {
        tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        MILLISECONDS.sleep(1);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));
    }


    @Test
    public void registerAndCountSingleEventAndRepeat() throws Exception {
        tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));

        tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(2));
    }

    @Test
    public void registerAndCountSingleEventAndRepeatAfterForgetting() throws Exception {
        tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        MILLISECONDS.sleep(1);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));

        TimeUnit.SECONDS.sleep(1);

        tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        MILLISECONDS.sleep(1);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));
    }

    @Test
    public void registerAndCountMultipleEventsAndRepeat() throws Exception {
        final int amount = (int) (5 + Math.random() * 10);
        for (int i = 0; i < amount; i++) {
            tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        }
        MILLISECONDS.sleep(1);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount));

        for (int i = 0; i < amount; i++) {
            tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        }
        MILLISECONDS.sleep(1);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount * 2));
    }
    @Test
    public void registerAndCountMultipleEvents() throws Exception {
        final int amount = (int) (5 + Math.random() * 10);
        for (int i = 0; i < amount; i++) {
            tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        }
        MILLISECONDS.sleep(1);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount));
    }

    @Test
    public void registerAndCountMultipleEventsAsync() throws Exception {
        final int amount = (int) (10 + Math.random() * 10);
        final ExecutorService threadPool = Executors.newWorkStealingPool();

        for (int i = 0; i < amount; i++) {
            threadPool.submit(
                    () -> tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {})
            );
        }
        threadPool.shutdown();
        threadPool.awaitTermination(80, MILLISECONDS);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount));
    }

    @Test
    public void registerAndCountMultipleEventsAsyncAndWaitToForget() throws Exception {
        final int amount = (int) (10 + Math.random() * 10);
        final ExecutorService threadPool = Executors.newWorkStealingPool();

        for (int i = 0; i < amount; i++) {
            threadPool.submit(
                    () -> tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {})
            );
        }
        threadPool.shutdown();
        threadPool.awaitTermination(80, MILLISECONDS);
        TimeUnit.SECONDS.sleep(2);
        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(0));
    }

    @Test
    public void registerAndCountMultipleEventsAsyncAndWaitToPartiallyForget() throws Exception {
        final int amount = (int) (10 + Math.random() * 10);
        final ExecutorService threadPool = Executors.newWorkStealingPool();

        for (int i = 0; i < amount; i++) {
            threadPool.submit(
                    () -> tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {})
            );
        }

        TimeUnit.SECONDS.sleep(1);

        final int secondAmount = (int) (1 + Math.random() * 10);
        for (int i = 0; i < secondAmount; i++) {
            threadPool.submit(
                    () -> tenthOfSecondMemoryDurationEventRegistrar.registerEvent(new Event() {})
            );
        }

        threadPool.shutdown();
        threadPool.awaitTermination(80, MILLISECONDS);

        assertThat(tenthOfSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(secondAmount));
    }
}
