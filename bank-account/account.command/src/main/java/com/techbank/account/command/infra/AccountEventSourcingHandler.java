package com.techbank.account.command.infra;

import com.techbank.account.command.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    @Autowired
    private EventStore eventStore;


    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvent(aggregate.getId(), aggregate.getUncommitedChanges(),
                aggregate.getVersion());
        aggregate.markChangesAsCommited();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if(events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var latestVersion = events.stream()
                    .map(BaseEvent::getVersion).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }

    @Override
    public void republishEvents() {
        List<String > aggregateIds = eventStore.getAggregateIds();
        for (String aggregateId : aggregateIds) {
            AccountAggregate accountAggregate = getById(aggregateId);
            if(accountAggregate == null || !accountAggregate.getActive()) {
                continue;
            }
            var events = eventStore.getEvents(aggregateId);
            EventProducer eventProducer = ((AccountEventStore) eventStore).getEventProducer();
            for (BaseEvent event : events) {
                eventProducer.producer(event.getClass().getSimpleName(), event);
            }

        }
    }
}
