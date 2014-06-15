package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 6/15/2014.
 */
public class SimpleCharacterSet extends CharacterSet{
    private final char startingCharacter;
    private final char endingCharacter;

    public SimpleCharacterSet(char startingCharacter, char endingCharacter) {
        if((int) endingCharacter < (int) startingCharacter)
            throw new RuntimeException(String.format("Ending character (%c) must be >= (%c) starting character",
                    startingCharacter, endingCharacter));

        this.startingCharacter = startingCharacter;
        this.endingCharacter = endingCharacter;
    }

    @Override
    public int getCount() {
        return (int) endingCharacter - (int) startingCharacter + 1;
    }

    @Override
    public char map(int value) throws Exception {
        validateIndex(value);
        value += (int) startingCharacter;
        return (char) value;
    }

    @Override
    public Boolean inRange(char character) {
        return (character >= startingCharacter) && (character <= endingCharacter);
    }
}
