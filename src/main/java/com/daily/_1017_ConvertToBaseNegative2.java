package com.daily;

/**
 * @author wangwei
 * @date 2023/4/7 22:57
 * @description: _1017_ConvertToBaseNegative2
 *
 * 1017. 负二进制转换
 * 给你一个整数 n ，以二进制字符串的形式返回该整数的 负二进制（base -2）表示。
 *
 * 注意，除非字符串就是 "0"，否则返回的字符串中不能含有前导零。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 2
 * 输出："110"
 * 解释：(-2)2 + (-2)1 = 2
 * 示例 2：
 *
 * 输入：n = 3
 * 输出："111"
 * 解释：(-2)2 + (-2)1 + (-2)0 = 3
 * 示例 3：
 *
 * 输入：n = 4
 * 输出："100"
 * 解释：(-2)2 = 4
 *
 *
 * 提示：
 *
 * 0 <= n <= 109
 * 通过次数21,579提交次数33,191
 *
 */
public class _1017_ConvertToBaseNegative2 {

    /**
     * 方法：进制转换 + 修改
     *
     * n 是 非负数
     *
     * 【十进制转x进制】：
     *
     * 本质：
     *     n = a * (x)^5 + b * (x)^4 + c * (x)^3 + d * (x)^2 + e * (x)^1 + f * (x)^0
     *     求 n 的 x 进制表示，本质上就是得到这里的系数 abcdef
     *
     * 对于一个数字 n 的 x 进制的表达式 abcdef 来说，更本质的x进制求法应该为：
     *
     *         得到最低位的值f；
     *
     *         f = n % x；ans.append(f)
     *
     *         n = n - f = a * (x)^5 + b * (x)^4 + c * (x)^3 + d * (x)^2 + e * (x)^1
     *         n = n / x = a * (x)^4 + b * (x)^3 + c * (x)^2 + d * (x)^1 + e * (x)^0
     *
     *         此时 e 变成了 n 的最低位
     *
     *         重复上述步骤，直到 n 为 0，最终 ans = (fedcba)，返回 ans.reverse() 即可
     *
     * 问题在于，若 x 为复数，比如 x = -2，那么 n % x 可能会出现的值有 1，0，-1
     * 是否会出现 -1，这取决于不同语言的特性
     * 比如 在 java 中，  19 % -2 = 1，  也就是 19 / -2 = -9 余 1
     * 而   在 python 中， 19 % -2 = -1，也就是 19 / -2 = -10 余 -1
     *
     * 然而二进制表示每个位的取值只能是0或1，是不允许出现 -1 的，而 我们得到 二进制表示 的计算过程，每次都保留的是 余数
     *
     * 因此，对于 python 这种 余数为 复数的情况，我们需要将其转为整数，如何操作：
     *
     * 以 x = -2， n = 19 为例，先求 最低位二进制 f
     *
     *      f = 19 % -2
     *      n = n - f
     *      n = n / x
     *      重复上述步骤
     *
     * 如果是 java
     *
     *      f = 19 % -2  = 1；  ans.append(1)
     *      n = n - f    = 18
     *      n = n / x    = -9
     *      重复上述步骤
     *
     * 如果是 python
     *
     *      f = 19 % -2  = -1；  ans.append(-1)
     *      n = n - f    = 20
     *      n = n / x    = -10
     *      重复上述步骤
     *
     *      想要将 f 矫正为 1，那么 应该满足，也就是 n(最后一步得到的n，商，下一轮循环的n) * -2 + 1 = 19，那么 n = -9
     *      也就是 从 矫正前的 -10 变为 -9，即 n = n / x + 1
     *
     *
     * 作者：yvette-na
     * 链接：https://leetcode.cn/problems/convert-to-base-2/solution/jin-zhi-biao-da-shi-de-ben-zhi-bu-tong-y-1x4a/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @return
     */
    public String baseNeg2(int n) {
        // 0 或 1直接返回 字符串形式
        if (n == 0 || n == 1) {
            return String.valueOf(n);
        }
        // n = a * (x)^5 + b * (x)^4 + c * (x)^3 + d * (x)^2 + e * (x)^1 + f * (x)^0
        // 求 n 的 -2 进制表示，本质上就是得到这里的系数 abcdef，其中 x = -2
        StringBuilder sb = new StringBuilder();
        // n 为 0 停止
        while (n != 0) {
            // 当前 最低位 f
            int rest = n % -2;
            // 余数可能是 -1，0，1，但是二进制最低位 只可以取 0 或 1
            // 余数是 0 或 1，正常，保留，下一步 n /= -2
            if (rest != -1) {
                sb.append(rest);
                n /= -2;
                continue;
            }
            // 如果 余数是 -1，需要 修正为1，修正为 1 后，余数 n 由 n/-2 变为 n/-2 + 1
            sb.append(1);
            n = n / -2 + 1;
        }
        // 得到的是 fdecba，需要逆序返回
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println(19 % -2);
    }
}
