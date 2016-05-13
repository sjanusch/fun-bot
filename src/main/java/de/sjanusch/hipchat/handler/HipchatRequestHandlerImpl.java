package de.sjanusch.hipchat.handler;

import com.google.inject.Inject;
import de.sjanusch.model.Room;
import de.sjanusch.networking.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 20:06
 */
public class HipchatRequestHandlerImpl implements HipchatRequestHandler {

    private final String apiKey = "0AvDGI72wHaRkWHUGIwcHKDiQRLav0RQ080Lnvt6";

    final private Connection connection;

    @Inject
    public HipchatRequestHandlerImpl(final Connection connection) {
        this.connection = connection;
    }

    public void sendMessageToRoom(final String name, final String message) {
        final Room room = connection.findRoom(name, apiKey);
        if (room != null) {
            sendMessage(message);
        }
    }

    public void sendMessage(String message) {
        try {
            final MultiUserChat chat = connection.getChat();
            if (connection.getChat() != null)
                chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
