package com.daily;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangwei
 * 2022/4/19 12:37
 *
 * 821. 字符的最短距离
 * 给你一个字符串 s 和一个字符 c ，且 c 是 s 中出现过的字符。
 *
 * 返回一个整数数组 answer ，其中 answer.length == s.length 且 answer[i] 是 s 中从下标 i 到离它 最近 的字符 c 的 距离 。
 *
 * 两个下标 i 和 j 之间的 距离 为 abs(i - j) ，其中 abs 是绝对值函数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "loveleetcode", c = "e"
 * 输出：[3,2,1,0,1,0,0,1,2,2,1,0]
 * 解释：字符 'e' 出现在下标 3、5、6 和 11 处（下标从 0 开始计数）。
 * 距下标 0 最近的 'e' 出现在下标 3 ，所以距离为 abs(0 - 3) = 3 。
 * 距下标 1 最近的 'e' 出现在下标 3 ，所以距离为 abs(1 - 3) = 2 。
 * 对于下标 4 ，出现在下标 3 和下标 5 处的 'e' 都离它最近，但距离是一样的 abs(4 - 3) == abs(4 - 5) = 1 。
 * 距下标 8 最近的 'e' 出现在下标 6 ，所以距离为 abs(8 - 6) = 2 。
 * 示例 2：
 *
 * 输入：s = "aaab", c = "b"
 * 输出：[3,2,1,0]
 *
 *
 * 提示：
 * 1 <= s.length <= 104
 * s[i] 和 c 均为小写英文字母
 * 题目数据保证 c 在 s 中至少出现一次
 */
public class _821_ShortestDistanceToACharacter {

    /**
     * 方法一：找到 c 在 s 中出现的全部位置，list[x,....y]
     * 对于 S[i] ，如果 s[i] == c，那么 res[i] = 0
     *            如果 i < x; 那么 res[i] = x - i
     *            如果 i > y；那么 res[i] = i - y
     *            否则 ： res[i] = min(abs(list(j) - i))
     * 效率太低下
     * @param s
     * @param c
     * @return
     */
    public int[] shortestToChar(String s, char c) {
        int len = s.length();
        int[] res = new int[len];
        Matcher matcher = Pattern.compile("" + c).matcher(s);
        List<Integer> indexes = new ArrayList<>();
        while (matcher.find()) {
            indexes.add(matcher.start());
        }
        for (int i = 0; i < len; ++i) {
            if (s.charAt(i) == c) {
                res[i] = 0;
            } else if (i < indexes.get(0)) {
                res[i] = indexes.get(0) - i;
            } else if (i > indexes.get(indexes.size() - 1)) {
                res[i] = i - indexes.get(indexes.size() - 1);
            } else {
                int min = 99999;
                for (Integer index : indexes) {
                    min = Math.min(min, Math.abs(index - i));
                }
                res[i] = min;
            }
        }
        return res;
    }

    /**
     * 方法二：两次遍历
     *
     * 问题可以转换成，对 s 的每个下标 i，求
     *      s[i] 到其左侧最近的字符 c 的距离
     *      s[i] 到其右侧最近的字符 c 的距离
     * 这两者的最小值。
     *
     * 对于前者，我们可以从左往右遍历 s，若 s[i]=c 则记录下此时字符 c 的的下标 idx。遍历的同时更新 answer[i]=i−idx。
     *
     * 对于后者，我们可以从右往左遍历 s，若 s[i]=c 则记录下此时字符 c 的的下标 idx。遍历的同时更新 answer[i]=min(answer[i],idx−i)。
     *
     * 代码实现时，在开始遍历的时候 idx 可能不存在，为了简化逻辑，我们可以分别用 −n 和 2n 初始化最左边和最右边的c的位置，这里 n 是 s 的长度。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/shortest-distance-to-a-character/solution/zi-fu-de-zui-duan-ju-chi-by-leetcode-sol-2t49/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param c
     * @return
     */
    public int[] shortestToChar2(String s, char c) {
        int len = s.length();
        int[] res = new int[len];
        // 从左往右遍历，每个字符记录离他最近的左边那个c的距离
        // 初始虚拟一个最左边的c在-n位置
        for (int i = 0, idx = -len; i < len; ++i) {
            // 这样就保证，下面距离计算的是最近遇到的c，并且 下面 i - idx一定是大于等于0
            if (s.charAt(i) == c) {
                idx = i;
            }
            res[i] = i - idx;
        }
        // 从右往左遍历，每个字符记录离他最近的右边那个c的距离
        // 初始虚拟一个最右边的c在2n位置
        for (int i = len - 1, idx = 2 * len; i >= 0; --i) {
            // 这样就保证，下面距离计算的是最近遇到的c，并且 下面 idx - i一定是大于等于0
            if (s.charAt(i) == c) {
                idx = i;
            }
            // idx - i 是 s[i] 到它右边最近的c的距离，
            // 可以直接和之前到左边最近的c的距离比较，取最小值，得到结果
            res[i] = Math.min(res[i], idx - i);
        }
        return res;
    }


    /**
     * 方法三：BFS 妙啊！
     * 起始令所有的 ans[i]=−1，然后将所有的 c 字符的下标入队，并更新 ans[i]=0，
     * 然后跑一遍 BFS 逻辑，通过 ans[i] 是否为 -1 来判断是否重复入队。
     *
     * 刚开始 队列中 全是对应位置字符是c的下标，每个下标的邻接点就是左右位置，如果 左右位置 对应的res 是 - 1， 就将其更新为 自己的位置加1，同时这个位置 入队列去更新它的左右邻居
     * 因为c节点队列其他节点之前，所以每个节点肯定是先被c字符节点更新，然后更新后res!=-1就不会被队列中其他节点更新，就保证了每个节点的res是离他最近的c的距离
     *
     * 作者：AC_OIer
     * 链接：https://leetcode-cn.com/problems/shortest-distance-to-a-character/solution/by-ac_oier-5bjs/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param c
     * @return
     */
    public int[] shortestToChar3(String s, char c) {
        int n = s.length();
        int[] ans = new int[n];
        // 初始化为全部未求解
        Arrays.fill(ans, -1);
        Deque<Integer> d = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            // c字符对应位置
            if (s.charAt(i) == c) {
                // 入队列
                d.addLast(i);
                // 距离是0
                ans[i] = 0;
            }
        }
        // 每个节点的左右邻居
        int[] dirs = new int[]{-1, 1};
        while (!d.isEmpty()) {
            int t = d.pollFirst();
            for (int di : dirs) {
                int ne = t + di;
                // 这个邻居没有求解，我给它更新一下
                if (ne >= 0 && ne < n && ans[ne] == -1) {
                    ans[ne] = ans[t] + 1;
                    // 它入队列，去更新它的邻居
                    d.addLast(ne);
                }
            }
        }
        return ans;
    }


    public static void main(String[] args) {
        _821_ShortestDistanceToACharacter obj = new _821_ShortestDistanceToACharacter();
        obj.shortestToChar("abcdabcdaaagc", 'a');
    }
}
