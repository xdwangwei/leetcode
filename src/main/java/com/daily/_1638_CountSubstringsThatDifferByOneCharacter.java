package com.daily;

/**
 * @author wangwei
 * @date 2023/3/27 11:59
 * @description: _1638_CountSubstringsThatDifferByOneCharacter
 *
 * 1638. 统计只差一个字符的子串数目
 * 给你两个字符串 s 和 t ，请你找出 s 中的非空子串的数目，这些子串满足替换 一个不同字符 以后，是 t 串的子串。换言之，请你找到 s 和 t 串中 恰好 只有一个字符不同的子字符串对的数目。
 *
 * 比方说， "computer" and "computation" 只有一个字符不同： 'e'/'a' ，所以这一对子字符串会给答案加 1 。
 *
 * 请你返回满足上述条件的不同子字符串对数目。
 *
 * 一个 子字符串 是一个字符串中连续的字符。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "aba", t = "baba"
 * 输出：6
 * 解释：以下为只相差 1 个字符的 s 和 t 串的子字符串对：
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * 加粗部分分别表示 s 和 t 串选出来的子字符串。
 * 示例 2：
 * 输入：s = "ab", t = "bb"
 * 输出：3
 * 解释：以下为只相差 1 个字符的 s 和 t 串的子字符串对：
 * ("ab", "bb")
 * ("ab", "bb")
 * ("ab", "bb")
 * 加粗部分分别表示 s 和 t 串选出来的子字符串。
 * 示例 3：
 * 输入：s = "a", t = "a"
 * 输出：0
 * 示例 4：
 *
 * 输入：s = "abe", t = "bbc"
 * 输出：10
 *
 *
 * 提示：
 *
 * 1 <= s.length, t.length <= 100
 * s 和 t 都只包含小写英文字母。
 * 通过次数11,085提交次数14,328
 */
public class _1638_CountSubstringsThatDifferByOneCharacter {

    /**
     * 方法一：枚举
     *
     * 因为字符串长度不超过 100，所以 我们可以枚举字符串 s 和 t 中不同的那个字符位置，然后分别向两边扩展，直到遇到不同的字符为止，
     * 这样就可以得到以该位置为中心的满足条件的子串对数目。
     *
     * 假设 对于 s[i] != t[j] 位置，左边扩展到的相同字符个数为 l，右边扩展的相同字符个数为  r，
     * 那么以该位置为中心的满足条件的子串对数目为 (l+1)×(r+1)，累加到答案中即可。
     *
     * （因为，当前字符 s[i] 必选，
     * 那么左边可以再选择0，1，...，l个字符前缀，共 l+1种选择，右边可以再选择0，1，...，r个字符后缀，共 r+1种选择
     *    总共 (l + 1) * (r + 1) 种选择）
     *
     * 枚举结束后，即可得到答案
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/count-substrings-that-differ-by-one-character/solution/python3javacgo-yi-ti-shuang-jie-mei-ju-y-zmin/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param t
     * @return
     */
    public int countSubstrings(String s, String t) {
        int m = s.length(), n = t.length();
        int ans = 0;
        // 枚举字符串 s 和 t 中不同的那个字符位置
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                // 跳过相同
                if (s.charAt(i) == t.charAt(j)) {
                    continue;
                }
                // l代表当前位置向左扩展得到的最长前缀字符个数，r代表当前位置向右扩展得到的最长后缀字符个数
                int l = 0, r = 0;
                // 由于排除当前位置，所以 s 中向左 从 i-1 位置开始； t 中向左从 j-1 位置开始，长度从 0 开始
                while (i - 1 - l >= 0 && j - 1 - l >= 0 && s.charAt(i - 1 - l) == t.charAt(j - 1 - l)) {
                    l++;
                }
                // 由于排除当前位置，所以 s 中向右 从 i+1 位置开始； t 中向右从 j+1 位置开始，长度从 0 开始
                while (i + 1 + r < m && j + 1 + r < n && s.charAt(i + 1 + r) == t.charAt(j + 1 + r)) {
                    r++;
                }
                // 累加以该位置为中心的满足条件的子串对数目。
                ans += (l + 1) * (r + 1);
            }
        }
        return ans;
    }


    /**
     * 方法二：预处理 + 枚举
     *
     * 方法一中，我们每次需要分别往左右两边扩展，得出 l 和 r 的值。
     *
     * 实际上，我们可以预处理出以每个位置 (i,j) 结尾的最长相同后缀的长度，以及以每个位置 (i,j) 开头的最长相同前缀的长度，
     * 分别记录在数组 f 和 g 中。
     *
     * 接下来，与方法一类似，我们枚举字符串 s 和 t 中不同的那个字符位置 (i,j)，
     * 那么以该位置为中心的满足条件的子串对数目为 (f[i][j]+1)×(g[i+1][j+1]+1)，累加到答案中即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/count-substrings-that-differ-by-one-character/solution/python3javacgo-yi-ti-shuang-jie-mei-ju-y-zmin/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param t
     * @return
     */
    public int countSubstrings2(String s, String t) {
        int m = s.length(), n = t.length();
        // 以每个位置 (i,j) 结尾的最长相同后缀的长度 f[i][j]  （向左扩展得到的是后缀）
        // 以每个位置 (i,j) 开头的最长相同前缀的长度 g[i][j]  （向右扩展得到的是前缀）
        int[][] f = new int[m][n];
        int[][] g = new int[m][n];
        // 正序遍历，更新 f
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                // s[i] = t[j], f[i][j] = f[i-1][j-1] + 1
                if (s.charAt(i) == t.charAt(j)) {
                    f[i][j] = i > 0 && j > 0 ? f[i - 1][j - 1] + 1 : 1;
                }
            }
        }
        int ans = 0;
        // 倒序遍历，更新 g，计算答案
        for(int i = m - 1; i >= 0; --i) {
            for(int j = n - 1; j >= 0; --j) {
                // s[i] = t[j], g[i][j] = g[i+1][j+1] + 1
                if (s.charAt(i) == t.charAt(j)) {
                    g[i][j] = i + 1 < m && j + 1 < n ? g[i + 1][j + 1] + 1 : 1;
                } else {
                    // s[i] != t[j], 向左右扩展
                    // 从 s[i-1],t[j-1] 往左扩展得到的最长后缀长度
                    int l = i - 1 >= 0 && j - 1 >= 0 ? f[i - 1][j - 1] : 0;
                    // 从 s[i+1],t[j+1] 往右扩展得到的最长前缀长度
                    int r = i + 1 < m && j + 1 < n ? g[i + 1][j + 1] : 0;
                    // 累加答案
                    ans += (l + 1) * (r + 1);
                }
            }
        }
        // 返回
        return ans;
    }

    public static void main(String[] args) {
        _1638_CountSubstringsThatDifferByOneCharacter obj = new _1638_CountSubstringsThatDifferByOneCharacter();
        obj.countSubstrings("ab", "bb");
    }
}
