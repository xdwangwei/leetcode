package com.array;


/**
 * @author wangwei
 * 2020/8/31 12:09
 *
 * 给定一个包含 0, 1, 2, ..., n 中 n 个数的序列，找出 0 .. n 中没有出现在序列中的那个数。
 *
 *
 * 示例 1:
 *
 * 输入: [3,0,1]
 * 输出: 2
 * 示例 2:
 *
 * 输入: [9,6,4,2,3,5,7,0,1]
 * 输出: 8
 */
public class _268_MissingNumber {

    /**
     * 方法一：对数组排序，如果数字完整，应该满足 num[i] = i，第一个不满足的i，就是缺失的数字
     *      时间复杂度 O(nlogn)
     * 方法二：将数组中的元素加入集合中，再从0到n逐个去集合中判断是否存在，不存在的那个就是缺失的
     *      时间复杂度 O(n) 空间复杂度 O(n)
     *
     * @param nums
     * @return
     */
    public int missingNumber(int[] nums) {
        return missingNumber1(nums);
    }


    /**
     * 方法三：异或，如果数字完整，应该满足 num[i] = i，i ^ num[i] = 0
     *      下标范围是 0...n-1,如果完整，数组元素也应该是 0...n-1，缺失一个，那么变为 0...n
     *      所以把全部的 i ^ num[i]异或起来，最后结果是 缺失的数字 ^ n，给他再异或一下 n就得到结果
     *
     *      时间复杂度 O(N) 空间复杂度 O(1)
     *
     * @param nums
     * @return
     */
    public int missingNumber1(int[] nums) {
        int res = 0, n = nums.length;
        // 和其他的 元素、索引 做异或
        for (int i = 0; i < n; i++) {
            res ^= i ^ nums[i];
        }
        // 和多出的那个数字异或一下(本应该是0-n-1,数组里面多出一个n)
        res ^= n;
        // 最终得到的就是缺失的
        return res;
    }

    /**
     * 方法四：连续数字求和，从 0-n 的和为 n * (n + 1) / 2
     *      再减去数组元素之和，就是缺失的那个数字
     *
     *      时间复杂度 O(N) 空间复杂度 O(1)
     *
     * @param nums
     * @return
     */
    public int missingNumber2(int[] nums) {
        int n = nums.length;
        // 完整的从0到n的和
        int sum = n * (n + 1) / 2;
        // 减去数组元素的和
        for (int num : nums) {
            sum -= num;
        }
        // 剩下的就是缺失的那个数字
        return sum;
    }

    /**
     * 方法五：方法四如果n太大，可能出现溢出，我们可不可以边求和边做减法
     * 其实从 0 + 1 + ... + n 就是 数组的下标在求和
     * 至于减去数组元素的和 就是减去 num[0],num[1],num[2],...,num[n-1]
     * 所以我们只需要对所有 i - num[i] 求和就能得到答案了,当然得把n加上，数组下标到n-1结束了
     *
     *      时间复杂度 O(N) 空间复杂度 O(1)
     *
     * @param nums
     * @return
     */
    public int missingNumber3(int[] nums) {
        // 得把n加上，数组下标到n-1结束了
        int n = nums.length;
        // 对所有 i - num[i] 求和
        for (int i = 0; i < nums.length; i++) {
            n += i - nums[i];
        }
        // 剩下的就是缺失的那个数字
        return n;
    }
}
