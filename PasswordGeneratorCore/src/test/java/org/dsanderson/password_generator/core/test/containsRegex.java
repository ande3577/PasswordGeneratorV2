package org.dsanderson.password_generator.core.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.regex.Pattern;

/**
 * Created by dsanderson on 7/3/2014.
 */
public class containsRegex extends BaseMatcher {
    private final String regex;

    public containsRegex(String regex){
        this.regex = regex;
    }

    public boolean matches(Object o){
        return Pattern.compile(regex).matcher((String) o).find();

    }

    public void describeTo(Description description){
        description.appendText("matches regex=" + regex);
    }

    public static containsRegex matches(String regex){
        return new containsRegex(regex);
    }
}
