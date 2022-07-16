package com.order;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/12/1 周日 00:02
 **/
public class _14_LongestCommonPrefix {
    
    public static String solution1(String[] strs){
        if (strs.length == 0) return "";
        if (strs.length == 1) return strs[0];
        for (String str: strs)
            if (str.equals("")) return "";
        int endIndex = 0;
        while (true){
            int j = 0;
            if (endIndex >= strs[j].length()) break;
            char c = strs[j++].charAt(endIndex);
            while (j < strs.length){
                if (endIndex >= strs[j].length()) break;
                if (c == strs[j].charAt(endIndex)) j++;
                else break;
            }
            if (j == strs.length) endIndex++;
            else break;
        }
        return strs[0].substring(0, endIndex);
    }
    
    public static String solution2(String[] strs){
        if (strs.length == 0) return "";
        String prefix = strs[0]; // 把strs[0]作为临时公共前缀
        for (int j = 1; j < strs.length; j++){
            // 如果是所有的公共前缀，prefix在strs[j]中第一次出现的位置一定是0
            while (strs[j].indexOf(prefix) != 0){
                // 如果不是，就把prefix最后一个字符去掉，再进行尝试匹配strs[j],一直到匹配成功strs[j]
                prefix = prefix.substring(0, prefix.length()-1);
            }
            // prefix已经裁剪到""了，说明没有公共前缀，或strs=[""],或strs=["","a"]或["a","","c"]
            if (prefix == "") return "";
        }
        return prefix;
    }

    public static void main(String[] args) {
        System.out.println(solution1(new String[]{"flower", "flow", "fligt"}));
    }
}
