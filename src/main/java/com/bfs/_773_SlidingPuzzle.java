package com.bfs;

import java.util.*;

/**
 * @author wangwei
 * 2020/8/29 17:12
 *
 * 在一个 2 x 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示, 以及一块空缺用0来表示.
 *
 * 一次移动定义为选择0与一个相邻的数字（上下左右）进行交换.
 *
 * 最终当板board的结果是[[1,2,3],[4,5,0]]谜板被解开。
 *
 * 给出一个谜板的初始状态，返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1 。
 *
 * 示例：
 *
 * 输入：board = [[1,2,3],[4,0,5]]
 * 输出：1
 * 解释：交换 0 和 5 ，1 步完成
 * 输入：board = [[1,2,3],[5,4,0]]
 * 输出：-1
 * 解释：没有办法完成谜板
 * 输入：board = [[4,1,2],[5,0,3]]
 * 输出：5
 * 解释：
 * 最少完成谜板的最少移动次数是 5 ，
 * 一种移动路径:
 * 尚未移动: [[4,1,2],[5,0,3]]
 * 移动 1 次: [[4,1,2],[0,5,3]]
 * 移动 2 次: [[0,1,2],[4,5,3]]
 * 移动 3 次: [[1,0,2],[4,5,3]]
 * 移动 4 次: [[1,2,0],[4,5,3]]
 * 移动 5 次: [[1,2,3],[4,5,0]]
 * 输入：board = [[3,2,4],[1,5,0]]
 * 输出：14
 * 提示：
 *
 * board是一个如上所述的 2 x 3 的数组.
 * board[i][j]是一个[0, 1, 2, 3, 4, 5]的排列.
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/sliding-puzzle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _773_SlidingPuzzle {

    /**
     * BFS
     * 穷举出board当前局面下可能衍生出的所有局面？这就简单了，看数字 0 的位置呗，和上下左右的数字进行交换就行了：
     * 套用BFS解题框架，当第一次到达target时，就得到了赢得游戏的最少步数。
     *
     * 在广度优先搜索实现中，需要将节点表示成可以哈希的数据结构
     * 本题中的每个节点是一个二维数组，我们将其转换为字符串，因为2*3
     *
     * 建表：这个表记录了原来二维数组中的每个点的邻接点在转换后的一维数组中的下标
     * 比如：
     *      1 2 3
     *      4 5 6    -->    123456
     *      对于1，原来的邻接数组是2和4，在转换后的一维数组中的下标为1,4
     *      对于2，原来的邻接数字是1,3,5，在转后后的一维数组中的下标是0,2,4
     * 因为这个题只是2*3，所以我们能直接找到位置，建立映射表，当原数组规模比较大时，就需要发现转换关系
     *
     *     int[][] neighbor = new int[][]{
     *         {1, 3},
     *         {0, 2, 4},
     *         {1, 5},
     *         {0, 4},
     *         {1, 3, 5},
     *         {2, 4}
     *     };
     *
     * 将初始状态转化为字符串，比如[[1, 2, 3],[4, 0, 5]]转为123405，目标值是123450。
     * 通过交换字符来转化。
     * BFS套路求最短路径
     *
     * @param board
     * @return
     */
    public int slidingPuzzle(int[][] board) {
        // 目标状态是"123450"
        String start = "", target = "123450";
        // 将初始状态转为字符串
        for (int[] row : board) {
            for (int item : row) {
                start += item;
            }
        }
        // 建立邻接元素位置表
        int[][] neighbor = {
            {1, 3},
            {0, 2, 4},
            {1, 5},
            {0, 4},
            {1, 3, 5},
            {2, 4}
        };
        /** BFS 框架求解问题  **/
        int depth = 0;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();
        // 加入初始状态
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            // 同层节点数
            int size = queue.size();
            // BFS齐头并进
            for (int i = 0; i < size; i++) {
                // 逐个处理这一层的节点
                String cur = queue.poll();
                // 达到目标状态
                if (target.equals(cur))
                    return depth;
                // 找到当前状态下，'0'的位置
                int idx = cur.indexOf('0');
                // 和它每个相邻位置开始交换，扩展出新的状态（孩子节点）
                for (int j: neighbor[idx]) {
                    String newBoard = swap(cur, idx, j);
                    if (!visited.contains(newBoard)) {
                        // 入队
                        queue.offer(newBoard);
                        visited.add(newBoard);
                    }
                }
            }
            // 深度加1，进入下一层
            depth++;
        }
        /** BFS 框架结束  **/
        // 无解
        return -1;
    }

    /**
     * 交换原字符串中两个指定位置处字符，产生一新字符串
     * @param str
     * @param i
     * @param j
     */
    private String swap(String str, int i, int j) {
        // String是不可变类型
        char[] array = str.toCharArray();
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return new String(array);
    }

    public static void main(String[] args) {
        String s = Arrays.deepToString(new int[][]{{2,3,5}, {1,3,5}});
        System.out.println(s);

        String test = "abcd";
        test.toCharArray()[0] = 'f';
        System.out.println(test);
    }

}
