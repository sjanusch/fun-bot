package de.sjanusch.configuration;

import com.google.inject.Inject;

/**
 * Created by Sandro Janusch
 * Date: 16.06.16
 * Time: 16:01
 */
public class AimlConfigurationImpl implements AimlConfiguration {

  private ConfigurationLoader configurationLoader;

  @Inject
  public AimlConfigurationImpl() {
    this.configurationLoader = new ConfigurationLoader("aiml.properties");
  }
}
