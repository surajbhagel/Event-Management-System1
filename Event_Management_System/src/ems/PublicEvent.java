package ems;

public class PublicEvent extends Event {
    public PublicEvent(int id, String name, String date, String venue) {
        super(id, name, date, venue, "PUBLIC");
    }
}