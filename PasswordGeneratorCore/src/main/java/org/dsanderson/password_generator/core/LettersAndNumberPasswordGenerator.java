package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 7/3/2014.
 */
public class LettersAndNumberPasswordGenerator extends PasswordGenerator {
    public LettersAndNumberPasswordGenerator() {
        setUpperCaseEnabled(true)
                .setLowerCaseEnabled(true)
                .setNumberEnabled(true)
                .setSpecialCharactersEnabled(false);
    }
}
