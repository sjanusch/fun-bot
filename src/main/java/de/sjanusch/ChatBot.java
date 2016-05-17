package de.sjanusch;

import com.google.inject.Inject;
import de.sjanusch.bot.Bot;
import de.sjanusch.runner.RunnableBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
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
        scheduler.schedule(botReminderTask, 60, TimeUnit.MINUTES);
    }

    private int getHoursUntilTarget(int targetHour) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < targetHour ? targetHour - hour : targetHour - hour + 24;
    }
}
