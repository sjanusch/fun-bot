package de.sjanusch.bot;

import com.google.inject.Inject;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.eventsystem.EventSystem;
import de.sjanusch.listener.MessageRecieveListener;
import de.sjanusch.networking.ChatClient;
import de.sjanusch.networking.Connection;
import de.sjanusch.networking.exceptions.LoginException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BotImpl implements Bot {

  private static final Logger logger = LoggerFactory.getLogger(BotImpl.class);

  private final EventSystem eventSystem;

  private final Connection connection;

  private final MessageRecieveListener messageRecieveListener;

  private final BotConfiguration botConfiguration;

  private final ChatClient chatClient;

  @Inject
  public BotImpl(final EventSystem eventSystem, final Connection connection, final BotConfiguration botConfiguration,
                 final ChatClient chatClient, final MessageRecieveListener messageRecieveListener) {
    this.eventSystem = eventSystem;
    this.connection = connection;
    this.messageRecieveListener = messageRecieveListener;
    this.botConfiguration = botConfiguration;
    this.chatClient = chatClient;
  }

  @Override
  public void run() {
    this.eventSystem.registerEvents(messageRecieveListener);
    try {
      connection.connect();
      boolean loggedIn = false;
      boolean joined = false;
      if (connection.isConnected()) {
        loggedIn = chatClient.login(connection.getXmpp(), this.getUsername(), this.getPassword());
        if (loggedIn) {
          joined = chatClient.joinChat(connection.getXmpp(), this.getBotroom(), this.getNickname(), this.getPassword());
        }
      }
      logger.debug(this.getNickname() + " loggedin: " + loggedIn + " and joined: " + joined + " in Room " + this.getBotroom());
    } catch (final XMPPException | LoginException | IOException e) {
      logger.warn(e.getClass().getName(), e);
      try {
        connection.disconnect();
      } catch (final XMPPException e2) {
        logger.debug("disconnect failed", e2);
      }
    }
  }

  @Override
  public void startPrivateChat(final String username) {
    chatClient.startPrivateChat(username);
  }

  @Override
  public String getBotroom() throws IOException {
    return botConfiguration.getBotChatRoom();
  }

  @Override
  public String getNickname() throws IOException {
    return botConfiguration.getBotNickname();
  }

  @Override
  public String getPassword() throws IOException {
    return botConfiguration.getBotPassword();
  }

  @Override
  public String getUsername() throws IOException {
    return botConfiguration.getBotUsername();
  }
}
