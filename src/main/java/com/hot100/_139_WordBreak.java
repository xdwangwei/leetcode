package com.hot100;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * 2022/4/19 16:00
 *
 * 139. 单词拆分
 * 给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出 s 。
 *
 * 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
 *
 *
 *
 * 示例 1：
 *
 * 输入: s = "leetcode", wordDict = ["leet", "code"]
 * 输出: true
 * 解释: 返回 true 因为 "leetcode" 可以由 "leet" 和 "code" 拼接成。
 * 示例 2：
 *
 * 输入: s = "applepenapple", wordDict = ["apple", "pen"]
 * 输出: true
 * 解释: 返回 true 因为 "applepenapple" 可以由 "apple" "pen" "apple" 拼接成。
 *      注意，你可以重复使用字典中的单词。
 * 示例 3：
 *
 * 输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
 * 输出: false
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 300
 * 1 <= wordDict.length <= 1000
 * 1 <= wordDict[i].length <= 20
 * s 和 wordDict[i] 仅有小写英文字母组成
 * wordDict 中的所有字符串 互不相同
 * 通过次数281,167提交次数531,809
 */
public class _139_WordBreak {

    /**
     * 题目：wordDict只单词不重复
     * 回溯
     *
     * 核心是，每次选择这样一个单词，它在s中对应位置对应长度一样。
     * 比如第一次选择 w1, 长度是2，那么 保证 s[0-2] == w1
     * 第二次选则 w2, 长度是3， 那么 保证   s[3-6] == w2
     * 每次在之前基础上考虑一个单词的长度部分匹配
     * 这样的话，我们不用维护当前拼凑出的字符串，因为我们知道每次匹配时前面部分肯定是成功的，我们只需要考虑当前一个单词的长度部分
     * 但这样未能通过全部测试用例
     *
     * 为了解决超时，需要 加入 备忘录，那么 备忘录的key肯定是当前拼凑出的字符串，value是当前状态最终是否成功
     * 但是我们记录的是已匹配到的字符长度，那么既然前半部分都匹配，那我直接用这个数字来做key是一样的吧，表示 这些部分匹配完成后，后续是否能成功
     * @param s
     * @param wordDict
     * @return
     */
    Map<Integer, Boolean> memo;
    public boolean wordBreak(String s, List<String> wordDict) {
        memo = new HashMap<>();
        // 初始匹配长度是0
        return backTrack(s, wordDict, 0);
    }

    /**
     * 回溯
     * @param s 目标字符串
     * @param wordDict 可选的单词列表
     * @param matchedLenth 当前已经匹配到的s的长度
     * @return
     */
    private boolean backTrack(String s, List<String> wordDict, int matchedLenth) {
        // 当前状态已出现过，直接返回结果
        if (memo.containsKey(matchedLenth)) {
            return memo.get(matchedLenth);
        }
        // 匹配完成，记录并返回
        if (matchedLenth == s.length()) {
            memo.put(matchedLenth, true);
            return true;
        }
        // 多个选择
        boolean res = false;
        for (String w : wordDict) {
            // 提前剪枝，必须保证 当前这个单词 和 s中的 对应位置一样
            // if (matchedLenth + w.length() > s.length() || !s.substring(matchedLenth, matchedLenth + w.length()).equals(w)) {
            // 简化写法
            if (((matchedLenth + w.length()) > s.length()) || !s.startsWith(w, matchedLenth)) {
                continue;
            }
            // 下一层，已匹配长度增加
            res |= backTrack(s, wordDict, matchedLenth + w.length());
            if (res) {
                break;
            }
        }
        // 保存当前状态结果
        memo.put(matchedLenth, res);
        return res;
    }

    /**
     * 考虑上面的回溯想法，按顺序，每次考虑一个单词的匹配，然后去完成s剩下部分的拼凑，那不就是 把 s 分成两部分，第一部分是个列表单词，第二部分就是重新调用这个函数？
     * 动态规划
     * 方法3：动态规划
     * s串能否分解为单词表的单词（前 s.length 个字符的 s 串能否分解为单词表单词）
     *
     * 将大问题分解为规模小一点的子问题：
     *      前 i 个字符的子串，能否分解成单词；剩余子串，是否为单个单词。
     * dp[i]：长度为i的s[0:i-1]子串是否能拆分成单词。题目求: dp[s.length]
     *
     *
     * 状态转移方程
     *      类似的，我们用指针 j 去划分s[0:i] 子串：
     *          s[0:i] 子串对应 dp[i+1] ，它是否为 true（s[0:i]能否 break），取决于两点：
     *          它的前缀子串 s[0:j-1] 的 dp[j]，是否为 true。
     *          剩余子串 s[j:i]，是否是单词表的单词。
     *
     * base case
     * base case 为dp[0] = true。即，长度为 0 的s[0:-1]能拆分成单词表单词。
     * 这看似荒谬，但这只是为了让边界情况也能套用状态转移方程，而已。
     * 当 j = 0 时，s[0:i]的dp[i+1]，取决于s[0:-1]的dp[0]，和，剩余子串s[0:i]是否是单个单词。
     * 只有让dp[0]为真，dp[i+1]才会只取决于s[0:i]是否为单个单词，才能用上这个状态转移方程。
     *
     * 作者：xiao_ben_zhu
     * 链接：https://leetcode-cn.com/problems/word-break/solution/shou-hui-tu-jie-san-chong-fang-fa-dfs-bfs-dong-tai/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public boolean wordBreak3(String s, List<String> wordDict) {
        int len = s.length();
        Set<String> set = wordDict.stream().collect(Collectors.toSet());
        // dp[i] 表示 s[0...i-1]能够完成拆分
        boolean[] dp = new boolean[len + 1];
        // 初始化空串可以拆分，使得状态转移方程统一
        dp[0] = true;
        for (int i = 1; i <= len; ++i) {
            // 枚举j，划分 s[0...i-1] 为两部分
            // dp[i] , s[0...i-1] = s[0,j-1] + s[j, i-1] = dp[j], set.contains(s[j,i]);
            for (int j = 0; j < i; ++j) {
                dp[i] = dp[j] && set.contains(s.substring(j, i));
                // s[0...i-1]可以拆分，不用去尝试新的位置j
                if (dp[i]) {
                    break;
                }
            }
        }
        return dp[len];
    }

    /**
     * 方法三优化：
     * 枚举 j 去 切分 s[0...i-1]
     * dp[i] = dp[j] && set.contains(s[j...i-1])，
     * 前面是判断能否被拆分，是计算完成的dp，后面是是否是给定列表中的单词，那么 可以倒着枚举j，当 s[j-i-1]长度大于maxLen时，那就不用继续了，继续枚举j--，后边部分更长了，不可能在set中
     * 所以这里需要先求一下单词列表中的最大单词长度
     * 然后求dp[i]的时候倒着枚举j
     * @param s
     * @param wordDict
     * @return
     */
    public boolean wordBreak4(String s, List<String> wordDict) {
        int len = s.length(), maxWordLen = 0;
        Set<String> set = new HashSet<>();
        // list --> set，计算最大单词长度
        for (String word : wordDict) {
            set.add(word);
            maxWordLen = Math.max(maxWordLen, word.length());
        }
        // dp[i] 表示 s[0...i-1]能够完成拆分
        boolean[] dp = new boolean[len + 1];
        // 初始化空串可以拆分，使得状态转移方程统一
        dp[0] = true;
        for (int i = 1; i <= len; ++i) {
            // 倒着枚举j，划分 s[0...i-1] 为两部分
            // dp[i] , s[0...i-1] = s[0,j-1] + s[j, i-1] = dp[j], set.contains(s[j,i]);
            for (int j = i - 1; j >= 0; --j) {
                // 再枚举，后边部分更大了，不可能存在于单词列表中
                if (i - j > maxWordLen) {
                    break;
                }
                dp[i] = dp[j] && set.contains(s.substring(j, i));
                // s[0...i-1]可以拆分，不用去尝试新的位置j
                if (dp[i]) {
                    break;
                }
            }
        }
        return dp[len];
    }
}
