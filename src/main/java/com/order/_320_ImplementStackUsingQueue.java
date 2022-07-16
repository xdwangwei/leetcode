package com.order;

import java.util.LinkedList;

/**
 * @author wangwei
 * 2020/4/23 15:59
 *
 * 使用队列实现栈的下列操作：
 *
 * push(x) -- 元素 x 入栈
 * pop() -- 移除栈顶元素
 * top() -- 获取栈顶元素
 * empty() -- 返回栈是否为空
 * 注意:
 *
 * 你只能使用队列的基本操作-- 也就是 push to back, peek/pop from front, size, 和 is empty 这些操作是合法的。
 * 你所使用的语言也许不支持队列。 你可以使用 list 或者 deque（双端队列）来模拟一个队列 , 只要是标准的队列操作即可。
 * 你可以假设所有操作都是有效的（例如, 对一个空的栈不会调用 pop 或者 top 操作）。
 * 通过次数48,985提交
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/implement-stack-using-queues
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _320_ImplementStackUsingQueue {

    class MyStack {

        private LinkedList<Integer> queue;
        private int topItem;

        /** Initialize your data structure here. */
        public MyStack() {
            // 用链表模拟队列
            queue = new LinkedList<>();
        }

        /** Push element x onto stack. */
        public void push(int x) {
            // 队列尾部加入
            queue.add(x);
            // 成为栈顶元素
            topItem = x;
        }

        /** Removes the element on top of the stack and returns that element. */
        /**
         * 加入队列是 1-->2-->3-->4-->5  现在要pop也就是要把5删除
         * 我们可以把队列变成这样 5-->1-->2-->3-->4,也就是重复 把对队头元素删除，添加到队尾 队列的大小-1 次
         * 然后再直接return remove()即可
         *
         * 但问题是，移除5之后，4会变为新的栈顶元素，所以我们减少一次上面那个过程，也就是只重复 队列大小-2次
         * 队列变为  4-->5-->1-->2-->3  然后，先 topItem = queue.peek()，新的栈顶元素保存了之后
         * 再把这个4移到队尾，让队列变成 5-->1-->2-->3-->4， 再 return remove()即可
         * @return
         */
        public int pop() {
            int size  = queue.size();
            while (size-- > 2){
                queue.add(queue.remove());
            }
            // 保存移除队尾之后，新的栈顶元素
            topItem = queue.peek();
            // 保存完，把它移到队尾
            queue.add(queue.remove());
            // 此时的队头就是原来的队尾，也就是要弹出的栈顶元素
            return queue.remove();
        }

        /** Get the top element. */
        public int top() {
            return topItem;
        }

        /** Returns whether the stack is empty. */
        public boolean empty() {
            return queue.isEmpty();
        }
    }

    /**
     * 也可以不用这个topItem保存栈顶，每次入队后，颠倒之前元素的顺序就可以
     * 比如 1 入 2 --> 颠倒1变成2-->1 再入3 2-->1-->3 颠倒 21 变成  3-->2-->1
     * 也就是说，每次push调整完后队列元素就满足栈的顺序，其他操作就和操作栈一样了
     */
    class MyStack2 {

        private LinkedList<Integer> queue;

        /** Initialize your data structure here. */
        public MyStack2() {
            // 用链表模拟队列
            queue = new LinkedList<>();
        }

        /** Push element x onto stack. */
        public void push(int x) {
            // 队列尾部加入
            queue.add(x);
            // 调整之前元素位置，使成为栈
            int size = queue.size();
            while (size-- > 1){
                queue.add(queue.remove());
            }
        }

        /** Removes the element on top of the stack and returns that element. */
        public int pop() {
            // push完后已调整成一个栈结构，直接移除第一个即可
            return queue.remove();
        }

        /** Get the top element. */
        public int top() {
            return queue.peek();
        }

        /** Returns whether the stack is empty. */
        public boolean empty() {
            return queue.isEmpty();
        }
    }
}
