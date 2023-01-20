package com.daily;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/1/20 13:19
 * @description: _1817_FindingTheUsersActiveMinutes
 *
 * 1817. 查找用户活跃分钟数
 * 给你用户在 LeetCode 的操作日志，和一个整数 k 。日志用一个二维整数数组 logs 表示，其中每个 logs[i] = [IDi, timei] 表示 ID 为 IDi 的用户在 timei 分钟时执行了某个操作。
 *
 * 多个用户 可以同时执行操作，单个用户可以在同一分钟内执行 多个操作 。
 *
 * 指定用户的 用户活跃分钟数（user active minutes，UAM） 定义为用户对 LeetCode 执行操作的 唯一分钟数 。 即使一分钟内执行多个操作，也只能按一分钟计数。
 *
 * 请你统计用户活跃分钟数的分布情况，统计结果是一个长度为 k 且 下标从 1 开始计数 的数组 answer ，对于每个 j（1 <= j <= k），answer[j] 表示 用户活跃分钟数 等于 j 的用户数。
 *
 * 返回上面描述的答案数组 answer 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：logs = [[0,5],[1,2],[0,2],[0,5],[1,3]], k = 5
 * 输出：[0,2,0,0,0]
 * 解释：
 * ID=0 的用户执行操作的分钟分别是：5 、2 和 5 。因此，该用户的用户活跃分钟数为 2（分钟 5 只计数一次）
 * ID=1 的用户执行操作的分钟分别是：2 和 3 。因此，该用户的用户活跃分钟数为 2
 * 2 个用户的用户活跃分钟数都是 2 ，answer[2] 为 2 ，其余 answer[j] 的值都是 0
 * 示例 2：
 *
 * 输入：logs = [[1,1],[2,2],[2,3]], k = 4
 * 输出：[1,1,0,0]
 * 解释：
 * ID=1 的用户仅在分钟 1 执行单个操作。因此，该用户的用户活跃分钟数为 1
 * ID=2 的用户执行操作的分钟分别是：2 和 3 。因此，该用户的用户活跃分钟数为 2
 * 1 个用户的用户活跃分钟数是 1 ，1 个用户的用户活跃分钟数是 2
 * 因此，answer[1] = 1 ，answer[2] = 1 ，其余的值都是 0
 *
 *
 * 提示：
 *
 * 1 <= logs.length <= 104
 * 0 <= IDi <= 109
 * 1 <= timei <= 105
 * k 的取值范围是 [用户的最大用户活跃分钟数, 105]
 * 通过次数15,105提交次数19,605
 */
public class _1817_FindingTheUsersActiveMinutes {

    /**
     * 哈希表 + 计数
     *
     * 题目：用户操作分钟不能重复，
     * 比如 (1,3), (1,3), (1,4) 表示 用户1 在 3分钟操作一次，用户1 在 3分钟 操作一次，只计算一次
     * 即 用户1 在 3分钟操作一次，用户1 在 4分钟操作一次，用户的 活跃分钟数 为 2
     *
     * 需要首先得到每个用户的用户活跃分钟数，然后计算用户活跃分钟数等于特定值的用户数。
     *
     * 由于同一个用户的每次执行操作的时间不重复计算，因此需要使用哈希表记录每个用户的执行操作的时间，
     * 哈希表的关键字是用户编号，哈希表的值是存储执行操作的分钟的哈希集合，
     * 使用哈希集合可以确保同一个用户执行操作的同一分钟只会计算一次，不会重复计算。
     *
     * 首先遍历操作日志数组 logs 得到每个用户对应的执行操作的时间并存入哈希表，然后遍历哈希表并更新答案数组。
     * 由于这道题只需要知道用户活跃分钟数等于特定值的用户数，不需要知道具体的用户编号，因此只需要遍历哈希表中每条记录的值即可。
     * 哈希表中的每条记录的值都是一个哈希集合，哈希集合的元素个数等于当前用户的用户活跃分钟数，将答案数组中的对应元素值加1。
     * 遍历哈希表中每条记录的值之后，即可得到答案数组。
     *
     * 由于答案数组的下标从 1 开始，因此在更新答案数组时需要注意将下标做转换。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/finding-the-users-active-minutes/solution/cha-zhao-yong-hu-huo-yue-fen-zhong-shu-b-p5f6/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param logs
     * @param k
     * @return
     */
    public int[] findingUsersActiveMinutes(int[][] logs, int k) {
        // 关键字是用户编号，哈希表的值是 用户在哪些分钟执行了操作 的哈希集合，
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[] log : logs) {
            int id = log[0], minute = log[1];
            map.putIfAbsent(id, new HashSet<>());
            // id号用户 在 minute 分钟 进行了一次操作
            map.get(id).add(minute);
        }
        // 对于 map里面 的 key->value，用户key的活跃分钟数为 value.size()
        // answer[j] 表示 活跃分钟数 = j 的 用户数
        int[] answer = new int[k];
        // 遍历所有用户的 活跃分钟数
        for (Set<Integer> ids : map.values()) {
            // 此 活跃分钟数 对应的用户数量 + 1
            answer[ids.size() - 1]++;
        }
        // 返回
        return answer;
    }
}
