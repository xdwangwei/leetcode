package com.dp;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/19 周二 20:00
 **/
public class _62_DiffPath {
    public static int solution1(int m, int n){
        int[][] dp = new int[m][n];
        for (int j = 0; j<n; j++){ //第一行，从原点到任意元素只能一直向右，一种走法
            dp[0][j] = 1;
        }
        for (int i = 0; i<m; i++){ //第一列，从原点到任意元素只能一直向下，一种走法
            dp[i][0] = 1;
        }
        for (int i = 1; i<m; i++){
            for (int j = 1; j<n; j++){
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        return dp[m-1][n-1];
    }
    // 由排列组合，每次不是 右1 就是 下1 ，这就是 m-1个横 和 n-1个竖 的组合数
    // C(m+n-2,n-1) 
    public static int solution2(int m, int n){
        return (int) (factorial(m+n-2)/factorial(m-1)/factorial(n-1));
    }
    
    public static long factorial(int n){
        if(n == 1) 
            return 1;
        else 
            return n * factorial(n-1);
    }

    public static void main(String[] args) {
/*        输入: m = 3, n = 2
        输出: 3
        输入: m = 7, n = 3
        输出: 28*/
        // System.out.println(solution1(7,3));
        System.out.println(solution2(7,3));
    }
}
