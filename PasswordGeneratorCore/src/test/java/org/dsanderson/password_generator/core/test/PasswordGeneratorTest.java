package org.dsanderson.password_generator.core.test;

import static org.junit.Assert.*;

import org.dsanderson.password_generator.core.PasswordGenerator;
import org.dsanderson.password_generator.core.SpecialCharacterSet;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class containsSpecialCharacter extends BaseMatcher {

    public containsSpecialCharacter() {
    }

    @Override
    public boolean matches(Object item) {
        SpecialCharacterSet characterSet = new SpecialCharacterSet();
        for(char c : ((String) item).toCharArray()) {
            if(characterSet.inRange(c))
                return true;
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("contains special character=");
    }
    public static containsSpecialCharacter matches(){
        return new containsSpecialCharacter();
    }

}

/**
 * Created by dsanderson on 6/15/2014.
 */
@RunWith(JUnit4.class)
public class PasswordGeneratorTest {
    static final int MINIMUM_LENGTH = 4;
    static final int NUMBER_OF_TEST_RUNS = 100;
    static final String KEYWORD = "key word";
    static final String KEYWORD_PATTERN = "[Kk][Ee3][Yy4][\\_\\-\\,\\.][Ww][Oo0][Rr][Dd]";

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
            assertThat(passwordGenerator().generate(MINIMUM_LENGTH), containsRegex.matches("[a-z]"));
    }

    @Test
    public void stringContainsUpperCaseLetter() throws Exception  {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertThat(passwordGenerator().generate(MINIMUM_LENGTH), containsRegex.matches("[A-Z]"));
    }

    @Test
    public void stringContainsNumber() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertThat(passwordGenerator().generate(MINIMUM_LENGTH), containsRegex.matches("[0-9]"));
    }

    @Test
    public void stringContainsSpecialCharacter() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++)
            assertThat(passwordGenerator().generate(MINIMUM_LENGTH), containsSpecialCharacter.matches());
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
            assertThat(generator.generate(1), containsRegex.matches("[a-z]"));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generatesUpperCaseOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled()
                    .setUpperCaseEnabled(true);
            assertThat(generator.generate(1), containsRegex.matches("[A-Z]"));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generatesNumberOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled()
                    .setNumberEnabled(true);
            assertThat(generator.generate(1), containsRegex.matches("[0-9]"));
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void generateSpecialCharacterOnly() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGeneratorWithNoneEnabled()
                    .setSpecialCharactersEnabled(true);
            assertThat(generator.generate(1), containsSpecialCharacter.matches());
            assertEquals(1, generator.getIterations());
        }
    }

    @Test
    public void testKeyword() throws Exception {
        for(int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            PasswordGenerator generator = passwordGenerator();
            int length = KEYWORD.length() + MINIMUM_LENGTH - 1;
            String password = generator.generate(length, KEYWORD);
            assertThat(password, containsRegex.matches(KEYWORD_PATTERN));
            assertEquals(length, password.length());
            assertEquals(1, generator.getIterations());
        }
    }

    @Test(expected = Exception.class)
    public void keywordInsufficientLength() throws Exception {
        int length = KEYWORD.length() + MINIMUM_LENGTH - 2;
        passwordGenerator().generate(length, KEYWORD);
    }

    @Test
    public void keywordIsRandomized() throws Exception {
        assertNotEquals(getKeywordFromPassword(), getKeywordFromPassword());
    }

    String getKeywordFromPassword() throws Exception {
        String password = passwordGenerator().generate(PasswordGenerator.DEFAULT_LENGTH, KEYWORD);
        Matcher matcher = Pattern.compile(KEYWORD_PATTERN).matcher(password);
        matcher.find();
        return matcher.group();
    }

    PasswordGenerator passwordGenerator() {
        return new PasswordGenerator();
    }

    PasswordGenerator passwordGeneratorWithNoneEnabled() {
        return new PasswordGenerator().setLowerCaseEnabled(false).setUpperCaseEnabled(false)
                .setNumberEnabled(false).setSpecialCharactersEnabled(false);
    }

}
