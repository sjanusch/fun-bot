package de.sjanusch.configuration;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sandro Janusch
 * Date: 17.05.16
 * Time: 15:48
 */
public class TexteConfigurationImpl implements TexteConfiguration {

    private ConfigurationLoader configurationLoader;

    public TexteConfigurationImpl() {
        this.configurationLoader = new ConfigurationLoader("texte.properties");
    }

    @Override
    public List<String> getHelloTexteAsList() throws IOException {
        return this.configurationLoader.getPropertyStringListValue("hello_texte");
    }

    @Override
    public List<String> getThankYouTexteAsList() throws IOException {
        return this.configurationLoader.getPropertyStringListValue("thankyou_texte");
    }

    @Override
    public List<String> getRandomTexteAsList() throws IOException {
        return this.configurationLoader.getPropertyStringListValue("random_texte");
    }
}
