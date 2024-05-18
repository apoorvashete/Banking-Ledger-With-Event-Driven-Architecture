package apoorva.current.banking.service;

import java.util.List;

import apoorva.current.banking.entity.Account;
import apoorva.current.banking.event.AccountCreatedEvent;
import apoorva.current.banking.event.FundsLoadedEvent;
import apoorva.current.banking.event.TransactionAuthorizedEvent;
import apoorva.current.banking.store.InMemoryEventStore;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventReplayer {

    // private static final Logger logger = LoggerFactory.getLogger(EventReplayer.class);
    private final InMemoryEventStore eventStore;


    @Autowired
    public EventReplayer(InMemoryEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Account replayEvents(String userId) {

        AccountAggregator accountAggregator = new AccountAggregator(userId);

        List<Object> events = eventStore.getEvents();

        for (Object event : events) {
            if (event instanceof AccountCreatedEvent) {
                AccountCreatedEvent accountCreatedEvent = (AccountCreatedEvent) event;
                if (accountCreatedEvent.getUserId().equals(userId)) {
                    accountAggregator.apply(accountCreatedEvent);
                    // logger.info("Applied AccountCreatedEvent: {}", accountCreatedEvent);
                }
            } else if (event instanceof FundsLoadedEvent) {
                FundsLoadedEvent fundsLoadedEvent = (FundsLoadedEvent) event;
                if (fundsLoadedEvent.getUserId().equals(userId)) {
                    accountAggregator.apply(fundsLoadedEvent);
                    // logger.info("Applied FundsLoadedEvent: {}", fundsLoadedEvent);
                }
            } else if (event instanceof TransactionAuthorizedEvent) {
                TransactionAuthorizedEvent authorizedEvent = (TransactionAuthorizedEvent) event;
                if (authorizedEvent.getUserId().equals(userId)) {
                    accountAggregator.apply(authorizedEvent);
                   // logger.info("Applied TransactionAuthorizedEvent: {}", authorizedEvent);
                }
            }
        }

        // logger.info("Final account state: {}", accountAggregator.getCurrentState(userId));

        return accountAggregator.getCurrentState(userId);
    }

    public Account loadLatestState(String userId) {
        return replayEvents(userId); // Directly return state from hashmap
    }
}
