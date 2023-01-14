package com.daily;

/**
 * @author wangwei
 * @date 2023/1/14 10:38
 * @description: _1819_NumberOfDifferentSubsequencesGcds
 *
 * 1819. 序列中不同最大公约数的数目
 * 给你一个由正整数组成的数组 nums 。
 *
 * 数字序列的 最大公约数 定义为序列中所有整数的共有约数中的最大整数。
 *
 * 例如，序列 [4,6,16] 的最大公约数是 2 。
 * 数组的一个 子序列 本质是一个序列，可以通过删除数组中的某些元素（或者不删除）得到。
 *
 * 例如，[2,5,10] 是 [1,2,1,2,4,1,5,10] 的一个子序列。
 * 计算并返回 nums 的所有 非空 子序列中 不同 最大公约数的 数目 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：nums = [6,10,3]
 * 输出：5
 * 解释：上图显示了所有的非空子序列与各自的最大公约数。
 * 不同的最大公约数为 6 、10 、3 、2 和 1 。
 * 示例 2：
 *
 * 输入：nums = [5,15,40,5,6]
 * 输出：7
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 2 * 105
 * 通过次数3,557提交次数8,401
 */
public class _1819_NumberOfDifferentSubsequencesGcds {

    /**
     * 方法一：枚举
     * 思路与算法
     *
     * 题目要求找到所有非空子序列中不同的最大公约数的数目，
     * 如果我们枚举nums的所有非空子序列，再计算每个子序列的最大公约数，最后统计不同公约数的个数，代价太高
     *
     * 逆向思维：我们可以尝试枚举所有的可能的最大公约数。
     * 将数组 nums 中的最大值记为 m，则数组 nums 的任意一个子序列的最大公约数一定在范围 [1,m] 中。
     * 分别判断范围 [1,m] 中的每个正整数是否可能为一个子序列的最大公约数。
     *
     * 对于正整数 i，只有 i 的倍数才有约数 i，不是 i 的倍数的整数一定没有约数 i，
     * 因此遍历 i 的倍数，若他的倍数出现在 nums 中，则加入集合 S ，
     * 只需要 S 中是否存在若干元素满足 gcd(x1,x2,...,xk) == i即可，
     *
     * 具体做法是，
     *       枚举 1 ~ m 每个正整数 i， 使用 S 保存 i 的倍数（nums中存在，不超过nums最大元素m）
     *       使用 subGcd 表示当前 S 中的所有 i 的倍数的最大公约数，初始时 S 为空，subGcd=0，执行如下操作。
     *          对于范围 [i,m] 中 i 的每个的倍数 j，如果 j 存在于 nums 中，
     *              更新 subGcd 为 gcd(subGcd,j)（gcd(a,b) 表示 a 和 b 的最大公约数,当b=0时返回a）。
     *              若 subGcd == i，则 i 为某个子序列的最大公约数，break，枚举下一个，ans++
     *
     * 实现方面，可以使用数组代替哈希集合。
     *
     * 作者：stormsunshine
     * 链接：https://leetcode.cn/problems/number-of-different-subsequences-gcds/solution/by-stormsunshine-mrrf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int countDifferentSubsequenceGCDs(int[] nums) {
        int maxVal = 0;
        // 统计 nums 最大值；使用数组代替hash表，记录nums中元素，实现快速判断数字是否出现在nums中，比hashset快
        boolean[] exist = new boolean[200010];
        for (int num : nums) {
            // 标记
            exist[num] = true;
            // 更新
            maxVal = Math.max(maxVal, num);
        }
        // 枚举 1，max 每个正整数，判断其是否可能为某个子序列的最大公约数
        int ans = 0;
        for (int i = 1; i <= maxVal; i++) {
            // S 保存 i 所有倍数（不超过maxVal，且存在于nums中）
            // subGcd 记录当前 S 中全部元素的 最大公约数
            // 初始 S 为 空，subGcd 为 0
            int subGcd = 0;
            // 遍历 i 所有倍数 j
            for (int j = i; j <= maxVal; j += i) {
                // 必须存在于 nums 中
                if (exist[j]) {
                    // 加入 S，更新 subGcd
                    subGcd = gcd(subGcd, j);
                    // S 中存在若干元素满足 gcd(x1,x2,...,xk) == i，且这些元素都存在于nums中
                    // 那么 i 为此子序列的最大公约数
                    if (subGcd == i) {
                        // 更新ans
                        ans++;
                        // 结束，枚举下一个
                        break;
                    }
                }
            }
        }
        // 返回
        return ans;
    }

    /**
     * 求 a 和 b 的最大公约数
     * @param a
     * @param b
     * @return
     */
    public int gcd(int a, int b) {
        while (b != 0) {
            int temp = a;
            a = b;
            b = temp % b;
        }
        return a;
    }
}
