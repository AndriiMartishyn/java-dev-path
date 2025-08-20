package com.martishyn.week3_dsa;

import java.util.HashMap;
import java.util.Map;

public class DsaFirstDay {

    /**
     * 1. Reverse String
     * Напиши метод, який приймає рядок і повертає його у зворотному порядку.
     * Приклад: "hello" -> "olleh"
     */
    public static String reverseString(String input) {
        StringBuilder reversed = new StringBuilder();
        for (int i = input.length() - 1; i >= 0; i--){
            reversed.append(input.charAt(i));
        }
        return reversed.toString();
    }

    /**
     * 2. Two Sum
     * Є масив цілих чисел і число target.
     * Потрібно знайти два індекси чисел у масиві, сума яких дорівнює target.
     * Приклад: nums = [2,7,11,15], target = 9 -> повернути [0,1]
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numToIndex = new HashMap<>();
        for (int i = 0; i < nums.length; i++){
            numToIndex.put(nums[i], i);
        }
        int[] result = new int[2];
        for (Map.Entry<Integer, Integer> entry :  numToIndex.entrySet()){
            int diff = target - entry.getKey();
            result[0] = entry.getValue();
            result[1] = numToIndex.get(diff);
            break;
        }
        return result;
    }

    /**
     * 3. Palindrome Check
     * Перевіряє, чи рядок є паліндромом.
     * Паліндром читається однаково вперед і назад.
     * Приклад: "racecar" -> true, "hello" -> false
     */
    public static boolean isPalindrome(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = input.length() - 1; i >= 0; i--) {
            result.append(input.charAt(i));
        }
        return input.contentEquals(result);
    }
}
