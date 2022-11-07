package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/11/7 10:37
 * @description: _816_AmbiguousCoordinates
 *
 * 816. 模糊坐标
 * 我们有一些二维坐标，如 "(1, 3)" 或 "(2, 0.5)"，然后我们移除所有逗号，小数点和空格，得到一个字符串S。返回所有可能的原始字符串到一个列表中。
 *
 * 原始的坐标表示法不会存在多余的零，所以不会出现类似于"00", "0.0", "0.00", "1.0", "001", "00.01"或一些其他更小的数来表示坐标。此外，一个小数点前至少存在一个数，所以也不会出现“.1”形式的数字。
 *
 * 最后返回的列表可以是任意顺序的。而且注意返回的两个数字中间（逗号之后）都有一个空格。
 *
 *
 *
 * 示例 1:
 * 输入: "(123)"
 * 输出: ["(1, 23)", "(12, 3)", "(1.2, 3)", "(1, 2.3)"]
 * 示例 2:
 * 输入: "(00011)"
 * 输出:  ["(0.001, 1)", "(0, 0.011)"]
 * 解释:
 * 0.0, 00, 0001 或 00.01 是不被允许的。
 * 示例 3:
 * 输入: "(0123)"
 * 输出: ["(0, 123)", "(0, 12.3)", "(0, 1.23)", "(0.1, 23)", "(0.1, 2.3)", "(0.12, 3)"]
 * 示例 4:
 * 输入: "(100)"
 * 输出: [(10, 0)]
 * 解释:
 * 1.0 是不被允许的。
 *
 *
 * 提示:
 *
 * 4 <= S.length <= 12.
 * S[0] = "(", S[S.length - 1] = ")", 且字符串 S 中的其他元素都是数字。
 */
public class _816_AmbiguousCoordinates {

    /**
     * 枚举
     * 我们先将原字符串 s 中的左右括号去掉，重新定义 s 为原字符串 s[1...(n−2)]，重新定义后的 s 长度为 n。
     *
     * 随后枚举逗号的位置 idx，分割出两个坐标，枚举范围为 [1,n−1]，含义为在 s[idx-1]后前，s[idx] 前面追加逗号。
     *
     * 此时左边部分字符串为 s[0, idx - 1]，右边部分字符串为 s[idx, n - 1]。
     *
     * 实现一个函数 List<String> getPosList(string str)，该函数返回使用字符串 str 构造的具体数值集合。
     *
     * 假设左边字符串 s[0, idx - 1] 搜索结果为 A，右边字符串 s[idx, n - 1] 搜索结果为 B，
     * 根据「乘法原理」，可知所有实际方案为 (x, y) 其中 x∈A，y∈B。
     *
     * 考虑如何实现 getPosList 函数（假设入参函数 str）：
     * 枚举字符串追加小数点的位置 idx，枚举范围为 [1,end−1]，含义为在 str[idx - 1] 后面追加小数点。
     * 小数点前面的部分不能包含前导零，小数点后面的部分不能包含后导零。注意记得把不添加小数点的合法方案也存入搜索集合。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/ambiguous-coordinates/solution/by-ac_oier-sbxl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public List<String> ambiguousCoordinates(String s) {
        String pos = s.substring(1, s.length() - 1);
        List<String> list = new ArrayList<>();
        // 枚举逗号位置，分割出两个坐标
        for (int i = 1; i < pos.length(); ++i) {
            // 在 s[i-1]后面，s[i]前面添加逗号
            // 左边部分字符串为 s[0, idx - 1]，
            String left = pos.substring(0, i);
            // 右边部分字符串为 s[idx, n - 1]。
            String right = pos.substring(i);
            // 对左右坐标取构造实际可能的数字，进行组合并拼接
            for (String x : getPosList(left)) {
                for (String y : getPosList(right)) {
                    list.add("(" + x + ", " + y + ")");
                }
            }
        }
        return list;
    }

    /**
     * 枚举小数点位置，由字符串形式恢复出可能的数字，不能包含前导0，后导0
     * 比如 “123”   -->    123, 12.3, 1.23
     *     "120"   -->    120, 由于最后一个位置是0，所以没办法添加小数点
     *     "012"   -->    0.12,  第一个数字是0，小数点只能在第一个0后面添加
     *     "0012"   -->    0.012,  第一个数字是0，小数点只能在第一个0后面添加，不能出现 00.12
     * @param str
     * @return
     */
    private List<String> getPosList(String str) {
        List<String> res = new ArrayList<>();
        // 不包含前导0，或者本身就是0，先加入自己
        if (str.charAt(0) != '0' || str.length() == 1) {
            res.add(str);
        }
        // 如果最后一位是0，没办法添加小鼠点
        if (str.charAt(str.length() - 1) == '0') {
            return res;
        }
        // 包含前导零，枚举小数点位置，s[0...i-1] 是小数点左部分，s[i...n-1]是小数点右部分
        for (int i = 1; i < str.length(); ++i) {
            // 左部分长度超过1，那么不能含有前导0，也就是不能出现010.xx这种
            // 此时直接退出，再往后划分没意义，左部分始终包括前导0
            if (i > 1 && str.charAt(0) == '0') {
                break;
            }
            // 拼接
            res.add(str.substring(0, i) + "." + str.substring(i));
        }
        return res;
    }
}
