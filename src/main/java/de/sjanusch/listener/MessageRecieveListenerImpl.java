package de.sjanusch.listener;

import com.google.inject.Inject;
import de.sjanusch.eventsystem.EventHandler;
import de.sjanusch.eventsystem.events.model.MessageRecivedEvent;
import de.sjanusch.helper.MessageHelper;
import de.sjanusch.protocol.MessageProtocol;
import de.sjanusch.redis.Redis;
import de.sjanusch.texte.TextHandler;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 07:00
 */
public class MessageRecieveListenerImpl implements MessageRecieveListener {

  private static final Logger logger = LoggerFactory.getLogger(MessageRecieveListenerImpl.class);

  private final TextHandler textHandler;

  private final MessageRecieverBase messageRecieverBase;

  private final MessageHelper messageHelper;

  private final MessageProtocol messageProtocol;

  private final Bot bot = new Bot();

  Redis redis = new Redis();

  @Inject
  public MessageRecieveListenerImpl(final TextHandler textHandler, final MessageRecieverBase messageRecieverBase, final MessageHelper messageHelper, final MessageProtocol messageProtocol) {
    this.textHandler = textHandler;
    this.messageRecieverBase = messageRecieverBase;
    this.messageHelper = messageHelper;
    this.messageProtocol = messageProtocol;
  }

  @SuppressWarnings("unused")
  @EventHandler
  @Override
  public void messageEvent(final MessageRecivedEvent event) {
    try {
      final String from = event.from();
      if (!messageRecieverBase.isMessageFromBot(from)) {
        handleMessage(event.getMessage(), from);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void handleMessage(final Message message, final String from) throws IOException {
    final String incomeMessage = message.getBody().toLowerCase().trim();
    final String actualUser = messageHelper.convertNames(from);
    final Chat chat = messageProtocol.getCurrentFlowForUser(actualUser);

    //redis.startRedis();

    logger.debug("Handle Message from " + actualUser + ": " + incomeMessage);

    if (incomeMessage.equals("ich")) {
      messageRecieverBase.sendNotification(textHandler.getRandomText(incomeMessage));
      return;
    }

    if (chat != null) {
      messageRecieverBase.sendNotification(chat.chat(incomeMessage));
    } else {
      final Chat newChat = new Chat(bot);
      messageProtocol.addFlowForUser(actualUser, newChat);
      messageRecieverBase.sendNotification(newChat.chat(incomeMessage));
    }
    return;
  }

  private void handleRandomText(final String message, final String actualUser) {
    messageRecieverBase.sendMessageText(actualUser, textHandler.getRandomText(message));
  }

  private void handleHelloMessages(final String message, final String actualUser) {
    if (messageRecieverBase.isMessageForBot(message)) {
      messageRecieverBase.sendMessageText(actualUser, textHandler.getHelloText());
    } else {
      messageRecieverBase.sendMessageText(textHandler.getHelloText(), actualUser);
    }
  }

  private void handleByeMessages(final String message, final String actualUser) {
    if (messageRecieverBase.isMessageForBot(message)) {
      messageRecieverBase.sendMessageText(actualUser, textHandler.getByeText());
    } else {
      messageRecieverBase.sendMessageText(textHandler.getByeText(), actualUser);
    }
  }

  private void handleThankYouMessages(final String message, final String actualUser) {
    if (messageRecieverBase.isMessageForBot(message)) {
      messageRecieverBase.sendMessageText(actualUser, textHandler.getPleaseText());
    } else {
      messageRecieverBase.sendMessageText(textHandler.getPleaseText(), actualUser);
    }
  }

  private void handlePleaseMessages(final String message, final String actualUser) {
    if (messageRecieverBase.isMessageForBot(message)) {
      messageRecieverBase.sendMessageText(actualUser, textHandler.getThankYouText());
    } else {
      messageRecieverBase.sendMessageText(textHandler.getThankYouText(), actualUser);
    }
  }

}
