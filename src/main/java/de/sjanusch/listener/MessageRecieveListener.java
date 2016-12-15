package de.sjanusch.listener;

import java.io.IOException;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 20:43
 */
public interface MessageRecieveListener {

  boolean handleMessage(final String chatMessage, final String from, final String roomId) throws IOException;

}
