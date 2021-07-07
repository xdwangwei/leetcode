package com.order;

import java.util.EventListener;
import java.util.concurrent.RecursiveTask;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/30 周六 22:33
 **/
public class _12_IntToRoman {
    
    public static String solution1(int x){
        StringBuilder builder = new StringBuilder();
        int ge = x%10;
        x = x/10;
        int shi = x%10;
        x = x/10;
        int bai = x%10;
        x = x/10;
        int qian = x%10;
        return builder.append(showQian(qian)).append(showBai(bai)).append(showShi(shi)).append(showGe(ge)).toString();
    }
    
    private static String showQian(int x){
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<x;i++)
            builder.append("M");
        return builder.toString();
    }

    private static String showBai(int x){
        StringBuilder builder = new StringBuilder();
        if (x >= 0 && x<=3){
            for (int i = 0; i < x; i++)
                builder.append("C");
        }else if (x == 4)
            builder.append("CD");
        else if (x < 9){
            builder.append("D");
            for (int i=0;i<x-5;i++)
                builder.append("C");
        }else 
            builder.append("CM");
        return builder.toString();
    }

    private static String showShi(int x){
        StringBuilder builder = new StringBuilder();
        if (x >= 0 && x<=3){
            for (int i = 0; i < x; i++)
                builder.append("X");
        }else if (x == 4)
            builder.append("XL");
        else if (x < 9){
            builder.append("L");
            for (int i=0;i<x-5;i++)
                builder.append("X");
        }else
            builder.append("XC");
        return builder.toString();
    }
    private static String showGe(int x){
        StringBuilder builder = new StringBuilder();
        if (x >= 0 && x<=3){
            for (int i = 0; i < x; i++)
                builder.append("I");
        }else if (x == 4)
            builder.append("IV");
        else if (x < 9){
            builder.append("V");
            for (int i=0;i<x-5;i++)
                builder.append("I");
        }else
            builder.append("IX");
        return builder.toString();
    }
    
    private static String solution2(int x){
        // 相当于 先分为 千 百 十 个
        // 再把每一位置是什么值显示什么字母对应起来
        String[] qian = {"","M","MM","MMM"};
        String[] bai =  {"","C","CC","CCC","CD","D","DC","DCC","DCCC","CM"};
        String[] shi =  {"","X","XX","XXX","XL","L","LX","LXX","LXXX","XC"};
        String[] ge =   {"","I","II","III","IV","V","VI","VII","VIII","IX"};
        StringBuilder builder = new StringBuilder();
        builder.append(qian[x/1000%10]).append(bai[x/100%10]).append(shi[x/10%10]).append(ge[x%10]);
        return builder.toString();
    }
    
    // 相当于不要按照 几个 1000  几个 100 几个 10 几个 1
    // 而按照 几个1000 几个900 几个500 几个400 几个100 几个90 几个50 几个40 几个10 几个9 几个5 几个4 几个1
    // 这些都是数字单位，对应一个字母单位，相当于把每个数字单位都变成字母单位
    public static String solution3(int x){
        String[] roman = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        int num[] = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9 ,5, 4, 1};
        int index = 0;
        StringBuilder builder = new StringBuilder();
        while (x > 0){
            int count = x / num[index];
            while (count-- > 0){
                builder.append(roman[index]); // 把count个数字单位变为count个字母单位
            }
            // 把count个数字单位减掉
            x %= num[index];
            index++;
        }
        return builder.toString();
    }
}
