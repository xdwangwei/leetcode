package com.array;

import javax.swing.*;
import java.util.Arrays;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/12/1 周日 20:12
 *
 * 给定一个包括n 个整数的数组nums和 一个目标值target。找出nums中的三个整数，使得它们的和与target最接近。返回这三个数的和。假定每组输入只存在唯一答案。
 *
 *
 *
 * 示例：
 *
 * 输入：nums = [-1,2,1,-4], target = 1
 * 输出：2
 * 解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/3sum-closest
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 **/
public class _16_ThreeSumClosest {
    
    public static int solution(int[] nums, int target){
        int res = 0;
        // 三者之和与目标值的差
        int minDiff = Integer.MAX_VALUE;
        // 排序，双指针
        Arrays.sort(nums);
        int len = nums.length;
        for (int i = 0; i < len; i++){
            // 跳过重复元素
            if (i > 0 && nums[i] == nums[i -1]) continue;
            int head = i + 1, tail = len - 1;
            while (head < tail){
                // 三者之和
                int sum = nums[i] + nums[head] + nums[tail];
                // 最接近就是相等
                if (sum == target){
                    return sum;
                }
                // 比较接近，比上一组更接近target
                if (Math.abs(sum - target) < minDiff){
                    // 更新最小差距 和 结果
                    minDiff = Math.abs(sum - target);
                    res = sum;
                // 与目标值差太多
                }else {
                    // 如果三者和太小
                    if (sum < target)
                        // 收缩右边界
                        while(head < len - 1 && nums[head] == nums[++head]);
                    else
                        // 右移左边界
                        while(tail > 0 && nums[tail] == nums[--tail]);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        // System.out.println(solution(new int[]{-1,1,2,-4}, 1));
        System.out.println(solution(new int[]{0,2,1,-3}, 1));
    }
}
