package org.dsanderson.password_generator.core.test;

import static org.junit.Assert.*;

import org.dsanderson.password_generator.core.PasswordGenerator;
import org.dsanderson.password_generator.core.SpecialCharacterSet;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.regex.Pattern;

/**
 * Created by dsanderson on 6/15/2014.
 */
@RunWith(JUnit4.class)
public class PasswordGeneratorTest {
    static final int MINIMUM_LENGTH = 4;
    static final int NUMBER_OF_TEST_RUNS = 100;

    @Test
    public void stringLengthMatches() throws Exception {
        assertEquals(8, passwordGenerator().generate(8).length());
    }

    @Test
    public void stringIsRandom() throws Exception {
        assertNotEquals(passwordGenerator().generate(), passwordGenerator().generate());
    }

    @Test
    public void stringContainsLowerCaseLetter()  throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertTrue(checkContains("[a-z]", passwordGenerator().generate(MINIMUM_LENGTH)));
    }

    @Test
    public void stringContainsUpperCaseLetter() throws Exception  {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertTrue(checkContains("[A-Z]", passwordGenerator().generate(MINIMUM_LENGTH)));
    }

    @Test
    public void stringContainsNumber() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertTrue(checkContains("[0-9]", passwordGenerator().generate(MINIMUM_LENGTH)));
    }

    @Test
    public void stringContainsSpecialCharacter() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertTrue(checkContainsSpecialCharacter(passwordGenerator().generate(MINIMUM_LENGTH)));
    }

    @Test
    public void requiresOnlyOneIteration() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGenerator();
            generator.generate(MINIMUM_LENGTH);
            assertEquals(1, generator.getIterations());
        }
    }

    @Test(expected = Exception.class)
    public void throwsExceptionForInvalidLength() throws Exception{
        passwordGenerator().generate(MINIMUM_LENGTH-1);
    }

    @Test
    public void generatesLowerCaseOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled()
                    .setLowerCaseEnabled(true);
            assertTrue(checkContains("[a-z]", generator.generate(1)));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generatesUpperCaseOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled()
                    .setUpperCaseEnabled(true);
            assertTrue(checkContains("[A-Z]", generator.generate(1)));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generatesNumberOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled()
                    .setNumberEnabled(true);
            assertTrue(checkContains("[0-9]", generator.generate(1)));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generateSpecialCharacterOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled()
                    .setSpecialCharactersEnabled(true);
            assertTrue(checkContainsSpecialCharacter(generator.generate(1)));
            assertEquals(1, generator.getIterations());
        }
    }

    PasswordGenerator passwordGenerator() {
        return new PasswordGenerator();
    }

    PasswordGenerator passwordGeneratorWithNoneEnabled() {
        return new PasswordGenerator().setLowerCaseEnabled(false).setUpperCaseEnabled(false)
                .setNumberEnabled(false).setSpecialCharactersEnabled(false);
    }

    Boolean checkContains(String regex, String password) {
        return Pattern.compile(regex).matcher(password).find();
    }

    Boolean checkContainsSpecialCharacter(String password) {
        SpecialCharacterSet characterSet = new SpecialCharacterSet();
        for(char c : password.toCharArray()) {
            if(characterSet.inRange(c))
                return true;
        }
        return false;
    }
}
