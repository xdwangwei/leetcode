package com.daily;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/5/2 14:33
 * @description: _970_PowerfulIntegers
 *
 * 970. 强整数
 * 给定三个整数 x 、 y 和 bound ，返回 值小于或等于 bound 的所有 强整数 组成的列表 。
 *
 * 如果某一整数可以表示为 xi + yj ，其中整数 i >= 0 且 j >= 0，那么我们认为该整数是一个 强整数 。
 *
 * 你可以按 任何顺序 返回答案。在你的回答中，每个值 最多 出现一次。
 *
 *
 *
 * 示例 1：
 *
 * 输入：x = 2, y = 3, bound = 10
 * 输出：[2,3,4,5,7,9,10]
 * 解释：
 * 2 = 20 + 30
 * 3 = 21 + 30
 * 4 = 20 + 31
 * 5 = 21 + 31
 * 7 = 22 + 31
 * 9 = 23 + 30
 * 10 = 20 + 32
 * 示例 2：
 *
 * 输入：x = 3, y = 5, bound = 15
 * 输出：[2,4,6,8,10,14]
 *
 *
 * 提示：
 *
 * 1 <= x, y <= 100
 * 0 <= bound <= 106
 * 通过次数20,489提交次数46,061
 */
public class _970_PowerfulIntegers {

    /**
     * 方法一：哈希表 + 枚举
     *
     * 根据题目描述，一个强整数可以表示成 x^i + y^j ，其中 i≥0, j≥0。
     *
     * 题目需要我们找出所有不超过 bound 的强整数，
     *
     * 我们注意到 bound 的取值范围不超过 10^6，而 2^20 = 1048576 > 10^6。
     *
     * 因此，如果 x≥2，那么 i 最大不超过 20，才有可能使得 x^i + y^j ≤ bound 成立。
     *
     * 同理，如果 y≥2，那么 j 最大不超过 20。
     *
     * 因此我们可以使用双重循环，枚举所有可能的 x^i 和 y^j，分别记为 a 和 b，并保证 a+b ≤ bound，
     *
     * 此时 a+b 即为一个强整数。
     *
     * 我们使用哈希表存储所有满足条件的强整数，最后将哈希表中的所有元素转换成答案列表返回即可。
     *
     * 注意，如果 x=1 或者 y=1，那么 a 或者 b 的值恒等于 1，对应的循环只需要执行一次即可退出。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/powerful-integers/solution/python3javacgotypescript-yi-ti-yi-jie-ha-javr/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param x
     * @param y
     * @param bound
     * @return
     */
    public List<Integer> powerfulIntegers(int x, int y, int bound) {
        // 去重
        Set<Integer> ans = new HashSet<>();
        // 枚举所有可能的 x^i 和 y^j，分别记为 a 和 b，并保证 a+b ≤ bound，
        for (int a = 1; a <= bound; a *= x) {
            for (int b = 1; a + b <= bound; b *= y) {
                ans.add(a + b);
                // 如果 x=1 或者 y=1，那么 a 或者 b 的值恒等于 1，对应的循环只需要执行一次即可退出。
                if (y == 1) {
                    break;
                }
            }
            // 如果 x=1 或者 y=1，那么 a 或者 b 的值恒等于 1，对应的循环只需要执行一次即可退出。
            if (x == 1) {
                break;
            }
        }
        // 转为 List 返回
        return new ArrayList<>(ans);
    }
}
