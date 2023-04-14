package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/4/13 22:34
 * @description: _1023_CamelCaseMatching
 *
 * 1023. 驼峰式匹配
 * 如果我们可以将小写字母插入模式串 pattern 得到待查询项 query，那么待查询项与给定模式串匹配。（我们可以在任何位置插入每个字符，也可以插入 0 个字符。）
 *
 * 给定待查询列表 queries，和模式串 pattern，返回由布尔值组成的答案列表 answer。只有在待查项 queries[i] 与模式串 pattern 匹配时， answer[i] 才为 true，否则为 false。
 *
 *
 *
 * 示例 1：
 *
 * 输入：queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FB"
 * 输出：[true,false,true,true,false]
 * 示例：
 * "FooBar" 可以这样生成："F" + "oo" + "B" + "ar"。
 * "FootBall" 可以这样生成："F" + "oot" + "B" + "all".
 * "FrameBuffer" 可以这样生成："F" + "rame" + "B" + "uffer".
 * 示例 2：
 *
 * 输入：queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FoBa"
 * 输出：[true,false,true,false,false]
 * 解释：
 * "FooBar" 可以这样生成："Fo" + "o" + "Ba" + "r".
 * "FootBall" 可以这样生成："Fo" + "ot" + "Ba" + "ll".
 * 示例 3：
 *
 * 输出：queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FoBaT"
 * 输入：[false,true,false,false,false]
 * 解释：
 * "FooBarTest" 可以这样生成："Fo" + "o" + "Ba" + "r" + "T" + "est".
 *
 *
 * 提示：
 *
 * 1 <= queries.length <= 100
 * 1 <= queries[i].length <= 100
 * 1 <= pattern.length <= 100
 * 所有字符串都仅由大写和小写英文字母组成。
 * 通过次数10,757提交次数18,653
 */
public class _1023_CamelCaseMatching {


    /**
     * 双指针
     *
     * 题意：判断 queries 中的每个单词，能够通过 向 pattern 中插入小写字符 的方式 组成，每个小写字符可插入任意次
     *
     * 设计 函数 check(s, p) 返回，能够通过向 p 中插入小写字符 形成 s，每个小写字符可插入任意次
     *      如果 s.len > p.len , 返回 false
     *      如果 s.len == p.len, 返回 s.equals(p)
     *      否则 （s.len > p.len）
     *          双指针，s[i]，p[j]，初始 i == j == 0
     *          遍历 p[j]:
     *              在 s 中去找 p[j] 的位置（要保证相对顺序，所以 i 从 上一次 p[j-1]匹配成功的位置开始，）
     *              while(s[i] != p[j]) {
     *                  // 中间不能间隔大写字符，因为只能插入小写字符，或者 s 中从上次位置开始，已经没有与 p[j] 匹配的字符了
     *                  if (s[i].isUpperCase() || i == s.len) {
     *                      return false;
     *                  }
     *              }
     *              // s[i] 和 p[j] 匹配成功
     *              // i++, j++
     *          while 结束后，需要 判断 s 是否遍历结束，如果有多余，那么剩余的字符中不能有大写字符，因为 p 无法追加小写字符
     *          while (i < s.len) {
     *              if s[i++].isUpperCase() {
     *                  return false;
     *              }
     *          }
     *          // 匹配成功
     *          return true;
     *
     *
     * @param queries
     * @param pattern
     * @return
     */
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> res = new ArrayList<>(queries.length);
        for (String query : queries) {
            res.add(check(query, pattern));
        }
        return res;
    }

    /**
     * 函数 check(s, p) 返回，能够通过向 p 中插入小写字符 形成 s，每个小写字符可插入任意次
     * 双指针
     * @param query
     * @param pattern
     * @return
     */
    private boolean check(String query, String pattern) {
        // s 不能 比 p 更短
        if (query.length() < pattern.length()) {
            return false;
        }
        // 长度相同，除非内容一致
        if (query.length() == pattern.length()) {
            return query.equals(pattern);
        }
        // s 比 p 长，往 p 中插入小写字符能够得到 s ？
        // 双指针，寻找 p每个字符在 s 中的对应位置，因为 p字符顺序遍历，所以在s中寻找时是从上次的位置往后，而不是从头开始
        // 因为只能插入小写字符，所以 s[i] 匹配 p[j] 的过程中不能遇到 大写字符
        int i = 0, j = 0;
        // 顺序遍历 p 的每个字符
        for (; j < pattern.length();) {
            char d = pattern.charAt(j);
            // 去 s 中从上个匹配位置开始寻找下一个位置 匹配 p[j]
            while (query.charAt(i) != d) {
                // 如果找不到匹配位置 或者 遇到大写字符，返回 false
                if (Character.isUpperCase(query.charAt(i)) || ++i == query.length()) {
                    return false;
                }
            }
            // s[i] 匹配 p[i]
            // 两指针后移
            i++; j++;
        }
        // while 结束后，p 肯定是匹配完了，但 s 有可能还没有结束，这种情况下 s 多出来的末尾部分不能包含 大写字符，因为 p 只能追加小写字符
        while (i < query.length()) {
            // s 多出来的末尾部分不能包含 大写字符
            if (Character.isUpperCase(query.charAt(i++))) {
                return false;
            }
        }
        // 最后，p 可以通过插入 小写字符的方式 组成 s
        return true;
    }

    public static void main(String[] args) {
        _1023_CamelCaseMatching obj = new _1023_CamelCaseMatching();
        System.out.println(obj.camelMatch(new String[]{"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"}, "FB"));
    }
}
