package ems;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/*
 EventManager - stores events in memory and persists to a local file (events.dat).
 This avoids database setup for easy running. Methods are synchronized (via lock)
 to demonstrate thread-safety. You can replace persistence with JDBC code later.
*/
public class EventManager implements EventOperations {
    private final List<Event> eventList = new ArrayList<>();
    private final Map<Integer, Event> eventMap = new HashMap<>();
    private final File storageFile;
    private final ReentrantLock lock = new ReentrantLock();

    public EventManager(String storagePath) {
        this.storageFile = new File(storagePath);
        loadFromFile();
    }

    @Override
    public void addEvent(Event e) throws Exception {
        lock.lock();
        try {
            if (eventMap.containsKey(e.getId())) throw new Exception("Event with id exists: " + e.getId());
            eventList.add(e);
            eventMap.put(e.getId(), e);
            saveToFile();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateEvent(Event e) throws Exception {
        lock.lock();
        try {
            if (!eventMap.containsKey(e.getId())) throw new EventNotFoundException("No event with id " + e.getId());
            Event stored = eventMap.get(e.getId());
            stored.setName(e.getName());
            stored.setDate(e.getDate());
            stored.setVenue(e.getVenue());
            saveToFile();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteEvent(int id) throws Exception {
        lock.lock();
        try {
            if (!eventMap.containsKey(id)) throw new EventNotFoundException("No event with id " + id);
            eventList.removeIf(ev -> ev.getId() == id);
            eventMap.remove(id);
            saveToFile();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Event getEventById(int id) throws EventNotFoundException {
        lock.lock();
        try {
            Event e = eventMap.get(id);
            if (e == null) throw new EventNotFoundException("Event id " + id + " not found");
            return e;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Event> getAllEvents() {
        lock.lock();
        try {
            return new ArrayList<>(eventList);
        } finally {
            lock.unlock();
        }
    }

    // Persistence: simple Java serialization
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(eventList);
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        if (!storageFile.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<Event> list = (List<Event>) obj;
                eventList.clear();
                eventMap.clear();
                for (Event e : list) {
                    eventList.add(e);
                    eventMap.put(e.getId(), e);
                }
            }
        } catch (Exception e) {
            System.err.println("Load failed: " + e.getMessage());
        }
    }
}