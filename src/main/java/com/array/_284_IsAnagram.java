package com.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @Author: wangwei
 * @Description: 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词
 *                相同字符个数要相等
 * @Time: 2019/11/28 周四 09:06
 **/
public class _284_IsAnagram {
    /* s1和s2的长度一定要相等 */
    public static boolean solution1(String s1, String s2){
        if (s1.length() != s2.length()) return false;
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s1.length(); i++) {
            if (!map.containsKey(s1.charAt(i))){
                map.put(s1.charAt(i),1);
            }else {
                Integer integer = map.get(s1.charAt(i));
                map.put(s1.charAt(i),integer+1);
            }
        }
        for (int i = 0; i < s2.length(); i++) {
            if (!map.containsKey(s2.charAt(i))){
                return false;
            }else {
                Integer integer = map.get(s2.charAt(i));
                map.put(s2.charAt(i),integer-1);
            }
        }
        for (Character c : map.keySet()){
            if (map.get(c) != 0) return false;
        }
        return true;
    }
    
    /* 将两字符串转为字符数组再排序，若为字母异位词，则结果应该相等 */
    public static boolean solution2(String s1, String s2){
        if (s1.length() != s2.length()) return false;
        char[] chars1 = s1.toCharArray();
        Arrays.sort(chars1);
        char[] chars2 = s2.toCharArray();
        Arrays.sort(chars2);
        return Arrays.equals(chars1, chars2);
    }
    
    /*  
        可以只利用一个长度为 26 的字符数组，将出现在字符串 s 里的字符个数加 1，
        而出现在字符串 t 里的字符个数减 1，最后判断每个小写字母的个数是否都为 0
        a -- count[0]  b -- count[1]  z -- count[25] -- count['z' - 'a']
        空间复杂度：O(1)O(1)。尽管我们使用了额外的空间，但是空间的复杂性是 O(1)O(1)，
        因为无论 NN 有多大，表的大小都保持不变
        无论字符串多长，都是 a-z ， 26
     */
    public static boolean solution3(String s1, String s2){
        if (s1.length() != s2.length()) return false;
        int[] count = new int[26];
        for (int i = 0; i < s1.length(); i++){
            count[s1.charAt(i) - 'a']++;
            count[s2.charAt(i) - 'a']--;
        }
        for (int i : count){
            if (i != 0) return false;
        }
        return true;
    }

    /**
     * 或者我们可以先用计数器表计算 s1，然后用 s2 减少计数器表中的每个字母的计数器。
     * 如果在任何时候计数器低于零，我们知道 s1 包含一个不在 s2 中的额外字母，并立即返回 FALSE
     */
    public static boolean solution4(String s1, String s2){
        /* 长度一定相等 */
        if (s1.length() != s2.length()) return false;
        int[] count = new int[26];
        for (int i = 0; i < s1.length(); i++){
            count[s1.charAt(i) - 'a']++;
        }
        for (int i = 0; i < s1.length(); i++){
            count[s2.charAt(i) - 'a']--;
            // s2中有个字符，s1中没有
            if (count[s2.charAt(i) - 'a'] < 0) return false;
        }
        return true;
    }

}
