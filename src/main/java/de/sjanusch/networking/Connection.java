package de.sjanusch.networking;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 06:27
 */
public interface Connection {

    void waitForEnd() throws InterruptedException, XMPPException;

    void connect() throws XMPPException;

    void disconnect() throws XMPPException;

    boolean isConnected();

    XMPPConnection getXmpp();
}
