package com.back;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2020/7/25 21:37
 *
 * 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
 *
 * 说明：解集不能包含重复的子集。
 *
 * 示例:
 *
 * 输入: nums = [1,2,3]
 * 输出:
 * [
 *   [3],
 *   [1],
 *   [2],
 *   [1,2,3],
 *   [1,3],
 *   [2,3],
 *   [1,2],
 *   []
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/subsets
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _78_Subsets {

    private List<List<Integer>> res = new ArrayList<>();

    /**
     * 回溯
     *
     *
     *
     *                               []
     *                 /             |            \
     *              /                |             \
     *            /                  |              \
     *           [1]                [2]             [3]
     *      /         \              |
     *   [1,2]      [1,3]         [2,3]
     *    |
     * [1,2,3]
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> subsets(int[] nums) {
        // 选择列表 1,3,4,...,n
        // 第一次选择从nums[0]开始
        // 临时列表保存已走路径
        backTrack(nums, 0, new ArrayList<>());
        return res;
    }

    /**
     * 回溯，选择列表是nums
     * 为了避免重复，如 [1,3] [3,1]，每次的选择列表需要限制开始位置
     * @param start
     * @param list
     */
    private void backTrack(int[] nums, int start, ArrayList<Integer> list) {
        // 求幂集，[1,2]算，[1,2,3]也算，
        // 所以不存在出口问题，只要进入回溯，就应该加入结果集
        // 与全排列的区别在于，全排列是一条路径包含了全部元素才能加入结果集（最终结果）
        // 求子集，就是路径每加入一个元素都要保存（中间过程）
        res.add(new ArrayList<>(list));
        // 限制选择
        for (int i = start; i < nums.length; ++i) {
            // 做选择
            list.add(nums[i]);
            // 下一步
            backTrack(nums, i + 1, list);
            // 撤销选择
            list.remove(list.size() - 1);
        }
    }

    /**
     * 递归
     *
     * 如果 A = subset([1,2]) ，那么：
     *
     * subset([1,2,3])
     *
     * = A + [A[i].add(3) for i = 1..len(A)]
     *
     * base： subsets([]) = []
     * @param nums
     * @return
     */
    public List<List<Integer>> subsets2(int[] nums) {
        return subset(nums, nums.length - 1);
    }

    private List<List<Integer>> subset(int[] nums, int idx) {
        List<List<Integer>> res = new ArrayList<>();
        // 空数组，返回空集
        if (idx == -1) {
            res.add(new ArrayList<>());
            return res;
        }

        // 要求 subsets([1,2,3])，先求subsets([1,2])
        List<List<Integer>> subs = subset(nums, idx - 1);
        // subset([1,2,3]) = A + [A[i].add(3) for i = 1..len(A)]
        res.addAll(subs);
        // 注意引用，
        for (List<Integer> set : subs) {
            // 这里需要重建对象（复制一份新的）
            ArrayList<Integer> temp = new ArrayList<>(set);
            // [A[i].add(3) for i = 1..len(A)]
            temp.add(nums[idx]);
            res.add(temp);
        }

        return res;
    }
}
