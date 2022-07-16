package com.string;

import java.util.ArrayList;

/**
 * @author wangwei
 * 2020/9/1 21:08
 *
 * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
 *
 * 你可以认为 s 和 t 中仅包含英文小写字母。字符串 t 可能会很长（长度 ~= 500,000），而 s 是个短字符串（长度 <=100）。
 *
 * 字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。（例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
 *
 * 示例 1:
 * s = "abc", t = "ahbgdc"
 *
 * 返回 true.
 *
 * 示例 2:
 * s = "axc", t = "ahbgdc"
 *
 * 返回 false.
 *
 * 后续挑战 :
 *
 * 如果有大量输入的 S，称作S1, S2, ... , Sk 其中 k >= 10亿，你需要依次检查它们是否为 T 的子序列。在这种情况下，你会怎样改变代码？
 */
public class _392_IsSubsequence {
    /**
     *
     * 双指针，时间复杂度 O(m+n)
     * @param s
     * @param t
     * @return
     */
    public boolean isSubsequence(String s, String t) {
        int m = s.length(), n = t.length();
        int i = 0;
        // 先为s[0]匹配，t从前往后逐个匹配s[0]，在j位置匹配成功后，t继续向后逐个匹配s[1]，...
        for (int j = 0; j < n && i < m; j++) {
            // 匹配到s的当前字符，i指针才后移
            if (s.charAt(i) == t.charAt(j)) i++;
        }
        // 说明s中的字符已全部匹配成功
        return i == m;
    }

    /**
     * 对于上面的解法
     * 如果给你一系列字符串 s1,s2,... 和字符串 t，你需要判定每个串 s 是否是 t 的子序列（可以假定 s 较短，t 很长）。
     * boolean[] isSubsequence(String[] sn, String t);
     * 你也许会问，这不是很简单吗，还是刚才的逻辑，加个 for 循环不就行了？
     * 可以，但是此解法处理每个 s 时间复杂度仍然是 O(N)，总时间复杂度 O(MN)
     * 而如果巧妙运用二分查找，可以将时间复杂度降低，大约是 O(MlogN)。
     * 由于 N 相对 M 大很多，所以后者效率会更高。
     *
     * 如何利用二分查找：
     * 我们知道，当s[i]和t[j]匹配成功时，对于s[i+1]，需要从t[j+1]开始匹配，
     * 所以如果我们先对t进行预处理，得到一个隐射，t中每个字符出现的全部索引，它就是个【有序】序列
     * 那么对于s[i+1]，我们要做的就是，在此字符对应的索引序列中去找到大于j的第一个索引
     * 也就是求【左边界】的二分查找
     */
    public boolean isSubsequence2(String s, String t) {
        int m = s.length(), n = t.length();
        // 预处理，得到字符与多次出现位置的索引的映射
        ArrayList<Integer>[] index = new ArrayList[256];
        for (int j = 0; j < n; j++) {
            // 当前字符
            char c = t.charAt(j);
            if (index[c] == null)
                index[c] = new ArrayList<>();
            // 添加字符和索引的映射关系
            index[c].add(j);
        }
        // 串t的指针
        int j = 0;
        // 借助index去串t中找匹配s[i]的字符位置
        for (int i = 0; i < m; i++) {
            // s[i]
            char c = s.charAt(i);
            // t中没有这个字符c，index[c]未初始化
            if (index[c] == null) return false;
            // 在t中字符c出现的所有位置中找大于j的第一个位置
            int pos = leftBoundBinarySearch(index[c], j);
            // 无法匹配，比如 s=ab,t=bccade,s的a在t[3]匹配，而b只在t[0]出现
            // 在[0]中去找大于3的数字，肯定不存在left=mid+1，最终左边界=数组大小
            if (pos == index[c].size()) return false;
            // 匹配成功，列表中下标pos处是第一个大于j的位置
            j = index[c].get(pos) + 1;
        }
        return true;
    }

    /**
     * 在有序列表中去寻找大于target的第一个数字
     * 如果不存在，也就是列表中所有数字都比target小，最终返回的lo == list.size()
     * @param list
     * @param target
     * @return
     */
    private int leftBoundBinarySearch(ArrayList<Integer> list, int target) {

        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            // 太小，扩大左边界
            if (list.get(mid) < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

}
