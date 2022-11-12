package com.daily;

/**
 * @author wangwei
 * @date 2022/11/12 11:05
 * @description: _790_DominoAndTrominoTiling
 *
 * 790. 多米诺和托米诺平铺
 * 有两种形状的瓷砖：一种是 2 x 1 的多米诺形，另一种是形如 "L" 的托米诺形。两种形状都可以旋转。
 *
 *
 *
 * 给定整数 n ，返回可以平铺 2 x n 的面板的方法的数量。返回对 10^9 + 7 取模 的值。
 *
 * 平铺指的是每个正方形都必须有瓷砖覆盖。两个平铺不同，当且仅当面板上有四个方向上的相邻单元中的两个，使得恰好有一个平铺有一个瓷砖占据两个正方形。
 *
 *
 *
 * 示例 1:
 *
 *
 *
 * 输入: n = 3
 * 输出: 5
 * 解释: 五种不同的方法如上所示。
 * 示例 2:
 *
 * 输入: n = 1
 * 输出: 1
 *
 *
 * 提示：
 *
 * 1 <= n <= 1000
 */
public class _790_DominoAndTrominoTiling {

    /**
     * 动态规划
     *
     * 我们将平铺看成一个从左到右铺上瓷砖的过程，
     * 假设第 i 列之前都被瓷砖覆盖，在第 i 列之后都没有被瓷砖覆盖。
     * 那么第 i 列的正方形有四种被覆盖的情况：
     *
     * 一个正方形都没有被覆盖，记为状态 0；
     * 只有上方的正方形被覆盖，记为状态 1；
     * 只有下方的正方形被覆盖，记为状态 2；
     * 上下两个正方形都被覆盖，记为状态 3。
     *
     * 使用dp[i][n]来表示将瓷砖铺成第i列为n状态的所有方法。共n列，最后返回 dp[n-1][3]
     *
     * 如果第i列的状态为0，即一个瓷砖都没有，
     *      因为第i列之前都被瓷砖覆盖了，所以其状态是与第i-1列的3状态相同的，即dp[i][0]=dp[i-1][3]。
     *
     * 如果第i列的状态为1，即只有上方有一个瓷砖，
     *      要想成为这种情况，有两种方式：在第i-1为2状态下时，横着铺一块多米诺形瓷砖，或者在i第i-1列为0状态下时铺一个托米诺形瓷砖，即dp[i][1]=dp[i-1][0]+dp[i-1][2]。
     *
     * 如果第i列的状态为2，即只有下方的一个瓷砖，
     *      与状态为1同理。有两种方式：在第i-1为1状态下时，横着铺一块多米诺形瓷砖，或者在i第i-1列为0状态下时铺一个托米诺形瓷砖，即dp[i][1]=dp[i-1][0]+dp[i-1][1]。
     *
     * 如果第i列的状态为3，即第i列已经铺满了瓷砖，
     *      此时需要在第i-1列的3状态下竖着铺一块多米诺形瓷砖，或者在第i-1列的0状态下横着铺二块多米诺形瓷砖，或者在第i-1列的1、2状态下铺一个托米诺形瓷砖，dp[i][3]=dp[i-1][0]+dp[i-1][1]+dp[i-1][2]+dp[i-1][3]
     *
     * 不难注意到，当i=0时，i-1会越界，所以需要为i=0时专门赋值。
     * dp[i][0]表示第i列上下位置都空着的方案数，那就是不放置瓷砖，默认为1；
     * dp[i][3]表示第i列上下位置都占满的方案数，那就是竖着放1号瓷砖，一种方案，也为1；
     * 多米诺形瓷砖与托米诺形瓷砖都无法在第0列只铺出一半，所以dp[i][1]与dp[i][2]都赋值为0；
     *
     * 作者：zhishangju
     * 链接：https://leetcode.cn/problems/domino-and-tromino-tiling/solution/xiao-bai-yi-dong-de-guan-fang-ti-jie-jie-7tjg/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int numTilings(int n) {
        final int mod = (int) (1e9 + 7);
        int[][] dp = new int[n + 1][4];
        // base case
        // 第一列上下都空着，就是什么也不铺，一种方案
        // 第一列占满，只有一种铺法
        // 第一列只占上或者下，没办法完成
        dp[0][0] = dp[0][3] = 1;
        // dp[0][1] = 0; dp[0][2] = 0;
        // 递推
        for (int i = 1; i < n; ++i) {
            // 第i列，一个正方形都没有被覆盖，记为状态 0；
            dp[i][0] = dp[i - 1][3];
            // 只有上方的正方形被覆盖，记为状态 1；
            dp[i][1] = (dp[i - 1][2] + dp[i - 1][0]) % mod;
            // 只有下方的正方形被覆盖，记为状态 2；
            dp[i][2] = (dp[i - 1][1] + dp[i - 1][0]) % mod;
            // 上下两个正方形都被覆盖，记为状态 3。
            dp[i][3] = (((dp[i - 1][0] + dp[i - 1][3]) % mod + dp[i-1][1]) % mod + dp[i - 1][2]) % mod;
        }
        // 返回前n列都被铺满
        return dp[n - 1][3];
    }

    public static void main(String[] args) {
        _790_DominoAndTrominoTiling obj = new _790_DominoAndTrominoTiling();
        System.out.println(obj.numTilings(5));
    }
}
