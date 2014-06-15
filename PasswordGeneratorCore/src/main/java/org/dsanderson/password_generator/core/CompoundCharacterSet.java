package org.dsanderson.password_generator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsanderson on 6/15/2014.
 */
public class CompoundCharacterSet extends CharacterSet {
    List<CharacterSet> characterSets = new ArrayList<CharacterSet>();

    public CompoundCharacterSet add(CharacterSet characterSet) {
        characterSets.add(characterSet);
        return this;
    }

    @Override
    public int getCount() {
        int count = 0;
        for(CharacterSet characterSet : characterSets) {
            count += characterSet.getCount();
        }
        return count;
    }

    @Override
    public Boolean inRange(char character) {
        for(CharacterSet characterSet: characterSets){
            if(characterSet.inRange(character))
                return true;
        }
        return false;
    }

    @Override
    public char map(int value) throws Exception {
        validateIndex(value);
        int remainingIndex = value;
        for(CharacterSet characterSet: characterSets) {
            int count = characterSet.getCount();
            if(remainingIndex < count)
                return characterSet.map(remainingIndex);
            else
                remainingIndex -= count;
        }
        return ' ';
    }
}
