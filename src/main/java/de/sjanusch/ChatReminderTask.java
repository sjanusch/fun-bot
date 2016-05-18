package de.sjanusch;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sjanusch.guice.GuiceModule;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.model.hipchat.HipchatMessage;
import de.sjanusch.texte.TextHandler;

import java.util.TimerTask;

/**
 * Created by Sandro Janusch
 * Date: 16.05.16
 * Time: 20:54
 */
public class ChatReminderTask extends TimerTask {

    private HipchatRequestHandler hipchatRequestHandler;

    private TextHandler textHandler;

    public ChatReminderTask() {
        final Injector injector = Guice.createInjector(new GuiceModule());
        this.hipchatRequestHandler = injector.getInstance(HipchatRequestHandler.class);
        this.textHandler = injector.getInstance(TextHandler.class);
    }

    @Override
    public void run() {
        hipchatRequestHandler.sendNotification(new HipchatMessage(textHandler.getRandomText(""), "text"));
    }

}
