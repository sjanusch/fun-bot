package org.alicebot.ab.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class NetworkUtils {

  private static final Logger logger = LoggerFactory.getLogger(NetworkUtils.class);

  public String localIPAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            String ipAddress = inetAddress.getHostAddress().toString();
            int p = ipAddress.indexOf("%");
            if (p > 0) ipAddress = ipAddress.substring(0, p);
            logger.debug("--> localIPAddress = "+ipAddress);
            return ipAddress;
          }
        }
      }
    } catch (SocketException ex) {
      ex.printStackTrace();
    }
    return "127.0.0.1";
  }

  public String responseContent(String url) throws Exception {
    Client client = buildClient();
    URI uri = new URI(url);
    Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
    StringBuilder sb = new StringBuilder("");
    String NL = System.getProperty("line.separator");
    sb.append(response.readEntity(String.class)).append(NL);
    return sb.toString();
  }

  private Client buildClient() throws NoSuchAlgorithmException, KeyManagementException {
    final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManagerAllowAll()};
    final SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());

    final Client client = ClientBuilder.newBuilder()
      .hostnameVerifier(new HostnameVerifierAllowAll())
      .sslContext(sc)
      .build();
    return client;
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
