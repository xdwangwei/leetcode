package com.daily;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/4/7 22:59
 * @description: _1125_SmallestSufficientTeam
 *
 * 1125. 最小的必要团队
 * 作为项目经理，你规划了一份需求的技能清单 req_skills，并打算从备选人员名单 people 中选出些人组成一个「必要团队」（ 编号为 i 的备选人员 people[i] 含有一份该备选人员掌握的技能列表）。
 *
 * 所谓「必要团队」，就是在这个团队中，对于所需求的技能列表 req_skills 中列出的每项技能，团队中至少有一名成员已经掌握。可以用每个人的编号来表示团队中的成员：
 *
 * 例如，团队 team = [0, 1, 3] 表示掌握技能分别为 people[0]，people[1]，和 people[3] 的备选人员。
 * 请你返回 任一 规模最小的必要团队，团队成员用人员编号表示。你可以按 任意顺序 返回答案，题目数据保证答案存在。
 *
 *
 *
 * 示例 1：
 *
 * 输入：req_skills = ["java","nodejs","reactjs"], people = [["java"],["nodejs"],["nodejs","reactjs"]]
 * 输出：[0,2]
 * 示例 2：
 *
 * 输入：req_skills = ["algorithms","math","java","reactjs","csharp","aws"], people = [["algorithms","math","java"],["algorithms","math","reactjs"],["java","csharp","aws"],["reactjs","csharp"],["csharp","math"],["aws","java"]]
 * 输出：[1,2]
 *
 *
 * 提示：
 *
 * 1 <= req_skills.length <= 16
 * 1 <= req_skills[i].length <= 16
 * req_skills[i] 由小写英文字母组成
 * req_skills 中的所有字符串 互不相同
 * 1 <= people.length <= 60
 * 0 <= people[i].length <= 16
 * 1 <= people[i][j].length <= 16
 * people[i][j] 由小写英文字母组成
 * people[i] 中的所有字符串 互不相同
 * people[i] 中的每个技能是 req_skills 中的技能
 * 题目数据保证「必要团队」一定存在
 * 通过次数5,019提交次数9,780
 */
public class _1125_SmallestSufficientTeam {

    /**
     *
     * 记忆化搜索 + 位运算
     *
     *
     * 重新描述一遍问题：从people 中选择一些元素（技能集合），这些技能集合的并集等于reqSkills，要求选的元素个数尽量少。
     *
     * 把people[i] 看成物品（集合），reqSkills 看成背包容量（目标集合），本题就是一个集合版本的 0-1 背包问题。
     *
     * 为方便计算，先把 reqSkills 中的每个字符串映射到其下标上，记到一个哈希表sid 中。（reqSkills长度不超过16）
     *
     * 然后把每个people[i] 通过映射转换成数字集合，再压缩成一个二进制数。（全掌握，就是二进制16个1，1个int就搞定）
     * 最后得到所有人的技能掩码数组 int[m]  personSkillMask，m 是 总人数
     *
     * 例如示例 1，把 "java","nodejs","reactjs" 分别映射到0,1,2 上，
     * 那么people[0],people[1],people[2] 按照这种映射关系就转换成集合{0},{1},{1,2}，
     * 对应的二进制数分别为 0(2),10(2),110(2)。
     * 那么选择集合{0} 和{1,2}，它俩的并集为 {0,1,2}，满足题目要求。
     * 这等价于选择二进制数1(2)和110(2) ，它俩的或运算的结果是111(2)，就对应着集合{0,1,2}。
     *
     * 类似 0-1 背包，定义 dfs(i,j) 表示 从 索引位 i 的物品 开始往后选，凑过重量为 j 的背包，至少需要选择的物品个数。每个物品只有1个
     *
     * 分类讨论：
     *      不选 i 号物品：   dfs(i,j)   =   dfs(i+1, j)。
     *                                      表示 从 i+1 号物品开始选，凑够重量为 j，最少物品数
     *      选  i 号物品：   dfs(i,j)    =   dfs(i+1, j - w[i]) + 1。
     *                                      表示 选择当前物品i，重量为w[i]，加上 从 i+1 号物品开始选，凑够重量为 j-w[i]，最少物品数
     *      两种方案取最小值，即 dfs(i,j) = min(dfs(i+1,j),  dfs(i+1,j-w[i])+1)。
     *
     * 在本题中，目标背包重量就是 目标团队技能掩码 target = (1 << n) - 1，其中 n 为 reqSkills 的长度
     * i 号物品，就是编号为 i 的人，物品重量 就是 i 号人员掌握的 技能列表，也就是 i 号人员的 技能掩码 personSkillMask[i]
     *
     * 由于本题不仅求最少物品（人）数量，还要求具体方案（哪些物品，哪些人）
     * 我们可以让 dfs 返回 二元组，
     *
     * 这里采用二进制优化：
     * 因为 people 最多 60 个人，所以可以把人员编号集合也用二进制数表示，可以压缩到一个64 位整数中。注意这里是 long
     * 那么 返回值 ans 二进制1的个数代表 至少需要的人数，二进制为1的位置代表这些人的编号
     *
     * 修改后，long dfs(i,j) 表示 从 索引为 i 的人 开始往后选，凑够总技能为 j，最优（人数最少，二进制1最少）选择的人员编号二进制：
     *      当 j == 0 时，不需要人，返回 0
     *      当 i == m 时，一共就 m 个人，编号最大是 m-1，所以这里是无人可选，那么不可能凑出 j，返回一个不可能的数
     *                  这里返回 (1 << m) - 1，代表需要全部人参考
     *                  为啥呢？因为求最优方案，就是需要的最少人，我们这里返回 全选，不会影响另一种方案 和它 取最小（更少）
     *      对于 i 号人
     *          如果 不选他，
     *                  返回  dfs(i+1,j)
     *          如果 选他，
     *                  他掌握的技能是 personSkillMask[i]，需要的总技能是 j，那么剩下人需要凑出的技能是 j & ~personSkillMask[i]
     *                  返回 1 << i | dfs(i+1, j & ~personSkillMask[i])
     *          两种方案求最小 （两个返回值，二进制中1更少的那个）
     *
     *  加上记忆化，
     *  由于 dfs(i, j) 有两个参数， i 最大为 m，j 最大为 target，所以用数组  int[m][target + 1] 实现memo
     *  由于 0 是合理的方案数，所以 memo 初始化为 -1，代表没计算过
     *
     *  最终返回什么呢？
     *
     *  long ans = dfs(0, target)  从 0 号人 开始选， 凑出团队技能掩码为 target，最优（人数最少）选择方案的 人员二进制掩码
     *  从 ans 中得到这些人的编号
     *  人数是 Long.bitCount(ans)
     *  int[] res = new int[Long.bitCount(ans)];
     *  for (int i = 0, j = 0; i < m; ++i) {
     *             // ans二进制为1的位置就是这些人的编号
     *             if ((ans >> i & 1) == 1) {
     *                 res[j++] = i;
     *             }
     *         }
     *  return res
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/smallest-sufficient-team/solution/zhuang-ya-0-1-bei-bao-cha-biao-fa-vs-shu-qode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */


    // 人数
    private int m;
    // 每个人的技能掩码
    private int[] peopleSkillMask;

    // 目标 团队技能掩码
    private int target;

    // 备忘录
    private long[][] memo;

    public int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int n = req_skills.length;
        Map<String, Integer> skillIdx = new HashMap<>(n);
        // 给技能编号
        for (int i = 0; i < n; ++i) {
            skillIdx.put(req_skills[i], i);
        }
        this.m = people.size();
        this.peopleSkillMask = new int[m];
        // 根据每个人的技能列表，得到每个人的技能掩码
        for (int i = 0; i < m; ++i) {
            List<String> skills = people.get(i);
            for (String skill : skills) {
                peopleSkillMask[i] |= 1 << skillIdx.get(skill);
            }
        }
        // 目标团队技能掩码，就是啥都会，一共n个技能，就是二进制n个1
        this.target = (1 << n) - 1;
        // 备忘录
        this.memo = new long[m][target + 1];
        // 初始化备忘录
        for (long[] mo : memo) {
            Arrays.fill(mo, -1);
        }
        // 从0号人员开始选，凑出 团队技能掩码为 target，至少需要的人数，这些人的编号是啥
        long ans = dfs(0, target);
        // ans的二进制1的个数就是人数
        int[] res = new int[Long.bitCount(ans)];
        for (int i = 0, j = 0; i < m; ++i) {
            // ans二进制为1的位置就是这些人的编号
            if ((ans >> i & 1) == 1) {
                res[j++] = i;
            }
        }
        // 返回
        return res;
    }

    /**
     * 0，1背包
     * 从 i 位置开始选，装满 重量为 j，至少需要的物品数
     * 从 i 位置开始选，凑出 总技能掩码为 teamSkillMask，至少需要的人数（哪些人）
     * ans 的二进制为1的个人就是 至少需要的人数，这些为1的位置，就是这些人的编号
     *
     * @param i
     * @param teamSkillMask
     * @return
     */
    private long dfs(int i, int teamSkillMask) {
        // 目标团队技能掩码为0，不需要人，返回0
        if (teamSkillMask == 0) {
            return 0;
        }
        // 一共就m个人，最大编号是m-1，从 m号人开始选，也就是无人可选，凑出 teamSkillMask 是 不可能的，
        // 因为求的是每个方案的最少人数，所以这种情况给他一个特别大的值，就是所有人都要选，二进制m个1
        if (i == m) {
            return (1L << m) - 1;
        }
        // 已经求解过
        if (memo[i][teamSkillMask] != -1) {
            return memo[i][teamSkillMask];
        }
        // 如果选这个人，那么剩下的就是 从 i+1 号开始选，需要凑出的技能列表为 当前的 teamSkillMask 排除掉 i 号本身的技能
        long res1 = 1L << i | dfs(i + 1, teamSkillMask & ~peopleSkillMask[i]);
        // 如果不选这个人，那么剩下的就是 从 i+1 号开始选，需要凑出的技能列表为 teamSkillMask
        long res2 = dfs(i + 1, teamSkillMask);
        // 选择两个方案中人数更少的那个，保存到备忘录中并返回
        return memo[i][teamSkillMask] = Long.bitCount(res1) < Long.bitCount(res2) ? res1 : res2;
    }
}
