package org.dsanderson.password_generator.core;

/**
 * Created by dsanderson on 6/15/2014.
 */
public class SpecialCharacterSet extends CharacterSet {
    final CompoundCharacterSet characterSets = new CompoundCharacterSet();

    public SpecialCharacterSet() {
        registerCharacterSets(CompoundCharacterSet.DEFAULT_WEIGHT);
    }

    public SpecialCharacterSet(int weight) {
        registerCharacterSets(weight);
    }

    void registerCharacterSets(int weight) {
        try {
            characterSets.add(new SimpleCharacterSet('!', '/'), weight)
                    .add(new SimpleCharacterSet(':', '@'), weight)
                    .add(new SimpleCharacterSet('[', '`'), weight)
                    .add(new SimpleCharacterSet('{', '~'), weight);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
