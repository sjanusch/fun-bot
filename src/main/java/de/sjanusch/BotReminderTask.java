package de.sjanusch;

import de.sjanusch.data.ConstantTextsImpl;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;

import java.util.TimerTask;

/**
 * Created by Sandro Janusch
 * Date: 16.05.16
 * Time: 20:54
 */
public class BotReminderTask extends TimerTask {

    private final HipchatRequestHandler hipchatRequestHandler;

    private ConstantTextsImpl constantTexts = new ConstantTextsImpl();

    public BotReminderTask(final HipchatRequestHandler hipchatRequestHandler) {
        this.hipchatRequestHandler = hipchatRequestHandler;
    }

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(constantTexts.getRandomText(""));
        hipchatRequestHandler.sendMessage(stringBuilder.toString());
    }
}
