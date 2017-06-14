package de.sjanusch.configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Sandro Janusch
 * Date: 17.05.16
 * Time: 11:15
 */
public class HipchatConfigurationImpl implements HipchatConfiguration {

    private ConfigurationLoader configurationLoader;

    public HipchatConfigurationImpl() {
        this.configurationLoader = new ConfigurationLoader("hipchat.properties");
    }
    @Override
    public String getHipchatRestApi() throws IOException {
        return this.configurationLoader.getPropertyStringValue("hipchat_rest_api");
    }

    @Override
    public String getHipchatRestApiKeyNotification() throws IOException {
        return System.getenv("HIPCHAT_REST_API_KEY_NOTIFICATION");
    }

    @Override
    public String getHipchatRestApiKeyMessage() throws IOException {
        return System.getenv("HIPCHAT_REST_API_KEY_MESSAGE");
    }

    @Override
    public String getHipchatRestApiRoomId(final String roomId) throws IOException {
        final Properties properties = configurationLoader.getProperties();
        if (properties.containsKey(roomId)) {
            return this.configurationLoader.getPropertyStringValue(roomId);
        }
        return null;
    }

}
