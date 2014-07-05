package org.dsanderson.password_generator.core.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Created by dsanderson on 7/3/2014.
 */
public class RegexMatcher extends BaseMatcher {
    private final String regex;

    public RegexMatcher(String regex){
        this.regex = regex;
    }

    public boolean matches(Object o){
        return ((String)o).matches(regex);

    }

    public void describeTo(Description description){
        description.appendText("matches regex=" + regex);
    }

    public static RegexMatcher matches(String regex){
        return new RegexMatcher(regex);
    }
}
