package com.daily;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/5/11 21:20
 * @description: _1016_BinaryStringWithSubstringRepresenting1ToN
 *
 * 1016. 子串能表示从 1 到 N 数字的二进制串
 * 给定一个二进制字符串 s 和一个正整数 n，如果对于 [1, n] 范围内的每个整数，其二进制表示都是 s 的 子字符串 ，就返回 true，否则返回 false 。
 *
 * 子字符串 是字符串中连续的字符序列。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "0110", n = 3
 * 输出：true
 * 示例 2：
 *
 * 输入：s = "0110", n = 4
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s[i] 不是 '0' 就是 '1'
 * 1 <= n <= 109
 * 通过次数21,735提交次数34,680
 */
public class _1016_BinaryStringWithSubstringRepresenting1ToN {

    /**
     * 枚举 + 优化
     *
     * 因为 n 是 int 类型，二进制表示最多 32 位
     *
     * 那么 s 有几个 长度为32 的子串，就 最多能表示 几个数字，
     *
     * s 长度为 32 的子串数量为 s.length() - 32 + 1 = m - 31
     *
     * 所以，如果 n >= m - 31，直接 返回 false
     *
     * 接下来，就可以判断 从 1 到 n 的每个数字的二进制串是否都是 s 的子串了 （n 在这里不超过 m-31，而题目中 m 最大为 1000）
     *
     * 另外，对于一个整数 x，如果 x 的二进制表示是 s 的子串，那么 ⌊x/2⌋ 的二进制表示也是 s 的子串。
     * （1xxxx 是 s 的子串，右移一位 xxxx 肯定也是 s 的子串）
     * 因此，我们只需要判断 [⌊n/2⌋+1,..n] 范围内的整数的二进制表示是否是 s 的子串即可。
     */
    public boolean queryString(String s, int n) {
        int m = s.length();
        //  s 有几个 长度为32 的子串，就 最多能表示 几个数字， s 长度为 32 的子串数量为 s.length() - 32 + 1 = m - 31
        // 如果 n >= m - 31，直接 返回 false，注意 判断大于 0
        if (m > 31 && n > m - 31) {
            return false;
        }
        // 判断 [⌊n/2⌋+1,..n] 范围内的整数的二进制表示是否是 s 的子串即可。
        for (int i = n; i > n / 2; i--) {
            if (!s.contains(Integer.toBinaryString(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 算法二
     * 反过来想，把 s 的子串都转成二进制数，如果数字在 [1,n] 内，就保存到一个哈希表中。
     * 如果哈希表的大小最终为 n，就说明 [1,n] 的二进制都在 s 里面。
     *
     * 代码实现时，设当前枚举的子串对应的下标区间为 [i,j]，手动把这段子串转成二进制数。
     * 这里的技巧是，设当前得到的二进制数为 x，且下一个字符 s[j+1] 为 c，
     * 那么将 x 更新为 (x << 1) | (c - '0')，从而 O(1) 地计算出子串 [i,j+1] 对应的的二进制数。
     *
     * 此外，可以跳过 s[i]=0 的情况。例如 s=0110，从 s[0] 开始和从 s[1] 开始，得到的二进制数都是一样的。
     * 并且，由于保证从 s[i]=1 开始枚举，二进制数的大小会指数增长，一旦 x>n，就停止枚举 j。
     * 所以对于固定的 i，至多枚举 O(logn) 个 j。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/binary-string-with-substrings-representing-1-to-n/solution/san-chong-suan-fa-cong-bao-li-dao-you-hu-nmtq/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param n
     * @return
     */
    public boolean queryString2(String s, int n) {
        int m = s.length();
        // 保存 s 所有子串得到的数字
        Set<Integer> seen = new HashSet<>();
        // 枚举子串起始位置
        for (int i = 0; i < m; ++i) {
            // 以 0 起始，不用统计，因为 0xxx 的结果 和 xxx 的结果一样
            if (s.charAt(i) == '0') {
                continue;
            }
            // 以 1 开始，x 代表 s[i, j] 代表的整数
            int x = 1;
            // [i, i] 的结果是 s[i]，就是 1
            int j = i + 1;
            // x 不能超过 n
            while (x <= n) {
                // 保存 x
                seen.add(x);
                // j 不能越界
                if (j == m) {
                    break;
                }
                // 由 [i, j - 1] 得到 [i, j]
                x = (x << 1) | (s.charAt(j++) - '0');
            }
        }
        // 最后，必须 n 个数字都表示出来了
        return seen.size() == n;
    }
}
