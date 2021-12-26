package com.order;

/**
 * @Author: wangwei
 * @Description: 实现 strStr() 函数。
 * <p>
 * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
 * <p>
 * 示例 1:
 * <p>
 * 输入: haystack = "hello", needle = "ll"
 * 输出: 2
 * 示例 2:
 * <p>
 * 输入: haystack = "aaaaa", needle = "bba"
 * 输出: -1
 * 说明:
 * <p>
 * 当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。
 * <p>
 * 对于本题而言，当 needle 是空字符串时我们应当返回 0 。这与C语言的 strstr() 以及 Java的 indexOf() 定义相符。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/implement-strstr
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @Time: 2019/12/12 周四 16:27
 **/
public class _28_SubStrIndex {

    /**
     * 直接使用库函数
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int solution1(String haystack, String needle) {
        return haystack.indexOf(needle);
    }

    /**
     * 借助 String.substring，一次遍历
     */
    public int solution2(String haystack, String needle) {
        int s = haystack.length(), t = needle.length();
        if (s < t) return -1;
        if (t == 0) return 0; // needle = ""
        for (int i = 0; i < s - t + 1; ++i)
            // 从当前位置开始长度为t的子串正好和目标串相同
            if (haystack.substring(i, i + t).equals(needle)) return i;
        return -1;
    }

    /**
     * 暴力搜索
     * @param haystack
     * @param needle
     * @return
     */
    public int strStr(String haystack, String needle) {
        int m = haystack.length(), n = needle.length();
        if (n == 0) {
            return 0;
        }
        if (m < n) {
            return -1;
        }
        // 对于 hatstack每一个位置
        for (int i = 0; i < m - n + 1; ++i) {
            int j = 0;
            // 从它开始，逐个去匹配needle字符
            for (j = 0; j < n; ++j) {
                if (haystack.charAt(i + j) != needle.charAt(j)) {
                    break;
                }
            }
            // needle完全匹配上
            if (j == n) {
                return i;
            }
        }
        return -1;
    }

    /**
     * KMP算法
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int solution3(String haystack, String needle) {
        int s = haystack.length();
        int t = needle.length();
        if (t == 0) return 0;
        if (s < t) return -1;
        // KMP
        int[] next = getNext(needle);
        int i = 0, j = 0;
        while (i < s && j < t) {
            // 模式串挪到了空位置或当前位置匹配，两指针都后移
            if (-1 == j || haystack.charAt(i) == needle.charAt(j)) {
                ++i;
                ++j;
            } else {
                // 主串指针不动，模式串后移到合适位置
                j = next[j];
            }
        }
        // j 挪到最后一个位置，表示模式串匹配成功，i 表示主串挪动的距离，减去模式串的长度就是模式串在主串第一次出现的位置
        if (j == needle.length())
            return i - t;
        return -1;
    }

    /**
     * 根据模式串求next数组，求next数组与主串无关
     * next数组记录的是当模式串与主串匹配到p[j]时发生不匹配，此时模式串应回退到那个位置，
     * 也就是从j之前哪个位置字符开始跟主串的当前位置继续开始匹配过程
     * 至于为什么求最大公共前后缀 参考这篇文章
     * https://www.cnblogs.com/zhangtianq/p/5839909.html
     *
     * @param pattern
     * @return
     */
    private int[] getNext(String pattern) {
        int[] next = new int[pattern.length()];
        next[0] = -1;
        // next[0] = -1,表示p[0]就不匹配时，模式串移到-1的位置，实际含义是，p[0]去和主串的下一个位置匹配，
        // next[i] = 0，表示p[i]不匹配时，模式串移到0的位置
        int j = 0, k = -1;
        while (j < pattern.length() - 1) {
            // p[0]...p[k-1]p[k]...p[j-k-1]...p[j]
            // next[j]代表的是字符j之前子串的最大公共前后缀，也就是 p[0]...p[k-1] = p[j-k-1]...p[j-1]
            // 当p[k] = p[j] 时，next[j+1]也就是字符j+1之前的子串的最大公共前后缀，比next[j]多了1，
            // 因为总是next[j] = k, 所以 k++ 即可
            if (-1 == k || pattern.charAt(j) == pattern.charAt(k)) {
                ++j; // 得到next[j+1] = next[j] + 1
                ++k;
                // 当前位置不匹配了，模式串要移动到next[j]，如果下一个位置的字符和当前位置一样，肯定不成功，直接跳到下下个位置
                if (pattern.charAt(j) != pattern.charAt(k))
                    next[j] = k; // k 就是最大公共前后缀的长度，next[k]是p[0]...p[k-1]最大公共前后缀
                else
                    next[j] = next[k];
            // p[j] != p[k],需要在前k个字符中去找与p[j]相等的p[k]
            } else {
                k = next[k];
            }
        }
        return next;
    }

}
