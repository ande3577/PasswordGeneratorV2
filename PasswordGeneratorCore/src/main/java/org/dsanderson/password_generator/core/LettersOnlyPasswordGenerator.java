package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 7/3/2014.
 */
public class LettersOnlyPasswordGenerator extends PasswordGenerator {
    public LettersOnlyPasswordGenerator() {
        setLowerCaseEnabled(true)
                .setUpperCaseEnabled(true)
                .setNumberEnabled(false)
                .setSpecialCharactersEnabled(false);
    }
}
