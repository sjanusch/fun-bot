package de.sjanusch.google;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Sandro Janusch
 * Date: 21.06.16
 * Time: 15:27
 */
public class SearchQuery {

  private final String query;

  private final String site;

  private final Integer numResults;

  public SearchQuery(Builder builder) {
    this.query = builder.query;
    this.site = builder.site;
    this.numResults = builder.numResults;
  }

  public Integer getNumResults() {
    return numResults;
  }

  public String getQuery() {
    return query;
  }

  public String getSite() {
    return site;
  }

  public static class Builder {

    private String query;

    private String site;

    private Integer numResults = 10;

    public Builder(String query) {
      try {
        this.query = URLEncoder.encode(query, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    public Builder site(String site) {
      this.site = site;
      return this;
    }

    public Builder numResults(Integer numResults) {
      this.numResults = numResults;
      return this;
    }

    public SearchQuery build() {
      return new SearchQuery(this);
    }
  }

}
