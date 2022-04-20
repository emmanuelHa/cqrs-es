package com.techbank.account.command.infra;

import com.techbank.account.command.domain.AccountAggregate;
import com.techbank.account.command.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.exceptions.ConcurrencyException;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public void saveEvent(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventList =
                eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(expectedVersion != -1 && eventList.get(eventList.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        var version = expectedVersion;
        for(var event: events) {
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if(!persistedEvent.getId().isEmpty()) {
                eventProducer.producer(event.getClass().getSimpleName(), event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventList =
                eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(eventList == null || eventList.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account ID provided");
        }

        return eventList.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }

    @Override
    public List<String> getAggregateIds() {
        List<EventModel> events = eventStoreRepository.findAll();
        if(events == null || events.isEmpty()) {
            throw new IllegalStateException("Cauld not retrieve events from event store");
        }
        return events.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
    }

    public EventProducer getEventProducer() {
        return eventProducer;
    }
}
