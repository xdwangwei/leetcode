package com.order;

/**
 * @author wangwei
 * 2020/4/5 17:30
 * <p>
 * 「外观数列」是一个整数序列，从数字 1 开始，序列中的每一项都是对前一项的描述。前五项如下：
 * <p>
 * 1.     1
 * 2.     11 // 前一项 一个1 记为 11
 * 3.     21 // 前一项，两个1，记为 21
 * 4.     1211 // 前一项，一个2，一个1，记为 12 11
 * 5.     111221 // 前一项，一个1，一个2，两个， 记为 11 12 21
 * 6.     312211 // 前一项，三个1，两个2，一个1，记为 31 22 11
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/count-and-say
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _38_CountAndSay {

    // n表示第n项
    public String solution(int n) {
        String lastStr = "1"; // 第1项
        while (--n > 0) { // 得到第n项，只需迭代n-1次
            lastStr = getNext(lastStr);
        }
        return lastStr;
    }

    /**
     * 由当前字符串得到下一项
     * @param lastStr
     * @return
     */
    private String getNext(String lastStr) {
        if (lastStr.equals("1")) return "11";
        int count = 1;
        StringBuilder builder = new StringBuilder();
        // i 最多到 lastStr.length - 2
        for (int i = 0; i < lastStr.length() - 1; i++) {
            if (lastStr.charAt(i) == lastStr.charAt(i + 1)) {
                count++;
            } else {
                // 发生转折 1 12 1，把当前之前的结果合并
                builder.append(count).append(lastStr.charAt(i));
                count = 1;
            }
        }
        // 最后一次转折后的结果没有加进来
        // 对于 1211 ，i最多到倒数第2(1)的位置，1==1,count++,for退出了，11组成的结果也要拼接上
        builder.append(count).append(lastStr.charAt(lastStr.length() - 1));
        return builder.toString();
    }

    /**
     * 递归形式
     * @param n
     * @return
     */
    private String solution2(int n) {
        // 出口
        if (n == 1) return "1";
        // 获取上一个
        String lastStr = solution2(n - 1);
        // 由上一个推出这一个
        int count = 1;
        StringBuilder builder = new StringBuilder();
        // i 最多到 lastStr.length - 2
        for (int i = 0; i < lastStr.length() - 1; i++) {
            if (lastStr.charAt(i) == lastStr.charAt(i + 1)) {
                count++;
            } else {
                builder.append(count).append(lastStr.charAt(i));
                count = 1;
            }
        }
        // 对于 1211 ，i最多到倒数第2(1)的位置，1==1,count++,for退出了，11组成的结果也要拼接上
        builder.append(count).append(lastStr.charAt(lastStr.length() - 1));
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(new _38_CountAndSay().solution(4));
        System.out.println(new _38_CountAndSay().solution2(4));
    }
}
