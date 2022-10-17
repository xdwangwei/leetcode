package com.daily;

/**
 * @author wangwei
 * @date 2022/10/17 9:46
 * @description: _904_FruitIntoBaskets
 *
 * 904. 水果成篮
 * 你正在探访一家农场，农场从左到右种植了一排果树。这些树用一个整数数组 fruits 表示，其中 fruits[i] 是第 i 棵树上的水果 种类 。
 *
 * 你想要尽可能多地收集水果。然而，农场的主人设定了一些严格的规矩，你必须按照要求采摘水果：
 *
 * 你只有 两个 篮子，并且每个篮子只能装 单一类型 的水果。每个篮子能够装的水果总量没有限制。
 * 你可以选择任意一棵树开始采摘，你必须从 每棵 树（包括开始采摘的树）上 恰好摘一个水果 。采摘的水果应当符合篮子中的水果类型。每采摘一次，你将会向右移动到下一棵树，并继续采摘。
 * 一旦你走到某棵树前，但水果不符合篮子的水果类型，那么就必须停止采摘。
 * 给你一个整数数组 fruits ，返回你可以收集的水果的 最大 数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：fruits = [1,2,1]
 * 输出：3
 * 解释：可以采摘全部 3 棵树。
 * 示例 2：
 *
 * 输入：fruits = [0,1,2,2]
 * 输出：3
 * 解释：可以采摘 [1,2,2] 这三棵树。
 * 如果从第一棵树开始采摘，则只能采摘 [0,1] 这两棵树。
 * 示例 3：
 *
 * 输入：fruits = [1,2,3,2,2]
 * 输出：4
 * 解释：可以采摘 [2,3,2,2] 这四棵树。
 * 如果从第一棵树开始采摘，则只能采摘 [1,2] 这两棵树。
 * 示例 4：
 *
 * 输入：fruits = [3,3,3,1,2,1,1,2,3,3,4]
 * 输出：5
 * 解释：可以采摘 [1,2,1,1,2] 这五棵树。
 *
 *
 * 提示：
 *
 * 1 <= fruits.length <= 105
 * 0 <= fruits[i] < fruits.length
 */
public class _904_FruitIntoBaskets {

    /**
     * 题目不好明白，就是始终保证你遇到的果树的种类不在你的篮子种类之外
     *
     * 等价于，在fruits中寻找只包含两种数字的最长连续子数组的长度
     *
     * 比如 1 2 3 2 2
     * 你先选1，再选2，到第3个树时，出现了篮子之外的种类，
     * 先选2，再选3，再选2，再选2，始终只有2、3两种数字，就符合要求‘
     *
     *
     * 因此，滑动窗口，始终保持窗口内只有两种元素，type表示窗口内元素种类，cnt记录窗口内每个数值的次数
     * 每当某个数字的次数从0变为1时，表示加如1种新的元素，type++
     * 每当某个数字的次数从1变为0时，表示移除1种新的元素，type++
     * 每当type > 2时需要缩小窗口来保证type不超过2
     * 每次收缩完窗口后，更新结果
     * @param fruits
     * @return
     */

    public int totalFruit(int[] fruits) {
        int n = fruits.length;
        int left = 0, right = 0, ans = 0;
        // 窗口内每种数字的次数
        int[] cnt = new int[n];
        // 窗口内数字的种类书
        int type = 0;
        // 滑动窗口，只包含两种数字的最长连续子数组的长度
        while (right < n) {
            // 加入元素，有边界扩展
            int cur = fruits[right++];
            // 加入一种新元素，type++
            if (++cnt[cur] == 1) {
                type++;
            }
            // 收缩窗口，保证type不超过2
            while (type > 2) {
                // 移除之前判断，如果del次数为1，那么移除之后，相当于少了一种元素，type--
                int del = fruits[left++];
                if (--cnt[del] == 0) {
                    type--;
                }
            }
            // 每次收缩完窗口后更新答案
            ans = Math.max(ans, right - left);
        }
        return ans;
    }

    public static void main(String[] args) {
        _904_FruitIntoBaskets obj = new _904_FruitIntoBaskets();
        System.out.println(obj.totalFruit(new int[]{1, 2, 3, 2, 2}));
    }
}
