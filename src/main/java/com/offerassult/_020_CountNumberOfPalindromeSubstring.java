package com.offerassult;

/**
 * @author wangwei
 * @date 2022/10/19 12:58
 * @description: _020_CountNumberOfPalindromeSubstring
 *
 * 剑指 Offer II 020. 回文子字符串的个数
 * 给定一个字符串 s ，请计算这个字符串中有多少个回文子字符串。
 *
 * 具有不同开始位置或结束位置的子串，即使是由相同的字符组成，也会被视作不同的子串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "abc"
 * 输出：3
 * 解释：三个回文子串: "a", "b", "c"
 * 示例 2：
 *
 * 输入：s = "aaa"
 * 输出：6
 * 解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 由小写英文字母组成
 *
 *
 * 注意：本题与主站 647 题相同：https://leetcode-cn.com/problems/palindromic-substrings/
 */
public class _020_CountNumberOfPalindromeSubstring {

    /**
     * 计算有多少个回文子串的最朴素方法就是枚举出所有的回文子串，而枚举出所有的回文字串又有两种思路，分别是：
     *
     * 枚举出所有的子串，然后再判断这些子串是否是回文；
     * 枚举每一个可能的回文中心，然后用两个指针分别向左右两边拓展，当两个指针指向的元素相同的时候就拓展，否则停止拓展。
     *
     * 假设字符串的长度为 n。我们可以看出
     * 前者会用 O(n^2)的时间枚举出所有的子串 s[l_i,r_i]，然后再用 O(r_i - l_i + 1)的时间检测当前的子串是否是回文，整个算法的时间复杂度是 O(n^3)O(n
     * 而后者枚举回文中心的是 O(n) 的，对于每个回文中心拓展的次数也是 O(n) 的，所以时间复杂度是 O(n^2)
     *
     * 所以我们选择第二种方法来枚举所有的回文子串。
     *
     * 在实现的时候，我们需要处理一个问题，即如何有序地枚举所有可能的回文中心，我们需要考虑回文长度是奇数和回文长度是偶数的两种情况。
     * 如果回文长度是奇数，那么回文中心是一个字符；
     * 如果回文长度是偶数，那么中心是两个字符。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/a7VOhD/solution/hui-wen-zi-zi-fu-chuan-de-ge-shu-by-leet-ejfv/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    /**
     * 动态规划，统计全部子串是否是回文串，
     * dp[i][j] 表示 s[i...j]是否是回文子串
     * 所以j的取值范围是 [i, n-1]，并且 i 要倒序遍历
     * dp[i][j] 为true，当且仅当 i == j 或 (s[i] == s[j] && (j==i+1 || dp[i+1][j-1])
     * j==i+1是串长度为2的时候，i+1,j-1间隔大于等于2，所以会漏掉这个段串
     * 合起来就是 dp[i][j] 为true，当且仅当 s[i] == s[j] && (j<=i+1 || dp[i+1][j-1])
     * 每一个dp[i][j]为true，表示回文串多了一个，res++
     * @param s
     * @return
     */
    public int countSubstrings(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int res = 0;
        // 倒序i
        for (int i = n - 1; i >= 0; --i) {
            // j 从 i 到 n - 1
            for (int j = i; j < n; ++j) {
                // dp[i][j] 为 true 的必要条件
                if (s.charAt(i) == s.charAt(j) && (j <= i + 1 || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    res++;
                }
            }
        }
        return res;
    }

    /**
     * 中心扩展法
     * 上面动态规划的递推式子核心还是由中间往两边扩展，所以直接采用中心扩展法
     * 回文串可能是奇数串也可能是偶数串，所以每个中心可以是(i,i)也可以是(i,i+1)
     * 从中心往左右同时扩展，每扩展一步，就能得到一个回文串，res++
     * @param s
     * @return
     */
    public int countSubstrings2(String s) {
        int n = s.length();
        int res = 0;
        for (int i = 0; i < n; ++i) {
            // 以 (i,i) 为中心同时向两边扩展
            // 左右指针同时往两个方向前进，必须保证不越界，并且 对应位置字符相等
            int left = i, right = i;
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
                // 每扩展一步得到的都是一个回文串
                res++;
            }
            // 以 (i,i+1) 为中心同时向两边扩展
            left = i;
            right = i + 1;
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
                res++;
            }
        }
        return res;
    }
}
