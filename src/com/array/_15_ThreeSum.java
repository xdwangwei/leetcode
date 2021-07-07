package com.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.hash;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/12/1 周日 10:39
 **/
public class _15_ThreeSum {
    public static List<List<Integer>> solution(int[] nums){
        List<List<Integer>> resList = new ArrayList<>();
        Arrays.sort(nums); // 先排序 -4 -1 -1 0 0 1 2 4
        int len = nums.length;
        for (int i = 0; i < len; i++){
            // 当前元素已做过第一个元素，经历过一次匹配过程，就跳过
            if (i > 0 && nums[i] == nums[i-1]) continue; 
            int head = i + 1, tail = len - 1;
            while (head < tail){
                if (nums[i] + nums[head] + nums[tail] > 0) tail--;
                else if (nums[i] + nums[head] + nums[tail] < 0) head++;
                else {
                    resList.add(Arrays.asList(nums[i], nums[head], nums[tail]));
                    // 过滤掉值相等的元素
                    while(head < len - 1 && nums[head] == nums[++head]);
                    while(tail > 0 && nums[tail] == nums[--tail]);
                }
            }
        }
        return resList;
    }

    public static void main(String[] args) {
        // System.out.println(solution(new int[]{-1, 0, 1, 2, -1, -4}).toString());
        System.out.println(solution(new int[]{0,-4,-1,-4,-2,-3,2}).toString());
        // System.out.println(solution(new int[]{0,0,0}).toString());
    }
}
