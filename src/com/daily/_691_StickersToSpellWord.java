package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/14 18:16
 * @description: _691_StickersToSpellWord
 *
 * 691. 贴纸拼词
 * 我们有 n 种不同的贴纸。每个贴纸上都有一个小写的英文单词。
 *
 * 您想要拼写出给定的字符串 target ，方法是从收集的贴纸中切割单个字母并重新排列它们。如果你愿意，你可以多次使用每个贴纸，每个贴纸的数量是无限的。
 *
 * 返回你需要拼出 target 的最小贴纸数量。如果任务不可能，则返回 -1 。
 *
 * 注意：在所有的测试用例中，所有的单词都是从 1000 个最常见的美国英语单词中随机选择的，并且 target 被选择为两个随机单词的连接。
 *
 *
 *
 * 示例 1：
 *
 * 输入： stickers = ["with","example","science"], target = "thehat"
 * 输出：3
 * 解释：
 * 我们可以使用 2 个 "with" 贴纸，和 1 个 "example" 贴纸。
 * 把贴纸上的字母剪下来并重新排列后，就可以形成目标 “thehat“ 了。
 * 此外，这是形成目标字符串所需的最小贴纸数量。
 * 示例 2:
 *
 * 输入：stickers = ["notice","possible"], target = "basicbasic"
 * 输出：-1
 * 解释：我们不能通过剪切给定贴纸的字母来形成目标“basicbasic”。
 *
 *
 * 提示:
 *
 * n == stickers.length
 * 1 <= n <= 50
 * 1 <= stickers[i].length <= 10
 * 1 <= target <= 15
 * stickers[i] 和 target 由小写英文单词组成
 */
public class _691_StickersToSpellWord {

    /**
     * 相当于每一次可以任意选择1张贴纸，这张贴纸上的部分字符可以为凑出target做出贡献，问至少需要几张贴纸，才能凑出target
     */

    int n;

    int[] memo;


    /**
     * 方法一：dfs
     * 需要考虑的是，对于任意一张贴纸，使用当前贴纸后(假设每个字符都做出最大贡献)，能够得到什么样的字符串
     * 我们最终要得到target，所以我们可以 用 0 或 1表示 target 每一位置上的字符是否已经凑出，因为 target长度在1-15之间，
     * 所以我们可以用1个int变量state完成这件事，
     * 初始时：state = 0，每个字符都没凑出来
     * 如果能凑出target，最终 state就是 2^(target.length)-1，也就是 target.length()个1，表示每个位置都被凑到了
     * 为了避免某些状态重复搜索，我们还需要一个memo数组来做备忘录，因为一共有 2^(target.length) 个状态（state取值为0-1<<len-1），所以数组大小为 1<< len
     *
     * 那么，假设当前状态为state，选择 某个 贴纸 sticker时，能得到什么样的新状态
     *
     * 遍历 sticker 的每个字符 s[i]
     *      遍历 target 的每个字符 t[j]
     *          if (s[i] == t[j] && state & (1 << j) == 0) 说明此时t[j]还未被凑出，并且s[i] 能凑出 t[j]，那么state第j个位置变为1
     *          break；s[i]对当前字符做出贡献后，就该考虑s[i+1]为哪个位置做贡献了，并且因为state已经更新，所以不会出现s[i+1]仍然为t[j]贡献的情况
     *                  这里不要先统计s每个字符出现次数，再统计t每个字符出现次数，又逐一比较，更新，这样是做不到明确target每个位置是否被凑出的，也就是得不到新的state
     * 因为每次可以选择任意一个贴纸，所以 遍历s前要准备一个 state的备份nstate，在遍历过程中使用的也是nstate
     * 并且，如果遍历完成后，nstate==state，说明当前贴纸对当前状态已再无贡献，直接换下一个贴纸
     * 否则·从当前状态 到 target 所需要的贴纸数 就是 1(使用当前贴纸) + dfs(nstate)
     *
     * 然后从多个选择的结果中选择结果最小的（所以，先给一个不可能的结果）
     *
     * 并且，当 state == 1<<n-1时，返回0，
     *
     *
     * @param stickers
     * @param target
     * @return
     */
    public int minStickers(String[] stickers, String target) {
        // 记录target长度
        n = target.length();
        // 备忘录，注意初始化大小
        memo = new int[1 << n];
        // 标记备忘录
        Arrays.fill(memo, -1);
        // 如果凑不出来target，返回的是个不可能的值，这里用 n + 1代替
        int ans = dfs(stickers, target, 0);
        return ans > n ? - 1: ans;
    }

    /**
     * dfs，当前状态是 state（二进制第j位为1代表target[j]已经凑出）
     * 返回从当前状态凑出target需要的最少贴纸数
     * @param stickers
     * @param target
     * @param state
     * @return
     */
    private int dfs(String[] stickers, String target, int state) {
        // target每个字符全部凑出
        if (state == (1 << n) - 1) {
            return 0;
        }
        // 已计算过
        if (memo[state] != -1) {
            return memo[state];
        }
        // 可以选择任意一张贴纸，选择结果最小的选择，先给个不可能的值
        int ans = n + 1;
        // 任意选一张贴纸
        for (String sticker : stickers) {
            // 先做state的备份
            int nstate = state;
            // 考虑贴纸s每个字符能做出的贡献
            for (char c: sticker.toCharArray()) {
                for (int j = 0; j < n; ++j) {
                    // 如果s[i]恰好能凑上t[j]（前提是t[j]还未被凑出，注意这里用的是nstate）
                    if (c == target.charAt(j) && ((nstate >> j) & 1) == 0) {
                        // 标记t[j]被凑出
                        nstate |= (1 << j);
                        // 考虑是[i+1]做贡献
                        break;
                    }
                }
            }
            // 的确做出了贡献
            if (nstate != state) {
                // 选择当前贴纸，完成整个过程所需要的最小贴纸数
                // 多个选择中选最优
                ans = Math.min(ans, 1 + dfs(stickers, target, nstate));
            }
        }
        // 更新备忘录并返回
        memo[state] = ans;
        return ans;
    }


    /**
     * 思路不变，使用 bfs, 每次可以 选择任意一张贴纸。选择不同贴纸就会得到不同邻接状态
     * 按照 bfs 【齐头并进】特点，最先 达到 终极状态 (1<<n-1)时，返回 step(看作多叉树数层序遍历的话，对应最小的叶子节点层数)即可
     * @param stickers
     * @param target
     * @return
     */
    public int minStickers2(String[] stickers, String target) {
        n = target.length();
        return bfs(stickers, target);
    }

    /**
     * bfs，当前状态是 state（二进制第j位为1代表target[j]已经凑出）
     *
     * 每个节点 有 stickers.length 种邻接选项，选择不同贴纸，得到不同邻接状态
     *
     * @param stickers
     * @param target
     * @return
     */
    private int bfs(String[] stickers, String target) {
        int step = 0;
        Deque<Integer> queue = new ArrayDeque<>();
        // bfs不需要备忘录，但要避免节点重复访问
        Set<Integer> set = new HashSet<>();
        // 初始状态
        queue.offer(0);
        set.add(0);
        while (!queue.isEmpty()) {
            // 当前层 节点个数
            int sz = queue.size();
            // 逐个取出当前层节点，找到他们的邻接点，加入下一层队列
            for (int i = 0; i < sz; ++i) {
                Integer state = queue.poll();
                // 发现目标状态，返回 step
                if (state == (1 << n) - 1) {
                    return step;
                }
                // 找寻全部邻接点
                // 任意选一张贴纸
                for (String sticker : stickers) {
                    // 先做state的备份
                    int nstate = state;
                    // 考虑贴纸s每个字符能做出的贡献
                    for (char c: sticker.toCharArray()) {
                        for (int j = 0; j < n; ++j) {
                            // 如果s[i]恰好能凑上t[j]（前提是t[j]还未被凑出，注意这里用的是nstate）
                            if (c == target.charAt(j) && ((nstate >> j) & 1) == 0) {
                                // 标记t[j]被凑出
                                nstate |= (1 << j);
                                // 考虑是[i+1]做贡献
                                break;
                            }
                        }
                    }
                    // 的确做出了贡献 并且 邻接状态没访问过
                    if (nstate != state && !set.contains(nstate)) {
                        // 邻接状态入队列
                        queue.offer(nstate);
                        // 标记邻接状态已访问
                        set.add(nstate);
                    }
                }
            }
            // 齐头并进
            step++;
        }
        // 不在bfs过程中返回，就是无法凑出target
        return -1;
    }
}
