package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 6/17/2014.
 */
public class FixedCharacterSet extends CharacterSet {
    private final char character;

    public FixedCharacterSet(char character) {
        this.character = character;
    }

    @Override
    int getCount() {
        return 1;
    }

    @Override
    Boolean inRange(char character) {
        return character == this.character;
    }

    @Override
    char map(int value) throws Exception {
        return character;
    }
}
