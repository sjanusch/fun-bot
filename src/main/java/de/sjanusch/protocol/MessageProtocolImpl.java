package de.sjanusch.protocol;

import de.sjanusch.flow.MessageFlow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sandro Janusch
 * Date: 18.05.16
 * Time: 21:39
 */
public class MessageProtocolImpl implements MessageProtocol {

    private Map<String, MessageFlow> messageProtocol = new HashMap<>();

    public void addFlowForUser(final String username, final MessageFlow flow) {
        if (!messageProtocol.containsKey(username)) {
            messageProtocol.put(username, flow);
        }
    }

    public void removeFlowForUser(final String username) {
        if (messageProtocol.containsKey(username)) {
            messageProtocol.remove(username);
        }
    }

    public MessageFlow getCurrentFlowForUser(final String username) {
        if (messageProtocol.containsKey(username)) {
            final MessageFlow messageFlow = messageProtocol.get(username);
            return messageFlow;
        }
        return null;
    }

}
