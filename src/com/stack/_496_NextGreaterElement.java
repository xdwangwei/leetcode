package com.stack;

import java.util.HashMap;
import java.util.Stack;

/**
 * @author wangwei
 * 2020/7/26 19:20
 * <p>
 * 给定两个 没有重复元素 的数组 nums1 和 nums2 ，其中nums1 是 nums2 的子集。找到 nums1 中每个元素在 nums2 中的下一个比其大的值。
 * <p>
 * nums1 中数字 x 的下一个更大元素是指 x 在 nums2 中对应位置的右边的第一个比 x 大的元素。如果不存在，对应位置输出 -1 。
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
     * 我们可以忽略数组 nums1，
     * 先对将 nums2 中的每一个元素，求出其下一个更大的元素。
     * 随后对于将这些答案放入哈希映射（HashMap）中，再遍历数组 nums1，并直接找出答案。
     * 对于 nums2，我们可以使用单调栈来解决这个问题。
     * <p>
     * 每个人都要往身后看，所以应该从后往前遍历
     * 再从后往前入栈，就能正着出栈
     * while 循环是把两个“高个”元素之间的元素排除，因为他们的存在没有意义，前面挡着个“更高”的元素，所以他们不可能被作为后续进来的元素的 Next Great Number 了
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
}
