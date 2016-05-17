package de.sjanusch;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sjanusch.texte.TextHandler;
import de.sjanusch.guice.GuiceModule;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;

import java.util.TimerTask;

/**
 * Created by Sandro Janusch
 * Date: 16.05.16
 * Time: 20:54
 */
public class BotReminderTask extends TimerTask {

    private HipchatRequestHandler hipchatRequestHandler;

    private TextHandler constantTexts;

    public BotReminderTask() {
        final Injector injector = Guice.createInjector(new GuiceModule());
        this.hipchatRequestHandler = injector.getInstance(HipchatRequestHandler.class);
        this.constantTexts = injector.getInstance(TextHandler.class);
    }

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(constantTexts.getRandomText(""));
        hipchatRequestHandler.sendMessage(stringBuilder.toString());
    }
}
