package com.efimchick.eventregistrar;

import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SecondMemoryDurationTimedEventRegistrarTest {

    private final EventRegistrar aSecondMemoryDurationEventRegistrar = new TimeBasedEventRegistrar(Duration.of(1, SECONDS));

    @Test
    public void registerEvent() throws Exception {
        aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
        });
    }

    @Test
    public void registerAndCountSingleEvent() throws Exception {
        aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
        });
        MILLISECONDS.sleep(1);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));
    }


    @Test
    public void registerAndCountSingleEventAndRepeat() throws Exception {
        aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
        });
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));

        aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
        });
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(2));
    }

    @Test
    public void registerAndCountSingleEventAndRepeatAfterForgetting() throws Exception {
        aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
        });
        MILLISECONDS.sleep(1);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));

        TimeUnit.SECONDS.sleep(1);

        aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
        });
        MILLISECONDS.sleep(1);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(1));
    }

    @Test
    public void registerAndCountMultipleEventsAndRepeat() throws Exception {
        final int amount = (int) (5 + Math.random() * 10);
        for (int i = 0; i < amount; i++) {
            aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
            });
        }
        MILLISECONDS.sleep(1);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount));

        for (int i = 0; i < amount; i++) {
            aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
            });
        }
        MILLISECONDS.sleep(1);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount * 2));
    }

    @Test
    public void registerAndCountMultipleEvents() throws Exception {
        final int amount = (int) (5 + Math.random() * 10);
        for (int i = 0; i < amount; i++) {
            aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
            });
        }
        MILLISECONDS.sleep(1);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount));
    }

    @Test
    public void registerAndCountMultipleEventsAsync() throws Exception {
        final int amount = (int) (10 + Math.random() * 10);
        final ExecutorService threadPool = Executors.newWorkStealingPool();

        for (int i = 0; i < amount; i++) {
            threadPool.submit(
                    () -> aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
                    })
            );
        }
        threadPool.shutdown();
        threadPool.awaitTermination(80, MILLISECONDS);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount));
    }

    @Test
    public void registerAndCountMultipleEventsAsyncAndWaitToForget() throws Exception {
        final int amount = (int) (100 + Math.random() * 100);
        final ExecutorService threadPool = Executors.newWorkStealingPool();

        for (int i = 0; i < amount; i++) {
            threadPool.submit(
                    () -> aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
                    })
            );
        }
        threadPool.shutdown();
        threadPool.awaitTermination(80, MILLISECONDS);
        TimeUnit.SECONDS.sleep(2);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(0));
    }

    @Test
    public void registerAndCountMultipleEventsAsyncAndWaitToPartiallyForget() throws Exception {
        final int amount = (int) (100 + Math.random() * 100);
        final ExecutorService threadPool = Executors.newWorkStealingPool();

        for (int i = 0; i < amount; i++) {
            threadPool.submit(
                    () -> aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
                    })
            );
        }

        TimeUnit.MILLISECONDS.sleep(10);
        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(amount));

        TimeUnit.SECONDS.sleep(1);

        final int secondAmount = (int) (100 + Math.random() * 100);
        for (int i = 0; i < secondAmount; i++) {
            threadPool.submit(
                    () -> aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {
                    })
            );
        }

        threadPool.shutdown();
        threadPool.awaitTermination(800, MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(100);

        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(secondAmount));
    }


    @Test
    public void registerALotOfEventsAndCount() throws Exception {
        final int amount = (int) (300 + Math.random() * 100);

        for (int i = 0; i < amount; i++) {
            TimeUnit.MILLISECONDS.sleep(10);
            aSecondMemoryDurationEventRegistrar.registerEvent(new Event() {});
        }

        assertThat(aSecondMemoryDurationEventRegistrar.countRegisteredEvents(), is(100));
    }
}
