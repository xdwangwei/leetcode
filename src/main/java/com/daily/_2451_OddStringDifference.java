package com.daily;

import java.awt.desktop.AboutHandler;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/5/25 21:17
 * @description: _2451_OddStringDifference
 *
 * 2451. 差值数组不同的字符串
 * 给你一个字符串数组 words ，每一个字符串长度都相同，令所有字符串的长度都为 n 。
 *
 * 每个字符串 words[i] 可以被转化为一个长度为 n - 1 的 差值整数数组 difference[i] ，其中对于 0 <= j <= n - 2 有 difference[i][j] = words[i][j+1] - words[i][j] 。注意两个字母的差值定义为它们在字母表中 位置 之差，也就是说 'a' 的位置是 0 ，'b' 的位置是 1 ，'z' 的位置是 25 。
 *
 * 比方说，字符串 "acb" 的差值整数数组是 [2 - 0, 1 - 2] = [2, -1] 。
 * words 中所有字符串 除了一个字符串以外 ，其他字符串的差值整数数组都相同。你需要找到那个不同的字符串。
 *
 * 请你返回 words中 差值整数数组 不同的字符串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：words = ["adc","wzy","abc"]
 * 输出："abc"
 * 解释：
 * - "adc" 的差值整数数组是 [3 - 0, 2 - 3] = [3, -1] 。
 * - "wzy" 的差值整数数组是 [25 - 22, 24 - 25]= [3, -1] 。
 * - "abc" 的差值整数数组是 [1 - 0, 2 - 1] = [1, 1] 。
 * 不同的数组是 [1, 1]，所以返回对应的字符串，"abc"。
 * 示例 2：
 *
 * 输入：words = ["aaa","bob","ccc","ddd"]
 * 输出："bob"
 * 解释：除了 "bob" 的差值整数数组是 [13, -13] 以外，其他字符串的差值整数数组都是 [0, 0] 。
 *
 *
 * 提示：
 *
 * 3 <= words.length <= 100
 * n == words[i].length
 * 2 <= n <= 20
 * words[i] 只含有小写英文字母。
 * 通过次数17,327提交次数26,252
 */
public class _2451_OddStringDifference {

    /**
     * 方法一：哈希表模拟
     *
     * 我们用哈希表 d 维护字符串的差值数组和字符串的映射关系，<差值，字符串[]>
     *
     * 其中差值数组为字符串的相邻字符的差值构成的数组（可以拼成一个字符串）。
     *
     * 由于题目保证了除了一个字符串以外，其他字符串的差值数组都相同，因此我们只需要找到差值数组不同的字符串即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/odd-string-difference/solution/python3javacgo-yi-ti-yi-jie-ha-xi-biao-m-qoji/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @return
     */
    public String oddString(String[] words) {
        // 维护字符串的差值数组和字符串的映射关系，<差值，字符串[]>
        Map<String, List<String>> d = new HashMap<>();
        // 遍历字符串
        for (String s : words) {
            int m = s.length();
            var cs = new char[m - 1];
            // 得到其差值数组
            for (int i = 0; i < m - 1; ++i) {
                cs[i] = (char) (s.charAt(i + 1) - s.charAt(i));
            }
            // 差值数组转为string作为key
            String t = String.valueOf(cs);
            // key对应的字符串列表加入当前字符串
            d.computeIfAbsent(t, k -> new ArrayList<>()).add(s);
        }
        // 遍历映射关系
        for (List<String> ss : d.values()) {
            // 某个key只有1个字符串，那么它就是不同的字符串，list中只有1个元素，直接返回 get(0)
            if (ss.size() == 1) {
                return ss.get(0);
            }
        }
        // 题目保证一定存在，不会到达此处
        return null;
    }
}
