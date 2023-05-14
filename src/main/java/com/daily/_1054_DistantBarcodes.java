package com.daily;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * @date 2023/5/14 20:04
 * @description: _1054_DistantBarcodes
 *
 * 1054. 距离相等的条形码
 * 在一个仓库里，有一排条形码，其中第 i 个条形码为 barcodes[i]。
 *
 * 请你重新排列这些条形码，使其中任意两个相邻的条形码不能相等。 你可以返回任何满足该要求的答案，此题保证存在答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：barcodes = [1,1,1,2,2,2]
 * 输出：[2,1,2,1,2,1]
 * 示例 2：
 *
 * 输入：barcodes = [1,1,1,1,2,2,3,3]
 * 输出：[1,3,1,3,2,1,2,1]
 *
 *
 * 提示：
 *
 * 1 <= barcodes.length <= 10000
 * 1 <= barcodes[i] <= 10000
 * 通过次数20,330提交次数46,761
 */
public class _1054_DistantBarcodes {

    /**
     *
     * 方法：贪心 + 计数 + 排序
     *
     * 此题保证存在答案。因此，不存在某个元素的出现次数超过数组长度的一半
     *
     * 我们首先想到的思路就是，找到数量最多的元素，尽可能优先排列它（留出空隙），然后让其他元素去“插空“
     * 注意：多数元素应该是被插空的而不是拿来插空的。
     * 我们需要根据元素的频数进行降序排序，频数大的元素尽量排在前半部分作为被插空的部分，而频数少的元素排在后半部分作为插空元素。
     *
     * 我们先用哈希表或数组 cnt 统计数组 barcodes 中各个数出现的次数，
     * 然后将 barcodes 中的数按照它们在 cnt 中出现的次数从大到小排序，
     *
     * 接下来，我们创建一个长度为 n 的答案数组 ans，然后遍历排好序的 barcodes，将元素依次填入答案数组的 0,2,4,⋯ 等偶数下标位置，
     * 然后将剩余元素依次填入答案数组的 1,3,5,⋯ 等奇数下标位置即可。
     * （可以直接原地修改barcodes数组）
     *
     * 注意，次数最多的元素可能并不能占满全部偶数下标，
     * 这种情况下，可以让次数第二多的元素先继续放在剩下的偶数位置上，再和其他元素一起从位置1开始插空
     *
     * @param barcodes
     * @return
     */
    public int[] rearrangeBarcodes(int[] barcodes) {
        // 统计每个元素的实际出现次数
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int barcode : barcodes) {
            cnt.put(barcode, cnt.getOrDefault(barcode, 0) + 1);
        }
        // 按照出现次数排序
        List<Map.Entry<Integer, Integer>> list = cnt.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
        // 直接修改barcodes，idx指示当前给哪个位置赋值
        // 先插入偶数位置，从0开始
        int idx = 0;
        // 倒序遍历排序后的 k,v
        for (int i = list.size() - 1; i >= 0; i--) {
            Map.Entry<Integer, Integer> entry = list.get(i);
            // 元素
            Integer x = entry.getKey();
            // 次数
            Integer count = entry.getValue();
            // 用这个元素去填barcodes
            while (count > 0) {
                // 填充
                barcodes[idx] = x;
                // 待填的下一个位置
                idx += 2;
                // 偶数位置填完了，从奇数位置开始
                if (idx >= barcodes.length) {
                    idx = 1;
                }
                // 这个元素的次数减少
                count--;
            }
        }
        // 返回
        return barcodes;
    }

    /**
     * 方法二：方法一优化
     *
     * 方法一排序浪费了一部分时间
     *
     * 实际上我们不需要对所有元素和出现次数排序，只需要知道哪个元素的出现次数最多，剩下元素无所谓，都是用来插空的
     * 这样，我们可以在统计次数的过程中记录下次数最多的元素，然后先安排它，再安排其他元素插空，不需要排序
     * @param barcodes
     * @return
     */
    public int[] rearrangeBarcodes2(int[] barcodes) {
        // 统计元素次数
        int[] cnt = new int[10001];
        // 次数最多的元素
        int maxCntItem = 0;
        // 统计元素出现次数过程中，顺便得到次数最多的元素 maxCntItem
        for (int x : barcodes) {
            if (++cnt[x] > cnt[maxCntItem]) {
                maxCntItem = x;
            }
        }
        // 直接修改barcodes，idx指示当前给哪个位置赋值
        // 先插入偶数位置，从0开始
        int idx = 0;
        // 先用次数最多的元素 maxCntItem 去填充 barcodes 的所有偶数位置
        for ( ; cnt[maxCntItem] > 0; idx += 2) {
            barcodes[idx] = maxCntItem;
            cnt[maxCntItem]--;
        }
        // 遍历其他元素
        for (int i = 0; i < cnt.length; i++) {
            // 跳过无效位置（跳过 不存在的元素，和已经使用过的maxCntItem）
            if (cnt[i] == 0 || i == maxCntItem) {
                continue;
            }
            // 用这个元素插空
            while (cnt[i] > 0) {
                // 因为maxCntItem可能没有填完所有偶数位置，所以先接着往后填
                // 回过头来再填奇数位置，从1开始
                if (idx >= barcodes.length) {
                    idx = 1;
                }
                // 填空
                barcodes[idx] = i;
                // 元素次数减少
                cnt[i]--;
                // 下一个位置
                idx += 2;
            }
        }
        // 返回
        return barcodes;
    }
}
