package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/6/2 22:38
 * @description: _473_MatchsticksToSquare
 *
 * 473. 火柴拼正方形
 * 你将得到一个整数数组 matchsticks ，其中 matchsticks[i] 是第 i 个火柴棒的长度。你要用 所有的火柴棍 拼成一个正方形。你 不能折断 任何一根火柴棒，但你可以把它们连在一起，而且每根火柴棒必须 使用一次 。
 *
 * 如果你能使这个正方形，则返回 true ，否则返回 false 。
 *
 *
 *
 * 示例 1:
 *
 *
 *
 * 输入: matchsticks = [1,1,2,2,2]
 * 输出: true
 * 解释: 能拼成一个边长为2的正方形，每边两根火柴。
 * 示例 2:
 *
 * 输入: matchsticks = [3,3,3,3,4]
 * 输出: false
 * 解释: 不能用所有火柴拼成一个正方形。
 *
 *
 * 提示:
 *
 * 1 <= matchsticks.length <= 15
 * 1 <= matchsticks[i] <= 108
 */
public class _473_MatchsticksToSquare {

    /**
     * 问题等价于，将集合划分为4个和相等的子集
     * 等价于 698 题 k = 4 的特殊情况
     *
     * 首先，全部元素和必须能够被4整除，每个桶元素和为 target = sum / 4
     *
     * 回溯算法：
     *  准备4个桶，每个元素可以放置在任意一个桶中，如果这个桶元素和已达到target或者放入当前元素后会超过target，那么就不放入这个桶，考虑下一个
     * 如果是顺序安排每个元素，对于 [1，1，1，1，1，1，102] 这种情况可能会超时，
     * 因此，提前对 nums进行排序，然后 从后往前逐个进行安排，这样能够让回溯过程中的剪枝(选择放入哪个桶)更可能多的出发，减少回溯的次数，提高算法的效率
     *
     * 另外，如果在回溯过程中 nums 放入某个桶（做选择），进入下一层，再取出（撤销选择），此时 bucket元素和=0
     * 那么说明 某个 nums元素 放入这个个bucket后，之后过程中一直未找到合适的其他元素凑成target，导致被回溯到清空了这个桶，
     * 说明这个元素无法和其他元素组成和为target的子集，那么nums就无法被划分到k个桶，返回false
     * 【注意】这个剪枝条件特别有效果，将时间从 107ms 减到了 7ms
     * @param matchsticks
     * @return
     */

    public boolean makesquare(int[] matchsticks) {
        // 元素个数 必须超过 4
        if (matchsticks.length < 4) {
            return false;
        }
        int sum = Arrays.stream(matchsticks).sum();
        // 元素和必须能等分为4部分
        if (sum % 4 != 0) {
            return false;
        }
        // 排序，从后往前安排，尽可能多地触发回溯过程中的剪枝条件，减少回溯深度
        Arrays.sort(matchsticks);
        // 每个桶的元素和
        int target = sum / 4;
        // 排序后已经有序，如果第一个元素就比target大，肯定无法划分成功
        if (matchsticks[0] > target) {
            return false;
        }
        // 准备四个桶
        int[] bucket = new int[4];
        // 回溯，从后往前逐个安排nums元素
        return backTrack(matchsticks, matchsticks.length - 1, bucket, target);
    }


    /**
     * 回溯
     * 把 nums[idx] 安排到某个 bucket 里面，并进入下一层，返回 安排完 全部nums元素后，是否4个bucket元素和都恰好==target
     * nums元素是从后往前安排的
     * @param nums
     * @param index
     * @param bucket
     * @param target
     * @return
     */
    private boolean backTrack(int[] nums, int index, int[] bucket, int target) {
        // 全部元素已安排完
        if (index == -1) {
            // 保证每个桶里元素和都为target
            for (int i = 0; i < 4; ++i) {
                if (bucket[i] != target) {
                    return false;
                }
            }
            // 合理划分成功
            return true;
        }
        // 将 nums[index] 放入某个bucket
        for (int i = 0; i < 4; ++i) {
            // 提前剪枝
            if (bucket[i] + nums[index] > target) {
                continue;
            }
            // 做选择
            bucket[i] += nums[index];
            // 如果后续过程顺利完成，直接返回，否则会继续往下尝试其他桶
            if (backTrack(nums, index - 1, bucket, target)) {
                return true;
            }
            // 撤销选择
            bucket[i] -= nums[index];

            // 【注意】很好的剪枝
            // 说明 某个 nums元素 放入这个个bucket后，之后过程中一直未找到合适的其他元素凑成target，导致被回溯到清空了这个桶，
            // 说明这个元素无法和其他元素组成和为target的子集，那么nums就无法被划分到k个桶，返回false
            if (bucket[i] == 0) {
                return false;
            }
        }
        // 默认，false
        return false;
    }
}
