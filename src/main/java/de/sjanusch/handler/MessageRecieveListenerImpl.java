package de.sjanusch.handler;

import com.google.inject.Inject;
import de.sjanusch.confluence.handler.SuperlunchRequestHandler;
import de.sjanusch.data.ConstantTexts;
import de.sjanusch.eventsystem.EventHandler;
import de.sjanusch.eventsystem.events.model.MessageRecivedEvent;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.model.Room;
import de.sjanusch.model.superlunch.Lunch;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 07:00
 */
public class MessageRecieveListenerImpl implements MessageRecieveListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageRecieveListenerImpl.class);

    private final HipchatRequestHandler hipchatRequestHandler;

    private final SuperlunchRequestHandler superlunchRequestHandler;

    private final ConstantTexts constantTexts;

    @Inject
    public MessageRecieveListenerImpl(final HipchatRequestHandler hipchatRequestHandler, final SuperlunchRequestHandler superlunchRequestHandler, final ConstantTexts constantTexts) {
        this.hipchatRequestHandler = hipchatRequestHandler;
        this.superlunchRequestHandler = superlunchRequestHandler;
        this.constantTexts = constantTexts;
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
        }
    }

    private void sendMessage(final String text) {
        hipchatRequestHandler.sendMessage(text);

    }

    private void handleMessage(final Message message, final String from, final Room room) throws JSONException, ParseException {
        logger.debug("Handle Message from " + from + ": " + message.getBody());
        final String talkTo = this.convertNames(message.getBody(), from);
        if (this.checkContentMittagessenInfo(message.getBody())) {
            this.handleMittagessenInfoMessage(talkTo);
        } else if (this.checkContentHello(message.getBody())) {
            this.handleHellodMessages(talkTo);
        } else {
            this.handlenoRandomText(talkTo, message.getBody());
        }
    }

    private void handleMittagessenInfoMessage(final String talkTo) throws JSONException, ParseException {
        List<Lunch> lunchList = superlunchRequestHandler.fetchFilteredLunchFromConfluence();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(talkTo);
        if (lunchList.size() > 0) {
            for (final Lunch lunch : lunchList) {
                if (!lunch.isClosed()) {
                    stringBuilder.append(lunch.getTitle() + "(" + lunch.getCreatorName() + ")");
                    stringBuilder.append("\n");
                    stringBuilder.append(lunch.getFormattedPrice() + ", " + this.convertVeggyValue(lunch.isVeggy()));
                    stringBuilder.append("\n");
                    stringBuilder.append(lunch.getDetailLink() + "\n");
                    stringBuilder.append("\n");
                } else {
                    stringBuilder.append(lunch.getTitle() + "(" + lunch.getCreatorName() + ") - ");
                    stringBuilder.append("Anmeldung nicht mehr möglich! \n");
                    stringBuilder.append("\n");
                }
            }
        } else {
            stringBuilder.append("Mittagessen Übersicht noch nicht verfügbar!");
            stringBuilder.append("\n");
            stringBuilder.append("Anmelden über https://confluence.rp.seibert-media.net/dashboard.action");
        }
        this.sendMessage(stringBuilder.toString());
    }

    private void handlenoRandomText(final String talkTo, final String message) {
        if (!talkTo.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(talkTo);
            stringBuilder.append(constantTexts.getRandomText(message));
            this.sendMessage(stringBuilder.toString());
        }
    }

    private void handleHellodMessages(final String talkTo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(talkTo + constantTexts.getHelloText());
        this.sendMessage(stringBuilder.toString());
    }

    private boolean checkContentMittagessenInfo(final String content) {
        final String lowerCaseContent = content.toLowerCase().trim();
        if (lowerCaseContent.contains("/essen") || lowerCaseContent.contains("/mittagessen") || lowerCaseContent.contains("futter") ||
            (lowerCaseContent.contains("was") && lowerCaseContent.contains("heute") && lowerCaseContent.contains("essen")) ||
            (lowerCaseContent.contains("habe") && lowerCaseContent.contains("hunger"))) {
            return true;
        }
        return false;
    }

    private boolean checkContentHello(final String content) {
        final String lowerCaseContent = content.toLowerCase().trim();
        if (lowerCaseContent.contains("hallo") || lowerCaseContent.contains("hello")
            || lowerCaseContent.contains("servus") || lowerCaseContent.contains("guten tag")) {
            return true;
        }
        return false;
    }

    private String convertVeggyValue(final boolean value) {
        return (value) ? "vegetarisch" : "nicht vegetarisch";
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
