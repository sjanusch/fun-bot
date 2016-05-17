package de.sjanusch.guice;

import com.google.inject.AbstractModule;
import de.sjanusch.bot.Bot;
import de.sjanusch.bot.BotImpl;
import de.sjanusch.configuration.LunchConfiguration;
import de.sjanusch.configuration.LunchConfigurationImpl;
import de.sjanusch.confluence.handler.SuperlunchRequestHandler;
import de.sjanusch.confluence.handler.SuperlunchRequestHandlerImpl;
import de.sjanusch.confluence.rest.SuperlunchRestClient;
import de.sjanusch.confluence.rest.SuperlunchRestClientImpl;
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
        bind(SuperlunchRequestHandler.class).to(SuperlunchRequestHandlerImpl.class);
        bind(SuperlunchRestClient.class).to(SuperlunchRestClientImpl.class);
        bind(HipchatRequestHandler.class).to(HipchatRequestHandlerImpl.class);
        bind(MessageRecieveListener.class).to(MessageRecieveListenerImpl.class);
        bind(LunchConfiguration.class).to(LunchConfigurationImpl.class);

        bind(EventSystem.class).to(EventSystemImpl.class).asEagerSingleton();
        bind(Connection.class).to(ConnectionImpl.class).asEagerSingleton();
    }
}
