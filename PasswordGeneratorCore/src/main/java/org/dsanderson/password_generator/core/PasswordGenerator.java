package org.dsanderson.password_generator.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {
    public static final int DEFAULT_LENGTH = 12;
    final SimpleCharacterSet lowerCaseLetters = new SimpleCharacterSet('a', 'z');
    final SimpleCharacterSet upperCaseLetters = new SimpleCharacterSet('A', 'Z');
    final SimpleCharacterSet numbers = new SimpleCharacterSet('0','9');
    final SpecialCharacterSet specialCharacters = new SpecialCharacterSet(SPECIAL_CHARACTER_WEIGHT);
    static final int NUMBER_OF_ATTEMPTS = 100;
    static final int SPECIAL_CHARACTER_WEIGHT = CompoundCharacterSet.DEFAULT_WEIGHT / 4;
    static final int NUMBER_WEIGHT = CompoundCharacterSet.DEFAULT_WEIGHT * 2;
    static final int KEYWORD_REPLACEMENT_WEIGHT = CompoundCharacterSet.DEFAULT_WEIGHT;
    int iterations = 0;
    CompoundCharacterSet characterSets;
    final SecureRandom random = new SecureRandom();
    final CharacterRandomizer characterRandomizer = new CharacterRandomizer(random);

    class CharacterInfo {
        Boolean lowerCaseLetter = true;
        Boolean upperCaseLetter = true;
        Boolean number = true;
        Boolean specialCharacter = true;

        CharacterInfo copy() {
            CharacterInfo newCharacterInfo = new CharacterInfo();
            newCharacterInfo.update(this);
            return newCharacterInfo;
        }

        void update(CharacterInfo characterInfoIn) {
            lowerCaseLetter = characterInfoIn.lowerCaseLetter;
            upperCaseLetter = characterInfoIn.upperCaseLetter;
            number = characterInfoIn.number;
            specialCharacter = characterInfoIn.specialCharacter;
        }

        Boolean any() {
            return count() > 0;
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
            if(specialCharacter)
                count++;
            return count;
        }
    }

    CharacterInfo characterEnabled = new CharacterInfo();

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

    public PasswordGenerator setSpecialCharactersEnabled(Boolean specialCharactersEnabled) {
        characterEnabled.specialCharacter = specialCharactersEnabled;
        return this;
    }

    public int getIterations() {
        return iterations;
    }

    public String generate() throws Exception {
        return generate(DEFAULT_LENGTH, "");
    }

    public String generate(int length) throws Exception {
        return generate(length, "");
    }

    public String generate(int length, String keyword)  throws Exception {
        validateLength(length, keyword);
        updateCharacterSets(characterEnabled);
        String password;
        iterations = 0;
        CharacterInfo characterNeeded;
        do {
            if(++iterations > NUMBER_OF_ATTEMPTS)
                throw new Exception("Cannot generate password!!!");
            characterNeeded = initializeNeeded();
            password = generatePasswordString(length, keyword, characterNeeded);
        } while(characterNeeded.any());
        return password;
    }

    void validateLength(int length, String keyword) throws Exception {
        int minimumLength = characterEnabled.count();
        if(keyword.length() > 0)
            minimumLength = keyword.length() + minimumLength - 1;
        if(length < minimumLength)
            throw new Exception(String.format("Length too short. Must be at least %d.", minimumLength));
    }

    void updateCharacterSets(CharacterInfo characterEnabled) throws Exception {
        if(characterEnabled.none())
            throw new Exception("Must have at least one character enabled!");
        characterSets = new CompoundCharacterSet();
        if(characterEnabled.lowerCaseLetter)
            characterSets.add(lowerCaseLetters);
        if(characterEnabled.upperCaseLetter)
            characterSets.add(upperCaseLetters);
        if(characterEnabled.number)
            characterSets.add(numbers, NUMBER_WEIGHT);
        if(characterEnabled.specialCharacter)
            characterSets.add(specialCharacters, 1);
    }

    CharacterInfo initializeNeeded() {
        return characterEnabled.copy();
    }

    String generatePasswordString(int length, String keyword,
                                  CharacterInfo characterNeeded) throws Exception {
        CharacterInfo characterEnabled = this.characterEnabled.copy();
        setCharacterRandomizerWeights();
        String randomizedKeyword = characterRandomizer.getRandomString(keyword,
                characterEnabled.specialCharacter);
        String password = "";
        int remaining_length = length;
        for(char c : randomizedKeyword.toCharArray()) {
            updateNeeded(c, characterNeeded);
            remaining_length--;
            updateEnabled(characterEnabled, characterNeeded, remaining_length);
        }
        while (remaining_length > 0) {
            char newCharacter = generateCharacter();
            updateNeeded(newCharacter, characterNeeded);
            remaining_length--;
            updateEnabled(characterEnabled, characterNeeded, remaining_length);
            password += newCharacter;
        }

        password = scrambleStringPassword(password);
        password = insertKeyword(password, randomizedKeyword);

        return password;
    }

    void setCharacterRandomizerWeights() {
        characterRandomizer.setNumberWeight(KEYWORD_REPLACEMENT_WEIGHT)
                .setSpecialCharacterWeight(KEYWORD_REPLACEMENT_WEIGHT);
    }

    char generateCharacter() throws Exception {
        int randomInt = random.nextInt(characterSets.getCount());
        return characterSets.map(randomInt);
    }

    void updateNeeded(char newCharacter, CharacterInfo characterNeeded) {
        if(lowerCaseLetters.inRange(newCharacter)) {
            characterNeeded.lowerCaseLetter = false;
        } else if(upperCaseLetters.inRange(newCharacter)) {
            characterNeeded.upperCaseLetter = false;
        } else if(numbers.inRange(newCharacter)) {
            characterNeeded.number = false;
        } else if(specialCharacters.inRange(newCharacter)) {
            characterNeeded.specialCharacter = false;
        }
    }

    void updateEnabled(CharacterInfo characterEnabled, CharacterInfo characterNeeded,
                       int remainingLength) throws Exception {
        if((characterNeeded.count() >= remainingLength) && (remainingLength > 0)) {
            characterEnabled.update(characterNeeded);
            updateCharacterSets(characterEnabled);
        }
    }

    String scrambleStringPassword(String password) {
        List<Character> characterList = stringToCharacterList(password);

        int length = characterList.size();
        String newPassword = "";
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(characterList.size());
            newPassword += characterList.get(index);
            characterList.remove(index);
        }
        return newPassword;
    }

    List<Character> stringToCharacterList(String str) {
        List<Character> characterList = new ArrayList<Character>();
        for(char c : str.toCharArray()) {
            characterList.add(c);
        }
        return characterList;
    }

    String insertKeyword(String password, String randomizedKeyword) {
        String newPassword = "";
        int randomizedIndex = random.nextInt(password.length());
        char[] characterArray = password.toCharArray();
        for(int i = 0; i < password.length(); i++) {
            if(i == randomizedIndex)
                newPassword += randomizedKeyword;
            newPassword += characterArray[i];
        }
        return newPassword;
    }

}
