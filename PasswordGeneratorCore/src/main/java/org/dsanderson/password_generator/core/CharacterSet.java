package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 6/15/2014.
 */
public abstract class CharacterSet {
    abstract int getCount();
    abstract Boolean inRange(char character);
    abstract char map(int value) throws Exception;

    protected void validateIndex(int value) throws Exception {
        int count = getCount();
        if((value >= count) || (value < 0))
            throw new Exception(String.format("Random value (%d) out of range, must be between 0 and %d", value, count));
    }
}
