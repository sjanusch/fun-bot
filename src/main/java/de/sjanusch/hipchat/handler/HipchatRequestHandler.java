package de.sjanusch.hipchat.handler;

import de.sjanusch.objects.ChatMessage;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 20:05
 */
public interface HipchatRequestHandler {

    void sendMessage(final ChatMessage chatMessage);

}
