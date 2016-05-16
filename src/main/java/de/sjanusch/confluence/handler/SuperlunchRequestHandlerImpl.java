package de.sjanusch.confluence.handler;

import com.google.inject.Inject;
import de.sjanusch.confluence.rest.SuperlunchRestClient;
import de.sjanusch.model.superlunch.Lunch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 19:42
 */
public class SuperlunchRequestHandlerImpl implements SuperlunchRequestHandler {

    public static final Logger logger = LoggerFactory.getLogger(SuperlunchRequestHandlerImpl.class);

    private final SuperlunchRestClient superlunchRestClient;

    @Inject
    public SuperlunchRequestHandlerImpl(final SuperlunchRestClient superlunchRestClient) {
        this.superlunchRestClient = superlunchRestClient;
    }

    @Override
    public void signInForLunch(final String id, final String username) {

    }

    @Override
    public List<Lunch> fetchLunchFromConfluence() {
        return superlunchRestClient.superlunchRestApiGet();
    }

    @Override
    public List<Lunch> fetchFilteredLunchFromConfluence() {
        List<Lunch> filteredLunches = new LinkedList<Lunch>();
        try {
            List<Lunch> lunches = superlunchRestClient.superlunchRestApiGet();
            if (lunches != null) {
                for (Lunch lunch : lunches) {
                    if (this.isLunchToday(lunch)) {
                        filteredLunches.add(lunch);
                    }
                }
            }
        } catch (ParseException e) {
            logger.error("Fehler beim Confluence Rest-Call: fetchFilteredLunchFromConfluence");
            logger.error(e.getMessage());
        }
        return filteredLunches;
    }

    private boolean isLunchToday(final Lunch lunch) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        return lunch.getDate().equals(String.valueOf(todayWithZeroTime.getTime()));
    }
}
