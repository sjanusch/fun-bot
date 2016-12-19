package de.sjanusch;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sjanusch.guice.GuiceModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new GuiceModule());
    final NSQ nsq = injector.getInstance(NSQ.class);
    final Xmpp xmpp = injector.getInstance(Xmpp.class);
    final IsALifeTimer isALifeTimer = injector.getInstance(IsALifeTimer.class);
    xmpp.run();
    nsq.run();
    isALifeTimer.run();
  }

}
