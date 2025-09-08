package com.martishyn.week3_dsa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DsaSecondDayTest {

    // 1. Kadane’s Algorithm
    @Test
    void testMaxSubArray() {
        assertEquals(6, DsaSecondDay.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4})); // [4, -1, 2, 1]
        assertEquals(1, DsaSecondDay.maxSubArray(new int[]{1}));
        assertEquals(23, DsaSecondDay.maxSubArray(new int[]{5, 4, -1, 7, 8}));
    }

    // 2. Anagram check
    @Test
    void testIsAnagram() {
        assertTrue(DsaSecondDay.isAnagram("listen", "silent"));
        assertTrue(DsaSecondDay.isAnagram("anagram", "nagaram"));
        assertFalse(DsaSecondDay.isAnagram("rat", "car"));
    }

    // 3. Longest substring without repeating chars
    @Test
    void testLengthOfLongestSubstring() {
        assertEquals(3, DsaSecondDay.lengthOfLongestSubstring("abcabcbb")); // "abc"
        assertEquals(1, DsaSecondDay.lengthOfLongestSubstring("bbbbb"));    // "b"
        assertEquals(3, DsaSecondDay.lengthOfLongestSubstring("pwwkew"));   // "wke"
        assertEquals(0, DsaSecondDay.lengthOfLongestSubstring(""));         // empty string
    }

    // 4. Move zeroes
    @Test
    void testMoveZeroes() {
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, DsaSecondDay.moveZeroes(new int[]{0, 1, 0, 3, 12}));
        assertArrayEquals(new int[]{0}, DsaSecondDay.moveZeroes(new int[]{0}));
        assertArrayEquals(new int[]{1}, DsaSecondDay.moveZeroes(new int[]{1}));
    }

    // 5. Valid parentheses
    @Test
    void testIsValid() {
        assertTrue(DsaSecondDay.isValid("()"));
        assertTrue(DsaSecondDay.isValid("()[]{}"));
        assertFalse(DsaSecondDay.isValid("(]"));
        assertFalse(DsaSecondDay.isValid("([)]"));
        assertTrue(DsaSecondDay.isValid("{[]}"));
        assertFalse(DsaSecondDay.isValid("]")); // починається з закриваючої
    }
}
