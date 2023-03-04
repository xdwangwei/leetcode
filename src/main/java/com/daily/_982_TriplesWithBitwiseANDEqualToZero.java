package com.daily;

/**
 * @author wangwei
 * @date 2023/3/4 13:52
 * @description: _982_TriplesWithBitwiseANDEqualToZero
 *
 * 982. 按位与为零的三元组
 * 给你一个整数数组 nums ，返回其中 按位与三元组 的数目。
 *
 * 按位与三元组 是由下标 (i, j, k) 组成的三元组，并满足下述全部条件：
 *
 * 0 <= i < nums.length
 * 0 <= j < nums.length
 * 0 <= k < nums.length
 * nums[i] & nums[j] & nums[k] == 0 ，其中 & 表示按位与运算符。
 *
 * 示例 1：
 *
 * 输入：nums = [2,1,3]
 * 输出：12
 * 解释：可以选出如下 i, j, k 三元组：
 * (i=0, j=0, k=1) : 2 & 2 & 1
 * (i=0, j=1, k=0) : 2 & 1 & 2
 * (i=0, j=1, k=1) : 2 & 1 & 1
 * (i=0, j=1, k=2) : 2 & 1 & 3
 * (i=0, j=2, k=1) : 2 & 3 & 1
 * (i=1, j=0, k=0) : 1 & 2 & 2
 * (i=1, j=0, k=1) : 1 & 2 & 1
 * (i=1, j=0, k=2) : 1 & 2 & 3
 * (i=1, j=1, k=0) : 1 & 1 & 2
 * (i=1, j=2, k=0) : 1 & 3 & 2
 * (i=2, j=0, k=1) : 3 & 2 & 1
 * (i=2, j=1, k=0) : 3 & 1 & 2
 * 示例 2：
 *
 * 输入：nums = [0,0,0]
 * 输出：27
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] < 216
 * 通过次数11,269提交次数17,367
 */
public class _982_TriplesWithBitwiseANDEqualToZero {

    /**
     * 方法一：枚举
     *
     * 最容易想到的做法是使用三重循环枚举三元组 (i,j,k)，再判断 nums[i]&nums[j]&nums[k] 的值是否为 0。
     * 但这样做的时间复杂度是 O(n^3)，其中 n 是数组 nums 的长度，会超出时间限制。
     *
     * 对于 nums[k] 来说，其实它只需要知道它 AND 一个数的结果是否等于 0，至于这个数具体是由哪些 nums[i]&nums[j] 得到的，并不重要。
     *
     * 注意到题目中给定了一个限制：数组 nums 的元素不会超过 2^16。这说明，nums[i]&nums[j] 的值也不会超过 2^16。
     * 因此，我们可以首先使用二重循环枚举 i 和 j，并使用一个长度为 的数组（或哈希表）存储每一种 nums[i]&nums[j] 以及它出现的次数。
     * 随后，我们再使用二重循环，然后枚举 nums[k] 和 x，如果 nums[k]&x 等于 0，那就把 cnt[x] 加到答案中。
     *
     * 这样就可以将时间复杂度从O(n^3) 降到 O(n^2) + O(n*2^16)。
     *
     * 用哈希表实现的 cnt 要枚举所有 key；用数组实现的 cnt 要枚举区间 [0,2^16) 内的所有数（根据题目的数据范围得出）。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/triples-with-bitwise-and-equal-to-zero/solution/an-wei-yu-wei-ling-de-san-yuan-zu-by-lee-gjud/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int countTriplets(int[] nums) {
        int ans = 0;
        // 所有数字大小不超过 2^16，那么二者之间进行与运算的结果也不会超过 2^16
        int[] cnt = new int[1 << 16];
        // 统计 所有 nums[i]&nums[j] 的值 及其出现次数
        for (int x : nums) {
            for (int y : nums) {
                ++cnt[x & y];
            }
        }
        // 对于 每个 z=nums[k]，需要找到 xy=nums[i]&nums[j] 满足 z & xy=0
        for (int z : nums) {
            // 遍历cnt数组下标xy
            for (int xy = 0; xy < 1 << 16; ++xy) {
                // 满足，
                if ((z & xy) == 0) {
                    // 累加
                    ans += cnt[xy];
                }
            }
        }
        // 返回
        return ans;
    }

    /**
     * 方法二：枚举 + 子集优化
     * 思路与算法
     *
     * 在方法一的第二个二重循环中，我们需要枚举 [0,2^16) 中的所有整数。即使我们使用哈希表代替数组，在数据随机的情况下，
     *
     * 这里我们介绍另一个常数级别的优化。当我们在第二个二重循环中枚举v k 时，我们希望统计出所有与 nums[k] 按位与为 0 的二元组数量。
     * 也就是说：nums[k] 的第 t 个二进制位是 0，那么二元组的第 t 个二进制位才可以是 1，否则一定不能是 1。
     *
     * 如果把二进制数看成集合的话，二进制从低到高第 i 位为 1 表示 i 在集合中，为 0 表示 i 不在集合中，
     * 例如 a=1101(2) 表示集合 A={0,2,3}。
     *
     * 那么 a&b=0 相当于集合 A 和集合 B 没有交集，也可以理解成 CuA 的子集，这里 U={0,1,2,⋯,15}，对应的数字就是 0xffff。
     * 一个数异或 0xffff 就可以得到这个数的补集了。
     *
     * 因此方法一中第二个for循环的内循环中，就不必枚举 [0,2^16) 中的所有整数，而是 枚举 m = nums[k] ^ 0xffff 的所有子集
     *
     * 【二进制从大到小枚举子集】
     * 怎么从大到小枚举 m 的子集 s 呢？可以从 m 不断减一，直到 0，如果 s&m = s 就表示 s 是 m 的子集。
     *
     * 更高效的做法是直接「跳到」下一个子集，即：
     *
     *      初始时 s=m，因为 m 也是本身的子集；
     *      我们不断地令 s=(s−1)&m，其中 & 表示按位与运算。当 s 再次 等于 m 时枚举结束。
     *      这样我们就可以从大到小枚举x 的所有子集。
     *
     * 这样做的正确性在于，假设当前枚举到的子集为 s，s−1 仅仅把 s 最低位的 1 改成了 0，比这个 1 更低的 0 全部改成了 1，
     * 那么比s小的下一个子集也一定是 s−1 的子集，直接 (s-1)&m，就能得到下一个子集了。
     * 最后，当 s=0 时，由于 −1 的二进制全为 1，所以 (s−1)&m=m，因此我们可以通过判断下一个子集是否又回到 =m，来判断是否要退出循环。
     *
     * 注：这一技巧经常用于子集状压 DP 中。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/triples-with-bitwise-and-equal-to-zero/solution/you-ji-qiao-de-mei-ju-chang-shu-you-hua-daxit/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/triples-with-bitwise-and-equal-to-zero/solution/an-wei-yu-wei-ling-de-san-yuan-zu-by-lee-gjud/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int countTriplets2(int[] nums) {
        int ans = 0;
        // 所有数字大小不超过 2^16，那么二者之间进行与运算的结果也不会超过 2^16
        int[] cnt = new int[1 << 16];
        // 统计 所有 nums[i]&nums[j] 的值 及其出现次数
        for (int x : nums) {
            for (int y : nums) {
                ++cnt[x & y];
            }
        }
        // 对于 每个 z=nums[k]，需要找到 xy=nums[i]&nums[j] 满足 z & xy=0
        for (int z : nums) {
            // z的二进制为1的位置，xy的二进制一定不能为1，因此 xy 只可能为 z^0xffff 的子集
            int m = z ^ 0xffff;
            // 二进制子集从大到小枚举
            int s = m;
            do {
                // 累加答案
                ans += cnt[s];
                // 下一个子集
                s = (s - 1) & m;
                // 当 s=0时，s-1=-1，(s-1) & m = m，循环停止
            } while (s != m);
        }
        // 返回
        return ans;
    }
}
