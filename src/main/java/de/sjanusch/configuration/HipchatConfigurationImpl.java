package de.sjanusch.configuration;

import java.io.IOException;

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
    public String getHipchatRestApiKey() throws IOException {
        return this.configurationLoader.getPropertyStringValue("hipchat_rest_api_key");
    }

    @Override
    public String getHipchatRestApiRoomId() throws IOException {
        return this.configurationLoader.getPropertyStringValue("hipchat_rest_api_room_id");
    }


}
