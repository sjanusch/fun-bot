package de.sjanusch.objects;

/**
 * Created by Sandro Janusch
 * Date: 17.05.16
 * Time: 20:38
 */
public class ChatMessage {

    private String color;

    private String message_format;

    private String message;

    private boolean notify = true;

    public ChatMessage(final String message) {
        this.message = message;
    }

    public String getColor() {
        return (color != null) ? color : "random";
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessage_format() {
        return (message_format != null) ? message_format : "text";
    }

    public void setMessage_format(final String message_format) {
        this.message_format = message_format;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(final boolean notify) {
        this.notify = notify;
    }
}
