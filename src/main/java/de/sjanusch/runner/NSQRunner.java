package de.sjanusch.runner;

import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.google.inject.Inject;
import de.sjanusch.configuration.NSQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Sandro Janusch
 * Date: 14.12.16
 * Time: 12:07
 */
public class NSQRunner {

  private static final Logger logger = LoggerFactory.getLogger(NSQRunner.class);

  private final NSQConfiguration nsqConfiguration;

  @Inject
  public NSQRunner(final NSQConfiguration nsqConfiguration) {
    this.nsqConfiguration = nsqConfiguration;
  }

  public void run(String name, String text) throws IOException {

    final NSQLookup lookup = new DefaultNSQLookup();
    lookup.addLookupAddress(nsqConfiguration.getNSQLookupAdress(), nsqConfiguration.getNSQLookupAdressPort());
    NSQConsumer consumer = new NSQConsumer(lookup, "TestTopic", name, (message) -> {
      logger.debug("received: " + message);

      logger.debug("Test HAllo : " + new String(message.getMessage(), StandardCharsets.UTF_8));

      //now mark the message as finished.
      message.finished();

      //or you could requeue it, which indicates a failure and puts it back on the queue.
      //message.requeue();
    });

    consumer.start();
  }

}
