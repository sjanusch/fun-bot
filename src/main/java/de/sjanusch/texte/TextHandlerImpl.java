package de.sjanusch.texte;

import com.google.inject.Inject;
import de.sjanusch.configuration.TexteConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandro Janusch
 * Date: 17.05.16
 * Time: 11:58
 */
public class TextHandlerImpl implements TextHandler {

    private final TexteConfiguration texteConfiguration;

    @Inject
    public TextHandlerImpl(final TexteConfiguration texteConfiguration) {
        this.texteConfiguration = texteConfiguration;
    }

    public String getRandomText(final String text) {
        try {
            return this.getText(text, texteConfiguration.getRandomTexteAsList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getThankYouText() {
        try {
            return this.getText(texteConfiguration.getThankYouTexteAsList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getHelloText() {
        try {
            return this.getText(texteConfiguration.getHelloTexteAsList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getByeText() {
        try {
            return this.getText(texteConfiguration.getByeTexteAsList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getText(final String text, final List<String> texts) {
        int number = this.getRandomNumberInRange(0, texts.size());
        if (number >= 0 && number < texts.size() && !texts.contains(text)) {
            return texts.get(number);
        }
        return null;
    }

    private String getText(final List<String> texts) {
        int number = this.getRandomNumberInRange(0, texts.size());
        if (number >= 0 && number < texts.size()) {
            return texts.get(number);
        }
        return null;
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
