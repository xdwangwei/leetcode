package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/3/29 20:19
 * @description: _1641_CountSortedVowelStrings
 *
 * 1641. 统计字典序元音字符串的数目
 * 给你一个整数 n，请返回长度为 n 、仅由元音 (a, e, i, o, u) 组成且按 字典序排列 的字符串数量。
 *
 * 字符串 s 按 字典序排列 需要满足：对于所有有效的 i，s[i] 在字母表中的位置总是与 s[i+1] 相同或在 s[i+1] 之前。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 1
 * 输出：5
 * 解释：仅由元音组成的 5 个字典序字符串为 ["a","e","i","o","u"]
 * 示例 2：
 *
 * 输入：n = 2
 * 输出：15
 * 解释：仅由元音组成的 15 个字典序字符串为
 * ["aa","ae","ai","ao","au","ee","ei","eo","eu","ii","io","iu","oo","ou","uu"]
 * 注意，"ea" 不是符合题意的字符串，因为 'e' 在字母表中的位置比 'a' 靠后
 * 示例 3：
 *
 * 输入：n = 33
 * 输出：66045
 *
 *
 * 提示：
 *
 * 1 <= n <= 50
 * 通过次数15,930提交次数20,415
 */
public class _1641_CountSortedVowelStrings {

    /**
     * 方法一：动态规划
     *
     * 长度为 n 的字符串 ss 可以由长度为 n-1 的字符串 s 末尾加上 'a','e','i','o','u' 其中一个字符构成
     * 当 s 末尾 为 'a' 时，可以 加的字符 有 'a','e','i','o','u'，
     * 当 s 末尾 为 'e' 时，可以 加的字符 有 'e','i','o','u'，
     * 当 s 末尾 为 'i' 时，可以 加的字符 有 'i','o','u'，
     * 当 s 末尾 为 'o' 时，可以 加的字符 有 'o','u'，
     * 当 s 末尾 为 'u' 时，可以 加的字符 有 'u'，
     *
     * 因此，dp[i][j] 表示长度为 i 且 以字符 j 结尾 的按字典序排列的字符串数量，
     *
     * 那么最终答案为 sum(dp[n][j]) for j in ('a','e','i','o','u'，)
     *
     * 既然考虑字符的相对顺序，那么 方便起见分别使用数字 0，1，2，3，4 代表元音字符 ‘a’，‘e’，‘i’，‘o’，‘u’。
     *
     * 记 dp[i][j] 表示长度为 i，且 以 j 结尾的按字典序排列的字符串数量，那么状态转移方程如下：
     *
     *      dp[0][...] = 0, 0 <= j <= 4
     *      dp[1][...] = 1, 0 <= j <= 4
     *      dp[i][j] = sum(dp[i-1][k], 0 <= k <= j)  i >= 2
     *                 (长度为 i，末尾为 j，来自于长度为 i-1 的字符串后缀追加字符 j，末尾允许追加 j 的前提是本身的末尾字符必须 <= j )
     *
     * 最终返回 sum(dp[n][k], 0 <= k <= 4)
     *
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/count-sorted-vowel-strings/solution/tong-ji-zi-dian-xu-yuan-yin-zi-fu-chuan-sk7y1/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @return
     */
    public int countVowelStrings(int n) {
        // dp[i][j] 表示长度为 i 且 以字符 j 结尾 的按字典序排列的字符串数量，
        int[][] dp = new int[n + 1][5];
        // base case dp[1][...] = 1, 0 <= j <= 4
        Arrays.fill(dp[1], 1);
        // dp[i][j] = sum(dp[i-1][k], 0 <= k <= j)
        for (int i = 2; i <= n; ++i) {
            for (int j = 0; j < 5; ++j) {
                for (int k = 0; k <= j; ++k) {
                    dp[i][j] += dp[i - 1][k];
                }
            }
        }
        // 最终返回 sum(dp[n][k], 0 <= k <= 4)
        int ans = 0;
        for (int j = 0; j < 5; ++j) {
            ans += dp[n][j];
        }
        // 返回
        return ans;
    }


    /**
     * 方法一中 三重循环， 求解 dp[i][j] 存在 j 范围内的枚举，因此可以优化
     *
     * dp[i][j] = sum(dp[i-1][k], 0 <= k <= j)
     *
     * dp[i][j+1] = sum(dp[i-1][k], 0 <= k <= j+1) = dp[i][j] + dp[i-1][j+1]
     *
     * 因此 dp[i][j] = dp[i][j - 1] + dp[i - 1][j]; 注意 j 的取值范围
     * 省略掉第三层循环
     *
     * 具体可以画一个 dp表格观察一下
     *
     * @param n
     * @return
     */
    public int countVowelStrings2(int n) {
        // dp[i][j] 表示长度为 i 且 以字符 j 结尾 的按字典序排列的字符串数量，
        int[][] dp = new int[n + 1][5];
        // base case dp[1][...] = 1, 0 <= j <= 4
        Arrays.fill(dp[1], 1);
        // dp[i][j] = sum(dp[i-1][k], 0 <= k <= j)
        for (int i = 2; i <= n; ++i) {
            for (int j = 0; j < 5; ++j) {
                dp[i][j] = (j > 0 ? dp[i][j - 1] : 0) + dp[i - 1][j];
            }
        }
        // 最终返回 sum(dp[n][k], 0 <= k <= 4)
        int ans = 0;
        for (int j = 0; j < 5; ++j) {
            ans += dp[n][j];
        }
        // 返回
        return ans;
    }


    /**
     * 从方法二中可以看出， dp[i] 只和 dp[i-1] 有关，因此可以 使用滚动数组 将空间复杂度 优化为 int[2][5]
     *
     * 具体 用 dp[i & 1] 代替原来的 dp[i]
     * 具体 用 dp[i & 1 ^ 1] 代替原来的 dp[i - 1]
     *
     * @param n
     * @return
     */
    public int countVowelStrings3(int n) {
        // dp[i][j] 表示长度为 i 且 以字符 j 结尾 的按字典序排列的字符串数量，
        int[][] dp = new int[2][5];
        // base case dp[1][...] = 1, 0 <= j <= 4
        // i --> 1&1
        Arrays.fill(dp[1 & 1], 1);
        // dp[i][j] = sum(dp[i-1][k], 0 <= k <= j)
        for (int i = 2; i <= n; ++i) {
            for (int j = 0; j < 5; ++j) {
                // i --> i&1, i-1 --> i&1^1
                dp[i & 1][j] = (j > 0 ? dp[i & 1][j - 1] : 0) + dp[i & 1 ^ 1][j];
            }
        }
        // 最终返回 sum(dp[n][k], 0 <= k <= 4)
        int ans = 0;
        for (int j = 0; j < 5; ++j) {
            // n --> n&1
            ans += dp[n & 1][j];
        }
        // 返回
        return ans;
    }
}
