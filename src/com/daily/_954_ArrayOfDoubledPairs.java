package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/4/1 9:34
 *
 * 给定一个长度为偶数的整数数组 arr，只有对 arr 进行重组后可以满足 “对于每个 0 <=i < len(arr) / 2，都有 arr[2 * i + 1] = 2 * arr[2 * i]” 时，返回 true；否则，返回 false。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr = [3,1,3,6]
 * 输出：false
 * 示例 2：
 *
 * 输入：arr = [2,1,2,6]
 * 输出：false
 * 示例 3：
 *
 * 输入：arr = [4,-2,2,-4]
 * 输出：true
 * 解释：可以用 [-2,-4] 和 [2,4] 这两组组成 [-2,-4,2,4] 或是 [2,4,-2,-4]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/array-of-doubled-pairs
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _954_ArrayOfDoubledPairs {


    /**
     * 【核心】 题目本质上是问 arr 能否分成 n / 2 对元素，每对元素中一个数是另一个数的两倍
     * 每个数字 x 可以 与 2 x 或 x/2 匹配，需要考虑两个方向
     * 如果我们从小到大排序，那么 每个数字 x 就 只需要和 2x 匹配。并且 2x 的个数应该 >= x 的个数，
     * 每一个x需要消耗一个2x，满足所有x后，剩下的2x应该和4x去匹配。所以如果 count(2x) < count(x)，那么肯定不够
     *
     * 因为数字中包含负数，比如 -2 要和 -4 去匹配，为了 从小到大排序后 也只需要考虑 从 x 到 2x 这个单向，我们用绝对值大小排序
     * 【注意】是对map中的key进行排序。不是对原数组进行排序
     * @param arr
     * @return
     */
    public boolean canReorderDoubled(int[] arr) {
        // 题目本质上是问 arr 能否分成 n / 2 对元素，每对元素中一个数是另一个数的两倍
        // 所以需要考虑每一个数字的出现次数
        // 比如2个3，2个6，那一定能凑成两对二倍关系，我们不需要关心这四个数字在原数字中放在哪个位置
        int n = arr.length;
        if (n == 0) {
            return false;
        }
        // 统计每个数字的出现次数
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i : arr) {
            countMap.put(i, countMap.getOrDefault(i, 0) + 1);
        }
        // 【注意】是对map中的key进行排序。不是对原数组进行排序，元素组中一个数字可能出现多次，我们统计了每一个数字的出现次数，这些数字都是独立的，
        // 最后配对也是对这些key进行配对，这些key都是不重复的
        // 收集map中的key
        List<Integer> vals = new ArrayList<>();
        for (int x : countMap.keySet()) {
            vals.add(x);
        }
        // 按绝对值从小到大排序
        Collections.sort(vals, Comparator.comparingInt(Math::abs));
        // 排完序后，每一个x只能和2x配对
        for (Integer x : vals) {
            // x是map中有的，但是2x不一定存在，并且2x的个数必须>=x，才能配对成功
            if (countMap.getOrDefault(2 * x, 0) < countMap.get(x)) {
                return false;
            }
            // 每次配对成功，2x的个数减少，几个x，消耗几个2x
            countMap.put(2 * x, countMap.getOrDefault(2 * x, 0) - countMap.get(x));
        }
        // 可以成功
        return true;
    }

}
