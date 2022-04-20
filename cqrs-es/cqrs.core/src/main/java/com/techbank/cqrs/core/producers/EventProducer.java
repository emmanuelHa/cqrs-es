package com.techbank.cqrs.core.producers;

import com.techbank.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void producer(String topic, BaseEvent event);
}
