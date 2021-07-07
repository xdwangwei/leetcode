package com.string;

import com.dp._70_ClimbStairs;

import java.util.HashSet;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/21 周四 08:51
 **/
public class _03_lengthOfLongestNoRepeatSubstring {
    
    // 滑动窗口 [i, j)
    // 如果从索引 i 到 j - 1之间的子字符串 s[i][j]已经被检查为没有重复字符。
    // 我们只需要检查 s[j] 对应的字符是否已经存在于子字符串Sij(set)中
    // 使用 HashSet 将字符存储在当前窗口 [i, j)（最初 j = ij=i）中。 
    // 然后我们向右侧滑动索引 j，如果它不在 HashSet 中，我们会继续滑动 j。直到 s[j] 已经存在于 HashSet 中。
    // 此时，我们找到的没有重复字符的最长子字符串将会以索引 i 开头。对所有的 i这样做，就可以得到答案。
    public static int solution(String s){
        int res = 0;
        HashSet<Character> set = new HashSet<>();
        int i = 0, j = 0;
        int len = s.length();
        while (i < len && j < len){
            if(! set.contains(s.charAt(j))){ // 集合中没有这个字符，加进去
                set.add(s.charAt(j++)); // j 后移
                res = Math.max(res, j - i);
            }else { // 集合里有s[j]这个字符了
                // 之前的最大串长度已保存，删除集合开始元素到与s[j]一样的字符的所有字符，从它之后判断其他串，i后移
                // 如 aebcdbpmn ,[a,d)->[a,第二个b)->删除第一个b及之前的字符[c,b),j++
                set.remove(s.charAt(i++)); 
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(solution("abcabcbb")); // 3
        System.out.println(solution("bbbbbb")); // 1
        System.out.println(solution("pwwkew")); // 3
    }
}
