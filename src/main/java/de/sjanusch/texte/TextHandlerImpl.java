package de.sjanusch.texte;

import com.google.inject.Inject;
import de.sjanusch.configuration.TexteConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandro Janusch
 * Date: 17.05.16
 * Time: 11:58
 */
public class TextHandlerImpl implements TextHandler {

    private static final Logger logger = LoggerFactory.getLogger(TextHandlerImpl.class);

    private final TexteConfiguration texteConfiguration;

    @Inject
    public TextHandlerImpl(final TexteConfiguration texteConfiguration) {
        this.texteConfiguration = texteConfiguration;
    }

    public String getRandomGeneratedText() {
        try {
            return this.getRandomText(texteConfiguration.getRandomTexteAsList());
        } catch (IOException e) {
            logger.error("Error loading Configuration: " + e.getMessage());
        }
        return null;
    }

    public String getRandomText(final String text) {
        try {
            return this.getText(text, texteConfiguration.getRandomTexteAsList());
        } catch (IOException e) {
            logger.error("Error loading Configuration: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getThankYouText() {
        try {
            return this.getText(texteConfiguration.getThankYouTexteAsList());
        } catch (IOException e) {
            logger.error("Error loading Configuration: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getHelloText() {
        try {
            return this.getText(texteConfiguration.getHelloTexteAsList());
        } catch (IOException e) {
            logger.error("Error loading Configuration: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean containsHelloText(final String text) {
        try {
            for (String hello : texteConfiguration.getHelloTexteAsList()) {
                if (this.containsWord(text.toLowerCase().trim(), hello.toLowerCase().trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.error("Error loading Configuration: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String getByeText() {
        try {
            return this.getText(texteConfiguration.getByeTexteAsList());
        } catch (IOException e) {
            logger.error("Error loading Configuration: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean containsByeText(final String text) {
        try {
            for (String bye : texteConfiguration.getByeTexteAsList()) {
                if (this.containsWord(text.toLowerCase().trim(), bye.toLowerCase().trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.error("Error loading Configuration: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean containsHelpCommand(final String text) {
        final String compare = text.toLowerCase().trim();
        if (text.startsWith("/") && (compare.contains("help") || compare.contains("hilfe") || compare.contains("befehle"))) {
            return true;
        }
        return false;
    }

    @Override
    public String getHelpText() {
        return null;
    }

    private String getText(final String text, final List<String> texts) {
        int number = this.getRandomNumberInRange(0, texts.size());
        if (number >= 0 && number < texts.size() && !texts.contains(text)) {
            return texts.get(number);
        }
        return this.getText(text, texts);
    }

    private String getText(final List<String> texts) {
        int number = this.getRandomNumberInRange(0, texts.size());
        if (number >= 0 && number < texts.size()) {
            return texts.get(number);
        }
        return this.getText(texts);
    }

    private String getRandomText(final List<String> texts) {
        if (this.getRandomTrueFalse()) {
            return this.getText(texts);
        }
        return null;
    }

    private boolean getRandomTrueFalse() {
        final int i = getRandomNumberInRange(0, 3);
        return (i == 1) ? true : false;
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private boolean containsWord(final String sentence, final String word) {
        return sentence.indexOf(word) != -1;
    }

}
