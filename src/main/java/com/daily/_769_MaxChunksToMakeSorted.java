package com.daily;

/**
 * @author wangwei
 * @date 2022/10/13 9:43
 * @description: _769_MaxChunksToMakeSorted
 *
 * 769. 最多能完成排序的块
 * 给定一个长度为 n 的整数数组 arr ，它表示在 [0, n - 1] 范围内的整数的排列。
 *
 * 我们将 arr 分割成若干 块 (即分区)，并对每个块单独排序。将它们连接起来后，使得连接的结果和按升序排序后的原数组相同。
 *
 * 返回数组能分成的最多块数量。
 *
 *
 *
 * 示例 1:
 *
 * 输入: arr = [4,3,2,1,0]
 * 输出: 1
 * 解释:
 * 将数组分成2块或者更多块，都无法得到所需的结果。
 * 例如，分成 [4, 3], [2, 1, 0] 的结果是 [3, 4, 0, 1, 2]，这不是有序的数组。
 * 示例 2:
 *
 * 输入: arr = [1,0,2,3,4]
 * 输出: 4
 * 解释:
 * 我们可以把它分成两块，例如 [1, 0], [2, 3, 4]。
 * 然而，分成 [1, 0], [2], [3], [4] 可以得到最多的块数。
 *
 *
 * 提示:
 *
 * n == arr.length
 * 1 <= n <= 10
 * 0 <= arr[i] < n
 * arr 中每个元素都 不同
 */
public class _769_MaxChunksToMakeSorted {

    /**
     * 如果前 i+1 个数的最大值为 i（此时有max(arr[?])=?） ，那么前 i+1 个数排序后一定是[0,1,2,...,i]
     *
     * 我们从前往后处理所有的 arr[i]（即 i 定义为当前划分块的右边界下标），处理过程中定义变量 j 为当前划分块的左边界下标（初始值为 0），
     * 定义 min 为当前划分块中元素最小值（初始值为 arr[0] 或 n），定义 max 为当前划分块中元素最大值（初始值为 arr[0] 或 -1）。
     *
     *
     * 当且仅当 j=min 且 i=max 时，下标范围 [j,i] 排序结果为 [min,max]，
     * 此时块的个数加一，并重新初始化几个变量，继续循环统计下个块的信息。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/max-chunks-to-make-sorted/solution/by-ac_oier-4uny/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr
     * @return
     */
    public int maxChunksToSorted(int[] arr) {
        // min 保存 [j...i] 内最小值，max 保存 [j...i] 内最大值；
        // 当且仅当 j == min && i == max 时，找到一个有效段
        // 此时 ans++， min,max重新初始化，i，j从下一个位置开始
        int n = arr.length, min = n, max = -1, ans = 0;
        for (int i = 0, j = 0; i < n; ++i) {
            min = Math.min(min, arr[i]);
            max = Math.max(max, arr[i]);
            if (min == j && max == i) {
                ans++; j = i + 1; min = n; max = -1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        _769_MaxChunksToMakeSorted obj = new _769_MaxChunksToMakeSorted();
        System.out.println(obj.maxChunksToSorted(new int[]{2, 0, 1, 3}));
        System.out.println(obj.maxChunksToSorted(new int[]{0, 2, 1}));
        System.out.println(obj.maxChunksToSorted(new int[]{4, 3, 2, 1, 0}));
        System.out.println(obj.maxChunksToSorted(new int[]{1, 0, 2, 3, 4}));
    }
}
