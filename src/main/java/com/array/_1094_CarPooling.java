package com.array;

import com.common.DiffArray;

/**
 * @author wangwei
 * 2021/11/29 10:24
 *
 * 假设你是一位顺风车司机，车上最初有 capacity 个空座位可以用来载客。由于道路的限制，车 只能 向一个方向行驶（也就是说，不允许掉头或改变方向，你可以将其想象为一个向量）。
 *
 * 这儿有一份乘客行程计划表 trips[][]，其中 trips[i] = [num_passengers, start_location, end_location] 包含了第 i 组乘客的行程信息：
 *
 * 必须接送的乘客数量；
 * 乘客的上车地点；
 * 以及乘客的下车地点。
 * 这些给出的地点位置是从你的 初始 出发位置向前行驶到这些地点所需的距离（它们一定在你的行驶方向上）。
 *
 * 请你根据给出的行程计划表和车子的座位数，来判断你的车是否可以顺利完成接送所有乘客的任务（当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则请返回 false）。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 4
 * 输出：false
 * 示例 2：
 *
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 5
 * 输出：true
 * 示例 3：
 *
 * 输入：trips = [[2,1,5],[3,5,7]], capacity = 3
 * 输出：true
 * 示例 4：
 *
 * 输入：trips = [[3,2,7],[3,7,9],[8,3,9]], capacity = 11
 * 输出：true
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/car-pooling
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。假设你是一位顺风车司机，车上最初有 capacity 个空座位可以用来载客。由于道路的限制，车 只能 向一个方向行驶（也就是说，不允许掉头或改变方向，你可以将其想象为一个向量）。
 *
 * 这儿有一份乘客行程计划表 trips[][]，其中 trips[i] = [num_passengers, start_location, end_location] 包含了第 i 组乘客的行程信息：
 *
 * 必须接送的乘客数量；
 * 乘客的上车地点；
 * 以及乘客的下车地点。
 * 这些给出的地点位置是从你的 初始 出发位置向前行驶到这些地点所需的距离（它们一定在你的行驶方向上）。
 *
 * 请你根据给出的行程计划表和车子的座位数，来判断你的车是否可以顺利完成接送所有乘客的任务（当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则请返回 false）。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 4
 * 输出：false
 * 示例 2：
 *
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 5
 * 输出：true
 * 示例 3：
 *
 * 输入：trips = [[2,1,5],[3,5,7]], capacity = 3
 * 输出：true
 * 示例 4：
 *
 * 输入：trips = [[3,2,7],[3,7,9],[8,3,9]], capacity = 11
 * 输出：true
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/car-pooling
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1094_CarPooling {

    /**
     * 差分数组
     *
     * nums[] 代表汽车在每个站点时车上的人数，初始都是0，
     * 不管到达哪个站点，车上人数都不能超过capacity
     *
     * trips[i] 代表着一组区间操作，旅客的上车和下车就相当于数组的区间加减；
     * 只要结果数组中的元素都小于 capacity，就说明可以不超载运输所有旅客。
     *
     * trips[x,y,z] 代表 有 x 个人 要在 站点 y 上车，到 站点 z 下车，也就是说 这x个人 从y到z-1都在车上
     * 也就是 对nums[y,z-1]范围内元素加x，在这个区间内的每个站点，车上人数多了x
     *
     * 但问题是，差分数组的长度（车站的个数）应该是多少呢？题目没有直接给，但给出了数据取值范围：
     *
     * 0 <= trips[i][1] < trips[i][2] <= 1000
     * 车站个数最多为 1000，那么我们的差分数组长度可以直接设置为 1001：
     * @param trips
     * @param capacity
     * @return
     */
    public boolean carPooling(int[][] trips, int capacity) {
        // 最多有 1000 个车站，初始都是0，汽车在这些站点时，车上都没人
        int[] nums = new int[1001];
        // 构造差分数组
        DiffArray df = new DiffArray(nums);

        for (int[] trip : trips) {
            // 乘客数量
            int val = trip[0];
            // 第 trip[1] 站乘客上车
            int i = trip[1];
            // 第 trip[2] 站乘客已经下车，
            // 即乘客在车上的区间是 [trip[1], trip[2] - 1]
            int j = trip[2] - 1;
            // 进行区间操作。在此区间内的每个站点，车上人数都比之前 多个 val 个
            // 相当于 对nums[i， j] 范围内元素 统一增加 val
            df.increment(i, j, val);
        }
        // 由差分数组恢复最终状态
        int[] res = df.result();

        // 客车自始至终都不应该超载
        for (int i = 0; i < res.length; i++) {
            // 汽车在某个站点时，车上人数 超过 capacity 了
            if (res[i] > capacity) {
                return false;
            }
        }
        return true;
    }
}
