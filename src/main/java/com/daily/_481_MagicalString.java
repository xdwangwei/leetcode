package com.daily;

/**
 * @author wangwei
 * @date 2022/10/31 14:21
 * @description: _481_MagicalString
 *
 * 481. 神奇字符串
 * 神奇字符串 s 仅由 '1' 和 '2' 组成，并需要遵守下面的规则：
 *
 * 神奇字符串 s 的神奇之处在于，串联字符串中 '1' 和 '2' 的连续出现次数可以生成该字符串。
 * s 的前几个元素是 s = "1221121221221121122……" 。如果将 s 中连续的若干 1 和 2 进行分组，可以得到 "1 22 11 2 1 22 1 22 11 2 11 22 ......" 。每组中 1 或者 2 的出现次数分别是 "1 2 2 1 1 2 1 2 2 1 2 2 ......" 。上面的出现次数正是 s 自身。
 *
 * 给你一个整数 n ，返回在神奇字符串 s 的前 n 个数字中 1 的数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 6
 * 输出：3
 * 解释：神奇字符串 s 的前 6 个元素是 “122112”，它包含三个 1，因此返回 3 。
 * 示例 2：
 *
 * 输入：n = 1
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= n <= 105
 * 通过次数18,908提交次数29,886
 */
public class _481_MagicalString {


    /**
     * 根据题意，我们可以把 s 看成是由「1 组，都是1」和「2 组，都是2」交替组成的，重点在于每组内的数字是一个还是两个，这可以从 s 自身上知道。
     *
     * s 的前六位是 122112
     *
     * 比如从 s 的前三位 122 开始，构造 s 的过程：
     *      假设 i = 2，指向最后一个字符'2'
     *      由于[1组][2组]交替出现，那么此时该构造 [1组了]，该构造几个1呢，由i指向的数字决定
     *      那么为什么i此时指向第三个位置呢？因为现在要构造第3组数，所以i指向第三个位置
     *      那么此时构造出两个1，s变为 12211，此时 i = 3，s[i] = 1，此时要构造第四组数，第四组是 [2组]，
     *      那么 构造 1 个 2，此时 s 变为  122112，此时 i = 4，s[i] = 1，此时构造第五组，第五组是 [1组]
     *      那么构造 1 个 1， s 变为 1221121.。。。
     *      直到 s 的长度 达到 n 就停止
     *
     *      或者这样理解 i 的初始位置
     *      从 122 开始构造，下一个要构造 [1组]，从题目给出的串来看，下一个[1组]应该有2个1，那么 i 必然等于1或2，s[i] = 2
     *      假如 i=1,s[i] =2,那么 构造2个1，s变为 12211，然后 i = 2， s[i] = 2，再构造 两个2，s变为 1221122，和题目给出的不一致了
     *      所以 i 必然是 从 2 开始
     *
     *      所以：当前要构造 第 ？ 组数字，那么 i 就指向 第 ？ 个位置
     *
     * 至于怎么快速直到下一组是[1组]还是[2组]呢？我们直到 1 ^ 3 = 2, 2 ^ 3 = 1，所以可以快速切换
     *
     * 我们从 122 往后构造，下一个要构造第三组数，即 c=[1组]，此时 i 指向第3个位置，
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/magical-string/solution/by-endlesscheng-z8o1/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @return
     */
    public int magicalString(int n) {
        if (n <= 3) {
            return 1;
        }
        // base 串
        StringBuilder builder = new StringBuilder("122");
        // nextChar 下一个要构造 [1组] 还是 [2组]
        // s[i] 决定下一组数的个数，由于下一组是第3组，所以 i 初始指向第三个位置，也就是 i = 2
        // k 我们要统计s[0...n-1]中 1 的个数，k从0开始，到n-1结束
        // ans 就是 s[0...n-1]中 1 的个数
        int nextChar = 1, i = 2, k = 0, ans = 0;
        while (k < n) {
            // 下一组数字的个数，1或者2
            int cnt = builder.charAt(i) - '0';
            // 拼接
            while (cnt-- > 0) {
                builder.append(nextChar);
            }
            // 下一组
            i++;
            // 切换下一组是 [1组] 还是 [2组]
            nextChar ^= 3;
            // 从0位置统计当前位置是否为1，k++
            if (builder.charAt(k++) == '1') {
                ans++;
            }
        }
        // 返回
        return ans;
    }


    /**
     * 还可以根据题目数据范围，直接构造出一个长度至少为 10^5 的 s，并求出每个前缀中 1 的个数，存到 acc 数组中，
     * 这样就不用每次都生成并统计了，直接返回 acc[n]。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/magical-string/solution/by-endlesscheng-z8o1/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param args
     */

    private static int[] acc;

    static {
        acc = new int[100001];
        // base 串
        StringBuilder builder = new StringBuilder("122");
        // acc[i] 表示前 i 个字符中1的个数，
        acc[0] = acc[1] = acc[2] = acc[3] = 1;
        // nextChar 下一个要构造 [1组] 还是 [2组]
        // s[i] 决定下一组数的个数，由于下一组是第3组，所以 i 初始指向第三个位置，也就是 i = 2
        // k 我们要统计s前k个字符中 1 的个数，由于acc[0...3]以赋初值，所以 k 从 4开始
        // ans 就是 s[0...n-1]中 1 的个数
        int nextChar = 1, i = 2, k = 4, ans = 0;
        while (k < acc.length) {
            // 下一组数字的个数，1或者2
            int cnt = builder.charAt(i) - '0';
            // 拼接
            while (cnt-- > 0) {
                builder.append(nextChar);
            }
            // 下一组
            i++;
            // 切换下一组是 [1组] 还是 [2组]
            nextChar ^= 3;
            // 更新acc
            acc[k] = builder.charAt(k - 1) == '1' ? acc[k - 1] + 1 : acc[k - 1];
            k++;
        }
    }

    public int magicalString2(int n) {
        return acc[n];
    }

    public static void main(String[] args) {
        _481_MagicalString obj = new _481_MagicalString();
        obj.magicalString(7);
    }
}
