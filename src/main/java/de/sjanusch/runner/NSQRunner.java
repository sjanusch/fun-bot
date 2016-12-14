package de.sjanusch.runner;

import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.NSQProducer;
import com.github.brainlag.nsq.exceptions.NSQException;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.google.inject.Inject;
import de.sjanusch.configuration.NSQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
    final NSQProducer producer = new NSQProducer().addAddress(nsqConfiguration.getNSQAdress(), nsqConfiguration.getNSQAdressPort()).start();
    final NSQLookup lookup = new DefaultNSQLookup();
    lookup.addLookupAddress(nsqConfiguration.getNSQLookupAdress(), nsqConfiguration.getNSQLookupAdressPort());
    NSQConsumer consumer = new NSQConsumer(lookup, "TestTopic", name, (message) -> {
      logger.debug("received: " + message);

      try {
        producer.produce("TestTopic", text.getBytes());
      } catch (NSQException e) {
        logger.error("NSQException: " + e.getMessage());
      } catch (TimeoutException e) {
        logger.error("TimeoutException: " + e.getMessage());
      }
      //now mark the message as finished.
      message.finished();

      //or you could requeue it, which indicates a failure and puts it back on the queue.
      //message.requeue();
    });

    consumer.start();
  }

}
