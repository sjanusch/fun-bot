package de.sjanusch.texte;

public interface TextHandler {

    String getRandomText(final String text);

    String getThankYouText();

    String getHelloText();

    String getByeText();

    boolean containsHelloText(final String text);

    boolean containsByeText(final String text);
}
