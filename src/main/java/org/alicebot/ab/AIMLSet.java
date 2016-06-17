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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * implements AIML Sets
 */
public class AIMLSet extends HashSet<String> {

  public String setName;

  int maxLength = 1; // there are no empty sets

  String host; // for external sets

  String botid; // for external sets

  boolean isExternal = false;

  Bot bot;

  private HashSet<String> inCache = new HashSet<String>();

  private HashSet<String> outCache = new HashSet<String>();

  private static final Logger logger = LoggerFactory.getLogger(AIMLSet.class);

  /**
   * constructor
   *
   * @param name name of set
   */
  public AIMLSet(String name, Bot bot) {
    super();
    this.bot = bot;
    this.setName = name.toLowerCase();
    if (setName.equals(MagicStrings.natural_number_set_name)) maxLength = 1;
  }

  public boolean contains(String s) {
    if (isExternal && MagicBooleans.enable_external_sets) {
      if (inCache.contains(s)) return true;
      if (outCache.contains(s)) return false;
      String[] split = s.split(" ");
      if (split.length > maxLength) return false;
      String query = MagicStrings.set_member_string + setName.toUpperCase() + " " + s;
      String response = Sraix.sraix(null, query, "false", null, host, botid, null, "0");
      if (response.equals("true")) {
        inCache.add(s);
        return true;
      } else {
        outCache.add(s);
        return false;
      }
    } else if (setName.equals(MagicStrings.natural_number_set_name)) {
      Pattern numberPattern = Pattern.compile("[0-9]+");
      Matcher numberMatcher = numberPattern.matcher(s);
      Boolean isanumber = numberMatcher.matches();
      return isanumber;
    } else return super.contains(s);
  }

  public void writeAIMLSet() {
    logger.debug("Writing AIML Set " + setName);
    try {
      FileWriter fstream = new FileWriter(bot.getSets_path() + "/" + setName + ".txt");
      BufferedWriter out = new BufferedWriter(fstream);
      for (String p : this) {
        out.write(p.trim());
        out.newLine();
      }
      out.close();
    } catch (Exception e) {
      logger.error("Error writing AIML Set " + e.getMessage());
    }
  }

  public void readAIMLSetFromInputStream(InputStream in, Bot bot) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      while ((strLine = br.readLine()) != null && strLine.length() > 0) {
        if (strLine.startsWith("external")) {
          String[] splitLine = strLine.split(":");
          if (splitLine.length >= 4) {
            host = splitLine[1];
            botid = splitLine[2];
            maxLength = Integer.parseInt(splitLine[3]);
            isExternal = true;
            logger.debug("Created external set at " + host + " " + botid);
          }
        } else {
          strLine = strLine.toUpperCase().trim();
          String[] splitLine = strLine.split(" ");
          int length = splitLine.length;
          if (length > maxLength) maxLength = length;
          add(strLine.trim());
        }
      }
    } catch (Exception ex) {
      logger.error("Error reading AIML Set Inpustream " + ex.getMessage());
    }
  }

  public void readAIMLSet(Bot bot) {
    logger.debug("Reading AIML Set " + bot.getSets_path() + "/" + setName + ".txt");
    try {
      File file = new File(bot.getSets_path() + "/" + setName + ".txt");
      if (file.exists()) {
        FileInputStream fstream = new FileInputStream(bot.getSets_path() + "/" + setName + ".txt");
        readAIMLSetFromInputStream(fstream, bot);
        fstream.close();
      } else System.out.println(bot.getSets_path() + "/" + setName + ".txt not found");
    } catch (Exception e) {
      logger.error("Error reading AIML Set " + e.getMessage());
    }
  }
}
