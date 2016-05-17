package de.sjanusch.networking;

import com.google.inject.Inject;
import de.sjanusch.bot.Bot;
import de.sjanusch.configuration.ChatConnectionConfiguration;
import de.sjanusch.eventsystem.EventSystem;
import de.sjanusch.eventsystem.events.model.MessageRecivedEvent;
import de.sjanusch.model.HipchatRoomInfo;
import de.sjanusch.model.Room;
import de.sjanusch.networking.exceptions.LoginException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Body;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class ConnectionImpl implements Connection, MessageListener, ConnectionListener {

    private final ChatConnectionConfiguration chatConnectionConfiguration;

    private XMPPConnection xmpp;

    private boolean connected;

    private String password;

    private HashMap<Room, MultiUserChat> rooms = new HashMap<Room, MultiUserChat>();

    private HashMap<String, Chat> cache = new HashMap<String, Chat>();

    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);

    private final EventSystem eventSystem;

    private final Bot bot;

    private MultiUserChat chat;

    @Inject
    public ConnectionImpl(final ChatConnectionConfiguration chatConnectionConfiguration, final EventSystem eventSystem, final Bot bot) throws IOException {
        this.chatConnectionConfiguration = chatConnectionConfiguration;
        this.eventSystem = eventSystem;
        this.bot = bot;
        this.xmpp = new XMPPConnection(new ConnectionConfiguration(chatConnectionConfiguration.getXmppUrl(), chatConnectionConfiguration.getXmppPort()));
    }

    public void connect() throws XMPPException {
        if (connected)
            return;
        xmpp.connect();
        xmpp.addConnectionListener(this);
        connected = true;
    }
    
    public void login(String username, String password) throws LoginException {
        if (!connected)
            return;
        if (!username.contains("hipchat.com"))
            logger.error("The username being used does not look like a Jabber ID. Are you sure this is the correct username?");
        try {
            xmpp.login(username, password);
        } catch (XMPPException exception) {
            throw new LoginException("There was an error logging in! Are you using the correct username/password?", exception);
        }
        this.password = password;
    }
    
    public void sendPM(String message, String to) throws XMPPException {
        Chat c;
        if (cache.containsKey(to))
            c = cache.get(to);
        else {
            c = xmpp.getChatManager().createChat(to, this);
            cache.put(to, c);
        }
        c.sendMessage(message);
    }
    
    public void joinRoom(String room, String nickname) throws XMPPException, IOException {
        joinRoom("", room, nickname);
    }

    /*
    public void joinRoom(String APIKey, String room, String nickname) throws XMPPException {
        if (!connected || nickname.equals("") || password.equals("") || rooms.containsKey(room))
            return;
        MultiUserChat muc2;
        if (!isJID(room)) {
            Room temp = Room.createRoom(APIKey, room);
            room = temp.getHipchatRoomInfo(APIKey).getJID();
            muc2 = new MultiUserChat(XMPP, room);
            temp = null;
        } else
            muc2 = new MultiUserChat(XMPP, (room.indexOf("@") != -1 ? room : room + "@" + CONF_URL));
        muc2.join(nickname, password);
        final Room obj = Room.createRoom(APIKey, room, muc2, XMPP);
        muc2.addMessageListener(new PacketListener() {

            @Override
            public void processPacket(Packet paramPacket) {
                Message m = new Message();
                m.setBody(toMessage(paramPacket));
                m.setFrom(paramPacket.getFrom().split("\\/")[1]);
                MessageRecivedEvent event = new MessageRecivedEvent(obj, m);
                eventSystem.callEvent(event);
            }
        });
        rooms.put(obj, muc2);
    }

    */

    public void joinRoom(String APIKey, String room, String nickname) throws XMPPException, IOException {
        if (!connected || nickname.equals("") || password.equals(""))
            return;
        if (!isJID(room)) {
            chat = new MultiUserChat(xmpp, room);
        } else {
            chat = new MultiUserChat(xmpp, (room.indexOf("@") != -1 ? room : room + "@" + chatConnectionConfiguration.getConfUrl()));
            chat.join(nickname, password);
            final Room obj = createRoom(APIKey, room, chat, xmpp);
            chat.addMessageListener(new PacketListener() {

                @Override
                public void processPacket(Packet paramPacket) {
                    Message m = new Message();
                    m.setBody(toMessage(paramPacket));
                    m.setFrom(paramPacket.getFrom().split("\\/")[1]);
                    MessageRecivedEvent event = new MessageRecivedEvent(obj, m);
                    eventSystem.callEvent(event);
                }
            });
            rooms.put(obj, chat);
        }
    }

    private Room createRoom(String APIKey, String name, MultiUserChat chat, XMPPConnection con) {
        Room r = createRoom2(name, chat, con);
        if (APIKey != null && !APIKey.equals(""))
            r.hinfo = HipchatRoomInfo.getInfo(APIKey, r);
        r.api_cache = APIKey;
        return r;
    }

    public Room createRoom2(String name, MultiUserChat chat, XMPPConnection con) {
        final Room r = new Room(this.bot, this.eventSystem);
        r.setName(name);
        r.setChat(chat);

        try {
            r.info = MultiUserChat.getRoomInfo(con, (name.indexOf("@") != -1 ? name : name + "@" + chatConnectionConfiguration.getConfUrl()));
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        r.subject = r.info.getSubject();
        chat.addSubjectUpdatedListener(new SubjectUpdatedListener() {

            public void subjectUpdated(String newsubject, String from) {
                r.subject = newsubject;
            }
        });
        for (String user : r.getConnectedUsers()) {
            r.users.add(user);
        }
        r.startThread();
        return r;
    }

    public Room findRoom(final String name, final String apiKey) {
        Room r = this.findConnectedRoom(name);
        if (r == null) {
            for (Room room : this.getRooms()) {
                if (room.getTrueName(apiKey).equals(name))
                    return room;
            }
            return null;
        } else
            return r;
    }

    public MultiUserChat getChat() {
        return chat;
    }

    public List<Room> getRooms() {
        ArrayList<Room> roomlist = new ArrayList<Room>();
        for (Room room : rooms.keySet()) {
            roomlist.add(room);
        }
        return Collections.unmodifiableList(roomlist);
    }
    
    public boolean sendMessageToRoom(String room, String message, String nickname) {
        if (!rooms.containsKey(room)) {
            try {
                joinRoom(room, nickname);
            } catch (XMPPException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        Room obj;
        if ((obj = findConnectedRoom(room)) != null)
            return obj.sendMessage(message, nickname);
        return false;
    }
    
    public Room findConnectedRoom(String name) {
        for (Room r : getRooms()) {
            if (r.getXMPPName().equals(name))
                return r;
        }
        return null;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void disconnect() {
        if (!connected)
            return;
        xmpp.disconnect();
        connected = false;
    }
    
    public Roster getRoster() {
        return xmpp.getRoster();
    }
    
    @Override
    public void processMessage(Chat arg0, Message arg1) {
        MessageRecivedEvent event = new MessageRecivedEvent(null, arg1);
        eventSystem.callEvent(event);
    }

    @Override
    public void connectionClosed() {
        connected = false;
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        connected = false;
    }

    @Override
    public void reconnectingIn(int seconds) {
        
    }

    @Override
    public void reconnectionFailed(Exception e) {
        if (connected)
            connected = false;
    }

    @Override
    public void reconnectionSuccessful() {
        if (!connected)
            connected = true;
    }
    
    public synchronized void waitForEnd() throws InterruptedException {
        while (true) {
            if (!connected)
                break;
            super.wait(0L);
        }
    }
    
    private boolean isJID(String name) {
        try {
            Integer.parseInt(name.split("\\_")[0]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private String toMessage(Packet packet) {
        try {
            Field f = packet.getClass().getDeclaredField("bodies");
            f.setAccessible(true);
            @SuppressWarnings("rawtypes")
            HashSet h = (HashSet) f.get(packet);
            if (h.size() == 0)
                return "";
            for (Object obj : h) {
                if (obj instanceof Body)
                    return ((Body) obj).getMessage();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
        
    }
}
