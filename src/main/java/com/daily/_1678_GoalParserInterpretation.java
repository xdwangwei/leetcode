package com.daily;

/**
 * @author wangwei
 * @date 2022/11/6 10:52
 * @description: _1678_GoalParserInterpretation
 *
 * 1678. 设计 Goal 解析器
 * 请你设计一个可以解释字符串 command 的 Goal 解析器 。command 由 "G"、"()" 和/或 "(al)" 按某种顺序组成。Goal 解析器会将 "G" 解释为字符串 "G"、"()" 解释为字符串 "o" ，"(al)" 解释为字符串 "al" 。然后，按原顺序将经解释得到的字符串连接成一个字符串。
 *
 * 给你字符串 command ，返回 Goal 解析器 对 command 的解释结果。
 *
 *
 *
 * 示例 1：
 *
 * 输入：command = "G()(al)"
 * 输出："Goal"
 * 解释：Goal 解析器解释命令的步骤如下所示：
 * G -> G
 * () -> o
 * (al) -> al
 * 最后连接得到的结果是 "Goal"
 * 示例 2：
 *
 * 输入：command = "G()()()()(al)"
 * 输出："Gooooal"
 * 示例 3：
 *
 * 输入：command = "(al)G(al)()()G"
 * 输出："alGalooG"
 *
 *
 * 提示：
 *
 * 1 <= command.length <= 100
 * command 由 "G"、"()" 和/或 "(al)" 按某种顺序组成
 * 通过次数34,160提交次数39,979
 */
public class _1678_GoalParserInterpretation {

    /**
     * 直接遍历
     *
     * 根据题意可以知道字符串 command 一定由三种不同的字符串 “G",“()",“(al)" 组合而成，其中的转换规则如下：
     *
     * “G" 转换为 “G"；
     * “()" 转换为 “o"；
     * “(al) 转换为 “al"；
     * 由于三种不同的字符串由于模式不同，我们可以按照如下规则进行匹配：
     *
     * 如果当前第 i 个字符为 ‘G’，则表示当前字符串模式为 “G"，转换后的结果为 “G"，我们直接在结果中添加 “G"；
     *
     * 如果当前第 i 个字符为 ‘(’，则表示当前字符串模式可能为 “()" 或 “(al)"；
     *
     *      如果第 i+1 个字符为 ‘)’，则当前字符串模式为 “()"，我们应将其转换为 “o"；
     *      如果第 i+1 个字符为 ‘a’，则当前字符串模式为 “(al)"，我们应将其转换为 “al"
     *
     * 其他字符跳过
     *
     * 我们按照以上规则进行转换即可得到转换后的结果。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/goal-parser-interpretation/solution/she-ji-goal-jie-xi-qi-by-leetcode-soluti-npnp/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param command
     * @return
     */
    public String interpret(String command) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < command.length(); i++) {
            // G
            if (command.charAt(i) == 'G') {
                res.append("G");
                // () 或 “(al)”
            } else if (command.charAt(i) == '(') {
                // "()"
                if (command.charAt(i + 1) == ')') {
                    res.append("o");
                } else {
                    // "(al)"
                    res.append("al");
                }
            }
            // 其他跳过
        }
        return res.toString();
    }
}
