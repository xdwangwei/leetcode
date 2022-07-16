package com.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/20 周三 16:28
 * <p>
 * 给定一个整数数组 nums和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * <p>
 * <p>
 * 示例:
 * <p>
 * 给定 nums = [2, 7, 11, 15], target = 9
 * <p>
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/two-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class _01_FindTwoIndexForTarget {

    /**
     * 暴力搜索
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] solution1(int[] nums, int target) {
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) { // O(n*n)
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return res;
    }

    /**
     * hashmap
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] solution2(int[] nums, int target) {
        int[] res = new int[2];
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                // nums[i]是map中的一个键，说明之前存进去的某个元素所需的互补元素就是它
                res[0] = map.get(nums[i]); // 之前那个元素的下标
                res[1] = i; // 我的下标
                return res;
            } else
                // 把当前元素所需的互补的元素的值存进去，表示下标为i的元素需要的是target-nums[i]
                map.put(target - nums[i], i);
        }
        return res;
    }


    public static void main(String[] args) {
        // int[] arr = {3,2,4};
        // System.out.println(solution1(arr, 6)[0]);
        // System.out.println(solution2(arr, 6)[0]);
        int n = 1;
        System.out.println(n);
        n = ~n;
        System.out.println(n);
        n = -~n;

        System.out.println(n);
        // 现在 n = 2
        System.out.println((char) ('a' ^ ' '));
    }
}
