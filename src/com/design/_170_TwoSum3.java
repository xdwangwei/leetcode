package com.design;

import java.util.*;

/**
 * @author wangwei
 * 2021/12/8 13:36
 *
 * 设计一个 Two 类，支持两个操作：
 * add： 添加一个数字
 * find(val): 已有数字中是否存在一对数字满足和为val
 */
public class _170_TwoSum3 {

    /**
     * 因为只需要返回bool，不需要索引，所以我们只需要记录已有的数字即可
     * 但是题目没有说每个数字不重复，所以我们应该记录每个数字出现次数
     *
     * 时间复杂度呢，add 方法是 O(1)，find 方法是 O(N)，空间复杂度为 O(N)
     *
     * 每次find都是O(N)，如果find调用的特别频繁，时间复杂度太高，想办法把find变成O(1)
     * 如果要把find变成O(1),那么就应该在add的时候，把所有可能的和都统计起来，
     *  只需要遍历已有数字，给它加上val，得到一个新的和，加入set就行了，这样的话，其实就不需要统计每个数字的次数了
     *
     */
    class TwoSum {

        Map<Integer, Integer> freq = new HashMap<>();

        public void add(int number) {
            // 记录 number 出现的次数
            freq.put(number, freq.getOrDefault(number, 0) + 1);
        }

        public boolean find(int value) {
            /**
             * 对于已有的每个数字，与他匹配的应该是 val - 自己，如果这个数字也存在，并且不等于自己，那么返回true
             * 如果 = 自己，那么就要判断当前数的个数是否 > 1
             */
            for (Integer key : freq.keySet()) {
                int other = value - key;
                // val = 2 * item
                if (other == key && freq.get(key) > 1)
                    return true;
                // val = item + other，二者均存在
                if (other != key && freq.containsKey(other))
                    return true;
            }
            return false;
        }


        /**
         * 方法二：
         * find  O(1)
         * add   O(N)
         */
        // List<Integer> nums = new LinkedList<>();
        // Set<Integer> sums = new HashSet<>();
        // public void add(int number) {
        //     for (Integer num : nums) {
        //         sums.add(num + number);
        //     }
        // }
        //
        // public boolean find(int value) {
        //     return sums.contains(value);
        // }
    }
}
