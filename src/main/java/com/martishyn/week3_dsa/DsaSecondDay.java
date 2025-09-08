package com.martishyn.week3_dsa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class DsaSecondDay {

    // 1. Kadane’s Algorithm
    public static int maxSubArray(int[] nums) {
        int currentMax = nums[0];
        int globalMax = nums[0];
        for (int i = 1; i < nums.length; i++) {
            currentMax = Math.max(currentMax + nums[i], nums[i]);
            globalMax = Math.max(globalMax, currentMax);
        }
        return globalMax;
    }

    // 2. Anagram check
    public static boolean isAnagram(String s, String t) {
        //silent - listen or secure -> rescue
        Set<Character> firstWord = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            firstWord.add(s.charAt(i));
        }
        for (int i = 0; i < s.length(); i++) {
            if (!firstWord.contains(t.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    // 3. Longest substring without repeating chars - //abcabcbb//
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> characterSet = new HashSet<>();
        int l = 0;
        int result = 0;
        for (int r = 0; r < s.length(); r++) {
            char currentChar = s.charAt(r);
            while (characterSet.contains(currentChar)) {
                characterSet.remove(s.charAt(l));
                l++;
            }
            characterSet.add(currentChar);
            result= Math.max(result, r - l + 1);
        }
        return result;
    }


    // 4. Move zeroes Example: [0,1,0,3,12] → [1,3,12,0,0].
    public static int[] moveZeroes(int[] nums) {
        int insertPos = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[insertPos] = nums[i];
                if (nums.length != 1) {
                    nums[i] = 0;
                }
                insertPos++;
            }
        }
        return nums;
    }

    // 5. Valid parentheses Example: "()[]{}" → true, "([)]" → false
    public static boolean isValid(String s) {
        Stack<Character> validStack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            var currentSymbol = s.charAt(i);
            if (currentSymbol == '[' || currentSymbol == '{' || currentSymbol == '(') {
                validStack.push(currentSymbol);
            } else if (currentSymbol == ']' || currentSymbol == '}' || currentSymbol == ')') {
                if (validStack.isEmpty()) {
                    return false;
                }
                Character topCharacter = validStack.pop();
                if (currentSymbol == ']' && topCharacter != '['
                        || currentSymbol == '}' && topCharacter != '{'
                        || currentSymbol == ')' && topCharacter != '(') {
                    return false;
                }
            }
        }
        return validStack.isEmpty();
    }

}

