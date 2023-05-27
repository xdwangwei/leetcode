package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/5/27 19:57
 * @description: _1093_StatisticsFromALargeSample
 *
 * 我们对0到255之间的整数进行采样，并将结果存储在数组count中：count[k]就是整数k 在样本中出现的次数。
 *
 * 计算以下统计数据:
 *
 * minimum：样本中的最小元素。
 * maximum：样品中的最大元素。
 * mean：样本的平均值，计算为所有元素的总和除以元素总数。
 * median：
 * 如果样本的元素个数是奇数，那么一旦样本排序后，中位数 median 就是中间的元素。
 * 如果样本中有偶数个元素，那么中位数median 就是样本排序后中间两个元素的平均值。
 * mode：样本中出现次数最多的数字。保众数是 唯一 的。
 * 以浮点数数组的形式返回样本的统计信息[minimum, maximum, mean, median, mode]。与真实答案误差在10-5内的答案都可以通过。
 *
 *
 *
 * 示例 1：
 *
 * 输入：count = [0,1,3,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
 * 输出：[1.00000,3.00000,2.37500,2.50000,3.00000]
 * 解释：用count表示的样本为[1,2,2,2,3,3,3,3]。
 * 最小值和最大值分别为1和3。
 * 均值是(1+2+2+2+3+3+3+3) / 8 = 19 / 8 = 2.375。
 * 因为样本的大小是偶数，所以中位数是中间两个元素2和3的平均值，也就是2.5。
 * 众数为3，因为它在样本中出现的次数最多。
 * 示例 2：
 *
 * 输入：count = [0,4,3,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
 * 输出：[1.00000,4.00000,2.18182,2.00000,1.00000]
 * 解释：用count表示的样本为[1,1,1,1,2,2,3,3,3,4,4]。
 * 最小值为1，最大值为4。
 * 平均数是(1+1+1+1+2+2+2+3+3+4+4)/ 11 = 24 / 11 = 2.18181818…(为了显示，输出显示了整数2.18182)。
 * 因为样本的大小是奇数，所以中值是中间元素2。
 * 众数为1，因为它在样本中出现的次数最多。
 *
 * 提示：
 *
 * count.length == 256
 * 0 <= count[i] <= 109
 * 1 <= sum(count) <= 109
 * count的众数是 唯一 的
 * 通过次数13,016提交次数32,048
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/statistics-from-a-large-sample
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1093_StatisticsFromALargeSample {

    private int[] count;

    /**
     * 方法：模拟
     *
     * 我们直接根据题目描述模拟即可，定义以下变量：
     *
     * mi 和 mx 分别表示最小值、最大值；
     * s 和  cnt 表示总和、总个数；
     * mode 表示众数。
     *
     * 我们遍历数组 count，对于当前遍历到的数字 count[k]，如果 count[k]>0，那么我们做以下更新操作：
     *
     * 更新 mi=min(mi,k)；mx=max(mx,k)；
     * 更新 s=s+k×count[k]；
     * 更新 cnt=cnt+count[k]；
     * 如果 count[k]>count[mode]，那么更新 mode=k。
     *
     * 遍历结束后，我们再根据 cnt 的奇偶性来计算中位数 median，
     *
     * 如果 cnt 是奇数，那么中位数就是第 ⌊cnt/2⌋ + 1 个数字，
     * 如果 cnt 是偶数，那么中位数就是第 ⌊cnt/2⌋ 和 第 ⌊cnt/2⌋ + 1 个数字的平均值。
     *
     * 这里我们通过一个简单的辅助函数 find(i) 来找到第 i 个数字，具体实现可以参考下面的代码。
     *
     * 最后，我们将 mi,mx, s / cnt, median,mode 放入答案数组中返回即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/statistics-from-a-large-sample/solution/python3javacgotypescript-yi-ti-yi-jie-mo-ov62/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param count
     * @return
     */
    public double[] sampleStats(int[] count) {
        this.count = count;
        // 最小、最大元素
        int mi = 1 << 30, mx = -1;
        // 总和
        long s = 0;
        // 数字个数
        int cnt = 0;
        // 众数
        int mode = 0;
        // 遍历元素 k 及 出现次数 count[k]
        for (int k = 0; k < count.length; ++k) {
            if (count[k] > 0) {
                // 更新 最小值
                mi = Math.min(mi, k);
                // 更新 最大值
                mx = Math.max(mx, k);
                // 更新 总和
                s += (long) k * count[k];
                // 更新 数字个数
                cnt += count[k];
                // 更新 众数
                if (count[k] > count[mode]) {
                    mode = k;
                }
            }
        }
        // 根据 元素个数的奇偶性 计算中位数
        double median = cnt % 2 == 1 ? find(cnt / 2 + 1) : (find(cnt / 2) + find(cnt / 2 + 1)) / 2.0;
        // 返回
        return new double[]{mi, mx, s * 1.0 / cnt, median, mode};
    }

    /**
     * 在 count 中寻找 从小到大第 k 个元素
     * @param i
     * @return
     */
    private int find(int i) {
        // 遍历元素及其出现次数
        for (int k = 0, t = 0; ; ++k) {
            // 累加已遍历元素个数
            t += count[k];
            // 找到第 i 个元素
            if (t >= i) {
                // 返回当前元素
                return k;
            }
        }
    }

}
