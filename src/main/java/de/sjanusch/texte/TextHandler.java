package de.sjanusch.texte;

public interface TextHandler {

    String getRandomText(final String text);

    String getRandomGeneratedText();

    String getThankYouText();

    String getHelloText();

    String getByeText();

    boolean containsHelloText(final String text);

    boolean containsByeText(final String text);

    boolean containsHelpCommand(final String text);

    String getHelpText();

    String getPleaseText();

    boolean containsPleaseText(final String text);

    boolean containsThankYouText(final String text);
}
