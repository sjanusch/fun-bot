package de.sjanusch.google;

import org.jsoup.select.Elements;

/**
 * Created by Sandro Janusch
 * Date: 21.06.16
 * Time: 15:27
 */
public class SearchHit {

  private final String url;

  private final Elements elements;

  public SearchHit(String url, final Elements elements) {
    this.url = url;
    this.elements = elements;
  }

  public String getUrl() {
    return url;
  }

  public Elements getElements() {
    return elements;
  }
}
