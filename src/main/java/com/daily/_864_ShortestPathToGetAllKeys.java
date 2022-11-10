package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/11/10 10:24
 * @description: _864_ShortestPathToGetAllKeys
 *
 * 864. 获取所有钥匙的最短路径
 * 给定一个二维网格 grid ，其中：
 *
 * '.' 代表一个空房间
 * '#' 代表一堵
 * '@' 是起点
 * 小写字母代表钥匙
 * 大写字母代表锁
 * 我们从起点开始出发，一次移动是指向四个基本方向之一行走一个单位空间。我们不能在网格外面行走，也无法穿过一堵墙。如果途经一个钥匙，我们就把它捡起来。除非我们手里有对应的钥匙，否则无法通过锁。
 *
 * 假设 k 为 钥匙/锁 的个数，且满足 1 <= k <= 6，字母表中的前 k 个字母在网格中都有自己对应的一个小写和一个大写字母。换言之，每个锁有唯一对应的钥匙，每个钥匙也有唯一对应的锁。另外，代表钥匙和锁的字母互为大小写并按字母顺序排列。
 *
 * 返回获取所有钥匙所需要的移动的最少次数。如果无法获取所有钥匙，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：grid = ["@.a.#","###.#","b.A.B"]
 * 输出：8
 * 解释：目标是获得所有钥匙，而不是打开所有锁。
 * 示例 2：
 *
 *
 *
 * 输入：grid = ["@..aA","..B#.","....b"]
 * 输出：6
 * 示例 3:
 *
 *
 * 输入: grid = ["@Aa"]
 * 输出: -1
 *
 *
 * 提示：
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 30
 * grid[i][j] 只含有 '.', '#', '@', 'a'-'f' 以及 'A'-'F'
 * 钥匙的数目范围是 [1, 6]
 * 每个钥匙都对应一个 不同 的字母
 * 每个钥匙正好打开一个对应的锁
 */
public class _864_ShortestPathToGetAllKeys {

    /**
     * 每次有多个选择，求从起始状态到目标状态的最短路径
     *
     * BFS + 状态压缩
     * 一道常规的 BFS 运用题，只不过需要在 BFS 过程中记录收集到的钥匙状态。
     *
     * 利用「钥匙数量不超过 6，并按字母顺序排列」，我们可以使用一个 int 类型二进制数 keys 来代指当前收集到钥匙情况：
     *
     * 若 keys 的二进制中的第 k 位为 1，代表当前种类编号为 k 的钥匙 已被收集，后续移动若遇到对应的锁则 能通过
     * 若 keys 的二进制中的第 k 位为 0，代表当前种类编号为 k 的钥匙 未被收集，后续移动若遇到对应的锁则 无法通过
     *
     * 其中「钥匙种类编号」则按照小写字母先后顺序，从 0 开始进行划分对应：
     * 即字符为 a 的钥匙编号为 0，字符为 b 的钥匙编号为 1，字符为 c 的钥匙编号为 2 ...
     *
     * 当使用了这样的「状态压缩」技巧后，我们可以很方便通过「位运算」进行 钥匙检测 和 更新钥匙收集状态：
     *
     * 钥匙检测：(keys >> k) & 1，若返回 1 说明第 k 位为 1，当前持有种类编号为 k 的钥匙
     * 更新钥匙收集状态：keys |= 1 << k，将 keys 的第 k 位设置为 1，代表当前新收集到种类编号为 k 的钥匙
     *
     * 搞明白如何记录当前收集到的钥匙状态后，剩下的则是常规 BFS 过程：
     *
     * 起始遍历一次棋盘，找到起点位置，并将其进行入队，同时统计整个棋盘所包含的钥匙数量 keys，
     * 队列维护的是 (x,y,keys) 三元组状态（其中 (x,y) 代表当前所在的棋盘位置，keys 代表当前的钥匙收集情况,起点时为0）
     *
     * 进行四联通方向的 BFS，转移过程中需要注意「遇到锁时，必须有对应钥匙才能通过」&「遇到钥匙时，需要更新对应的 keys 再进行入队」
     *
     * 当 BFS 过程中遇到 keys == cnt 时，代表所有钥匙均被收集完成，可结束搜索
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/shortest-path-to-get-all-keys/solution/by-ac_oier-5gxc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 上下左右方向导航
    private static final int[] dirs = new int[]{-1, 0, 1, 0, -1};

    // 队列中的三元组
    // 之所以定义类并重写hashcode和equals方法是因为bfs过程中需要去重，set集合判断重复，需要重写
    // 也可以使用三维boolean数组
    static class Node {
        // 当前位置
        int x, y;
        // 当前获得的钥匙状体，二进制第k位为1表示获得了第k把钥匙
        int keys;

        Node(int x, int y, int keys) {
            this.x = x;
            this.y = y;
            this.keys = keys;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y && keys == node.keys;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, keys);
        }
    }

    /**
     * bfs
     * @param grid
     * @return
     */
    public int shortestPathAllKeys(String[] grid) {
        int n = grid.length, m = grid[0].length();
        int startX = 0, startY = 0, keys = 0;
        // 寻找起点，统计所有钥匙数量
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; ++j) {
                char c = grid[i].charAt(j);
                if (c == '@') {
                    startX = i;
                    startY = j;
                } else if (isKey(c)) {
                    keys |= 1 << (c - 'a');
                }
            }
        }
        // bfs
        // 初始节点入队列
        Queue<Node> queue = new ArrayDeque<>();
        // 每个节点都需要考虑上下左右，避免重复遍历
        Set<Node> visited = new HashSet<>();
        Node start = new Node(startX, startY, 0);
        queue.offer(start);
        visited.add(start);
        // 步数。四叉树层数
        int step = 0;
        while (!queue.isEmpty()) {
            // 当前层节点个数
            int sz = queue.size();
            for (int i = 0; i < sz; ++i) {
                Node cur = queue.poll();
                // 获得了全部钥匙，返回
                if (cur.keys == keys) {
                    return step;
                }
                // 上下左右四个方向点
                for (int k = 0; k < dirs.length - 1; ++k) {
                    int nx = cur.x + dirs[k], ny = cur.y + dirs[k + 1];
                    // 不能越界
                    if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                        continue;
                    }
                    char d = grid[nx].charAt(ny);
                    // 墙 无法通过
                    if (d == '#') {
                        continue;
                    }
                    // 是锁，但没有钥匙，无法通过
                    if (isLock(d) && !hasKeyForLock(cur.keys, d)) {
                        continue;
                    }
                    // 是空白，是钥匙，是墙并且有钥匙，构造新节点
                    Node nn = new Node(nx, ny, cur.keys);
                    // 如果是钥匙，则更新钥匙状态
                    if (isKey(d)) {
                        nn.keys |= 1 << (d - 'a');
                    }
                    // 避免重复入队
                    if (!visited.contains(nn)) {
                        queue.offer(nn);
                        visited.add(nn);
                    }
                }
            }
            step++;
        }
        // 无解，返回-1
        return -1;
    }

    /**
     * 判断当前字符是否是钥匙
     * @param c
     * @return
     */
    private boolean isKey(char c) {
        return Character.isLowerCase(c);
    }

    /**
     * 当前字符是否代表锁
     * @param c
     * @return
     */
    private boolean isLock(char c) {
        return Character.isUpperCase(c);
    }

    /**
     * 当前是否具有锁d的钥匙
     * 要求这个d必须代表锁
     * @param keys
     * @param d
     * @return
     */
    private boolean hasKeyForLock(int keys, char d) {
        return (keys >> (d - 'A') & 1) == 1;
    }


    /**
     * 空间优化，因为 根据题意，x，y 均小于 30，只需要5个二进制位，钥匙数量不会超过6并且是按abcdef顺序编号，需要6个二进制位
     * 那么可以使用一个int同时代表x，y，keys
     * 低8位代表当前钥匙状态，8到15位代表y，16到23位代表x
     * 那么比如初始状态 (3, 8, 0) 就可表示为 00000000 00000011 00001000 00000000
     * @param grid
     * @return
     */
    public int shortestPathAllKeys2(String[] grid) {
        int n = grid.length, m = grid[0].length();
        int startX = 0, startY = 0, keys = 0;
        // 寻找起点，统计所有钥匙数量
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; ++j) {
                char c = grid[i].charAt(j);
                if (c == '@') {
                    startX = i;
                    startY = j;
                } else if (isKey(c)) {
                    keys |= 1 << (c - 'a');
                }
            }
        }
        // bfs
        Queue<Integer> queue = new ArrayDeque<>();
        // 每个节点都需要考虑上下左右，避免重复遍历
        Set<Integer> visited = new HashSet<>();
        // 上下左右方向导航
        final int[] dirs = new int[]{-1, 0, 1, 0, -1};
        // 构造初始节点
        int startState = buildState(startX, startY, 0);
        // 初始节点入队列
        queue.offer(startState);
        visited.add(startState);
        // 步数。四叉树层数
        int step = 0;
        while (!queue.isEmpty()) {
            // 当前层节点个数
            int sz = queue.size();
            for (int i = 0; i < sz; ++i) {
                int state = queue.poll();
                int x = getX(state), y = getY(state), curKeys = getKeys(state);
                // 上下左右四个方向点
                for (int k = 0; k < dirs.length - 1; ++k) {
                    int nx = x + dirs[k], ny = y + dirs[k + 1];
                    // 不能越界
                    if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                        continue;
                    }
                    char d = grid[nx].charAt(ny);
                    // 墙 无法通过
                    if (d == '#') {
                        continue;
                    }
                    // 是锁，但没有钥匙，无法通过
                    if (isLock(d) && !hasKeyForLock(curKeys, d)) {
                        continue;
                    }
                    // 是空白，是钥匙，是墙并且有钥匙，构造新节点
                    int nkeys = curKeys;
                    // 如果是钥匙，则更新钥匙状态
                    if (isKey(d)) {
                        nkeys |= 1 << (d - 'a');
                    }
                    // 获得了全部钥匙，提前返回
                    if (nkeys == keys) {
                        return step + 1;
                    }
                    // 构造邻接点状态
                    int nstate = buildState(nx, ny, nkeys);
                    // 避免重复入队
                    if (!visited.contains(nstate)) {
                        queue.offer(nstate);
                        visited.add(nstate);
                    }
                }
            }
            step++;
        }
        // 无解，返回-1
        return -1;
    }

    /**
     * 构造int值，低8位代表当前钥匙状态，8到15位代表y，16到23位代表x
     * @param x
     * @param y
     * @param keys
     * @return
     */
    private int buildState(int x, int y, int keys) {
        return x << 16 | y << 8 | keys;
    }

    /**
     * 从state中获取坐标x
     * @param state
     * @return
     */
    private int getX(int state) {
        return state >> 16;
    }

    /**
     * 从state中获取坐标y
     * @param state
     * @return
     */
    private int getY(int state) {
        return (state & 0x0000ff00) >> 8;
    }

    /**
     * 从state中获取钥匙状态
     * @param state
     * @return
     */
    private int getKeys(int state) {
        return state & 0x0000003f;
    }

    public static void main(String[] args) {
        _864_ShortestPathToGetAllKeys obj = new _864_ShortestPathToGetAllKeys();
        System.out.println(obj.shortestPathAllKeys(new String[]{"@Aa"}));
    }
}
