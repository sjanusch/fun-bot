package de.sjanusch.protocol;

import org.alicebot.ab.Chat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sandro Janusch
 * Date: 18.05.16
 * Time: 21:39
 */
public class MessageProtocolImpl implements MessageProtocol {

  private Map<String, Chat> messageProtocol = new HashMap<>();

  private boolean talkMode = true;

  public void addFlowForUser(final String username, final Chat chat) {
    if (!messageProtocol.containsKey(username)) {
      messageProtocol.put(username, chat);
    }
  }

  public void removeFlowForUser(final String username) {
    if (messageProtocol.containsKey(username)) {
      messageProtocol.remove(username);
    }
  }

  public Chat getCurrentFlowForUser(final String username) {
    if (messageProtocol.containsKey(username)) {
      final Chat chat = messageProtocol.get(username);
      return chat;
    }
    return null;
  }

  public boolean isTalkMode() {
    return talkMode;
  }

  public void setTalkMode(final boolean talkMode) {
    this.talkMode = talkMode;
  }
}
