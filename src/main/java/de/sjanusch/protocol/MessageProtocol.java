package de.sjanusch.protocol;

import de.sjanusch.flow.MessageFlow;

/**
 * Created by Sandro Janusch
 * Date: 18.05.16
 * Time: 21:39
 */
public interface MessageProtocol {

    void addFlowForUser(final String username, final MessageFlow flow);

    void removeFlowForUser(final String username);

    MessageFlow getCurrentFlowForUser(final String username);

}
