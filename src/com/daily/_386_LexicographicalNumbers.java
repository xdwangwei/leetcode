package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2022/4/18 9:15
 *
 * 386. 字典序排数
 * 给你一个整数 n ，按字典序返回范围 [1, n] 内所有整数。
 *
 * 你必须设计一个时间复杂度为 O(n) 且使用 O(1) 额外空间的算法。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 13
 * 输出：[1,10,11,12,13,2,3,4,5,6,7,8,9]
 * 示例 2：
 *
 * 输入：n = 2
 * 输出：[1,2]
 *
 *
 * 提示：
 *
 * 1 <= n <= 5 * 104
 */
public class _386_LexicographicalNumbers {


    /**
     * 迭代写法
     * 题目要求设计一个时间复杂度为 O(n) 且使用 O(1) 额外空间的算法，因此我们不能使用直接排序的方法。
     *
     * 那么对于一个整数 number，它的下一个字典序整数对应下面的规则：
     *
     * 尝试在 number 后面附加一个零，即 number×10，如果 number×10 ≤ n，那么说明 number×10 是下一个字典序整数；
     *
     * 如果 number mod 10 = 9 或  number + 1 > n，那么说明末尾的数位已经搜索完成，退回上一位，即  number /= 10
     * 然后继续判断直到 number mod 10 != 9 && number + 1 <= n 为止，那么 number+1 是下一个字典序整数。
     *
     * 字典序最小的整数为 number=1，我们从它开始，然后依次获取下一个字典序整数，加入结果中，结束条件为已经获取到 n 个整数。
     *
     * 相当于是按照顺序去枚举出1-n所有的字典数
     * 第一个是 1， 1 完了应该 是 10，10 完了应该是 11， 12，13，。。。，19
     * 接下来是 2， 2 完了应该 是 20，20 完了应该是 21，22，23，，，，，29
     * 接下来是3，  3 完了应该是 30，30完了应该是 31，32，33，，，，，，39
     *
     * 可以看到，对于 当前字典数num，下一个应该是 num * 10，当前前提是 num * 10 <= n
     *                           否则应该是 num + 1，
     *                           但是问题在于，假如 num 现在是19，假如 n是200，那么 num * 10 = 190 正确，
     *                                                        假如 n是20，那么 num * 10 > n
     *                                                        下一个应该是 num + 1
     *                                                        但是19 + 1 = 20
     *                                                        然而 19 的下一个 字典数 应该是 2，所以 这里 num 应该等于 num / 10 = 1
     *                                                        然后 再进行 num = num + 1，就正确了
     *                                                        相当于 11-19是num=1变为10之后完成的，那么20-29应该是num=2再变成20再完成
     *                                                        所以这里num应该回退到原本的1
     *                                                   当 num = 29. 39也是一样的，也就是 n % 10 ==9
     *                                                    并且，num + 1 不能超过 n，如果 num + 1 > n, 那也要先回退 num
     *                                              另外，这里的回退num不是一次就够了，上面的只是两位数，如果是多位数，num还需要继续回退，也就是说这个回退过程是个while
     * 综上，第一个num=1，一共有n个数字，按字典序枚举出这些数字，加入 list
     * for (int i = 0, next = 1; i < n; ++i) {
     *     list.add(next);
     *     if (next * 10 <= n) next *= 10;
     *     else {
     *         while (next % 10 ==9 || next + 1 > n) {
     *             next /= 10;
     *         }
     *         next++;
     *     }
     * }
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/lexicographical-numbers/solution/zi-dian-xu-pai-shu-by-leetcode-solution-98mz/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @return
     */
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        // 第一个字典数
        int next = 1;
        // 一共有n个，按字典序枚举出来
        for (int i = 0; i < n; ++i) {
            // 依次添加进list
            res.add(next);
            // 如果 next * 10 <= 10，下一个应该是 next * 10, 比如 1 的下一个是 应该是 10
            if (next * 10 <= n) {
                next *= 10;
            } else {
                // 否则下一个应该是 next + 1，但是得先判断临界情况
                // 比如 19的下一个是2，不是20，所以 等 19 完了，应该 把 next 变为 1，然后 next + 1 = 2，2 的下一个 是 20，这样才正确
                while (next % 10 == 9 || next + 1 > n) {
                    next /= 10;
                }
                // 下一个是 next + 1
                next++;
            }
        }
        return res;
    }

    /**
     * 递归，相当于在字典树上进行深度优先搜索，或者说前序遍历
     * 根节点是0，然后每个节点都有0-9十个孩子，每个孩子也是有0-9十个孩子，以此类推，那么字典序得到的列表应该是 从 节点1开始进行 前序遍历
     * 哦不，第一层节点只有1-9 9个孩子，0不算
     * 也就是对第一层节点，按顺序进行深度优先搜搜
     * @param n
     * @return
     */
    public List<Integer> lexicalOrde2(int n) {
        List<Integer> res = new ArrayList<>();
        // 按顺序对第一层节点进行dfs就好了，注意第一层只有1-9
        for (int i = 1; i <= 9; ++i) {
            dfs(i, n, res);
        }
        return res;
    }

    private void dfs(int cur, int n, List<Integer> res) {
        if (cur > n) {
            return;
        }
        // 访问自己
        res.add(cur);
        // 从左到右访问所有孩子，每个孩子代表的数字是 cur * 10 + i。第一层往后每个孩子都有0-9 10个孩子
        for (int i = 0; i <= 9; ++i) {
            dfs(cur * 10 + i, n, res);
        }
    }
}
