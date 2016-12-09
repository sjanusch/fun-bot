package de.sjanusch.listener;

/**
 * Created by Sandro Janusch
 * Date: 18.05.16
 * Time: 20:45
 */
public interface MessageRecieverBase {

  void sendMessageText(final String text, final String username, final String roomId);

  void sendNotification(final String text, final String roomId);

  void sendNotificationError(final String text, final String roomId);

  void sendNotificationSucess(final String text, final String roomId);

  void sendMessageTextToRoom(final String text, final String roomId);

  boolean isMessageFromBot(final String from);

  boolean isMessageForBot(final String message);

  String extractMessage(final String message);
}
