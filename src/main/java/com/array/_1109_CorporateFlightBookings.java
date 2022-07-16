package com.array;

import com.common.DiffArray;

/**
 * @author wangwei
 * 2021/11/29 10:11
 *
 * 这里有 n 个航班，它们分别从 1 到 n 进行编号。
 *
 * 有一份航班预订表 bookings ，表中第 i 条预订记录 bookings[i] = [firsti, lasti, seatsi] 意味着在从 firsti 到 lasti （包含 firsti 和 lasti ）的 每个航班 上预订了 seatsi 个座位。
 *
 * 请你返回一个长度为 n 的数组 answer，里面的元素是每个航班预定的座位总数。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
 * 输出：[10,55,45,25,25]
 * 解释：
 * 航班编号        1   2   3   4   5
 * 预订记录 1 ：   10  10
 * 预订记录 2 ：       20  20
 * 预订记录 3 ：       25  25  25  25
 * 总座位数：      10  55  45  25  25
 * 因此，answer = [10,55,45,25,25]
 * 示例 2：
 *
 * 输入：bookings = [[1,2,10],[2,2,15]], n = 2
 * 输出：[10,25]
 * 解释：
 * 航班编号        1   2
 * 预订记录 1 ：   10  10
 * 预订记录 2 ：       15
 * 总座位数：      10  25
 * 因此，answer = [10,25]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/corporate-flight-bookings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1109_CorporateFlightBookings {

    /**
     * 差分数组
     *
     * 这个题目就在那绕弯弯，其实它就是个差分数组的题，我给你翻译一下：
     *
     * 给你输入一个长度为 n 的数组 nums，其中所有元素都是 0。
     * 再给你输入一个 bookings，里面是若干三元组 (i,j,k)，每个三元组的含义就是要求你给 nums 数组的闭区间 [i-1,j-1] 中所有元素都加上 k。
     * 请你返回最后的 nums 数组是多少？
     *
     * PS：因为题目说的 n 是从 1 开始计数的，而数组索引从 0 开始，
     * 所以对于输入的三元组 (i,j,k)，数组区间应该对应 [i-1,j-1]。
     *
     * 这么一看，不就是一道标准的差分数组题嘛？
     * @param bookings
     * @param n
     * @return
     */
    public int[] corpFlightBookings(int[][] bookings, int n) {
        int[] nums = new int[n];
        DiffArray diffArray = new DiffArray(nums);
        for (int[] booking : bookings) {
            int i = booking[0] - 1;
            int j = booking[1] - 1;
            int count = booking[2];
            diffArray.increment(i, j, count);
        }
        return diffArray.result();
    }

    public static void main(String[] args) {
        _1109_CorporateFlightBookings obj = new _1109_CorporateFlightBookings();
        int[] ints = obj.corpFlightBookings(new int[][]{{1, 2, 10}, {2, 3, 20}, {2, 5, 25}}, 5);
        System.out.println(ints);
    }
}
