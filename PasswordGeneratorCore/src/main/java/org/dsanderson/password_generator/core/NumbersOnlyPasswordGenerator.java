package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 7/3/2014.
 */
public class NumbersOnlyPasswordGenerator extends PasswordGenerator {
    public NumbersOnlyPasswordGenerator() {
        setUpperCaseEnabled(false)
                .setLowerCaseEnabled(false)
                .setNumberEnabled(true)
                .setSpecialCharactersEnabled(false);
    }
}
