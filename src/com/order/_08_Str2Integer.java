package com.order;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/25 周一 17:46
 **/
public class _08_Str2Integer {
    
    public static int solution1(String s){
        s = s.trim(); //先去除空格
        if (s.length() == 0) return 0; // 空串，返回0
        char c = s.charAt(0); // 判断第一个非空字符，只允许第一个字符是非数字且为+/-
        if (c != '-' && c != '+' && !Character.isDigit(c)) return 0;
        int res;
        if (c == '-'){ // 如果是负数
            int i = 1; // 从符号位之后扫描
            while (i < s.length() && s.charAt(i) == '0') i++; // 过滤掉多余的前导0
            if (i >= s.length()) return 0;// -000000
            res = 0;
            // 如果是1-9,就持续转成十进制数值
            while (i < s.length() && Character.isDigit(s.charAt(i))){
                int pop = Integer.parseInt(String.valueOf(s.charAt(i)));
                // 避免下面的操作会溢出
                if (res > Integer.MAX_VALUE/10 || (res == Integer.MAX_VALUE/10 && pop > 7))
                    // 当前是在负号情况下，如果绝对值大于INT+MAX，应该返回负的最小值
                    return Integer.MIN_VALUE;
                res = res * 10 + pop;
                i++;
            }
            // res = -res;
            return -res;
        }else {
            // +222882这种先去掉+号
            if (c == '+') s = s.substring(1, s.length());
            int i = 0;
            // 过滤掉无用前导0
            while (i < s.length() && s.charAt(i) == '0') i++;
            if (i >= s.length()) return 0; // 全0串
            res = 0;
            // 从第一个非零数字字符开始转换成十进制数值
            while (i < s.length() && Character.isDigit(s.charAt(i))){
                int pop = Integer.parseInt(String.valueOf(s.charAt(i)));
                // 提前判断下面的操作是否会溢出
                if (res > Integer.MAX_VALUE/10 || (res == Integer.MAX_VALUE/10 && pop > 7))
                    return Integer.MAX_VALUE;
                res = res * 10 + pop;
                i++;
            }
        }
        return res;
    }

    public static int solution2(String s){
        s = s.trim();
        if (s.length() == 0) return 0;
        char c = s.charAt(0);
        if (c != '-' && c != '+' && !Character.isDigit(c)) return 0;
        if (Character.isDigit(c)) { // 如果第一位直接是数字，加一个正号，使操作统一
            s = '+' + s;
            c = '+';//记录当前数符号
        }
        int i = 1;
        // 过滤无用的0串
        while (i < s.length() && s.charAt(i) == '0') i++;
        if (i >= s.length()) return 0; // 全0串
        int res = 0;
        // 转换数字字符为数值
        while (i < s.length() && Character.isDigit(s.charAt(i))){
            int pop = Integer.parseInt(String.valueOf(s.charAt(i)));
            if (res > Integer.MAX_VALUE/10 || (res == Integer.MAX_VALUE/10 && pop > 7))
                // 根据符号位决定返回最大正值还是最小负值
                return c == '+' ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            res = res * 10 + pop;
            i++;
        }
        // 未溢出的有效数值，根据符号位决定返回正值或负值
        return c == '+' ? res : -res;
    }
    
    public static int solution3(String s){
        
        s = s.trim(); // 先去掉空格避免下面正则匹配失误(实际上只需要去掉前面的空格即可)
        // 第一个位置只能是 +/-或者数字字符，也就是 +/-可以出现0次或1次，之后必须是数字
        // 题目要求 +-2 -+12 这种直接返回0
        Pattern p = Pattern.compile("^[\\+\\-]?\\d+");
        Matcher matcher = p.matcher(s);
        // 如果匹配到了(因为我们只考虑第一段数字串)
        if (matcher.find()){
            try {
                // 截取这段字串试着转换成整数输出
                return Integer.parseInt(s.substring(matcher.start(), matcher.end()));
            }catch (Exception e){
                // 溢出，根据第一位的符号来判断输出最小值还是最大值
                return s.charAt(0) == '-' ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(solution1("42"));
        System.out.println(solution1("     -42"));
        System.out.println(solution1("4193 with words"));
        System.out.println(solution1("words and 987"));

        System.out.println(solution2("42"));
        System.out.println(solution2("     -42"));
        System.out.println(solution2("4193 with words"));
        System.out.println(solution2("words and 987"));

        System.out.println(solution3("42"));
        System.out.println(solution3("     -42"));
        System.out.println(solution3("4193 with words"));
        System.out.println(solution3("words and 987"));
    }
}
