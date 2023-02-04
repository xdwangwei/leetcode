package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/2/4 21:35
 * @description: _1798_MaximumNumberOfConsecutiveValuesYouCanMake
 *
 * 1798. 你能构造出连续值的最大数目
 * 给你一个长度为 n 的整数数组 coins ，它代表你拥有的 n 个硬币。第 i 个硬币的值为 coins[i] 。如果你从这些硬币中选出一部分硬币，它们的和为 x ，那么称，你可以 构造 出 x 。
 *
 * 请返回从 0 开始（包括 0 ），你最多能 构造 出多少个连续整数。
 *
 * 你可能有多个相同值的硬币。
 *
 *
 *
 * 示例 1：
 *
 * 输入：coins = [1,3]
 * 输出：2
 * 解释：你可以得到以下这些值：
 * - 0：什么都不取 []
 * - 1：取 [1]
 * 从 0 开始，你可以构造出 2 个连续整数。
 * 示例 2：
 *
 * 输入：coins = [1,1,1,4]
 * 输出：8
 * 解释：你可以得到以下这些值：
 * - 0：什么都不取 []
 * - 1：取 [1]
 * - 2：取 [1,1]
 * - 3：取 [1,1,1]
 * - 4：取 [4]
 * - 5：取 [4,1]
 * - 6：取 [4,1,1]
 * - 7：取 [4,1,1,1]
 * 从 0 开始，你可以构造出 8 个连续整数。
 * 示例 3：
 *
 * 输入：nums = [1,4,10,3,1]
 * 输出：20
 *
 *
 * 提示：
 *
 * coins.length == n
 * 1 <= n <= 4 * 104
 * 1 <= coins[i] <= 4 * 104
 */
public class _1798_MaximumNumberOfConsecutiveValuesYouCanMake {

    /**
     * 方法：贪心
     *
     * 需要返回我们能构造出从 0 开始（包括 0）的多少个连续整数。
     *
     * 首先我们用 [l,r]，0≤l,r 表示一段连续的从 l 到 r 的连续整数区间，
     * 不妨设我们现在能构造出 [0,x] 区间的整数，现在我们新增一个整数 y，那么我们可以构造出的区间为 [0,x] 和 [y,x+y]，
     * 由于我们需要从 0 开始构造出尽可能多的连续整数，而不在区间 [0,x] 中的最小整数是 x+1，
     * 因此如果 y <= x+1，那么元素 y 就会使得构造出的连续整数的范围从 [0,x] 增加到 [0,y+x]；
     * 否则，元素 y 不会对答案产生任何影响。
     *
     * 为了让最终区间尽可能大，我们希望每次以最小的代价 y 实现区间的扩大，
     * 即，每次选择数组中还未被选的元素中最小的那个作为 y 即可。
     * 如果 y≤x+1，那么就可以更新答案区间，否则剩下更大的元素也不会对答案产生任何影响，直接结束后序过程。
     *
     * 初始时我们没有选择任何元素，对应的区间为 [0,0]。
     * 随后我们将数组中的元素升序排序，
     * 然后依次选取数组元素作为y判断是否能更新答案区间即可；若能则继续，否则结束
     *
     * 作者：zerotrac2
     * 链接：https://leetcode.cn/problems/maximum-number-of-consecutive-values-you-can-make/solution/ni-neng-gou-zao-chu-lian-xu-zhi-de-zui-d-hlxf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/maximum-number-of-consecutive-values-you-can-make/solution/ni-neng-gou-zao-chu-lian-xu-zhi-de-zui-d-wci4/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param coins
     * @return
     */
    public int getMaximumConsecutive(int[] coins) {
        // 初始时我们没有选择任何元素，对应的区间为 [0,0]。
        int max = 0;
        // 对数组元素进行升序排序
        Arrays.sort(coins);
        // 选择元素
        for (int y : coins) {
            // 加入 y后，得到区间 [0, max], [y, max + y]
            // 若 y <= max + 1，则能合并区间得到 [0, max + y]
            // 否则，无法合并，并且选择后序元素更加不可能成功，直接结束
            if (y > max + 1) {
                break;
            }
            max += y;
        }
        // 返回：区间 [0, max]，元素个数为 max + 1
        return max + 1;
    }
}
