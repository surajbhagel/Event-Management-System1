package ems;

public class PrivateEvent extends Event {
    public PrivateEvent(int id, String name, String date, String venue) {
        super(id, name, date, venue, "PRIVATE");
    }
}