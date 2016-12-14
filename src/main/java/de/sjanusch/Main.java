package de.sjanusch;

import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.NSQProducer;
import com.github.brainlag.nsq.exceptions.NSQException;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sjanusch.bot.Bot;
import de.sjanusch.guice.GuiceModule;
import de.sjanusch.runner.BotRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new GuiceModule());
    final Bot bot = injector.getInstance(Bot.class);
    final BotRunner botRunner = injector.getInstance(BotRunner.class);
    final Thread t = botRunner.runBotDesync(bot);
    t.start();

    NSQProducer producer = new NSQProducer().addAddress("localhost", 4150).start();
    try {
      producer.produce("TestTopic", ("this is a message").getBytes());
    } catch (NSQException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }


    NSQLookup lookup = new DefaultNSQLookup();
    lookup.addLookupAddress("localhost", 4161);
    NSQConsumer consumer = new NSQConsumer(lookup, "speedtest", "dustin", (message) -> {
     logger.debug("received: " + message);
      //now mark the message as finished.
      message.finished();

      //or you could requeue it, which indicates a failure and puts it back on the queue.
      //message.requeue();
    });

    consumer.start();

  }

}
