package de.sjanusch;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sjanusch.bot.Bot;
import de.sjanusch.guice.GuiceModule;
import de.sjanusch.nsq.NSQProducer;
import de.sjanusch.runner.BotRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new GuiceModule());
        final Bot bot = injector.getInstance(Bot.class);
        final BotRunner botRunner = injector.getInstance(BotRunner.class);
        final Thread t = botRunner.runBotDesync(bot);

        NSQProducer nsqProducer = new NSQProducer("Test");
        try {
            nsqProducer.put("Hallo");
        } catch (Exception e) {
            logger.error("NSQProducer: " + e.getMessage());
        }

        t.start();
    }

}
