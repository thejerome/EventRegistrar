package com.efimchick.eventregistrar;

import one.util.streamex.StreamEx;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


class TimeBasedEventRegistrar implements EventRegistrar {

    private final Duration duration;
    private volatile ConcurrentLinkedDeque<Instant> eventInstants = new ConcurrentLinkedDeque<>();

    private final ReentrantLock cleanUpLock = new ReentrantLock();

    TimeBasedEventRegistrar(Duration duration) {
        this.duration = duration;
    }

    @Override
    public void registerEvent(Event event) {
        final Instant now = Instant.now();
        eventInstants.addFirst(now);
    }

    @Override
    public int countRegisteredEvents() {
        return cleanUpAndReturnNewSize(Instant.now());
    }

    private int cleanUpAndReturnNewSize(Instant now) {
        try {
            cleanUpLock.lock();

            this.eventInstants = new ConcurrentLinkedDeque<>(StreamEx.of(this.eventInstants)
                    .takeWhile(eventInstant -> Duration.between(eventInstant, now).compareTo(duration) <= 0)
                    .collect(Collectors.toList())
            );
            return eventInstants.size();
        } finally {
            cleanUpLock.unlock();
        }
    }
}
