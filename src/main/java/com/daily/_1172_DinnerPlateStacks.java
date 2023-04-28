package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/4/28 10:54
 * @description: _1172_DinnerPlateStacks
 *
 * 1172. 餐盘栈
 * 我们把无限数量 ∞ 的栈排成一行，按从左到右的次序从 0 开始编号。每个栈的的最大容量 capacity 都相同。
 *
 * 实现一个叫「餐盘」的类 DinnerPlates：
 *
 * DinnerPlates(int capacity) - 给出栈的最大容量 capacity。
 * void push(int val) - 将给出的正整数 val 推入 从左往右第一个 没有满的栈。
 * int pop() - 返回 从右往左第一个 非空栈顶部的值，并将其从栈中删除；如果所有的栈都是空的，请返回 -1。
 * int popAtStack(int index) - 返回编号 index 的栈顶部的值，并将其从栈中删除；如果编号 index 的栈是空的，请返回 -1。
 *
 *
 * 示例：
 *
 * 输入：
 * ["DinnerPlates","push","push","push","push","push","popAtStack","push","push","popAtStack","popAtStack","pop","pop","pop","pop","pop"]
 * [[2],[1],[2],[3],[4],[5],[0],[20],[21],[0],[2],[],[],[],[],[]]
 * 输出：
 * [null,null,null,null,null,null,2,null,null,20,21,5,4,3,1,-1]
 *
 * 解释：
 * DinnerPlates D = DinnerPlates(2);  // 初始化，栈最大容量 capacity = 2
 * D.push(1);
 * D.push(2);
 * D.push(3);
 * D.push(4);
 * D.push(5);         // 栈的现状为：    2  4
 *                                     1  3  5
 *                                     ﹈ ﹈ ﹈
 * D.popAtStack(0);   // 返回 2。栈的现状为：      4
 *                                           1  3  5
 *                                           ﹈ ﹈ ﹈
 * D.push(20);        // 栈的现状为：  20  4
 *                                    1  3  5
 *                                    ﹈ ﹈ ﹈
 * D.push(21);        // 栈的现状为：  20  4 21
 *                                    1  3  5
 *                                    ﹈ ﹈ ﹈
 * D.popAtStack(0);   // 返回 20。栈的现状为：       4 21
 *                                             1  3  5
 *                                             ﹈ ﹈ ﹈
 * D.popAtStack(2);   // 返回 21。栈的现状为：       4
 *                                             1  3  5
 *                                             ﹈ ﹈ ﹈
 * D.pop()            // 返回 5。栈的现状为：        4
 *                                             1  3
 *                                             ﹈ ﹈
 * D.pop()            // 返回 4。栈的现状为：    1  3
 *                                            ﹈ ﹈
 * D.pop()            // 返回 3。栈的现状为：    1
 *                                            ﹈
 * D.pop()            // 返回 1。现在没有栈。
 * D.pop()            // 返回 -1。仍然没有栈。
 *
 *
 * 提示：
 *
 * 1 <= capacity <= 20000
 * 1 <= val <= 20000
 * 0 <= index <= 100000
 * 最多会对 push，pop，和 popAtStack 进行 200000 次调用。
 * 通过次数4,969提交次数15,313
 */
public class _1172_DinnerPlateStacks {

    /**
     * 方法：栈数组 + 优先队列
     * 
     * 试想一下，假如一开始连续执行了很多次 push 操作，就会得到一排整整齐齐的栈；
     * 然后再执行一些 popAtStack 操作，随机挑选 index，就会把这些栈弄得参差不齐。
     *
     * 注：如果中间一个栈空了，那么它会继续占据着这个位置，push会在左侧栈进行，pop会在右侧栈进行，popAt自己时返回-1即可。
     *
     * 这个时候再交替执行 push 和 popAtStack，
     * 「从左往右第一个没有满的栈」就没有什么规律可言了。
     * 
     * 如果把第一个未满栈填满，就要暴力寻找下一个未满栈了。如何优化？
     *
     * 格局打开：要快速知道第一个未满的栈，与其维护第一个未满栈，不如维护所有未满栈。
     *
     * 假设可以用一个数据结构来维护这些【未满栈】（的下标），看看需要哪些操作：
     *
     * 对于 push 来说，需要知道所有未满栈的下标中的最小值。
     *      如果入栈后，栈满了，那么这个栈就不能算作未满栈，此时这个最小下标就需要从数据结构中移除。
     * 对于 popAtStack 来说，如果操作的是一个满栈，操作后就得到了一个未满栈，那么就往数据结构中添加这个栈的下标。
     * 对于 pop 来说，它等价于 popAtStack(lastIndex)，其中 lastIndex 是最后一个非空栈的下标。
     * 
     * 所以我们需要一个支持添加元素、查询最小值和移除最小值的数据结构。最小堆，就你了。
     *
     * 此外还需要一个列表 stacks，它的每个元素都是一个栈。上面说的 lastIndex 就是 stacks 的长度减一。
     * 由于需要快速知道右侧第一个非空栈，那么 stacks 只维护【非空栈（中间空没关系）】就行，此时 stacks[n-1]就是目标栈
     * 因此，对于 popAtStack 操作，要及时移除掉 stacks 末尾的空栈
     *
     * 具体如何维护 stacks 呢？
     *
     * 如果 push 时最小堆为空，说明还没有栈或者当前stacks中所有栈都满了，那么就需要向 stacks 的末尾添加一个新的栈。
     *      此时除非 capacity>1，才能说这个新栈是个未满栈，就把这个新的未满栈的下标入堆。
     * 如果 popAtStack 操作的是最后一个栈，且操作后栈为空，那么就从 stacks 中移除最后一个空栈。
     * 如果移除后 stacks 的新的最后一个栈也是空的，就不断移除直到 stacks 为空或者最后一个栈不为空。
     * 
     * 细节：需要同步移除最小堆中的下标吗？
     * 其实不需要，因为堆内下标是从小到大的，如果在 push 时发现堆顶的下标大于等于 stacks 的长度，
     * 说明 popAtStack 操作导致最后几个栈空了，stacks中移除了这些栈，但是堆中没移除下标，
     * 此时 堆中的所有下标都会大于等于 stacks的长度，全都是无效的
     * 把整个堆清空即可。（这个技巧叫懒删除。）
     * 
     * 
     * 此外，如果 popAtStack 操作越界或者操作的栈为空，则返回 −1。
     *
     * 答疑
     * 问：懒删除是否会导致堆中有重复的下标？
     *
     * 答：不会有重复下标。假设重复下标是在 push 时插入的，说明此前所有栈都是满的，
     * 堆中残留的下标必然都大于等于 stacks 的长度，但这种情况下 push 会清空堆，不会导致重复下标。
     * 假设重复下标是在 popAtStack 时插入的，这只会发生在 stacks[index] 是满栈的情况下，
     * 而 push 保证在满栈时会去掉这个下标，所以也不会导致重复下标。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/dinner-plate-stacks/solution/yu-qi-wei-hu-di-yi-ge-wei-man-zhan-bu-ru-sphs/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class DinnerPlates {

        // 每个栈的容量
        private int capacity;

        // 维护所有非空栈（中间空没关系，末尾不空就行）
        private List<Deque<Integer>> stacks;

        // 所有未满栈在stacks中的下标，从小到大
        private PriorityQueue<Integer> notFullIdxQueue;

        public DinnerPlates(int capacity) {
            // 初始化
            this.capacity = capacity;
            this.notFullIdxQueue = new PriorityQueue<>();
            this.stacks = new ArrayList<>();
        }

        public void push(int val) {
            // 懒删除，此时堆中的下标全部无效
            if (!notFullIdxQueue.isEmpty() && notFullIdxQueue.peek() >= stacks.size()) {
                notFullIdxQueue.clear();
            }
            // 还没有栈，或者当前stacks中的栈都是满的
            if (notFullIdxQueue.isEmpty()) {
                // 新加一个栈
                Deque<Integer> stack = new ArrayDeque<>();
                // 加入当前元素
                stack.push(val);
                // stacks中加入当前栈
                stacks.add(stack);
                // 如果 capacity > 1，那么这个新的栈也是非满栈
                if (capacity > 1) {
                    // 将索引加入队列
                    notFullIdxQueue.offer(stacks.size() - 1);
                }
            } else {
                // 取出第一个非满栈
                Integer idx = notFullIdxQueue.peek();
                // 加入元素
                stacks.get(idx).push(val);
                // 如果满了，那么从 notFullIdxQueue 中移除，因为它就是第一个，所以直接 poll
                if (stacks.get(idx).size() == capacity) {
                    notFullIdxQueue.poll();
                }
            }
        }

        public int pop() {
            // stacks保证了末尾的栈不为空，直接调用 popAtStack(stacks.size() - 1) 即可
            return popAtStack(stacks.size() - 1);
        }

        public int popAtStack(int index) {
            // 下标不能越界，栈不能空
            if (index < 0 || index >= stacks.size() || stacks.get(index).isEmpty()) {
                return -1;
            }
            // 找到对应栈
            Deque<Integer> stack = stacks.get(index);
            // 是个满栈，移除栈顶后，就是非满栈，需要加入其索引到 notFullIdxQueue 中
            if (stack.size() == capacity) {
                notFullIdxQueue.offer(index);
            }
            // 移除栈顶元素
            int ans = stack.pop();
            // 如果移除的是最后一个栈的栈顶
            if (index == stacks.size() - 1) {
                // 如果移除后，这个栈空了，将其中stacks中移除；循环，直到最后一个栈非空
                // 懒删除，不需要移除这个栈在 notFullIdxQueue 中的下标
                while (!stacks.isEmpty() && stacks.get(stacks.size() - 1).isEmpty()) {
                    stacks.remove(stacks.size() - 1);
                }
            }
            // 返回
            return ans;
        }
    }
}
