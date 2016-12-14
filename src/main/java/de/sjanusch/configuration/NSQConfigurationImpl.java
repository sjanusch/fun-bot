package de.sjanusch.configuration;

import java.io.IOException;

/**
 * Created by Sandro Janusch
 * Date: 14.12.16
 * Time: 10:22
 */

public class NSQConfigurationImpl implements NSQConfiguration {

  @Override
  public String getNSQLookupAdress() throws IOException {
    final String[] lookupAdressArray = this.splitEnvVariable(System.getenv("NSQ_LOOKUPD_ADDRESS"));
    return lookupAdressArray[0];
  }

  @Override
  public Integer getNSQLookupAdressPort() throws IOException {
    final String[] lookupAdressArray = this.splitEnvVariable(System.getenv("NSQ_LOOKUPD_ADDRESS"));
    return Integer.valueOf(lookupAdressArray[1]);
  }

  @Override
  public String getNSQAdress() throws IOException {
    final String[] adressArray = this.splitEnvVariable(System.getenv("NSQD_ADDRESS"));
    return adressArray[0];
  }

  @Override
  public Integer getNSQAdressPort() throws IOException {
    final String[] adressArray = this.splitEnvVariable(System.getenv("NSQD_ADDRESS"));
    return Integer.valueOf(adressArray[1]);
  }

  private String[] splitEnvVariable(final String string) {
    return string.split(":");
  }

}
