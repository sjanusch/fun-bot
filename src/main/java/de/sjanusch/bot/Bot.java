package de.sjanusch.bot;

import de.sjanusch.runner.RunnableBot;

import java.io.IOException;

public interface Bot extends RunnableBot {

    void run();

    String getNickname() throws IOException;

    String getPassword() throws IOException;

    String getUsername() throws IOException;

    void startPrivateChat(String username);

    void restart();

    void leaveChatRoom();
}
