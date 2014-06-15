package org.dsanderson.password_generator.core;

import java.security.SecureRandom;

public class PasswordGenerator {
    public static final int DEFAULT_LENGTH = 12;
    static final int NUMBER_OF_LOWER_CASE_LETTERS = (int) 'z' - (int) 'a' + 1;
    static final int NUMBER_OF_UPPER_CASE_LETTERS = (int) 'Z' - (int) 'A' + 1;
    static final int NUMBER_OF_DIGITS = (int) '9' - (int) '0';
    static final int NUMBER_OF_ATTEMPTS = 100;
    int length = DEFAULT_LENGTH;
    int iterations = 0;

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
            randomCount += NUMBER_OF_LOWER_CASE_LETTERS;
        if(characterEnabled.upperCaseLetter)
            randomCount += NUMBER_OF_UPPER_CASE_LETTERS;
        if(characterEnabled.number)
            randomCount += NUMBER_OF_DIGITS;
        return randomCount;
    }

    char randomIntToChar(int randomInt, CharacterInfo characterEnabled) {
        int newRandomInt = randomInt;
        if(characterEnabled.lowerCaseLetter) {
            if(newRandomInt < NUMBER_OF_LOWER_CASE_LETTERS) {
                return mapChar(newRandomInt, 'a');
            } else {
                newRandomInt -= NUMBER_OF_LOWER_CASE_LETTERS;
            }
        }
        if(characterEnabled.upperCaseLetter) {
            if(newRandomInt < NUMBER_OF_UPPER_CASE_LETTERS) {
                return mapChar(newRandomInt, 'A');
            }
            else {
                newRandomInt -= NUMBER_OF_UPPER_CASE_LETTERS;
            }
        }
        if(characterEnabled.number) {
            if(newRandomInt  < NUMBER_OF_DIGITS) {
                return mapChar(newRandomInt, '0');
            } else {
                newRandomInt -= NUMBER_OF_DIGITS;
            }
        }
        return ' ';
    }

    char mapChar(int randomInt, char startingChar) {
        int charInt = randomInt + (int) startingChar;
        return (char) charInt;
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
        return characterInRange(character, 'a', 'z');
    }

    Boolean isUpperCaseCharacter(char character) {
        return characterInRange(character, 'A', 'Z');
    }

    Boolean isNumber(char character) {
        return characterInRange(character, '0', '9');
    }

    Boolean characterInRange(char character, char min, char max) {
        return (int) character >= (int) min && (int) character <= (int) max;
    }

    void updateEnabled(CharacterInfo characterEnabled, CharacterInfo characterNeeded,
                       int remainingLength) {
        if(characterNeeded.count() >= remainingLength)
            characterEnabled.update(characterNeeded);
    }

    Boolean validateRequiredCharacters(CharacterInfo characterNeeded) {
        return characterNeeded.none();
    }
}
