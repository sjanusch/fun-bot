package de.sjanusch.google;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandro Janusch
 * Date: 21.06.16
 * Time: 15:27
 */
public class SearchResult {

  private final List<SearchHit> hits;

  private final SearchQuery query;

  public SearchResult(SearchQuery query, List<SearchHit> hits) {
    this.query = query;
    this.hits = hits;
  }

  public List<String> getUrls() {
    List<String> urls = new ArrayList<String>(hits.size());
    for (SearchHit hit : hits) {
      urls.add(hit.getUrl());
    }
    return urls;
  }

  public int getSize() {
    return hits.size();
  }

  public List<SearchHit> getHits() {
    return hits;
  }
}
