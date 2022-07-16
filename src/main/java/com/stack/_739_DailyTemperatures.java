package com.stack;

import java.util.Stack;

/**
 * @author wangwei
 * 2021/11/26 15:11
 *
 * 请根据每日 气温 列表 temperatures ，请计算在每一天需要等几天才会有更高的温度。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 *
 * 示例 1:
 *
 * 输入: temperatures = [73,74,75,71,69,72,76,73]
 * 输出: [1,1,4,2,1,1,0,0]
 * 示例 2:
 *
 * 输入: temperatures = [30,40,50,60]
 * 输出: [1,1,1,0]
 * 示例 3:
 *
 * 输入: temperatures = [30,60,90]
 * 输出: [1,1,0]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/daily-temperatures
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _739_DailyTemperatures {

    /**
     * 单调栈
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
}
