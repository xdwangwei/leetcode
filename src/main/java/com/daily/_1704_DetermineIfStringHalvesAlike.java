package com.daily;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/11/11 10:35
 * @description: _1704_DetermineIfStringHalvesAlike
 * Determine if String Halves Are Alike
 */
public class _1704_DetermineIfStringHalvesAlike {


    /**
     * 模拟
     * 统计s每一半中元音字符的数量，判断是否相等即可
     * @param s
     * @return
     */
    public boolean halvesAreAlike(String s) {
        Set<Character> set = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));
        int n = s.length();
        int r = n / 2;
        int cnt = 0;
        // 前半部分
        for (int i = 0; i < r; ++i) {
            if (set.contains(s.charAt(i))) {
                cnt++;
            }
        }
        // 后半部分
        for (int i = r; i < s.length(); ++i) {
            if (set.contains(s.charAt(i))) {
                cnt--;
                if (cnt < 0) {
                    return false;
                }
            }
        }
        // 必须一致
        return cnt == 0;
    }
}
