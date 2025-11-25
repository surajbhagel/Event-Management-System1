package ems;

import java.util.List;

public interface EventOperations {
    void addEvent(Event e) throws Exception;
    void updateEvent(Event e) throws Exception;
    void deleteEvent(int id) throws Exception;
    Event getEventById(int id) throws EventNotFoundException;
    List<Event> getAllEvents() throws Exception;
}