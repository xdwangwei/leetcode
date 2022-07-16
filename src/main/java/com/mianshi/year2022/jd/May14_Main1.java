package com.mianshi.year2022.jd;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/5/14 19:37
 * @description: Main1
 */
public class May14_Main1 {

    /**
     *  信息在传递之前需要经过一些加密算法才能由信道进行传递。小明设计了一套新的加密算法，该算法如下：
     *
     *  设原字符串为 p，该算法会首先将 p 中的字符随机打乱，比如 abcdef 可以打乱成cdfabe 等。
     *  之后会随机生成两个字符串 s1, s2（可以为空），再将 s1 拼接到 p 的前面， s2 拼接到 p 的后面。
     *  形成的新的字符串 s′ 就是加密后的字符串。
     *
     *  小明设计出来算法之后需要写一个验证程序，即给定 s 和 s′，判断 s′ 是否可以由 s 加密而来。
     *
     *  s1 和 s2 随机产生的，也就是不用考虑
     *
     *  所以这个问题转变为 s‘ 中 是否包含 s的一种字符重排后的排列，假设s长度是n
     *  假设 s’包含，且这部分为 x 到 x + n - 1， 那么 s1 就是 s'[0...x-1]，s2就是 s'[x+n,...]
     *
     *  题目变为 leetcode 第 567 题，
     *  https://leetcode.cn/problems/permutation-in-string/solution/zi-fu-chuan-de-pai-lie-by-leetcode-solut-7k7u/
     *
     *  假设 s 长度是 m， t 长度是 n
     *
     *  使用固定大小m的滑动窗口在t中滑动，统计窗口内每个字符出现次数是否与s中对应，若匹配成功，返回true，否则返回false
     *  可参考官方题解，
     *
     *  简化使用双指针滑动窗口，保持窗口内每个字符与原串对应一致，否则收缩左边界，当窗口能扩张到大小和原串一样时，那说明能匹配成功
     *  具体：
     *
     *  先统计s中每个字符出现次数 for (char c : s) cnt[c]--;
     *  t，滑动窗口 left =0， right = 0.
     *  while (rigth < n) {
     *      // 扩张
     *      char c = t[right]; right++;
     *      cnt[c]++;
     *      // 窗口内字符c的次数多于原串s，此时进行窗口收缩,对于未在原串出现的字符e，也会触发收缩，所以能自动移除去
     *      while (cnt[c] > 0) {
     *          char d = t[left];
     *          cnt[d]--;
     *          left++;
     *      }
     *      // 收缩完成后，窗口内字符c个数与原串一样，
     *      // 当 窗口大小恰好为原串长度时，说明窗口恰好包含了原串每个字符，且次数都一样，此时 返回 true
     *      if (right - left + 1  == m) return true;
     *  }
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        while (n-- > 0) {
            String s = scanner.nextLine();
            String t = scanner.nextLine();
        //     如果 s′ 可以由 s 加密而来，输出 YES；否则输出 NO。
        //     Arrays.sort(s);
            if (s.length() > t.length()) {
                System.out.println("NO");
            } else {
                System.out.println(helper(s, t) ? "YES":"NO");
            }
        }
    }


    /**
     * 拉跨写法，从t中且分出所有长度和s一致的子串，二者如果只是字符重新排列，那么排序后一定一致
     * @param s
     * @param t
     * @return
     */
    private static boolean helper(String s, String t) {
        int m = s.length(), n = t.length();
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        String target = new String(chars);
        for (int i = 0; i < n - m; ++i) {
            char[] temp = t.substring(i, i + m).toCharArray();
            Arrays.sort(temp);
            if (target.equals(new String(temp))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 双指针滑动窗口写法
     *  简化使用双指针滑动窗口，保持窗口内每个字符与原串对应一致，否则收缩左边界，当窗口能扩张到大小和原串一样时，那说明能匹配成功
     *  具体：
     *
     *  先统计s中每个字符出现次数 for (char c : s) cnt[c]--;
     *  t，滑动窗口 left =0， right = 0.
     *  while (rigth < n) {
     *      // 扩张
     *      char c = t[right]; right++;
     *      cnt[c]++;
     *      // 窗口内字符c的次数多于原串s，此时进行窗口收缩,对于未在原串出现的字符e，也会触发收缩，所以能自动移除去
     *      while (cnt[c] > 0) {
     *          char d = t[left];
     *          cnt[d]--;
     *          left++;
     *      }
     *      // 收缩完成后，窗口内字符c个数与原串一样，
     *      // 当 窗口大小恰好为原串长度时，说明窗口恰好包含了原串每个字符，且次数都一样，此时 返回 true
     *      if (right - left + 1  == m) return true;
     *  }
     */
    private static boolean contain(String s, String t) {
        int m = s.length(), n = t.length();
        if (m > n) {
            return false;
        }
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']--;
        }
        int left = 0, right = 0;
        while (right < n) {
            char c = t.charAt(right++);
            cnt[c - 'a']++;
            while (cnt[c - 'a'] > 0) {
                char d = t.charAt(left);
                cnt[d - 'a']--;
                left++;
            }
            if (right - left + 1 == m) {
                return true;
            }
        }
        return false;
    }
}
