package com.martishyn.week3_dsa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DsaTests {


    @Test
    public void shouldReverseString() {
        String actual = DsaFirstDay.reverseString("hello");
        String expected = "olleh";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnIndexesOfSumOfTarget() {
        int target = 9;
        int[] array = new int[] {2,7,11,15};
        int[] result = DsaFirstDay.twoSum(array, target);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(1, result[1]);
    }

    @Test
    public void shouldCheckIfPalindrome() {
        boolean actual1 = DsaFirstDay.isPalindrome("hello");
        boolean actual2 = DsaFirstDay.isPalindrome("racecar");
        boolean actual3 = DsaFirstDay.isPalindrome("rbbr");
        Assertions.assertFalse(actual1);
        Assertions.assertTrue(actual2);
        Assertions.assertTrue(actual3);
    }
}
