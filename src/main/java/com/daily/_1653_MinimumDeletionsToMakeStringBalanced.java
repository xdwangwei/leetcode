package com.daily;

/**
 * @author wangwei
 * @date 2023/3/6 12:04
 * @description: _1653_MinimumDeletionsToMakeStringBalanced
 *
 * 1653. 使字符串平衡的最少删除次数
 * 给你一个字符串 s ，它仅包含字符 'a' 和 'b' 。
 *
 * 你可以删除 s 中任意数目的字符，使得 s 平衡 。当不存在下标对 (i,j) 满足 i < j ，且 s[i] = 'b' 的同时 s[j]= 'a' ，此时认为 s 是 平衡 的。
 *
 * 请你返回使 s 平衡 的 最少 删除次数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "aababbab"
 * 输出：2
 * 解释：你可以选择以下任意一种方案：
 * 下标从 0 开始，删除第 2 和第 6 个字符（"aababbab" -> "aaabbb"），
 * 下标从 0 开始，删除第 3 和第 6 个字符（"aababbab" -> "aabbbb"）。
 * 示例 2：
 *
 * 输入：s = "bbaaaaabb"
 * 输出：2
 * 解释：唯一的最优解是删除最前面两个字符。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 105
 * s[i] 要么是 'a' 要么是 'b' 。
 * 通过次数6,843提交次数12,314
 */
public class _1653_MinimumDeletionsToMakeStringBalanced {

    /**
     * 方法一：枚举
     *
     * 思路
     *
     * 通过删除部分字符串，使得字符串达到下列三种情况之一，即为平衡状态：
     *
     * 字符串全为 “a”；
     * 字符串全为 “b”
     * 字符串既有 “a” 也有 “b”，且所有 “a” 都在所有 “b” 左侧。
     *
     * 其中，为了达到第 1 种情况，最少需要删除所有的 “b”。
     * 为了达到第 2 种情况，最少需要删除所有的 “a”。
     * 而第 3 种情况，可以在原字符串相邻的任意两个字符之间划一条间隔，删除间隔左侧所有的 “b” 和间隔右侧所有的 “a” 即可达到。
     * 用 leftb 表示间隔左侧的 “b” 的数目，righta 表示间隔右侧的 “a” 的数目，那么 leftb+righta 即为当前划分的间隔下最少需要删除的字符数。
     * 这样的间隔一共有 n−1 种，其中 n 是 s 的长度。
     *
     * 遍历字符串 s，即可以遍历 n−1 种间隔，同时更新 leftb 和 righta 的数目。
     * 而讨论的前两种情况，其实就是间隔位于首字符前和末字符后的两种特殊情况，可以加入第 3 种情况一并计算。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-deletions-to-make-string-balanced/solution/shi-zi-fu-chuan-ping-heng-de-zui-shao-sh-l5lk/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int minimumDeletions(String s) {
        // 间隔右侧所有a字符，间隔从字符串最前面开始枚举，统计右侧a的个数
        int righta = 0;
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c == 'a') {
                righta++;
            }
        }
        // 初始，间隔在字符串左侧，左侧b的个数为0，右侧a的个数为righta，ans = 0+righta = righta
        // 下一个间隔，在第一个字符右侧，对于这个间隔来说，它的leftb和righta的更新取决于第一个字符是a还是b
        // 因此for循环遍历从0位置开始，枚举的是间隔，影响间隔的是左侧字符
        int ans = righta, leftb = 0;
        for (int i = 0; i < chars.length; ++i) {
            // 字符是a，对于右侧间隔来说，它的righta--
            if (chars[i] == 'a') {
                righta--;
            } else {
                // 对于右侧间隔来说，它的leftb++
                leftb++;
            }
            // 对于这个间隔，需要移除左侧所有b和右侧所有a实现平衡，取所有情况最小值
            ans = Math.min(ans, leftb + righta);
        }
        // 返回
        return ans;
    }

    /**
     * 方法二：动态规划
     *
     * 令 dp[i]为使字符串 s[0:i]平衡的最少删除次数，考虑状态转移过程：
     *
     * 若 s[i]为'b'，很好，这时候只要 s[0:i−1]是平衡的， s[0:i]就是平衡的，于是 dp[i]=dp[i−1]；
     * 若 s[i]为'a'，这时候 s[i]有两种命运：
     *      如果我们保留 s[i]，那么我们要删掉 s[0:i−1]中所有的'b'，s[0:i−1]中'b'的数量（记为 ni）；
     *      如果我们删除 s[i]，那么 dp[i] = 1 + dp[i−1]。
     * 于是，状态转移方程为：
     *
     * dp[i] = dp[i - 1] if s[i]==‘b‘.
     * dp[i] = { dp[i−1], min(1+dp[i−1],ni } if s[i]==‘a‘,
     *
     * 直接状态压缩，用变量代替，在遍历的过程中可更新得到 ni
     *
     * 作者：_dc
     * 链接：https://leetcode.cn/problems/minimum-deletions-to-make-string-balanced/solution/dong-tai-gui-hua-by-_dc-ok1j/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int minimumDeletions2(String s) {
        // dp[i]为使字符串 s[0:i]平衡的最少删除次数，cnt_b 表示 s[i] 左侧字符b的个数
        int n = s.length(), cnt_b = 0, dp = 0;
        // 遍历
        for (int i = 0; i < n; ++i){
            // 字符b
            if (s.charAt(i) == 'b') {
                // dp[i] = dp[i-1]
                ++cnt_b;
            } else {
                // 字符 a
                // dp[i] = min(1 + dp[i-1], ni)
                dp = Math.min(dp + 1, cnt_b);
            }
        }
        // return dp[n-1]
        return dp;
    }
}
