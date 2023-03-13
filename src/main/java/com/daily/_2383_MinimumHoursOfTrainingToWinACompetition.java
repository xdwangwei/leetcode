package com.daily;

/**
 * @author wangwei
 * @date 2023/3/13 10:40
 * @description: _2383_MinimumHoursOfTrainingToWinACompetition
 *
 * 2383. 赢得比赛需要的最少训练时长
 * 你正在参加一场比赛，给你两个 正 整数 initialEnergy 和 initialExperience 分别表示你的初始精力和初始经验。
 *
 * 另给你两个下标从 0 开始的整数数组 energy 和 experience，长度均为 n 。
 *
 * 你将会 依次 对上 n 个对手。第 i 个对手的精力和经验分别用 energy[i] 和 experience[i] 表示。当你对上对手时，需要在经验和精力上都 严格 超过对手才能击败他们，然后在可能的情况下继续对上下一个对手。
 *
 * 击败第 i 个对手会使你的经验 增加 experience[i]，但会将你的精力 减少  energy[i] 。
 *
 * 在开始比赛前，你可以训练几个小时。每训练一个小时，你可以选择将增加经验增加 1 或者 将精力增加 1 。
 *
 * 返回击败全部 n 个对手需要训练的 最少 小时数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：initialEnergy = 5, initialExperience = 3, energy = [1,4,3,2], experience = [2,6,3,1]
 * 输出：8
 * 解释：在 6 小时训练后，你可以将精力提高到 11 ，并且再训练 2 个小时将经验提高到 5 。
 * 按以下顺序与对手比赛：
 * - 你的精力与经验都超过第 0 个对手，所以获胜。
 *   精力变为：11 - 1 = 10 ，经验变为：5 + 2 = 7 。
 * - 你的精力与经验都超过第 1 个对手，所以获胜。
 *   精力变为：10 - 4 = 6 ，经验变为：7 + 6 = 13 。
 * - 你的精力与经验都超过第 2 个对手，所以获胜。
 *   精力变为：6 - 3 = 3 ，经验变为：13 + 3 = 16 。
 * - 你的精力与经验都超过第 3 个对手，所以获胜。
 *   精力变为：3 - 2 = 1 ，经验变为：16 + 1 = 17 。
 * 在比赛前进行了 8 小时训练，所以返回 8 。
 * 可以证明不存在更小的答案。
 * 示例 2：
 *
 * 输入：initialEnergy = 2, initialExperience = 4, energy = [1], experience = [3]
 * 输出：0
 * 解释：你不需要额外的精力和经验就可以赢得比赛，所以返回 0 。
 *
 *
 * 提示：
 *
 * n == energy.length == experience.length
 * 1 <= n <= 100
 * 1 <= initialEnergy, initialExperience, energy[i], experience[i] <= 100
 * 通过次数17,631提交次数38,475
 */
public class _2383_MinimumHoursOfTrainingToWinACompetition {

    /**
     * 方法：模拟 + 贪心
     *
     * ans 表示通过训练获得的最少精力和经验值总和
     *
     * 遍历数组，按题目要求进行判断是否能够打败对手。
     * 遇到每一个对手 i
     * 能够打败对手，直接扣减精力和增加经验。
     *      精力直接扣减 initialEnergy-= energy[i];，经验直接累加 initialExperience+= experience[i];
     * 不能够打败对手，精力或经验不够时，就需要进行训练，直到刚好能够打败对手才停止训练，
     *      需要训练获取精力 energy[i]-initialEnergy+1 （那么此时精力恰好为 energy[i]+1）
     *      战胜对手后剩下一点精力 initialEnergy=1;
     *      需要训练获取经验 experience[i]-initialExperience+1 （那么此时经验恰好为 experience[i]+1）
     *      战胜对手不需要消耗经验，并且会获得对手经验，所以 initialExperience = 2 * experience[i]+1;
     * 重复1,2的过程，记录通过训练获取的经验值和精力值，知道数组遍历结束。
     * 返回ans。
     *
     * 作者：huang-ji-hua
     * 链接：https://leetcode.cn/problems/minimum-hours-of-training-to-win-a-competition/solution/java-bian-li-zhu-shi-xiang-xi-jian-dan-y-kzru/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param initialEnergy
     * @param initialExperience
     * @param energy
     * @param experience
     * @return
     */
    public int minNumberOfHours(int initialEnergy, int initialExperience, int[] energy, int[] experience) {
        // ans 表示通过训练获得的最少精力和经验值总和
        int ans = 0;
        int n = energy.length;
        // 模拟遍历依次遇到对手i
        for (int i = 0; i < n; ++i) {
            // 精力不够
            if (initialEnergy <= energy[i]) {
                // 需要提前训练获取精力 energy[i]-initialEnergy+1 （那么此时精力恰好为 energy[i]+1）
                ans += energy[i] - initialEnergy + 1;
                // 战胜对手后剩下一点精力 initialEnergy=1;
                initialEnergy = 1;
            // 精力足够，直接扣减
            } else {
                initialEnergy -= energy[i];
            }
            // 经验不够
            if (initialExperience <= experience[i]) {
                // 需要提前训练获取经验 experience[i]-initialExperience+1
                // （那么此时经验恰好为 experience[i]+1）
                // 战胜对手不需要消耗经验，并且会获得对手经验，
                // 所以 initialExperience = 2 * experience[i]+1;
                ans += experience[i] - initialExperience + 1;
                initialExperience = 2 * experience[i] + 1;
            // 经验足够直接扣
            } else {
                initialExperience += experience[i];
            }
        }
        // 返回
        return ans;
    }
}
