package de.sjanusch.bot;

import de.sjanusch.model.HipchatUser;
import de.sjanusch.model.Room;

import java.util.List;

public interface Bot {

    void run();

    void changeRoom(Room room);

    Room getSelectedRoom();

    List<HipchatUser> getUsers();

    String sendNotification(String message);

    String sendNotification(String message, String from);

    String getApiKey();

    String getBotroom();

    String getNickname();

    String getPassword();

    String getUsername();
}
