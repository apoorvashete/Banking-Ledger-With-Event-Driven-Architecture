package apoorva.current.banking.store;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

@Component
public class InMemoryEventStore {
    
    private static final Logger logger = (Logger) LoggerFactory.getLogger(InMemoryEventStore.class);

    private List<Object> events = new ArrayList<>();

    public void addEvent(Object event) {
        logger.info("Adding event: {}", event);
        events.add(event);
    }

    public List<Object> getEvents() {
        logger.info("Retrieving events, total count: {}", events.size());
        return new ArrayList<>(events);
    }

}
