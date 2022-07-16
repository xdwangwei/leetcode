package com.recursion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

/**
 * @Author: wangwei
 * @Description: 给出 n 代表生成括号的对数，请你写出一个函数，使其能够生成所有可能的并且有效的括号组合
 * [
 * "((()))",
 * "(()())",
 * "(())()",
 * "()(())",
 * "()()()"
 * ]
 * @Time: 2019/12/11 周三 09:26
 **/
public class _22_GenerateBrackets {

    List<String> res = new ArrayList<>();

    public List<String> solution1(int n) {
        // n 对括号，一共 2 * n 个字符，第一个只能是 ( , 因此递归 2 * n - 1
        backtrack1(n * 2, "(", 1);
        return res;
    }

    /**
     * 与第17题类似，借助递归，每次都是分配 ( 和 )
     *
     * @param n
     * @param letter
     * @param i
     */
    private void backtrack1(int n, String letter, int i) {
        int l = 0, r = 0;
        // 得到letter中已有的 ( 和 ) 的数量
        for (int j = 0; j < letter.length(); ++j) {
            if (letter.charAt(j) == '(') ++l;
            else if (letter.charAt(j) == ')') ++r;
        }
        // 右括号多于左括号，直接返回
        if (r > l) return;
        // 递归到最后一层
        if (i == n) {
            // 如果左括号数等于右括号数
            if (l == r) res.add(letter);
            return;
        }
        // 如果左括号数等于右括号数但不是最后一层递归，就只能往后添 (
        backtrack1(n, letter + "(", i + 1);
        // 如果左括号数大于右括号数，还要考虑添 )
        if (l > r)
            backtrack1(n, letter + ")", i + 1);
    }


    /**
     * @param args
     */
    public List<String> solution2(int n) {
        // n 对括号，一共 2 * n 个字符
        backtrack2("", 0, 0, n);
        return res;
    }

    /**
     * @param letter
     * @param left   左括号数目
     * @param right  右括号数目
     * @param n
     */
    private void backtrack2(String letter, int left, int right, int n) {
        // 当前串的长度已经 2 * n，保存当前结果
        if (letter.length() == n * 2) {
            res.add(letter);
            return;
        }
        /*左括号数目小于n，就能添加左括号*/
        if (left < n)
            backtrack2(letter + "(", left + 1, right, n);
        // 如果左括号数多于右括号，就要继续添加右括号
        if (left > right)
            backtrack2(letter + ")", left, right + 1, n);
    }

    /**
     * 动态规划
     * 当我们清楚所有 i<n 时括号的可能生成排列后，对于 i=n 的情况，我们考虑整个括号排列中最左边的括号。
     * 它一定是一个左括号，那么它可以和它对应的右括号组成一组完整的括号 "( )"，我们认为这一组是相比 n-1 增加进来的括号。
     * 那么，剩下 n-1 组括号要么在这一组新增的括号内部，要么在这一组新增括号的外部（右侧）
     * 因此第一个一定是 (，它一定会有一个对应的 ), 可以在最右侧，也可以在中间某个位置
     * 也就是说始终可以看成这样 (有p对括号的字串)有q对括号的子串，且 p + q = n - 1,  0 <= p <= n - 1
     * 因此只要按着这种模式将所有满足匹配的p子串和q子串和()进行拼接，就能得到全部结果
     *
     * @param n
     * @return
     */
    public List<String> solution3(int n) {
        List<String> res3 = new ArrayList<>();
        // 0对括号的结果
        if (n == 0)
            res3.add("");
        else {
            // p + q = n - 1, 0 <= p <= n - 1
            for (int p = 0; p < n; ++p) {
                // 从有p对括号的结果集中取出每个结果
                for (String pstr : solution3(p)) {
                    // 从有q对括号的结果集中取出每个结果
                    for (String qstr : solution3(n - 1 - p))
                        // 拼接得到一个答案
                        res3.add("(" + pstr + ")" + qstr);
                }
            }
        }
        return res3;
    }

    /**
     * 回溯
     *
     * @param n
     * @return
     */
    public List<String> solution4(int n) {
        backTrack4("", n, n);
        return res;
    }

    /**
     * @param letter 当前串
     * @param left 还需要几个左括号
     * @param right 还需要几个右括号
     */
    private void backTrack4(String letter, int left, int right) {
        if (left == 0 && right == 0) {
            res.add(letter);
            return;
        }
        // left > right说明需要更多的左括号，也就是已有串中 左括号数 < 右括号数，这肯定无效
        if (left < 0 || right < 0 || left > right) return;
        // 做选择
        backTrack4(letter + "(", left - 1, right);
        backTrack4(letter + ")", left, right - 1);
    }

    public static void main(String[] args) {
        _22_GenerateBrackets t = new _22_GenerateBrackets();
        List<String> list = t.solution4(3);
        for (String s : list) {
            System.out.println(s);
        }

    }
}
