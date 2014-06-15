package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 6/15/2014.
 */
public class SimpleCharacterSet {
    private final char startingCharacter;
    private final char endingCharacter;

    public SimpleCharacterSet(char startingCharacter, char endingCharacter) {
        if((int) endingCharacter < (int) startingCharacter)
            throw new RuntimeException(String.format("Ending character (%c) must be >= (%c) starting character",
                    startingCharacter, endingCharacter));

        this.startingCharacter = startingCharacter;
        this.endingCharacter = endingCharacter;
    }

    public int count() {
        return (int) endingCharacter - (int) startingCharacter + 1;
    }

    public char map(int value) throws Exception {
        validateIndex(value);
        value += (int) startingCharacter;
        return (char) value;
    }

    void validateIndex(int value) throws Exception {
        if((value >= count()) || (value < 0))
            throw new Exception(String.format("Random value (%d) out of range, must be between 0 and %d", value, count()));
    }

    public Boolean inRange(char character) {
        return (character >= startingCharacter) && (character <= endingCharacter);
    }
}
