package com.hot100;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/4/26 16:11
 * @description: _347_TopKFrequentElements
 *
 * 347. 前 K 个高频元素
 * 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums = [1,1,1,2,2,3], k = 2
 * 输出: [1,2]
 * 示例 2:
 *
 * 输入: nums = [1], k = 1
 * 输出: [1]
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * k 的取值范围是 [1, 数组中不相同的元素的个数]
 * 题目数据保证答案唯一，换句话说，数组中前 k 个高频元素的集合是唯一的
 *
 *
 * 进阶：你所设计算法的时间复杂度 必须 优于 O(n log n) ，其中 n 是数组大小。
 */
public class _347_TopKFrequentElements {

    /**
     * 先统计频次，再使用优先队列，按照频次从低到高的规则 往队列里添加元素
     * 只维护队列元素最多k个，每加入一个元素，如果队列元素数目大于k，那么移除第一个元素（频数最小）
     * 最后剩下的前k个频数最大的元素
     *
     * 时间复杂度 O(NlogK) k 是 堆中元素个数
     * 空间复杂度 O(N)
     * @param nums
     * @param k
     * @return
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        // 统计出现次数
        for (int num: nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        // 优先队列，设置规则，按照频数从低到高
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(countMap::get));
        for (Integer num: countMap.keySet()) {
            // 加入元素
            queue.offer(num);
            // 队列元素不超过k个，否则移除第一个频数最小的元素
            if (queue.size() > k) {
                queue.remove();
            }
        }
        // 队列剩下的就是前k个频数最高的元素
        int[] res = new int[k];
        int i = 0;
        while (i < k) {
            res[i++] = queue.poll();
        }
        return res;
    }


    /**
     * 先计数，再桶排序
     * count[i]表示 i 出现的次数
     * list[j]代表出现次数为 j 的所有元素
     * 因为题目说答案唯一，那么 我们从 最后一个 非空 list 开始加入结果，知道选出 k 个元素即可。
     * 将频率作为数组下标，对于出现频率不同的数字集合，存入对应的数组下标即可。
     *
     *      * 时间复杂度 O(N)
     *      * 空间复杂度 O(N)
     */
    public int[] topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        // 统计出现次数
        for (int num: nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        // list[j]代表出现次数为 j 的所有元素
        // 以频数为下标，频数最大就是所有数组元素相同，那么就是nums.length
        List<Integer>[] list = new ArrayList[nums.length + 1];
        // 统计每个频数 对应的 所有元素
        for (Integer key : countMap.keySet()) {
            int count = countMap.get(key);
            if (list[count] == null) {
                list[count] = new ArrayList<>();
            }
            list[count].add(key);
        }
        // 找到频数从高到底选取元素加入res
        int[] res = new int[k];
        for(int i = list.length - 1; i > 0 && k > 0; --i) {
            if (list[i] == null) {
                continue;
            }
            // 填入res
            for (Integer num : list[i]) {
                res[--k] = num;
                if (k < 0) {
                    break;
                }
            }
        }
        return res;
    }
}
