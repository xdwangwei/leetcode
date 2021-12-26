package com.dp;

import java.util.HashMap;

/**
 * @author wangwei
 * 2021/12/26 9:49
 *
 * 假设你有一个特殊的键盘，包含以下的按键：
 *
 * key 1：（A）：在屏幕上打印一个 A
 *
 * key 2：（Ctrl-A）：选中整个屏幕
 *
 * key 3：（Ctrl-C）：复制选中区域到缓冲区
 *
 * key 4：（Ctrl-V）：将缓冲区内容输出到上次输入的结束位置，并显示在屏幕上
 *
 * 现在，你只可以按键N次（使用上述四种按键），请问屏幕上最多可以显示几个A？
 *
 * 样例1：
 *
 * 输入：N=3
 *
 * 输出：3
 *
 * 解释：我们最多可以在屏幕上显示3个A，通过如下顺序按键：A, A, A
 *
 * 样例2：
 *
 * 输入：N=7
 *
 * 输出：N=9
 *
 * 解释：我们最多可以在屏幕上显示9个A，通过如下顺序按键：A, A, A, Ctrl-A, Ctrl-C, Ctrl-V, Ctrl-V
 */
public class _651_FourKeysKeyboard {

    /**
     * 动态规划 自顶向下记忆化搜索，N 稍微大一点，此方法就算不出来了
     *
     * 如何在 N 次敲击按钮后得到最多的 A？我们穷举呗，对于每次按键，我们可以穷举四种可能，很明显就是一个动态规划问题。
     *
     * 第一种思路，复杂度太高，计算了很多实际上不应该统计的选择(最优解是有规律的)
     * 这种思路会很容易理解，但是效率并不高，我们直接走流程：对于动态规划问题，首先要明白有哪些「状态」，有哪些「选择」。
     *
     * 具体到这个问题，对于每次敲击按键，有哪些「选择」是很明显的：4 种，就是题目中提到的四个按键，分别是 A、C-A、C-C、C-V（Ctrl 简写为 C）。
     *
     * 接下来，思考一下对于这个问题有哪些「状态」？或者换句话说，我们需要知道什么信息，才能将原问题分解为规模更小的子问题？
     *
     * 你看我这样定义三个状态行不行：
     *      第一个状态是剩余的按键次数，用 n 表示；
     *      第二个状态是当前屏幕上字符 A 的数量，用 a_num 表示；
     *      第三个状态是剪切板中字符 A 的数量，用 copy 表示。
     *
     * 如此定义「状态」，就可以知道 base case：当剩余次数 n 为 0 时，a_num 就是我们想要的答案。
     *
     * 结合刚才说的 4 种「选择」，我们可以把这几种选择通过状态转移表示出来：
     *
     * 对于每一次按键，可以选择：
     *      dp(n - 1, a_num + 1, copy),    # A
     *          解释：按下 A 键，屏幕上加一个字符
     *          同时消耗 1 个操作数
     *      dp(n - 1, a_num + copy, copy), # C-V
     *          解释：按下 C-V 粘贴，剪切板中的字符加入屏幕
     *          同时消耗 1 个操作数
     *      dp(n - 2, a_num, a_num)        # C-A C-C
     *          解释：全选和复制必然是联合使用的，
     *          剪切板中 A 的数量变为屏幕上 A 的数量
     *          同时消耗 2 个操作数
     * 这样可以看到问题的规模 n 在不断减小，肯定可以到达 n = 0 的 base case，所以这个思路是正确的：
     * @param N
     * @return
     */
    private HashMap<String, Integer> memo;
    public int maxA(int N) {
        if (N < 7) {
            return N;
        }
        // 备忘录
        memo = new HashMap<>();
        // 初始时屏幕上0个A，剪切板上0个A
        return dp(N, 0, 0);
    }

    /**
     * 这种状态下，最终屏幕上最多能有几个A
     * @param n 剩余操作次数
     * @param aScreen 屏幕上A的个数
     * @param aCpopy 剪贴板上A的个数
     * @return
     */
    private int dp(int n, int aScreen, int aCpopy) {
        if (n <= 0) {
            return aScreen;
        }
        String key = n + "," + aScreen + "," + aCpopy;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        int val = Math.max(
                // 按下 A 键，操作次数减一，屏幕上加一个A，剪贴板上A个数不变
                // 按下 C-V 粘贴，操作次数减一，屏幕上增加了上一次剪贴板上的A，剪切板中的A不变
                // 全选和复制必然是联合使用的，操作次数减二，屏幕上A的个数不变，剪切板中 A 的数量变为此时屏幕上 A 的数量
                Math.max(dp(n - 1, aScreen + 1, aCpopy), dp(n - 1, aScreen + aCpopy, aCpopy)),
                dp(n - 2, aScreen, aScreen));
        memo.put(key, val);
        return val;
    }

    /**
     * 动态规划自底向上，
     * 记忆化搜索虽然解决了子问题重复计算的问题，但是由于考虑的是所有的选择，没有择优，所以复杂度特别高，效率低下。
     *
     * 这种思路稍微有点复杂，但是效率高。继续走流程，「选择」还是那 4 个，但是这次我们只定义一个「状态」，也就是剩余的敲击次数 n。
     *
     * 这个算法基于这样一个事实，最优按键序列一定只有两种情况：
     *      要么一直按 A：A,A,...A（当 N 比较小时）。
     *      要么是这么一个形式：A,A,...C-A,C-C,C-V,C-V,...C-V（当 N 比较大时）。
     * N 比较小时，C-A C-C C-V 这一套操作的代价相对比较高，可能不如一个个按 A；
     * 而当 N 比较大时，后期 C-V 的收获肯定很大。这种情况下整个操作序列大致是：开头连按几个 A，然后 C-A C-C 组合再接若干 C-V，然后再 C-A C-C 接着若干 C-V，循环下去。
     *
     * 换句话说，最优情况，【最后一次按键要么是 A 要么是 C-V】。明确了这一点，可以通过这两种情况来设计算法：
     *
     *      int[] dp = new int[N + 1];
     *      // 定义：dp[i] 表示 i 次操作后最多能显示多少个 A
     *      for (int i = 0; i <= N; i++)
     *          dp[i] = max(
     *             这次按 A 键，
     *             这次按 C-V
     *         )
     * 对于「按 A 键」这种情况，就是状态 i - 1 的屏幕上新增了一个 A 而已，很容易得到结果：dp[i] = dp[i - 1] + 1;
     * 但是，如果要按 C-V，还要考虑之前是在哪里 C-A C-C 的。
     * 刚才说了，最优的操作序列一定是 C-A C-C 接着若干 C-V，所以我们用一个变量 j 作为若干 C-V 的起点（应该看作C-C的位置）。
     * 那么 j - 1 、j 操作就应该是 C-A C-C 了，此时 C-V 得到的数量应该等于 C-A C-C之间的屏幕上的数量
     * @param N
     * @return
     */
    public int maxA2(int N) {
        if (N < 7) {
            return N;
        }
        int[] dp = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            // 按 A 键
            dp[i] = dp[i - 1] + 1;
            // 或者按 C-V，按 C-V就要考虑是从何时按的C-C
            // j是C-C的位置，j - 2就是j之前的最后按键A的位置
            for (int j = 2; j < i; j++) {
                // 全选 & 复制 dp[j-2]，连续粘贴 i - j 次
                // 屏幕上共 dp[j - 2] * (i - j + 1) 个 A（包含j位置前的A的总数，所以这里 + 1）
                // j一直小于i，说明 j - 2 的位置，一定是按键A，即j - 2位置上表明当前位置上A的最大个数
                dp[i] = Math.max(dp[i], dp[j - 2] * (i - j + 1));
            }
        }
        // N 次按键之后最多有几个 A？
        return dp[N];
    }

    public static void main(String[] args) {
        _651_FourKeysKeyboard obj = new _651_FourKeysKeyboard();
        System.out.println(obj.maxA(20));
        System.out.println(obj.maxA2(20));
        // 自顶向下算不出来了，垃圾
        // System.out.println(obj.maxA(50));
        System.out.println(obj.maxA2(50));
    }
}
