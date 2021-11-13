package com.order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author wangwei
 * 2021/11/11 16:20
 *
 * 给你一个正整数n ，请你找出符合条件的最小整数，其由重新排列 n中存在的每位数字组成，并且其值大于 n 。如果不存在这样的正整数，则返回 -1 。
 *
 * 注意 ，返回的整数应当是一个 32 位整数 ，如果存在满足题意的答案，但不是 32 位整数 ，同样返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 12
 * 输出：21
 * 示例 2：
 *
 * 输入：n = 21
 * 输出：-1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/next-greater-element-iii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _556_NextGreaterElement3 {

    /**
     * 回溯法找出这些数字的全部排列，恢复成数字，筛选出未超过int范围且比n大的，选择其中最小那个
     *
     * 超出时间限制，pass
     * @param n
     * @return
     */
    public int nextGreaterElement(int n) {
        if (n < 12) {
            return -1;
        }
        // 拆出每一个数字
        ArrayList<Integer> nums = new ArrayList<>();
        int m = n;
        while (m > 0) {
            nums.add(m % 10);
            m /= 10;
        }
        // 回溯法找出这些数字的全排列
        List<LinkedList<Integer>> res = new LinkedList<>();
        boolean[] visited = new boolean[nums.size()];
        backTrack(res, nums, visited, nums.size(), new LinkedList<>());
        // 将这些全排列转成数字，并选取出最小的那个
        PriorityQueue<Integer> integers = new PriorityQueue<>();
        // 每一个排列转数字
        for (LinkedList<Integer> numList : res) {
            // 可能超出int值，所以选择long
            long temp = 0;
            int length = numList.size();
            while (length-- > 0) {
                temp *= 10;
                temp += numList.removeFirst();
            }
            // 筛选出未超出int范围，并且比n大的，入队列
            if (temp > n && temp <= Integer.MAX_VALUE) {
                integers.offer((int) temp);
            }
        }
        // 返回队列中最小的那个
        return integers.isEmpty() ? -1 : integers.poll();
    }

    /**
     * 回溯法找全排列
     * @param res
     * @param nums
     * @param visited 放重复，对于 [12]这种数字，避免将[11]和[22]也认为是其中一个排列
     * @param target
     * @param path
     */
    private void backTrack(List<LinkedList<Integer>> res, List<Integer> nums, boolean[] visited, int target, LinkedList<Integer> path) {
        if (target == 0) {
            res.add(new LinkedList<>(path));
            return;
        }
        for (int i = 0; i < nums.size(); ++i) {
            // 避免同一个数字多次选取
            if (visited[i]) {
                continue;
            }
            visited[i] = true;
            path.addLast(nums.get(i));
            backTrack(res, nums, visited, target - 1, path);
            path.removeLast();
            visited[i] = false;
        }
    }

    /**
     * 方法 2：线性解法 [Accepted]
     *
     * 这种方法中，我们同样将给定数字 n 当做字符串数组 a，首先我们观察到任意降序的序列，不会有更大的排列出现。
     *
     * 比方说，下面数列就没有下一排列：[9, 5, 4, 3, 1]
     *
     * 首先：
     *
     * 从右往左找到第一对破坏降序规则的点a[i-1]和a[i] 满足 a[i−1]<a[i]。那么a[i]及之后部分是一个降序序列，这部分不存在更大的排列
     *
     *          比如 [1 ,3, (5), (7), 6 ,4 ,2, 1]   那么 5，7破坏降序
     *
     * 我们要找的是比n大的最小的一个排列，那么前半部分不用动，只需要把 [i-1]位置换成一个更大的数，就能保证新的这个数字 > n
     *
     * 那么这个比a[i-1]大的数字去哪里找，当前是 后半部分，因为如果你和前半部分里面的数字交换，那肯定导致这个新的数字更小了
     *
     * 然后：
     *
     * 找到 a[i]及之后部分中最小的那个比a[i-1]大的数字，并且和a[i-1]交换，因为后半段是个降序排列，所以要从后往前找，其实也可以二分搜素
     *
     *          所以 5 和 后面的 6交换 --> [1, 3, (6), 7, (5), 4, 2, 1]
     *
     * 接着：
     *      交换完了之后，这个数字肯定比n大了，但是它还不是满足要求的最小的那个，因为后半段的降序如果反过来变为升序，那才是最小的
     *
     *       [1, 3, (6), 7, (5), 4, 2, 1]  --> [1, 3, (6), 1, 2, 4, 5, 7]
     *
     * 下面的动画更形象地解释了这一过程：
     *
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/next-greater-element-iii/solution/xia-yi-ge-geng-da-yuan-su-iii-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @return
     */
    public int nextGreaterElement2(int n) {
        // 先转成字符数组
        char[] a = (n + "").toCharArray();
        int len = a.length;
        // 1. 从后往前找到破坏降序的位置 a[i]
        int i = len - 2;
        // 数字字符可以直接比大小，符合数字比较规则
        while (i >= 0 && a[i] >= a[i + 1]) {
            i--;
        }
        // i < 0 说明整个串就是一个降序排列，不存在更大的排列
        if (i < 0) {
            return -1;
        }
        // 2. 找到后半段中大于a[i]的最小的a[j]，并进行交换
        // 这里不用判断j >= 0，因为从上面走下来， j 最多 = i + 1
        int j = len - 1;
        while (a[j] <= a[i]) {
            j--;
        }
        swap(a, i, j);
        // 3. 反转 从a [i+1] 到 a[len-1]
        reverse(a, i + 1, len - 1);
        // 4. 将字符数组转为整数，需要注意超过int范围
        try {
            return Integer.parseInt(new String(a));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 反转字符数组a中[i, j]部分
     * @param a
     * @param start
     * @param end
     */
    private void reverse(char[] a, int start, int end) {
        while (start < end) {
            swap(a, start, end);
            start++;
            end--;
        }
    }

    /**
     * 交换字符数字a中i和j位置字符
     * @param a
     * @param i
     * @param j
     */
    private void swap(char[] a, int i, int j) {
        if (i == j) {
            return;
        }
        char temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        System.out.println(new _556_NextGreaterElement3().nextGreaterElement(12));
    }
}
