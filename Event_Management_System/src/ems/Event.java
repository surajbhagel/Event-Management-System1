package ems;

import java.io.Serializable;

public class Event implements Serializable {
    private int id;
    private String name;
    private String date;
    private String venue;
    private String type; // PUBLIC or PRIVATE

    public Event(int id, String name, String date, String venue, String type) {
        this.id = id; this.name = name; this.date = date; this.venue = venue; this.type = type;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getVenue() { return venue; }
    public String getType() { return type; }

    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setVenue(String venue) { this.venue = venue; }

    @Override
    public String toString() {
        return String.format("Event[id=%d, name=%s, date=%s, venue=%s, type=%s]", id, name, date, venue, type);
    }
}