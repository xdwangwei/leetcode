package com.daily;

/**
 * @author wangwei
 * @date 2022/6/3 16:57
 * @description: _829_ConsecutiveNumbersSum
 *
 * 829. 连续整数求和
 * 给定一个正整数 n，返回 连续正整数满足所有数字之和为 n 的组数 。
 *
 *
 *
 * 示例 1:
 *
 * 输入: n = 5
 * 输出: 2
 * 解释: 5 = 2 + 3，共有两组连续整数([5],[2,3])求和后为 5。
 * 示例 2:
 *
 * 输入: n = 9
 * 输出: 3
 * 解释: 9 = 4 + 5 = 2 + 3 + 4
 * 示例 3:
 *
 * 输入: n = 15
 * 输出: 4
 * 解释: 15 = 8 + 7 = 4 + 5 + 6 = 1 + 2 + 3 + 4 + 5
 *
 *
 * 提示:
 *
 * 1 <= n <= 10^9
 */
public class _829_ConsecutiveNumbersSum {


    /**
     * 暴力搜索，超时
     *
     * 数论
     * 假设我们存在某个连续段之和为 n，假定该连续段首项为 a，项数为 k，则尾项为 a + k - 1
     * 根据「等差数列求和」可知：(a + a+k−1) × k/2 = n
     *
     * 简单变形可得：
     *
     * (2a+k−1) × k = 2n  ⇔  2a = 2n/k − k + 1
     *
     * 我们已知和为 n, 那么就要求上述方程的正整数解，但有两个未知数 a,k，
     * 一种简单的思路是：枚举其中一个数，那么另外一个数字就能计算出来了，我们只需验证得到的解是否合法即可，
     * 这里的合法是指解出的 a,k 应该是正整数。
     *
     * 相比之下，枚举连续数字的个数 k 比枚举起点 a 要容易一些（关于k是2次，），将公式变形，即
     *
     * a = [2n/k − k + 1] / 2
     *
     * 那么还剩下一个问题是，枚举到什么时候终止呢？换而言之，k 最大取值是多少？
     *
     * 不难发现，在和一定的情况下，以 1 为起点得到的数列是最长的，即 a=1 代入时得到的 k 最大，所以当我们计算到 a<1 即可终止。
     * 并且 从 (2a+k−1) × k = 2n 可以看出， k 必然为 2n 的因子
     *
     * 作者：meteordream
     * 链接：https://leetcode.cn/problems/consecutive-numbers-sum/solution/-by-meteordream-sfib/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param n
     * @return
     */
    public int consecutiveNumbersSum(int n) {
        int cnt = 0, k = 1;
        // 枚举连续序列长度，虽然n的取值可能 超出了INT_MAX，但是 当a<1时会退出循环，当a=1时，k约等于sqrt(2n)，此时k绝对不会超出int_max，所以我们可以忽略
        while (k <= n) {
            // 序列起点是a，终点是 a+k-1，等差数列求和 sum=(a + a+k-1) * k/2 = n，推导a
            // a = [2n/k − k + 1] / 2
            int a = (2 * n / k - k + 1) / 2;
            // 序列起点最小正整数是1
            if (a < 1) {
                break;
            }
            // 保证目标和的确是 n
            if ((a + a + k - 1) * k / 2 == n) {
                cnt++;
            }
            // 别忘记更新k
            k++;
        }
        return cnt;
    }

    /**
     * a = [2n/k − k + 1] / 2
     * 第二种写法，枚举序列长度k的时候，k越长，起点a应该越小，但是a是正整数，所以当 a = 1时退出
     * 反过来，a = 1时，2n/k -k = 1, k ~= sqrt(2n)
     * 所以k的枚举范围是 [1,sqrt(2n)]
     * 从 (2a+k−1) × k = 2n 可以看出， k 必然为 2n 的因子
     * @param n
     * @return
     */
    public int consecutiveNumbersSum2(int n) {
        int cnt = 0;
        // sum[i...j] == n
        // 枚举 j
        for (int k = 1; k * k <= 2 * n; ++k) {
            // 从 (2a+k−1) × k = 2n 可以看出， k 必然为 2n 的因子
            if (2 * n % k != 0) {
                continue;
            }
            // 序列起点是a，终点是 a+k-1，等差数列求和 sum=(a + a+k-1) * k/2 = n，推导a
            // a = [2n/k − k + 1] / 2
            // a必然为正整数
            if ((2 * n / k - k + 1) % 2 == 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        _829_ConsecutiveNumbersSum obj = new _829_ConsecutiveNumbersSum();
        obj.consecutiveNumbersSum(5);
    }


}
