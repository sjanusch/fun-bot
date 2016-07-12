package de.sjanusch.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by Sandro Janusch
 * Date: 22.06.16
 * Time: 14:50
 */
public class Redis {

  private static final Logger logger = LoggerFactory.getLogger(Redis.class);

  public void startRedis() {

    Jedis jedis = new Jedis("172.16.11.11");
    logger.debug("Connection to server sucessfully");
    logger.debug("Server is running: " + jedis.ping());
  }

}
