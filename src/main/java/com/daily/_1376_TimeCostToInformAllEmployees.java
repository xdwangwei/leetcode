package com.daily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/5/1 17:39
 * @description: _1376_TimeCostToInfomeAllEmployees
 *
 * 1376. 通知所有员工所需的时间
 * 公司里有 n 名员工，每个员工的 ID 都是独一无二的，编号从 0 到 n - 1。公司的总负责人通过 headID 进行标识。
 *
 * 在 manager 数组中，每个员工都有一个直属负责人，其中 manager[i] 是第 i 名员工的直属负责人。对于总负责人，manager[headID] = -1。
 *
 * 题目保证从属关系可以用树结构显示。
 *
 * 公司总负责人想要向公司所有员工通告一条紧急消息。他将会首先通知他的直属下属们，然后由这些下属通知他们的下属，直到所有的员工都得知这条紧急消息。
 *
 * 第 i 名员工需要 informTime[i] 分钟来通知它的所有直属下属（也就是说在 informTime[i] 分钟后，他的所有直属下属都可以开始传播这一消息）。
 *
 * 返回通知所有员工这一紧急消息所需要的 分钟数 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 1, headID = 0, manager = [-1], informTime = [0]
 * 输出：0
 * 解释：公司总负责人是该公司的唯一一名员工。
 * 示例 2：
 *
 *
 *
 * 输入：n = 6, headID = 2, manager = [2,2,-1,2,2,2], informTime = [0,0,1,0,0,0]
 * 输出：1
 * 解释：id = 2 的员工是公司的总负责人，也是其他所有员工的直属负责人，他需要 1 分钟来通知所有员工。
 * 上图显示了公司员工的树结构。
 *
 *
 * 提示：
 *
 * 1 <= n <= 10^5
 * 0 <= headID < n
 * manager.length == n
 * 0 <= manager[i] < n
 * manager[headID] == -1
 * informTime.length == n
 * 0 <= informTime[i] <= 1000
 * 如果员工 i 没有下属，informTime[i] == 0 。
 * 题目 保证 所有员工都可以收到通知。
 * 通过次数23,889提交次数40,909
 */
public class _1376_TimeCostToInformAllEmployees {

    private List<Integer>[] g;
    private int[] informTime;

    /**
     * 方法一：DFS
     *
     * 我们先根据 manager 数组构建邻接表 g，其中 g[i] 表示员工 i 的所有直接下属。
     *
     * 接下来，我们设计一个函数 dfs(i)，表示从员工 i 开始，将消息通知给他的所有下属（包括直接下属、间接下属）所需要的时间，
     * 那么答案就是 dfs(headID)。
     *
     * 在函数 dfs(i) 中，我们需要遍历 i 的所有直接下属 j，对于每个下属，员工 i 需要将消息通知给他，这需要花费 informTime[i] 的时间，
     * 而他的所有下属需要将消息通知给他们的下属，这需要花费 dfs(j) 的时间，
     * 取 informTime[i]+dfs(j) 的最大值作为函数 dfs(i) 的返回值即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/time-needed-to-inform-all-employees/solution/python3javacgotypescript-yi-ti-yi-jie-df-agfy/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param headID
     * @param manager
     * @param informTime
     * @return
     */
    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        // g[i] 表示员工 i 的所有直接下属。
        g = new List[n];
        // 初始化空数组
        Arrays.setAll(g, k -> new ArrayList<>());
        this.informTime = informTime;
        // 构建 g
        for (int i = 0; i < n; ++i) {
            // headId的manager是-1，注意越界
            if (manager[i] >= 0) {
                g[manager[i]].add(i);
            }
        }
        // 返回 headId 通知完所有人需要的时间
        return dfs(headID);
    }

    /**
     * 从员工 i 开始，将消息通知给他的所有下属（包括直接下属、间接下属）所需要的时间，
     * @param i
     * @return
     */
    private int dfs(int i) {
        int ans = 0;
        // 每个直系下属通知完自己的所有下属需要的时间
        for (int j : g[i]) {
            //  取最大值
            ans = Math.max(ans, dfs(j));
        }
        // 加上自己通知直系下属的时间 就是返回值
        return ans + informTime[i];
    }
}
