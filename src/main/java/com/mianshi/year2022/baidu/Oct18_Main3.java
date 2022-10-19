package com.mianshi.year2022.baidu;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/10/18 18:24
 * @description: Oct18_Main3
 *
 * 输入n行变量定义语句，如果由语法问题，跳过此行
 * 只可能出现两种语法问题：
 *      int a=0,b,a;   // 变量重复定义
 *      int a=c;           // 变量未定义
 *
 * 对于输入，可能有以下结果
 *      int a=0,b,a;
 *      int a=c;
 *      b=1;int a=3;
 *
 * 按字典序输出最终所有成功定义的变量及它的值
 * a 1
 * b 2
 */
public class Oct18_Main3 {

    static Map<String, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] split = line.split(";");
            if (split.length == 1) {
                int idx = split[0].indexOf("int ");
                if (idx == -1) {
                    String[] info = split[0].split("=");
                    if (info.length == 2) {
                        String var1 = info[0];
                        if (Character.isLetter(info[1].charAt(0))) {
                            String var2 = info[1];
                            if (!map.containsKey(var2)) {
                                continue;
                            } else {
                                map.put(var1, map.get(var2));
                            }
                        } else {
                            map.put(var1, Integer.parseInt(info[1]));
                        }
                    }
                } else {
                    String expr = split[0].substring(idx + 4);
                    String[] strings = expr.split(",");
                    if (strings.length == 1) {
                        if (calc(strings[0])) continue;
                    }
                }
            }
            System.out.println();
        }
    }

    private static boolean calc(String str) {
        if (str.contains("=")) {
            String[] info = str.split("=");
            String var1 = info[0], var2 = info[1];
            if (Character.isLetter(var2.charAt(0))) {
                if (!map.containsKey(var2)) {
                    return true;
                } else {
                    map.put(var1, map.get(var2));
                }
            } else {
                map.put(var1, Integer.parseInt(var2));
            }
        } else {
            map.put(str, 0);
        }
        return false;
    }
}
