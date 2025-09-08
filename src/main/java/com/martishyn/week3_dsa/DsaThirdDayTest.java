package com.martishyn.week3_dsa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DsaThirdDayTest {

    @Test
    void testIntersection() {
        int[] result = DsaThirdDay.intersection(
                new int[]{1, 2, 2, 1}, new int[]{2, 2}
        );
        assertArrayEquals(new int[]{2}, result);
    }

    @Test
    void testContainsDuplicateTrue() {
        boolean result = DsaThirdDay.containsDuplicate(new int[]{1, 2, 3, 1});
        assertTrue(result);
    }

    @Test
    void testContainsDuplicateFalse() {
        boolean result = DsaThirdDay.containsDuplicate(new int[]{1, 2, 3, 4});
        assertFalse(result);
    }

    @Test
    void testGroupAnagrams() {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> grouped = DsaThirdDay.groupAnagrams(input);

        // Перевіримо розмір і що групи сформовані
        assertEquals(3, grouped.size());
        assertTrue(grouped.stream().anyMatch(g -> g.containsAll(List.of("eat", "tea", "ate"))));
        assertTrue(grouped.stream().anyMatch(g -> g.containsAll(List.of("tan", "nat"))));
        assertTrue(grouped.stream().anyMatch(g -> g.contains("bat")));
    }

    @Test
    void testLongestPalindrome() {
        int result = DsaThirdDay.longestPalindrome("abccccdd");
        assertEquals(7, result);
        int result2 = DsaThirdDay.longestPalindrome("aaabccccdd");
        assertEquals(9, result2);
    }

    @Test
    void testProductExceptSelf() {
        int[] result = DsaThirdDay.productExceptSelf(new int[]{1, 2, 3, 4});
        assertArrayEquals(new int[]{24, 12, 8, 6}, result);

        int[] result2 = DsaThirdDay.productExceptSelfWithoutArrays(new int[]{1, 2, 3, 4});
        assertArrayEquals(new int[]{24, 12, 8, 6}, result2);
    }
}
