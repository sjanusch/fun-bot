package de.sjanusch.nsq;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class NSQProducer {

  private static final Logger log = LoggerFactory.getLogger(NSQProducer.class);

  private static final String PUT_URL = "/put?topic=";

  private static final int DEFAULT_SOCKET_TIMEOUT = 2000;

  private static final int DEFAULT_CONNECTION_TIMEOUT = 2000;

  protected ExecutorService executor = Executors.newCachedThreadPool();

  protected HttpClient httpclient;

  private String url;

  private String topic;
  // TODO add timeout config / allow setting any httpclient param via getHtttpClient

  // Convenience  constructor assuming local nsqd on standard port
  public NSQProducer(String topic) {
    this("http://localhost:4150", topic);
  }

  public NSQProducer(String url, String topic) {
    this.topic = topic;
    this.url = url + PUT_URL + topic;

    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(
      new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

    ClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);

    this.httpclient = new DefaultHttpClient(cm);
    this.setSocketTimeout(DEFAULT_SOCKET_TIMEOUT);
    this.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
    // see https://code.google.com/p/crawler4j/issues/detail?id=136: potentially works around a jvm crash at
    // org.apache.http.impl.cookie.BestMatchSpec.formatCookies(Ljava/util/List;)Ljava/util/List
    this.httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);

    // register action for shutdown
    Runtime.getRuntime().addShutdownHook(new Thread() {

      @Override
      public void run() {
        shutdown();
      }
    });
  }

  /**
   * Post a message onto NSQ (via the http interface)
   *
   * @param message
   */
  public void put(String message) throws Exception {
    HttpPost post = null;
    try {
      post = new HttpPost(url);
      post.setEntity(new StringEntity(message));
      HttpResponse response = this.httpclient.execute(post);
      if (response.getStatusLine().getStatusCode() != 200) {
        throw new Exception("POST to " + url + " returned HTTP " + response.getStatusLine().getStatusCode());
      }
      if (response.getEntity() != null) {
        EntityUtils.consume(response.getEntity());
      }
    } catch (UnsupportedEncodingException e) {
      throw new Exception(e);
    } catch (ClientProtocolException e) {
      throw new Exception(e);
    } catch (IOException e) {
      throw new Exception(e);
    } finally {
      if (post != null) {
        post.releaseConnection();
      }
    }
  }

  /**
   * Post the message in a background executor thread, and log any error that occurs.
   * If you want, you can call task.get() but then you may as well just call put().
   *
   * @param message
   * @return
   */
  public FutureTask<Void> putAsync(String message) {
    FutureTask task = new FutureTask<Void>(new NSQAsyncWriter(message));
    executor.execute(task);
    return task;

  }

  public void shutdown() {
    if (this.executor != null) {
      this.executor.shutdown();
    }
  }

  public String toString() {
    return "Writer<" + this.url + ">";
  }

  public String getUrl() {
    return url;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  /**
   * This setter is probably only useful in a unit test / mocking context.
   *
   * @param client
   */
  public void setHttpClient(HttpClient client) {
    this.httpclient = client;
  }

  public HttpClient getHttpclient() {
    return this.httpclient;
  }

  public void setSocketTimeout(int timeout) {
    this.httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
  }

  public void setConnectionTimeout(int timeout) {
    this.httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
  }

  public class NSQAsyncWriter implements Callable<Void> {

    private String message = null;

    NSQAsyncWriter(String message) {
      this.message = message;
    }

    public Void call() throws Exception {
      try {
        NSQProducer.this.put(message);
      } catch (Exception e) {
        // Log the error here since caller probably won't ever check the future.
        log.error("Error posting NSQ message:", e);
        throw e;
      }
      return null;
    }
  }

}
