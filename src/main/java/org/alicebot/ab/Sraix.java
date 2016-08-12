package org.alicebot.ab;
/* Program AB Reference AIML 2.0 implementation
        Copyright (C) 2013 ALICE A.I. Foundation
        Contact: info@alicebot.org

        This library is free software; you can redistribute it and/or
        modify it under the terms of the GNU Library General Public
        License as published by the Free Software Foundation; either
        version 2 of the License, or (at your option) any later version.

        This library is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
        Library General Public License for more details.

        You should have received a copy of the GNU Library General Public
        License along with this library; if not, write to the
        Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
        Boston, MA  02110-1301, USA.
*/

import org.alicebot.ab.utils.CalendarUtils;
import org.alicebot.ab.utils.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sraix {
  
  private static final Logger logger = LoggerFactory.getLogger(Sraix.class);
  
  private NetworkUtils networkUtils = new NetworkUtils();
  
  public String sraix(Chat chatSession, String input, String defaultResponse, String hint, String host, String botid, String apiKey, String limit) {
    String response;
    if (!MagicBooleans.enable_network_connection) {
      response = MagicStrings.sraix_failed;
    } else {
      response = this.sraixPannous(input, hint, chatSession);
    }
    logger.debug("Sraix: response = " + response + " defaultResponse = " + defaultResponse);
    if (response.equals(MagicStrings.sraix_failed)) {
      if (chatSession != null && defaultResponse == null) response = AIMLProcessor.respond(MagicStrings.sraix_failed, "nothing", "nothing", chatSession);
      else if (defaultResponse != null) response = defaultResponse;
    }
    return response;
  }
  
  private String sraixPannous(String input, String hint, Chat chatSession) {
    try {
      if (hint == null) hint = MagicStrings.sraix_no_hint;
      input = " " + input + " ";
      input = input.replace(" point ", ".");
      input = input.replace(" rparen ", ")");
      input = input.replace(" lparen ", "(");
      input = input.replace(" slash ", "/");
      input = input.replace(" star ", "*");
      input = input.replace(" dash ", "-");
      // input = chatSession.bot.preProcessor.denormalize(input);
      input = input.trim();
      input = input.replace(" ", "+");
      int offset = CalendarUtils.timeZoneOffset();
      logger.debug("OFFSET = " + offset);
      String locationString = "Germany";
      // https://weannie.pannous.com/api?input=when+is+daylight+savings+time+in+the+us&locale=en_US&login=pandorabots&ip=169.254.178.212&botid=0&key=CKNgaaVLvNcLhDupiJ1R8vtPzHzWc8mhIQDFSYWj&exclude=Dialogues,ChatBot&out=json
      // exclude=Dialogues,ChatBot&out=json&clientFeatures=show-images,reminder,say&debug=true
      
      String url = "https://ask.pannous.com/api?input=" + input + "&locale=de_DE&timeZone=" + offset + locationString + "&login=" + MagicStrings.pannous_login + "&ip=" + networkUtils.localIPAddress() + "&botid=0&key=" + MagicStrings.pannous_api_key + "&exclude=Dialogues,ChatBot&out=json&clientFeatures=show-images,reminder,say&debug=true";
      
      logger.debug("in Sraix.sraixPannous, url: '" + url + "'");
      String page = networkUtils.responseContent(url);
      logger.debug("in Sraix.sraixPannous, page: " + page);
      String text = "";
      String imgRef = "";
      String urlRef = "";
      if (page == null || page.length() == 0) {
        text = MagicStrings.sraix_failed;
      } else {
        JSONArray outputJson = new JSONObject(page).getJSONArray("output");
        logger.debug("in Sraix.sraixPannous, outputJson class: " + outputJson.getClass() + ", outputJson: " + outputJson);
        if (outputJson.length() == 0) {
          text = MagicStrings.sraix_failed;
        } else {
          JSONObject firstHandler = outputJson.getJSONObject(0);
          logger.debug("in Sraix.sraixPannous, firstHandler class: " + firstHandler.getClass() + ", firstHandler: " + firstHandler);
          JSONObject actions = firstHandler.getJSONObject("actions");
          logger.debug("in Sraix.sraixPannous, actions class: " + actions.getClass() + ", actions: " + actions);
          if (actions.has("reminder")) {
            logger.debug("in Sraix.sraixPannous, found reminder action");
            Object obj = actions.get("reminder");
            if (obj instanceof JSONObject) {
              logger.debug("Found JSON Object");
              JSONObject sObj = (JSONObject) obj;
              String duration = sObj.getString("duration");
              logger.debug("duration=" + duration);
            }
          } else if (actions.has("say") && !hint.equals(MagicStrings.sraix_pic_hint) && !hint.equals(MagicStrings.sraix_shopping_hint)) {
            logger.debug("in Sraix.sraixPannous, found say action");
            Object obj = actions.get("say");
            logger.debug("in Sraix.sraixPannous, obj class: " + obj.getClass());
            logger.debug("in Sraix.sraixPannous, obj instanceof JSONObject: " + (obj instanceof JSONObject));
            if (obj instanceof JSONObject) {
              JSONObject sObj = (JSONObject) obj;
              text = sObj.getString("text");
              if (sObj.has("moreText")) {
                JSONArray arr = sObj.getJSONArray("moreText");
                for (int i = 0; i < arr.length(); i++) {
                  text += " " + arr.getString(i);
                }
              }
            } else {
              text = obj.toString();
            }
          }
          if (actions.has("show") && !text.contains("Wolfram")
            && actions.getJSONObject("show").has("images")) {
            logger.debug("in Sraix.sraixPannous, found show action");
            JSONArray arr = actions.getJSONObject("show").getJSONArray(
              "images");
            int i = (int) (arr.length() * Math.random());
            for (int j = 0; j < arr.length(); j++) logger.debug(arr.getString(j));
            imgRef = arr.getString(i);
            if (imgRef.startsWith("//")) imgRef = "http:" + imgRef;
            imgRef = "<a href=\"" + imgRef + "\"><img src=\"" + imgRef + "\"/></a>";
            logger.debug("IMAGE REF=" + imgRef);
            
          }
          if (hint.equals(MagicStrings.sraix_shopping_hint) && actions.has("open") && actions.getJSONObject("open").has("url")) {
            urlRef = "<oob><url>" + actions.getJSONObject("open").getString("url") + "</oob></url>";
            
          }
        }
        if (hint.equals(MagicStrings.sraix_event_hint) && !text.startsWith("<year>")) return MagicStrings.sraix_failed;
        else if (text.equals(MagicStrings.sraix_failed)) return AIMLProcessor.respond(MagicStrings.sraix_failed, "nothing", "nothing", chatSession);
        else {
          text = text.replace("&#39;", "'");
          text = text.replace("&apos;", "'");
          text = text.replaceAll("\\[(.*)\\]", "");
          String[] sentences;
          sentences = text.split("\\. ");
          logger.debug("Sraix: text has " + sentences.length + " sentences:");
          String clippedPage = sentences[0];
          for (int i = 1; i < sentences.length; i++) {
            if (clippedPage.length() < 500) clippedPage = clippedPage + ". " + sentences[i];
            logger.debug(i + ". " + sentences[i]);
          }
          
          clippedPage = clippedPage + " " + imgRef + " " + urlRef;
          clippedPage = clippedPage.trim();
          return clippedPage;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      logger.error("Sraix '" + input + "' failed");
    }
    return MagicStrings.sraix_failed;
  }
}
