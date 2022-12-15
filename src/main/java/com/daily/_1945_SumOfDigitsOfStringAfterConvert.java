package com.daily;

/**
 * @author wangwei
 * @date 2022/12/15 11:56
 * @description: _1945_SumOfDigitsOfStringAfterConvert
 *
 * 1945. 字符串转化后的各位数字之和
 * 给你一个由小写字母组成的字符串 s ，以及一个整数 k 。
 *
 * 首先，用字母在字母表中的位置替换该字母，将 s 转化 为一个整数（也就是，'a' 用 1 替换，'b' 用 2 替换，... 'z' 用 26 替换）。接着，将整数 转换 为其 各位数字之和 。共重复 转换 操作 k 次 。
 *
 * 例如，如果 s = "zbax" 且 k = 2 ，那么执行下述步骤后得到的结果是整数 8 ：
 *
 * 转化："zbax" ➝ "(26)(2)(1)(24)" ➝ "262124" ➝ 262124
 * 转换 #1：262124 ➝ 2 + 6 + 2 + 1 + 2 + 4 ➝ 17
 * 转换 #2：17 ➝ 1 + 7 ➝ 8
 * 返回执行上述操作后得到的结果整数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "iiii", k = 1
 * 输出：36
 * 解释：操作如下：
 * - 转化："iiii" ➝ "(9)(9)(9)(9)" ➝ "9999" ➝ 9999
 * - 转换 #1：9999 ➝ 9 + 9 + 9 + 9 ➝ 36
 * 因此，结果整数为 36 。
 * 示例 2：
 *
 * 输入：s = "leetcode", k = 2
 * 输出：6
 * 解释：操作如下：
 * - 转化："leetcode" ➝ "(12)(5)(5)(20)(3)(15)(4)(5)" ➝ "12552031545" ➝ 12552031545
 * - 转换 #1：12552031545 ➝ 1 + 2 + 5 + 5 + 2 + 0 + 3 + 1 + 5 + 4 + 5 ➝ 33
 * - 转换 #2：33 ➝ 3 + 3 ➝ 6
 * 因此，结果整数为 6 。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 100
 * 1 <= k <= 10
 * s 由小写英文字母组成
 * 通过次数18,189提交次数26,161
 */
public class _1945_SumOfDigitsOfStringAfterConvert {


    /**
     * 方法一：模拟
     * 思路与算法
     *
     * 我们根据题目的要求模拟即可。
     *
     * 在第一次操作前，我们根据给定的字符串 s，得到对应的数字串 str。
     *
     * 随后我们进行 k 次操作，每次操作将 str 的每个数位进行累加，得到新的数字串。
     *
     * 最后将经过k次操作后的串str转为int返回
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/sum-of-digits-of-string-after-convert/solution/zi-fu-chuan-zhuan-hua-hou-de-ge-wei-shu-bhdx4/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param k
     * @return
     */
    public int getLucky(String s, int k) {
        StringBuilder builder = new StringBuilder();
        // 根据字符串 s，得到对应的数字串 str。
        for (char c : s.toCharArray()) {
            builder.append(c - 'a' + 1);
        }
        String str = builder.toString();
        // 进行k次转换
        while (k-- > 0) {
            char[] chars = str.toCharArray();
            int sum = 0;
            // 每一位数字字符加起来
            for (char aChar : chars) {
                sum += aChar - '0';
            }
            // 重新转为字符串，进行下一次转换
            str = Integer.toString(sum);
        }
        // 经过k次转换后，转为int，返回
        return Integer.parseInt(str);
    }
}
