package org.dsanderson.password_generator.core.test;

import org.dsanderson.password_generator.core.CompoundCharacterSet;
import org.dsanderson.password_generator.core.SimpleCharacterSet;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompoundCharacterSetTest {

    @Test
    public void testCount() throws Exception {
        Assert.assertEquals(52 * CompoundCharacterSet.DEFAULT_WEIGHT, characterSet().getCount());
    }

    @Test
    public void testCountIfEmpty() throws Exception {
        Assert.assertEquals(0, new CompoundCharacterSet().getCount());
    }

    @Test
    public void testInRange() throws Exception {
        CompoundCharacterSet characterSet = this.characterSet();
        assertFalse(characterSet.inRange((char)('a' - 1)));
        assertTrue(characterSet.inRange('a'));
        assertTrue(characterSet.inRange('Z'));
    }

    @Test
    public void testMap() throws Exception {
        CompoundCharacterSet characterSet = this.characterSet();
        assertEquals('a', characterSet.map(0));
        assertEquals('Z', characterSet.map(characterSet.getCount() - 1));
    }

    @Test(expected=Exception.class)
    public void mapThrowsForGreaterThanOrEqualToCount() throws Exception {
        CompoundCharacterSet characterSet = this.characterSet();
        characterSet.map(characterSet.getCount());
    }

    @Test(expected=Exception.class)
    public void mapThrowsForLessThanZero() throws Exception {
        CompoundCharacterSet characterSet = this.characterSet();
        characterSet.map(-1);
    }

    CompoundCharacterSet characterSet() throws Exception {
        CompoundCharacterSet characterSet = new CompoundCharacterSet();
        characterSet.add(new SimpleCharacterSet('a', 'z'));
        characterSet.add(new SimpleCharacterSet('A', 'Z'));
        return characterSet;
    }
}