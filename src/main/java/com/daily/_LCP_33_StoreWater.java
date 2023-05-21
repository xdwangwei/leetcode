package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/5/21 10:35
 * @description: _LCP_33_StoreWater
 * 给定 N 个无限容量且初始均空的水缸，每个水缸配有一个水桶用来打水，第 i 个水缸配备的水桶容量记作 bucket[i]。小扣有以下两种操作：
 *
 * 升级水桶：选择任意一个水桶，使其容量增加为 bucket[i]+1
 * 蓄水：将全部水桶接满水，倒入各自对应的水缸
 * 每个水缸对应最低蓄水量记作 vat[i]，返回小扣至少需要多少次操作可以完成所有水缸蓄水要求。
 *
 * 注意：实际蓄水量 达到或超过 最低蓄水量，即完成蓄水要求。
 *
 * 示例 1：
 *
 * 输入：bucket = [1,3], vat = [6,8]
 *
 * 输出：4
 *
 * 解释：
 * 第 1 次操作升级 bucket[0]；
 * 第 2 ~ 4 次操作均选择蓄水，即可完成蓄水要求。
 *
 *
 * 示例 2：
 *
 * 输入：bucket = [9,0,1], vat = [0,2,2]
 *
 * 输出：3
 *
 * 解释：
 * 第 1 次操作均选择升级 bucket[1]
 * 第 2~3 次操作选择蓄水，即可完成蓄水要求。
 *
 * 提示：
 *
 * 1 <= bucket.length == vat.length <= 100
 * 0 <= bucket[i], vat[i] <= 10^4
 * 通过次数10,683提交次数37,458
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/o8SXZn
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _LCP_33_StoreWater {

    /**
     * 方法一：贪心 + 枚举
     *
     * 题目中涉及两个操作：升级水桶、蓄水。我们应该贪心地把升级水桶的操作放在前面，这样在蓄水时，每次能蓄水的量就会更多，操作次数就会更少。
     *
     * 首先，如果最低蓄水量 vat 中所有元素都为 0，说明不需要蓄水，直接返回 0 即可。
     *
     * 接下来，我们可以枚举[蓄水的次数] x，其中 x ∈ [1,max(vat)]，
     *
     *      那么在开始蓄水前，每个水桶的容量至少应该为 ⌈vat_i / x⌉，其中 ⌈⌉ 表示向上取整。
     *
     *      因此，每个水桶的升级次数为 max(0,⌈vat_i / x⌉ − bucket_i)，
     *      我们将所有水桶的升级次数累加，记为 y，再加上蓄水的次数 x，就是总的操作次数。
     *
     * 答案为所有情况下 x+y 中的最小值。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/o8SXZn/solution/python3javacgotypescript-yi-ti-yi-jie-ta-awvh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param bucket
     * @param vat
     * @return
     */
    public int storeWater(int[] bucket, int[] vat) {
        // 最大水缸容量
        int max = Arrays.stream(vat).max().getAsInt();
        // 水缸空，不需要操作，返回 0
        if (max == 0) {
            return 0;
        }
        // 初始化最少操作次数ans
        int ans = 1 << 30;
        // 枚举可能的 【蓄水】次数
        for (int x = 1; x <= max; ++x) {
            // 蓄水次数为 x 的情况下，所有 水桶的升级次数
            int y = 0;
            // 遍历水桶
            for (int i = 0; i < bucket.length; ++i) {
                // 水缸容量 vat[i]，蓄水次数为 x ，那么水桶大小至少为 (vat[i] + x - 1) / x
                // 水桶初始大小为 bucket[i]，那么 升级次数为 (vat[i] + x - 1) / x - bucket[i]，不能为 0
                y += Math.max(0, (vat[i] + x - 1) / x - bucket[i]);
            }
            // 次数，蓄水次数 x，所有桶升级次数 y，总共 x + y，更新 ans
            ans = Math.min(ans, x + y);
        }
        // 返回 ans
        return ans;
    }
}
