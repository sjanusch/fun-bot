package de.sjanusch;

import de.sjanusch.hipchat.handler.HipchatRequestHandler;

import java.util.TimerTask;

/**
 * Created by Sandro Janusch
 * Date: 16.05.16
 * Time: 20:54
 */
public class BotReminderTask extends TimerTask {

    private final HipchatRequestHandler hipchatRequestHandler;

    public BotReminderTask(final HipchatRequestHandler hipchatRequestHandler) {
        this.hipchatRequestHandler = hipchatRequestHandler;
    }

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@all Mittagessen Anmeldung nicht vergessen!");
        stringBuilder.append("\n");
        stringBuilder.append("https://confluence.rp.seibert-media.net/dashboard.action");
        hipchatRequestHandler.sendMessage(stringBuilder.toString());
    }
}
