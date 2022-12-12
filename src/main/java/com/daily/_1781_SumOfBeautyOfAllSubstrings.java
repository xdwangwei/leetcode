package com.daily;

/**
 * @author wangwei
 * @date 2022/12/12 12:35
 * @description: _1781_SumOfBeautyOfAllSubstrings
 * 1781. 所有子字符串美丽值之和
 * 一个字符串的 美丽值 定义为：出现频率最高字符与出现频率最低字符的出现次数之差。
 *
 * 比方说，"abaacc" 的美丽值为 3 - 1 = 2 。
 * 给你一个字符串 s ，请你返回它所有子字符串的 美丽值 之和。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "aabcb"
 * 输出：5
 * 解释：美丽值不为零的字符串包括 ["aab","aabc","aabcb","abcb","bcb"] ，每一个字符串的美丽值都为 1 。
 * 示例 2：
 *
 * 输入：s = "aabcbaa"
 * 输出：17
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 500
 * s 只包含小写英文字母
 *
 */
public class _1781_SumOfBeautyOfAllSubstrings {


    /**
     * 暴力枚举 + 计数
     * 思路
     *
     * 用下标 i 和 j 表示子字符串的两端。
     * 用双层循环来遍历所有子字符串，第一层循环子字符串的起点 i，第二层循环固定 i，遍历子字符串的重点 j，
     * 遍历时维护数组cnt[]来记录字符频率的哈希表，并计算美丽值。
     *
     * 时间复杂度 O(C * N^2) C 是s字符种类， N 是s长度
     *
     * 本来对于每个 i，j， 需要统计 i 到 j 范围内 每个字符的次数（这又是一个for循环），选出其中的最大最小值，计算美丽值
     * 但实际上，对于固定的i，当cnt记录了[i,j] 范围内频率时，对于 [i, j+1] ，只需要在[i,j] 的基础上更新 cnt[s[j+1]-'a']++即可
     * 因此[i,j]范围重新遍历统计这个过程没有必要
     * 但是对于每一次的[i,j]需要重新遍历cnt来查找最大最小值从而得到当前部分对应子串的美丽值
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/sum-of-beauty-of-all-substrings/solution/suo-you-zi-zi-fu-chuan-mei-li-zhi-zhi-he-rq3x/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int beautySum(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        int ans = 0;
        // 枚举左端点 i
        for (int i = 0; i < n; ++i) {
            // 枚举右端点 j
            // 统计 [i, j] 范围内字符频率
            int[] cnt = new int[26];
            // [i,j] 内字符频率，不需要从新从i统计到j，只需要在[i,j-1]的统计结果cnt基础上，更新当前字符的频率即可
            for (int j = i; j < n; ++j) {
                cnt[chars[j] - 'a']++;
                // 不过对于每个j，需要重新遍历cnt，得到[i,j]内字符频率最大最小值，从而计算美丽值
                int max = 0, min = n;
                for (int x : cnt) {
                    // 只有出现过的字符才有意义
                    if (x > 0) {
                        max = Math.max(max, x);
                        min = Math.min(min, x);
                    }
                }
                // 累加美丽值
                ans += max - min;
            }
        }
        // 返回
        return ans;
    }
}
