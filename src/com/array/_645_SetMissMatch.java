package com.array;

/**
 * @author wangwei
 * 2020/8/31 15:41
 *
 * 集合 S 包含从1到 n 的整数。不幸的是，因为数据错误，导致集合里面某一个元素复制了成了集合里面的另外一个元素的值，导致集合丢失了一个整数并且有一个元素重复。
 *
 * 给定一个数组 nums 代表了集合 S 发生错误后的结果。你的任务是首先寻找到重复出现的整数，再找到丢失的整数，将它们以数组的形式返回。
 *
 * 示例 1:
 *
 * 输入: nums = [1,2,2,4]
 * 输出: [2,3]
 *
 */
public class _645_SetMissMatch {

    /**
     * 暂且将 nums 中的元素变为 [0..N-1]，这样每个元素就和一个数组索引完全对应了，这样方便理解一些。
     * 如果说 nums 中不存在重复元素和缺失元素，那么每个元素就和唯一一个索引值对应，对吧？
     * 现在的问题是，有一个元素重复了，同时导致一个元素缺失了，这会产生什么现象呢？
     * 会导致有两个元素对应到了同一个索引，而且会有一个索引没有元素对应过去。
     *
     * 那么，如果我能够通过某些方法，找到这个重复对应的索引，不就是找到了那个重复元素么？
     * 找到那个没有元素对应的索引，不就是找到了那个缺失的元素了么？
     *
     * 那么，如何不使用额外空间判断某个索引有多少个元素对应呢？这就是这个问题的精妙之处了：
     * 通过将每个索引对应的元素变成负数，以表示这个索引被对应过一次了：
     *
     * 比如对于 1 2 4 4
     * 第一次遇到4，把num[4]变成负数，再遇到4，发现num[4]已经负了，所以这个4肯定是第二次出现
     * 而因为没有3出现，所以num[3]没有被改变过，还是正数
     *
     *
     * @param nums
     * @return
     */
    public int[] findErrorNums1(int[] nums) {
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            // 数组元素和下标一一对应
            int index = Math.abs(nums[i]);
            // 这个索引对应的元素已经变为负数，说明这个索引已经对应了一个数字，说明这个数字有两个
            if (nums[index] < 0) {
                res[0] = index;
            } else {
                nums[index] *= -1;
            }
        }
        // 找到那个没有元素对应的索引，它对应的数组元素还是正的
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                res[1] = i;
            }
        }
        return res;
    }

    /**
     * 因为数组中的元素是 1...N，所以得稍微改一下
     * @param nums
     * @return
     */
    public int[] findErrorNums(int[] nums) {
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            // 数组元素和下标一一对应，差了个1
            int index = Math.abs(nums[i]) - 1;
            // 这个索引对应的元素已经变为负数，说明这个索引已经对应了一个数字，说明这个数字有两个
            if (nums[index] < 0) {
                res[0] = Math.abs(nums[index]);
            } else {
                nums[index] *= -1;
            }
        }
        // 找到那个没有元素对应的索引，它对应的数组元素还是正的
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                // 数组中元素从1开始，所以应该加1
                res[1] = i + 1;
            }
        }
        return res;
    }

    /**
     * 异或
     *
     * 数字与其本身做异或运算结果为 0
     *
     * 按照这个方法，将 nums 中所有的数字与 1 到 n 的所有数字做异或运算，得到的结果为 x^y
     * x 和 y 分别表示 nums 中重复数字和缺失数字。
     *
     * 比如 [1244] 和1234 全部异或，最终结果就是 3^4(3由于缺失匹配的数字所以留下，4由于多出一个所以留下)
     *
     * 在异或结果 xor 的二进制中，值为 1 的位置表示 x 和 y 在该位置的值不同，
     * 我们称 xor 最右边比特位为 rightmostbit，且使该位置为 1。
     * 【 xor & ~(xor - 1)】 这个运算能将xor二进制中最后一个1保留，其他位全变为0
     *
     * 根据 rightmostbit 不同将数组 nums 分为两部分。（看与运算结果是1还是0）
     * 第一部分所有数字的 rightmostbit 为 1，
     * 第二部分所有数字的 rightmostbit 为 0。
     *
     * 那么 x 和 y 会被分配到不同的部分(因为这个rightmostbit就是xy那个位置不一样)。
     * 此时问题就简化为最开始的简单问题。
     *
     * 根据 rightmostbit 的不同，将 1 到 n 的所有元素分为两部分。
     * 根据 rightmostbit 的不同，将 nums 中的所有元素分为两部分。
     *
     * 划分后，一部分中：重复元素x和好多成对的数字；另一部分：缺失元素y，好多成对的数字
     *
     * 在每一部分中，分别做异或操作，最终得到的就是 缺失数字x和重复元素y
     *
     * 然后从头到尾扫描一次数组，如果遇到x，说明x是重复元素，否则y是重复元素
     *
     *
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/set-mismatch/solution/cuo-wu-de-ji-he-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     */
    public int[] findErrorNums3(int[] nums) {
        int n = nums.length;
        // xor保存全部的异或结果
        // xor0和xor1分别保存划分后的两部分的异或结果
        int xor = 0, xor0 = 0, xor1 = 0;
        // 计算xor，nums中的全部元素以及1...n
        for (int num: nums) xor ^= num;
        for (int i = 1; i <= n; i++) xor ^= i;
        // 根据xor得到一个划分标准(只有一位为1的二进制数)
        int mostrightbit = xor & ~(xor - 1);
        // 根据划分标准，把nums划分为两部分，每一部分内求异或
        for (int num: nums) {
            if ((num & mostrightbit) != 0)
                xor1 ^= num;
            else
                xor0 ^= num;
        }
        // 根据划分标准，把1...n划分为两部分，每一部分内求异或
        for (int i = 1; i <= n; i++) {
            if ((i & mostrightbit) != 0)
                xor1 ^= i;
            else
                xor0 ^= i;
        }
        // xor0和xor1分别是重复元素和缺失元素，扫描一次数组，判断一下哪个才是重复元素
        for (int num: nums) {
            if (num == xor0)
                // 按要求的顺序返回结果
                return new int[]{xor0, xor1};
        }
        return new int[]{xor1, xor0};
    }
}
