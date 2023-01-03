package com.daily;

/**
 * @author wangwei
 * @date 2023/1/3 17:47
 * @description: _204_CheckIfNumbersAreAscendingInASentence
 *
 * 2042. 检查句子中的数字是否递增
 * 句子是由若干 token 组成的一个列表，token 间用 单个 空格分隔，句子没有前导或尾随空格。每个 token 要么是一个由数字 0-9 组成的不含前导零的 正整数 ，要么是一个由小写英文字母组成的 单词 。
 *
 * 示例，"a puppy has 2 eyes 4 legs" 是一个由 7 个 token 组成的句子："2" 和 "4" 是数字，其他像 "puppy" 这样的 tokens 属于单词。
 * 给你一个表示句子的字符串 s ，你需要检查 s 中的 全部 数字是否从左到右严格递增（即，除了最后一个数字，s 中的 每个 数字都严格小于它 右侧 的数字）。
 *
 * 如果满足题目要求，返回 true ，否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * example-1
 *
 * 输入：s = "1 box has 3 blue 4 red 6 green and 12 yellow marbles"
 * 输出：true
 * 解释：句子中的数字是：1, 3, 4, 6, 12 。
 * 这些数字是按从左到右严格递增的 1 < 3 < 4 < 6 < 12 。
 * 示例 2：
 *
 * 输入：s = "hello world 5 x 5"
 * 输出：false
 * 解释：句子中的数字是：5, 5 。这些数字不是严格递增的。
 * 示例 3：
 *
 * example-3
 *
 * 输入：s = "sunset is at 7 51 pm overnight lows will be in the low 50 and 60 s"
 * 输出：false
 * 解释：s 中的数字是：7, 51, 50, 60 。这些数字不是严格递增的。
 * 示例 4：
 *
 * 输入：s = "4 5 11 26"
 * 输出：true
 * 解释：s 中的数字是：4, 5, 11, 26 。
 * 这些数字是按从左到右严格递增的：4 < 5 < 11 < 26 。
 *
 *
 * 提示：
 *
 * 3 <= s.length <= 200
 * s 由小写英文字母、空格和数字 0 到 9 组成（包含 0 和 9）
 * s 中数字 token 的数目在 2 和 100 之间（包含 2 和 100）
 * s 中的 token 之间由单个空格分隔
 * s 中至少有 两个 数字
 * s 中的每个数字都是一个 小于 100 的 正 数，且不含前导零
 * s 不含前导或尾随空格
 * 通过次数13,281提交次数19,654
 */
public class _204_CheckIfNumbersAreAscendingInASentence {


    /**
     * 法一：直接遍历
     * 思路与算法
     *
     * 题目要求检查给定的字符串 s 中所有的 数字串token 是否从左到右严格递增，
     * 根据题意可知相邻的 token 之间由空格分割，
     * 我们按照要求依次取出字符串中的每个 token，
     * 如果当前的 token 由数字组成，将该 token 转换为十进制数 cur，
     * 设前一个数字 token 转换后的整数 pre，检验过程如下:
     *
     * 如果 cur 大于 pre，则认为当前的 token 满足递增要求，更新 pre 为 cur，并检测下一个数字 token 是否满足递增；
     * 如果 cur 小于或者等于 pre，则认为不满足递增要求，返回 false；
     * 由于题目中的每个数字 token 转换后的十进制数均为正整数且小于 100，因此我们可以初始化 pre 等于 -1，
     * 我们依次检测每个为数字的 token 是否满足题目要求即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/check-if-numbers-are-ascending-in-a-sentence/solution/jian-cha-ju-zi-zhong-de-shu-zi-shi-fou-d-uhaf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public boolean areNumbersAscending(String s) {
        // 当前遍历到的字符位置
        int pos = 0;
        // 上一个数字串token对应数值
        int prev = -1;
        // 遍历
        while (pos < s.length()) {
            // 跳过非数字字符
            if (!Character.isDigit(s.charAt(pos))) {
                pos++;
                continue;
            }
            // 数字串部分token
            // 遍历并计算对应整数值，遇到空格结束
            int cur = 0;
            while (pos < s.length() && s.charAt(pos) != ' ') {
                cur = cur * 10 + s.charAt(pos) - '0';
                pos++;
            }
            // 不满足严格递增，提前结束并返回
            if (cur >= prev) {
                return false;
            }
            // 更新prev为当前数值，继续遍历
            prev = cur;
        }
        // 顺利遍历结束，返回true
        return true;
    }
}
