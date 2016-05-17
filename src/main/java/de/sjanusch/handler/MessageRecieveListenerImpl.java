package de.sjanusch.handler;

import com.google.inject.Inject;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.data.ConstantTexts;
import de.sjanusch.eventsystem.EventHandler;
import de.sjanusch.eventsystem.events.model.MessageRecivedEvent;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.model.Room;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 07:00
 */
public class MessageRecieveListenerImpl implements MessageRecieveListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageRecieveListenerImpl.class);

    private final HipchatRequestHandler hipchatRequestHandler;

    private final ConstantTexts constantTexts;

    private final BotConfiguration botConfiguration;

    @Inject
    public MessageRecieveListenerImpl(final HipchatRequestHandler hipchatRequestHandler, final ConstantTexts constantTexts, final BotConfiguration botConfiguration) {
        this.hipchatRequestHandler = hipchatRequestHandler;
        this.constantTexts = constantTexts;
        this.botConfiguration = botConfiguration;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void messageEvent(final MessageRecivedEvent event) {
        try {
            handleMessage(event.getMessage(), event.from(), event.getRoom());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(final String text) {
        hipchatRequestHandler.sendMessage(text);

    }

    private void handleMessage(final Message message, final String from, final Room room) throws JSONException, ParseException, IOException {
        if (!from.equals(botConfiguration.getBotNickname())) {
            logger.debug("Handle Message from " + from + ": " + message.getBody());
            final String talkTo = this.convertNames(message.getBody(), from);
            if (this.checkContentHello(message.getBody())) {
                this.handleHellodMessages(talkTo);
            } else {
                this.handlenoRandomText(talkTo, message.getBody());
            }
        }
    }

    private void handlenoRandomText(final String talkTo, final String message) {
        StringBuilder stringBuilder = new StringBuilder();
        String text = constantTexts.getRandomText(message);
        if (text != null) {
            stringBuilder.append(talkTo + constantTexts.getRandomText(message));
            this.sendMessage(stringBuilder.toString());
        }
    }

    private void handleHellodMessages(final String talkTo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(talkTo + constantTexts.getHelloText());
        this.sendMessage(stringBuilder.toString());
    }

    private boolean checkContentHello(final String content) {
        final String lowerCaseContent = content.toLowerCase().trim();
        if (lowerCaseContent.contains("hallo") || lowerCaseContent.contains("hello")
            || lowerCaseContent.contains("servus") || lowerCaseContent.contains("guten tag")) {
            return true;
        }
        return false;
    }

    private String convertNames(final String message, final String from) {
        if (message.toLowerCase().contains("@lunchbot")) {
            String[] names = from.split(" ");
            String newName = null;
            if (names.length > 1) {
                newName = names[0].toLowerCase().charAt(0) + names[1].toLowerCase();
            }
            return ("@" + newName + " ");
        }
        return "";
    }

}
