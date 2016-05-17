package de.sjanusch.hipchat.rest;

import de.sjanusch.model.hipchat.ChatMessage;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 19:39
 */
public interface HipchatRestClient {


    void hipchatRestApiSendNotification(final ChatMessage chatMessage);

}
