package de.sjanusch;

import com.google.inject.Inject;
import de.sjanusch.bot.Bot;
import de.sjanusch.runner.RunnableBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatBot implements RunnableBot {

    private static final Logger logger = LoggerFactory.getLogger(ChatBot.class);

    private final Bot bot;

    @Inject
    public ChatBot(final Bot bot) {
        this.bot = bot;
    }

    public void run() {
        bot.run();
        ChatReminderTask botReminderTask = new ChatReminderTask();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(botReminderTask, 5, 30, TimeUnit.MINUTES);
    }
}
