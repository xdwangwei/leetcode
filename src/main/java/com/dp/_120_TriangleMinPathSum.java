package com.dp;

import java.util.List;

/**
 * @Author: wangwei
 * @Description: 
 * @Time: 2019/11/19 周二 19:38
 **/
public class _120_TriangleMinPathSum {
    
    // 从下往上.考虑从最后一层向上，dp[i][j]表示最后一层到[i][j]的最短路径和
    // 至于原因微信公众号收藏动态规划
    public static int solution(List<List<Integer>> triangle){
        int layers = triangle.size(); // n层三角形，第一层tr.get(0).size()个，第二层tr.get(1).size()两个
        int[][] dp = new int[layers][]; // 应该是第一层1个，第二层2个，第三层3个，等效的
        List<Integer> lastLayer = triangle.get(layers - 1);
        // 最后一层
        for (int j=0; j < lastLayer.size(); j++) { //最后一层有layers个元素，和lastLayer.size()一样
            dp[layers-1][j] = lastLayer.get(j);
        }
        // 从倒数第二层往前
        for (int i = layers-2; i>=0; i--){
            List<Integer> layeri = triangle.get(i);
            for (int j = 0; j < layeri.size(); j++){ //下表是i表示第i+1层，有i+1个元素，layeri.size()==i+1
                dp[i][j] = Math.min(dp[i+1][j],dp[i+1][j+1]) + layeri.get(j);
            }
        }

        return dp[0][0];
    }

    public static void main(String[] args) {
        
    }
    
    
}
