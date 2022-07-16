package com.daily;

/**
 * @author wangwei
 * 2022/4/7 15:58
 *
 * 796. 旋转字符串
 * 给定两个字符串, s 和 goal。如果在若干次旋转操作之后，s 能变成 goal ，那么返回 true 。
 *
 * s 的 旋转操作 就是将 s 最左边的字符移动到最右边。
 *
 * 例如, 若 s = 'abcde'，在旋转一次之后结果就是'bcdea' 。
 *
 *
 * 示例 1:
 *
 * 输入: s = "abcde", goal = "cdeab"
 * 输出: true
 * 示例 2:
 *
 * 输入: s = "abcde", goal = "abced"
 * 输出: false
 */
public class _796_RotateString {

    /**
     * 方法一：暴力解法，从s的不同位置出切割，将前半部分拼接到后半部分的后面，去和goal比较，若一致，则返回true
     * @param s
     * @param goal
     * @return
     */
    public boolean rotateString(String s, String goal) {
        int m = s.length(), n = goal.length();
        if (m != n) {
            return false;
        }
        String temp;
        for (int i = 0; i < m; i++) {
            // 从s的不同位置出切割，将前半部分拼接到后半部分的后面，
            temp = s.substring(i) + s.substring(0, i);
            if (temp.equals(goal)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法二：改进方法一，并不去实际的切割和拼接字符串，从s的不同位置切割再拼接，那么每个字符新的位置也就不一样
     * 比如从i位置切割，那么s[i+0]会跑到t[0]位置，s[i+1]会跑到t[1]位置，s[i+j]会跑到t[j]的位置，避免溢出 s[(i+j)%m] -> t[j]
     * 如果某一次的t和goal一样，也就是说 s[(i+j)%m] == goal[j]
     * 在每个位置都成立(j=[0,m-1])，那么返回true
     * @param s
     * @param goal
     * @return
     */
    public boolean rotateString2(String s, String goal) {
        int m = s.length(), n = goal.length();
        // 一定等长
        if (m != n) {
            return false;
        }
        // 从s[i]位置切割
        for (int i = 0; i < m; i++) {
            // 记录档次切割拼接后的结果是否和goal一致
            boolean flag = true;
            for (int j = 0; j < m; j++) {
                // s[(i+j)%m]会跑到t[j]的位置
                if (s.charAt((i + j) % m) != goal.charAt(j)) {
                    // 当次切割失败，提前结束，进入下一个位置切割
                    flag = false;
                    break;
                }
            }
            // 某个位置切割成功
            if (flag) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法三：字符串 s+s 包含了所有 s 可以通过旋转操作得到的字符串，只需要检查 goal 是否为 s + s 的子字符串即可。
     * 具体可以参考「28. 实现 strStr() 的官方题解」的实现代码，本题解中采用直接调用库函数的方法。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/rotate-string/solution/xuan-zhuan-zi-fu-chuan-by-leetcode-solut-4hlp/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public boolean rotateString3(String s, String goal) {
        return  s.length() == goal.length() && (s + s).contains(goal);
    }

}
