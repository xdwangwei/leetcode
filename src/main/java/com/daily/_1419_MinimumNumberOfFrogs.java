package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/5/6 21:10
 * @description: _1419_MinimumNumberOfFrogs
 * <p>
 * 1419. 数青蛙
 * 给你一个字符串 croakOfFrogs，它表示不同青蛙发出的蛙鸣声（字符串 "croak" ）的组合。由于同一时间可以有多只青蛙呱呱作响，所以 croakOfFrogs 中会混合多个 “croak” 。
 * <p>
 * 请你返回模拟字符串中所有蛙鸣所需不同青蛙的最少数目。
 * <p>
 * 要想发出蛙鸣 "croak"，青蛙必须 依序 输出 ‘c’, ’r’, ’o’, ’a’, ’k’ 这 5 个字母。如果没有输出全部五个字母，那么它就不会发出声音。如果字符串 croakOfFrogs 不是由若干有效的 "croak" 字符混合而成，请返回 -1 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：croakOfFrogs = "croakcroak"
 * 输出：1
 * 解释：一只青蛙 “呱呱” 两次
 * 示例 2：
 * <p>
 * 输入：croakOfFrogs = "crcoakroak"
 * 输出：2
 * 解释：最少需要两只青蛙，“呱呱” 声用黑体标注
 * 第一只青蛙 "crcoakroak"
 * 第二只青蛙 "crcoakroak"
 * 示例 3：
 * <p>
 * 输入：croakOfFrogs = "croakcrook"
 * 输出：-1
 * 解释：给出的字符串不是 "croak" 的有效组合。
 * <p>
 * <p>
 * 提示：
 * <p>
 * 1 <= croakOfFrogs.length <= 105
 * 字符串中的字符只有 'c', 'r', 'o', 'a' 或者 'k'
 * 通过次数26,581提交次数53,841
 */
public class _1419_MinimumNumberOfFrogs {

    /**
     *
     * 方法：计数 + 贪心
     *
     * 将青蛙分成 5 种：
     *
     * 刚才发出了 c 的声音。
     * 刚才发出了 r 的声音。
     * 刚才发出了 o 的声音。
     * 刚才发出了 a 的声音。
     * 刚才发出了 k 的声音。
     *
     * 分别记录这5种青蛙的个数
     *
     * 遍历 croakOfFrogs，例如当前遍历到 r，那么就看看有没有青蛙刚才发出了 c 的声音，如果有，那么让它接着发出 r 的声音。
     *
     * 这启发我们使用一个哈希表（数组）cnt 来维护这 5 种青蛙的个数，并分类讨论：
     *
     * 遍历到 'c' 时，看看有没有青蛙刚才发出了 'k' 的声音，
     *      如果有，那么复用（贪心）这只青蛙，让它接着发出 c 的声音，
     *              此时 cnt['k']-- 和 cnt['c']++；发出 'k' 的青蛙少一只，发出 'a' 的青蛙多一只
     *      如果没有这种青蛙（cnt['k']），那么新增一只青蛙发出 c 的声音，即 cnt['c']++。
     * 遍历到 'r' 时，看看有没有青蛙刚才发出了 'c' 的声音，
     *      如果有，那么复用这只青蛙，让它接着发出 r 的声音，
     *          即 cnt['c']-- 和 cnt['r']++；
     *     如果没有这种青蛙，由于题目要求青蛙必须从 'c' 开始蛙鸣，不能直接从 'r' 开始，所以返回 −1。
     * 遍历到 'o','a','k' 的情况类似 'r'，
     *      找到该字母在 croak 的上一个字母的 cnt 值，
     *      如果 cnt 值大于 0，那么将其减一，同时当前字母的 cnt 值加一；
     *      如果上一个字母的 cnt 值等于 0，那么就返回 −1。
     *
     * 遍历结束后，所有青蛙必须在最后发出 'k' 的声音，如果有青蛙在最后发出的声音不是 ’k‘（也就是 cnt 值大于 0），那么返回 −1，
     * 否则返回 cnt[k]。（贪心策略复用青蛙保证了这种情况下的 cnt[’k‘] 就是所需最少的青蛙）
     *
     * 代码实现时，可以用一个哈希表（数组）PREVIOUS 记录 croak 中的每个字母的上一个字母，从而避免写出大量 if-else。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/minimum-number-of-frogs-croaking/solution/bie-xiang-tai-fu-za-mo-ni-ti-ba-liao-pyt-9t87/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    private static final Map<Character, Character> prevMap = new HashMap<>() {{
        // 记录当前字符和上一个字符的对应关系
        put('c', 'k');
        put('r', 'c');
        put('o', 'r');
        put('a', 'o');
        put('k', 'a');
    }};

    public int minNumberOfFrogs(String croakOfFrogs) {
        // 记录每个字符的出现次数（刚刚发完字符c的青蛙个数）
        int[] cnt = new int[26];
        // 顺序遍历
        for (char c : croakOfFrogs.toCharArray()) {
            // 当前字符c的前一个字符pre
            char pre = prevMap.get(c);
            // 如果当前字符是 'c'，（首字符）
            if (c == 'c') {
                // 如果有青蛙刚发完 'k'，让它重新开始，发 'c'
                if (cnt[pre - 'a'] > 0) {
                    // 发 'k' 的青蛙数 减1
                    cnt[pre - 'a']--;
                }
                // 发 'c' 的青蛙数 +1，
                cnt[c - 'a']++;
            } else {
                // 当前字符是 'roak'
                // 没有青蛙发出前一个字符，此时不可能发c，返回-1
                if (cnt[pre - 'a'] == 0) {
                    return -1;
                } else {
                    // 随便取一个青蛙，让它继续发c，然后 pre的青蛙数减1，c的青蛙数+1
                    cnt[pre - 'a']--;
                    cnt[c - 'a']++;
                }
            }
        }
        // 最后，必然要满足每个青蛙都发声到 'k'，否则返回-1
        if (cnt['c' - 'a'] > 0 || cnt['r' - 'a'] > 0 || cnt['o' - 'a'] > 0 || cnt['a' - 'a'] > 0) {
            return -1;
        }
        // 返回 发 'k' 的青蛙个数
        return cnt['k' - 'a'];
    }
}
