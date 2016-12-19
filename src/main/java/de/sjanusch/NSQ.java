package de.sjanusch;

import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.google.inject.Inject;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.configuration.NSQConfiguration;
import de.sjanusch.listener.MessageRecieveListener;
import de.sjanusch.model.nsq.NsqPublicMessage;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Sandro Janusch
 * Date: 14.12.16
 * Time: 14:15
 */
public class NSQ implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(NSQ.class);

  private final NSQConfiguration nsqConfiguration;

  private final BotConfiguration botConfiguration;

  private final ObjectMapper mapper = new ObjectMapper();

  private final MessageRecieveListener messageRecieveListener;

  @Inject
  public NSQ(final NSQConfiguration nsqConfiguration, final BotConfiguration botConfiguration, final MessageRecieveListener messageRecieveListener) {
    this.nsqConfiguration = nsqConfiguration;
    this.botConfiguration = botConfiguration;
    this.messageRecieveListener = messageRecieveListener;
  }

  @Override
  public void run() {
    Thread nsqFunThread = this.nsqFunThread();
    try {
      nsqFunThread.join();
    } catch (InterruptedException e) {
      logger.error("InterruptedException: " + e.getMessage());
    }
    nsqFunThread.start();
    logger.debug("NSQ Fun started");
  }

  private Thread nsqFunThread() {
    Thread threadNsq = new Thread() {

      @Override
      public void run() {
        startNsqFun();
      }
    };
    return threadNsq;
  }

  private void startNsqFun() {
    try {
      final NSQLookup lookup = new DefaultNSQLookup();
      lookup.addLookupAddress(nsqConfiguration.getNSQLookupAdress(), nsqConfiguration.getNSQLookupAdressPort());
      final NSQConsumer consumer = new NSQConsumer(lookup, nsqConfiguration.getNsqTopicName(), botConfiguration.getBotNickname(), (message) -> {
        try {
          if (messageToString(message).equals("ping")) {
            logger.debug(nsqConfiguration.getNsqTopicName() + " Queue is alife.");
            finishMessage(message, true);
          } else {
            logger.debug("received message: " + nsqConfiguration.getNsqTopicName() + ": " + messageToString(message));
            final NsqPublicMessage nsqPublicMessage = mapper.readValue(messageToString(message), NsqPublicMessage.class);
            if (nsqPublicMessage.getText() != null && nsqPublicMessage.getFullName() != null && nsqPublicMessage.getRoom() != null) {
              finishMessage(message, messageRecieveListener.handleMessage(nsqPublicMessage.getText(), nsqPublicMessage.getFullName(), nsqPublicMessage.getRoom()));
            } else {
              finishMessage(message, true);
            }
          }
        } catch (IOException e) {
          logger.error("IOException: " + e.getMessage());
        }
      });
      consumer.start();
    } catch (IOException e) {
      logger.error("IOException: " + e.getMessage());
    }
  }

  private String messageToString(final NSQMessage message) {
    return new String(message.getMessage(), StandardCharsets.UTF_8);
  }

  private void finishMessage(final NSQMessage message, final boolean result) {
    if (result) {
      message.finished();
    } else {
      message.requeue();
    }
  }
}

