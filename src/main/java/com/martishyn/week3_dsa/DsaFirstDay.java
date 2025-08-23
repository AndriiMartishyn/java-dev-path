package com.martishyn.week3_dsa;

import com.sun.source.tree.EnhancedForLoopTree;

import java.awt.PageAttributes;
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
            int diff = target - nums[i];
            if (numToIndex.containsKey(diff)){
                return new int[] {numToIndex.get(diff),i};
            }
            numToIndex.put(nums[i], i);
        }
        return new int[0];
    }

    /**
     * 3. Palindrome Check
     * Перевіряє, чи рядок є паліндромом.
     * Паліндром читається однаково вперед і назад.
     * Приклад: "racecar" -> true, "hello" -> false
     */
    public static boolean isPalindrome(String input) {
        int left = 0;
        int right = input.length() - 1;
        while (left < right){
            if (input.charAt(left)!= input.charAt(right)){
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
