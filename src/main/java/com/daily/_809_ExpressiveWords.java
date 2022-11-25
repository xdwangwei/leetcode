package com.daily;

/**
 * @author wangwei
 * @date 2022/11/25 11:59
 * @description: _809_ExpressiveWords
 *
 * 809. 情感丰富的文字
 * 有时候人们会用重复写一些字母来表示额外的感受，比如 "hello" -> "heeellooo", "hi" -> "hiii"。我们将相邻字母都相同的一串字符定义为相同字母组，例如："h", "eee", "ll", "ooo"。
 *
 * 对于一个给定的字符串 S ，如果另一个单词能够通过将一些字母组扩张从而使其和 S 相同，我们将这个单词定义为可扩张的（stretchy）。扩张操作定义如下：选择一个字母组（包含字母 c ），然后往其中添加相同的字母 c 使其长度达到 3 或以上。
 *
 * 例如，以 "hello" 为例，我们可以对字母组 "o" 扩张得到 "hellooo"，但是无法以同样的方法得到 "helloo" 因为字母组 "oo" 长度小于 3。此外，我们可以进行另一种扩张 "ll" -> "lllll" 以获得 "helllllooo"。如果 s = "helllllooo"，那么查询词 "hello" 是可扩张的，因为可以对它执行这两种扩张操作使得 query = "hello" -> "hellooo" -> "helllllooo" = s。
 *
 * 输入一组查询单词，输出其中可扩张的单词数量。
 *
 *
 *
 * 示例：
 *
 * 输入：
 * s = "heeellooo"
 * words = ["hello", "hi", "helo"]
 * 输出：1
 * 解释：
 * 我们能通过扩张 "hello" 的 "e" 和 "o" 来得到 "heeellooo"。
 * 我们不能通过扩张 "helo" 来得到 "heeellooo" 因为 "ll" 的长度小于 3 。
 *
 *
 * 提示：
 *
 * 1 <= s.length, words.length <= 100
 * 1 <= words[i].length <= 100
 * s 和所有在 words 中的单词都只由小写字母组成。
 * 通过次数13,470提交次数29,006
 */
public class _809_ExpressiveWords {


    /**
     * 逐个判断 words 中单词是否能扩充得到s，
     * @param s
     * @param words
     * @return
     */
    public int expressiveWords(String s, String[] words) {
        int ans = 0;
        // 遍历
        for (String word : words) {
            // 判断
            if (check(s, word)) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * 判断 t 能否扩张得到s
     * @param s
     * @param t
     * @return
     */
    private boolean check(String s, String t) {
        // t本身长度不能超过s
        if (s.length() < t.length()) {
            return false;
        }
        int m = s.length(), n = t.length();
        int i = 0, j = 0;
        // 双指针，i，j每次分别指向s，t中下一组字符的起始字符位置，比如对于 aaabbb，i首先指向0，下一次指向3
        while (i < m && j < n) {
            // 首先字符必须对应一致（否则就是t中出现了s中没有的字符或者t中字符组的顺序与s中不一致）
            if (s.charAt(i) != t.charAt(j)) {
                return false;
            }
            // 对于当前字符组，统计s中当前字符个数，注意不能越界
            int a = i;
            while (a < m && s.charAt(a) == s.charAt(i)) {
                a++;
            }
            // 对于当前字符组，统计s中当前字符个数，注意不能越界
            int b = j;
            while (b < n && t.charAt(b) == t.charAt(j)) {
                b++;
            }
            // 得到s、t中当前字符个数
            a -= i;
            b -= j;
            // t中当前字符数不能比s中的多，如果 t中当前字符个数比s中少，它可以扩充，但是要求至少扩充到3个，所以s中此字符个数必须>=3
            if (a < b || (a > b && a < 3)) {
                return false;
            }
            // i、j分别跳转到s、t下一个字符组起始字符位置
            i += a;
            j += b;
        }
        // 结束时，s、t必须恰好都结束
        return i == m && j == n;
    }
}
