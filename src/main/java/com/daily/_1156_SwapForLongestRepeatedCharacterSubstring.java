package com.daily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangwei
 * @date 2023/6/3 19:57
 * @description: _1156_SwapForLongestRepeatedCharacterSubstring
 *
 * 1156. 单字符重复子串的最大长度
 * 如果字符串中的所有字符都相同，那么这个字符串是单字符重复的字符串。
 *
 * 给你一个字符串 text，你只能交换其中两个字符一次或者什么都不做，然后得到一些单字符重复的子串。返回其中最长的子串的长度。
 *
 *
 *
 * 示例 1：
 *
 * 输入：text = "ababa"
 * 输出：3
 * 示例 2：
 *
 * 输入：text = "aaabaaa"
 * 输出：6
 * 示例 3：
 *
 * 输入：text = "aaabbaaa"
 * 输出：4
 * 示例 4：
 *
 * 输入：text = "aaaaa"
 * 输出：5
 * 示例 5：
 *
 * 输入：text = "abcdef"
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= text.length <= 20000
 * text 仅由小写英文字母组成。
 * 通过次数16,401提交次数33,070
 */
public class _1156_SwapForLongestRepeatedCharacterSubstring {


    /**
     * 方法：滑动窗口（双指针）
     *
     * 思路与算法
     *
     * 给定一个字符串，你需要选择两个字符进行交换，这个操作最多进行一次，要求使得仅包含相同字符的子串尽可能的长。
     * 例如 "bbababaaaa"，可以交换第 2（下标从 0 开始） 个字符 a 与第 5 个字符 b，使得包含相同字符的子串最长为 6，即 "aaaaaa"。
     *
     * 我们设 n 为字符串 text 的长度，下标从 0 开始，
     *
     * 现在有一段区间  [i,j) （不包括 j ）由相同字符 a 构成，并且该区间两边字符都不是a，（即一段重复字符a组成的子串）
     *
     * 而整个 text 中 a 的出现次数为  count[a]，那么当
     *
     * 即，此时 [i,j - 1] 是一段重复的a， text[j] != text[-1]
     * 那么 如果 text[j+1] = a 时，且后面的一段 [j+1,k) 又是一段重复的 a，
     * 那么我们就可以将其他部分（更后面位置）的 a 与 j 位置的字符交换，得到 [i...k-1] 这部分构成更长的子串（长度为 k - i）。
     *
     * 注意，这里的前提是存在多余的 a 交换到中间来，这种情况下长度是 k-i
     * 那么如果不存在多余的a呢，此时可以将 [j+1,k-1] 这部分的最后一个字符a交换到 j 位置去，此时的长度是 k-i-1
     * 如何知道是否存在多余的a呢，只要 k-i 不超过 count[a] 那么就说明还有其他a
     * 因此，这两种情况可以统一表示为 min(k-i, count[a])
     *
     * 接着，让 i 跳到 j 位置，继续上述过程
     *
     * 在实现过程中，我们可以使用滑动窗口算法来求解由相同字符构成的最长区间。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/swap-for-longest-repeated-character-substring/solution/dan-zi-fu-zhong-fu-zi-chuan-de-zui-da-ch-9ywr/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param text
     * @return
     */
    public int maxRepOpt1(String text) {
        int[] cnt = new int[26];
        for (char c : text.toCharArray()) {
            cnt[c - 'a']++;
        }
        int i = 0, n = text.length();
        int ans = 0;
        while (i < n) {
            // 找到一段重复字符c [i...j)
            int j = i;
            while (j < n && text.charAt(j) == text.charAt(i)) {
                j++;
            }
            // 间隔字符 text[j]，再找下一段重复字符 c [j+1...k)
            int k = j + 1;
            while (k < n && text.charAt(k) == text.charAt(i)) {
                k++;
            }
            // 如果存在多余的a，那么我们就可以将其他部分的 a 与 j 位置的字符交换，得到 [i...k-1] 这部分构成更长的子串（长度为 k - i）。
            // 否则可以将 [j+1,k-1] 这部分的最后一个字符a交换到 j 位置去，此时的长度是 k-i-1
            // 如何知道是否存在多余的a呢，只要 k-i 不超过 count[a] 那么就说明还有其他a
            // 因此，这两种情况可以统一表示为 min(k-i, count[a])
            ans = Math.max(ans, Math.min(k - i, cnt[text.charAt(i) - 'a']));
            // 接着，让 i 跳到 j 位置，再找一段重复字符d，并继续上述过程
            i = j;
        }
        // 返回
        return ans;
    }

    public static void main(String[] args) {
        _1156_SwapForLongestRepeatedCharacterSubstring obj = new _1156_SwapForLongestRepeatedCharacterSubstring();
        System.out.println(obj.maxRepOpt1("ababa"));
    }
}
