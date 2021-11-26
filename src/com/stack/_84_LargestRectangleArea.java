package com.stack;

import java.util.Stack;

/**
 * @author wangwei
 * 2020/4/15 18:21
 * <p>
 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
 * <p>
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 * 以上是柱状图的示例，其中每个柱子的宽度为 1，给定的高度为 [2,1,5,6,2,3]。
 * <p>
 * 图中阴影部分为所能勾勒出的最大矩形面积，其面积为 10 个单位。
 * 示例:
 * <p>
 * 输入: [2,1,5,6,2,3]
 * 输出: 10
 * <p>
 * https://leetcode-cn.com/problems/largest-rectangle-in-histogram/
 */
public class _84_LargestRectangleArea {

    /**
     * 方法一：暴力解法，
     * 找到从每个矩形开始的所有矩形，求的面积，取最大值
     * [i,j]宽的矩形的面积由范围中最低的那个矩形决定，可以再加一轮循环k，k从i到j，就是为了找到这个范围中的最低矩形
     * 我们也可以优化
     * i从0到len-1，每次j从i开始到len-1，假入[i,j]中最低矩阵为minHeight
     * 那么[i，j+1]中，最低矩形为Math.min(minHeight, height[j+1])，
     * 为了保证单个矩阵不被遗漏(比如它两边都比它小)，每次j从i开始，而不是i+1
     *
     * @param heights
     * @return
     */
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) return 0;
        if (heights.length == 1) return heights[0];
        int res = 0, len = heights.length;
        // 每个位置都作为起始位置
        for (int i = 0; i < len; i++) {
            // 从自己开始
            int minHeight = heights[i];
            // 注意起始位置
            for (int j = i; j < len; j++) {
                // 找到从i到j的连续矩形中最低的那个，minHeight保留了上次的比较结果
                minHeight = Math.min(minHeight, heights[j]);
                // 当j==i时，宽度为1，也就是当前矩形，
                int width = j - i + 1;
                res = Math.max(res, minHeight * width);
            }
        }
        return res;
    }

    /**
     * 中心扩展法
     * 把每个矩形都作为最低的矩形，由它向两边扩展，找到【最】左边和【最【右边比它高的矩形
     * 就是它能扩展的最大范围
     *
     * @param heights
     * @return
     */
    public int largestRectangleArea2(int[] heights) {
        if (heights == null || heights.length == 0) return 0;
        if (heights.length == 1) return heights[0];
        int res = 0, len = heights.length;
        for (int i = 0; i < len; i++) {
            int left = i, right = i, curHeight = heights[i];
            // 以自己为中心，找到最左边比它高的矩形
            while (left > 0 && heights[left - 1] >= curHeight)
                left--;
            // 以它为中心，找到最右边比它高的那个矩形
            while (right < len - 1 && heights[right + 1] >= curHeight)
                right++;
            // 以它为最低矩形延申的最大宽度是 right - left - 1
            res = Math.max(res, curHeight * (right - left + 1));
        }
        return res;
    }

    /**
     * 二分法，基于上面的优化
     * 首先找到最低的矩形，然后向左向右找到最后一个大于它的矩形，这是由它确定的最大的矩形面积
     * 由于它切断了整个矩形，所以剩余情况一定在于它左半边的矩形和右半边的矩形中
     * <p>
     * 在矩阵高低交错分布时，平均时间复杂度是O(nlogn)
     * 但当所有矩阵都是从低到高一次排过去时，和暴力法没多大区别
     *
     * @param heights
     * @return
     */
    public int largestRectangleArea3(int[] heights) {
        if (heights == null || heights.length == 0) return 0;
        if (heights.length == 1) return heights[0];
        return subLargestArea(0, heights.length - 1, heights);
    }

    private int subLargestArea(int start, int end, int[] heights) {
        // 退出条件
        if (end < start)
            return 0;
        int minIndex = start;
        // 找到从start到end的矩形中最低的那个矩形的位置
        for (int i = start; i <= end; i++) {
            if (heights[i] < heights[minIndex])
                minIndex = i;
        }
        // 返回以它为中心的矩形面积，它左变部分中的最大矩形、它右边部分中的最大矩形中的最大值
        return Math.max(heights[minIndex] * (end - start + 1),
                Math.max(subLargestArea(start, minIndex - 1, heights),
                        subLargestArea(minIndex + 1, end, heights)));

    }

    /**
     * 方法四：栈
     * 单调栈
     * 但不能是单调递减栈，比如 栈里 5 4 3 好，遇到 一个 7， 对于 栈顶3来说，它的左边界最多扩展到栈底5的位置，但是它的有边界没法确定，因为下一个元素可能比7还大
     *
     * 单调递增栈， 比如 栈里 123，遇到一个2 ，那么 栈顶3 的左右边界都就知道了，
     * 也就是说栈里元素的左边界都知道了，每次遇到一个元素破坏单调递增性的时候，就相当于栈顶元素的有边界确定了，就可以统计了
     *
     * 但是这样可能有个问题，就是 对于1234结束的数组，没有一个更小的值来破坏递增性，这样1234都没有出栈，这几个的右边界都没有确定，虽然这样也可以解决
     * 就是按照上面的逻辑处理完后，判断栈，如果非空，那么继续进行上面的逻辑，先出栈4，那么栈顶3的左右边界都就知道了，同样出栈3，2，然后出栈1，【栈空】了，
     * 那么它的左右边界是什么呢，栈空说明它左边的都比它大(单调递增栈，破坏递增性才出栈，所以说栈里都比它大，所以被弹空了)，那么它的宽度就是它所处的位置(因为之前的都比它大啊，没有边界)。
     *
     * 为了简单起见，首先加一个 右哨兵0，它比所有元素都小，最后一个处理它，能保证栈里元素都会出栈，
     * 再增加一个 左哨兵0，这样它肯定是第一个入栈的，并且没有比它小的，也就是没人能让它弹栈，栈永远不空，我们的处理逻辑就同一个
     * https://leetcode-cn.com/problems/largest-rectangle-in-histogram/solution/bao-li-jie-fa-zhan-by-liweiwei1419/
     * 从第一个矩形考虑，如果它右边那个矩形比它高，那么以它为高的最大矩形的面积是不能确定的，因为可能继续向右延申
     * 但只要是遇到了当前柱形的高度比它上一个柱形的高度严格小的时候，一定可以确定它之前的某些柱形的最大宽度，并且确定的柱形宽度的顺序是从右边向左边。
     * 因为当前矩形低于它，所以以它为高的最大矩形的右边界就是当前位置，至于左边界，因为我们从一开始考虑的就是递增，所以它左变的那个矩形就比它低，也就是左边界
     * <p>
     * 这个现象告诉我们，在遍历的时候需要记录的信息就是遍历到的柱形的下标，它一左一右的两个柱形的下标的差就是以它为高的面积最大的矩形对应的最大宽度。
     * <p>
     * 我们在缓存数据的时候，是从左向右缓存的，我们计算出一个结果的顺序是从右向左的，因为只有先确定了有边界，才能保证它不会再向右延申了，
     * 并且计算完成以后我们就不再需要了，因为我们会实时更新保存res的值，
     * 符合后进先出的特点。因此，我们需要的这个作为缓存的数据结构就是栈。
     * <p>
     * 有两个特殊情况：
     * 对于第一个矩形，它没有上一个，所以我们给栈的初始位置赋值-1，这样我们不用考虑栈空的特殊情况
     * 对于最后一个矩形，它后面没有了，为了操作统一，因为以它为高的最大矩形的右边界就是它右面那个下表，我们给所有矩形最后添加一个高为-1的矩形
     * <p>
     * 相当于加了两个哨兵，
     * 但是由于我们存的是下标，所以为了在合法不越界，我们把-1改为0，只要满足比所有矩形高度都小即可
     *
     * @param heights
     * @return
     */
    public int largestRectangleArea4(int[] heights) {
        if (heights == null || heights.length == 0) return 0;
        if (heights.length == 1) return heights[0];
        int res = 0, len = heights.length;
        Stack<Integer> stack = new Stack<>();
        // 拷贝一个副本，多两个位置
        int[] newHeights = new int[len + 2];
        System.arraycopy(heights, 0, newHeights, 1, len);
        // 给初始加一个高为 0 的矩形
        newHeights[0] = 0;
        // 给最后加一个高为 0 的矩形
        len += 2;
        newHeights[len - 1] = 0;
        heights = newHeights;

        // 先放入哨兵，在循环里就不用做栈的非空判断
        stack.push(0);
        // 真正的数组元素从1号位置开始
        for (int i = 1; i < len; ++i) {
            // 一旦破坏递增关系，前面的矩形就有可能算出最大面积
            // 注意可能有多个矩形能得到以它为高的最大矩形的面积
            // 比如当前位置矩形高度小于前一个，然后前一个矩形 为高的 最大矩形边界和面积确定
            // 我们更新了res后，它就没用了，所以它会出栈，它一旦出栈，那他之前的那个矩形也有可能能得到答案，只要当前元素的高度小于它，就破坏递增，它就能计算
            // 所以此处要用while，值与为什么不用判断栈空，首元素是0，就算全出栈，最终也会高于第一个哨兵，就会满足递增所以while能终止
            while (heights[i] < heights[stack.peek()]){
                // 计算它前面那个矩形对应的最大面积，pop得到索引，得到高度
                // 右边界是当前位置，左边界是它之前的位置，也就是pop后的栈顶
                // 顺便，计算完它，更新完res，它就没用了，所以去除它，pop是合适的
                int curHeight = heights[stack.pop()];
                // 至于这里的加1还是减1，画一下就知道了
                // 这里其实跟加入哨兵改变了数组长度以及数组从1开始遍历也有关系
                // 但前后加哨兵确实派出了特殊情况，使得操作统一
                int curWidth = i - stack.peek() - 1;
                res = Math.max(res, curWidth * curHeight);
            }
            // 递增，前面都无法确定边界，无法求面积
            stack.push(i);
        }
        return res;
    }


    public static void main(String[] args) {
        _84_LargestRectangleArea obj = new _84_LargestRectangleArea();
        // int res = obj.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3});
        // System.out.println(res);
        // int res = obj.largestRectangleArea2(new int[]{2, 1, 5, 6, 2, 3});
        // System.out.println(res);
        // int res = obj.largestRectangleArea3(new int[]{2, 1, 5, 6, 2, 3});
        // System.out.println(res);
        int res = obj.largestRectangleArea4(new int[]{2, 1, 5, 6, 2, 3});
        System.out.println(res);
    }
}
