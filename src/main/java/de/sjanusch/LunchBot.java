package de.sjanusch;

import com.google.inject.Inject;
import de.sjanusch.bot.Bot;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.networking.Connection;
import de.sjanusch.runner.RunnableBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LunchBot implements RunnableBot {

    private static final Logger logger = LoggerFactory.getLogger(LunchBot.class);

    private final Bot bot;

    private final Connection connection;

    private final HipchatRequestHandler hipchatRequestHandler;

    @Inject
    public LunchBot(final Bot bot, final Connection connection, final HipchatRequestHandler hipchatRequestHandler) {
        this.bot = bot;
        this.connection = connection;
        this.hipchatRequestHandler = hipchatRequestHandler;
    }

    public void run() {
        bot.run();

        /*
        Thread t = new Thread() {

            public void run() {
                while (true) {
                    try {
                        String text = Constants.getRandomText("");
                        if (text != null) {
                            //hipchatRequestHandler.sendMessage(text);
                        }
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        */
    }

}
