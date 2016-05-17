package de.sjanusch.hipchat.handler;

import com.google.inject.Inject;
import de.sjanusch.hipchat.rest.HipchatRestClient;
import de.sjanusch.networking.Connection;
import de.sjanusch.model.hipchat.ChatMessage;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 20:06
 */
public class HipchatRequestHandlerImpl implements HipchatRequestHandler {

    final private Connection connection;

    final private HipchatRestClient hipchatRestClient;

    @Inject
    public HipchatRequestHandlerImpl(final Connection connection, final HipchatRestClient hipchatRestClient) {
        this.connection = connection;
        this.hipchatRestClient = hipchatRestClient;
    }

    public void sendMessage(final ChatMessage chatMessage) {
        hipchatRestClient.hipchatRestApiSendNotification(chatMessage);
    }
}
