package com.hot100;

/**
 * @author wangwei
 * @date 2022/4/25 16:18
 * @description: _FindTheDulplicateNumber
 *
 * 287. 寻找重复数
 *
 * 给定一个包含 n + 1 个整数的数组 nums ，其数字都在 [1, n] 范围内（包括 1 和 n），可知至少存在一个重复的整数。
 *
 * 假设 nums 只有 一个重复的整数 ，返回 这个重复的数 。
 *
 * 你设计的解决方案必须 不修改 数组 nums 且只用常量级 O(1) 的额外空间。
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,3,4,2,2]
 * 输出：2
 * 示例 2：
 *
 * 输入：nums = [3,1,3,4,2]
 * 输出：3
 *
 *
 * 提示：
 *
 * 1 <= n <= 105
 * nums.length == n + 1
 * 1 <= nums[i] <= n
 * nums 中 只有一个整数 出现 两次或多次 ，其余整数均只出现 一次
 *
 *
 * 进阶：
 *
 * 如何证明 nums 中至少存在一个重复的数字?
 * 你可以设计一个线性级时间复杂度 O(n) 的解决方案吗？
 */
public class _287_FindTheDulplicateNumber {

    /**
     * 暴力：先排序，再找重复
     * 或者，直接hashset
     * 位运算, 发现怎么异或好像都出不来结果，所以要换方向   】
     * 0 1 2 3 4
     * 1 2 3 4
     * 3 2 2 1
     *
     *
     * 妙妙妙的快慢指针
     *
     * 这一题可以利用寻找链表的入环节点的思想, 把数组当成对链表的一种描述, 数组里的每一个元素的值表示链表的下一个节点的索引
     *
     * 如示例1中的[1, 3, 4, 2, 2]
     *
     * 把数组索引为0的元素当成链表的头节点
     * 索引为0的元素的值为1, 表示头节点的下一个节点的索引为1, 即数组中的3
     * 再下一个节点的索引为3, 即为第一个2
     * 再下一个节点的索引为2, 即为4
     * 再下一个节点的索引为4, 即为第二个2
     * 再下一个节点的索引为2, 即为4
     * 此时形成了一个环
     * 而形成环的原因是下一节点的索引一致, 即为重复的数字
     *
     * 对于环形链表，使用快慢指针找到环的起点
     *
     * 作者：mupup
     * 链接：https://leetcode-cn.com/problems/find-the-duplicate-number/solution/qian-duan-ling-hun-hua-shi-tu-jie-kuai-man-zhi-z-3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int findDuplicate(int[] nums) {
        // 起点开始
        int fast = 0, slow = 0;
        while (true) {
            // fast 一次走两步
            fast = nums[nums[fast]];
            // slow 一次走一次
            slow = nums[slow];
            // 二者第一次相遇
            if (fast == slow) {
                // 让slow回到起点
                slow = 0;
                // 二者再次相遇的位置就是 环的起点
                while (slow != fast) {
                    fast = nums[fast];
                    slow = nums[slow];
                }
                // 至于这里返回 slow 和 nums[slow]，
                // 此时 slow 已经是位置，在这道题中，nums[i]是位置，所以直接返回slow本身，nums[slow]是下一个位置
                // 实在不确定就拿测试用例测一次
                return slow;
            }
        }
    }


    /**
     * 也很奇妙的 二分搜索
     * 可以使用「二分查找」的原因
     * 因为题目要找的是一个 整数，并且这个整数有明确的范围，所以可以使用「二分查找」。
     *
     * 重点理解：这个问题使用「二分查找」是在数组 [1, 2,.., n] 中查找一个整数，而 并非在输入数组数组中查找一个整数。
     *
     * 具体一点：【这想法想不到啊，反而上面那个循环链表相对好想一点】
     *
     * 定义 cnt[i] 表示 nums 数组中小于等于 i 的数有多少个，假设我们重复的数 target，
     * 那么[1, target − 1]里的所有数i满足 cnt[i] ≤ i (因为都不重复)； [target, n] 里的所有数满足 cnt[i] > i(因为target是重复数字，所以会多1个)，具有单调性。
     *
     * 然后：数组长度是 n + 1, 每个元素取值范围是 1 -- n，也就是数 搜索范围是 1 - n 即 1 -- len(nums)-1
     * 那么对于这个范围内任意一个数字 m , 统计原始数组中 小于等于 m 的元素的个数 cnt：
     *
     *      如果 cnt 严格大于 m（取值<=m的数字个数竟然比m大，那么肯定有某个数字重复）: 根据抽屉原理，重复元素就在区间 [left...mid] 里；
     *      否则，重复元素就在区间 [mid + 1..right] 里。
     *
     * 根据这个思路完成二分搜索
     *
     * 作者：liweiwei1419
     * 链接：https://leetcode-cn.com/problems/find-the-duplicate-number/solution/er-fen-fa-si-lu-ji-dai-ma-python-by-liweiwei1419/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/find-the-duplicate-number/solution/xun-zhao-zhong-fu-shu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int findDuplicate2(int[] nums) {
        // 搜索范围 1--len-1，使用左包右不包形式的二分搜索
        // 这里的 l 和 h 是实际数值，不是 下标
        int l = 1, h = nums.length;
        while (l < h) {
            int mid = l + (h - l) >> 1;
            // 统计 <= mid 的数字个数
            int cnt = 0;
            for (int n : nums) {
                if (n <= cnt) {
                    cnt++;
                }
            }
            // 如果 cnt 严格大于 m（取值<=m的数字个数竟然比m大，那么肯定有某个数字重复）: 根据抽屉原理，重复元素就在区间 [left...mid] 里；
            if (cnt > mid) {
                h = mid;
                // 否则 target 就 在 1 -- mid-1
            } else {
                l = mid + 1;
            }
        }
        // 最后 l == h
        return l;
    }


    /**
     * 一次扫描
     * 因为所有数字取值都是 [1, n] 可以将其映射到一个索引 ，将这个索引位置的数字变为相反数(负数)
     * 如果遍历到某个数字。发现这个数字对应的索引位置的数字已经是负数了，说明这个索引出现第二次了，说明能产生这个索引的数字有两个，返回这个数字
     * @return
     */

    private int findDulplicate3(int[] nums) {
        for (int i = 0; i < nums.length; ++i) {
            // 每个数字唯一对应一个索引
            // 由于数字本身可能已被之前某个数字对应索引后变为相反数，索引这里加绝对值
            int idx = Math.abs(nums[i]) - 1;
            // 如果这个索引位置数字已经是负数
            // 说明这个索引是第二次出现，即能产生这个索引的数字有两个
            if (nums[idx] < 0) {
                // 索引 + 1就是产生这个索引的数字
                return idx + 1;
            }
            // 对应索引位置变为相反数
            nums[idx] *= -1;
        }
        return -1;
    }
}
