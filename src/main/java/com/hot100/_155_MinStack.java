package com.hot100;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * 2022/4/19 16:01
 * 155. 最小栈
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 *
 * 实现 MinStack 类:
 *
 * MinStack() 初始化堆栈对象。
 * void push(int val) 将元素val推入堆栈。
 * void pop() 删除堆栈顶部的元素。
 * int top() 获取堆栈顶部的元素。
 * int getMin() 获取堆栈中的最小元素。
 *
 *
 * 示例 1:
 *
 * 输入：
 * ["MinStack","push","push","push","getMin","pop","top","getMin"]
 * [[],[-2],[0],[-3],[],[],[],[]]
 *
 * 输出：
 * [null,null,null,null,-3,null,0,-2]
 *
 * 解释：
 * MinStack minStack = new MinStack();
 * minStack.push(-2);
 * minStack.push(0);
 * minStack.push(-3);
 * minStack.getMin();   --> 返回 -3.
 * minStack.pop();
 * minStack.top();      --> 返回 0.
 * minStack.getMin();   --> 返回 -2.
 *
 *
 * 提示：
 *
 * -231 <= val <= 231 - 1
 * pop、top 和 getMin 操作总是在 非空栈 上调用
 * push, pop, top, and getMin最多被调用 3 * 104 次
 *
 */
public class _155_MinStack {

    /**
     * 首先，我们需要一个栈来进行正常的栈操作过程，需要特殊处理的问题是 0(1) 得到最小值
     * 并且，由于出栈可能会造成最小值被pop，我们需要保存的是所有的最小值，也就是按非递增顺序保存所有数字，假设这个结构是helper
     * 也就是说，helper里面元素非递减，而且如果stack pop的是最小元素，那么helper也要同时把这个元素移出去
     *
     * 这个helper最简单可以搞一个能自动维护非递减规则的结构，比如 优先队列，二叉堆等，但是 还要能从中随机删除stack移除的元素，那么 只能红黑树了，前面那辆只能移除顶端元素，但是 时间复杂度是 O(logN)
     *
     * 然后这个helper可以是一个栈，每次stack.push(x),时，helper也对应push一个数字，但是要保证helper是一个非递减栈
     *              也就是     如果  x<=helper.peek()。那么helper.push(x)
     *                        否则，我把helper.peek()再入一个
     * 这样的话，每次你stack pop。我helper也pop。这样就能保证如果stack把某个最小值pop了，那么helper里面也能对应把这个数字移除，时间复杂度 O(1)
     *
     *
     * 优化：
     * 但是这样有个问题就是heler里面可能保存了许多没必要的数字，原因是为了让helper和stack一一对应上，主要解决的还是stack pop最小值时helper也能对应pop的问题，其他时候都无所谓
     * 那么其实能够让helper不保存这些多余的数字，
     *      stack.push(x)时，
     *              如果 x <= helper.peek(),那么helper.push(x)，
     *              否则helper不保存，此时的x比当前栈顶(目前的最小值)大，对于这些数字，stack出栈，我根本不用管，我只在乎stack出某个最小值，那这些我不用管也不用存
     * 这样，每次 stack.pop()，如果 这个值 正好是 helper的顶端元素，那么 helper 才 pop，我们要的不就是这个对应移除？我只保存非递增的数字，其他我不用管，但是你移除这些数字，我也得对应移除
     *                                                                                    并且这些数字咋两都是按同样的先后顺序保存进来的，所以也能对应顺序移除
     *
     *
     * 还有个问题是 为什么helper不是严格递增？，也就是说 ，我只保存 比helper.peek()小的，
     *  加入stack连续push两个3呢，你只保存1次3，那stack pop 第一次时，你也跟着pop，pop完了，最小值应该还是3，但是helper里面已经没有3了
     *
     *
     * 辅助栈：
     * 写法1：helper和stack同时保存元素，helper非递减
     */
    class MinStack {

        Deque<Integer> stack, helper;

        public MinStack() {
            stack = new ArrayDeque<>();
            helper = new ArrayDeque<>();
        }

        public void push(int val) {
            // 正常栈操作
            stack.push(val);
            // 辅助栈维持非递减，保存val或者上一个栈顶
            if (helper.isEmpty() || val <= helper.peek()) {
                helper.push(val);
            } else {
                helper.push(helper.peek());
            }
        }
        // 两个同步出栈
        public void pop() {
            if (!stack.isEmpty()) {
                stack.pop();
                helper.pop();
            }
        }

        public int top() {
            // 返回正常栈栈顶
            if (!stack.isEmpty()) {
                return stack.peek();
            }
            throw new RuntimeException();
        }

        public int getMin() {
            // 返回辅助栈栈顶
            if (!helper.isEmpty()) {
                return helper.peek();
            }
            throw new RuntimeException();
        }
    }

    /**
     * 辅助栈写法2，辅助栈不用和stack严格同步，只在val<=helper.peek()时入栈val
     * 当然，stack出栈时，只有出栈元素和helper栈顶一样时，helper才出栈
     */
    class MinStack2 {

        Deque<Integer> stack, helper;

        public MinStack2() {
            stack = new ArrayDeque<>();
            helper = new ArrayDeque<>();
        }

        public void push(int val) {
            // 正常栈操作
            stack.push(val);
            // 辅助栈维持非递减，不保存多余的(比当前栈顶(最小值)大的不需要考虑)，
            if (helper.isEmpty() || val <= helper.peek()) {
                helper.push(val);
            }
        }
        // stack正常出栈，只有出栈元素==helper.peek helper才出栈
        public void pop() {
            if (!stack.isEmpty()) {
                int x = stack.pop();
                if (x == helper.peek()) {
                    helper.pop();
                }
            }
        }

        public int top() {
            // 返回正常栈栈顶
            if (!stack.isEmpty()) {
                return stack.peek();
            }
            throw new RuntimeException();
        }

        public int getMin() {
            // 返回辅助栈栈顶
            if (!helper.isEmpty()) {
                return helper.peek();
            }
            throw new RuntimeException();
        }
    }

}
