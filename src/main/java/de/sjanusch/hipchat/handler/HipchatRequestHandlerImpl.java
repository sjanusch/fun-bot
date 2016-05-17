package de.sjanusch.hipchat.handler;

import com.google.inject.Inject;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.model.Room;
import de.sjanusch.networking.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.IOException;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 20:06
 */
public class HipchatRequestHandlerImpl implements HipchatRequestHandler {

    private final BotConfiguration botConfiguration;

    final private Connection connection;

    @Inject
    public HipchatRequestHandlerImpl(final BotConfiguration botConfiguration, final Connection connection) {
        this.botConfiguration = botConfiguration;
        this.connection = connection;
    }

    public void sendMessageToRoom(final String name, final String message) throws IOException {
        final Room room = connection.findRoom(name, botConfiguration.getBotChatApikey());
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
