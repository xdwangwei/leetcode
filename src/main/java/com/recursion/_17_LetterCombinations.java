package com.recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangwei
 * @Description: 
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母
 * 输入："23"
 * 输出：["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
 * @Time: 2019/12/10 周二 22:45
 **/
public class _17_LetterCombinations {
    
    // 建立数字字符和字符串的对应关系
    String[] phoneMap = {"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
    
    // 保存全部结果集
    List<String> res = new ArrayList<>();

    /**
     * 递归，逐个处理当前数字字符，将其对应的字符串数组中的每一个字符和下一个要处理的数字字符
     * 对应的字符串结合，以使问题由n转换到n-1，n各字符串对应分配n次，到第n次的时候没有要处理
     * 的字符了，把当前结果保存在结果集中，这也就是递归的结束条件
     * 以树形结构去看
     */
    public List<String> solution(String digits) {
        
        // 输入为空
        if ((null == digits) || (digits.isEmpty())){
            return res;
        }
        // 从第0层开始分配，当前没有数字字符，把""传下去
        backtrack(digits, "", 0);
        return res;
    }
    
    /**
     * @param digits  原始数字字符串
     * @param letter  从上一层分配过来的字符串
     * @param index  递归层数，分配次数，也就是从分配第一个开始分配到哪个了，分配到最后一个时把当前结果加入结果集
     */
    private void backtrack(String digits, String letter, int index){
        // 已经分配到最后一层了，本次结果直接加入结果集
        if (index == digits.length()) {
            res.add(letter);
            return;
        }
        // 继续分配    
        // 当前要分配哪个字符
        char c = digits.charAt(index);
        // 找到这个数字字符对应的字符串
        String str = phoneMap[c - '0'];
        // 逐个分配到下一层，每次分配都进行求解
        for (int i = 0; i < str.length(); i++){
            // 当前字符是跟在上个字符之后的，所以这里是 letter + str.charAt(i)
            // 把上一次的结果和当前字符结合，再继续和下层结合，一直到最后一层
            backtrack(digits, letter + str.charAt(i), index + 1);
        }
    }
}
