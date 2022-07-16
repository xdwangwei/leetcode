package com.back;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2020/7/25 21:36
 * 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
 *
 * 示例:
 *
 * 输入:n = 4, k = 2
 * 输出:
 * [
 *   [2,4],
 *   [3,4],
 *   [2,3],
 *   [1,2],
 *   [1,3],
 *   [1,4],
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/combinations
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _77_Combinations {

    private List<List<Integer>> res = new ArrayList<>();

    /**
     * 回溯，
     *
     * 每次选择一个数字，接下来从剩下的里面的选，但是为了避免重复，比如第一次选择3，第二次选择1，和第一次选择1，第二次选择3，
     * 每次往后进行选择，比如本次选择3，下次就从3之后开始选择
     * @param n
     * @param k
     * @return
     */
    public List<List<Integer>> combine(int n, int k) {
        if (n < k) return res;
        // 选择列表 1,3,4,...,n
        // 第一次选择从1开始
        // 还需要添加k个数字
        backTrack(n, 1,  k, new ArrayList<>());
        return res;
    }

    /**
     * 回溯，选择列表是1,2,3,4,5,6
     * 为了避免重复，如 [1,3] [3,1]，每次的选择列表需要限制开始位置
     * @param n
     * @param start
     * @param k
     * @param list
     */
    private void backTrack(int n, int start, int k, ArrayList<Integer> list) {
        // 满足条件
        if (k == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        // 限制选择
        for (int i = start; i <= n; ++i) {
            // 做选择
            list.add(i);
            // 下一步
            backTrack(n, i + 1, k - 1, list);
            // 撤销选择
            list.remove(list.size() - 1);
        }
    }
}
