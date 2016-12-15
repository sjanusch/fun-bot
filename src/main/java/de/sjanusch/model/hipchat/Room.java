package de.sjanusch.model.hipchat;

import com.google.inject.Inject;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Room {

  public static final String CONF_URL = "conf.hipchat.com";

  private static final Logger logger = LoggerFactory.getLogger(Room.class);

  public RoomInfo info;

  public String subject;

  public HipchatRoomInfo hinfo;

  public ArrayList<String> users = new ArrayList<String>();

  public String api_cache;

  private MultiUserChat chat;

  private String name;

  private int lastcount;

  private Thread joinchecker;

  private boolean halt;

  @Inject
  public Room() {
  }

  public void setChat(final MultiUserChat chat) {
    this.chat = chat;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void startThread() {
    joinchecker = new JoinLookout();
    joinchecker.start();
  }

  private void stopThread() {
    halt = true;
    joinchecker.interrupt();
    try {
      joinchecker.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void disconnect() {
    stopThread();
    //TODO Disconnect
  }

  public int getUserCount() {
    if (!isConnected())
      return -1;
    return chat.getOccupantsCount();
  }

  public boolean isConnected() {
    return chat != null;
  }

  public String getXMPPName() {
    return (name.indexOf("@") != -1 ? name.split("\\@")[0] : name);
  }

  public String getXMPP_JID() {
    return (name.indexOf("@") != -1 ? name : name + "@" + CONF_URL);
  }

  public String getTrueName(String APIKey) {
    if (hinfo == null) {
      hinfo = HipchatRoomInfo.getInfo(APIKey, this);
      if (hinfo == null)
        return null;
    }
    return hinfo.getRoomName();
  }

  public String getTrueName() {
    if (hinfo == null)
      return null;
    return hinfo.getRoomName();
  }

  public HipchatRoomInfo getHipchatRoomInfo() {
    return hinfo;
  }

  public HipchatRoomInfo getHipchatRoomInfo(String APIKey) {
    if (hinfo == null) {
      hinfo = HipchatRoomInfo.getInfo(APIKey, this);
      if (hinfo == null)
        return null;
    }
    return hinfo;
  }

  public String getSubject() {
    if (subject == null || subject.equals("")) {
      if (info != null)
        subject = info.getSubject();
      else if (hinfo != null)
        subject = hinfo.getTopic();
    }
    return subject;
  }

  public boolean setSubject(String subject) {
    if (chat == null)
      return false;
    try {
      chat.changeSubject(subject);
    } catch (XMPPException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public List<String> getConnectedUsers() {
    Iterator<String> temp = chat.getOccupants();
    List<String> copy = new ArrayList<String>();
    while (temp.hasNext())
      copy.add(temp.next());
    return Collections.unmodifiableList(copy);
  }

  public boolean sendMessage(String message, String from) {
    if (chat == null)
      return false;
    try {
      chat.sendMessage(message);
      return true;
    } catch (XMPPException e) {
      e.printStackTrace();
    }
    return false;
  }

  private class JoinLookout extends Thread {

    @Override
    public void run() {
      ArrayList<String> toremove = new ArrayList<String>();
      while (isConnected()) {
        toremove.clear();
        if (halt)
          continue;
        if (getUserCount() != lastcount) {
          List<String> connected = getConnectedUsers();
          for (String nick : connected) {
            if (!users.contains(nick)) { //connected
              HipchatUser user = null;
              if (api_cache != null && !api_cache.equals(""))
                user = HipchatUser.createInstance(nick.split("\\/")[1], api_cache);
              users.add(nick);
              lastcount = getUserCount();
              logger.debug("Room run");
            }
          }

          for (String nick : users) {
            if (!connected.contains(nick)) { //disconnected
              HipchatUser user = null;
              if (api_cache != null && !api_cache.equals(""))
                user = HipchatUser.createInstance(nick.split("\\/")[1], api_cache);
              toremove.add(nick);
              lastcount = getUserCount();
            }
          }

          for (String nick : toremove) {
            users.remove(nick);
          }
        }

        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }
    }
  }

}
