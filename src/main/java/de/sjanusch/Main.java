package de.sjanusch;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sjanusch.bot.Bot;
import de.sjanusch.guice.GuiceModule;
import de.sjanusch.runner.BotRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.walkmod.nsq.NSQProducer;
import org.walkmod.nsq.exceptions.NSQException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new GuiceModule());
        final Bot bot = injector.getInstance(Bot.class);
        final BotRunner botRunner = injector.getInstance(BotRunner.class);
        final Thread t = botRunner.runBotDesync(bot);

        NSQProducer producer = new NSQProducer("http://localhost:4150", "testTopic");

        for(int i=0; i<100; i++) {
            try {
                String message = "{\"foo\":\"bar\"}";
                System.out.println("Sending: " + message);
                producer.put(message);
                Thread.sleep(1000);
            } catch (NSQException e) {
                logger.error("NSQException " + e.getMessage());
                System.exit(1);
            } catch (InterruptedException e) {
                logger.error("InterruptedException " + e.getMessage());
            }
        }

        t.start();
    }

}
