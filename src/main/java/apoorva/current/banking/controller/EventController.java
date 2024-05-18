package apoorva.current.banking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import apoorva.current.banking.store.InMemoryEventStore;

@RestController
public class EventController {

    private final InMemoryEventStore eventStore;

    public EventController(InMemoryEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @GetMapping("/api/events")
    public List<Object> getAllEvents() {
        return eventStore.getEvents();
    }

}
