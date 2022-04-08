package com.daily;

/**
 * @author wangwei
 * @date 2022/4/3 13:36
 *
 * 744. 寻找比目标字母大的最小字母
 * 给你一个排序后的字符列表 letters ，列表中只包含小写英文字母。另给出一个目标字母 target，请你寻找在这一有序列表里比目标字母大的最小字母。
 *
 * 在比较时，字母是依序循环出现的。举个例子：
 *
 * 如果目标字母 target = 'z' 并且字符列表为 letters = ['a', 'b']，则答案返回 'a'
 *
 *
 * 示例 1：
 *
 * 输入: letters = ["c", "f", "j"]，target = "a"
 * 输出: "c"
 * 示例 2:
 *
 * 输入: letters = ["c","f","j"], target = "c"
 * 输出: "f"
 * 示例 3:
 *
 * 输入: letters = ["c","f","j"], target = "d"
 * 输出: "f"
 */
public class _744_FindSmallestLetterGreaterThanTarget {

    /**
     * letters已经从小到大排好序了，因此直接遍历letters，找到第一个大于target的字符返回即可。
     * 另外，若不存在，按照提议返回 letters[0]
     * @param letters
     * @param target
     * @return
     */
    public char nextGreatestLetter(char[] letters, char target) {
        int len = letters.length;
        // 给个默认值，不存在的情况下返回letters[0]
        char res = letters[0];
        for (int i = 0; i < len; ++i) {
            // 找到第一个大于target的字符即可返回
            if (letters[i] > target) {
                res = letters[i];
                break;
            }
        }
        return res;
    }

    /**
     * 由于已排好序，因此可以使用寻找左边界的二分查找
     * @param letters
     * @param target
     * @return
     */
    public char nextGreatestLetter2(char[] letters, char target) {
        int lo = 0, hi = letters.length;
        // 所有字符都小于等于target，直接返回第一个
        if (letters[hi - 1] <= target) {
            return letters[0];
        }
        // 左边界二分搜索
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            // 注意这里的等号，因为要找的是比target大的第一个字符，不是target
            if (letters[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return letters[lo];
    }
}
