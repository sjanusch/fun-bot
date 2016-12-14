package de.sjanusch.listener;

import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.NSQProducer;
import com.github.brainlag.nsq.exceptions.NSQException;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.google.inject.Inject;
import de.sjanusch.configuration.NSQConfiguration;
import de.sjanusch.eventsystem.EventHandler;
import de.sjanusch.eventsystem.events.model.MessageRecivedEvent;
import de.sjanusch.helper.MessageHelper;
import de.sjanusch.protocol.MessageProtocol;
import de.sjanusch.texte.TextHandler;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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

  private final NSQConfiguration nsqConfiguration;

  @Inject
  public MessageRecieveListenerImpl(final TextHandler textHandler, final MessageRecieverBase messageRecieverBase, final MessageHelper messageHelper, final MessageProtocol messageProtocol, final NSQConfiguration nsqConfiguration) {
    this.textHandler = textHandler;
    this.messageRecieverBase = messageRecieverBase;
    this.messageHelper = messageHelper;
    this.messageProtocol = messageProtocol;
    this.nsqConfiguration = nsqConfiguration;
  }

  @SuppressWarnings("unused")
  @EventHandler
  @Override
  public void messageEvent(final MessageRecivedEvent event) {
    try {
      final String from = event.from();
      final String message = event.getMessage().getBody().toLowerCase().trim();
      if (!messageRecieverBase.isMessageFromBot(from)) {
        handleMessage(event.getMessage(), from, event.getRoom().getXMPPName());
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void handleMessage(final Message chatMessage, final String from, final String roomId) throws IOException {
    final String incomeMessage = messageRecieverBase.extractMessage(chatMessage.getBody().toLowerCase().trim());
    final String actualUser = messageHelper.convertNames(from);
    final Chat chat = messageProtocol.getCurrentFlowForUser(actualUser);

    if (incomeMessage.equals("reader")) {
      NSQLookup lookup = new DefaultNSQLookup();
      logger.debug("NSQLookup: " + nsqConfiguration.getNSQLookupAdress() + ":" + nsqConfiguration.getNSQLookupAdressPort());
      lookup.addLookupAddress(nsqConfiguration.getNSQLookupAdress(), nsqConfiguration.getNSQLookupAdressPort());
      NSQConsumer consumer = new NSQConsumer(lookup, "TestTopic", "dustin", (message) -> {
        logger.debug("received: " + message);
        //now mark the message as finished.
        message.finished();

        //or you could requeue it, which indicates a failure and puts it back on the queue.
        //message.requeue();
      });

      consumer.start();

    }

    if (incomeMessage.equals("write")) {
      logger.debug("NSQProducer: " + nsqConfiguration.getNSQAdress() + ":" + nsqConfiguration.getNSQAdressPort());
      NSQProducer producer = new NSQProducer().addAddress(nsqConfiguration.getNSQAdress(), nsqConfiguration.getNSQAdressPort()).start();
      try {
        producer.produce("TestTopic", ("this is a message").getBytes());
      } catch (NSQException e) {
        logger.error("NSQException: " + e.getMessage());
      } catch (TimeoutException e) {
        logger.error("TimeoutException: " + e.getMessage());
      }
    }







    /*
    logger.debug("Handle Message from " + actualUser + ": " + incomeMessage);

    if (chat != null) {
      messageRecieverBase.sendNotification(chat.chat(incomeMessage), roomId);
    } else {
      final Chat newChat = new Chat(bot);
      messageProtocol.addFlowForUser(actualUser, newChat);
      messageRecieverBase.sendNotification(newChat.chat(incomeMessage), roomId);
    }
    */
    return;
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
