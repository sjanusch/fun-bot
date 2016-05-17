package de.sjanusch.guice;

import com.google.inject.AbstractModule;
import de.sjanusch.bot.Bot;
import de.sjanusch.bot.BotImpl;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.configuration.BotConfigurationImpl;
import de.sjanusch.configuration.ChatConnectionConfiguration;
import de.sjanusch.configuration.ChatConnectionConfigurationImpl;
import de.sjanusch.configuration.TexteConfiguration;
import de.sjanusch.configuration.TexteConfigurationImpl;
import de.sjanusch.texte.TextHandler;
import de.sjanusch.texte.TextHandlerImpl;
import de.sjanusch.eventsystem.EventSystem;
import de.sjanusch.eventsystem.EventSystemImpl;
import de.sjanusch.handler.MessageRecieveListener;
import de.sjanusch.handler.MessageRecieveListenerImpl;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.hipchat.handler.HipchatRequestHandlerImpl;
import de.sjanusch.networking.Connection;
import de.sjanusch.networking.ConnectionImpl;
import de.sjanusch.runner.BotRunner;
import de.sjanusch.runner.BotRunnerImpl;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Bot.class).to(BotImpl.class);
        bind(BotRunner.class).to(BotRunnerImpl.class);
        bind(HipchatRequestHandler.class).to(HipchatRequestHandlerImpl.class);
        bind(MessageRecieveListener.class).to(MessageRecieveListenerImpl.class);
        bind(BotConfiguration.class).to(BotConfigurationImpl.class);
        bind(ChatConnectionConfiguration.class).to(ChatConnectionConfigurationImpl.class);
        bind(TextHandler.class).to(TextHandlerImpl.class);
        bind(TexteConfiguration.class).to(TexteConfigurationImpl.class);

        bind(EventSystem.class).to(EventSystemImpl.class).asEagerSingleton();
        bind(Connection.class).to(ConnectionImpl.class).asEagerSingleton();
    }
}
