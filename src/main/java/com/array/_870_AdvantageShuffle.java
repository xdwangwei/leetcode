package com.array;

import java.util.*;

/**
 * @author wangwei
 * 2021/11/30 17:20
 *
 * 给定两个大小相等的数组A和B，A 相对于 B 的优势可以用满足A[i] > B[i]的索引 i的数目来描述。
 *
 * 返回A的任意排列，使其相对于 B的优势最大化。
 *
 *
 *
 * 示例 1：
 *
 * 输入：A = [2,7,11,15], B = [1,10,4,11]
 * 输出：[2,11,7,15]
 * 示例 2：
 *
 * 输入：A = [12,24,8,32], B = [13,25,32,11]
 * 输出：[24,32,8,12]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/advantage-shuffle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _870_AdvantageShuffle {

    /**
     * 双指针 + 优先队列
     *
     * 简单来说：干的过就自己上，干不过就找菜鸡顶替我
     *
     * 对于 nums2[i] ， 如果 nums1[i] > nums2[i]， 那么 i 位置 就选择 nums1[i]
     *                 否则，                      i 位置 放置最菜的那个
     *
     * 注意：由于是针对 nums2 每个位置去安排nums1的每个位置元素，所以如果要对nums2进行排序，那么我们需要保留nums2元素原来的索引
     * 或者新建一个索引数组，这里采用优先队列来完成对nums2的排序，队列中每个元素是(idx, val)
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] advantageCount(int[] nums1, int[] nums2) {
        // 不能打乱nums2的顺序，所以 使用优先队列，同时记录索引值和对应值，然后选择最大堆
        PriorityQueue<int[]> queue = new PriorityQueue<>(((o1, o2) -> o2[1] - o1[1]));
        int len = nums1.length;
        // nums2 降序保存
        for (int i = 0; i < len; ++i) {
            queue.offer(new int[]{i, nums2[i]});
        }
        // nums1 可以改变原顺序，所以直接排序，成升序了
        Arrays.sort(nums1);
        // 对于nums2的每个元素，nums1对应的位置应该放多少
        int[] res = new int[len];
        // 双指针，left指向nums1最菜的那个，right指向nums1最好的那个
        int left = 0, right = len - 1;
        // nums从大到小出战
        while (!queue.isEmpty()) {
            int[] poll = queue.poll();
            int index = poll[0], val = poll[1];
            // 如果nums1当前的最大值 比它大，那么自己上阵
            if (nums1[right] > val) {
                res[index] = nums1[right];
                // 最厉害的变成了倒数第二个
                right--;
            } else {
                // 打不过，保留老大，让小弟去送死
                res[index] = nums1[left];
                // 最菜的小弟挂了，倒数第二菜的准备上了
                left++;
            }
        }
        return res;
    }


    /**
     * 贪心 + 二分搜索
     *
     * 对于nums2每个位置的元素：
     *      若num1中有大于num2的数字，我们选择最小的大于num2的数字和其配对。
     *      若num1中没有大于num2的数字，则选择num1中最小的数字和其配对。
     *
     * 以nums1 = [2,7,11,15], nums2 = [1,10,4,11]为例：
     *
     *      大于1的最小元素为2，则ans[0] = 2;
     *      大于10的最小元素为11，则ans[1] = 11;
     *      大于4的最小元素为7，则ans[2] = 7;
     *      大于11的最小元素为15，则ans[3] = 15;
     *
     *
     * 作者：capital-worker
     * 链接：https://leetcode.cn/problems/advantage-shuffle/solution/you-shi-xi-pai-by-capital-worker-jl94/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] advantageCount2(int[] nums1, int[] nums2) {
        List<Integer> list = new ArrayList<>();
        // 排序nums1，用于二分搜索，因为每次选择一个位置元素后，要移除，所以转为list
        for (int num : nums1) {
            list.add(num);
        }
        Collections.sort(list);
        // 返回数组
        int[] res = new int[nums1.length];
        // 针对nums2每个位置元素，来安排nums1
        for (int i = 0; i < nums1.length; ++i) {
            // 在nums1中找到比当前位置元素大的 最小值
            int idx = rightBoundBinarySearch(list, nums2[i]);
            // 如果不存在，就选择nums1中最小的
            res[i] = list.remove(idx == -1 ? 0 : idx);
        }
        return res;
    }

    /**
     * 边界二分搜索
     * 在升序数组nums中寻找 大于 target 的最小元素位置，若不存在，则返回 -1
     * @param nums
     * @param target
     * @return
     */
    private int rightBoundBinarySearch(List<Integer> nums, int target) {
        int l = 0, r = nums.size();
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums.get(mid) <= target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l == nums.size() ? -1 : l;
    }


    /**
     * 方法三改进方法二：方法二存在多次list.toArray() 和 list.remove() 导致时间复杂度较高
     * 每次找到nums1元素后不再移除，而是标记其已使用，
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] advantageCount3(int[] nums1, int[] nums2) {
        // 排序
        Arrays.sort(nums1);
        // minIndex 是最小元素的索引
        int n = nums1.length, minIndex = 0;
        // 判断num1的元素 是否使用
        boolean[] visited = new boolean[n];
        // 保存结果
        int[] ans = new int[n];
        // 对于 nums2 中的每一个元素
        for (int i = 0; i < n; i++) {
            int left = 0, right = n;
            // 二分查找 找最小的大于 num2[i]的元素
            while (left != right) {
                int mid = left + ((right - left) >> 1);
                if (nums1[mid] <= nums2[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            // 过滤掉已经利用过的
            while (left < n && visited[left]) {
                left++;
            }
            // nums1中不存在 比 nums2[i] 大的最小元素
            if (left == n) {
                // 取最小的元素
                // 过滤掉已访问的，注意这里虽然 minIndex 是从0开始的，但可能 某一次 得到的left值刚好和minIndex一样，就需要过滤这个
                while (visited[minIndex]) {
                    minIndex++;
                }
                ans[i] = nums1[minIndex];
                // 标记已使用
                visited[minIndex++] = true;
            } else {
                // 选择目标位置元素
                ans[i] = nums1[left];
                // 标记已使用
                visited[left] = true;
            }
        }
        return ans;
    }
}
