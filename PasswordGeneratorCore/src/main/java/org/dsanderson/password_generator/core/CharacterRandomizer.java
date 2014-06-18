package org.dsanderson.password_generator.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * Created by dsanderson on 6/16/2014.
 */
public class CharacterRandomizer {
    final Random random;
    int lowerCaseWeight = CompoundCharacterSet.DEFAULT_WEIGHT;
    int upperCaseWeight = CompoundCharacterSet.DEFAULT_WEIGHT;
    int numberWeight = CompoundCharacterSet.DEFAULT_WEIGHT;
    int specialCharacterWeight = CompoundCharacterSet.DEFAULT_WEIGHT;

    public CharacterRandomizer(Random random) {
       this.random = random;
    }

    public CharacterRandomizer setLowerCaseWeight(int weight) {
        lowerCaseWeight = weight;
        return this;
    }

    public CharacterRandomizer setUpperCaseWeight(int weight) {
        upperCaseWeight = weight;
        return this;
    }

    public CharacterRandomizer setNumberWeight(int weight) {
        numberWeight = weight;
        return this;
    }

    public CharacterRandomizer setSpecialCharacterWeight(int weight) {
        specialCharacterWeight = weight;
        return this;
    }

    public String getRandomString(String string, boolean specialCharactersEnable) throws Exception {
        String outputString = "";
        for(char c : string.toCharArray()) {
            outputString += getCharacter(c, specialCharactersEnable);
        }
        return outputString;
    }

    char getCharacter(char character, boolean specialCharactersEnable) throws Exception {
        CompoundCharacterSet characterSet = new CompoundCharacterSet();
        if(Character.isLetter(character)) {
            addCharacter(Character.toLowerCase(character), characterSet);
            addCharacter(Character.toUpperCase(character), characterSet);
            if(specialCharactersEnable)
                addSpecialCharacterFromLetter(character, characterSet);
        } else if(character == ' ') {
            addSpaceCharacter(characterSet);
        } else {
            addCharacter(character, characterSet);
        }
        int randomIndex = random.nextInt(characterSet.getCount());
        return characterSet.map(randomIndex);
    }

    void addSpaceCharacter(CompoundCharacterSet characterSet) throws Exception {
        List<Character> characters = new ArrayList<Character>();
        characters.add(',');
        characters.add('.');
        characters.add('_');
        characters.add('-');
        addCharacterList(characters, characterSet);
    }

    void addCharacterList(List<Character> characters, CompoundCharacterSet characterSet) throws Exception {
        for(char c : characters) {
            addCharacter(c, characterSet);
        }
    }

    void addCharacter(char c, CompoundCharacterSet characterSet) throws Exception {
        characterSet.add(new FixedCharacterSet(c), getWeight(c));
    }

    int getWeight(char c) {
        if(Character.isLetter(c)) {
            if (Character.isUpperCase(c))
                return upperCaseWeight;
            else
                return lowerCaseWeight;
        }
        else if(Character.isDigit(c)) {
            return numberWeight;
        } else {
            return specialCharacterWeight;
        }
    }

    void addSpecialCharacterFromLetter(char character, CompoundCharacterSet characterSet) throws Exception {
        List<Character> characters = new ArrayList<Character>();
        switch (Character.toLowerCase(character)) {
            case 'a':
                characters.add('@');
                break;
            case 'c':
                characters.add('(');
                break;
            case 'e':
                characters.add('3');
                break;
            case 'g':
                characters.add('6');
                break;
            case 'i':
                characters.add('1');
                break;
            case 'l':
                characters.add('1');
                break;
            case 'o':
                characters.add('0');
                break;
            case 'q':
                characters.add('9');
                break;
            case 's':
                characters.add('5');
                break;
            case 'u':
                characters.add('(');
                break;
            case 'v':
                characters.add('7');
                break;
            case 'y':
                characters.add('4');
                break;
        }
        addCharacterList(characters, characterSet);
    }
}
