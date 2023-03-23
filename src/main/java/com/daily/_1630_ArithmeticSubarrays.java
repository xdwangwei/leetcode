package com.daily;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/3/23 11:47
 * @description: _1630_ArithmeticSubarrays
 *
 * 1630. 等差子数组
 * 如果一个数列由至少两个元素组成，且每两个连续元素之间的差值都相同，那么这个序列就是 等差数列 。更正式地，数列 s 是等差数列，只需要满足：对于每个有效的 i ， s[i+1] - s[i] == s[1] - s[0] 都成立。
 *
 * 例如，下面这些都是 等差数列 ：
 *
 * 1, 3, 5, 7, 9
 * 7, 7, 7, 7
 * 3, -1, -5, -9
 * 下面的数列 不是等差数列 ：
 *
 * 1, 1, 2, 5, 7
 * 给你一个由 n 个整数组成的数组 nums，和两个由 m 个整数组成的数组 l 和 r，后两个数组表示 m 组范围查询，其中第 i 个查询对应范围 [l[i], r[i]] 。所有数组的下标都是 从 0 开始 的。
 *
 * 返回 boolean 元素构成的答案列表 answer 。如果子数组 nums[l[i]], nums[l[i]+1], ... , nums[r[i]] 可以 重新排列 形成 等差数列 ，answer[i] 的值就是 true；否则answer[i] 的值就是 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [4,6,5,9,3,7], l = [0,0,2], r = [2,3,5]
 * 输出：[true,false,true]
 * 解释：
 * 第 0 个查询，对应子数组 [4,6,5] 。可以重新排列为等差数列 [6,5,4] 。
 * 第 1 个查询，对应子数组 [4,6,5,9] 。无法重新排列形成等差数列。
 * 第 2 个查询，对应子数组 [5,9,3,7] 。可以重新排列为等差数列 [3,5,7,9] 。
 * 示例 2：
 *
 * 输入：nums = [-12,-9,-3,-12,-6,15,20,-25,-20,-15,-10], l = [0,1,6,4,8,7], r = [4,4,9,7,9,10]
 * 输出：[false,true,false,false,true,true]
 *
 *
 * 提示：
 *
 * n == nums.length
 * m == l.length
 * m == r.length
 * 2 <= n <= 500
 * 1 <= m <= 500
 * 0 <= l[i] < r[i] < n
 * -105 <= nums[i] <= 105
 * 通过次数20,536提交次数26,443
 */
public class _1630_ArithmeticSubarrays {

    /**
     * 方法一：数学 + 模拟
     *
     * 我们设计一个函数 check(nums,l,r)，用于判断子数组 nums[l],nums[l+1],…,nums[r] 是否可以重新排列形成等差数列。
     * 在主函数中，我们遍历所有的查询，对于每个查询 l[i] 和 r[i]，我们调用函数 check(nums,l[i],r[i]) 将结果存入答案数组中。
     *
     * 函数 check(nums,l,r) 的实现逻辑如下：
     *
     * 首先，我们计算子数组的元素个数 n=r−l+1，在遍历的过程中，将子数组中的元素放入集合 s 中，并得到子数组中的最小值 a1 和 最大值 an ；
     *
     * 如果 an - a1 不能被 n−1 整除，那么子数组不可能形成等差数列，直接返回 false；
     *
     * 否则，我们计算等差数列的公差 d=(an-a1)/(n-1)
     * 接下来从 i = 1 开始，依次计算等差数列中第 i 项元素 a1 + (i−1)×d 是否在集合 s 中，
     * 若某一项不在集合s中，那么子数组不可能形成等差数列，直接返回 false；
     * 否则，当我们遍历完所有的元素，说明子数组可以重新排列形成等差数列，返回 true。
     *
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/arithmetic-subarrays/solution/python3javacgotypescript-yi-ti-yi-jie-sh-o8fj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param l
     * @param r
     * @return
     */
    public List<Boolean> checkArithmeticSubarrays(int[] nums, int[] l, int[] r) {
        List<Boolean> ans = new ArrayList<>();
        // 遍历所有的查询，对于每个查询 l[i] 和 r[i]，我们调用函数 check(nums,l[i],r[i]) 将结果存入答案数组中。
        for (int i = 0; i < l.length; ++i) {
            ans.add(check(nums, l[i], r[i]));
        }
        // 返回
        return ans;
    }

    /**
     * 用于判断子数组 nums[l],nums[l+1],…,nums[r] 是否可以重新排列形成等差数列。
     * @param nums
     * @param l
     * @param r
     * @return
     */
    private boolean check(int[] nums, int l, int r) {
        // 我们计算子数组的元素个数 n=r−l+1，
        int n = r - l + 1;
        // 在遍历的过程中，将子数组中的元素放入集合 s 中，并得到子数组中的最小值 a1 和 最大值 an ；
        Set<Integer> s = new HashSet<>();
        int a1 = nums[l], an = nums[l];
        for (int i = l; i <= r; ++i) {
            s.add(nums[i]);
            a1 = Math.min(a1, nums[i]);
            an = Math.max(an, nums[i]);
        }
        // 如果 an - a1 不能被 n−1 整除，那么子数组不可能形成等差数列，直接返回 false；
        if ((an - a1) % (n - 1) != 0) {
            return false;
        }
        // 计算等差数列公差
        int d = (an - a1) / (n - 1);
        // 从 i = 1 开始，依次计算等差数列中第 i 项元素 a1 + (i−1)×d 是否在集合 s 中，
        for (int i = 1; i < n; ++i) {
            // 若不在，则子数组不构成等差数列
            if (!s.contains(a1 + (i - 1) * d)) {
                return false;
            }
        }
        // 每一项都在，则构成等差数列
        return true;
    }
}
