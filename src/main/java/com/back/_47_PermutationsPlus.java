package com.back;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * 2020/4/20 22:32
 * <p>
 * 给定一个可包含重复数字的序列，返回所有不重复的全排列。
 * <p>
 * 示例:
 * <p>
 * 输入: [1,1,2]
 * 输出:
 * [
 * [1,1,2],
 * [1,2,1],
 * [2,1,1]
 * ]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/permutations-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _47_PermutationsPlus {

    /**
     * 回溯法
     * @param nums
     * @return
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        // 先排序，解决重复元素问题，遇到重复元素时划过
        Arrays.sort(nums);
        // 用数组标记，list.contains()效率较低
        boolean[] visited = new boolean[nums.length];
        backTrack(res, nums, new ArrayList<>(), visited);
        return res;
    }

    private void backTrack(List<List<Integer>> res, int[] nums, List<Integer> tempList, boolean[] visited) {
        // 出口，所有元素均出现
        if (tempList.size() == nums.length) {
            res.add(new ArrayList<>(tempList));
            return;
        }
        // 遍历可选列表
        for (int i = 0; i < nums.length; i++) {
            // 排除无效选择
            // 当前元素已选取过
            // 当前元素没选取过，但是上个元素和它相等并且上个元素已选取过
            if (visited[i] || (i > 0 && visited[i-1] && nums[i-1] == nums[i])) continue;
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

    public static void main(String[] args) {
        System.out.println(new _47_PermutationsPlus().permuteUnique(new int[]{1, 1, 2}));
    }
}
