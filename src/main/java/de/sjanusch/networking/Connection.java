package de.sjanusch.networking;

import de.sjanusch.model.Room;
import de.sjanusch.networking.exceptions.LoginException;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 06:27
 */
public interface Connection {

    void waitForEnd() throws InterruptedException;

    void connect() throws XMPPException;

    void login(String username, String password) throws LoginException;

    void sendPM(String message, String to) throws XMPPException;

    void joinRoom(String room, String nickname) throws XMPPException, IOException;

    void joinRoom(String APIKey, String room, String nickname) throws XMPPException, IOException;

    MultiUserChat getChat();

    Roster getRoster();

    Room findConnectedRoom(String name);

    Room findRoom(final String name, final String apiKey);

    List<Room> getRooms();

}
