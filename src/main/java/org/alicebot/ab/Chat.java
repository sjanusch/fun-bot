package org.alicebot.ab;

import org.alicebot.ab.utils.JapaneseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Chat {

  private Bot bot;

  private boolean doWrites;

  private String customerId = "unknown";

  private History<History> thatHistory = new History<History>("that");

  private History<String> requestHistory = new History<String>("request");

  private History<String> responseHistory = new History<String>("response");

  private History<String> inputHistory = new History<String>("input");

  private Predicates predicates = new Predicates();

  private boolean locationKnown = false;

  private String longitude;

  private String latitude;

  private TripleStore tripleStore = new TripleStore("anon", this);

  private static final Logger logger = LoggerFactory.getLogger(Chat.class);

  public Chat(final Bot bot) {
    this.customerId = "0";
    this.bot = bot;
    this.doWrites = true;
    History<String> contextThatHistory = new History<String>();
    contextThatHistory.add("unknown");
    thatHistory.add(contextThatHistory);
    addPredicates();
    addTriples();
    predicates.put("topic", "unknown");
    predicates.put("jsenabled", "true");
  }

  public Bot getBot() {
    return bot;
  }

  public String getCustomerId() {
    return customerId;
  }

  public History<History> getThatHistory() {
    return thatHistory;
  }

  public History<String> getRequestHistory() {
    return requestHistory;
  }

  public History<String> getResponseHistory() {
    return responseHistory;
  }

  public History<String> getInputHistory() {
    return inputHistory;
  }

  public Predicates getPredicates() {
    return predicates;
  }

  public boolean isLocationKnown() {
    return locationKnown;
  }

  public TripleStore getTripleStore() {
    return tripleStore;
  }

  public String getLatitude() {
    return latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public String chat(String test) {
    String response = multisentenceRespond(test);
    return response;
  }

  private void addPredicates() {
    try {
      predicates.getPredicateDefaults(bot.getConfig_path() + "/predicates.txt");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private int addTriples() {
    int tripleCnt = 0;
    logger.debug("Loading Triples from " + bot.getConfig_path() + "/triples.txt");
    File f = new File(bot.getConfig_path() + "/triples.txt");
    if (f.exists())
      try {
        InputStream is = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String strLine;
        while ((strLine = br.readLine()) != null) {
          String[] triple = strLine.split(":");
          if (triple.length >= 3) {
            String subject = triple[0];
            String predicate = triple[1];
            String object = triple[2];
            tripleStore.addTriple(subject, predicate, object);
            logger.debug("Added Triple:" + subject + " " + predicate + " " + object);
            tripleCnt++;
          }
        }
        is.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    logger.debug("Loaded " + tripleCnt + " triples");
    return tripleCnt;
  }

  private String respond(String input, String that, String topic, History contextThatHistory) {
    logger.debug("chat.respond(input: " + input + ", that: " + that + ", topic: " + topic + ", contextThatHistory: " + contextThatHistory + ")");
    boolean repetition = true;
    //inputHistory.printHistory();
    for (int i = 0; i < MagicNumbers.repetition_count; i++) {
      if (inputHistory.get(i) == null || !input.toUpperCase().equals(inputHistory.get(i).toUpperCase()))
        repetition = false;
    }
    if (input.equals(MagicStrings.null_input)) repetition = false;
    inputHistory.add(input);
    if (repetition) {
      input = MagicStrings.repetition_detected;
    }

    String response;

    response = AIMLProcessor.respond(input, that, topic, this);
    logger.debug("in chat.respond(), response: " + response);
    String normResponse = bot.getPreProcessor().normalize(response);
    logger.debug("in chat.respond(), normResponse: " + normResponse);
    if (MagicBooleans.jp_tokenize) normResponse = JapaneseUtils.tokenizeSentence(normResponse);
    String sentences[] = bot.getPreProcessor().sentenceSplit(normResponse);
    for (int i = 0; i < sentences.length; i++) {
      that = sentences[i];
      logger.debug("That " + i + " '" + that + "'");
      if (that.trim().equals("")) that = MagicStrings.default_that;
      contextThatHistory.add(that);
    }
    String result = response.trim() + "  ";
    logger.debug("in chat.respond(), returning: " + result);
    return result;
  }

  private String respond(String input, History<String> contextThatHistory) {
    History hist = thatHistory.get(0);
    String that;
    if (hist == null) that = "unknown";
    else that = hist.getString(0);
    return respond(input, that, predicates.get("topic"), contextThatHistory);
  }

  private String multisentenceRespond(String request) {
    logger.debug("chat.multisentenceRespond(request: " + request + ")");
    String response = "";
    try {
      String normalized = bot.getPreProcessor().normalize(request);
      normalized = JapaneseUtils.tokenizeSentence(normalized);
      logger.debug("in chat.multisentenceRespond(), normalized: " + normalized);
      String sentences[] = bot.getPreProcessor().sentenceSplit(normalized);
      History<String> contextThatHistory = new History<String>("contextThat");
      for (int i = 0; i < sentences.length; i++) {
        logger.debug("Human: " + sentences[i]);
        AIMLProcessor.trace_count = 0;
        String reply = respond(sentences[i], contextThatHistory);
        response += "  " + reply;
        logger.debug("Robot: " + reply);
      }
      requestHistory.add(request);
      responseHistory.add(response);
      thatHistory.add(contextThatHistory);
      response = response.replaceAll("[\n]+", "\n");
      response = response.trim();
    } catch (Exception ex) {
      ex.printStackTrace();
      return MagicStrings.error_bot_response;
    }

    if (doWrites) {
      bot.writeLearnfIFCategories();
    }
    logger.debug("in chat.multisentenceRespond(), returning: " + response);
    return response;
  }
}
