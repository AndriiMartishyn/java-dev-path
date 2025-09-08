package com.martishyn.week3_dsa;

import com.sun.source.tree.BinaryTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DsaThirdDay {

    /**
     * 1. Intersection of Two Arrays
     * nums1 = [1,2,2,1], nums2 = [2,2] → [2]
     */
    public static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> firstSet = new HashSet<>();
        for (int i = 0; i < nums1.length; i++) {
            firstSet.add(nums1[i]); // 1, 2
        }
        Set<Integer> secondSet = new HashSet<>();
        for (int i = 0; i < nums2.length; i++) {
            secondSet.add(nums2[i]);
        }
        firstSet.retainAll(secondSet);
        return firstSet.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * 2. Contains Duplicate
     * nums = [1,2,3,1] → true
     */
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }

    /**
     * 3. Group Anagrams
     * ["eat","tea","tan","ate","nat","bat"] →
     * [["eat","tea","ate"],["tan","nat"],["bat"]]
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> result = new HashMap<>();
        for (int i = 0; i < strs.length; i++) {
            // eat -> [e,a,t] -> sort -> [a,t,e] -> toString -> to Map ???
            String str = strs[i];
            char[] charArray = str.toCharArray();
            Arrays.sort(charArray);
            if (result.containsKey(String.valueOf(charArray))) {
                result.get(String.valueOf(charArray)).add(str);
            } else {
                List<String> list = new ArrayList<>();
                list.add(str);
                result.put(String.valueOf(charArray), list);
            }
        }
        return result.values().stream().toList();
    }

    /**
     * 4. Longest Palindrome (from letters)
     * "abccccdd" → 7
     */
    public static int longestPalindrome(String s) {
        Map<Character, Integer> charCount = new HashMap<>();
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            charCount.put(currentChar, charCount.getOrDefault(currentChar, 0) + 1);
            if (charCount.get(currentChar) % 2 == 0) {
                result += 2;
            }
        }
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            if (entry.getValue() % 2 == 1) {
                result += 1;
                break;
            }
        }
        return result;
    }

    /**
     * 5. Product of Array Except Self
     * nums = [1,2,3,4] → [24,12,8,6]
     * <p>
     * nums    -> [ 1 , 2 , 3 , 4]
     * prefix ->  [ 1 , 1 , 2 , 6]
     * postfix -> [ 24,24 ,12 ,4]
     * <p>
     * result -> [            2 * 4  ,6]
     */
    public static int[] productExceptSelf(int[] nums) {
        int length = nums.length;
        int[] left_products = new int[length];
        int[] right_products = new int[length];
        int[] result = new int[length];

        left_products[0] = 1;
        right_products[length - 1] = 1;
        for (int i = 1; i < length; i++) {
            left_products[i] = nums[i-1] * left_products[i-1]; // 1 1 2 6
        }
        for (int i = length - 2; i >= 0; i--) {
            right_products[i] = nums[i + 1] * right_products[i + 1]; // 24 12 4 1
        }
        for (int i = 0; i < length; i++) {
            result[i] = left_products[i] * right_products[i];
        }
        return result;
    }

    public static int[] productExceptSelfWithoutArrays(int[] nums) {
        int length = nums.length;

        int[] result = new int[length];

        result[0] = 1;
        for (int i = 1; i < length; i++) {
            result[i] = nums[i-1] * result[i-1]; // 1 1 2 6
        }

        int right = 1;
        for (int i = length - 1; i >= 0; i--) { // 24 12 4 1
            result[i] = result[i] * right;
            right = right * nums[i]; //
        }
        return result;
    }
}
