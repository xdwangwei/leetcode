package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/3/31 8:58
 *
 * 每日一题
 *
 * 自除数是指可以被它包含的每一位数整除的数。
 *
 * 例如，128 是一个 自除数 ，因为128 % 1 == 0，128 % 2 == 0，128 % 8 == 0。
 * 自除数 不允许包含 0 。
 *
 * 给定两个整数left和right ，返回一个列表，列表的元素是范围[left, right]内所有的 自除数 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：left = 1, right = 22
 * 输出：[1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 15, 22]
 * 示例 2:
 *
 * 输入：left = 47, right = 85
 * 输出：[48,55,66,77]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/self-dividing-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _728_SelfDividingNumbers {

    /**
     * 逐个判断法
     * @param left
     * @param right
     * @return
     */
    public List<Integer> selfDividingNumbers(int left, int right) {

        List<Integer> nums = new ArrayList<>();

        for (int i = left; i <= right; ++i) {

            int j = i;
            while (j > 0) {
                // 得到 i 每个位置上的数字
                int mod = j % 10;
                // 自除数不能包含0，而且每个位置都能整除
                if (mod == 0 || i % mod != 0) {
                    break;
                }
                j /= 10;
            }
            // 正常退出while循环，说明 i 是自除数
            if (j == 0) {
                nums.add(i);
            }
        }

        return nums;

    }
}
