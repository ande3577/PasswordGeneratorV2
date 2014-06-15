package org.dsanderson.password_generator.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {
    public static final int DEFAULT_LENGTH = 12;
    final SimpleCharacterSet lowerCaseLetters = new SimpleCharacterSet('a', 'z');
    final SimpleCharacterSet upperCaseLetters = new SimpleCharacterSet('A', 'Z');
    final SimpleCharacterSet numbers = new SimpleCharacterSet('0','9');
    static final int NUMBER_OF_ATTEMPTS = 100;
    int length = DEFAULT_LENGTH;
    int iterations = 0;
    List<SimpleCharacterSet> characterSets;

    class CharacterInfo {
        Boolean lowerCaseLetter = true;
        Boolean upperCaseLetter = true;
        Boolean number = true;

        CharacterInfo copy() {
            CharacterInfo newCharacterInfo = new CharacterInfo();
            newCharacterInfo.update(this);
            return newCharacterInfo;
        }

        void update(CharacterInfo characterInfoIn) {
            lowerCaseLetter = characterInfoIn.lowerCaseLetter;
            upperCaseLetter = characterInfoIn.upperCaseLetter;
            number = characterInfoIn.number;
        }

        Boolean any() {
            return lowerCaseLetter || upperCaseLetter || number;
        }

        Boolean none() {
            return !any();
        }

        int count() {
            int count = 0;
            if(lowerCaseLetter)
                count++;
            if(upperCaseLetter)
                count++;
            if(number)
                count++;
            return count;
        }
    }

    CharacterInfo characterEnabled = new CharacterInfo();

    public PasswordGenerator setLength(int length) {
        this.length = length;
        return this;
    }

    public PasswordGenerator setLowerCaseEnabled(Boolean lowerCaseEnabled) {
        characterEnabled.lowerCaseLetter = lowerCaseEnabled;
        return this;
    }

    public PasswordGenerator setUpperCaseEnabled(Boolean upperCaseEnabled) {
        characterEnabled.upperCaseLetter = upperCaseEnabled;
        return this;
    }

    public PasswordGenerator setNumberEnabled(Boolean numberEnabled) {
        characterEnabled.number = numberEnabled;
        return this;
    }

    public int getIterations() {
        return iterations;
    }

    public String generate()  throws Exception {
        validateLength();
        updateCharacterSets(characterEnabled);
        String password;
        iterations = 0;
        CharacterInfo characterNeeded;
        do {
            if(++iterations > NUMBER_OF_ATTEMPTS)
                throw new Exception("Cannot generate password!!!");
            characterNeeded = initializeNeeded();
            password = generatePasswordString(characterNeeded);
        } while(!validateRequiredCharacters(characterNeeded));
        return password;
    }

    void validateLength() throws Exception {
        int minimumLength = characterEnabled.count();
        if(length < minimumLength)
            throw new Exception(String.format("Length too short. Must be at least %d.", minimumLength));
    }

    void updateCharacterSets(CharacterInfo characterEnabled) {
        characterSets = new ArrayList<SimpleCharacterSet>();
        if(characterEnabled.lowerCaseLetter)
            characterSets.add(lowerCaseLetters);
        if(characterEnabled.upperCaseLetter)
            characterSets.add(upperCaseLetters);
        if(characterEnabled.number)
            characterSets.add(numbers);
    }

    CharacterInfo initializeNeeded() {
        return characterEnabled.copy();
    }

    String generatePasswordString(CharacterInfo characterNeeded) throws Exception {
        CharacterInfo characterEnabled = this.characterEnabled.copy();
        int remaining_length = length;
        String password = "";
        for (int i = 0; i < length; i++) {
            char newCharacter = generateCharacter(characterEnabled);
            updateNeeded(newCharacter, characterNeeded);
            remaining_length--;
            updateEnabled(characterEnabled, characterNeeded, remaining_length);
            password += newCharacter;
        }
        return password;
    }

    char generateCharacter(CharacterInfo characterEnabled) throws Exception {
        int randomInt = getRandomInt(characterEnabled);
        return randomIntToChar(randomInt, characterEnabled);
    }

    int getRandomInt(CharacterInfo characterEnabled) throws Exception {
        SecureRandom random = new SecureRandom();
        return random.nextInt(getRandomIntCount(characterEnabled));
    }

    int getRandomIntCount(CharacterInfo characterEnabled) throws Exception {
        if(characterEnabled.none())
            throw new Exception("Must have at least one character enabled!");

        int randomCount = 0;
        if(characterEnabled.lowerCaseLetter)
            randomCount += lowerCaseLetters.count();
        if(characterEnabled.upperCaseLetter)
            randomCount += upperCaseLetters.count();
        if(characterEnabled.number)
            randomCount += numbers.count();
        return randomCount;
    }

    char randomIntToChar(int randomInt, CharacterInfo characterEnabled) throws Exception {
        int newRandomInt = randomInt;
        for(SimpleCharacterSet characterSet : characterSets) {
            if(newRandomInt < characterSet.count())
                return characterSet.map(newRandomInt);
            else
                newRandomInt -= characterSet.count();
        }
        return ' ';
    }

    void updateNeeded(char newCharacter, CharacterInfo characterNeeded) {
        if(isLowerCaseCharacter(newCharacter)) {
            characterNeeded.lowerCaseLetter = false;
        } else if(isUpperCaseCharacter(newCharacter)) {
            characterNeeded.upperCaseLetter = false;
        } else if(isNumber(newCharacter)) {
            characterNeeded.number = false;
        }
    }

    Boolean isLowerCaseCharacter(char character) {
        return lowerCaseLetters.inRange(character);
    }

    Boolean isUpperCaseCharacter(char character) {
        return upperCaseLetters.inRange(character);
    }

    Boolean isNumber(char character) {
        return numbers.inRange(character);
    }

    void updateEnabled(CharacterInfo characterEnabled, CharacterInfo characterNeeded,
                       int remainingLength) {
        if(characterNeeded.count() >= remainingLength) {
            characterEnabled.update(characterNeeded);
            updateCharacterSets(characterEnabled);
        }
    }

    Boolean validateRequiredCharacters(CharacterInfo characterNeeded) {
        return characterNeeded.none();
    }
}
