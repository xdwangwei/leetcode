package com.dp;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/10 12:03
 * @description: _918_CatAndMouse
 *
 * 913. 猫和老鼠
 * 两位玩家分别扮演猫和老鼠，在一张 无向 图上进行游戏，两人轮流行动。
 *
 * 图的形式是：graph[a] 是一个列表，由满足 ab 是图中的一条边的所有节点 b 组成。
 *
 * 老鼠从节点 1 开始，第一个出发；猫从节点 2 开始，第二个出发。在节点 0 处有一个洞。
 *
 * 在每个玩家的行动中，他们 必须 沿着图中与所在当前位置连通的一条边移动。例如，如果老鼠在节点 1 ，那么它必须移动到 graph[1] 中的任一节点。
 *
 * 此外，猫无法移动到洞中（节点 0）。
 *
 * 然后，游戏在出现以下三种情形之一时结束：
 *
 * 如果猫和老鼠出现在同一个节点，猫获胜。
 * 如果老鼠到达洞中，老鼠获胜。
 * 如果某一位置重复出现（即，玩家的位置和移动顺序都与上一次行动相同），游戏平局。
 * 给你一张图 graph ，并假设两位玩家都都以最佳状态参与游戏：
 *
 * 如果老鼠获胜，则返回 1；
 * 如果猫获胜，则返回 2；
 * 如果平局，则返回 0 。
 *
 * 示例 1：
 *
 *
 * 输入：graph = [[2,5],[3],[0,4,5],[1,4,5],[2,3],[0,2,3]]
 * 输出：0
 * 示例 2：
 *
 *
 * 输入：graph = [[1,3],[0],[3],[0,2]]
 * 输出：1
 *
 *
 * 提示：
 *
 * 3 <= graph.length <= 50
 * 1 <= graph[i].length < graph.length
 * 0 <= graph[i][j] < graph.length
 * graph[i][j] != i
 * graph[i] 互不相同
 * 猫和老鼠在游戏中总是移动
 */
public class _918_CatAndMouse {


    // 猫赢
    static final int CAT_WIN = 2;
    // 老鼠赢
    static final int MOUSE_WIN = 1;
    // 平局
    static final int DRAW = 0;

    int[][] graph;
    int n;
    // 自顶向下记忆化数组
    int[][][] dp;


    /**
     * 自顶向下动态规划
     * 状态：猫的位置、老鼠的位置、当前谁先手
     * 使用三维数组 dp 表示状态，dp[mouse][cat][turns] 表示从老鼠位于节点mouse、猫位于节点 cat、游戏已经进行了 turns 轮的状态开始，猫和老鼠都按照【最优】策略的情况下的游戏结果。
     * 假设图中的节点数是 n，则有 0 ≤ mouse,cat < n。利用turns的奇偶性分辨当前轮到谁先手
     *
     *      老鼠可能的位置数是 n，因此猫可能的位置数是 n−1（由于猫不能移动到节点 0），当前先手 有 2 种可能，因此游戏中所有可能的局面数是 2n(n−1)。
     *
     * 由于游戏的初始状态是老鼠位于节点 1，猫位于节点 2，因此 dp[1][2][0] 为从初始状态开始的游戏结果。
     *
     * 动态规划的边界条件为可以直接得到游戏结果的状态，包括以下三种状态：
     *
     * 1. 如果 mouse=0，老鼠躲入洞里，则老鼠获胜，因此对于任意 cat 和 turns 都有 dp[0][cat][turns]=1，该状态为老鼠的必胜状态，猫的必败状态。
     *
     * 2. 如果 cat=mouse，猫和老鼠占据相同的节点，则猫获胜，因此当 cat=mouse 时，对于任意 mouse、cat 和 turns 都有 dp[mouse][cat][turns]=2，该状态为老鼠的必败状态，猫的必胜状态。
     *      注意猫不能移动到节点 0
     *
     * 3. 如果 turns ≥ 2n(n−1)，则是平局，该状态为双方的必和状态。
     *      直观理解：全部局面数是 2n(n−1)， 从初始态 走了 2n(n−1) 轮后，根据抽屉原理，当前局面至少和之前某个局面一致，由于 二者都以最优状态参与，所以 结果只会是平局
     *      理论证明：看官方题解吧，其实感觉没必要
     *
     * 实现方面，双方移动的策略相似，根据游戏已经进行的轮数的奇偶性决定当前轮到的玩家。对于特定玩家的移动，实现方法如下：
     *
     * 1. 如果当前玩家存在一种移动方法到达非必败状态，则用该状态更新游戏结果。
     *
     *      如果该移动方法到达必胜状态，则将当前状态（移动前的状态）设为必胜状态，结束遍历其他可能的移动。
     *
     *      如果该移动方法到达必和状态，则将当前状态（移动前的状态）设为必和状态，继续遍历其他可能的移动，因为可能存在到达必胜状态的移动方法。
     *
     * 2. 如果当前玩家的任何移动方法都到达必败状态，则将当前状态（移动前的状态）设为必败状态。
     *
     * 代码写法就是，
     *      先默认自己赢不了，然后遍历所有邻接状态，如果某个状态下自己赢了或者平局，那么更新返回值，并且，如果自己赢了，直接停止遍历，并认为当前局面自己会赢，否则，就遍历完所有邻接态，最好的结果也就是个平局
     *
     *
     * 时间复杂度：
     *
     * 由于老鼠可能的位置有 n 个，猫可能的位置有 n−1 个，游戏轮数最大为 2n(n−1)，因此动态规划的状态数是 O(n^4)，对于每个状态需要 O(n) 的时间计算状态值，
     * 因此总时间复杂度是 O(n^5)，该时间复杂度会超出时间限制，因此自顶向下的动态规划不适用于这道题。以下代码为自顶向下的动态规划的实现，仅供读者参考。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/cat-and-mouse/solution/mao-he-lao-shu-by-leetcode-solution-444x/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param graph
     * @return
     */
    public int catMouseGame(int[][] graph) {
        this.graph = graph;
        this.n = graph.length;
        // 鼠可能的位置有 n 个，猫可能的位置有 n−1 个，游戏轮数最大为 2n(n−1)，之后都是重复
        this.dp = new int[n][n][2 * n * (n - 1)];
        // 初始化记忆数组
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        // 初始，猫在1，老鼠在2，游戏进行了0轮
        return dp(1, 2, 0);
    }

    /**
     * 自定向下记忆化搜索
     * @param mouse
     * @param cat
     * @param turns
     * @return
     */
    private int dp(int mouse, int cat, int turns) {
        // 开始出现重复局面，直接返回平局，因为 下面要用到 dp[mouse][cat][turns]，避免turns越界，先进行这个if判断
        if (turns == 2 * n * (n - 1)) {
            return DRAW;
        }
        // 当前局面已计算过，直接返回
        if (dp[mouse][cat][turns] != -1) {
            return dp[mouse][cat][turns];
        }
        // 计算当前局面结果
        int ans = -1;
        // 老鼠入洞，老鼠赢
        if (mouse == 0) {
            ans = MOUSE_WIN;
        // 猫和老鼠同一位置，猫赢
        } else if (cat == mouse) {
            ans = CAT_WIN;
        // 其他
        } else {
            // 当前轮到老鼠移动，mouse
            if (turns % 2 == 0) {
                // 假设赢不了
                ans = CAT_WIN;
                // 遍历老鼠可能移动到的位置
                for (int nextMouse : graph[mouse]) {
                    // 下一局面的结果
                    int nextAns = dp(nextMouse, cat, turns + 1);
                    // 下一局面结果是 平局 或者 老鼠赢，就更新返回结果
                    if (nextAns != CAT_WIN) {
                        ans = nextAns;
                        // 如果 某个局面是老鼠赢，那不再遍历其他的了，否则（平局）就继续遍历
                        if (ans != DRAW) {
                            break;
                        }
                    }

                }
            } else {
                // 当前轮到猫移动，cat
                // 假设 猫 赢不了
                ans = MOUSE_WIN;
                // 遍历所有可能的移动位置
                for (int nextCat : graph[cat]) {
                    // 猫不能去0号位置
                    if (nextCat == 0) {
                        continue;
                    }
                    // 下一局面的结果
                    int nextAns = dp(mouse, nextCat, turns + 1);
                    // 如果出现 猫赢 或者 平局
                    if (nextAns != MOUSE_WIN) {
                        // 更新返回值
                        ans = nextAns;
                        // 返现更新后是 猫赢，那就不再遍历其他的
                        if (ans != DRAW) {
                            break;
                        }
                    }
                }
            }
        }
        // 记录当前局面结果
        dp[mouse][cat][turns] = ans;
        // 返回当前局面结果
        return ans;
    }


    /**
     * 自底向上的动态规划、拓扑排序
     * 自顶向下的动态规划由于判定平局的标准和轮数有关，因此时间复杂度较高。为了降低时间复杂度，需要使用自底向上的方法实现，消除结果和轮数之间的关系。
     *
     * 使用自底向上的方法实现时，游戏中的状态由老鼠的位置、猫的位置和轮到移动的一方三个因素确定。初始时，只有边界情况的胜负结果已知，其余所有状态的结果都初始化为平局。边界情况为直接确定胜负的情况，包括两类情况：老鼠躲入洞里，无论猫位于哪个结点，都是老鼠获胜；猫和老鼠占据相同的节点，无论占据哪个结点，都是猫获胜。
     *
     * 从边界情况出发遍历其他情况。对于当前状态，可以得到老鼠的位置、猫的位置和轮到移动的一方，根据当前状态可知上一轮的所有可能状态，其中上一轮的移动方和当前的移动方相反，上一轮的移动方在上一轮状态和当前状态所在的节点不同。假设当前状态是老鼠所在节点是 mouse，猫所在节点是 cat，则根据当前的移动方，可以得到上一轮的所有可能状态：
     *
     * 如果当前的移动方是老鼠，则上一轮的移动方是猫，上一轮状态中老鼠所在节点是 mouse，猫所在节点可能是 graph[cat] 中的任意一个节点（除了节点 0）；
     *
     * 如果当前的移动方是猫，则上一轮的移动方是老鼠，上一轮状态中老鼠所在节点可能是 graph[mouse] 中的任意一个节点，猫所在节点是 cat。
     *
     * 对于上一轮的每一种可能的状态，如果该状态的结果已知不是平局，则不需要重复计算该状态的结果，只有对结果是平局的状态，才需要计算该状态的结果。对于上一轮的移动方，只有当可以确定上一轮状态是必胜状态或者必败状态时，才更新上一轮状态的结果。
     *
     * 具体直接看题解吧
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/cat-and-mouse/solution/mao-he-lao-shu-by-leetcode-solution-444x/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param graph
     * @return
     */

    // results[i][j][turn] 表示，老鼠在i位置，猫在j位置，先手是turn的游戏结果
    // 初始，全部赋值为 0， 也就是 平局，我们只需要找出所有 胜/负，
    private int results[][][];
    // degrees[i][j][turn] 表示，老鼠在i位置，猫在j位置，先手是turn的 所有邻接点 的入度
    // 因为你要知道，状态 results[i][j][turn] 为 胜，那只需要一个邻接点的状态为胜，
    // 但你要判断它必败，需要所有邻接点都是必败，所以每碰到一个失败的邻接点((上一轮的移动方和当前状态的结果的获胜方不同))，就把它的度数-1
    // 如果上一轮状态的度减少到 0，则从上一轮状态出发到达的所有状态都是上一轮的移动方的必败状态，因此上一轮状态也是上一轮的移动方的必败状态。
    private int degrees[][][];
    // 当前先手是谁
    static final int MOUSE_TURN = 0, CAT_TURN = 1;


    /**
     * 自底向上的动态规划、拓扑排序
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/cat-and-mouse/solution/mao-he-lao-shu-by-leetcode-solution-444x/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param graph
     * @return
     */
    public int catMouseGame2(int[][] graph) {
        this.graph = graph;
        this.n = graph.length;
        // results[i][j][turn] 表示，老鼠在i位置，猫在j位置，先手是turn的游戏结果
        // 默认都是 draw，也就是0，就是平局
        results = new int[n][n][2];
        // degrees[i][j][turn] 表示，老鼠在i位置，猫在j位置，先手是turn的 所有邻接点 的度
        degrees = new int[n][n][2];

        // 枚举 老鼠的位置，
        for (int i = 0; i < n; ++i) {
            // 枚举 j 的位置
            for (int j = 1; j < n; ++j) {
                degrees[i][j][MOUSE_TURN] = graph[i].length;
                degrees[i][j][CAT_TURN] = graph[j].length;
            }
        }

        // 猫不能入洞，相当于 能到达0号位置的那些点的度数受限，我们需要将这个条件处理到degrees上
        // 猫不能入洞，也就是 猫入洞就是猫的必败态，那么对于 所有 猫为先手 且 能跳到0号位置 的状态来说，他们的 degree--，记住我们统计的degree的意义
        // 这些位置能入0
        for (int neighbor : graph[0]) {
            // 此时老鼠可以在任意位置
            for (int i = 0; i < n; ++i) {
                // 猫从这些位置 入0就是 猫必败，那么 对于这些位置来说，它的 非必败邻接点个数减1
                degrees[i][neighbor][CAT_TURN]--;
            }
        }

        // 拓扑排序，队列中都是确定了游戏结果的状态点
        Queue<int[]> queue = new ArrayDeque<>();
        // base case
        // 老鼠在0位置时，无论谁先手，无论猫在哪里（非0），老鼠必胜
        for (int j = 1; j < n; ++j) {
            results[0][j][CAT_TURN] = MOUSE_WIN;
            results[0][j][MOUSE_TURN] = MOUSE_WIN;
            queue.offer(new int[]{0, j, CAT_TURN});
            queue.offer(new int[]{0, j, MOUSE_TURN});
        }
        // base case
        // 猫和老鼠在同一位置(非0)时，无论谁先手，猫必胜
        for (int j = 1; j < n; ++j) {
            results[j][j][CAT_TURN] = CAT_WIN;
            results[j][j][MOUSE_TURN] = CAT_WIN;
            queue.offer(new int[]{j, j, CAT_TURN});
            queue.offer(new int[]{j, j, MOUSE_TURN});
        }
        // 拓扑排序
        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int mouse = state[0], cat = state[1], turn = state[2];
            // 当前状态的结果
            int result = results[mouse][cat][turn];
            // 所有可能的上一结果
            List<int[]> prevStates = getPrevStates(mouse, cat, turn);
            for (int[] prevState : prevStates) {
                int prevMouse = prevState[0], prevCat = prevState[1], prevTurn = prevState[2];
                int prevResult = results[prevMouse][prevCat][prevTurn];
                // 我们只去更新还未被更新过的节点，并且只在能够确定其必胜必败性的情况下去更新
                if (prevResult == DRAW) {
                    // 只在能够确定其必胜必败性的情况下 去更新 上一轮结果
                    // 如果 上一轮的移动方 和 当前的 获胜方 一致，说明 它移动完就赢了，那么上一轮 就是它的一个 必胜态
                    // 这一轮结果相当于上一轮的一个可能状态，如果这一轮 它不是先手但是它赢了，那不就说明上一轮时，它一定是必胜的？
                    if ((result == CAT_WIN && prevTurn == CAT_TURN) || (result == MOUSE_WIN && prevTurn == MOUSE_TURN)) {
                        // 更新上一轮结果 为 这一轮结果
                        results[prevMouse][prevCat][prevTurn] = result;
                        // 确定了游戏结果的点 入队列
                        queue.offer(new int[]{prevMouse, prevCat, prevTurn});
                    // 如果 上一轮的移动方 和 当前的 获胜方 不一致，说明 当前状态就是它的一个 非必胜态，那么 它的邻接点度数-1
                    } else {
                        degrees[prevMouse][prevCat][prevTurn]--;
                        // 如果上一轮状态的度减少到 0，则从上一轮状态出发到达的所有状态都是上一轮的移动方的必败状态，因此上一轮状态也是上一轮的移动方的必败状态。
                        if (degrees[prevMouse][prevCat][prevTurn] == 0) {
                            // 此时就能确定上一状态的游戏结果，上一轮的移动方的必败状态，那么 游戏结果获胜方 就是 另一方
                            // 更新这个状态点
                            results[prevMouse][prevCat][prevTurn] = prevTurn == MOUSE_TURN ? CAT_WIN : MOUSE_WIN;
                            // 确定了游戏结果的点 入队列
                            queue.offer(new int[]{prevMouse, prevCat, prevTurn});
                        }
                    }
                }

            }
        }
        // 返回 初始 老鼠在1，猫在2，先手是老鼠时 的游戏结果
        return results[1][2][MOUSE_TURN];
    }

    /**
     * 获取指定状态的全部可能前一状态
     * @param mouse
     * @param cat
     * @param turn
     * @return
     */
    private List<int[]> getPrevStates(int mouse, int cat, int turn) {
        List<int[]> list = new ArrayList<>();
        int prevTurn = turn == MOUSE_TURN ? CAT_TURN : MOUSE_TURN;
        // 上一轮是 老鼠先手
        if (prevTurn == MOUSE_TURN) {
            // 那么 老鼠可能从这些位置(当前老鼠位置的邻接位置)跳到当前位置
            for (int prevMouse : graph[mouse]) {
                // 加入上一状态
                list.add(new int[]{prevMouse, cat, MOUSE_TURN});
            }
        } else {
            // 上一状态是 猫先手，
            // 那么 猫可能从这些位置(当前猫位置的邻接位置)跳到当前位置
            for (int prevCat : graph[cat]) {
                if (prevCat != 0) {
                    list.add(new int[]{mouse, prevCat, CAT_TURN});
                }
            }
        }
        return list;
    }
}
