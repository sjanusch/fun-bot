package de.sjanusch.guice;

import com.google.inject.AbstractModule;
import de.sjanusch.bot.Bot;
import de.sjanusch.bot.BotImpl;
import de.sjanusch.configuration.BotConfiguration;
import de.sjanusch.configuration.BotConfigurationImpl;
import de.sjanusch.configuration.ChatConnectionConfiguration;
import de.sjanusch.configuration.ChatConnectionConfigurationImpl;
import de.sjanusch.configuration.HipchatConfiguration;
import de.sjanusch.configuration.HipchatConfigurationImpl;
import de.sjanusch.configuration.NSQConfiguration;
import de.sjanusch.configuration.NSQConfigurationImpl;
import de.sjanusch.configuration.TexteConfiguration;
import de.sjanusch.configuration.TexteConfigurationImpl;
import de.sjanusch.helper.MessageHelper;
import de.sjanusch.helper.MessageHelperImpl;
import de.sjanusch.hipchat.handler.HipchatRequestHandler;
import de.sjanusch.hipchat.handler.HipchatRequestHandlerImpl;
import de.sjanusch.hipchat.rest.HipchatRestClient;
import de.sjanusch.hipchat.rest.HipchatRestClientImpl;
import de.sjanusch.listener.MessageRecieveListener;
import de.sjanusch.listener.MessageRecieveListenerImpl;
import de.sjanusch.listener.MessageRecieverBase;
import de.sjanusch.listener.MessageRecieverBaseImpl;
import de.sjanusch.networking.ChatClient;
import de.sjanusch.networking.ChatClientImpl;
import de.sjanusch.networking.Connection;
import de.sjanusch.networking.ConnectionImpl;
import de.sjanusch.protocol.MessageProtocol;
import de.sjanusch.protocol.MessageProtocolImpl;
import de.sjanusch.runner.BotRunner;
import de.sjanusch.runner.BotRunnerImpl;
import de.sjanusch.texte.TextHandler;
import de.sjanusch.texte.TextHandlerImpl;

public class GuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Bot.class).to(BotImpl.class);
    bind(BotRunner.class).to(BotRunnerImpl.class);
    bind(HipchatRequestHandler.class).to(HipchatRequestHandlerImpl.class);
    bind(BotConfiguration.class).to(BotConfigurationImpl.class);
    bind(ChatConnectionConfiguration.class).to(ChatConnectionConfigurationImpl.class);
    bind(HipchatConfiguration.class).to(HipchatConfigurationImpl.class);
    bind(HipchatRestClient.class).to(HipchatRestClientImpl.class);
    bind(TextHandler.class).to(TextHandlerImpl.class);
    bind(TexteConfiguration.class).to(TexteConfigurationImpl.class);
    bind(ChatClient.class).to(ChatClientImpl.class);
    bind(MessageRecieveListener.class).to(MessageRecieveListenerImpl.class);
    bind(MessageRecieverBase.class).to(MessageRecieverBaseImpl.class);
    bind(MessageHelper.class).to(MessageHelperImpl.class);
    bind(NSQConfiguration.class).to(NSQConfigurationImpl.class);

    bind(Connection.class).to(ConnectionImpl.class).asEagerSingleton();
    bind(MessageProtocol.class).to(MessageProtocolImpl.class).asEagerSingleton();
    bind(Bot.class).to(BotImpl.class).asEagerSingleton();
  }
}
