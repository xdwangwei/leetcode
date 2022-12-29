package com.daily;

/**
 * @author wangwei
 * @date 2022/12/29 16:33
 * @description: _1759_CountNumberOfHomogenousSubstrings
 *
 * 1759. 统计同构子字符串的数目
 * 给你一个字符串 s ，返回 s 中 同构子字符串 的数目。由于答案可能很大，只需返回对 109 + 7 取余 后的结果。
 *
 * 同构字符串 的定义为：如果一个字符串中的所有字符都相同，那么该字符串就是同构字符串。
 *
 * 子字符串 是字符串中的一个连续字符序列。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "abbcccaa"
 * 输出：13
 * 解释：同构子字符串如下所列：
 * "a"   出现 3 次。
 * "aa"  出现 1 次。
 * "b"   出现 2 次。
 * "bb"  出现 1 次。
 * "c"   出现 3 次。
 * "cc"  出现 2 次。
 * "ccc" 出现 1 次。
 * 3 + 1 + 2 + 1 + 3 + 2 + 1 = 13
 * 示例 2：
 *
 * 输入：s = "xy"
 * 输出：2
 * 解释：同构子字符串是 "x" 和 "y" 。
 * 示例 3：
 *
 * 输入：s = "zzzzz"
 * 输出：15
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 105
 * s 由小写字符串组成
 * 通过次数22,716提交次数44,353
 */
public class _1759_CountNumberOfHomogenousSubstrings {

    /**
     * 方法一：数学
     * 思路与算法
     *
     * 题目给出一个长度为 n 的字符串 s，
     * 并给出「同构字符串」的定义为：如果一个字符串中的所有字符都相同，那么该字符串就是同构字符串。
     *
     * 现在我们需要求 ss 中「同构子字符串」的数目。
     *
     * 因为「同构子字符串」为一个连续的字符序列且需要序列中的字符都相同，那么我们首先按照连续相同的字符来对字符串 s 进行分组，
     * 比如对于字符串 “abbcccaa" 我们的分组结果为 [a,bb,ccc,aa]。\
     * 因为对于一个组中字符串的任意子字符串都为「同构子字符串」，而一个长度为 m 的字符串的子字符串的数目为 m×(m+1)/2。
     *      比如：m个a组成的字符串，里面包括：
     *          m个a
     *          m-1个 aa
     *          m-2个 aaa
     *          ...
     *          1个   aaaaaa....
     *          共 1+2+3+...+m = m(m+1)/2
     * 那么我们对于每一个组来统计其贡献的「同构子字符串」数目并求和即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/count-number-of-homogenous-substrings/solution/tong-ji-tong-gou-zi-zi-fu-chuan-de-shu-m-tw5m/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int countHomogenous(String s) {
        final int MOD = 1000000007;
        // 注意数据范围
        long res = 0;
        // 按照连续相同的字符来对字符串 s 进行分组
        // prev标记当前组的字符
        char prev = s.charAt(0);
        // cnt记录当前组的长度
        int cnt = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // 当前组内字符数+1
            if (c == prev) {
                cnt++;
            } else {
                // 另开一组，累计上一组的贡献值
                res += (long) (cnt + 1) * cnt / 2;
                // 更新 cnt 为新一组当前统计到的长度
                cnt = 1;
                // 更新 prev 为新一组的字符
                prev = c;
            }
        }
        // 记得累加上for循环结束时最后一组的贡献值
        res += (long) (cnt + 1) * cnt / 2;
        // 对mod取模，再转为int
        return (int) (res % MOD);
    }
}
