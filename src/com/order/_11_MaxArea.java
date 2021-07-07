package com.order;

import java.security.PublicKey;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/29 周五 15:39
 **/
public class _11_MaxArea {

    public static int solution(int[] height) {
        int lo = 0, hi = height.length - 1, maxArea = 0;
        while (lo < hi) {
            maxArea = Math.max(maxArea, Math.min(height[lo], height[hi]) * (hi - lo));
            if (height[lo] < height[hi]) ++lo;
            else --hi;
        }
        return maxArea;
    }
}
