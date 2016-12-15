package de.sjanusch.protocol;

import org.alicebot.ab.Chat;

/**
 * Created by Sandro Janusch
 * Date: 18.05.16
 * Time: 21:39
 */
public interface MessageProtocol {

  void addFlowForUser(final String username, final Chat chat);

  void removeFlowForUser(final String username);

  Chat getCurrentFlowForUser(final String username);

  boolean isTalkMode();

  void setTalkMode(final boolean talkMode);

}
