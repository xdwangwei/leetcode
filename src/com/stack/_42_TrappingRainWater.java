package com.stack;

import java.util.Stack;

/**
 * @author wangwei
 * 2020/4/17 14:44
 * <p>
 * 给定n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * <p>
 * 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。感谢 Marcos 贡献此图。
 * <p>
 * 示例:
 * <p>
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/trapping-rain-water
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _42_TrappingRainWater {


    /**
     * 单调栈
     * 至少三个柱子才能接水
     * 我们可以不用像方法 2 那样存储最大高度，而是用栈来跟踪可能储水的最长的条形块。使用栈就可以在一次遍历内完成计算。
     * 维护一个单调递减栈，对于每个元素 h[i]， 判断 它 和栈顶元素top 的大小，如果比它大，那么它就是top的右边界，而栈里下一个元素就是top的左边界，然后top就可以接水了
     * 否则就入栈(不破坏单调递减性)
     * 这样，栈里面每一个元素的左边界都是知道的(它的前一个入栈元素)，每次遇到破坏递减性的元素，实际上就是为栈顶元素找到了右边界
     * <p>
     * 我们在遍历数组时维护一个栈。如果当前的条形块小于或等于栈顶的条形块，我们将条形块的索引入栈，
     * 意思是当前的条形块被栈中的前一个条形块界定（左边界）。
     * 如果我们发现一个条形块长于栈顶（找到了右边界），我们可以确定栈顶的条形块被当前条形块和栈的前一个条形块界定
     * 因此我们可以弹出栈顶元素并且累加答案到 ans。
     * <p>
     * 需要注意到的是，这种做法相当于 【按层】 累积的，因为每个柱子高度不一样，它的左右边界也不一样，它头顶能铺开的水的宽度也不一样
     * <p>
     * 使用栈来存储条形块的索引下标。
     * 遍历数组：
     * 当栈非空且 height[current]>height[st.top()]
     * 意味着栈中元素可以被弹出。弹出栈顶元素 top。
     * 计算当前元素和栈顶元素的距离，准备进行填充操作
     * distance = current -st.top() −1
     * 找出界定高度
     * bounded_height = min(height[current], height[st.top()]) - height[top]
     * 往答案中累加积水量 ans += distance * bounded_height
     * 将当前索引下标入栈
     * 将 current 移动到下个位置
     * <p>
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/trapping-rain-water/solution/jie-yu-shui-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param height
     */

    public int trap5(int[] height) {
        // 至少要三个柱子
        if (height == null || height.length < 3)
            return 0;
        int res = 0, len = height.length;
        // 单调递减栈，表明此柱子的有边界未确定，一旦破坏递减性，则相当于右边界确定，栈里面存下标
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < len; i++) {
            // 破会递减性，栈顶柱子的有边界确定，左边界是它的前一个元素，也就是弹出它之后的栈顶
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                // 栈顶那个柱子，它出栈后 stack.peek() 就是它的左边界
                int top = stack.pop();
                // 如果栈里只有一个柱子，有了右边界，不存在左边界这个柱子无法积水
                if (stack.isEmpty()) break;
                // 确定它头顶能存储的水的高度，
                int h = Math.min(height[stack.peek()], height[i]) - height[top];
                // 头顶能存储的水的宽度是左边边界确定
                int w = i - stack.peek() - 1;
                // 累加水
                res += h * w;
            }
            // 不破坏递减性，有边界无法确定
            stack.push(i);
        }

        return res;
    }

    /**
     * 暴力法，求在每个柱子上能积累的雨水高度，再求和
     * 首先，第一个柱子没有左边界，最后一个柱子没有右边界，所以这两个柱子不用考虑
     * 对于中间的某个柱子，只要它两边都有比它高的，那么它就能向上积累水，因为一定能被左右包裹住，
     * 它能积累多高呢？min(它左变最高的柱子，它右边最高的柱子) - 自己的高度，宽度是1
     *
     * @param height
     * @return
     */
    public int trap(int[] height) {
        // 至少要三个柱子
        if (height == null || height.length < 3)
            return 0;
        int res = 0;
        // 第一个和最后一个柱子不能积水
        for (int i = 1; i < height.length - 1; i++) {
            int leftMax = 0, rightMax = 0;
            // 为避免它最高，向左向后扫描都要从自己开始，这样之后做差就不会出现负数
            // 找到它左边最高的柱子
            for (int j = i; j >= 0; j--) {
                leftMax = Math.max(leftMax, height[j]);
            }
            // 它右边最高的柱子
            for (int j = i; j < height.length; j++) {
                rightMax = Math.max(rightMax, height[j]);
            }
            // 它能积累的高度
            res += Math.min(leftMax, rightMax) - height[i];
        }
        return res;
    }

    /**
     * 动态编程
     * 找到 每个元素 左边最大值 和 右边最大值， 那么 它能存储的水的高度就是 二者之间的min - 自己的height
     * 在暴力方法中，我们仅仅为了找到最大值每次都要向左和向右扫描一次。
     * 但是我们可以提前存储这个值。因此，可以通过动态编程解决。三次 for 循环
     * <p>
     * 找到数组中从下标 i 到最左端最高的条形块高度 left_max。
     * 找到数组中从下标 i 到最右端最高的条形块高度 right_max。
     * 扫描数组 height 并更新答案：
     * 累加 min(max_left[i],max_right[i])−height[i] 到 ansans 上
     *
     * 空间复杂度 O(n)
     * <p>
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/trapping-rain-water/solution/jie-yu-shui-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param height
     * @return
     */
    public int trap2(int[] height) {
        // 至少要三个柱子
        if (height == null || height.length < 3)
            return 0;
        int res = 0, len = height.length;
        // 保存每个柱子的左边柱子最高高度
        int[] leftMax = new int[len];
        // 保存每个柱子的右边柱子最高高度
        int[] rightMax = new int[len];
        // 第一个和最后一个柱子不能积水,作为左右边界比较的初始值
        leftMax[0] = height[0];
        rightMax[len - 1] = height[len - 1];
        // 从左到右，更新每个柱子左边的最高高度
        for (int i = 1; i < len; i++)
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        // 从右到左，更新每个柱子右边的最高高度,注意是它右边柱子先解决
        for (int i = len - 2; i >= 0; i--)
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        // 从左到右，累加每个柱子能积累的水的高度
        for (int i = 1; i < len - 1; i++)
            res += Math.min(leftMax[i], rightMax[i]) - height[i];

        return res;
    }

    /**
     * 双指针法
     * 能否将2的空间复杂度优化到 O(1)
     * 其实在 2 的基础上，我们能够看出。找到当前柱子的左边最高柱子，用到了它之前柱子的左变的柱子的最高柱子，右边也一样
     * 然后找完了全部的柱子的左右边界再逐个求和，
     * 我们可以考虑，既然当前柱子的边界是依靠之前的柱子的算出来的，之后的柱子的边界又是依赖当前柱子算出来的，这个值用完就不用了
     * 那么既然每个柱子的边界是依靠之前柱子的边界算出来的，那么我们是不是可以不用用一个数组去保存所有的柱子的边界
     * 而只是时时更新上一个值，得到当前边界，把这个柱子积累的雨水加上去，再用它去跟新下一个柱子的边界并求雨水，
     * <p>
     * 我们任然使用 leftMax 和 rightMax 作为左右边界的参考，但不是求出每个柱子的边界，并且从两边柱子向中间柱子推进，累加每个柱子的雨水
     * 当 height[i] < height[j] 而 leftMax < height[i] 时，其实 这个柱子的 左边界就确定了，就是leftMax,
     * 为什么呢，因为 rightMax 只会比 height[j]更大，因为我们求最大边界始终取更大的那个值
     * <p>
     * 这个时候，这个柱子求完了，我们更新一下 leftMax = max(leftMax, height[i]) 因为当前柱子也可能是下个柱子的左边界
     *
     * 【参考】
     * 双指针法真的妙，那么如何理解双指针法呢？听我来给你捋一捋。（捋的过程和原解中的C++在细节方面的处理是有出入的）
     *
     * 我们先明确几个变量的意思：
     *
     * left_max：左边的最大值，它是从左往右遍历找到的
     * right_max：右边的最大值，它是从右往左遍历找到的
     * left：从左往右处理的当前下标
     * right：从右往左处理的当前下标
     *
     * 定理一：在某个位置i处，它能存的水，取决于它左右两边的最大值中较小的一个。
     *
     * 定理二：当我们从左往右处理到left下标时，左边的最大值left_max对它而言是可信的，但right_max对它而言是不可信的。
     * （见下图，由于中间状况未知，对于left下标而言，right_max未必就是它右边最大的值）
     *
     * 定理三：当我们从右往左处理到right下标时，右边的最大值right_max对它而言是可信的，但left_max对它而言是不可信的。
     *
     *                                    right_max
     *  left_max                             __
     *    __                                |  |
     *   |  |__   __??????????????????????  |  |
     * __|     |__|                       __|  |__
     *         left                      right
     *
     *  也就是，leftMax对于当前遍历到的left柱子是可靠的，它左边最大值一定是left_max，右边最大值“大于等于”right_max，因为我们总选取更大的作为边界
     * 【这时候，如果left_max<right_max成立，那么它就知道自己能存多少水了。无论右边将来会不会出现更大的right_max，都不影响这个结果。】
     * 所以当left_max<right_max时，我们就希望去处理left下标，反之，我们希望去处理right下标，每次都像可靠的一方去调整遍历方向
     *
     * @param height
     * @return
     */
    public int trap3(int[] height) {
        // 至少要三个柱子
        if (height == null || height.length < 3)
            return 0;

        int res = 0, left = 0, right = height.length - 1;

        int leftMax = 0, rightMax = 0;
        // 退出时(left > right)，说明柱子全部遍历完了
        // 注意这个等号，因为我们的写法是，每次要么从左往右一步，要么从右往左一步，【不存在同时跨步】，
        // 也就是说必须考虑left == right，不然就会漏掉这个柱子
        while (left <= right) {
            // 此时leftMax对于left就是可靠的，也就是它的左边界确定了，因为rightMax只会更大
            if (leftMax < rightMax) {
                // 这里先为后面的柱子更新leftMax，是因为左边界本来就是在他左边去找的，和本身是没有关系的，
                // 和自己再取个最大值，如果之前的柱子都比自己低，那么更新完也就和自己一样，既然如此说明这个柱子积累不了水，res += 0
                // 也可以先更新res，但这就要考虑leftMax比自己低时，leftMax - height[left] < 0
                // 代码就需要写成 res += Math.max(0, leftMax - height[left]); leftMax = Math.max(leftMax, height[left]);
                // 所以这样写就解决了这个问题
                // 就算leftMax比自己小，更新后就是自己，不会改变res，也不会出现负数
                leftMax = Math.max(leftMax, height[left]);
                res += leftMax - height[left];
                // 左变可靠就继续从左往右
                left++;
                // 右边可靠
            } else {
                rightMax = Math.max(rightMax, height[right]);
                res += rightMax - height[right];
                // 右边可靠就继续从右向左
                right--;
            }
        }
        return res;
    }
    // 另一种写法
    // l_max 是 height[0..left] 中最高柱子的高度，r_max 是 height[right..end] 的最高柱子的高度。
    public int trap4(int[] height) {
        // 至少要三个柱子
        if (height == null || height.length < 3)
            return 0;
        int n = height.length;
        int left = 0, right = n - 1;
        int ans = 0;
        // l_max 是 height[0..left] 中最高柱子的高度
        int l_max = height[0];
        // r_max 是 height[right..end] 的最高柱子的高度
        int r_max = height[n - 1];

        while (left <= right) {
            l_max = Math.max(l_max, height[left]);
            r_max = Math.max(r_max, height[right]);

            // ans += min(l_max, r_max) - height[i]
            if (l_max < r_max) {
                ans += l_max - height[left];
                left++;
            } else {
                ans += r_max - height[right];
                right--;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        _42_TrappingRainWater obj = new _42_TrappingRainWater();
        System.out.println(obj.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }
}
