package com.offerassult;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/12/15 19:27
 * @description: _060_TopKFrequentElements
 *
 * 剑指 Offer II 060. 出现频率最高的 k 个数字
 * 给定一个整数数组 nums 和一个整数 k ，请返回其中出现频率前 k 高的元素。可以按 任意顺序 返回答案。
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
 * 进阶：所设计算法的时间复杂度 必须 优于 O(n log n) ，其中 n 是数组大小。
 *
 *
 *
 * 注意：本题与主站 347 题相同：https://leetcode-cn.com/problems/top-k-frequent-elements/
 *
 * 通过次数21,035提交次数30,505
 */
public class _060_TopKFrequentElements {

    /**
     * 方法一：词频统计 + 最小堆
     * 先用hashmap进行统计频次，得到一组 (val， freq) 类型键值对数据
     * 接着参考topk问题解决方案：
     *      维护一个大小为k的最小堆，堆中元素按照freq从小到大排序，这样，堆顶元素就是词频最小元素
     *      当一个新的元素加入堆中，如果队列元素数目大于k，那么移除第一个元素（频数最小）
     *      这样，最终堆中剩余的k个元素就是剩下的频率最大的k个元素
     * 最后将堆中k个元素转存到数组并返回
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
        // 优先队列（最小堆，按照出现频率排序），设置规则，按照频数从低到高
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(countMap::get));
        for (Integer num: countMap.keySet()) {
            // 加入元素
            queue.offer(num);
            // 队列元素不超过k个，否则移除第一个频数最小的元素
            if (queue.size() > k) {
                queue.remove();
            }
        }
        // 最后堆中剩下的就是k个频数最高的元素
        int[] res = new int[k];
        int i = 0;
        while (i < k) {
            res[i++] = queue.poll();
        }
        return res;
    }


    /**
     * 方法二：词频统计 + 桶排序
     * 先用hashmap进行统计频次，得到一组 (val， freq) 类型键值对数据
     * 然后按照频率值的不同，将所有元素分配到不同频率值对应的桶中
     * 用数组实现桶，用频率作为桶的索引，这样，频率值（索引值）自然递增
     * 然后，我们按照频率值递减序（倒序遍历）从所有桶中取出k个元素
     *
     * 时间复杂度 O(N)
     * 空间复杂度 O(N)
     */
    public int[] topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        // 统计出现次数
        for (int num: nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        // 用数组实现桶，用频率值作为桶的索引，一个元素最多出现 nums.length 次，所以桶编号最大为 nums.length
        // list[j]代表出现次数为 j 的所有元素
        List<Integer>[] list = new ArrayList[nums.length + 1];
        // 将 元素 按照其 频数 转存到对应的 桶 中
        for (Integer key : countMap.keySet()) {
            // 频数就是桶的下标
            int count = countMap.get(key);
            if (list[count] == null) {
                list[count] = new ArrayList<>();
            }
            // 加入当前桶
            list[count].add(key);
        }
        // 数组索引（桶编号，频率值）自然递增
        // 倒序从所有桶中取元素，取够k个即可。
        int[] res = new int[k];
        // 从最后一个桶开始取
        for(int i = list.length - 1; i > 0; --i) {
            // 当前桶中没元素，跳过
            if (list[i] == null) {
                continue;
            }
            // 从当前桶中取元素，累计最多取 k 个
            for (Integer num : list[i]) {
                if (k > 0) {
                    res[--k] = num;
                } else {
                    return res;
                }
            }
        }
        return res;
    }


    /**
     * 方法三：词频统计 + 快速排序（选择）
     * 先用hashmap进行统计频次，得到一组 (val， freq) 类型键值对数据
     * 接着参考topk问题解决方案：
     * 我们可以使用基于快速排序的方法，求出「出现次数数组」的前 k 大的值。
     *
     * qsort(int[][] arr, int start, int end, int[] ans, int idx, int k)
     * 表示当前需要从arr[start, end] 中选择k个词频最大的元素，填充到 ans[idx....idx+k-1]
     *
     * 在对数组 arr[l…r] 做快速排序的过程中，我们首先将数组划分为两个部分 arr[i…q−1] 与 arr[q+1…j]，
     * 并使得 arr[i…q−1] 中的每一个值都不超过 arr[q]，且 arr[q+1…j] 中的每一个值都大于 arr[q]。
     *
     * 于是，我们根据 k 与左侧子数组 arr[i…q−1] 的长度（为 q−i）的大小关系：
     *      如果 len=r-(q+1)+1 > k，那么 arr[l…r] 前 k 大的值，就等于子数组 arr[q+1…r] 前 k 大的值。
     *          表示我们需要从 arr[q+1...j] 中继续选择前k个词频最大的元素
     *      否则，arr[q+1...j] 这些不足k个的元素肯定可以直接被选择，并填充到ans中
     *          如果 len < k，那么 我们还需要继续去 arr[l...q] 范围内选择 剩下的k-len个元素
     *          当然了，因为 len=[q+1...r] 不超过k个，
     *              如果 len < k，那么 arr[q] 也可以选择填充，此时如果刚好凑够k个，那么结束；
     *                      否则剩下的去 arr[l...q-1]范围内选择
     *              如果 len = k，那么 arr[q+1...r] 正好凑够k个，直接结束
     *
     * 原版的快速排序算法的平均时间复杂度为 O(NlogN)。
     * 我们的算法中，每次只需在其中的一个分支递归即可，因此算法的平均时间复杂度降为 O(N)。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/top-k-frequent-elements/solution/qian-k-ge-gao-pin-yuan-su-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public int[] topKFrequent3(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        // 统计出现次数
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        // 转存到数组中进行快速选择，arr[i] = [val, freq]
        int[][] arr = new int[countMap.keySet().size()][2];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            arr[i++] = new int[]{entry.getKey(), entry.getValue()};
        }
        int[] ans = new int[k];
        // 快速选择，从 ans idx位置开始填充，需要从 arr[0...len-1]范围选出前k个最大频数的元素
        qsort(arr, 0, arr.length - 1, ans, 0, k);
        return ans;
    }

    /**
     * 快速选择，从 arr[0...len-1]范围选出前k个最大频数的元素，将其填充到ans[idx....idx+k-1]位置
     * @param arr
     * @param start
     * @param end
     * @param ans
     * @param idx  【此次】选出的k个最大频数元素，从ans哪个位置开始填充
     * @param k    【此次】qsort需要选择前几名频数最大元素
     */
    private void qsort(int[][] arr, int start, int end, int[] ans, int idx, int k) {
        // 快速排序partition模板
        // ...p] <= pivot < [p+1...
        // 先随机选一个位置和end位置交换，
        int rand = (int) (Math.random() * (end - start + 1) + start);
        // 再选择end位置为pivot
        swap(arr, rand, end);
        // 注意这里比较的是频数，不是元素值
        int pivot = arr[end][1];
        // 最终 arr[...p] <= pivot < arr[p+1...]
        // 初始化 p 为 start - 1
        int p = start - 1;
        // 快速排序模板，从左往右扫描，到end停止
        int i = start;
        while (i < end) {
            // 当前元素应该划分到 ...p]
            if (arr[i][1] < pivot) {
                swap(arr, ++p, i);
            }
            i++;
        }
        // 将pivot放置到合适位置，
        // 此时 arr[start,p-1] <= arr[p] < arr[p+1...end]
        swap(arr, ++p, end);
        // arr[p+1...end] 部分 长度是 end - (p+1) + 1 = end - p
        // 如果右半部分元素个数 > k 个，那么 arr[l..r] 的前k大，相当于 arr[p+1...r]的前k大，
        if (end - p > k) {
            qsort(arr, p + 1, end, ans, idx, k);
        } else {
            // 如果右半部分元素个数 <= k 个，这些元素可以直接选择
            // 如果 右半部分元素个数 == k 个，那么 会在 arr[p] 之前结束
            // 如果 右半部分元素个数 < k 个，此时 arr[p] 也可以选择
            for(int j = end; j >= p && k > 0; --j) {
                ans[idx++] = arr[j][0];
                k--;
            }
            // 如果 选完 右半部分甚至 arr[p] 后，还是没有凑够 k 个
            // 那么此时需要去 [start, p-1] 范围内凑够剩下的元素，注意 idx 和 k 在上面for循环已改变
            if (k > 0) {
                qsort(arr, start, p - 1, ans, idx, k);
            }
        }
    }

    private void swap(int[][] arr, int x, int y) {
        if (x == y) {
            return;
        }
        int[] temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }

    public static void main(String[] args) {
        _060_TopKFrequentElements obj = new _060_TopKFrequentElements();
        obj.topKFrequent2(new int[]{1}, 1);
        obj.topKFrequent2(new int[]{1, 1, 1, 2, 2, 3}, 2);
    }
}
