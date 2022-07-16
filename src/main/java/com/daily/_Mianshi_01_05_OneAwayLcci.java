package com.daily;

/**
 * @author wangwei
 * @date 2022/5/13 20:50
 * @description: _Mianshi_01_05_OneAwayLcci
 *
 * 面试题 01.05. 一次编辑
 * 字符串有三种编辑操作:插入一个字符、删除一个字符或者替换一个字符。 给定两个字符串，编写一个函数判定它们是否只需要一次(或者零次)编辑。
 *
 *
 *
 * 示例 1:
 *
 * 输入:
 * first = "pale"
 * second = "ple"
 * 输出: True
 *
 *
 * 示例 2:
 *
 * 输入:
 * first = "pales"
 * second = "pal"
 * 输出: False
 * 通过次数66,733提交次数190,503
 */
public class _Mianshi_01_05_OneAwayLcci {


    /**
     * 方法一：动态规划
     * 计算将 first 转换为 second 所需要的最少操作次数，返回 这个次数 是否 <= 1
     *
     * 相当于 题目 最少编辑次数
     *
     * 效率太低，没用到题目只能编辑1次或0次这个条件
     *
     * 每次只能进行替换、删除、或插入
     *
     * dp[i][j] 表示 将 s[0...i] 转换为 t[0...j] 所需要的最少编辑次数，
     *
     * 为了简单起见，定义 dp[0][0] 表示空串到空串的最少编辑次数 = 0
     *
     * 所以 dp[i][j] 表示 将 s[0...i-1] 转换为 t[0...j-1] 所需要的最少编辑次数，
     *
     * 返回 dp[m][n]
     *
     * base case
     * dp[0][j]表示把空串转换为 t[0...j-1] 所需要的最少操作次数，那 只能一个个插入，共插入 0...j-1 共 j 个字符
     * 即 dp[0][j] = j
     * 同理 dp[i][0] = 0
     *
     * 递推
     * dp[i][j] ，如果 s[i - 1] = t[j - 1]，那么 dp[i][j] = dp[i - 1][j - 1]
     *           否则，此处可选择 替换  dp[i][j] = 1 + dp[i - 1][j - 1]
     *                          删除  dp[i][j] = 1 + dp[i - 1][j] 或 1 + dp[i][j - 1]
     *                          插入 插入是相对于删除而言了，二者等价
     *                          所以 dp[i][j] = 1 + min(dp[i - 1][j],dp[i][j - 1],dp[i-1][j-1])
     *
     * @param first
     * @param second
     * @return
     */
    public boolean oneEditAway(String first, String second) {
        int m = first.length(), n = second.length();
        // 本题是要求只能1次或0次操作
        if (m <= 1 && n <= 1) {
            return true;
        }
        // 本题是要求只能1次或0次操作，那么长度差不能超过1
        if (Math.abs(m - n) > 1) {
            return false;
        }
        // dp[0][j]表示把空串转换为 t[0...j-1] 所需要的最少操作次数
        int[][] dp = new int[m + 1][n + 1];
        // base case，空串 到 t[0...j-1]
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
        }
        // base case，s[0...i-1] 到 空串
        for (int i = 1; i <= m; ++i) {
            dp[i][0] = i;
        }
        // 递推
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 三选一操作
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j - 1], dp[i - 1][j]));
                }
            }
        }
        // 返回
        return dp[m][n] <= 1;
    }


    /**
     * 方法一：分情况讨论
     * 假设字符串 \textit{first}first 和 \textit{second}second 的长度分别是 mm 和 nn。
     *
     * 如果 \textit{first}first 和 \textit{second}second 需要一次编辑，则可能有三种情况：
     *
     * 往 \textit{first}first 中插入一个字符得到 \textit{second}second，此时 n - m = 1n−m=1，\textit{second}second 比 \textit{first}first 多一个字符，其余字符都相同；
     *
     * 从 \textit{first}first 中删除一个字符得到 \textit{second}second，此时 m - n = 1m−n=1，\textit{first}first 比 \textit{second}second 多一个字符，其余字符都相同；
     *
     * 将 \textit{first}first 中的一个字符替换成不同的字符得到 \textit{second}second，此时 m = nm=n，\textit{first}first 和 \textit{second}second 恰好有一个字符不同。
     *
     * 如果 \textit{first}first 和 \textit{second}second 需要零次编辑，则 m = nm=n 且 \textit{first}first 和 \textit{second}second 相等。
     *
     * 根据上述分析，当符合一次编辑时，\textit{first}first 和 \textit{second}second 的长度关系可能有三种情况，分别是 n - m = 1n−m=1、m - n = 1m−n=1 和 m = nm=n。首先计算 \textit{first}first 和 \textit{second}second 的长度关系，在可能的三种情况中找到对应的一种情况，然后遍历字符串判断是否符合一次编辑或零次编辑。特别地，只有当 m = nm=n 时才需要判断是否符合零次编辑。
     *
     * 如果长度关系不符合上述三种情况，即 |m - n| > 1∣m−n∣>1，则不符合一次编辑或零次编辑。
     *
     * 具体实现方法如下。
     *
     * 当 n - m = 1n−m=1 或 m - n = 1m−n=1 时，由于两种情况具有对称性，因此可以定义一个函数统一计算这两种情况。用 \textit{longer}longer 表示较长的字符串，\textit{shorter}shorter 表示较短的字符串，同时遍历两个字符串，比较对应下标处的字符是否相同，如果字符相同则将两个字符串的下标同时加 11，如果字符不同则只将 \textit{longer}longer 的下标加 11。遍历过程中如果出现两个字符串的下标之差大于 11 则不符合一次编辑，遍历结束时如果两个字符串的下标之差不大于 11 则符合一次编辑。
     *
     * 当 m = nm=n 时，同时遍历 \textit{first}first 和 \textit{second}second，比较相同下标处的字符是否相同。如果字符不同的下标个数不超过 11，则符合一次编辑或零次编辑。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/one-away-lcci/solution/yi-ci-bian-ji-by-leetcode-solution-2xkr/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param first
     * @param second
     * @return
     */
    private boolean canEditAway2(String first, String second) {
        int m = first.length(), n = second.length();
        if (m <= 1 && n <= 1) {
            return true;
        }
        if (Math.abs(m - n) > 1) {
            return false;
        }
        // 二者长度相等，那么最多有1处字符不同（其他地方字符都对应相等），因为要求只能进行1次替换操作
        if (m == n) {
            int i = 0, j = 0, diff = 0;
            while (i < m && j < n) {
                if (first.charAt(i) != second.charAt(j)) {
                    diff++;
                    // 最多只能有1个字符不一样
                    if (diff > 1) {
                        return false;
                    }
                }
                i++; j++;
            }
            return true;
        } else {
            // 判断能够通过1次操作完成转换
            return m > n ? canOnceEdit(first, second) : canOnceEdit(second, first);
        }
    }

    /**
     * longer 比 shorter 多1个字符
     * 能够通过一次编辑操作让二者等同
     * 除非 shorter只是缺失longer某一个位置的字符，其他位置都对应相等
     * @param longer
     * @param shorter
     * @return
     */
    private boolean canOnceEdit(String longer, String shorter) {
        int m = longer.length(), n = shorter.length();
        int i = 0, j = 0;
        while (i < m && j < n) {
            // 二者对应相等
            if (longer.charAt(i) == shorter.charAt(j)) {
                // 同时前进
                i++;
                j++;
            } else {
                // 否则，相当于 shorter在此处插入这个缺少的字符，或者 longer 删除此处这个字符。longer指针前进
                i++;
            }
            // 二者最多差1，负责，说明多处不相等
            if (i - j > 1) {
                return false;
            }
        }
        return true;
    }


    /**
     * 观察方法二 对 二者长度相等 与 长度差1 的分别处理，能够发现，其实可以通过一次遍历统一完成
     * 每次出现s[i] != t[j] 那么这个地方就需要进行一次编辑，一旦有多于1处需要编辑，那直接返回false
     * 不同的是 如果二者长度相同，此处进行的就是 替换，然后双方均后移
     * 如果二者长度差1，此处的操作就是 删除，长者移除此处字符，长者指针后移
     * @param first
     * @param second
     * @return
     */
    private boolean canEditAway3(String first, String second) {
        int m = first.length(), n = second.length();
        // 默认 让first比second长
        if (m < n) {
            return canEditAway3(second, first);
        }
        // 二者长度差不能超过1
        if (m - n > 1) {
            return false;
        }
        int i = 0, j = 0, cnt = 0;
        while (i < m && j < n) {
            if (first.charAt(i) == second.charAt(j)) {
                i++; j++;
            } else {
                // 此处需要编辑
                cnt++;
                // 不能有超过两处编辑
                if (cnt > 1) {
                    return false;
                }
                // 如果 是等长串，那么此处的编辑就是 替换，然后双反均后移
                if (m == n) {
                    i++; j++;
                } else {
                    // 二者长度不等，此处的编辑就是 长者 删除这个字符，长者指针后移
                    i++;
                }
            }
        }
        return true;
    }
}
