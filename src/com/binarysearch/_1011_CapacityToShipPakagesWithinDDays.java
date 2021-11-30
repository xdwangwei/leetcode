package com.binarysearch;

/**
 * @author wangwei
 * 2020/8/30 18:50
 *
 * 传送带上的包裹必须在 D 天内从一个港口运送到另一个港口。
 *
 * 传送带上的第 i个包裹的重量为weights[i]。每一天，我们都会按给出重量的顺序往传送带上装载包裹。我们装载的重量不会超过船的最大运载重量。
 *
 * 返回能在 D 天内将传送带上的所有包裹送达的船的最低运载能力。
 *
 *
 *
 * 示例 1：
 *
 * 输入：weights = [1,2,3,4,5,6,7,8,9,10], D = 5
 * 输出：15
 * 解释：
 * 船舶最低载重 15 就能够在 5 天内送达所有包裹，如下所示：
 * 第 1 天：1, 2, 3, 4, 5
 * 第 2 天：6, 7
 * 第 3 天：8
 * 第 4 天：9
 * 第 5 天：10
 *
 * 请注意，货物必须按照给定的顺序装运，因此使用载重能力为 14 的船舶并将包装分成 (2, 3, 4, 5), (1, 6, 7), (8), (9), (10) 是不允许的。
 * 示例 2：
 *
 * 输入：weights = [3,2,2,4,1,4], D = 3
 * 输出：6
 * 解释：
 * 船舶最低载重 6 就能够在 3 天内送达所有包裹，如下所示：
 * 第 1 天：3, 2
 * 第 2 天：2, 4
 * 第 3 天：1, 4
 * 示例 3：
 *
 * 输入：weights = [1,2,3,1,1], D = 4
 * 输出：3
 * 解释：
 * 第 1 天：1
 * 第 2 天：2
 * 第 3 天：3
 * 第 4 天：1, 1
 *
 *
 * 提示：
 *
 * 1 <= D <= weights.length <= 50000
 * 1 <= weights[i] <= 500
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/capacity-to-ship-packages-within-d-days
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _1011_CapacityToShipPakagesWithinDDays {


    /**
     * 边界值二分搜索
     * 和 _875_KoKoEatingBnanas 思路一致，寻找左边界的二分查找
     * 轮船载重能力越强，花费的时间越少，形成单调性，并且要求的是最小承重能力，限制量是时间，所以采用左边界二分搜索
     * 【最小值】应该是重量【最大】的那个货物的重量，最大值是所有货物重量之和
     * 对于此题，承重能力的下界应该是所有货物中最大的那个，不然无法运送所有货物
     * 上界应该是，全部货物重量之和，一次全送完
     * @param weights
     * @param D
     * @return
     */
    public int shipWithinDays(int[] weights, int D) {
        int left = 1, right = 1;
        // 承重能力的下界就是所有货物中最重的那个货物，不然没法运送
        // 上界就是一次能送完所有货物，虽然可能更高，但也没必要
        for (int weight : weights) {
            left = Math.max(left, weight);
            right += weight;
        }
        // 上界不可达的二分法
        while (left < right) {
            int mid = left + (right - left) / 2;
            // 当前承重能力下，能送完所有货物
            if (canFinishShip(weights, mid, D)) {
                // 缩小上界，承重能力可以更低一些
                right = mid;
            // 运不完，承重能力不够，
            } else {
                left = mid + 1;
            }
        }
        // 一定存在解
        return left;
    }

    /**
     * 能否在承重能力为capacity的情况下，在D天内运送完weights数组中的所有货物
     * @param weights
     * @param capacity
     * @param d
     * @return
     */
    private boolean canFinishShip(int[] weights, int capacity, int d) {
        int day = 0, sum = 0;
        for (int weight : weights) {
            sum += weight;
            // 当日能够运送的重量达上限
            if (sum > capacity) {
                // 凑够当日运送的货物
                day++;
                // 当前货物需要等第二天运送
                sum = weight;
            }
        }
        // for循环之后，sum不为0，是最后一天要运送的货物，++day
        // 花费的总时间应该在限制d之内
        return ++day <= d;
    }
}
