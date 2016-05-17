package de.sjanusch.hipchat.rest;

import com.google.inject.Inject;
import de.sjanusch.configuration.HipchatConfiguration;
import de.sjanusch.objects.ChatMessage;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HipchatRestClientImpl implements HipchatRestClient {

    public static final Logger logger = LoggerFactory.getLogger(HipchatRestClientImpl.class);

    private final HipchatConfiguration hipchatConfiguration;

    @Inject
    public HipchatRestClientImpl(final HipchatConfiguration hipchatConfiguration) {
        this.hipchatConfiguration = hipchatConfiguration;
    }

    private Client buildClient() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManagerAllowAll()};
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        final Client client = ClientBuilder.newBuilder()
            .register(JacksonFeature.class)
            .hostnameVerifier(new HostnameVerifierAllowAll())
            .sslContext(sc)
            .build();
        client.property(ClientProperties.CONNECT_TIMEOUT, 3000);
        client.property(ClientProperties.READ_TIMEOUT, 3000);
        return client;
    }

    @Override
    public void hipchatRestApiSendNotification(final ChatMessage chatMessage) {
        try {
            final String path = "/room/" + this.getHipchatRoomId() + "/notification";
            hipchatSendNotification(buildClient().target(this.getHipchatRestApi()).path(path), chatMessage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hipchatSendNotification(final WebTarget target, final ChatMessage chatMessage) throws IOException {
        logger.debug("Requesting  '" + target.getUri() + "' by GET ");
        Response response = null;
        try {
            response = target.request(MediaType.APPLICATION_JSON_TYPE).header("Authorization", this.getHeaderValue())
                .post(Entity.entity(chatMessage, MediaType.APPLICATION_JSON_TYPE));
            switch (response.getStatus()) {
                default:
                    logger.error("Unexpected return code from calling '" + response.readEntity(String.class));
                    break;
                case 400:
                    logger.error("Unexpected return code from calling '" + response.readEntity(String.class));
                    break;
                case 403:
                    logger.error("Unexpected return code from calling '" + response.readEntity(String.class));
                    break;
                case 204:
                    logger.debug("Message sucess");
                    break;
                case 200:
                    logger.debug("Message sucess");
                    break;
            }
        } catch (ProcessingException e) {
            logger.error("Unexpected return code from calling '" + e.getMessage());
        }
    }

    private String getHeaderValue() throws IOException {
        return "Bearer " + hipchatConfiguration.getHipchatRestApiKey();
    }

    private String getHipchatRoomId() throws IOException {
        return hipchatConfiguration.getHipchatRestApiRoomId();
    }

    private String getHipchatRestApi() throws IOException {
        return hipchatConfiguration.getHipchatRestApi();
    }

    private final class HostnameVerifierAllowAll implements HostnameVerifier {

        @Override
        public boolean verify(final String arg0, final SSLSession arg1) {
            return true;
        }
    }

    private final class X509TrustManagerAllowAll implements X509TrustManager {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
        }

        @Override
        public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
        }
    }

}
