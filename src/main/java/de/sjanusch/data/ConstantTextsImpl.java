package de.sjanusch.data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandro Janusch
 * Date: 17.05.16
 * Time: 11:58
 */
public class ConstantTextsImpl implements ConstantTexts {

    public String getRandomText(final String text) {
        return this.getText(text, RandomTextConstant.TEXT);
    }

    @Override
    public String getThankYouText() {
        return this.getText("", ThankYouTextConstant.TEXT);
    }

    @Override
    public String getHelloText() {
        return this.getText("", HelloTextConstant.TEXT);
    }

    private String getText(final String text, final String[] textArray) {
        int number = this.getRandomNumberInRange(0, textArray.length);
        List<String> randomTextList = Arrays.asList(textArray);
        if (number >= 0 && number < textArray.length && !randomTextList.contains(text)) {
            return textArray[number];
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
