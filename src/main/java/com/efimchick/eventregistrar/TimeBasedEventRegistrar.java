package com.efimchick.eventregistrar;

import one.util.streamex.StreamEx;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


class TimeBasedEventRegistrar implements EventRegistrar {

    private final Duration duration;
    private volatile ConcurrentLinkedDeque<Instant> eventInstants = new ConcurrentLinkedDeque<>();
    private volatile Instant previousCleanUpInstant = Instant.now();

    private final ReentrantLock cleanUpLock = new ReentrantLock();
    private final ReentrantLock sizeCountLock = new ReentrantLock();

    TimeBasedEventRegistrar(Duration duration) {
        this.duration = duration;
    }

    @Override
    public void registerEvent(Event event) {
        final Instant now = Instant.now();
        eventInstants.addFirst(now);

        if(Duration.between(previousCleanUpInstant, now).compareTo(duration) > 0){
            cleanUp(now);
        }
    }

    @Override
    public int countRegisteredEvents() {
        final Instant now = Instant.now();
        final int size;
        try {
            sizeCountLock.lock();
            cleanUp(now);
            size = eventInstants.size();
        } finally {
            sizeCountLock.unlock();
        }
        return size;
    }

    private void cleanUp(Instant now) {
        try {
            cleanUpLock.lock();

            this.eventInstants = new ConcurrentLinkedDeque<>(StreamEx.of(this.eventInstants)
                    .takeWhile(eventInstant -> Duration.between(eventInstant, now).compareTo(duration) <= 0)
                    .collect(Collectors.toList())
            );

            previousCleanUpInstant = now;
        } finally {
            cleanUpLock.unlock();
        }
    }
}
