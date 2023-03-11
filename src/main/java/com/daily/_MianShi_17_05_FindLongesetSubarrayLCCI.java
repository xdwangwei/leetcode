package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/3/11 15:47
 * @description: _MianShi_17_05_FindLongesetSubarrayLCCI
 *
 * 面试题 17.05.  字母与数字
 * 给定一个放有字母和数字的数组，找到最长的子数组，且包含的字母和数字的个数相同。
 *
 * 返回该子数组，若存在多个最长子数组，返回左端点下标值最小的子数组。若不存在这样的数组，返回一个空数组。
 *
 * 示例 1:
 *
 * 输入: ["A","1","B","C","D","2","3","4","E","5","F","G","6","7","H","I","J","K","L","M"]
 *
 * 输出: ["A","1","B","C","D","2","3","4","E","5","F","G","6","7"]
 * 示例 2:
 *
 * 输入: ["A","A"]
 *
 * 输出: []
 * 提示：
 *
 * array.length <= 100000
 */
public class _MianShi_17_05_FindLongesetSubarrayLCCI {

    /**
     * 方法一：前缀和 + 哈希表
     *
     * 题目要求找到最长的子数组，且包含的字符和数字的个数相同。我们可以将字符看作 1，数字看作 −1，
     *
     * 那么问题就转化为：求最长的子数组，使得该子数组的和为 0。
     *
     * 我们可以运用前缀和的思想，
     * 用哈希表 vis 记录每个前缀和第一次出现的位置，
     * 用变量 mx 和 k 分别记录最长的满足条件的子数组的长度和左端点位置。
     *
     * 接下来遍历数组，计算当前位置 i 的前缀和 s：
     *
     * 如果当前位置的前缀和 s 在哈希表 vis 中存在，
     *      我们记第一次出现 s 的位置为 j，那么区间 [j+1,..,i] 的子数组和就为 0。
     *      如果此前的最长子数组的长度小于当前子数组的长度，即 mx<i−j，我们就更新 mx=i−j 和 k=j+1。
     * 否则，我们将当前位置的前缀和 s 作为键，当前位置 i 作为值，存入哈希表 vis 中。
     * 遍历结束后，返回左端点为 k，且长度为 mx 的子数组即可。
     *
     * 【注意】
     * 由于省略了前缀和数组s[n+1]的求取过程，通过遍历原数组的过程累计前缀和，
     * 那么需要设置原前缀和 s[?]=0的位置索引初始化为-1， 即 map.put(0, -1)
     *
     * 【注意】
     * 求解的是最长子数组，所以只记录每个前缀和第一次出现的位置
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/find-longest-subarray-lcci/solution/python3javacgotypescript-yi-ti-yi-jie-qi-qy7i/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param array
     * @return
     */
    public String[] findLongestSubarray(String[] array) {
        // 和为0的子数组的最大长度；在array中的起始位置
        int maxLen = 0, start = 0;
        // 当前前缀和，array[0]到array[i]累加和
        int curSum = 0;
        // 每个前缀和第一次出现 的 元素位置
        Map<Integer, Integer> map = new HashMap<>();
        // 初始化，前缀和为0，对应出现位置为-1
        map.put(0, -1);
        // 遍历
        for (int i = 0; i < array.length; i++) {
            // 累计更新前缀和
            curSum += array[i].charAt(0) >= 'A' ? 1 : -1;
            // 存在nums[0...j] == nums[0...i]
            if (map.containsKey(curSum)) {
                // 判断此部分长度是否更长，
                int j = map.get(curSum);
                // 如果是，则更新长度和起点
                if (i - j > maxLen) {
                    maxLen = i - j;
                    start = j + 1;
                }
            } else {
                // 否则，记录当前前缀和第一次出现的位置
                map.put(curSum, i);
            }
        }
        // 准备返回结果
        String[] ans = new String[maxLen];
        // 拷贝
        System.arraycopy(array, start, ans, 0, maxLen);
        return ans;
    }
}
