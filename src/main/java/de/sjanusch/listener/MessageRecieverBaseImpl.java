package de.sjanusch.listener;

import com.google.inject.Inject;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.model.hipchat.HipchatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Sandro Janusch
 * Date: 18.05.16
 * Time: 20:42
 */
public class MessageRecieverBaseImpl implements MessageRecieverBase {

  private static final Logger logger = LoggerFactory.getLogger(MessageRecieverBaseImpl.class);

  private final HipchatRequestHandler hipchatRequestHandler;

  private final BotConfiguration botConfiguration;

  @Inject
  public MessageRecieverBaseImpl(final HipchatRequestHandler hipchatRequestHandler, final BotConfiguration botConfiguration) {
    this.hipchatRequestHandler = hipchatRequestHandler;
    this.botConfiguration = botConfiguration;
  }

  @Override
  public void sendMessageText(final String text, final String username) {
    if (text != null && username != null) {
      hipchatRequestHandler.sendMessage(new HipchatMessage("@" + username + " " + text));
    }
  }

  @Override
  public void sendNotification(final String text, final String username) {
    if (text != null && username != null) {
      hipchatRequestHandler.sendMessage(new HipchatMessage(text, "html"));
    }
  }

  @Override
  public void sendNotificationError(final String text, final String username) {
    if (text != null && username != null) {
      final HipchatMessage hipchatMessage = new HipchatMessage(text, "html");
      hipchatMessage.setColor("red");
      hipchatRequestHandler.sendMessage(hipchatMessage);
    }
  }

  @Override
  public void sendNotificationSucess(final String text, final String username) {
    if (text != null && username != null) {
      final HipchatMessage hipchatMessage = new HipchatMessage(text, "html");
      hipchatMessage.setColor("green");
      hipchatRequestHandler.sendMessage(hipchatMessage);
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
