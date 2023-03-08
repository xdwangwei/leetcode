package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/3/7 18:39
 * @description: _1096_BraceExpansion2
 * 1096. 花括号展开 II
 * 如果你熟悉 Shell 编程，那么一定了解过花括号展开，它可以用来生成任意字符串。
 *
 * 花括号展开的表达式可以看作一个由 花括号、逗号 和 小写英文字母 组成的字符串，定义下面几条语法规则：
 *
 * 如果只给出单一的元素 x，那么表达式表示的字符串就只有 "x"。R(x) = {x}
 * 例如，表达式 "a" 表示字符串 "a"。
 * 而表达式 "w" 就表示字符串 "w"。
 * 当两个或多个表达式并列，以逗号分隔，我们取这些表达式中元素的并集。R({e_1,e_2,...}) = R(e_1) ∪ R(e_2) ∪ ...
 * 例如，表达式 "{a,b,c}" 表示字符串 "a","b","c"。
 * 而表达式 "{{a,b},{b,c}}" 也可以表示字符串 "a","b","c"。
 * 要是两个或多个表达式相接，中间没有隔开时，我们从这些表达式中各取一个元素依次连接形成字符串。R(e_1 + e_2) = {a + b for (a, b) in R(e_1) × R(e_2)}
 * 例如，表达式 "{a,b}{c,d}" 表示字符串 "ac","ad","bc","bd"。
 * 表达式之间允许嵌套，单一元素与表达式的连接也是允许的。
 * 例如，表达式 "a{b,c,d}" 表示字符串 "ab","ac","ad"。
 * 例如，表达式 "a{b,c}{d,e}f{g,h}" 可以表示字符串 "abdfg", "abdfh", "abefg", "abefh", "acdfg", "acdfh", "acefg", "acefh"。
 * 给出表示基于给定语法规则的表达式 expression，返回它所表示的所有字符串组成的有序列表。
 *
 * 假如你希望以「集合」的概念了解此题，也可以通过点击 “显示英文描述” 获取详情。
 *
 *
 *
 * 示例 1：
 *
 * 输入：expression = "{a,b}{c,{d,e}}"
 * 输出：["ac","ad","ae","bc","bd","be"]
 * 示例 2：
 *
 * 输入：expression = "{{a,z},a{b,c},{ab,z}}"
 * 输出：["a","ab","ac","z"]
 * 解释：输出中 不应 出现重复的组合结果。
 *
 *
 * 提示：
 *
 * 1 <= expression.length <= 60
 * expression[i] 由 '{'，'}'，',' 或小写英文字母组成
 * 给出的表达式 expression 用以表示一组基于题目描述中语法构造的字符串
 * 通过次数9,946提交次数13,726
 */
public class _1096_BraceExpansion2 {

    /**
     * 方法一：递归解析 (编译原理，递归下降分析法)
     * 思路与算法
     *
     * 表达式可以拆分为多个子表达式，以逗号分隔或者直接相接。我们应当先按照逗号分割成多个子表达式进行求解，然后再对所有结果求并集。
     * 这样做的原因是求积的优先级高于求并集的优先级。
     *
     * 我们用 expr 表示一个任意一种表达式，表达式可以根据 逗号分割成多个部分，用 term 表示一个最外层没有逗号分割的表达式，
     * 那么 expr 可以按照如下规则分解：
     *
     *      expr  -->  term | term,expr    (最外围，加法，并列)
     *
     * 其中的 | 表示或者，即 expr 可以分解为前者(单个部分)，也可以分解为后者（逗号分割的多个部分）。
     *
     * 再来看 term
     *
     * term 可以由小写英文字母或者花括号包括的表达式直接相接组成，我们用 item 来表示每一个相接单元，那么 term 可以按照如下规则分解：
     *
     *      term --> item | item * term，其中 * 代表字符传拼接，如 a{b,c} = ab,ac   (中间层，乘法，乘积)
     *
     * item 可以进一步分解为小写英文字母 letter 或者花括号包括的表达式，它的分解如下：
     *
     *      item --> letter | {expr}   （内层，最小单元）
     *
     * 在代码中，我们编写三个函数，分别负责以上三种规则的分解：
     *
     * expr 函数，不断的调用 term，并与其结果进行合并。如果匹配到表达式末尾或者当前字符不是逗号时，则结束返回。
     * term 函数，不断的调用 item，并与其结果求积。如果匹配到表达式末尾或者当前字符不是小写字母也不是左括号时，则返回。
     * item 函数，根据当前字符是不是左括号来求解。如果是左括号，则调用 expr，返回结果；否则构造一个只包含当前字符的字符串集合，返回结果。
     *
     * 以下示意图以 {a,b}{c,{d,e}} 为例，展示了表达式递归拆解以及回溯的全过程。‘
     *
     * 初始时 expression 作为一个 expr 进行求解
     *
     * 为了保证不重复和有序性，返回值类型采用 TreeSet
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/brace-expansion-ii/solution/hua-gua-hao-zhan-kai-ii-by-leetcode-solu-1s1y/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param expression
     * @return
     */

    private int idx = 0;
    private String expression;


    public List<String> braceExpansionII(String expression) {
        this.expression = expression;
        // 为什么不在这里直接 idx++，因为 expression 不一定是 "{xxx}" 形式，也可能是 "a,c" ，所以进入 term 后再更新idx
        // 第一次 expr() -> term() -> item(){这里idx++，去掉了第一个'{'，进入内部计算} -->>>>
        return new ArrayList<>(expr());
    }

    /**
     * expr --> item | item,expr
     *
     * 合并 以 逗号 分割的 多个部分
     * @return
     */
    private TreeSet<String> expr() {
        TreeSet<String> ans = new TreeSet<>();
        while (true) {
            // 递归调用多个 iterm，取并集
            ans.addAll(term());
            // 遇到 逗号，满足并列关系，继续
            if (idx < expression.length() && expression.charAt(idx) == ',') {
                idx++;
            } else {
                // 否则退出
                break;
            }
        }
        return ans;
    }

    /**
     * term --> item | item * term,  多个部分间的拼接，每个部分可以是 单个字符，也可以是 {} 表达式
     * @return
     */
    private TreeSet<String> term() {
        // 准备返回结果，因为要笛卡尔乘积，避免空集
        TreeSet<String> ans = new TreeSet<>(){{ add(""); }};
        // 每个 item 部分可以是 单个字符，也可以是 {} 表达式
        while (idx < expression.length() && (expression.charAt(idx) == '{' || Character.isLetter(expression.charAt(idx)))) {
            // 求出 item
            TreeSet<String> words = item();
            TreeSet<String> temp = new TreeSet<>();
            // 拼接
            for (String w : ans) {
                for (String w2 : words) {
                    temp.add(w + w2);
                }
            }
            // 合并
            ans = temp;
        }
        // 返回
        return ans;
    }

    /**
     * item --> letter | {expr}，最小部分
     * @return
     */
    private TreeSet<String> item() {
        TreeSet<String> ans = new TreeSet<>();
        char c = expression.charAt(idx);
        // 如果是 {expr}
        if (c == '{') {
            // 跳过 { 开始 expr 计算
            idx++;
            // 当前 item 的返回值 等于 expr 的返回值
            ans = expr();
        // 单个字符
        } else if (Character.isLetter(c)) {
            // 加入集合
            ans.add(c + "");
        }
        // 当前 item 结束，更新 idx
        idx++;
        // 返回
        return ans;
    }


    /**
     * 方法二：更简单的递归 或 bfs
     *
     * 方法一：递归
     *
     * 我们设计一个递归函数 dfs(exp)，用于处理表达式 exp，并将结果存入集合 s 中。
     *
     * 对于表达式 exp，我们首先找到第一个右花括号的位置 j，
     *      如果找不到，说明 exp 中没有右花括号，即 exp 为单一元素，直接将 exp 加入集合 s 中即可。
     *      否则，我们从位置 j 开始往左找到第一个左花括号的位置 i，此时  exp[:i] 和 exp[j+1:] 分别为 exp 的前缀和后缀，记为 a 和 c。
     *          而 exp[i+1:j] 为 exp 最中心花括号内的部分，即 exp 中的子表达式，我们将其按照逗号分割成多个字符串 b1,b2,⋯,bk，
     *          然后对每个 bi，我们将 a+b+c 拼接（乘法）成新的表达式，递归调用 dfs 函数处理新的表达式，即 dfs(a+b+c)。（加法）
     *
     * 最后，我们将集合 s 中的元素按照字典序排序，即可得到答案。
     *
     * bfs 和 上述思想一致，只是简单修改
     *
     * 首先将原始表达式 exp 加入 队列
     *
     * 循环遍历队列：
     *
     *      出队 字符串  curr
     *
     *      对于表达式 curr，我们首先找到第一个右花括号的位置 j，
     *              如果找不到，说明 curr 中没有右花括号，即 curr 为单一元素，直接将 curr 加入集合 s 中即可。
     *              否则，我们从位置 j 开始往左找到第一个左花括号的位置 i，此时  curr[:i] 和 curr[j+1:] 分别为 exp 的前缀和后缀，记为 a 和 c。
     *              而 curr[i+1:j] 为 curr 最中心花括号内的部分，即 curr 中的子表达式，我们将其按照逗号分割成多个字符串 b1,b2,⋯,bk，
     *              然后对每个 bi，我们将 a+b+c 拼接（乘法）成新的表达式，将其加入队列等待处理，即 queue.offer(a+b+c)。（加法）
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/brace-expansion-ii/solution/python3javacgotypescript-yi-ti-yi-jie-di-gs64/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param expression
     * @return
     */
    TreeSet<String> ans = new TreeSet<>();
    public List<String> braceExpansionII2(String expression) {
        // 更简单的递归
        // dfs(expression);
        // 更简单的bfs
        bfs(expression);
        return new ArrayList<>(ans);
    }

    private void dfs(String exp) {
        // 不包含 {}，直接加入结果集
        int j = exp.indexOf('}');
        if (j == -1) {
            ans.add(exp);
            return;
        }
        int i = j;
        while (exp.charAt(--i) != '{');
        // i，j 代表了 最中间 的 {}
        String mid = exp.substring(i + 1, j);
        // 前缀
        String left = exp.substring(0, i);
        // 后缀
        String right = exp.substring(j + 1);
        // 拆分中间部分，拼接前缀后缀（乘法）
        // 依次递归，for 循环并列调用 （加法）
        for (String m : mid.split(",")) {
            dfs(left + m + right);
        }
    }

    /**
     * 将上述 dfs 改为 bfs，
     * @param exp
     */
    private void bfs(String exp) {
        Deque<String> queue = new ArrayDeque<>();
        // 初始表达式加入队列
        queue.offer(exp);

        // bfs
        while (!queue.isEmpty()) {
            // 出队列
            String curr = queue.poll();
            // 不包含 {}，直接加入结果集
            int j = curr.indexOf('}');
            if (j == -1) {
                ans.add(curr);
                continue;
            }
            int i = j;
            while (curr.charAt(--i) != '{');
            // i，j 代表了 最中间 的 {}
            String mid = curr.substring(i + 1, j);
            // 前缀
            String left = curr.substring(0, i);
            // 后缀
            String right = curr.substring(j + 1);
            // 拆分中间部分，拼接前缀后缀（乘法）
            // 依次入队列 （加法）
            for (String m : mid.split(",")) {
                queue.offer(left + m + right);
            }
        }
    }


    public static void main(String[] args) {
        _1096_BraceExpansion2 obj = new _1096_BraceExpansion2();
        obj.braceExpansionII("{{a,z},a{b,c},{ab,z}}");
    }
}
