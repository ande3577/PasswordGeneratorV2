package org.dsanderson.password_generator.core.test;

import static org.junit.Assert.*;

import org.dsanderson.password_generator.core.PasswordGenerator;
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
    static final int MINIMUM_LENGTH = 3;
    static final int NUMBER_OF_TEST_RUNS = 100;

    @Test
    public void stringLengthMatches() throws Exception {
        assertEquals(8, passwordGenerator().setLength(8).generate().length());
    }

    @Test
    public void stringIsRandom() throws Exception {
        assertNotEquals(passwordGenerator().generate(), passwordGenerator().generate());
    }

    @Test
    public void stringContainsLowerCaseLetter()  throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertTrue(checkContains("[a-z]", passwordGenerator(MINIMUM_LENGTH).generate()));
    }

    @Test
    public void stringContainsUpperCaseLetter() throws Exception  {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertTrue(checkContains("[A-Z]", passwordGenerator(MINIMUM_LENGTH).generate()));
    }

    @Test
    public void stringContainsNumber() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertTrue(checkContains("[0-9]", passwordGenerator(MINIMUM_LENGTH).generate()));
    }

    @Test
    public void requiresOnlyOneIteration() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGenerator(MINIMUM_LENGTH);
            generator.generate();
            assertEquals(1, generator.getIterations());
        }
    }

    @Test(expected = Exception.class)
    public void throwsExceptionForInvalidLength() throws Exception{
        passwordGenerator(MINIMUM_LENGTH-1).generate();
    }

    @Test
    public void generatesLowerCaseOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled().setLength(1)
                    .setLowerCaseEnabled(true);
            assertTrue(checkContains("[a-z]", generator.generate()));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generatesUpperCaseOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled().setLength(1)
                    .setUpperCaseEnabled(true);
            assertTrue(checkContains("[A-Z]", generator.generate()));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generatesNumberOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled().setLength(1)
                    .setNumberEnabled(true);
            assertTrue(checkContains("[0-9]", generator.generate()));
            assertEquals(1, generator.getIterations());
        }
    }

    PasswordGenerator passwordGenerator() {
        return new PasswordGenerator();
    }

    PasswordGenerator passwordGeneratorWithNoneEnabled() {
        return new PasswordGenerator().setLowerCaseEnabled(false).setUpperCaseEnabled(false)
                .setNumberEnabled(false);
    }

    PasswordGenerator passwordGenerator(int length){
        return passwordGenerator().setLength(length);
    }

    Boolean checkContains(String regex, String password) {
        return Pattern.compile(regex).matcher(password).find();
    }
}
