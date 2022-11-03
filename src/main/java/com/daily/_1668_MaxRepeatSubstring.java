package com.daily;

/**
 * @author wangwei
 * @date 2022/11/3 12:35
 * @description: _1668_MaxRepeatSubstring
 *
 * 1668. 最大重复子字符串
 * 给你一个字符串 sequence ，如果字符串 word 连续重复 k 次形成的字符串是 sequence 的一个子字符串，那么单词 word 的 重复值为 k 。单词 word 的 最大重复值 是单词 word 在 sequence 中最大的重复值。如果 word 不是 sequence 的子串，那么重复值 k 为 0 。
 *
 * 给你一个字符串 sequence 和 word ，请你返回 最大重复值 k 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：sequence = "ababc", word = "ab"
 * 输出：2
 * 解释："abab" 是 "ababc" 的子字符串。
 * 示例 2：
 *
 * 输入：sequence = "ababc", word = "ba"
 * 输出：1
 * 解释："ba" 是 "ababc" 的子字符串，但 "baba" 不是 "ababc" 的子字符串。
 * 示例 3：
 *
 * 输入：sequence = "ababc", word = "ac"
 * 输出：0
 * 解释："ac" 不是 "ababc" 的子字符串。
 *
 *
 * 提示：
 *
 * 1 <= sequence.length <= 100
 * 1 <= word.length <= 100
 * sequence 和 word 都只包含小写英文字母。
 * 通过次数23,602提交次数50,023
 */
public class _1668_MaxRepeatSubstring {

    /**
     * 方法一：直接枚举
     *
     * 注意到字符串长度不超过 100，我们直接从大到小枚举 word 的重复次数 k，判断 word 重复该次数后是否是 sequence 的子串，是则直接返回当前的重复次数 k。
     *
     * 时间复杂度为 O(n^2)，其中 n 为 sequence 的长度。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximum-repeating-substring/solution/by-lcbin-cg8m/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param sequence
     * @param word
     * @return
     */
    public int maxRepeating(String sequence, String word) {
        int m = sequence.length(), n = word.length();
        // 边界
        if (m < n) {
            return 0;
        }
        if (m == n) {
            return sequence.equals(word) ? 1 : 0;
        }
        // 从大到小枚举重复次数k
        for (int k = m / n; k >= 0; --k) {
            // 判断word重复k次形成的串是否是s的子串
            if (sequence.contains(word.repeat(k))) {
                return k;
            }
        }
        return 0;
    }

    /**
     * 滑动窗口，枚举左端点left，
     * 假设 s 长度为 m， word 长度为 n，
     * left 从0开始，每次截取 s中 (left, left + n) 部分子串，和 word 比较
     *      若 相等，重复次数+1，left += n;
     *      若 不等，若此时是第一次匹配失败，则 left 从 left+1位置开始重新尝试，即 left += 1；
     *              若此时是匹配一次后，第二次匹配失败，则 left 从上一次开始的 下一个位置开始重新开始匹配，即 left = left -n + 1；
     * @param sequence
     * @param word
     * @return
     */
    public int maxRepeating2(String sequence, String word) {
        int m = sequence.length(), n = word.length();
        // 特殊情况
        if (m < n) {
            return 0;
        }
        if (m == n) {
            return sequence.equals(word) ? 1 : 0;
        }
        // 枚举左端点，count是每次枚举过程中得到的最大重复次数
        int ans = 0, count = 0, left = 0;
        // flag标记当前是第一次匹配还是已经成功匹配了长度n后的第二次匹配
        boolean flag = false;
        // 枚举左端点
        while (left < m - n + 1) {
            // 截取 长度n 部分 和 word比较
            // 若 相等，
            if (sequence.substring(left, left + n).equals(word)) {
                // 重复次数 +1
                count++;
                // left 跳过 长度 n
                left += n;
                // 标记已经匹配一次
                flag = true;
            } else {
                // 若不等
                // 如果是匹配一次后第二次匹配失败
                if (flag) {
                    // 跳到上一次匹配开始的下一个位置重新开始
                    left = left - n + 1;
                    // 清空标志
                    flag = false;
                } else {
                    // 如果是第一次匹配失败
                    // left直接从下一个位置开始
                    left++;
                }
            }
            // 匹配失败时，用已经匹配的重复值更新结果
            ans = Math.max(ans, count);
            // 清空当前枚举匹配过程得到的重复次数，进行下一次枚举匹配
            count = 0;
        }
        // while结束后需要再比较一次
        ans = Math.max(ans, count);
        // 返回
        return ans;
    }

    /**
     * 序列 DP
     * 为了方便，我们记 sequence 为 ss，记 word 为 pp，将两者长度分别记为 n 和 m。
     *
     * 同时我们调整「字符串」以及「将要用到的动规数组」的下标从 1 开始。
     *
     * 这是一道入门级的「序列 DP」运用题，容易想到 定义 f[i] 为了考虑以 ss[i] 结尾时的最大重复值。
     *
     * 不失一般性考虑 f[i] 该如何转移：
     * 由于 pp 的长度已知，每次转移 f[i] 时我们可以从 ss 中截取 以 ss[i] 为结尾，长度为 m 的后缀字符串 sub 并与 pp 匹配，
     * 若两者相等，说明 sub 贡献了大小为 1 的重复度，同时该重复度可累加在 f[i−m] 上（好好回想我们的状态定义），
     * 即有状态转移方程：f[i]=f[i−m]+1。
     *
     * 最终所有 f[i] 的最大值即为答案。
     *
     * 此解法瓶颈在于：每次需要花费 O(m) 的复杂度来生成子串，并进行字符串比较。
     *
     * 该过程可用「字符串哈希」进行优化：将 ss 和 pp 进行拼接得到完整字符串，并计算完整字符串的哈希数组和次方数组。
     * 随后从前往后检查 ss，
     * 若「某个以 ss[i] 结尾长度为 m 的后缀字符串哈希值」与「 pp 字符串的哈希值」相等，
     * 说明找到了前驱状态值 f[i−m]，可进行转移。
     *
     * 我们通过 O(n + m) 复杂度预处理了字符串哈希，将转移过程中「复杂度为 O(m) 的子串截取与字符串比较」替换成了「复杂度为 O(1) 的数值对比」，
     * 整体复杂度从 O(n×m) 下降到 O(n+m)。
     *
     * 不了解「字符串哈希」的同学可见前置  : 字符串哈希入门。
     * https://mp.weixin.qq.com/s?__biz=MzU4NDE3MTEyMA==&mid=2247489813&idx=1&sn=7f3bc18ca390d85b17655f7164d8e660
     * 里面详解字符串哈希基本原理以及哈希冲突简单处理方式
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/maximum-repeating-substring/solution/by-ac_oier-xjhn/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    long[] h, p;
    public int maxRepeating3(String ss, String pp) {
        int n = ss.length(), m = pp.length(), ans = 0;
        int[] f = new int[n + 10];

        String s = ss + pp;
        int P = 1313131, N = s.length();
        h = new long[N + 10]; p = new long[N + 10];
        p[0] = 1;
        for (int i = 1; i <= N; i++) {
            h[i] = h[i - 1] * P + s.charAt(i - 1);
            p[i] = p[i - 1] * P;
        }
        long phash = h[N] - h[N - m] * p[m];

        for (int i = 1; i <= n; i++) {
            if (i - m < 0) continue;
            long cur = h[i] - h[i - m] * p[m];
            if (cur == phash) f[i] = f[i - m] + 1;
            ans = Math.max(ans, f[i]);
        }
        return ans;
    }
}
