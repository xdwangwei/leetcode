package com.order;

import javax.swing.tree.ExpandVetoException;
import java.util.Stack;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/24 周日 19:27
 **/
public class _05_LongestPalindrome {
    
    // 回文中心的两侧互为镜像。因此，回文可以从它的中心展开，并且只有 2n−1 个这样的中心。
    // 所含字母数为偶数的回文的中心可以处于两字母之间（如“abba”的中心在两个'b’之间）
    public static String solution1(String s){
        if (s == null || s.length() == 0) return "";
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++){
            int len1 = expandAroundCenter(s, i, i); // 相当于以s[i]为中心扩展
            int len2 = expandAroundCenter(s, i , i + 1); // 相当于以s[i]和s[i+1]中心的空为空心扩展
            int len = Math.max(len1, len2);
            // 这几个取值分析一下长度为1获2的回文串就得到了
            // 上次的串长为 end - start + 1
            // 这里加不加1都行,不加1代表本次取得的串大于等于上次的串都更新结果
            // 加1代表本次取得的串大于上次的串才更新结果
            if (len > end - start + 1){ 
                start = i - (len - 1)/2;
                end = i + len/2;
            }
        }
        return s.substring(start, end + 1);
    }

    private static int expandAroundCenter(String s, int left, int right){
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)){
            L--; // 中心向左扩张
            R++; // 中心向右扩张
        }
        return R - L - 1;
    }
}
