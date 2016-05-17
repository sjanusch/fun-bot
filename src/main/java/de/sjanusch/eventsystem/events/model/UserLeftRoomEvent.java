package de.sjanusch.eventsystem.events.model;

import de.sjanusch.eventsystem.EventList;
import de.sjanusch.eventsystem.events.UserRoomEvent;
import de.sjanusch.model.HipchatUser;
import de.sjanusch.model.Room;

public class UserLeftRoomEvent extends UserRoomEvent {

    private static final EventList events = new EventList();
    
    public UserLeftRoomEvent(Room room, HipchatUser user, String nick) {
        super(room, user, nick);
    }

    @Override
    public EventList getEvents() {
        return events;
    }
    
    public static EventList getEventList() {
        return events;
    }

}
