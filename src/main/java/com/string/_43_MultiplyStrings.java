package com.string;

/**
 * @author wangwei
 * 2020/4/20 11:01
 * <p>
 * 给定两个以字符串形式表示的非负整数num1和num2，返回num1和num2的乘积，它们的乘积也表示为字符串形式。
 * <p>
 * 示例 1:
 * <p>
 * 输入: num1 = "2", num2 = "3"
 * 输出: "6"
 * 示例2:
 * <p>
 * 输入: num1 = "123", num2 = "456"
 * 输出: "56088"
 * 说明：
 * <p>
 * num1和num2的长度小于110。
 * num1 和num2 只包含数字0-9。
 * num1 和num2均不以零开头，除非是数字 0 本身。
 * 不能使用任何标准库的大数类型（比如 BigInteger）或直接将输入转换为整数来处理。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/multiply-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _43_MultiplyStrings {

    /**
     * 普通竖式(乘法)
     * 将num2每一位与num1的乘机保存下来，分别进行累加
     * 竖式运算思想，以 num1 为 123，num2 为 456 为例分析：
     * 123       123     123      123           0         738         6888
     * 456         6      5       4         + 738      + 6150    +   49200
     * 738       738    615     492           738        6888        56088
     * 615
     * 492
     * 56088
     * <p>
     * 遍历 num2,从最后一位往前 每一位与 num1 进行相乘，将每一步的结果进行累加。
     * 注意每一次的乘法结果需要一位，最后一位--补0个0  倒数第二位--补1个0   倒数第3位--补2个0
     *
     * @param num1
     * @param num2
     * @return
     */
    public String multiply(String num1, String num2) {
        // 某个乘数为0.结果为0
        // 不然下面这种做法，在遇到 类似 123 * 0 时会返回 "0000"
        if (num1.equals("0") || num2.equals("0")) return "0";
        String res = "0";
        for (int i = num2.length() - 1; i >= 0; i--) {
            // 保留当前位与num1相乘的结果
            StringBuilder tempBuilder = new StringBuilder();
            // 不同位置的乘积需要向左错位，也就是补0
            // 最后一位--错0位-补0个0，倒数第二位--向左错1位--补1个0
            for (int j = num2.length() - i - 1; j > 0; j--)
                tempBuilder.append(0);
            // 求这一位与num1的乘积结果
            int n2 = num2.charAt(i) - '0';
            int incr = 0, sum = 0;
            for (int j = num1.length() - 1; j >= 0 || incr > 0; j--) {
                int n1 = j >= 0 ? num1.charAt(j) - '0' : 0;
                sum = n1 * n2 + incr;
                tempBuilder.append(sum % 10);
                incr = sum / 10;
            }
            // 求当前乘积与上个结果(res)的和，再赋值给res
            // 注意当前乘积是反着保存的
            res = addStrings(res, tempBuilder.reverse().toString());
        }
        return res;
    }



    /**
     * 两字符串相加
     *
     * @param num1
     * @param num2
     */
    private String addStrings(String num1, String num2) {
        StringBuilder builder = new StringBuilder();
        int incr = 0;
        for (int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0 || incr > 0; i--, j--) {
            int n1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int n2 = j >= 0 ? num2.charAt(j) - '0' : 0;
            int sum = n1 + n2 + incr;
            builder.append(sum % 10);
            incr = sum / 10;
        }
        return builder.reverse().toString();
    }

    /**
     * 优化竖式
     * 首先，两数相乘，最终结果的长度【不会超过】 num1.length + num2.length
     * 我们可以考虑结果的每一位来自于哪些部分，但这样的思路比较复杂，对于结果的每一位都要去统计好几部分
     * 因此我们可以反向考虑 两个乘数每一位乘积 最后会落到结果的哪个位置
     * 把所有情况考虑完后，再对结果数组进行统一处理(取余、进位)
     * 我们再来考虑 落到哪个位置的 问题,
     *        1   2   3   i[0,2]
     *        4   5   6   j[0,2]   假设用长度为3+3=6的数组保存结果的每一位
     *
     *        6  12  18      [0][0] 1 * 4 落到结果的 第1位
     *      5 10 15          [1][1] 2 * 5 落到结果的 第3位
     *    4 8 12             [2][2] 3 * 6 落到结果的 第5位
     *
     *  0 4 13 28 27 18      也就是说 [i][j] 落到结果的第 i + j + 1位，前提是我们选择右对齐
     *  也就是说我们用6位的数字保存每一位置的和，其实结果只有5位，可以选择最后一位是0或者第一位是0，然后再处理进位
     *  但我们应该选择右对齐，因为第1个位置可能向前进位，而且本来乘法两个乘数最后一位相乘的结果就应该在最后一个位置
     * @param num1
     * @param num2
     * @return
     */
    public String multiply2(String num1, String num2) {
        // 某个乘数为0.结果为0
        // 不然下面这种做法，在遇到 类似 123 * 0 时会返回 "0000"
        if (num1.equals("0") || num2.equals("0")) return "0";
        // 保留每一个位置的中间和
        int[] sum = new int[num1.length() + num2.length()];
        for (int i = 0; i < num1.length(); i++) {
            for (int j = 0; j < num2.length(); j++) {
                // 落到对应位置
                sum[i + j + 1] += (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
            }
        }
        // 再处理每一个位置
        for (int i = sum.length - 1; i > 0 ; i--) {
            if (sum[i] > 9){
                // 向前进位
                sum[i - 1] += sum[i] / 10;
                sum[i] = sum[i] % 10;
            }
        }
        // 得到最终结果
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sum.length; i++) {
            if (i == 0 && sum[i] == 0) continue;
            builder.append(sum[i]);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        _43_MultiplyStrings obj = new _43_MultiplyStrings();
        // System.out.println(obj.multiply("0", "0"));
        // System.out.println(obj.multiply("2", "3"));
        System.out.println(obj.multiply2("123", "456"));
    }
}
