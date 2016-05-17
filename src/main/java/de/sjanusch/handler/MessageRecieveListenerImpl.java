package de.sjanusch.handler;

import com.google.inject.Inject;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.eventsystem.EventHandler;
import de.sjanusch.eventsystem.events.model.MessageRecivedEvent;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.model.Room;
import de.sjanusch.texte.TextHandler;
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

    private final TextHandler textHandler;

    private final BotConfiguration botConfiguration;

    @Inject
    public MessageRecieveListenerImpl(final HipchatRequestHandler hipchatRequestHandler, final TextHandler textHandler, final BotConfiguration botConfiguration) {
        this.hipchatRequestHandler = hipchatRequestHandler;
        this.textHandler = textHandler;
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
                this.handleHelloMessages(talkTo);
            } else if (this.checkContentBye(message.getBody())) {
                this.handleByeMessages(talkTo);
            } else {
                this.handlenoRandomText(talkTo, message.getBody());
            }
        }
    }

    private void handlenoRandomText(final String talkTo, final String message) {
        StringBuilder stringBuilder = new StringBuilder();
        String text = textHandler.getRandomText(message);
        if (text != null) {
            stringBuilder.append(talkTo + text);
            this.sendMessage(stringBuilder.toString());
        }
    }

    private void handleHelloMessages(final String talkTo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(talkTo + textHandler.getHelloText());
        this.sendMessage(stringBuilder.toString());
    }

    private void handleByeMessages(final String talkTo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(talkTo + textHandler.getByeText());
        this.sendMessage(stringBuilder.toString());
    }

    private boolean checkContentHello(final String content) {
        final String lowerCaseContent = content.toLowerCase().trim();
        if (lowerCaseContent.contains("hallo") || lowerCaseContent.contains("hello") || lowerCaseContent.contains("tag")
            || lowerCaseContent.contains("servus") || lowerCaseContent.contains("guten tag") || lowerCaseContent.contains("hi")) {
            return true;
        }
        return false;
    }

    private boolean checkContentBye(final String content) {
        final String lowerCaseContent = content.toLowerCase().trim();
        if (lowerCaseContent.contains("bye") || lowerCaseContent.contains("tschüß") || lowerCaseContent.contains("tschüss")
            || lowerCaseContent.contains("tschö") || lowerCaseContent.contains("auf wiedersehen") || lowerCaseContent.contains("wiedersehen")) {
            return true;
        }
        return false;
    }

    private String convertNames(final String message, final String from) throws IOException {
        if (message.toLowerCase().contains("@" + botConfiguration.getBotMentionName())) {
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
