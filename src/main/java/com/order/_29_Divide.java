package com.order;

/**
 * @Author: wangwei
 * @Description: _29_Divide
 * @Time: 2019/12/13 周五 09:40
 *
 * 29. 两数相除
 * 给定两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
 *
 * 返回被除数 dividend 除以除数 divisor 得到的商。
 *
 * 整数除法的结果应当截去（truncate）其小数部分，例如：truncate(8.345) = 8 以及 truncate(-2.7335) = -2
 *
 *
 *
 * 示例 1:
 *
 * 输入: dividend = 10, divisor = 3
 * 输出: 3
 * 解释: 10/3 = truncate(3.33333..) = truncate(3) = 3
 * 示例 2:
 *
 * 输入: dividend = 7, divisor = -3
 * 输出: -2
 * 解释: 7/-3 = truncate(-2.33333..) = -2
 *
 *
 * 提示：
 *
 * 被除数和除数均为 32 位有符号整数。
 * 除数不为 0。
 * 假设我们的环境只能存储 32 位有符号整数，其数值范围是 [−231,  231 − 1]。本题中，如果除法结果溢出，则返回 231 − 1。
 **/
public class _29_Divide {
	
	/**
	 * 给定两个整数，被除数dividend和除数divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
	 * 返回被除数dividend除以除数divisor得到的商
	 * 注意 Integer.MIN_VALUE / -1 = Integer.MAX_VALUE + 1，但int值无法保存，会溢出，自动变为Integer.MIN_VALUE
	 * 理解除法 a / b 的意思就是：a 包含了几个 b
	 * 
	 * 递归
	 * @param dividend
	 * @param divisor
	 * @return
	 */
	public int solution1(int dividend, int divisor) {
		if (1 == divisor) return dividend; // 任何数除以1得本身
		if (-1 == divisor) { // 除数是-1时
			if (dividend > Integer.MIN_VALUE) { // 被除数大于Integer.MIN_VALUE, 相反数就不会溢出整形最大正数
				return -dividend;
			} // 被除数等于最小赋值，相反数溢出最大正数，直接返回最大正数
			return Integer.MAX_VALUE;
		}
		// 得到符号位，两种方式结果相反
		// boolean sign = (dividend > 0 && divisor > 0) || (dividend < 0 && divisor < 0);
		boolean sign = (dividend > 0) ^ (divisor > 0);
		// 前面只处理了Integer.MIN_VALUE / -1, 但除数是其他数的情况存在，不能直接直接转为正数
		// 先转为long型，再取绝对值，如果直接取绝对值并赋值给long型变量，在取绝对值的过程中已经溢出
		long a = dividend, b = divisor;
		a = a > 0 ? a : -a;
		// 被除数可能为最小负数
		b = b > 0 ? b : -b;
		// 结果可能为最小负值的情况已经排除，因此这里可以直接用int接收并返回
		int res = div(a, b);
		if (sign) return -res; //负号
		return res; 
	}

	/**
	 * 计算 a / b
	 * @param a
	 * @param b
	 * @return
	 */
	private int div(long a, long b) {
		if (a < b) return 0;
		// 既然 a >= b，结果至少是1
		int count = 1;
		long temp = b;
		// 被除数大于2倍的除数，结果就可以翻倍
		// while (temp + temp <= a) {
		// 	count = count + count;
		// 	temp = temp + temp;
		// }
		// 移位效率高
		while ((temp << 1) <= a) {
			count = count << 1;
			temp = temp << 1;
		}
		// 被除数不大于此时除数temp的二倍了,但被除数此次除数(上次除数的二倍)，就看被除数减去上次除数
		// 剩下的部分还能容纳几个除数(只可能是0或1)
		return count + div(a - temp, b);
	}

	/**
	 * 因为转成正数计算比较复杂，所以转成负数计算(正数转负数不会溢出，只需返回结果时注意即可)
	 * @param dividend
	 * @param divisor
	 * @return
	 */
	public int solution2(int dividend, int divisor) {
		// 记录符号位，同号为0，异号为1
		boolean sign = (dividend > 0) ^ (divisor > 0);
		// 转成负数，不会溢出
		dividend = dividend < 0 ? dividend : -dividend;
		divisor = divisor < 0 ? divisor: -divisor;
		// 返回结果，默认为0
		int res = 0;
		// 因为是负数 / 负数，只有被除数小于等于除数，商才不会是0
		while (dividend <= divisor) {
			// tempRes记录当前的被除数能包含多少个除数，可以用1做初始值，最后res += tempRes
			// 也可以用 -1做初始值，最后 res += tempRes，但负数能保存的结果比正数多1
			// 结果可能为最小负数，所以我们用负的1的个数记录结果
			int tempRes = -1; // 肯定能包含一个除数
			int tempDivisro = divisor;
			// 把上面递归函数思想用尽来，为避免一直进行加法造成超时，我们每次判断是不是能包含两个除数
			while (dividend <= (tempDivisro << 1)) {
				// 当前被除数 -3 --> -6，因为是int,如果左移一位后比int_min小了，会溢出
				if (tempDivisro < (Integer.MIN_VALUE >> 1)) break;
				tempDivisro = tempDivisro << 1;
				tempRes = tempRes << 1;
			}
			// dividend中tempDivisro部分能包含tempRes个除数
			res += tempRes;
			// 把这部分减掉，算剩下部分能包含多少个除数
			dividend -= tempDivisro;
		}
		if (sign) return res; // 异号情况下，结果最小为最小负数，我们是用负数保存的结果，不存在溢出
		// 同号，我们用负数保存的结果，转成正数就可能溢出，
		else {
			if (res > Integer.MIN_VALUE) return -res;
			else return Integer.MAX_VALUE;
		}
	}
	
	public static void main(String[] args) {
		// System.out.println(new _29_Divide().solution1(Integer.MIN_VALUE, 2));
		// System.out.println(1 ^ 1);
	}
}
