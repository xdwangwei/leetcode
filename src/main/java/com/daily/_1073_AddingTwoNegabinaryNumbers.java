package com.daily;

import com.sun.nio.sctp.AssociationChangeNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/5/18 11:16
 * @description: _1037_AddingTwoNegabinaryNumbers
 *
 * 1073. 负二进制数相加
 * 给出基数为 -2 的两个数 arr1 和 arr2，返回两数相加的结果。
 *
 * 数字以 数组形式 给出：数组由若干 0 和 1 组成，按最高有效位到最低有效位的顺序排列。例如，arr = [1,1,0,1] 表示数字 (-2)^3 + (-2)^2 + (-2)^0 = -3。数组形式 中的数字 arr 也同样不含前导零：即 arr == [0] 或 arr[0] == 1。
 *
 * 返回相同表示形式的 arr1 和 arr2 相加的结果。两数的表示形式为：不含前导零、由若干 0 和 1 组成的数组。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr1 = [1,1,1,1,1], arr2 = [1,0,1]
 * 输出：[1,0,0,0,0]
 * 解释：arr1 表示 11，arr2 表示 5，输出表示 16 。
 * 示例 2：
 *
 * 输入：arr1 = [0], arr2 = [0]
 * 输出：[0]
 * 示例 3：
 *
 * 输入：arr1 = [0], arr2 = [1]
 * 输出：[1]
 *
 *
 * 提示：
 *
 * 1 <= arr1.length, arr2.length <= 1000
 * arr1[i] 和 arr2[i] 都是 0 或 1
 * arr1 和 arr2 都没有前导0
 * 通过次数7,707提交次数19,240
 */
public class _1073_AddingTwoNegabinaryNumbers {

    /**
     * 方法一：模拟
     * 思路与算法
     *
     * 为了叙述方便，记 arr1[i] 和 arr2[i] 分别是 arr1和 arr2 从低到高的第 i 个数位。
     *
     * 在加法运算中，我们需要将它们相加。
     *
     * 对于普通的二进制数相加，我们可以从低位到高位进行运算，在运算的过程中维护一个变量 carry 表示进位。
     *
     * 当运算到第 i 位时，令 x = arr1[i] + arr2[i] + carry：
     *
     * 如果 x=0,1，第 i 位的结果就是 x，并且将 carry 置 0；
     *
     * 如果 x=2,3，第 i 位的结果是 x−2（就是 x % 2），需要进位，将 carry 置 1。
     *
     * 而本题中是「负二进制数」，
     * 第 i 位对应的是 (−2)^i，而第 i+1 位对应的是 (−2)^i+1，是第 i 位的 −2 倍。
     * 因此当第 i 位发生进位时，carry 应当置为 −1，而不是 1。
     *
     * 此时，由于 arr1[i] 和 arr2[i] 的范围都是 {0,1}，而 carry 的范围从 {0,1} 变成了 {−1,0}，
     * 因此 x 的范围从 {0,1,2,3} 变成了 {−1,0,1,2}。
     *
     * 那么：
     *
     * 如果 x=0,1，第 i 位的结果就是 x，并且将 carry 置 0；
     *
     * 如果 x=2，第 i 位的结果是 x−2，需要进位，将 carry 置为 −1；
     *
     * 如果 x=−1，此时第 i 位的结果应该是什么呢？二进制只能取0或1，可以发现，
     *      由于：−1 * (−2)^i = 1 * (-2)^i + 1 * (−2)^(i+1)
     *      等式左侧表示第 i 位为 −1 的值，右侧表示第 i 和 i+1 位为 1 的值。
     *      因此，此时可以让 x 转为 1，即第 i 位的结果应当为 1，但同时需要向前进位，将 carry 置 1（注意这里不是 −1）。
     *
     * 最终，carry 的范围为 {−1,0,1}，会多出 x=3 的情况，但它与  x=2 的情况处理是一致的。
     *
     * 【细节】
     *
     * 在最坏的情况下，当我们计算完 arr1 和 arr2 的最高位的 x 时，得到的结果为 x=3，此时产生 −1 的进位，而 −1 在之后又会产生 1 的进位，
     * 因此，答案的长度最多为 arr1 和 arr2 中较长的长度加 2。
     *
     * 由于题目描述中 arr1[i] 和 arr2[i] 表示的是 arr1 和 arr2 从高到低的第 i 个数位，与题解中的叙述相反。
     *
     * 因此，在实际的代码编写中，我们可以使用两个指针从 arr1 和 arr2 的末尾同时开始进行逆序的遍历，得到逆序的答案，
     * 去除前导零，再将答案反转即可得到顺序的最终答案。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/adding-two-negabinary-numbers/solution/fu-er-jin-zhi-shu-xiang-jia-by-leetcode-nwktq/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr1
     * @param arr2
     * @return
     */
    public int[] addNegabinary(int[] arr1, int[] arr2) {
        int m = arr1.length, n = arr2.length;
        // 答案的长度最多为 arr1 和 arr2 中较长的长度加 2。
        List<Integer> ans = new ArrayList<>(Math.max(m, n) + 2);
        // 从低到高按位加法运算，carry 表示 上一个（低位）的进位，初始 为 0
        // 注意这里的条件 全是 或
        for (int i = m - 1, j = n - 1, carry = 0; i >= 0 || j >= 0 || carry != 0; i--, j--) {
            // arr1 和 arr2 从低到高 i、j 位对应取值
            // 某个串已经遍历完了就取0
            int a = i >= 0 ? arr1[i] : 0, b = j >= 0 ? arr2[j] : 0;
            // 当前位加法结果 {−1,0,1,2}。
            int x = a + b + carry;
            // 2 或 3，当前位取值1，进位 【-1】
            if (x >= 2) {
                ans.add(x - 2);
                carry = -1;
            // 0 或 1，正常取值，清除 carry
            } else if (x > -1) {
                ans.add(x);
                carry = 0;
            // -1，转为1，向前进位 【1】
            } else {
                ans.add(1);
                carry = 1;
            }
        }
        // 移除前导0（ans最后面的0，注意可能出现结果就是0的情况，所以这里要限制在 ans.size() > 0 情况下才移除多余0）
        while (ans.size() > 1 && ans.get(ans.size() - 1) == 0) {
            ans.remove(ans.size() - 1);
        }
        // 转为数组
        int[] ret = new int[ans.size()];
        // ans 从后往前遍历，ret 从前往后赋值
        for (int i = ans.size() - 1, j = 0; i >= 0; --i, ++j) {
            ret[j] = ans.get(i);
        }
        // 返回
        return ret;
    }
}
