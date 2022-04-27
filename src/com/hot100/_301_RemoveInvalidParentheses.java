package com.hot100;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/4/26 14:01
 * @description: _301_RemoveInvalidParentheses
 *
 * 301. 删除无效的括号
 * 给你一个由若干括号和字母组成的字符串 s ，删除最小数量的无效括号，使得输入的字符串有效。
 *
 * 返回所有可能的结果。答案可以按 任意顺序 返回。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "()())()"
 * 输出：["(())()","()()()"]
 * 示例 2：
 *
 * 输入：s = "(a)())()"
 * 输出：["(a())()","(a)()()"]
 * 示例 3：
 *
 * 输入：s = ")("
 * 输出：[""]
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 25
 * s 由小写英文字母以及括号 '(' 和 ')' 组成
 * s 中至多含 20 个括号
 */
public class _301_RemoveInvalidParentheses {


    private List<String> res;
    private Set<String> set;

    /**
     * 方法一：回溯，对于原字符串每个位置的每个字符，都可以选择 保留 或 跳过，
     * 每完成一次对所有元素的选择后得到一个新的字符串，去判断当前这个字符串是否是有效括号
     *      如果是，则判断 保存全部结果的集合 res：
     *          如果 res 为空，那么 直接 res.add(res);
     *          如果 res 不空，那么 判断 res里面(之前的可能结果) 的长度 和当前字符串的长度
     *              如果当前字符串更长，说明 当次选择 删除的字符更少，那么 清空 res， 加入 当前字符串
     *              如果一样长，那么 说明是一个并列的结果，加入res
     *              这种情况下，为了避免重复结果，再创建一个set，让它和res同时操作，并用于去重
     *
     * 886ms，拉跨
     *
     * @param s
     * @return
     */
    public List<String> removeInvalidParentheses(String s) {
        // 保留所有可能结果
        res = new ArrayList<>();
        // 去除重复结果
        set = new HashSet<>();
        // 回溯
        backTrack(s, 0, "");
        return res;
    }

    /**
     * 回溯，是否选择字符串s当前位置index的字符，temp是之前的回溯路径（得到的字符串）
     * set是为了去除重复
     * @param s
     * @param index
     * @param temp
     */
    private void backTrack(String s, int index, String temp) {
        // 如果对所有字符都完成了选择
        if (index == s.length()) {
            // 如果是有效的字符串
            if (isValid(temp)) {
                // 如果 res 空，直接加入
                if (res.isEmpty()) {
                    res.add(temp);
                    set.add(temp);
                } else {
                    // res不空，只 保留 删除更少字符 得到的结果
                    if (temp.length() > res.get(res.size() - 1).length()) {
                        res.clear();
                        res.add(temp);
                        set.clear();
                        set.add(temp);
                        // 多种可能结果
                    } else if (temp.length() == res.get(res.size() - 1).length() && !set.contains(temp)) {
                        res.add(temp);
                        set.add(temp);
                    }
                }
            }
            return;
        }
        // 对于每个字符，可以选，也可以不选，但是 小写字母并不影响结果，所以为了尽可能少的删除字符，小写字母必选
        // 选择当前字符
        backTrack(s, index + 1, temp + s.charAt(index));
        if (s.charAt(index) == '(' || s.charAt(index) == ')') {
            // 不选择当前字符
            backTrack(s, index + 1, temp);
        }
    }


    /**
     * 判断当前字符是否是有效的括号串
     * @param str
     * @return
     */
    private boolean isValid(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); ++i) {
            // 遇到（，count++
            char c = str.charAt(i);
            if (c == '(') {
                count++;
                // 遇到 ），count--
            } else if (c == ')') {
                count--;
                // （（（）））））这种清空，直接返回false
                if (count < 0) {
                    return false;
                }
            }
            // 小写字母不影响结果
        }
        // 最终count应该为0，count不为0，就是（（（（）），这种情况
        return count == 0;
    }

    /**
     * 方法二：回溯，官方题解的回溯
     * ，要求我们删除最少的括号数，并且要求得到所有的结果。我们可以使用回溯算法，尝试遍历[所有可能的去掉非法括号的方案]。
     *
     * 【首先】我们利用括号匹配的规则求出该字符串 s 中[最少]需要去掉的左括号的数目 lremove 和右括号的数目 rremove，
     * 然后我们尝试在原字符串 s 中去掉 lremove 个左括号和 rremove 个右括号，然后检测剩余的字符串是否合法匹配，
     * 如果合法匹配则我们则认为该字符串为可能的结果，我们利用回溯算法来尝试搜索所有可能的去除括号的方案。
     *
     * 在进行回溯时可以利用以下的剪枝技巧来增加搜索的效率：
     *
     * 我们从字符串中每去掉一个括号，则更新 lremove 或者 rremove，当我们发现剩余未尝试的字符串的长度小于 lremove+rremove 时，则停止本次搜索。
     * 当 lremove 和 rremove 同时为 0 时，则我们检测当前的字符串是否合法匹配，如果合法匹配则我们将其记录下来。
     * 由于记录的字符串可能存在重复，因此需要对重复的结果进行去重，去重的办法有如下两种：
     *
     * 利用哈希表对最终生成的字符串去重。
     * 我们在每次进行搜索时，如果遇到连续相同的括号我们只需要搜索一次即可，比如当前遇到的字符串为 "(((())"，去掉前四个左括号中的任意一个，生成的字符串是一样的，均为 "((())"，
     * 因此我们在尝试搜索时，只需去掉一个左括号进行下一轮搜索，不需要将前四个左括号都尝试一遍。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/remove-invalid-parentheses/solution/shan-chu-wu-xiao-de-gua-hao-by-leetcode-9w8au/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public List<String> removeInvalidParentheses2(String s) {
        res = new ArrayList<>();
        int lremove = 0, rremove = 0;
        // s 中[最少]需要去掉的左括号的数目 lremove 和右括号的数目 rremove，
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(') {
                lremove++;
            } else if (s.charAt(i) == ')') {
                // 多出来的 ）
                if (lremove == 0) {
                    rremove++;
                } else {
                    // 否则，抵消一个 (
                    lremove--;
                }
            }
        }
        backTrack2(s, 0, lremove, rremove);
        return res;
    }

    /**
     * 从 s 的 start 位置开始，需要去除 lremove 个 （ 和 rremvoe 个 ）
     * @param s
     * @param start
     * @param lremove
     * @param rremove
     */
    private void backTrack2(String s, int start, int lremove, int rremove) {
        // 去除够了
        if (lremove == 0 && rremove == 0) {
            // 有效串
            if (isValid(s)) {
                res.add(s);
            }
            return;
        }
        // 从指定位置开始尝试去除 指定数量的括号
        for (int i = start; i < s.length(); ++i) {
            // 当前位置字符和上一位置一样，那肯定考虑过了已经
            if (i != start && s.charAt(i) == s.charAt(i - 1)) {
                continue;
            }
            // 剩余长度根本不够要去除的括号数目
            if (lremove + rremove > s.length() - start) {
                return;
            }
            // 尝试去掉当前位置的 （，只有 lremove不为0才有必要
            if (lremove > 0 && s.charAt(i) == '(') {
                backTrack2(s.substring(0, i) + s.substring(i + 1), i, lremove - 1, rremove);
            }
            // 尝试去除当前位置的 ）
            if (rremove > 0 && s.charAt(i) == ')') {
                backTrack2(s.substring(0, i) + s.substring(i + 1), i, lremove, rremove - 1);
            }
        }
    }


    /**
     * 方法三：广度优先遍历
     * 注意到题目中要求最少删除，这样的描述正是广度优先搜索算法应用的场景，并且题目也要求我们输出所有的结果。
     * 我们在进行广度优先搜索时每一轮删除字符串中的 1 个括号，直到出现合法匹配的字符串为止，此时进行轮转的次数即为最少需要删除括号的个数。
     *
     * 我们进行广度优先搜索时，每次保存上一轮搜索的结果，然后对上一轮已经保存的结果中的每一个字符串尝试所有可能的删除一个括号的方法，然后将保存的结果进行下一轮搜索。在保存结果时，我们可以利用哈希表对上一轮生成的结果去重，从而提高效率。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/remove-invalid-parentheses/solution/shan-chu-wu-xiao-de-gua-hao-by-leetcode-9w8au/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public List<String> removeInvalidParentheses3(String s) {
        res = new ArrayList<>();
        // 每次保存上一轮搜索的结果,初始
        Set<String> currSet = new HashSet<>();
        currSet.add(s);
        while (true) {
            // 判断当前层，是否有目标节点，如果有，就加入
            for (String str : currSet) {
                if (isValid(str)) {
                    res.add(str);
                }
            }
            // 因为是广度优先遍历，齐头并进，所以第一次出现目标的这一层，就是 删除字符最少得到的结果，再往下，即便是目标，也删除了更多字符，，所以直接返回
            if (res.size() > 0) {
                return res;
            }
            // 保存当前层每个节点扩展出的下一层节点
            Set<String> nextSet = new HashSet<>();
            for (String str : currSet) {
                // 每个节点尝试删除一个括号
                for (int i = 0; i < str.length(); i ++) {
                    // 相同字符不重复统计
                    if (i > 0 && str.charAt(i) == str.charAt(i - 1)) {
                        continue;
                    }
                    // 尝试移除一个括号，得到下层节点
                    if (str.charAt(i) == '(' || str.charAt(i) == ')') {
                        nextSet.add(str.substring(0, i) + str.substring(i + 1));
                    }
                }
            }
            // 迭代
            currSet = nextSet;
        }
    }
}
