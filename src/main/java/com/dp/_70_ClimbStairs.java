package com.dp;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/19 周二 19:27
 **/
public class _70_ClimbStairs {
    public static int solution(int n){
        if(n <= 2)
            return n;
        int[] dp = new int[n+1];
        dp[1] = 1; // 从0级跳到1级
        dp[2] = 2; // 从0级跳到2级
        for (int i = 3; i <= n; i++){
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }
    
    public static void main(String[] args) {
        System.out.println(solution(4));
    }
}
