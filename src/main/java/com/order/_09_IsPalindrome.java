package com.order;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/28 周四 10:13
 **/
public class _09_IsPalindrome {
    
    public static boolean solution1(int num){
        if (num < 0) return false;
        if (num == 0) return true;
        if (num % 10 == 0) return false;
        int temp = num, res = 0;
        while (temp > 0){
            res = res * 10  + temp % 10;
            temp = temp / 10;
        }
        return res == num;
    }

    public static boolean solution2(int num){
        return (num + "").equals(new StringBuilder(num+"").reverse().toString());
    }
}
