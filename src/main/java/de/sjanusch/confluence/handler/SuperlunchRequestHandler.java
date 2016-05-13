package de.sjanusch.confluence.handler;

import de.sjanusch.model.superlunch.Lunch;

import java.util.List;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 19:42
 */
public interface SuperlunchRequestHandler {

    void signInForLunch(final String id, final String username);

    List<Lunch> fetchLunchFromConfluence();

    List<Lunch> fetchFilteredLunchFromConfluence();

}
