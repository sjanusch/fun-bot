package de.sjanusch.hipchat.rest;

import com.google.inject.Inject;
import de.sjanusch.configuration.HipchatConfiguration;
import de.sjanusch.model.hipchat.Error;
import de.sjanusch.model.hipchat.HipchatMessage;
import de.sjanusch.model.hipchat.HipchatRestError;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.client.ClientProperties;
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

  public static final String EMAIL_POSTFIX = "@seibert-media.net";

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
      .hostnameVerifier(new HostnameVerifierAllowAll())
      .sslContext(sc)
      .build();
    client.property(ClientProperties.CONNECT_TIMEOUT, 3000);
    client.property(ClientProperties.READ_TIMEOUT, 3000);
    return client;
  }

  @Override
  public void hipchatRestApiSendNotification(final HipchatMessage chatMessage) {
    try {
      final String path = "/room/" + this.getHipchatRoomId(chatMessage) + "/notification";
      this.hipchatRestApiNotification(buildClient().target(this.getHipchatRestApi()).path(path), chatMessage);
    } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
      logger.warn(e.getClass().getName(), e);
    }
  }

  @Override
  public void hipchatRestApiSendMessage(final HipchatMessage chatMessage) {
    try {
      final String path = "/room/" + this.getHipchatRoomId(chatMessage) + "/message";
      this.hipchatRestApiMessage(buildClient().target(this.getHipchatRestApi()).path(path), chatMessage);
    } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
      logger.warn(e.getClass().getName(), e);
    }
  }

  @Override
  public void hipchatRestApiSendPrivateMessage(final HipchatMessage chatMessage, final String userNickName) {
    logger.debug("hipchatRestApiSendPrivateMessage to {}", userNickName);
    try {
      final String path = this.getHipChatUserMail(userNickName);
      this.hipchatRestApiPrivateMessage(buildClient().target(this.getHipchatRestApi()).path(path), chatMessage);
    } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
      logger.warn(e.getClass().getName(), e);
    }
  }

  private void hipchatRestApiNotification(final WebTarget target, final HipchatMessage chatMessage) throws IOException {
    logger.debug("Requesting  '" + target.getUri() + "' by POST ");
    final ObjectMapper mapper = new ObjectMapper();
    try {
      final Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
        .header("Authorization", "Bearer " + hipchatConfiguration.getHipchatRestApiKeyNotification())
        .post(Entity.entity(mapper.writeValueAsString(chatMessage), MediaType.APPLICATION_JSON_TYPE));
      this.handleResponse(response, chatMessage);
    } catch (final ProcessingException e) {
      logger.error("Unexpected return code from calling", e);
    }
  }

  private void hipchatRestApiMessage(final WebTarget target, final HipchatMessage chatMessage) throws IOException {
    logger.debug("Requesting  '" + target.getUri() + "' by POST ");
    final ObjectMapper mapper = new ObjectMapper();
    try {
      final Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
        .header("Authorization", "Bearer " + hipchatConfiguration.getHipchatRestApiKeyMessage())
        .post(Entity.entity(mapper.writeValueAsString(chatMessage), MediaType.APPLICATION_JSON_TYPE));
      this.handleResponse(response, chatMessage);
    } catch (final ProcessingException e) {
      logger.error("Unexpected return code from calling", e);
    }
  }

  private void hipchatRestApiPrivateMessage(final WebTarget target, final HipchatMessage chatMessage) throws IOException {
    logger.debug("Requesting  '{}' by POST - message: {}", target.getUri(), chatMessage.getMessage());
    final ObjectMapper mapper = new ObjectMapper();
    try {
      final Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
        .header("Authorization", "Bearer " + hipchatConfiguration.getHipchatRestApiKeyMessage())
        .post(Entity.entity(mapper.writeValueAsString(chatMessage), MediaType.APPLICATION_JSON_TYPE));
      this.handleResponse(response, chatMessage);
    } catch (final ProcessingException e) {
      logger.error("Unexpected return code from calling", e);
    }
  }

  private void handleResponse(final Response response, final HipchatMessage chatMessage) {
    switch (response.getStatus()) {
      default:
        this.sendResponseError(response, chatMessage);
        break;
      case 204:
        logger.debug("Message success");
        break;
      case 201:
        logger.debug("Message success");
        break;
      case 200:
        logger.debug("Message success");
        break;
    }
  }

  private void sendResponseError(final Response response, final HipchatMessage chatMessage) {
    final String errorString = response.readEntity(String.class);
    try {
      final HipchatRestError hipchatRestError = new ObjectMapper().readValue(errorString, HipchatRestError.class);
      final Error error = hipchatRestError.getError();
      logger.error("Unexpected return: " + error.getCode() + ", " + error.getMessage() + ", " + error.getType());
      final HipchatMessage hipchatMessage = new HipchatMessage(chatMessage.getHipchatRoomId(), error.getType() + ": " + error.getMessage(), "html");
      hipchatMessage.setColor("red");
      this.hipchatRestApiSendMessage(hipchatMessage);
    } catch (IOException e) {
      logger.error("parsing hipchat JSON response: " + errorString);
    }
  }

  private String getHipchatRoomId(final HipchatMessage chatMessage) throws IOException {
    return hipchatConfiguration.getHipchatRestApiRoomId(chatMessage.getHipchatRoomId());
  }

  private String getHipchatRestApi() throws IOException {
    return hipchatConfiguration.getHipchatRestApi();
  }

  private String getHipChatUserMail(final String userNickName) {
    return "/user/" + userNickName + EMAIL_POSTFIX + "/message";
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
