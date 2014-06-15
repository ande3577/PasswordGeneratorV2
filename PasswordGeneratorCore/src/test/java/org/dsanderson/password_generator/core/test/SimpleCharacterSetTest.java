package org.dsanderson.password_generator.core.test;

import org.dsanderson.password_generator.core.SimpleCharacterSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleCharacterSetTest {

    @Test(expected = RuntimeException.class)
    public void constructorThrowsRuntimeExceptionIfOutOfOrder() {
        new SimpleCharacterSet('z', 'a');
    }

    @Test
    public void testCount() throws Exception {
        assertEquals(26, characterSet().getCount());
    }

    @Test
    public void testMap() throws Exception {
        assertEquals('a', characterSet().map(0));
        assertEquals('z', characterSet().map(25));
    }

    @Test(expected = Exception.class)
    public void testMapThrowsExceptionForGreaterThanCount() throws Exception {
        SimpleCharacterSet characterSet = this.characterSet();
        characterSet.map(characterSet.getCount());
    }

    @Test(expected = Exception.class)
    public void testMapThrowsForLessThanZero() throws Exception {
        SimpleCharacterSet characterSet = this.characterSet();
        characterSet.map(-1);
    }

    @Test
    public void testInRange() throws Exception {
        assertFalse(characterSet().inRange((char) ('a' - 1)));
        assertTrue(characterSet().inRange('a'));
        assertTrue(characterSet().inRange('z'));
        assertFalse(characterSet().inRange((char) ('z' + 1)));
    }

    SimpleCharacterSet characterSet() throws Exception {
        return new SimpleCharacterSet('a', 'z');
    }
}