package com.stack;

import java.util.Stack;

/**
 * @author wangwei
 * 2020/7/26 19:26
 *
 * 503. 下一个更大元素 II
 * 给定一个循环数组（最后一个元素的下一个元素是数组的第一个元素），输出每个元素的下一个更大元素。数字 x 的下一个更大的元素是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 -1。
 *
 * 示例 1:
 *
 * 输入: [1,2,1]
 * 输出: [2,-1,2]
 * 解释: 第一个 1 的下一个更大的数是 2；
 * 数字 2 找不到下一个更大的数；
 * 第二个 1 的下一个最大的数需要循环搜索，结果也是 2。
 * 注意: 输入数组的长度不会超过 10000。
 */
public class _503_NextGreaterElement2 {


    /**
     * 单调栈
     * 假如数组不是循环数组
     * 因为要找的是它后面第一个比他大的元素，所以当处理num[i]时，它身后的元素都已经入栈，这样就能逐个弹出栈中比他小的元素
     * 如果栈空了，那么next[i]=-1.否则栈顶就是它身后第一个比他大的元素
     * 所以应该倒序遍历
     * 每个人都要往身后看，所以应该从后往前遍历
     * 再从后往前入栈，就能正着出栈
     * while 循环是把两个“高个”元素之间的元素排除，因为他们的存在没有意义，前面挡着个“更高”的元素，所以他们不可能被作为后续进来的元素的 Next Great Number 了
     * 如果栈空了，说明后面没有比他更高的
     * 否则栈顶元素就是他后面第一个比他高的
     * @param nums
     * @return
     */
    public int[] nextGreaterElements(int[] nums) {
        // 保存结果
        int[] res = new int[nums.length];
        Stack<Integer> stack = new Stack<>();
        // 倒着遍历
        for (int i = nums.length - 1; i >= 0; --i) {
            // 他后面的比他低的统统出栈
            while (!stack.empty() && stack.peek() <= nums[i])
                stack.pop();
            // 如果栈空了，说明后面没有比他更高的
            res[i] = stack.isEmpty() ? -1 : stack.peek();
            // 自己成为“高个”
            stack.push(nums[i]);
        }
        return res;
    }

    /**
     * 增加了环形属性后，问题的难点在于：这个 Next 的意义不仅仅是当前元素的右边了，有可能出现在当前元素的左边
     * 比如 [2,2,1,4,3] 本来对于3来说答案-1，但循环数组，答案变成4
     * 明确问题，问题就已经解决了一半了。
     * 我们可以考虑这样的思路：将原始数组“翻倍”，就是在后面再接一个原始数组，
     * 这样的话，按照之前“比身高”的流程，每个元素不仅可以比较自己右边的元素，而且也可以和左边的元素比较了。
     * 因为求的是他身后第一个比他高的，所以即便数组加长了，后面的元素也不会影响
     *
     * 可以把这个双倍长度的数组构造出来，然后套用算法模板。但是，我们可以不用构造新数组，而是利用循环数组的技巧来模拟
     */
    public int[] nextGreaterElements2(int[] nums) {
        // 保存结果
        int n = nums.length;
        int[] res = new int[n];
        Stack<Integer> stack = new Stack<>();
        // 倒着遍历 ，把索引i变成i%n即可，模拟循环数组
        for (int i = 2 * n - 1; i >= 0; --i) {
            // 他后面的比他低的统统出栈
            while (!stack.empty() && stack.peek() <= nums[i % n])
                stack.pop();
            // 如果栈空了，说明后面没有比他更高的
            res[i % n] = stack.isEmpty() ? -1 : stack.peek();
            // 自己成为“高个”
            stack.push(nums[i % n]);
        }
        return res;
    }
}
