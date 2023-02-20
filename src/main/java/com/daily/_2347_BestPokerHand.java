package com.daily;

/**
 * @author wangwei
 * @date 2023/2/20 15:35
 * @description: _2347_BestPokerHand
 *
 * 2347. 最好的扑克手牌
 * 给你一个整数数组 ranks 和一个字符数组 suit 。你有 5 张扑克牌，第 i 张牌大小为 ranks[i] ，花色为 suits[i] 。
 *
 * 下述是从好到坏你可能持有的 手牌类型 ：
 *
 * "Flush"：同花，五张相同花色的扑克牌。
 * "Three of a Kind"：三条，有 3 张大小相同的扑克牌。
 * "Pair"：对子，两张大小一样的扑克牌。
 * "High Card"：高牌，五张大小互不相同的扑克牌。
 * 请你返回一个字符串，表示给定的 5 张牌中，你能组成的 最好手牌类型 。
 *
 * 注意：返回的字符串 大小写 需与题目描述相同。
 *
 *
 *
 * 示例 1：
 *
 * 输入：ranks = [13,2,3,1,9], suits = ["a","a","a","a","a"]
 * 输出："Flush"
 * 解释：5 张扑克牌的花色相同，所以返回 "Flush" 。
 * 示例 2：
 *
 * 输入：ranks = [4,4,2,4,4], suits = ["d","a","a","b","c"]
 * 输出："Three of a Kind"
 * 解释：第一、二和四张牌组成三张相同大小的扑克牌，所以得到 "Three of a Kind" 。
 * 注意我们也可以得到 "Pair" ，但是 "Three of a Kind" 是更好的手牌类型。
 * 有其他的 3 张牌也可以组成 "Three of a Kind" 手牌类型。
 * 示例 3：
 *
 * 输入：ranks = [10,10,2,12,9], suits = ["a","b","c","a","d"]
 * 输出："Pair"
 * 解释：第一和第二张牌大小相同，所以得到 "Pair" 。
 * 我们无法得到 "Flush" 或者 "Three of a Kind" 。
 *
 *
 * 提示：
 *
 * ranks.length == suits.length == 5
 * 1 <= ranks[i] <= 13
 * 'a' <= suits[i] <= 'd'
 * 任意两张扑克牌不会同时有相同的大小和花色。
 * 通过次数7,339提交次数13,502
 */
public class _2347_BestPokerHand {

    /**
     * 方法：模拟 + 计数
     *
     * 方法一：计数
     *
     * 我们可以先遍历数组 suits，判断相邻两个元素是否均相等，如果是，则返回 "Flush"。
     *
     * 接下来，我们用哈希表或数组 cnt 统计每张牌的数量：
     *
     * 如果有任意一张牌的数量等于 3，直接返回 "Three of a Kind"；
     * 否则，如果有任意一张牌的数量等于 2，返回 "Pair"；
     * 否则，返回 "High Card"。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/best-poker-hand/solution/python3javacgo-yi-ti-yi-jie-ji-shu-by-lc-i8vb/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param ranks
     * @param suits
     * @return
     */
    public String bestHand(int[] ranks, char[] suits) {
        // 判断是否是同一花色
        boolean same = true;
        for (int i = 1; i < suits.length; i++) {
            if (suits[i] != suits[i - 1]) {
                same = false;
                break;
            }
        }
        // 同一花色
        if (same) {
            return "Flush";
        }
        // 统计每个数字的次数
        int[] cnt = new int[15];
        // 是否有某个数字出现2次
        boolean pair = false;
        for (int rank : ranks) {
            // 某个数字出现三次，直接返回
            if (++cnt[rank] == 3) {
                return "Three of a Kind";
            }
            // 记录，有某个数字出现两次
            if (cnt[rank] == 2) {
                pair = true;
            }
        }
        // 有数字出现两次就返回 Pair，否则说明每个数字各不相同，返回 High Card
        return pair ? "Pair" : "High Card";
    }
}
