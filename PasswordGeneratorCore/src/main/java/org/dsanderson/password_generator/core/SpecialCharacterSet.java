package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 6/15/2014.
 */
public class SpecialCharacterSet extends CharacterSet {
    CompoundCharacterSet characterSets = new CompoundCharacterSet();

    public SpecialCharacterSet() {
        characterSets.add(new SimpleCharacterSet('!', '/'))
                .add(new SimpleCharacterSet(':', '@'))
                .add(new SimpleCharacterSet('[', '`'))
                .add(new SimpleCharacterSet('{', '~'));
    }

    @Override
    public int getCount() {
        return characterSets.getCount();
    }

    @Override
    public Boolean inRange(char character) {
        return characterSets.inRange(character);
    }

    @Override
    public char map(int value) throws Exception {
        return characterSets.map(value);
    }
}
