package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/12/31 18:11
 * @description: _2037_MinimumMovesToSeatEveryone
 *
 * 2037. 使每位学生都有座位的最少移动次数
 * 一个房间里有 n 个座位和 n 名学生，房间用一个数轴表示。给你一个长度为 n 的数组 seats ，其中 seats[i] 是第 i 个座位的位置。同时给你一个长度为 n 的数组 students ，其中 students[j] 是第 j 位学生的位置。
 *
 * 你可以执行以下操作任意次：
 *
 * 增加或者减少第 i 位学生的位置，每次变化量为 1 （也就是将第 i 位学生从位置 x 移动到 x + 1 或者 x - 1）
 * 请你返回使所有学生都有座位坐的 最少移动次数 ，并确保没有两位学生的座位相同。
 *
 * 请注意，初始时有可能有多个座位或者多位学生在 同一 位置。
 *
 *
 *
 * 示例 1：
 *
 * 输入：seats = [3,1,5], students = [2,7,4]
 * 输出：4
 * 解释：学生移动方式如下：
 * - 第一位学生从位置 2 移动到位置 1 ，移动 1 次。
 * - 第二位学生从位置 7 移动到位置 5 ，移动 2 次。
 * - 第三位学生从位置 4 移动到位置 3 ，移动 1 次。
 * 总共 1 + 2 + 1 = 4 次移动。
 * 示例 2：
 *
 * 输入：seats = [4,1,5,9], students = [1,3,2,6]
 * 输出：7
 * 解释：学生移动方式如下：
 * - 第一位学生不移动。
 * - 第二位学生从位置 3 移动到位置 4 ，移动 1 次。
 * - 第三位学生从位置 2 移动到位置 5 ，移动 3 次。
 * - 第四位学生从位置 6 移动到位置 9 ，移动 3 次。
 * 总共 0 + 1 + 3 + 3 = 7 次移动。
 * 示例 3：
 *
 * 输入：seats = [2,2,6,6], students = [1,3,2,6]
 * 输出：4
 * 解释：学生移动方式如下：
 * - 第一位学生从位置 1 移动到位置 2 ，移动 1 次。
 * - 第二位学生从位置 3 移动到位置 6 ，移动 3 次。
 * - 第三位学生不移动。
 * - 第四位学生不移动。
 * 总共 1 + 3 + 0 + 0 = 4 次移动。
 *
 *
 * 提示：
 *
 * n == seats.length == students.length
 * 1 <= n <= 100
 * 1 <= seats[i], students[j] <= 100
 * 通过次数17,109提交次数20,007
 */
public class _2037_MinimumMovesToSeatEveryone {

    /**
     * 贪心 + 排序
     *
     * 思路与算法
     *
     * 一个房间共有 n 个学生和 n 个座位，每个学生对应一个座位。
     *
     * 将学生和座位的位置分别排序后，第 i 个学生对应第 i 个座位，即第 i 个学生需要挪动的距离是 |students[i] - seats[i]|
     *
     * 证明 除此之外，交换任何两个学生的座位并不会使得答案更优
     *
     * 贪心成立的证明思路为：
     *
     * 设排序后的座位在坐标轴上的位置为： a1,a2,a3,...,an，学生在坐标轴上的位置为：b1,b2,b3,...,bn，
     * 任取两个学生 i < j，可知 ai <= aj, bi <= bj
     *
     * 考虑 d =  | ai - bi | + | aj - bj |，分情况去掉绝对值
     *      d1 = ai - bi + aj - bj = (ai + aj) - (bi + bj)
     *      d2 = bi - ai + aj - bj = (aj + bi) - (ai + bj)
     *      d3 = bi - ai + bj - aj = (bi + bj) - (ai + aj)
     *
     * 如果交换 i,j 位置，可以看出 d1、d3 不会改变，
     *      而 d2' = (aj + bj) - (ai + bi)
     *      由于 bi <= bj, 因此 d2' >= d2
     *      综上， d' >= d, 即，交换 i 、j 位置后，移动代价增大
     *
     * 故，我们可以直接按照顺序，将 seats[i] 和 student[i] 进行对应，然后计算总移动次数。
     *
     * 作者：sheng-huo-huan-yao-ji-xu
     * 链接：https://leetcode.cn/problems/minimum-number-of-moves-to-seat-everyone/solution/by-sheng-huo-huan-yao-ji-xu-d6nm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param seats
     * @param students
     * @return
     */
    public int minMovesToSeat(int[] seats, int[] students) {
        // 按照顺序，将 seats[i] 和 student[i] 进行对应，然后计算总移动次数。
        Arrays.sort(seats);
        Arrays.sort(students);
        int ans = 0;
        // 累加每个人到其对应位置的移动次数
        for (int i = 0; i < seats.length; i++) {
            ans += Math.abs(seats[i] - students[i]);
        }
        // 返回
        return ans;
    }
}
