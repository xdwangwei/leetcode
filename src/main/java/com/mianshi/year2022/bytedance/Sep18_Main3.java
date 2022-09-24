package com.mianshi.year2022.bytedance;

import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/9/18 18:18
 * @description: Sep18_Main3
 */
public class Sep18_Main3 {


    /**
     * 对于长度为 n 的字符串 s，首先遍历字符串 s 统计每种字符的出现次数。
     * 如果每种字符的出现次数都是 n/4，则字符串 ss 已经是平衡的，返回 0。
     *
     * 当字符串 s 不平衡时，一定有至少一种字符的出现次数大于 n/4, 以及至少一种字符的出现次数小于 n/4
     *
     * 若字符 c 的出现次数 cnt 大于 n/4，则窗口内至少需要包含 cnt - n/4 个字符c，用来替换缺少的其他字符，来使字符串保持平衡。
     * 如果我能保证我们的窗口中的数量包含多出那部分，我们就能够替换这个窗口内的字符串达到平衡字符串
     *。
     */
    static class Solution {
        public static void main(String[] args) {
            // 读取原串
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();

            int[] cnt = new int[26];
            // 每个字符数量
            for (char c : s.toCharArray()) {
                cnt[c - 'A']++;
            }
            int len = s.length();
            // 每个字符多出来的数量
            int targetQ = Math.max(cnt['Q' - 'A'] - len / 4, 0);
            int targetW = Math.max(cnt['W' - 'A'] - len / 4, 0);
            int targetE = Math.max(cnt['E' - 'A'] - len / 4, 0);
            int targetR = Math.max(cnt['R' - 'A'] - len / 4, 0);
            // 恰好平衡
            if (targetQ == 0 && targetW == 0 && targetE == 0 && targetR == 0) {
                System.out.println(0);
            }
            // 当前窗口内每个字符的数量
            int[] cur = new int[26];
            int l = 0, r = 0;
            // 初始化为原串长
            int ans = len;
            // 滑动窗口
            while (r < len) {
                cur[s.charAt(r) - 'A']++;
                // 扩大右边界
                r++;
                // 如果我能保证我们的窗口中的数量包含多出来的那些字符，我们就能够替换这个窗口内的字符串达到平衡字符串
                while (l <= r && cur['Q' - 'A'] >= targetQ
                        && cur['W' - 'A'] >= targetW
                        && cur['E' - 'A'] >= targetE
                        && cur['R' - 'A'] >= targetR) {
                    // r 已经自增了，这里 不需要 r-l+1
                    ans = Math.min(ans, r - l);
                    cur[s.charAt(l) - 'A']--;
                    // 缩小左边界
                    l++;
                }
            }
            System.out.println(ans);
        }
    }
}
