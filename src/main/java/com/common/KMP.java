package com.common;

/**
 * @author wangwei
 * 2021/12/27 10:50
 *
 * KMP算法实现
 * https://leetcode-cn.com/problems/implement-strstr/solution/shua-chuan-lc-shuang-bai-po-su-jie-fa-km-tb86/
 * 如何更好地理解和掌握 KMP 算法? - 阮行止的回答 - 知乎
 * https://www.zhihu.com/question/21923021/answer/1032665486
 */
public class KMP {

    /**
     * 返回模式串 pattern 的 next 数组
     * next[i] 代表 串 p[0...i]的最长公共真前后缀的长度，真前后缀表示不包括自己的前后缀
     * 准确的来说，这应该叫做 PMT 表 (Partial Match Table)
     * 通常为了便于在 search 方法中更方便的使用 next，会将 PMT表 的所有值右移一位，以此得到 next 数组，
     * 相当于 next[j] = PMT[j-1], 因为 PMT[j-1]右移一个位置就到了j位置，也就是next[j]
     * 而实际上，当 s[i] 和 p[j] 配置失败时，因为主串s不回退， j 应该回退到 PMT[j-1] 的位置，所以 如果使用 next 数组，那就是 j = next[j]，
     * 从编程的角度来说 next 使用起来更方便一些，但是 个人觉得 不如 PMT 好理解。
     * 而且 直接得到 PMT 表的编程过程也更容易理解。
     *
     * 这里返回的实际上就是PMT。不是右移后的next。
     *
     * 计算过程
     * 定义 “k-前缀” 为一个字符串的前 k 个字符； “k-后缀” 为一个字符串的后 k 个字符。k 必须小于字符串长度。
     * pmt[x] 定义为： P[0]~P[x] 这一段字符串，使得k-前缀恰等于k-后缀的最大的k，也也就是 p[0...j]的最长公共前后缀长度。
     * 这个定义中，不知不觉地就包含了一个匹配——前缀和后缀相等。
     * 接下来，我们考虑采用递推的方式求出pmt数组。
     *
     * 假设当前处理到索引j，也就是求 串 p[0...j]的最长公共前后缀长度，用 preLen 表示 p[0...j-1]的最长公共前后缀长度，即 pmt[j-1]
     * 如果 pmt[0], pmt[1], ... pmt[j-1]均已知，那么如何求出 pmt[j] 呢？　　来分情况讨论。
     *      首先，已经知道了 pmt[j-1]=preLen，也就是 p[j]前面 preLen 个字符(即p[?...j-1])与 p[0...preLen-1] 相等
     *          如果 P[j] 与 P[preLen] 一样，那么前缀和后缀能同时增加一个相同的字符，也就是那最长相等前后缀的长度就可以扩展一位，即 pmt[j] = preLen + 1。
     *              此时 j++ 处理下一个位置，preLen + 1，对于下一个位置来说，它的前一个位置的 pmt 值已经变成 preLen+1 了。
     *          那如果 P[j] 与 P[preLen] 不一样，又该怎么办？
     *          长度为 preLen 的子串 A(从头开始那部分) 和子串 B(j之前那部分) 是 P[0]~P[j-1] 中最长的公共前后缀。可惜 A 右边的字符和 B 右边的那个字符不相等，pmt[j]不能改成 preLen+1 了。
     *          因此，我们应该缩短这个 preLen，把它改成小一点的值，再来试试 P[j] 是否等于 P[preLen].　　
     *          preLen 该缩小到多少呢？显然，我们不想让 preLen 缩小太多。
     *          因此我们决定，在保持“P[0]~P[j-1]的 preLen-前缀仍然等于 preLen-后缀”的前提下，让这个新的 preLen 尽可能大一点。
     *          P[0]~P[j-1] 的公共前后缀，前缀一定落在串A里面、后缀一定落在串B里面。
     *          换句话讲：接下来 preLen 应该改成：使得 A的k-前缀等于B的k-后缀 的最大的k.　　
     *          您应该已经注意到了一个非常强的性质——串A和串B是相同的！B的后缀等于A的后缀！
     *          因此，使得A的k-前缀等于B的k-后缀的最大的k，其实就是串A的最长公共前后缀的长度 —— pmt[preLen-1]！
     *
     * 作者：阮行止
     * 链接：https://www.zhihu.com/question/21923021/answer/1032665486
     * 来源：知乎
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param pattern
     * @return
     */
    private int[] getNext(String pattern) {
        int len = pattern.length();
        int[] pmt = new int[len];
        // pmt[0]肯定是0，单个字符嘛，最长公共前后缀长度又不算自身，所以肯定是0
        pmt[0] = 0;
        // preMaxCommonFixLen 表示， 当前 位置 j 的 前一个位置 j-1 对应的 PMT 中的值，也就是 p[0...j-1] 的最长公共前后缀长度
        // 从 1 位置开始计算，这个j代表的是p[0...j]，那么 preMaxCommonFixLen = pmt[j-1] = pmt[0] = 0;
        int j = 1, preMaxCommonFixLen = 0;
        while (j < len) {
            // p[preLen] = p[j] 那么 对于 串p[0...j]来说，前后缀同时可以增加一个相同字符，那么它的最长公共前后缀长度 相当于 pmt[j-1] + 1
            if (pattern.charAt(j) == pattern.charAt(preMaxCommonFixLen)) {
                pmt[j] = preMaxCommonFixLen + 1;
                // j 后移，处理下一个位置
                j++;
                // 对于下一个位置来说，它的前一个位置的最长公共前后缀的长度应该+1
                preMaxCommonFixLen++;
            // p[preLen] != p[j]，缩小 preLen 的长度
            // 在保持“P[0]~P[j-1]的 preLen-前缀仍然等于 preLen-后缀”的前提下，让这个新的 preLen 尽可能大一点
            } else if (preMaxCommonFixLen > 0) {
                // pre = pmt[pre - 1]
                preMaxCommonFixLen = pmt[preMaxCommonFixLen - 1];
                // p[preLen] != p[j]，缩小 preLen 的长度，但是 preLen = 0，此时 pre 已经无法缩小，pmt[j] = 0
            } else {
                pmt[j] = 0;
                j++;
            }
        }
        return pmt;
    }

    /**
     * 字符串匹配算法，KMP。寻找 模式串p 在 串s 中第一次出现的位置
     * @param str
     * @param target
     * @return
     */
    public int search(String str, String target) {
        int m = str.length(), n = target.length();
        int[] pmt = getNext(target);
        int i = 0, j = 0;
        while (i < m && j < n) {
            // 当前对应位置字符相同
            if (str.charAt(i) == target.charAt(j)) {
                i++;
                j++;
            // s[i] != p[j], 主串不动，模式串回退之后，再去和 s[i]匹配，当然前提是 模式串能回退，也就是j>0
            } else if (j > 0) {
                j = pmt[j - 1];
            //  s[i] != p[j]，并且 j == 0, 和模式串第一个字符不等，那么 只能从主串下一个位置开始匹配
            } else {
                i++;
            }
        }
        // 如果 j 顺利走到末尾
        if (j == n) {
            // 此时主串停在i位置，匹配的长度就是模式串的长度j，那么起始位置就是 i - j
            return i - j;
        }
        return -1;

        // 如果是要找到所有出现的位置
        // while (i < m) {
        //     if (str.charAt(i) == target.charAt(j)) {
        //         i++;
        //         j++;
        //     } else if (j > 0) {
        //         j = pmt[j - 1];
        //     } else {
        //         i++;
        //     }
        //     if (j == n) {
        //         System.out.println(i - j + 1);
        //         j = pmt[j - 1];
        //     }
        // }
    }

    public static void main(String[] args) {
        KMP obj = new KMP();
        System.out.println(obj.search("ababcde", "bcd"));
        System.out.println(obj.search("abababc", "ababc"));
    }
}
