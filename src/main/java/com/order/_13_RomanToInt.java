package com.order;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/30 周六 23:25
 **/
public class _13_RomanToInt {
    
    // 利用了数字排布从大到小的规律
    // 只要每次比较前一个值和当前值的大小关系即可：
    // 前值小于当前值(CM,IX,IV等)，减去前值, 这些是特殊情况
    // 前值大于或等于当前值，加上前值(罗马数字是 大数+...+小数) CMCC(CM是特殊，C会被减去,M一定大于后面，所以会被加上)
    // 当前值变为前值
    // 相当于每次遍历都是在前面那个值要加上还是减去，而没有处理当前值，所以循环结束后，最后次的前值必然是加上的
    public static int solution(String s){
        int res = 0;
        int preVlaue = getValue(s.charAt(0));
        for (int i = 1; i < s.length(); i++){
            int curVlue = getValue(s.charAt(i));
            if (preVlaue < curVlue)
                res -= preVlaue;
            else 
                res += preVlaue;
            preVlaue = curVlue;
        }
        return res + preVlaue; // 最后一个值没有经过与后续的比较，一定要加上
    }
    
    private static int getValue(char ch){
        switch (ch){
            case 'M':
                return 1000;
            case 'D':
                return 500;
            case 'C':
                return 100;
            case 'L':
                return 50;
            case 'X':
                return 10;
            case 'V':
                return 5;
            case 'I':
                return 1;
            default:
                return 0;
        }
    }
}
