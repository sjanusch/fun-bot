package de.sjanusch;

import com.google.inject.Inject;
import de.sjanusch.bot.Bot;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.runner.RunnableBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LunchBot implements RunnableBot {

    private static final Logger logger = LoggerFactory.getLogger(LunchBot.class);

    private final Bot bot;

    private final HipchatRequestHandler hipchatRequestHandler;

    @Inject
    public LunchBot(final Bot bot, final HipchatRequestHandler hipchatRequestHandler) {
        this.bot = bot;
        this.hipchatRequestHandler = hipchatRequestHandler;
    }

    public void run() {
        bot.run();
        LunchReminderTask lunchReminderTask = new LunchReminderTask(hipchatRequestHandler);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(lunchReminderTask, this.getHoursUntilTarget(10), 24, TimeUnit.HOURS);
    }

    private int getHoursUntilTarget(int targetHour) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < targetHour ? targetHour - hour : targetHour - hour + 24;
    }
}
