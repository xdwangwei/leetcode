package com.offerassult;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * @author wangwei
 * @date 2022/11/1 10:45
 * @description: _038_DailyTemperatures
 *
 * 剑指 Offer II 038. 每日温度
 * 请根据每日 气温 列表 temperatures ，重新生成一个列表，要求其对应位置的输出为：要想观测到更高的气温，至少需要等待的天数。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 *
 *
 *
 * 示例 1:
 *
 * 输入: temperatures = [73,74,75,71,69,72,76,73]
 * 输出: [1,1,4,2,1,1,0,0]
 * 示例 2:
 *
 * 输入: temperatures = [30,40,50,60]
 * 输出: [1,1,1,0]
 * 示例 3:
 *
 * 输入: temperatures = [30,60,90]
 * 输出: [1,1,0]
 *
 *
 * 提示：
 *
 * 1 <= temperatures.length <= 105
 * 30 <= temperatures[i] <= 100
 *
 *
 * 注意：本题与主站 739 题相同： https://leetcode-cn.com/problems/daily-temperatures/
 */
public class _038_DailyTemperatures {

    /**
     *
     * 其实就是寻找每个数字 右边 第一个 大于自己 的值 与自己的距离
     *
     * 单调栈
     *
     * 从后往前遍历
     * 每个元素只关心自己后面的比自己大的元素，且只需要得到第一个比他的元素的位置
     * 所以从后往前遍历+入栈，对于每个元素，栈中元素从顶到底就是原数组中它之后比它的元素的顺序排放
     * 逐个比较栈顶元素与自身，比自己小，就弹栈，如果栈空了，说明没有比自己大的，得到0，
     * 再把自己入栈
     *
     * 因为要计算的是这两个位置的距离，所以在栈里面保存下标
     * @param temperatures
     * @return
     */
    public int[] dailyTemperatures(int[] temperatures) {
        if (temperatures == null || temperatures.length < 2) {
            return temperatures;
        }
        int len = temperatures.length;
        // 结果集
        int[] res = new int[len];
        Stack<Integer> stack = new Stack<>();
        // 从后往前遍历
        for (int i = len - 1; i >= 0; i--) {
            // 移除后面所有比自己小的元素
            while (!stack.isEmpty() && temperatures[stack.peek()] <= temperatures[i]) {
                stack.pop();
            }
            // 栈空了，后面没有比自己大的，否则栈顶就是下一个比自己大的，距离就是 栈顶元素(索引) - 当前索引
            res[i] = stack.isEmpty() ? 0 : stack.peek() - i;
            // 自己入栈，成为下一个元素(自己前面)的第一个挡板
            stack.push(i);
        }
        return res;
    }


    /**
     * 单调栈
     * 从前往后遍历
     *
     * 若当前温度 > 栈顶温度，那么对于栈顶温度来说，当前温度就是右边第一个大于它的值，计算二者插值
     *
     * 当前入栈
     * @param temperatures
     * @return
     */
    public int[] dailyTemperatures2(int[] temperatures) {
        // 保存结果
        int[] ans = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < temperatures.length; ++i) {
            // 当前温度大于栈顶温度
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                // 当前温度就是栈顶温度右边第一个比它大的值
                int idx = stack.pop();
                // 计算差值并记录答案
                ans[idx] = i - idx;
            }
            // 当前入栈
            stack.push(i);
        }
        // 返回
        return ans;
    }
}
