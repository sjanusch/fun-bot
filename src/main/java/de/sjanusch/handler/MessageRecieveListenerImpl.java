package de.sjanusch.handler;

import com.google.inject.Inject;
import de.sjanusch.confluence.rest.SuperlunchRestClientImpl;
import de.sjanusch.eventsystem.EventHandler;
import de.sjanusch.eventsystem.events.model.MessageRecivedEvent;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.model.Room;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 07:00
 */
public class MessageRecieveListenerImpl implements MessageRecieveListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageRecieveListenerImpl.class);

    private final HipchatRequestHandler hipchatRequestHandler;

    private final SuperlunchRestClientImpl superlunchRestClient;

    @Inject
    public MessageRecieveListenerImpl(final HipchatRequestHandler hipchatRequestHandler, final SuperlunchRestClientImpl superlunchRestClient) {
        this.hipchatRequestHandler = hipchatRequestHandler;
        this.superlunchRestClient = superlunchRestClient;
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

    public void handleMessage(final Message message, final String from, final Room room) throws JSONException, ParseException {
        logger.debug(from + "(" + room.getTrueName() + ")" + ": " + message);
        hipchatRequestHandler.sendMessage(message.getBody());

    }



    /*
    public void handleMessage(final String message, final String from, final Room room) throws JSONException, ParseException {
        logger.debug(from + "(" + room.getTrueName() + ")" + ": " + message);
        if (message.toLowerCase().contains("/essen") || message.toLowerCase().replace(" ", "").contains("wasgibtesheute")) {
            lunchOverview(from);
            return;
        } else if (message.toLowerCase().contains("hallo") || message.toLowerCase().contains("hello")
            || message.toLowerCase().contains("servus") || message.toLowerCase().replace(" ", "").contains("gutentag")) {
            handleHellodMessages(from, message);
            return;
        } else if (message.toLowerCase().replace(" ", "").contains("dasgibtesheutezummittagessen:")) {
            return;
        } else {
            handlenoRandomText(from, message);
            return;
        }

    }


    private void lunchOverview(final String from) throws JSONException, ParseException {
        String newName = convertNames(from);
        if (lunchObject.getList() != null && lunchObject.getList().size() > 0) {
            bot.sendMessage("@" + newName + " Das gibt es heute zum Mittagessen:");
            for (String string : lunchObject.getLunchToday()) {
                bot.sendMessage(string);
            }
        } else {
            bot.sendMessage("@" + newName + " Diesen Wunsch kenne ich noch nicht, versuch es nocheinmal!");
        }
    }

    private void lunchAnmelden() {
        superlunchRestClient.superlunchRestApiSignIn("1107", "sjanusch");
    }

    private void handleBuzzwordMessages(final String from, final String message) {
        String text = Constants.getRandomText(message);
        if (text != null) {
            String newName = convertNames(from);
            if (newName != null) {
                bot.sendMessage("@" + newName + " " + text);
            } else {
                bot.sendMessage(text);
            }
        }
    }

    private void handlenoRandomText(final String from, final String message) {
        String text = Constants.getNoRandomText(message);
        if (text != null) {
            String newName = convertNames(from);
            if (newName != null) {
                bot.sendMessage("@" + newName + " " + text);
            } else {
                bot.sendMessage(text);
            }
        }
    }

    private void handleHellodMessages(final String from, final String message) {
        String text = "Salut";
        if (text != null) {
            String newName = convertNames(from);
            if (newName != null) {
                bot.sendMessage(text + " @" + newName);
            } else {
                bot.sendMessage(text);
            }
        }
    }

    private void handleNormalMessages(final String from) {
        bot.sendMessage("Leider gibt es nur Bastians " + from);
    }

    private HipchatUser getHipchatUser(final String name) {
        List<HipchatUser> list = bot.getUsers();
        for (HipchatUser hipchatUser : list) {
            if (hipchatUser.getName().equals(name)) {
                return hipchatUser;
            }
        }
        return null;
    }

    private String convertNames(final String from) {
        String[] names = from.split(" ");
        String newName = null;
        if (names.length > 1) {
            newName = names[0].toLowerCase().charAt(0) + names[1].toLowerCase();
        }
        return newName;
    }
   */
}
