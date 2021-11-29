package com.queue;

import java.util.*;

/**
 * @author wangwei
 * 2020/7/26 21:29
 * 给定一个数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 *
 * 返回滑动窗口中的最大值。
 *
 *  
 *
 * 进阶：
 *
 * 你能在线性时间复杂度内解决此题吗？
 *
 *  
 *
 * 示例:
 *
 * 输入: nums = [1,3,-1,-3,5,3,6,7], 和 k = 3
 * 输出: [3,3,5,5,6,7]
 * 解释:
 *
 *   滑动窗口的位置                最大值
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 *  
 *
 * 提示：
 *
 * 1 <= nums.length <= 10^5
 * -10^4 <= nums[i] <= 10^4
 * 1 <= k <= nums.length
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/sliding-window-maximum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _239_SlidingWindowMaximum {

    /**
     * 单调队列
     * 单调栈和单调队列的区别：单调栈由于只能取到栈顶（back），相当于是一个临近的最大值，单调队列可以取到全局最大值（front）。
     * 这道题是在一个窗口中取最大值，这个值可能是窗口中任意一个值，所以就是范围内取值，选择单调队列
     *
     * 为什么用队列？
     * 滑动窗口 [i, j] 扩张就是 j++， 缩小就是 i++， 这就相当于 一个双端队列，在队头和队尾都要能操作
     *
     * 所以这个题选择【双端队列】来存储每个窗口范围内的元素。
     * 对于每一个窗口，要在O(1)时间复杂度取出最大值，所以这个队列应该具有单调性，这样直接访问队首或队尾元素就是最大值
     * 那么应该是递增还是递减队列呢？
     * 如果是递增队列，每一次的队尾就是最大值，比如对于 [1,3,2,1] k = 3，
     *      1 入队，3 入队，2 破坏递增性，但是满足3个元素了，所以 res[0] = 3
     *      3 出队，2 入队，下一个元素是1，队列中第一个1已经不属于这个窗口了，应该移除。队列中[2]
     *          3已经被移走了。第二个窗口的最大值肯定错误，而实际上应该也是3
     * 如果是递减队列，每一次的队首就是最大值，同样对于 [1,3,2,1]
     *      1入队，3破坏递减性，1出队，3入队，队列[3]，对于2，是第3个元素，第一个窗口凑齐，它的最大值是队首3
     *      然后2入队，队列[32]，下一个是1，1入队[321]，第二个窗口凑齐，最大值还是队首元素3，答案正确
     */

    /**
     * 最简单直接的方法是遍历每个滑动窗口，找到每个窗口的最大值。
     * 一共有 N - k + 1 个滑动窗口，每个有 k 个元素，于是算法的时间复杂度为 O(Nk)，表现较差。
     *
     * 如何优化时间复杂度呢？首先想到的是使用堆，因为在最大堆中 heap[0] 永远是最大的元素。
     * 在大小为 k 的堆中插入一个元素消耗 log(k) 时间，因此算法的时间复杂度为 O(Nlog(k))。
     *
     * 我们可以使用双向队列，该数据结构可以从两端以常数时间压入/弹出元素。
     * 存储双向队列的索引比存储元素更方便，因为两者都能在数组解析中使用。
     *
     * 步骤：
     * 处理前 k 个元素，初始化双向队列。
     *
     * 遍历整个数组。在每一步 :
     *
     * 清理双向队列 :
     *   - 只保留当前滑动窗口中范围内的元素的索引。
     *   - 移除比当前元素小的所有元素，它们不可能是最大的。
     * 将当前元素添加到双向队列中。
     * 将 deque[0] 添加到输出中。
     * 返回输出数组。
     *
     * 声明一个变量 deque<int>window，用于存储下标。这个变量有以下 特点:
     *
     * 1. 队列的最前端（也就是 window.front()）是此次遍历（当前窗口）的最大值的下标
     * 2. 双项队列中的所有值都要在当前窗口范围内
     * 3. 当我们遇到新的数时，将新的数和双项队列的末尾（也就是window.back()）比较，
     *      如果末尾比新数小，则把末尾扔掉，直到该队列的末尾比新数大或者队列为空的时候才停止
     *
     * 特点一只是方便我们获取每次窗口滑动一格之后的最大值，我们可以直接通过 window.front() 获得
     *
     * 通过特点二，可以保证队列里的元素是从头到尾降序的，由于队列里只有窗口内的数，所以他们其实就是窗口内第一大，第二大，第三大... 的数。
     *
     * 特点三就是根据题意设置的。但我们实际上只用比较现在的下标和 window.front() 就可以了，想想为什么？
     *
     * Answer： 因为只要窗口内第一大元素也就是这个 window.front() 在窗口内，那我们可以不用管第二大第三大元素在不在区间内了。因为答案一定是这个第一大元素。如果 window.front() 不在窗口内，则将其弹出，第二个大元素变成第一大元素，第三大元素变成第二大元素以此类推。
     *
     * 代码编写的过程还要时刻检查队列是否为空防止抛出异常。
     *
     * 作者：AdamWong
     * 链接：https://leetcode-cn.com/problems/sliding-window-maximum/solution/shuang-xiang-dui-lie-jie-jue-hua-dong-chuang-kou-z/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     *
     * @param k
     * @return
     */

    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        if (n * k == 0) return new int[0];
        // 创建双端队列
        Deque<Integer> deque = new LinkedList<>();
        // 保留每个窗口中的最大值
        int[] res = new int[n - k + 1];
        // 逐个入队列，注意保存的是下标
        for (int i = 0; i < nums.length; ++i) {
            // 当前元素所在窗口第一个元素下标应该为 i - k + 1
            // 1. 从队头清除不在当前窗口范围内的值
            while (!deque.isEmpty() && deque.getFirst() < i - k + 1)
                deque.removeFirst();
            // 2. 从队尾移除比当前元素小的，他们不可能是窗口最大值
            while (!deque.isEmpty() && nums[deque.getLast()] < nums[i])
                deque.removeLast();
            // 3. 当前元素从队尾入队
            deque.addLast(i);
            // 4. 从队头拿出当前窗口的最大值
            // 因为凑够三个元素窗口才满，所以要注意下标，防止越界
            if (i - k + 1 >= 0)
                res[i - k + 1] = nums[deque.getFirst()];
        }
        return res;
    }

    /**
     * 方法：优先队列
     * 思路与算法
     *
     * 对于「最大值」，我们可以想到一种非常合适的数据结构，那就是优先队列（堆），其中的大根堆可以帮助我们实时维护一系列元素中的最大值。
     *
     * 对于本题而言，初始时，我们将数组 nums 的前 kk个元素放入优先队列中。
     * 每当我们向右移动窗口时，我们就可以把一个新的元素放入优先队列中，此时堆顶的元素就是堆中所有元素的最大值。
     * 然而这个最大值可能并不在滑动窗口中，在这种情况下，这个值在数组 nums 中的位置出现在滑动窗口左边界的左侧。
     * 因此，当扩大窗口时(加入当前元素前)，先判断堆顶元素，如果是这个窗口以外的元素，那先poll掉，
     *
     * 我们不断地移除堆顶的元素，直到其确实出现在滑动窗口中。
     * 此时，堆顶元素就是滑动窗口中的最大值。
     * 为了方便判断堆顶元素与滑动窗口的位置关系，我们在优先队列中存储每个元素在原数组中的index。然后优先队列的比较规则设置为按照这个值在nums中对应值降序排列
     * PriorityQueue<Integer> queue = new PriorityQueue<>(((o1, o2) -> nums[o2] - nums[o1]));
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/sliding-window-maximum/solution/hua-dong-chuang-kou-zui-da-zhi-by-leetco-ki6m/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public int[] maxSlidingWindow2(int[] nums, int k) {
        if (nums == null || nums.length < k || k == 0) {
            return new int[0];
        }
        PriorityQueue<Integer> queue = new PriorityQueue<>(((o1, o2) -> nums[o2] - nums[o1]));
        int len = nums.length - k + 1;
        // 前 k 个元素入队，注意入队的是下标
        for (int i = 0; i < k; i++) {
            queue.offer(i);
        }
        int[] res = new int[len];
        // k个元素凑齐第一个窗口
        res[0] = nums[queue.peek()];
        // 之后的元素
        for (int i = k; i < nums.length; i++) {
            // 当前位置i对应的窗口的起始位置应该是 i -k + 1，队列头元素是最大值，但如果它不在当前窗口中，就要移除
            while (!queue.isEmpty() && queue.peek() < i - k + 1) {
                queue.poll();
            }
            // 当前元素入队列
            queue.offer(i);
            // 当前元素对应的窗口
            res[i - k + 1] = nums[queue.peek()];
        }
        return res;
    }
}
