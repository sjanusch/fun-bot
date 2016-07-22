package de.sjanusch.runner;

import com.google.inject.Inject;
import de.sjanusch.networking.Connection;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotRunnerImpl implements BotRunner {

  private static final Logger logger = LoggerFactory.getLogger(BotRunnerImpl.class);

  private final Connection connection;

  @Inject
  public BotRunnerImpl(final Connection connection) {
    this.connection = connection;
  }

  private void run(RunnableBot bot) {
    boolean isConnected = false;
    bot.run();
    try {
      while (!isConnected) {
        connection.waitForEnd();
        isConnected = connection.isConnected();
        logger.debug("Bot is connected: " + isConnected);
      }
    } catch (InterruptedException e) {
      logger.error("Bot InterruptedException: " + e.getMessage());
    } catch (XMPPException e) {
      logger.error("Bot XMPPException: " + e.getMessage());
    }
  }

  private Thread runBotDysync(final RunnableBot bot) {
    Thread t = new Thread() {

      @Override
      public void run() {
        BotRunnerImpl.this.run(bot);
      }
    };
    return t;
  }

  public void runBot(final RunnableBot bot) {
    new BotRunnerImpl(connection).run(bot);
  }

  public Thread runBotDesync(final RunnableBot bot) {
    return new BotRunnerImpl(connection).runBotDysync(bot);
  }
}
