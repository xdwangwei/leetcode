package com.dp;

/**
 * @author wangwei
 * 2021/12/26 10:59
 *
 * 给你一个字符串 s ，每一次操作你都可以在字符串的任意位置插入任意字符。
 *
 * 请你返回让 s 成为回文串的 最少操作次数 。
 *
 * 「回文串」是正读和反读都相同的字符串。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：s = "zzazz"
 * 输出：0
 * 解释：字符串 "zzazz" 已经是回文串了，所以不需要做任何插入操作。
 * 示例 2：
 *
 * 输入：s = "mbadm"
 * 输出：2
 * 解释：字符串可变为 "mbdadbm" 或者 "mdbabdm" 。
 * 示例 3：
 *
 * 输入：s = "leetcode"
 * 输出：5
 * 解释：插入 5 个字符后字符串变为 "leetcodocteel" 。
 * 示例 4：
 *
 * 输入：s = "g"
 * 输出：0
 * 示例 5：
 *
 * 输入：s = "no"
 * 输出：1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-insertion-steps-to-make-a-string-palindrome
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1312_MinimumInsertionToMakeStringPalindrome {

    /**
     * 动态规划
     * 我们定义一个二维的 dp 数组，dp[i][j] 的定义如下：对字符串 s[i..j]，最少需要进行 dp[i][j] 次插入才能变成回文串。
     *
     * 我们想求整个 s 的最少插入次数，根据这个定义，也就是想求 dp[0][n-1] 的大小（n 为 s 的长度）。
     *
     * 同时，base case 也很容易想到，当 i == j 时 dp[i][j] = 0，因为当 i == j 时 s[i..j] 就是一个字符，本身就是回文串，所以不需要进行任何插入操作。
     *
     * 接下来就是动态规划的重头戏了，利用数学归纳法思考状态转移方程。
     * 如果我们现在想计算 dp[i][j] 的值，而且假设我们已经计算出了子问题 dp[i+1][j-1] 的值了，你能不能想办法推出 dp[i][j] 的值呢？
     *
     * 既然已经算出 dp[i+1][j-1]，即知道了 s[i+1..j-1] 成为回文串的最小插入次数，那么也就可以认为 s[i+1..j-1] 已经是一个回文串了，
     * 所以通过 dp[i+1][j-1] 推导 dp[i][j] 的关键就在于 s[i] 和 s[j] 这两个字符。
     *
     * 如果 s[i] == s[j] 的话，我们不需要进行任何插入，只要知道如何把 s[i+1..j-1] 变成回文串即可：dp[i][j] = dp[i+1][j-1]
     *
     *  如果 s[i] != s[j] 的话，要么在s[i]前面放一个s[j]，然后需要处理的就是s[i...j-1]
     *                       要么在s[j]后面放一个s[i], 然后需要处理的就是s[i+1...j]
     *                       选择较小的那个，也就是 min(dp[i][j-1], dp[i+1][j])，还要加上补字符的操作1
     *
     *      if (s[i] == s[j]) {
     *          dp[i][j] = dp[i + 1][j - 1];
     *      } else {
     *          dp[i][j] = min(dp[i + 1][j], dp[i][j - 1]) + 1;
     *      }
     *
     * dp[i][j]需要dp[i+1][j-1],dp[i+1][j],dp[i][j-1]，所以需要倒序遍历
     * @param s
     * @return
     */
    public int minInsertions(String s) {
        int n = s.length();
        if (n == 1) {
            return 0;
        }
        // dp[i][j] 的定义如下：对字符串 s[i..j]，最少需要进行 dp[i][j] 次插入才能变成回文串。
        int[][] dp = new int[n][n];
        // base case , i == j, dp[i][j] = 0, 不用单独赋值
        // i = n - 1 时，j从n开始，for循环不用执行，因此不用考虑。这样可避免循环体内[i+1]越界
        for (int i = n - 2; i >= 0; --i) {
            // i = j 是base case，j 从 i + 1开始
            for (int j = i + 1; j < n; ++j) {
                // s[i] == s[j]
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1];
                    // s[i] != s[j]
                } else {
                    // 两种处理方式
                    dp[i][j] = Math.min(dp[i + 1][j], dp[i][j - 1]) + 1;
                }
            }
        }
        // 返回
        return dp[0][n - 1];
    }

    /**
     * 状态压缩，消除掉i维度
     * @param s
     * @return
     */
    public int minInsertions2(String s) {
        int n = s.length();
        if (n == 1) {
            return 0;
        }
        int[] dp = new int[n];
        for (int i = n - 2; i >= 0; --i) {
            // 每一轮开始的时候，这个pre要重新初始化，
            int dp_pre_j = 0;
            for (int j = i + 1; j < n; ++j) {
                int temp = dp[j];
                if (s.charAt(i) == s.charAt(j)) {
                    // dp[i][j] = dp[i + 1][j - 1];
                    // 这里需要的是，上一轮的j-1
                    // dp[j-1]是刚更新过的j-1
                    // 所以需要在刚才更新j-1的时候保存下来j-1上一轮的值，也就是pre_j
                    dp[j] = dp_pre_j;
                } else {
                    // dp[i][j] = Math.min(dp[i + 1][j], dp[i][j - 1]) + 1;
                    // 这里需要的是上一轮的j和这一轮的j-1
                    // j未更新前，它就是上一轮的j，所以直接消掉i+1
                    // j-1刚完成更新，所以它就是这一轮的j-1，直接消掉i
                    dp[j] = Math.min(dp[j], dp[j - 1]) + 1;
                }
                dp_pre_j = temp;
            }
        }
        return dp[n - 1];
    }
}
