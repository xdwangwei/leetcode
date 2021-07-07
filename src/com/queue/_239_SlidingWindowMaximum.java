package com.queue;

import java.util.Deque;
import java.util.LinkedList;

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
}
