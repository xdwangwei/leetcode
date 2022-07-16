package com.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.hash;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/12/1 周日 10:39
 *
 * 15. 三数之和
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
 *
 * 注意：答案中不可以包含重复的三元组。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [-1,0,1,2,-1,-4]
 * 输出：[[-1,-1,2],[-1,0,1]]
 * 示例 2：
 *
 * 输入：nums = []
 * 输出：[]
 * 示例 3：
 *
 * 输入：nums = [0]
 * 输出：[]
 *
 *
 * 提示：
 *
 * 0 <= nums.length <= 3000
 * -105 <= nums[i] <= 105
 **/
public class _15_ThreeSum {

    /**
     * 排序+双指针
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        int n = nums.length;
        // 少于三个元素，直接返回
        if (n < 3) {
            return ans;
        }
        // 排序
        Arrays.sort(nums);
        // 枚举第一个元素
        for (int i = 0; i < n - 2; i++) {
            // 已递增，当前元素>0，后序不存在连续三个元素和为0，提前结束
            if (nums[i] > 0) {
                return ans;
            }
            // 避免重复
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            // 左右指针
            int low = i + 1, high = n - 1;
            while (low < high) {
                int target = -nums[i];
                if (nums[low] + nums[high] < target) {
                    low++;
                } else if (nums[low] + nums[high] > target) {
                    high--;
                } else {
                    // 找到，保留
                    ans.add(Arrays.asList(nums[i], nums[low], nums[high]));
                    // 做指针推进，右指针推进 low++,high--
                    // 因为可能有重复元素，因此循环推进
                    while (low < high && nums[low] == nums[++low]);
                    while (low < high && nums[high] == nums[--high]);
                }
            }
        }
        return ans;
    }

}
