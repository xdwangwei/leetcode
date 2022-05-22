package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/5/22 20:26
 * @description: _464_CanIWin
 *
 * 464. 我能赢吗
 * 在 "100 game" 这个游戏中，两名玩家轮流选择从 1 到 10 的任意整数，累计整数和，先使得累计整数和 达到或超过  100 的玩家，即为胜者。
 *
 * 如果我们将游戏规则改为 “玩家 不能 重复使用整数” 呢？
 *
 * 例如，两个玩家可以轮流从公共整数池中抽取从 1 到 15 的整数（不放回），直到累计整数和 >= 100。
 *
 * 给定两个整数 maxChoosableInteger （整数池中可选择的最大数）和 desiredTotal（累计和），若先出手的玩家是否能稳赢则返回 true ，否则返回 false 。假设两位玩家游戏时都表现 最佳 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：maxChoosableInteger = 10, desiredTotal = 11
 * 输出：false
 * 解释：
 * 无论第一个玩家选择哪个整数，他都会失败。
 * 第一个玩家可以选择从 1 到 10 的整数。
 * 如果第一个玩家选择 1，那么第二个玩家只能选择从 2 到 10 的整数。
 * 第二个玩家可以通过选择整数 10（那么累积和为 11 >= desiredTotal），从而取得胜利.
 * 同样地，第一个玩家选择任意其他整数，第二个玩家都会赢。
 * 示例 2:
 *
 * 输入：maxChoosableInteger = 10, desiredTotal = 0
 * 输出：true
 * 示例 3:
 *
 * 输入：maxChoosableInteger = 10, desiredTotal = 1
 * 输出：true
 *
 *
 * 提示:
 *
 * 1 <= maxChoosableInteger <= 20
 * 0 <= desiredTotal <= 300
 */
public class _464_CanIWin {

    /**
     * 方法一：记忆化搜索 + 状态压缩
     * 思路
     *
     * 考虑边界情况，
     *      当可选数字最大值 > desiredTotal，先手可以直接取胜
     *      当所有数字选完仍无法到达 desiredTotal 时，两人都无法获胜，返回 false。
     *
     * 当所有数字的和大于等于 desiredTotal 时，其中一方能获得胜利，需要通过搜索来判断获胜方。
     *
     * 注意双方共同维护一个累加和变量，先使得累计整数和 达到或超过 desiredTotal 的玩家，获胜
     *
     * 在游戏中途，假设已经被使用的数字的集合为 usedNumbers，这些数字的和为 currentTotal。
     * 当某方行动时，如果他能在未选择的数字中选出一个 i，使得 i + currentTotal ≥ desiredTotal，则他能获胜。
     * 否则，需要继续通过搜索来判断获胜方。
     * 在剩下的数字中，如果他能选择一个 i，使得对方在接下来的局面中无法获胜，则他会获胜。否则，他会失败。
     *
     * 我们不必区分当前是第几轮，我们考虑的是每个局面下的先手是否能赢
     * 那么返回值就是 初始局面下的先手是否能赢
     * 局面 起始就是 可选数字的选择情况，也就是 usedNumbers
     *
     * 根据这个思想设计搜索函数 dfs，
     * 因为 1 <= maxChoosableInteger <= 20 ，所以 usedNumbers 可以用一个整数来表示，
     * 从低位到高位，第 i 位为 1 则表示数字 i 已经被使用，为 0 则表示数字 i 未被使用。
     * 如果当前玩家获胜，则返回 true，否则返回 false。
     * 为了避免重复计算，需要使用记忆化的操作来降低时间复杂度。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/can-i-win/solution/wo-neng-ying-ma-by-leetcode-solution-ef5v/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */


    // 备忘录，当前局面下，先手是否能赢
    Map<Integer, Boolean> memo = new HashMap<>();

    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        // 当可选数字最大值 > desiredTotal，先手可以直接取胜
        if (desiredTotal <= maxChoosableInteger) {
            return true;
        }
        // 当所有数字选完仍无法到达 desiredTotal 时，两人都无法获胜，返回 false。
        if (maxChoosableInteger * (maxChoosableInteger + 1) / 2 < desiredTotal) {
            return false;
        }
        // 其他情况，通过dfs解决
        // 返回 初始局面下，先手是否能赢，初始局面所有数字还未被使用，所以 usedNumbers = 0，累加和是0
        return dfs(maxChoosableInteger, desiredTotal, 0, 0);
    }


    /**
     * dfs计算当前局面下，先手是否能赢，两种取胜方式：
     *     找到一个可选数字，和当前累加值的和达到目标值
     *     找到一个可选数字，形成下一局面。下一局面下先手(对手)无法取胜，==自己取胜
     * @param maxChoosableInteger
     * @param desiredTotal
     * @param used
     * @param curSum
     * @return
     */
    private boolean dfs(int maxChoosableInteger, int desiredTotal, int used, int curSum) {
        // 局面重复
        if (memo.containsKey(used)) {
            return memo.get(used);
        }
        boolean res = false;
        // 可选数字
        for (int i = 1; i <= maxChoosableInteger; ++i) {
            // 当前数字已选取过
            if (((used >> i) & 1) == 1) {
                continue;
            }
            // 取胜方式一：找到一个可选数字，和当前累加值的和达到目标值
            if (curSum + i >= desiredTotal) {
                res = true;
                break;
            }
            // 取胜方式二：找到一个可选数字，形成下一局面。下一局面下先手(对手)无法取胜，==自己取胜
            if (!dfs(maxChoosableInteger, desiredTotal, used | (1 << i), curSum + i)) {
                res = true;
                break;
            }
        }
        // 更新备忘录
        memo.put(used, res);
        // 返回结果
        return res;
    }
}
