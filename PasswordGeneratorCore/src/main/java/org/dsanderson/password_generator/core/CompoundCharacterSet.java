package org.dsanderson.password_generator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsanderson on 6/15/2014.
 */
public class CompoundCharacterSet extends CharacterSet {
    List<CharacterSet> characterSets = new ArrayList<CharacterSet>();
    List<Integer> weights = new ArrayList<Integer>();
    public static final int DEFAULT_WEIGHT = 100;

    public CompoundCharacterSet add(CharacterSet characterSet) throws Exception {
        return add(characterSet, DEFAULT_WEIGHT);
    }

    public CompoundCharacterSet add(CharacterSet characterSet, int weight) throws Exception {
        if(weight < 0)
            throw new Exception(String.format("Weight (%d) must be > 0", weight));
        characterSets.add(characterSet);
        weights.add(weight);
        return this;
    }

    @Override
    public int getCount() {
        int count = 0;
        for(int i = 0; i < characterSets.size(); i++)
            count += getWeightedCount(i);
        return count;
    }

    int getWeightedCount(int index) {
        return characterSets.get(index).getCount() * weights.get(index);
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
        for(int i = 0; i < characterSets.size(); i++) {
            int weightedCount = getWeightedCount(i);
            if(remainingIndex < weightedCount) {
                int weight = weights.get(i);
                return characterSets.get(i).map(unweightValue(remainingIndex, weight));
            } else {
                remainingIndex -= weightedCount;
            }
        }
        return ' ';
    }

    int unweightValue(int value, int weight) {
        return value / weight;
    }
}
