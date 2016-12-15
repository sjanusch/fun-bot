package de.sjanusch.listener;

import com.google.inject.Inject;
import de.sjanusch.helper.MessageHelper;
import de.sjanusch.protocol.MessageProtocol;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
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

  private final MessageRecieverBase messageRecieverBase;

  private final MessageHelper messageHelper;

  private final MessageProtocol messageProtocol;

  private final Bot bot = new Bot();

  @Inject
  public MessageRecieveListenerImpl(final MessageRecieverBase messageRecieverBase, final MessageHelper messageHelper, final MessageProtocol messageProtocol) {
    this.messageRecieverBase = messageRecieverBase;
    this.messageHelper = messageHelper;
    this.messageProtocol = messageProtocol;
  }

  @Override
  public boolean handleMessage(final String chatMessage, final String from, final String roomId) throws IOException {
    final String incomeMessage = messageRecieverBase.extractMessage(chatMessage.toLowerCase().trim());
    final String actualUser = messageHelper.convertNames(from);
    if (!messageRecieverBase.isMessageFromBot(from)) {
      if (incomeMessage.equals("/talkmode on")) {
        messageProtocol.setTalkMode(true);
        return true;
      }
      if (incomeMessage.equals("/talkmode off")) {
        messageProtocol.setTalkMode(false);
        return true;
      }
      if (messageProtocol.isTalkMode()) {
        final Chat chat = messageProtocol.getCurrentFlowForUser(actualUser);
        logger.debug("Handle Message from " + actualUser + ": " + incomeMessage);
        if (chat != null) {
          messageRecieverBase.sendNotification(chat.chat(incomeMessage), roomId);
        } else {
          final Chat newChat = new Chat(bot);
          messageProtocol.addFlowForUser(actualUser, newChat);
          messageRecieverBase.sendNotification(newChat.chat(incomeMessage), roomId);
        }
        return true;
      }
    }
    return true;
  }



  /*
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
  */

}
