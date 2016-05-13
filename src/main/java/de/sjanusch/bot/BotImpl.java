package de.sjanusch.bot;

import com.google.inject.Inject;
import de.sjanusch.eventsystem.EventSystem;
import de.sjanusch.handler.MessageRecieveListener;
import de.sjanusch.model.HipchatUser;
import de.sjanusch.model.Room;
import de.sjanusch.networking.Connection;
import de.sjanusch.utils.NotificationColor;
import de.sjanusch.utils.NotificationType;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BotImpl implements Bot {

    private static final Logger logger = LoggerFactory.getLogger(BotImpl.class);

    private final String nickname = "LunchBot";

    private final String apiKey = "0AvDGI72wHaRkWHUGIwcHKDiQRLav0RQ080Lnvt6";

    private final String username = "23504_3927963@chat.hipchat.com";

    private final String botroom = "23504_sjanusch_bot_test";

    //private final String botroom = "23504_hackathon_-_bot";

    //private final String botroom = "23504_mittagessen";

    private final String password = "Sandro060979";

    private Room selected;

    private final EventSystem eventSystem;

    private final Connection connection;

    private final MessageRecieveListener messageRecieveListener;

    @Inject
    public BotImpl(final EventSystem eventSystem, final Connection connection, final MessageRecieveListener messageRecieveListener) {
        this.eventSystem = eventSystem;
        this.connection = connection;
        this.messageRecieveListener = messageRecieveListener;
    }

    @Override
    public void run() {
        this.eventSystem.registerEvents(messageRecieveListener);
        try {
            connection.connect();
            connection.login(username, password);
            this.joinRoom();
            logger.debug("Joined " + getSelectedRoom().getXMPPName() + " !");
        } catch (XMPPException e) {
            logger.error("Error during join Room");
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("Error Connection");
            logger.error(e.getMessage());
        }
    }


    public void changeRoom(String name) {
        final Room room = connection.findRoom(name, apiKey);
        if (room != null)
            changeRoom(room);
    }

    public void joinRoom() throws XMPPException {
        if (apiKey.equals("")) {
            this.connection.joinRoom(botroom, nickname);
        } else {
            this.connection.joinRoom(apiKey, botroom, nickname);
            selected = connection.findRoom(botroom, apiKey);
        }
    }

    @Override
    public Room getSelectedRoom() {
        return selected;
    }

    @Override
    public void changeRoom(Room room) {
        this.selected = room;
    }

    public boolean sendPM(String message, String to) {
        if (to.indexOf("@") == -1) { //oh noes its not a JID! The user didnt follow the rules!
            HipchatUser user = findUser(to);
            if (user != null)
                to = nickToJID(user.getName());
            else //Ok I just dont know anymore
                return false;
        }
        try {
            connection.sendPM(message, to);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendPM(String message, HipchatUser user) {
        return sendPM(message, nickToJID(user.getName()));
    }

    public String nickToJID(String nick) {
        for (RosterEntry r : connection.getRoster().getEntries()) {
            if (r.getName().equals(nick))
                return r.getUser();
        }
        return nick;
    }


    @Override
    public List<HipchatUser> getUsers() {
        ArrayList<HipchatUser> users = new ArrayList<HipchatUser>();
        if (apiKey.equals(""))
            return Collections.unmodifiableList(users);
        HipchatUser[] u = HipchatUser.getHipchatUsers(apiKey);
        for (HipchatUser user : u) {
            users.add(user);
        }
        return Collections.unmodifiableList(users);
    }

    public HipchatUser findUser(String name) {
        for (HipchatUser u : getUsers()) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }

    @Override
    public String sendNotification(String message) {
        if (selected == null)
            return "{\"status\": \"failed\"}";
        return sendNotification(message, nickname, selected, NotificationType.TEXT, true, NotificationColor.YELLOW);
    }

    @Override
    public String sendNotification(String message, String from) {
        if (selected == null)
            return "{\"status\": \"failed\"}";
        return sendNotification(message, from, selected, NotificationType.TEXT, true, NotificationColor.YELLOW);
    }

    public String sendNotification(String message, String from, Room room) {
        return sendNotification(message, from, room, NotificationType.TEXT, true, NotificationColor.YELLOW);
    }

    /*
    public String sendNotification(String message, String from, String room) {
        Room r = findRoom(room);
        if (r == null)
            return "{\"status\": \"failed\"}";
        return sendNotification(message, from, r, NotificationType.TEXT, true, NotificationColor.YELLOW);
    }

    */

    public String sendNotification(String message, String from, Room room, NotificationType type) {
        return sendNotification(message, from, room, type, true, NotificationColor.YELLOW);
    }

    public String sendNotification(String message, String from, Room room, NotificationType type, boolean notifyusers) {
        return sendNotification(message, from, room, type, notifyusers, NotificationColor.YELLOW);
    }

    public String sendNotification(String message, String from, Room room, NotificationType type, boolean notifyusers, NotificationColor color) {
        try {
            URL url = new URL("https://api.hipchat.com/v2/room/*/notification?format=json&auth_token=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String tosend = "room_id=" + room.getHipchatRoomInfo(apiKey).getID() + "&from=" + from + "&message=" + message.replaceAll(" ", "+") + "&message_format=" + type.getType() + "&notify=" + notifyusers + "&color=" + color.getType();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + tosend.length());
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(tosend);
            writer.close();
            BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder(100);
            String line;
            while ((line = read.readLine()) != null)
                builder.append(line);
            read.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\": \"failed\"}";
        }
    }

    /**
     * public static String sendNotification(String message, String from, String room_name, String APIKey, NotificationType type, boolean notifyusers, NotificationColor color) throws IOException {
     * <p/>
     * HttpURLConnection connection = (HttpURLConnection) url.openConnection();
     * Room room = Room.createRoom(APIKey, room_name);
     * from = from.replace(" ", "_");
     * String tosend = "room_id=" + room.getHipchatRoomInfo(APIKey).getID() + "&from=" + from + "&message=" + message.replaceAll(" ", "+") + "&message_format=" + type.getType() + "&notify=" + notifyusers + "&color=" + color.getType();
     * connection.setDoOutput(true);
     * connection.setDoInput(true);
     * connection.setRequestMethod("POST");
     * connection.setRequestProperty("Content-Length", "" + tosend.length());
     * connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
     * OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
     * writer.write(tosend);
     * writer.close();
     * BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
     * StringBuilder builder = new StringBuilder(100);
     * String line;
     * while ((line = read.readLine()) != null)
     * builder.append(line);
     * read.close();
     * return builder.toString();
     * }
     */

    public String getApiKey() {
        return apiKey;
    }

    public String getBotroom() {
        return botroom;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
