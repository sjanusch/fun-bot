package de.sjanusch;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sjanusch.bot.Bot;
import de.sjanusch.guice.GuiceModule;
import de.sjanusch.runner.BotRunner;
import de.sjanusch.runner.NSQRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new GuiceModule());
    final Bot bot = injector.getInstance(Bot.class);
    final BotRunner botRunner = injector.getInstance(BotRunner.class);
    final Thread t = botRunner.runBotDesync(bot);
    t.start();

    final NSQRunner instance = injector.getInstance(NSQRunner.class);
    try {
      instance.run("Ahllo du", "funbot");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }



}
