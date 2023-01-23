package com.daily;

/**
 * @author wangwei
 * @date 2023/1/23 11:29
 * @description: _2303_CalculateAmountPaidInTaxes
 *
 * 2303. 计算应缴税款总额
 * 给你一个下标从 0 开始的二维整数数组 brackets ，其中 brackets[i] = [upperi, percenti] ，表示第 i 个税级的上限是 upperi ，征收的税率为 percenti 。税级按上限 从低到高排序（在满足 0 < i < brackets.length 的前提下，upperi-1 < upperi）。
 *
 * 税款计算方式如下：
 *
 * 不超过 upper0 的收入按税率 percent0 缴纳
 * 接着 upper1 - upper0 的部分按税率 percent1 缴纳
 * 然后 upper2 - upper1 的部分按税率 percent2 缴纳
 * 以此类推
 * 给你一个整数 income 表示你的总收入。返回你需要缴纳的税款总额。与标准答案误差不超 10-5 的结果将被视作正确答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：brackets = [[3,50],[7,10],[12,25]], income = 10
 * 输出：2.65000
 * 解释：
 * 前 $3 的税率为 50% 。需要支付税款 $3 * 50% = $1.50 。
 * 接下来 $7 - $3 = $4 的税率为 10% 。需要支付税款 $4 * 10% = $0.40 。
 * 最后 $10 - $7 = $3 的税率为 25% 。需要支付税款 $3 * 25% = $0.75 。
 * 需要支付的税款总计 $1.50 + $0.40 + $0.75 = $2.65 。
 * 示例 2：
 *
 * 输入：brackets = [[1,0],[4,25],[5,50]], income = 2
 * 输出：0.25000
 * 解释：
 * 前 $1 的税率为 0% 。需要支付税款 $1 * 0% = $0 。
 * 剩下 $1 的税率为 25% 。需要支付税款 $1 * 25% = $0.25 。
 * 需要支付的税款总计 $0 + $0.25 = $0.25 。
 * 示例 3：
 *
 * 输入：brackets = [[2,50]], income = 0
 * 输出：0.00000
 * 解释：
 * 没有收入，无需纳税，需要支付的税款总计 $0 。
 *
 *
 * 提示：
 *
 * 1 <= brackets.length <= 100
 * 1 <= upperi <= 1000
 * 0 <= percenti <= 100
 * 0 <= income <= 1000
 * upperi 按递增顺序排列
 * upperi 中的所有值 互不相同
 * 最后一个税级的上限大于等于 income
 * 通过次数13,261提交次数19,580
 */
public class _2303_CalculateAmountPaidInTaxes {

    /**
     * 方法一：直接模拟
     *
     * 思路与算法
     *
     * 设第 i 个税级的上限是 upper[i] ，征收的税率为 percent[i]。
     * 税级按上限从低到高排序且满足 upper[i-1] < upper[i]
     *
     * 根据题意税款计算方式如下：
     *
     * 不超过 upper[0] 的收入按税率 percent[0] 缴纳；
     * 即  处于 [0       , upper[0]] 的部分按税率 percent[0] 缴纳；
     * 接着处于 [upper[0], upper[1]] 的部分按税率 percent[1] 缴纳；
     * 接着处于 [upper[1], upper[2]] 的部分按税率 percent[2] 缴纳；
     * 以此类推；
     *
     * 给定的 income 表示总收入，依次计算 income 处于第 i 个区间 [upper[i-1], upper[i]] 之间的部分为 pay[i]，
     * 此时计算公式为 pay[i] = min(income, upper[i]) - upper[i-1] ，
     * 则在第 i 个征税区间缴纳的税为 tax[i] = pay[i] * percent[i]
     * 则征税总额为
     * totalTax = ∑tax[i],
     *
     * 其中第 0 个征税区间为 [0,upper[0]]，为了计算方便可先进行整数累加，最后再进行浮点数除法计算。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/calculate-amount-paid-in-taxes/solution/ji-suan-ying-jiao-shui-kuan-zong-e-by-le-jv5s/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param brackets
     * @param income
     * @return
     */
    public double calculateTax(int[][] brackets, int income) {
        // prevUpper 记录上一个区间的上限，初始为0
        int ans = 0, prevUpper = 0;
        // 遍历每个区间
        for (int i = 0; i < brackets.length; ++i) {
            int upper = brackets[i][0], percent = brackets[i][1];
            // 得到 income 在每个区间应该缴纳的部分
            ans += (Math.min(income, upper) - prevUpper) * percent;
            // 最高到当前区间，后序没有了
            if (income <= upper) {
                break;
            }
            // 更新 prevUpper
            prevUpper = upper;
        }
        // brackets[i][j] 给的是整数，需要转为百分比
        return ans / 100.0;
    }
}
