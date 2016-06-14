package de.sjanusch.listener;

import com.google.inject.Inject;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.model.hipchat.HipchatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Sandro Janusch Date: 18.05.16 Time: 20:42
 */
public class PrivateMessageRecieverBaseImpl implements PrivateMessageRecieverBase {

  private static final Logger logger = LoggerFactory.getLogger(PrivateMessageRecieverBaseImpl.class);

  private final HipchatRequestHandler hipchatRequestHandler;

  private final BotConfiguration botConfiguration;

  @Inject
  public PrivateMessageRecieverBaseImpl(final HipchatRequestHandler hipchatRequestHandler, final BotConfiguration botConfiguration) {
    this.hipchatRequestHandler = hipchatRequestHandler;
    this.botConfiguration = botConfiguration;
  }

  @Override
  public void sendMessageText(final String text, final String username) {
    if (text != null && username != null) {
      hipchatRequestHandler.sendPrivateMessage(new HipchatMessage(text), username);
    }
  }

  @Override
  public void sendNotification(final String text, final String username) {
    if (text != null && username != null) {
      hipchatRequestHandler.sendPrivateMessage(new HipchatMessage(text, "html"), username);
    }
  }

  @Override
  public void sendNotificationError(final String text, final String username) {
    if (text != null && username != null) {
      final HipchatMessage hipchatMessage = new HipchatMessage(text, "html");
      hipchatMessage.setColor("red");
      hipchatRequestHandler.sendPrivateMessage(hipchatMessage, username);
    }
  }

  @Override
  public void sendNotificationSucess(final String text, final String username) {
    if (text != null && username != null) {
      final HipchatMessage hipchatMessage = new HipchatMessage(text, "html");
      hipchatMessage.setColor("green");
      hipchatRequestHandler.sendPrivateMessage(hipchatMessage, username);
    }
  }

  @Override
  public void sendMessageTextToRoom(final String text) {
    if (text != null) {
      hipchatRequestHandler.sendMessage(new HipchatMessage(text));
    }
  }

  @Override
  public boolean isMessageFromBot(final String from) {
    try {
      return from.toLowerCase().trim().equals(botConfiguration.getBotNickname().toLowerCase().trim());
    } catch (final IOException e) {
      logger.warn(e.getClass().getName(), e);
    }
    return false;
  }

  @Override
  public boolean isMessageForBot(final String message) {
    try {
      return message.toLowerCase().trim().contains("@" + botConfiguration.getBotMentionName().toLowerCase().trim());
    } catch (final IOException e) {
      logger.warn(e.getClass().getName(), e);
    }
    return false;
  }
}
