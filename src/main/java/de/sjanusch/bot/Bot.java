package de.sjanusch.bot;

import de.sjanusch.model.HipchatUser;
import de.sjanusch.model.Room;

import java.io.IOException;
import java.util.List;

public interface Bot {

    void run();

    void changeRoom(Room room);

    Room getSelectedRoom();

    List<HipchatUser> getUsers() throws IOException;

    String sendNotification(String message) throws IOException;

    String sendNotification(String message, String from);

    String getApiKey() throws IOException;

    String getBotroom() throws IOException;

    String getNickname() throws IOException;

    String getPassword() throws IOException;

    String getUsername() throws IOException;
}
