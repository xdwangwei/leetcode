package com.back;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2020/4/20 22:19
 *
 * 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
 *
 * 示例:
 *
 * 输入: [1,2,3]
 * 输出:
 * [
 *   [1,2,3],
 *   [1,3,2],
 *   [2,1,3],
 *   [2,3,1],
 *   [3,1,2],
 *   [3,2,1]
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/permutations
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _46_Permutations {

    /**
     * 回溯法
     *
     * 每次选择一个数字，接下来从剩下的数字中进行选择。因为是排列，所以先选1后选3 和 先选3后选1是两种情况，不用对回溯进行特殊处理
     * @param nums
     * @return
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        backTrack(res, nums, new ArrayList<>());

        // 在第一种回溯中，backTrack中排除重复元素是 tempList.contains()时间复杂度是O(n)
        // 我们可以借助一个数组来表示它是否已访问，
        // boolean[] visited = new boolean[nums.length];
        // backTrack(res, nums, new ArrayList<>(), visited);
        return res;
    }


    private void backTrack(List<List<Integer>> res, int[] nums, List<Integer> tempList){
        // 出口
        if (tempList.size() == nums.length){
            res.add(new ArrayList<>(tempList));
            return;
        }
        // 做选择
        for (int num: nums){
            // 排除无效选择
            if (tempList.contains(num)) continue;
            // 做选择
            tempList.add(num);
            // 进入下一层
            backTrack(res, nums, tempList);
            // 回溯选择
            tempList.remove(tempList.size() - 1);
        }
    }

    private void backTrack(List<List<Integer>> res, int[] nums, List<Integer> tempList, boolean[] visited) {
        // 出口
        if (tempList.size() == nums.length) {
            res.add(new ArrayList<>(tempList));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            // 排除无效选择
            if (visited[i]) continue;
            // 做选择
            tempList.add(nums[i]);
            visited[i] = true;
            // 进入下一层
            backTrack(res, nums, tempList, visited);
            // 撤销选择
            tempList.remove(tempList.size() - 1);
            visited[i] = false;
        }
    }
}
