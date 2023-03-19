package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/3/19 14:25
 * @description: _1625_Smallest_String_After_Operations
 *
 * 1625. 执行操作后字典序最小的字符串
 * 给你一个字符串 s 以及两个整数 a 和 b 。其中，字符串 s 的长度为偶数，且仅由数字 0 到 9 组成。
 *
 * 你可以在 s 上按任意顺序多次执行下面两个操作之一：
 *
 * 累加：将  a 加到 s 中所有下标为奇数的元素上（下标从 0 开始）。数字一旦超过 9 就会变成 0，如此循环往复。例如，s = "3456" 且 a = 5，则执行此操作后 s 变成 "3951"。
 * 轮转：将 s 向右轮转 b 位。例如，s = "3456" 且 b = 1，则执行此操作后 s 变成 "6345"。
 * 请你返回在 s 上执行上述操作任意次后可以得到的 字典序最小 的字符串。
 *
 * 如果两个字符串长度相同，那么字符串 a 字典序比字符串 b 小可以这样定义：在 a 和 b 出现不同的第一个位置上，字符串 a 中的字符出现在字母表中的时间早于 b 中的对应字符。例如，"0158” 字典序比 "0190" 小，因为不同的第一个位置是在第三个字符，显然 '5' 出现在 '9' 之前。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "5525", a = 9, b = 2
 * 输出："2050"
 * 解释：执行操作如下：
 * 初态："5525"
 * 轮转："2555"
 * 累加："2454"
 * 累加："2353"
 * 轮转："5323"
 * 累加："5222"
 * 累加："5121"
 * 轮转："2151"
 * 累加："2050"
 * 无法获得字典序小于 "2050" 的字符串。
 * 示例 2：
 *
 * 输入：s = "74", a = 5, b = 1
 * 输出："24"
 * 解释：执行操作如下：
 * 初态："74"
 * 轮转："47"
 * 累加："42"
 * 轮转："24"
 * 无法获得字典序小于 "24" 的字符串。
 * 示例 3：
 *
 * 输入：s = "0011", a = 4, b = 2
 * 输出："0011"
 * 解释：无法获得字典序小于 "0011" 的字符串。
 * 示例 4：
 *
 * 输入：s = "43987654", a = 7, b = 3
 * 输出："00553311"
 *
 *
 * 提示：
 *
 * 2 <= s.length <= 100
 * s.length 是偶数
 * s 仅由数字 0 到 9 组成
 * 1 <= a <= 9
 * 1 <= b <= s.length - 1
 */
public class _1625_Smallest_String_After_Operations {

    /**
     * 由于字符串长度不会超过100，可以考虑暴力搜索
     *
     * 方法一：BFS
     *
     * 本题数据规模较小，我们可以考虑使用 BFS 暴力搜索所有可能的状态，然后取字典序最小的状态即可。
     *
     * 我们定义队列 q，初始时将字符串 s 入队，定义一个哈希表 vis，用于记录字符串是否已经出现过，另外定义一个字符串 ans，用于记录答案。
     *
     * 然后，我们不断从队列中取出字符串，将其与答案 ans 进行比较，如果当前字符串字典序更小，则更新答案。
     *
     * 然后我们对该字符串进行累加和轮转操作，得到新的字符串，如果新的字符串没有出现过，则将其入队，并更新 vis。
     *
     * 一直重复上述操作，直到队列为空。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/lexicographically-smallest-string-after-applying-operations/solution/python3javacgo-yi-ti-shuang-jie-bfs-bao-xl8n2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param a
     * @param b
     * @return
     */
    public String findLexSmallestString(String s, int a, int b) {
        // bfs
        // 队列
        Deque<String> q = new ArrayDeque<>();
        // 已访问元素集合
        Set<String> vis = new HashSet<>();
        // 起点入队列、入集合
        q.offer(s);
        vis.add(s);
        // 最小字典序字符串，初始化为s本身
        String ans = s;
        // bfs
        while (!q.isEmpty()) {
            // 出队列
            String cur = q.poll();
            // 比较并更新ans
            if (ans.compareTo(cur) > 0) {
                ans = cur;
            }
            // 轮转操作得到新的字符串s1
            int idx = cur.length() - b;
            String s1 = cur.substring(idx) + cur.substring(0, idx);
            // 累加操作得到新的字符串s1
            char[] chars = cur.toCharArray();
            // 只累加奇数位置
            for (int i = 1; i < chars.length; i += 2) {
                chars[i] = (char) (((chars[i] - '0' + a) % 10) + '0');
            }
            String s2 = String.valueOf(chars);
            // 如果累加和轮转操作得到新的字符串，没有访问过，则将其入队，并加入 vis。
            for (String t : List.of(s1, s2)) {
                if (vis.add(t)) {
                    q.offer(t);
                }
            }
        }
        // 返回
        return ans;
    }
}
