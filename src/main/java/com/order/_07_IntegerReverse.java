package com.order;

import java.util.Stack;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/24 周日 21:58
 **/
public class _07_IntegerReverse {
    
    public static int solution(int num){
        int res = 0;
        // 不能先取绝对值，之后根据输入的数的正负判断输出符号
        // 补码。如-128--127,直接取绝对值-128，直接益处，后面结果无意义
        // 除非先排除最小负数
        // if (num == Integer.MIN_VALUE) return 0;
/*        int cop = Math.abs(num);
        while (cop != 0){
            int pop = cop % 10;
            cop = cop/10;
            // 如果下面语句益处了，
            // 肯定有res > Integer.MAX_VALUE || res == Integer.MAX_VALUE && pop > 7
            // 如果不知道这个7，通过对10取余得出
            if (res > Integer.MAX_VALUE/10 || 
                    (res == Integer.MAX_VALUE/10 && pop > Integer.MAX_VALUE%10))
                return 0;
            res = res * 10 + pop;
        }
        return num > 0 ? res : -res;*/
        while (num != 0){
            int pop = num % 10;
            num = num / 10;
            // 2147483647  7
            if (res > Integer.MAX_VALUE/10 ||
                    (res == Integer.MAX_VALUE/10 && pop > Integer.MAX_VALUE%10))
                return 0;
            // -2147483648  -8
            else if (res < Integer.MIN_VALUE/10 ||
                    (res == Integer.MIN_VALUE/10 && pop < Integer.MIN_VALUE%10))
                return 0;
            res = res * 10 + pop;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
    }
}
