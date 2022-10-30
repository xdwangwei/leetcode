package com.daily;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/10/30 12:23
 * @description: _784_LetterCasePermutation
 *
 * 784. 字母大小写全排列
 * 给定一个字符串 s ，通过将字符串 s 中的每个字母转变大小写，我们可以获得一个新的字符串。
 *
 * 返回 所有可能得到的字符串集合 。以 任意顺序 返回输出。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "a1b2"
 * 输出：["a1b2", "a1B2", "A1b2", "A1B2"]
 * 示例 2:
 *
 * 输入: s = "3z4"
 * 输出: ["3z4","3Z4"]
 *
 *
 * 提示:
 *
 * 1 <= s.length <= 12
 * s 由小写英文字母、大写英文字母和数字组成
 */
public class _784_LetterCasePermutation {

    // 去重
    private Set<String> resSet;

    public List<String> letterCasePermutation(String s) {
        resSet = new HashSet<>();
        // 从0号位置字符开始
        backTrack(s, 0, "");
        return new ArrayList<>(resSet);
    }


    /**
     * 回溯，对于每个字符，如果是非字母，那么保留并跳过，
     * 如果是字母，那么可以选择保留当前小写或改为大写，或者反过来
     *
     * 由于大小写字母的 ascii 值恰好差了32，因此可通过 异或操作快速完成大小写转换
     * @param s
     * @param idx
     * @param temp
     */
    private void backTrack(String s, int idx, String temp) {
        // 所有字符选择完毕
        if (idx == s.length()) {
            resSet.add(temp);
            return;
        }
        char c = s.charAt(idx);
        // 非字母，直接保留下一个
        if (!Character.isLetter(c)) {
            backTrack(s, idx + 1, temp + c);
            return;
        }
        // 不改变大小写
        backTrack(s, idx + 1, temp + c);
        // 改变大小写
        backTrack(s, idx + 1, temp + (char)(c ^ 32));
    }

    /**
     * 二进制枚举
     * 根据解法一可知，具体方案的个数与字符串 s1 存在的字母个数相关，
     * 当 s1 存在 m 个字母时，由于每个字母存在两种决策，总的方案数个数为 2^m 个。
     *
     * 因此可以使用「二进制枚举」的方式来做：
     * 使用变量 s 代表字符串 s1 的字母翻转状态，s 的取值范围为 [0, 1 << m)。
     * 若 s 的第 j 位为 0 代表在 s 中第 j 个字母不进行翻转，而当为 1 时则代表翻转。
     *
     * 每一个状态 s 对应了一个具体方案，通过枚举所有翻转状态 s，可构造出所有具体方案。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/letter-case-permutation/solution/by-ac_oier-x7f0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param str
     * @return
     */
    public List<String> letterCasePermutation2(String str) {
        char[] chars = str.toCharArray();
        int m = 0;
        // 统计字符个数，那么字符可以选择保留或者改变大小写，共 2 ^ m 种方案
        // m 的 二进制中每个位置j是否为1，代表str中第j个字符是否要切换大小写
        for (char c : chars) {
            if (Character.isLetter(c)) {
                m++;
            }
        }
        List<String> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        // 枚举所有可能性
        for (int s = 0; s < (1 << m); ++s) {
            // 对于每一种可能性，恢复对应的字符串
            // i 是 原串下标，j 是 m 的二进制表示下标，也是原串中所有字母的顺序下标
            for (int i = 0, j = 0; i < chars.length; ++i) {
                // 跳过数字字符，直接保留
                if (Character.isDigit(chars[i])) {
                    builder.append(chars[i]);
                    continue;
                }
                // 对于字母，在 str 中是第j个字母
                // 如果在s中，第j个位置为0，那么保留当前字母；否则，保留当前字母切换大小写
                builder.append(((s >> j++) & 1) == 0 ? chars[i] : (char)(chars[i] ^ 32));
            }
            // 加入这种方案
            list.add(builder.toString());
            // 清空
            builder.setLength(0);
        }
        // 方案
        return list;
    }

    public static void main(String[] args) {
        _784_LetterCasePermutation obj = new _784_LetterCasePermutation();
        System.out.println(obj.letterCasePermutation("a1b2"));
    }
}
