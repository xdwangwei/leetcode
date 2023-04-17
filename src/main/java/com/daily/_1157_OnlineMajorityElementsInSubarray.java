package com.daily;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * @date 2023/4/16 19:18
 * @description: _1157_OnlineMajorityElementsInSubarray
 *
 * 设计一个数据结构，有效地找到给定子数组的 多数元素 。
 *
 * 子数组的 多数元素 是在子数组中出现threshold次数或次数以上的元素。
 *
 * 实现 MajorityChecker 类:
 *
 * MajorityChecker(int[] arr)会用给定的数组 arr对MajorityChecker 初始化。
 * int query(int left, int right, int threshold)返回子数组中的元素 arr[left...right]至少出现threshold次数，如果不存在这样的元素则返回 -1。
 *
 *
 * 示例 1：
 *
 * 输入:
 * ["MajorityChecker", "query", "query", "query"]
 * [[[1, 1, 2, 2, 1, 1]], [0, 5, 4], [0, 3, 3], [2, 3, 2]]
 * 输出：
 * [null, 1, -1, 2]
 *
 * 解释：
 * MajorityChecker majorityChecker = new MajorityChecker([1,1,2,2,1,1]);
 * majorityChecker.query(0,5,4); // 返回 1
 * majorityChecker.query(0,3,3); // 返回 -1
 * majorityChecker.query(2,3,2); // 返回 2
 *
 *
 * 提示：
 *
 * 1 <= arr.length <= 2 * 104
 * 1 <= arr[i] <= 2 * 104
 * 0 <= left <= right < arr.length
 * threshold <= right - left + 1
 * 2 * threshold > right - left + 1
 * 调用query的次数最多为104
 * 通过次数4,743提交次数13,059
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/online-majority-element-in-subarray
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1157_OnlineMajorityElementsInSubarray {


    /**
     * 记录元素出现位置 + 二分搜索
     *
     * 顺序遍历 arr，记录 arr 中每个元素出现的位置，得到 <val, idxes> 键值对，且 idxes 一定是元素不重复的递增序列
     * 按照 idxes.size() 进行逆序排序得到 <val, idxes>[]
     * 这样，第一个键值对 <x, idxes> 就是 arr 中 出现次数最多的元素 x 及它的出现位置
     *
     * 对于 query(left, right, threshold)
     *      顺序遍历 <val, idxes>[]
     *          如果 当前 idxes.size() < threshold，返回 -1；（最大次数都 < threshold，其他的也不用看了）
     *          否则 （idxes.size() < threshold）
     *              当前元素 x 的出现次数 >= threshold
     *              需要进一步判断，其在 [left, right] 内的出现次数
     *              在 x 所有出现位置 idxes 中寻找 left位置之后的  第一次 出现位置 （左边界二分搜索），得到其在 idxes 中的下标 l
     *              在 x 所有出现位置 idxes 中寻找 right位置之前的 最后一次 出现位置 （右边界二分搜索），得到其在 idxes 中的下标 r
     *              那么，元素 x 在 [left, right] 范围内实际出现的 位置是 idxes[l].idxes[l+1]...idxes[r]，共 r- l + 1 次
     *              如果 r - l + 1 >= threshold，返回 x
     *              否则，遍历下一个键值对。。。。
     *
     *  实际测试是否排序结果变化不大，是因为 threshold 的不确定性 和 每个元素出现位置的 分布 不同，有时不排序更符合随机情况，结果会好点
     *
     *  通过全部测试用例，击败 10%，耗时 1238ms
     */
    public class MajorityChecker {

        private List<Map.Entry<Integer, List<Integer>>> entryList;

        public MajorityChecker(int[] arr) {
            Map<Integer, List<Integer>> map = new HashMap<>();
            // 得到每个元素及其所有出现位置的键值对
            for (int i = 0; i < arr.length; i++) {
                int x = arr[i];
                map.putIfAbsent(x, new ArrayList<>());
                map.get(x).add(i);
            }
            // 对键值对排序，按照 出现次数 从大到小
            entryList = map.entrySet().stream().sorted((o1, o2) -> o2.getValue().size() - o1.getValue().size()).collect(Collectors.toList());
        }

        public int query(int left, int right, int threshold) {
            // 顺序遍历键值对
            for (Map.Entry<Integer, List<Integer>> entry : entryList) {
                // 当前元素 x 和 它的所有出现位置  idxes
                int x = entry.getKey();
                List<Integer> idxes = entry.getValue();
                // 如果 当前 idxes.size() < threshold，返回 -1；（最大次数都 < threshold，其他的也不用看了）
                if (idxes.size() < threshold) {
                    return -1;
                }
                // 需要进一步判断，其在 [left, right] 内的出现次数
                // 在 x 所有出现位置 idxes 中寻找 left位置之后的  第一次 出现位置 （左边界二分搜索），得到其在 idxes 中的下标 l
                // 在 x 所有出现位置 idxes 中寻找 right位置之前的 最后一次 出现位置 （右边界二分搜索），得到其在 idxes 中的下标 r
                // 那么，元素 x 在 [left, right] 范围内实际出现的 位置是 idxes[l].idxes[l+1]...idxes[r]，共 r- l + 1 次
                int l = leftBound(idxes, left);
                int r = rightBound(idxes, right);
                // 如果 r - l + 1 >= threshold，返回 x
                // 否则，遍历下一个键值对。。。。
                if (r - l + 1 >= threshold) {
                    return x;
                }
            }
            return -1;
        }
    }

    /**
     * 改进
     *
     *
     * """  评论原内容
     *      可以先假设存在这个数，然后再通过这个方法来检验。
     *      相当于省去遍历idx的过程。确定这个数的方法如下：
     *      对arr数组，针对每一个bit位设置一个前缀数组记录bit 1个数的前缀和。
     *      这样对于任意区间可以在O(1)时间复杂度，确定这个区间内使这个bit位1的元素个数，同时可以确定这个区间内bit位为0的个数。
     *      由于2 * threshold > right - left + 1，0bit数量和1bit数量不可能同时>=threshold,
     *      因此如果存在这样一个数满足条件，可以确定这个bit是0还是1，
     *      最后再由这一条评论的方法检测这个数的数目是否>=threshold即可
     * """
     *
     * 可以先假设存在这个数，然后再通过 二分搜索 来检验这个数在 left，right 范围内的次数是否 >= threshold。
     *
     * 相当于省去遍历键值对 <x, idxes>的过程。
     *
     * 确定这个数的方法如下：对 arr 数组的元素，针对每一个 bit 位 设置一个前缀数组记录 此 bit位 为1 的元素个数的前缀和。
     *
     * arr[i] <= 2 * 10^4，因此最多有 15 个二进制位
     *
     * int n = arr.length;
     * bitOnesPreSum = new int[15][n]
     *
     * bit[j][i]，表示 第 j 个 bit 位，nums[0...i] 元素中，此bit位为1的元素个数
     *
     * 这样对于任意区间[left, right]，可以在O(1)时间复杂度，确定这个区间内的nums元素，某个bit位上是1的 元素个数，同时可以确定这个区间内某个bit位为0的个数。
     *
     * bitOnes = bitOnesPreSum[j][right] - bitOnesPreSum[j][left - 1]  // 第 j 个 bit 位，nums[left...right] 此位置为1的元素个数
     *
     * 由于 2 * threshold > right - left + 1，所以如果这个数字candidate存在，那么它在[left, right] 这个区间内至少出现 一半以上，
     * 那么可以得到candidate的每个二进制位：
     *      如果 [left,right] 内元素 第j个bit位为1 的元素个数 >= threshold，那么 candidate 这个二进制位为1，否则为0
     *
     * 这样，就可以得到预期的元素 candidate
     *
     * 如果 arr 中并不存在这个元素，返回 -1
     *
     * 否则，通过 二分搜索 判断这个元素 在 [left, right] 范围内的 出现次数是否真的 >= threshold
     *
     */
    public class MajorityChecker2 {

        // 每个元素及其所有出现位置
        private Map<Integer, List<Integer>> map;

        // bit[j][i]，表示 第 j 个 bit 位，nums[0...i] 前i个元素中，此bit位为1的元素个数
        private int[][] bitOnesPreSum;

        public MajorityChecker2(int[] arr) {
            map = new HashMap<>();
            int n = arr.length;
            // 最多15个二进制位
            bitOnesPreSum = new int[15][n];
            // 得到每个元素及其所有出现位置
            for (int i = 0; i < n; i++) {
                int x = arr[i];
                map.computeIfAbsent(x, k -> new ArrayList<>()).add(i);
                // 遍历这个元素每个二进制位
                for (int j = 0; j < 15; ++j) {
                    int curBit = (x >> j & 1) == 1 ? 1 : 0;
                    // 前缀和计算，注意数组越界
                    bitOnesPreSum[j][i] = i > 0 ? bitOnesPreSum[j][i - 1] + curBit : curBit;
                }
            }
        }

        public int query(int left, int right, int threshold) {
            // 假设这个元素存在
            int candidate = 0;
            // 判断它每个二进制位的是1还是0
            for (int i = 0; i < 15; ++i) {
                int bitOnes = bitOnesPreSum[i][right] - (left > 0 ? bitOnesPreSum[i][left - 1] : 0);
                // 这个区间内有至少 threshold个数字（超过一半）的这个二进制位是1，那么 candidate 这个二进制位一定是1
                if (bitOnes >= threshold) {
                    candidate |= 1 << i;
                }
            }
            // 得到一个预期的数字
            // 实际并不存在
            if (!map.containsKey(candidate)) {
                return -1;
            }
            // 验证这个元素在 当前区间内的出现次数是否真的 >= threshold
            // 当前元素 candidate 和 它的所有出现位置  idxes
            List<Integer> idxes = map.get(candidate);
            // 如果 当前 idxes.size() < threshold，返回 -1；（最大次数都 < threshold，其他的也不用看了）
            if (idxes.size() < threshold) {
                return -1;
            }
            // 需要进一步判断，其在 [left, right] 内的出现次数
            // 在 x 所有出现位置 idxes 中寻找 left位置之后的  第一次 出现位置 （左边界二分搜索），得到其在 idxes 中的下标 l
            // 在 x 所有出现位置 idxes 中寻找 right位置之前的 最后一次 出现位置 （右边界二分搜索），得到其在 idxes 中的下标 r
            // 那么，元素 x 在 [left, right] 范围内实际出现的 位置是 idxes[l].idxes[l+1]...idxes[r]，共 r- l + 1 次
            int l = leftBound(idxes, left);
            int r = rightBound(idxes, right);
            // 如果 r - l + 1 >= threshold，返回 x
            // 否则，遍历下一个键值对。。。。
            if (r - l + 1 >= threshold) {
                return candidate;
            }
            return -1;
        }
    }

    /**
     * 左边界二分查找
     * 在 递增 nums 中 寻找第一个 大于等于 target 的元素的索引
     * nums 中每个元素唯一
     * @param nums
     * @param target
     * @return
     */
    private int leftBound(List<Integer> nums, int target) {
        int l = 0, r = nums.size();
        while (l < r) {
            int m = (l + r) >> 1;
            // 寻找最左的满足要求的位置，所以继续缩小右边界
            if (nums.get(m) >= target) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        // 在符合要求的位置m处，会进行 r = m，所以最后退出时，r 是有效位置
        return r;
    }

    /**
     * 在 递增 nums 中 寻找最后一个 小于等于 target 的元素的索引
     * nums 中每个元素唯一
     * @param nums
     * @param target
     * @return
     */
    private int rightBound(List<Integer> nums, int target) {
        int l = 0, r = nums.size();
        while (l < r) {
            int m = (l + r) >> 1;
            // 寻找最右的满足要求的位置，所以继续增大左边界
            if (nums.get(m) <= target) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        // 在符合要求的位置m处，会进行 l = m + 1，所以最后退出时，l - 1 是有效位置
        return l - 1;
    }
}
