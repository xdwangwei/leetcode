package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/10/15 9:29
 * @description: _1441_UseStackToOperateArray
 *
 * 1441. 用栈操作构建数组
 * 给你一个数组 target 和一个整数 n。每次迭代，需要从  list = { 1 , 2 , 3 ..., n } 中依次读取一个数字。
 *
 * 请使用下述操作来构建目标数组 target ：
 *
 * "Push"：从 list 中读取一个新元素， 并将其推入数组中。
 * "Pop"：删除数组中的最后一个元素。
 * 如果目标数组构建完成，就停止读取更多元素。
 * 题目数据保证目标数组严格递增，并且只包含 1 到 n 之间的数字。
 *
 * 请返回构建目标数组所用的操作序列。如果存在多个可行方案，返回任一即可。
 *
 *
 *
 * 示例 1：
 *
 * 输入：target = [1,3], n = 3
 * 输出：["Push","Push","Pop","Push"]
 * 解释：
 * 读取 1 并自动推入数组 -> [1]
 * 读取 2 并自动推入数组，然后删除它 -> [1]
 * 读取 3 并自动推入数组 -> [1,3]
 * 示例 2：
 *
 * 输入：target = [1,2,3], n = 3
 * 输出：["Push","Push","Push"]
 * 示例 3：
 *
 * 输入：target = [1,2], n = 4
 * 输出：["Push","Push"]
 * 解释：只需要读取前 2 个数字就可以停止。
 *
 *
 * 提示：
 *
 * 1 <= target.length <= 100
 * 1 <= n <= 100
 * 1 <= target[i] <= n
 * target 严格递增
 */
public class _1441_UseStackToOperateArray {

    /**
     * 根据题意进行模拟即可：
     * 因为是从  list = { 1 , 2 , 3 ..., n } 中【依次】读取一个数字，所以每次将当前处理到的 j 先压入栈中（往答案添加一个 Push），
     * 然后判断当前处理到的 j 是否最新的栈顶元素 target[i] 是否相同，
     * 若不相同则丢弃当前元素（往答案添加一个 Pop），若存在则将指针 j 后移，直到构建出目标答案。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/build-an-array-with-stack-operations/solution/by-ac_oier-q37s/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param target
     * @param n
     * @return
     */
    public List<String> buildArray(int[] target, int n) {
        List<String> ans = new ArrayList<>();
        int m = target.length;
        // j 是当前迭代到的数字，初始值是1，每次都要自增；target[i]是当前栈顶元素，也就是顺序处理的target中元素，只有j匹配栈顶时才推进下一个
        for (int i = 0, j = 1; i < m && j <= n; j++) {
            // 因为需要依次处理，没法跳过，所以先入栈
            ans.add("Push");
            // 发现和栈顶不匹配，就跳过它，相当于target[i]=4,当前迭代到j=1，那只能把123顺序处理完
            if (target[i] != j) {
                ans.add("Pop");
            }
            // 若正好匹配，则可处理下一个栈顶元素
            else {
                i++;
            }
        }
        return ans;
    }
}
