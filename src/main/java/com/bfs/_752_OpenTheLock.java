package com.bfs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * @author wangwei
 * 2020/8/29 19:55
 * 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。
 *
 * 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
 *
 * 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
 *
 * 字符串 target 代表可以解锁的数字，你需要给出最小的旋转次数，如果无论如何不能解锁，返回 -1。
 *
 *
 * 示例 1:
 *
 * 输入：deadends = ["0201","0101","0102","1212","2002"], target = "0202"
 * 输出：6
 * 解释：
 * 可能的移动序列为 "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202"。
 * 注意 "0000" -> "0001" -> "0002" -> "0102" -> "0202" 这样的序列是不能解锁的，
 * 因为当拨动到 "0102" 时这个锁就会被锁定。
 * 示例 2:
 *
 * 输入: deadends = ["8888"], target = "0009"
 * 输出：1
 * 解释：
 * 把最后一位反向旋转一次即可 "0000" -> "0009"。
 * 示例 3:
 *
 * 输入: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
 * 输出：-1
 * 解释：
 * 无法旋转到目标数字且不被锁定。
 * 示例 4:
 *
 * 输入: deadends = ["0000"], target = "8888"
 * 输出：-1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/open-the-lock
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _752_OpenTheLock {

    /**
     * 如果你只转一下锁，有几种可能？总共有 4 个位置，每个位置可以向上转，也可以向下转，也就是有 8 种可能对吧。
     *
     * 比如说从"0000"开始，转一次，可以穷举出"1000", "9000", "0100", "0900"...共 8 种密码。
     * 然后，再以这 8 种密码作为基础，对每个密码再转一下，穷举出所有可能…
     *
     * 仔细想想，这就可以抽象成一幅图，每个节点有 8 个相邻的节点，又让你求最短距离，这不就是典型的 BFS 嘛，框架就可以派上用场了
     *
     * 会走回头路。比如说我们从"0000"拨到"1000"，但是等从队列拿出"1000"时，还会拨出一个"0000"，这样的话会产生死循环。
     * @param deadends
     * @param target
     * @return
     */
    public int openLock(String[] deadends, String target) {
        // 记录需要跳过的死亡密码
        Set<String> deadSet = new HashSet<>();
        for (String s : deadends) deadSet.add(s);

        /** BFS解题框架 **/
        Queue<String> q = new LinkedList<>();
        // 有回头路就需要visited集合
        Set<String> visited = new HashSet<>();
        // 起始节点入队列
        q.offer("0000");
        visited.add("0000");

        int depth = 0;

        while (!q.isEmpty()) {
            // 当前层节点个数
            int sz = q.size();
            /* 将当前队列中的所有节点向四周扩散 */
            for (int i = 0; i < sz; i++) {
                String cur = q.poll();
                // 跳过死状态
                if (deadSet.contains(cur))
                    continue;
                // 判断是否达到目标状态
                if (cur.equals(target))
                    return depth;
                // 对四个锁分别进行一次左旋或者右旋，得到新的状态
                for (int j = 0; j < 4; j++) {
                    // 左旋产生新状态
                    String left = leftRotate(cur, j);
                    if (!visited.contains(left)) {
                        q.offer(left);
                        visited.add(left);
                    }
                    // 右旋产生新状态
                    String right = rightRotate(cur, j);
                    if (!visited.contains(right)) {
                        q.offer(right);
                        visited.add(right);
                    }
                }
            }
            /* 这里增加深度 */
            depth++;
        }
        // 找不到解
        return -1;
    }

    /**
     * 双向BFS
     *
     * 双向 BFS 还是遵循 BFS 算法框架的，只是不再使用队列，而是使用 HashSet 方便快速判断两个集合是否有交集(终点)。
     *
     * 另外的一个技巧点就是 while 循环的最后交换q1和q2的内容，所以只要默认扩散q1就相当于轮流扩散q1和q2。
     *
     * 其实双向 BFS 还有一个优化，就是在 while 循环开始时做一个判断：
     *
     * // ...
     * while (!q1.isEmpty() && !q2.isEmpty()) {
     *     if (q1.size() > q2.size()) {
     *         // 交换 q1 和 q2
     *         temp = q1;
     *         q1 = q2;
     *         q2 = temp;
     *     }
     * 按照 BFS 的逻辑，队列（集合）中的元素越多，扩散之后新的队列（集合）中的元素就越多；
     * 在双向 BFS 算法中，如果我们每次都选择一个较小的集合进行扩散，那么占用的空间增长速度就会慢一些，效率就会高一些。
     *
     * @param deadends
     * @param target
     * @return
     */
    public int openLock2(String[] deadends, String target) {
        // 记录需要跳过的死亡密码
        Set<String> deadSet = new HashSet<>();
        for (String s : deadends) deadSet.add(s);

        /** BFS解题框架，不使用队列，使用集合 **/
        // 从上往下
        Set<String> q1 = new HashSet<>();
        // 起始节点入队列
        q1.add("0000");
        // 从下往上
        Set<String> q2 = new HashSet<>();
        // 目标节点加入队列
        q2.add(target);

        // 有回头路就需要visited集合
        Set<String> visited = new HashSet<>();

        int depth = 0;

        while (!q1.isEmpty() && !q2.isEmpty()) {
            // 哈希集合在遍历的过程中不能修改，用 temp 存储本轮扩散结果
            Set<String> temp = new HashSet<>();

            /* 将当前队列中的所有节点向四周扩散 */
            for (String cur: q1) {
                // 跳过死状态
                if (deadSet.contains(cur))
                    continue;
                // 判断是否达到目标状态，cur既在q1.又在q2
                if (q2.contains(cur))
                    return depth;
                // 标记当前状态
                visited.add(cur);

                // 对四个锁分别进行一次左旋或者右旋，得到新的状态
                for (int j = 0; j < 4; j++) {
                    // 左旋产生新状态
                    String left = leftRotate(cur, j);
                    if (!visited.contains(left)) {
                        // 不是加入q1或q2，而是加入temp
                        temp.add(left);
                    }
                    // 右旋产生新状态
                    String right = rightRotate(cur, j);
                    if (!visited.contains(right)) {
                        temp.add(right);
                    }
                }
            }
            /* 这里增加深度 */
            depth++;
            // 交换q1.q2
            q1 = q2;
            q2 = temp;
        }
        // 找不到解
        return -1;
    }

    /**
     * str是一个长度为4的字符串，每一个位置代表一个锁当前对准的字符，
     * 对指定的那个锁左旋一下，得到一个新的状态
     * @param str
     * @param idx
     * @return
     */
    private String leftRotate(String str, int idx) {
        char[] arr = str.toCharArray();
        // 1->2  2->3  9->0
        if (arr[idx] == '9')
            arr[idx] = '0';
        else arr[idx] += 1;
        return new String(arr);
    }

    /**
     * str是一个长度为4的字符串，每一个位置代表一个锁当前对准的字符，
     * 对指定的那个锁右旋一下，得到一个新的状态
     * @param str
     * @param idx
     * @return
     */
    private String rightRotate(String str, int idx) {
        char[] arr = str.toCharArray();
        // 2->1  3->2  0->9
        if (arr[idx] == '0')
            arr[idx] = '1';
        else arr[idx] -= 1;
        return new String(arr);
    }

}
