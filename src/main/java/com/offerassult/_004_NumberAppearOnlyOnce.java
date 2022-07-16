package com.offerassult;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/9 16:13
 * @description: _004_NumberPresentOnlyOnce
 *
 * 剑指 Offer II 004. 只出现一次的数字
 * 给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [2,2,3,2]
 * 输出：3
 * 示例 2：
 *
 * 输入：nums = [0,1,0,1,0,1,100]
 * 输出：100
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 3 * 104
 * -231 <= nums[i] <= 231 - 1
 * nums 中，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次
 *
 *
 * 进阶：你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 *
 *
 *
 * 注意：本题与主站 137 题相同：https://leetcode-cn.com/problems/single-number-ii/
 */
public class _004_NumberAppearOnlyOnce {


    /**
     * 方法一：排序
     * 排序后三个一组，三个一组，单独出来的那个就是只出现一次的数字
     *
     * 时间复杂度O(NlogN)
     * @param nums
     * @return
     */
    public int singleNumber(int[] nums) {
        // 排序
        Arrays.sort(nums);
        // 三个一组判断
        for (int i = 0; i < nums.length; i += 3) {
            // 如果只剩下最后一个，或者 它和后面两个数字不相等
            if (i == nums.length - 1 || nums[i + 1] != nums[i] || nums[i + 2] != nums[i]) {
                return nums[i];
            }
        }
        return -1;
    }


    /**
     * 使用HashMap统计每个数字的出现结果，再返回出现次数是1的那个数字
     * 空间复杂度O(N)
     * @param nums
     * @return
     */
    public int singleNumber2(int[] nums) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        return -1;
    }


    /**
     * 思路与算法
     *
     * 为了方便叙述，我们称「只出现了一次的元素」为「答案」。
     *
     * 由于数组中的元素都在 int（即 32 位整数）范围内，因此我们可以依次计算答案的每一个二进制位是 0 还是 1。
     *
     * 具体地，考虑答案的第 i 个二进制位（i 从 0 开始编号），它可能为 0 或 1。
     * 对于数组中非答案的元素，每一个元素都出现了 3 次，对应着第 i 个二进制位的 3 个 0 或 3 个 1，
     * 无论是哪一种情况，它们的和都是 3 的倍数（即和为 0 或 3）。因此：
     *
     *      答案的第 i 个二进制位就是数组中所有元素的第 i 个二进制位之和除以 3 的余数。
     *
     * 这样一来，对于数组中的每一个元素 x，我们使用位运算 (x >> i) & 1 得到 x 的第 i 个二进制位，
     * 并将它们相加再对 3 取余，得到的结果一定为 0 或 1，即为答案的第 i 个二进制位。
     *
     *
     * 复杂度分析
     *
     * 时间复杂度：O(nlogC)，其中 n 是数组的长度，C 是元素的数据范围，在本题中 logC=log2^32=32，
     * 也就是我们需要遍历第 0∼31 个二进制位。
     *
     * 空间复杂度：O(1)。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/WGki4K/solution/zhi-chu-xian-yi-ci-de-shu-zi-by-leetcode-0vrt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int singleNumber3(int[] nums) {
        int ans = 0;
        // 依次得到答案的每一个二进位的值是0还是1
        for (int i = 0; i < 32; ++i) {
            // 统计所有数字，当前二进制位 的 和，其实就是1的个数
            int total = 0;
            for (int num : nums) {
                total += (1 & (num >> i));
            }
            // 说明 答案 这个二进制位也是1，否则 就是0
            // 本来%3可能是012，但是最终结果，也就是当前二进制位只可能是 0 或 1，
            if (total % 3 != 0) {
                // 把这一部分补上
                ans |= (1 << i);
            }
        }
        return ans;
    }

    /**
     * 在方法三中，我们是依次处理每一个二进制位的，那么时间复杂度中就引入了 O(logC) 这一项。
     * 我们在对两个整数进行普通的二元运算时，都是将它们看成整体进行处理的，那么是否能看成二进制表示，同时处理所有的二进制位？
     *
     * 答案是可以的。
     *
     * 【以某一个二进制位为例】
     *
     * 对于某一个二进制位，数组中 所有 元素这个二进制位加起来的结果 % 3 的结果可能 是 0 1 2，也就是有三种状态
     * 此时，如果再来一个数字，它的这个二进制位是1，那么 就会有这样的状态变化：0 -> 1, 1 -> 2, 2 -> 0
     * (比如 0 -> 1, 就是 前面数字这个二进制位的和是3的整数倍，当前数字这个二进制是1，加上去的和，对3取余，自然结果变为1)
     *
     * 所以这相当于，在统计所有元素这个二进制位值和的过程中，当前位置的最终结果 是 在 0 - 1 - 2 这三种结果中变化的，就是一个状态转移
     * 借助有限状态自助机原理：
     *
     * 因为二进制只有0和1，但我们有三种状态，所以需要两个二进制位合起来表示这三种状态
     *
     * 我们可以使用一个「黑盒」存储当前遍历过的所有整数。
     * 「黑盒」的第 i 位为 {0,1,2} 三者之一，表示当前遍历过的所有整数的第 i 位之和除以 3 的余数。
     * 考虑在「黑盒」中使用两个比特位来进行存储，即：
     *
     * 黑盒中存储了两个比特位 a 和 b，且会有三种情况：
     *
     *      a 的第 i 位为 0 且 b 的第 i 位为 0，表示 结果 0；(相当于a是高位，b是低位，不过不重要)
     *      a 的第 i 位为 0 且 b 的第 i 位为 1，表示 结果 1；
     *      a 的第 i 位为 1 且 b 的第 i 位为 0，表示 结果 2。
     * 为了方便叙述，我们用 (00) 表示 a 的第 i 位为 0 且 b 的第 i 位为 0，其余的情况类似。
     *
     * 当我们遍历到一个新的整数 x 时，对于 x 的第 i 位  xi，我们可以得出下面的真值表：
     *                (a, b)   x(当前数字这个bit位的值)   (新的a，新的b)
     *     (mod3=0)     00	        0	                    00          (mod3=0)
     *     (mod3=0)     00	        1	                    01          (mod3=1)
     *     (mod3=1)     01	        0	                    01          (mod3=1)
     *     (mod3=1)     01	        1	                    10          (mod3=2)
     *     (mod3=2)     10	        0	                    10          (mod3=2)
     *     (mod3=2)     10	        1	                    00          (mod3=0)
     *
     * 如果单独考虑新的a
     *                (a, b)   x(当前数字这个bit位的值)   新的a
     *                  00	        0	                 0
     *                  00	        1	                 0
     *                  01	        0	                 0
     *                  01	        1	                 1
     *                  10	        0	                 1
     *                  10	        1	                 0
     *
     * 通过真值表写逻辑表达式    新a = !a & b & x | a & !b & !x ，也就是真值表取值为1的那两行 或 起来
     *
     * 如果单独考虑新的b
     *                (a, b)   x(当前数字这个bit位的值)   新的b
     *                  00	        0	                 0
     *                  00	        1	                 1
     *                  01	        0	                 1
     *                  01	        1	                 0
     *                  10	        0	                 0
     *                  10	        1	                 0
     *
     * 通过真值表写逻辑表达式    新b = !a & !b & x | !a & b & !x  = !a & (!b & x | b & !x) = !a & (b ^ x)
     *
     * 所以：当前二进制位的结果 (a,b), a ，b，x都是一个二进制位
     *      新a = !a & b & x | a & !b & !x
     *      新b = !a & (b ^ x)
     * 因为最终结果，也就是当前二进制位只可能是 0 或 1，对应我们的状态表示就是 00 或 01，因此 我们不需要关注a，只需要返回 比特b
     *
     * 【由1个二进制位扩展到32个二进制位同时运算】
     *
     * 我们可以同时对 32 个二进制位进行运算，每一个二进制位的结果需要 两个比特位 以及 当前数字对应二进制位的值
     * 而int型正好是32位，所以用两个int整数就可以完成上面需要的32个a和32个b
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/WGki4K/solution/zhi-chu-xian-yi-ci-de-shu-zi-by-leetcode-0vrt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param nums
     * @return
     */
    public int singleNumber4(int[] nums) {
        // 32个a，b同时运算
        int a = 0, b = 0;
        for (int x : nums) {
            // 新a = !a & b & x | a & !b & !x
            // 新b = !a & (b ^ x)
            int na = (~a & b & x) | (a & ~b & ~x), nb = ~a & (b ^ x);
            a = na;
            b = nb;
        }
        // 最后只关心32个b
        return b;
    }


    /**
     * 方法四改进
     * // 新a = !a & b & x | a & !b & !x
     * // 新b = !a & (b ^ x)
     * 上面过程中，发现 新 b 的计算比较简单，而 新a 的计算比较复杂，
     * 上述相当于从 (a,b)和x同时得到 新a 和 新b，然后 分别写出 两个表达式，
     * 既然 新b计算简单，那么我们能够改为「分别计算」，即先计算出 b，再拿新的 b 值计算 a。
     *
     * 原真值表如下：
     *                (a, b)   x(当前数字这个bit位的值)   (新的a，新的b)
     *     (mod3=0)     00	        0	                    00          (mod3=0)
     *     (mod3=0)     00	        1	                    01          (mod3=1)
     *     (mod3=1)     01	        0	                    01          (mod3=1)
     *     (mod3=1)     01	        1	                    10          (mod3=2)
     *     (mod3=2)     10	        0	                    10          (mod3=2)
     *     (mod3=2)     10	        1	                    00          (mod3=0)
     *
     * 如果单独考虑新的b
     *                (a, b)   x(当前数字这个bit位的值)   新的b
     *                  00	        0	                 0
     *                  00	        1	                 1
     *                  01	        0	                 1
     *                  01	        1	                 0
     *                  10	        0	                 0
     *                  10	        1	                 0
     *
     * 通过真值表写逻辑表达式    新b = !a & !b & x | !a & b & !x  = !a & (!b & x | b & !x) = !a & (b ^ x)
     *
     * 【利用新b计算新a】
     * 如果我们将第一列的b换成新b，得到如下
     *                (a, [新b])   x(当前数字这个bit位的值)   新的a (从上面真值表抄下来，这里不是让你重新计算)
     *                  0 0	            0	                   0
     *                  0 1	            1	                   0
     *                  0 1	            0	                   0
     *                  0 0	            1	                   1
     *                  1 0	            0	                   1
     *                  1 0	            1	                   0
     *
     * 我们发现   新a = !a & !新b & x | a & !新b & !x  = !新b & (a ^ x)
     * 这样的话，就简单多了，所以 先计算新b，再计算新a
     *
     *          新b = !a & (b ^ x)
     *          新a = !新b & (a ^ x)
     * @param nums
     * @return
     */
    public int singleNumber5(int[] nums) {
        // 32个a，b同时运算
        int a = 0, b = 0;
        for (int x : nums) {
            // 新b = !a & (b ^ x)
            // 新a = !新b & (a ^ x)
            b = ~a & (b ^ x);
            a = ~b & (a ^ x);
        }
        // 最后只关心32个b
        return b;
    }

    public static void main(String[] args) {
        _004_NumberAppearOnlyOnce obj = new _004_NumberAppearOnlyOnce();
        obj.singleNumber(new int[]{2, 2, 3, 2});
        System.out.println(Integer.bitCount(Integer.MIN_VALUE));
    }
}
