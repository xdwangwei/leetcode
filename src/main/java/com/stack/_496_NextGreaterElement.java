package com.stack;

import java.util.HashMap;
import java.util.Stack;

/**
 * @author wangwei
 * 2020/7/26 19:20
 * <p>
 * 给定两个 没有重复元素 的数组nums1 和nums2，其中nums1是nums2的子集。找到nums1中每个元素在nums2中的下一个比其大的值。
 * <p>
 * nums1中数字x的下一个更大元素是指x在nums2中对应位置的右边的第一个比x大的元素。如果不存在，对应位置输出 -1 。
 * <p>
 *
 * <p>
 * 示例 1:
 * <p>
 * 输入: nums1 = [4,1,2], nums2 = [1,3,4,2].
 * 输出: [-1,3,-1]
 * 解释:
 * <p>
 * 对于num1中的数字4，你无法在第二个数组中找到下一个更大的数字，因此输出 -1。
 * 对于num1中的数字1，第二个数组中数字1右边的下一个较大数字是 3。
 * 对于num1中的数字2，第二个数组中没有下一个更大的数字，因此输出 -1。
 * 示例 2:
 * <p>
 * 输入: nums1 = [2,4], nums2 = [1,2,3,4].
 * 输出: [3,-1]
 * 解释:
 *     对于 num1 中的数字 2 ，第二个数组中的下一个较大数字是 3 。
 * 对于 num1 中的数字 4 ，第二个数组中没有下一个更大的数字，因此输出 -1 。
 *  
 * <p>
 * 提示：
 * <p>
 * nums1和nums2中所有元素是唯一的。
 * nums1和nums2 的数组大小都不超过1000。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/next-greater-element-i
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _496_NextGreaterElement {


    /**
     *
     * leetcode
     *
     * 因为nums1是nums2的子集，所以可以先忽略数组 nums1，对nums2中的每一个元素，找到它在nums中下一个比它大的元素，
     * 并将这些答案放入哈希映射（HashMap）中，再遍历数组 nums1，并直接找出答案。
     * 对于 nums2，我们可以使用单调栈来解决这个问题。
     * <p>
     * 每个人都要找到它后面第一个比他大的值，也就是说，对于每个位置，只考虑从它往后的位置
     * 那么如果顺序遍历，那就是
     *      0  --》 1， 2， 3， 4， 5
     *      1  --   2， 3，4， 5
     *      2  --   3，4，5
     *      很明显，一直在重复
     * 但如果倒着遍历，那就是
     *      4  --  5
     *      3  --  4，5
     *      2  --  3，4，5
     *      看起来好像也在重复，但是别急，
     * 从后往前 遍历+入栈， 就能实现，
     *      处理每个元素时，它身后的元素已经放在栈中，并且从栈顶到栈底就是原顺序，逐个判断栈顶，比它小就弹出，
     *      遇到比它大的栈顶就停止，也就找到了比它大的第一个元素，然后把自己入栈。
     *    弹出元素是否会影响对下一个元素的处理，当然不会!!!
     *    比如 现在处理3，栈中是 底[4, 2, 1]顶，那么 12出栈，3入栈， 底[4, 3]顶
     *          下一个处理原数组中3前面的x，对于它来说栈顶是3，如果x<3那直接满足，弹出的元素对它来说没有意义，本来就被3挡住了
     *                                                  如果x>3，那栈顶元素3比它小，要弹栈，3都比它小了，之前处理3的时候弹出的那些比3小的元素就更没用了
     * 需要注意的是这个弹栈过程可能一次弹出多个元素，应该是一个while 循环是把两个“高个”元素之间的元素排除，因为他们的存在没有意义，前面挡着个“更高”的元素，所以他们不可能被作为后续进来的元素的 Next Great Number 了
     * 如果栈空了，说明后面没有比他更高的
     * 否则栈顶元素就是他后面第一个比他高的
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        // nums2 中的每一个元素，求出其下一个更大的元素。
        // 倒序遍历
        for (int i = nums2.length - 1; i >= 0; --i) {
            // 把它身后比他小的全部出栈
            while (!stack.empty() && stack.peek() <= nums2[i])
                stack.pop();
            // 栈空说明他后面没有比他大的
            map.put(nums2[i], stack.empty() ? -1 : stack.peek());
            // 他成为新的“防火墙”
            stack.push(nums2[i]);
        }
        // 只需要返回nums1这些元素的下一个比他的值
        int[] res = new int[nums1.length];
        for (int i = 0; i < nums1.length; ++i)
            res[i] = map.get(nums1[i]);
        return res;
    }


    /**
     * labuladong，这个函数和题目不一样，只是来说单调栈思想
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
}
