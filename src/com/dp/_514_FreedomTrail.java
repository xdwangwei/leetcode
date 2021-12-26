package com.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author wangwei
 * 2021/12/13 17:54
 *
 * 电子游戏“辐射4”中，任务“通向自由”要求玩家到达名为“Freedom Trail Ring”的金属表盘，并使用表盘拼写特定关键词才能开门。
 *
 * 给定一个字符串ring，表示刻在外环上的编码；给定另一个字符串key，表示需要拼写的关键词。您需要算出能够拼写关键词中所有字符的最少步数。
 *
 * 最初，ring的第一个字符与12:00方向对齐。您需要顺时针或逆时针旋转 ring 以使key的一个字符在 12:00 方向对齐，然后按下中心按钮，以此逐个拼写完key中的所有字符。
 *
 * 旋转ring拼出 key 字符key[i]的阶段中：
 *
 * 您可以将ring顺时针或逆时针旋转一个位置，计为1步。旋转的最终目的是将字符串ring的一个字符与 12:00 方向对齐，并且这个字符必须等于字符key[i] 。
 * 如果字符key[i]已经对齐到12:00方向，您需要按下中心按钮进行拼写，这也将算作1 步。按完之后，您可以开始拼写key的下一个字符（下一阶段）, 直至完成所有拼写。
 * 示例：
 *
 *
 *
 *
 *
 * 输入: ring = "godding", key = "gd"
 * 输出: 4
 * 解释:
 *  对于 key 的第一个字符 'g'，已经在正确的位置, 我们只需要1步来拼写这个字符。
 *  对于 key 的第二个字符 'd'，我们需要逆时针旋转 ring "godding" 2步使它变成 "ddinggo"。
 *  当然, 我们还需要1步进行拼写。
 *  因此最终的输出是 4。
 * 提示：
 *
 * ring 和key的字符串长度取值范围均为1 至100；
 * 两个字符串中都只有小写字符，并且均可能存在重复字符；
 * 字符串key一定可以由字符串 ring旋转拼出。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/freedom-trail
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _514_FreedomTrail {

    /**
     * 动态规划
     *
     * 最值问题，而且有比较明显的特征，首先是找最小操作次数，然后，每次可以向左向右转
     *
     * 明确状态 + 选择，状态，也就是 需要哪些部分才能完整描述出当前场面；对于这个题，首先要知道当前圆盘指针指在哪个位置，下一个目标字符是什么(目标串还有哪些字符没处理)
     * 而选择就是，逆时针 还是 顺序针，
     *      另外，假如 下一个目标字符x在圆盘上出现多次，那么无论是逆时针还是顺时针，都会有多个选择x1，x2，
     *          这种情况不能说哪个近x1，就转哪个x1，说不定下一个字符就在x2的隔壁呢，你现在选了x1，那么下一次就要转更远的距离
     *      所以需要把后续的情况计算出来，再比较，做出选择
     *   这样的话，相当于回溯，因此需要备忘录提高效率, 自定向下的动态规划 + 备忘录
     *
     * 这个 dp 函数的定义如下：
     *      当圆盘指针指向 ring[i] 时，输入字符串 key[j..] 至少需要 dp(ring, i, key, j) 次操作。
     *          当 j = key.length 时，说明处理完了。
     * @param ring
     * @param key
     * @return
     */
    // 需要记录转盘上的每个字符在转盘上的索引。
    HashMap<Character, List<Integer>> charToIndex;
    // 备忘录。
    int[][] memo;
    public int findRotateSteps(String ring, String key) {
        int ringLen = ring.length(), keyLen = key.length();
        // 需要记录转盘上的每个字符在转盘上的索引。
        charToIndex = new HashMap<>();
        for (int i = 0; i < ringLen; ++i) {
            charToIndex.putIfAbsent(ring.charAt(i), new ArrayList<>());
            charToIndex.get(ring.charAt(i)).add(i);
        }
        // 备忘录。
        // 既然每一次dp是先通过变量得到结果，再给memo赋值，而不是迭代更新memo(如memo=min(memo,cur)等)，那么memo初始值为0即可。不需要初始化为MAX
        // 对于自底向上的动态规划，每一步都是min或max方式迭代更新dp[i][j]，所以通常需要初始化
        memo = new int[ringLen][keyLen];
        // 初始化备忘录
        // for (int i = 0; i < ringLen; ++i) {
        //     Arrays.fill(memo[i], Integer.MAX_VALUE);
        // }
        // 初始，指针在12:00方向，记为0位置，j=0.代表需要处理的字符为key[0...完]
        return dp(ring, 0, key, 0);
    }

    private int dp(String ring, int i, String key, int j) {
        // key已经处理完了，不用操作。返回 0
        if (j == key.length()) {
            return 0;
        }
        // 已计算过
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        // 这个字符出现在圆盘上这些位置
        List<Integer> indexes = charToIndex.get(key.charAt(j));
        // 圆盘上所有可取的位置，选择操作次数最少
        int res = Integer.MAX_VALUE;
        // 对于每一个可能位置，当前位置是 i
        // 注意 for循环内部递归调用了dp函数，而dp函数里面会对memo的值进行判断，所以这里不能边遍历，边更新memo[i][j]，否则会导致for循环没有正确执行完
        // 需要用一个变量记录每次的结果，选择最小值，for循环结束后再赋值给memo[i][j]
        // 既然是通过变量的方式给memo赋值，而不是迭代更新，那么memo初始值为0即可。不需要初始化为MAX
        for (Integer index : indexes) {
            // 如果是顺时针转过去，需要 abs(index - i) 次, 注意需要加 abs，否则从 0 位置转到 6位置怎么算
            // 如果是逆时针转过去，圆盘嘛，肯定互相对称， ringLen - abs(index - i)
            // 选择次数少的方向，
            int step = Math.abs(index - i);
            step = Math.min(step, ring.length() - step);
            // 剩余的步数
            int left = dp(ring, index, key, j + 1);
            // 选择这个位置一共要操作
            int cur = step + left + 1;
            // 在多个位置中选择最小的那个
            res = Math.min(res, cur);
        }
        // 更新备忘录
        memo[i][j] = res;
        return memo[i][j];
    }

    /**
     * 自底向上动态规划
     *
     * dp[i][j] 表示 若初始从rings[i]开始,处理完key[0...j]部分，所用的最少操作步数
     *
     * 对于 key[0]可以直接得到结果，作为 base case，找到转盘上那些 key[0] 字符的位置，选一个转到0位置，选择最小的那个
     *
     * 那么 递推就是正着进行。并且最后应该返回，从转盘任意一个位置开始，输入完所有key，需要的最少步数
     *         for (int i = 0; i < ringLen; ++i) {
     *             res = Math.min(res, dp[i][keyLen - 1]);
     *         }
     *
     * 对于每个字符，应该怎么转动转盘？所以应该让j在外循环
     * 关于递推 对于 key[j]，上一个字符是 key[j-1], 出现在了转盘 charToindex.get(key[j-1])等位置，从这些位置输入key[j-1]的次数是 dp[k][j-1]
     *     当前字符key[j]出现在了charToindex.get(key[j])等位置，那么之前的可选位置转到当前这些可选位置，再加上之前的操作次数。在这些值中取最小值
     *        // 对于当前字符key[j]
     *         for (int j = 1; j < keyLen; ++j) {
     *             // 对于圆盘上存在的多个j
     *             for (Integer idx : charToIndex.get(key.charAt(j))) {
     *
     *                 // 考虑上一步的来源，也就是需要把之前的操作次数加上。那么之前是哪些来源呢，也就是j的上一个字符是怎么拨动的
     *                 for (Integer k : charToIndex.get(key.charAt(j - 1))) {
     *                     // 从之前位置转到当前选择的位置
     *                     int curStep = Math.abs(k - idx);
     *                     curStep = Math.min(curStep, ringLen - curStep) + 1;
     *                     // dp[k][j - 1] 表示 处理到 j 之前的多种来源对应的操作步数
     *                     dp[idx][j] =  Math.min(dp[idx][j], dp[k][j - 1] + curStep);
     *                 }
     *             }
     *         }
     *
     * @param ring
     * @param key
     * @return
     */
    public int findRotateSteps2(String ring, String key) {
        int ringLen = ring.length(), keyLen = key.length();
        // 对于自底向上的动态规划，每一步都是min或max方式迭代更新dp[i][j]，所以通常需要初始化
        int[][] dp = new int[ringLen][keyLen];
        for (int i = 0; i < ringLen; ++i) {
            Arrays.fill(dp[i], 66666);
        }
        // 需要记录转盘上的每个字符在转盘上的索引。
        charToIndex = new HashMap<>();
        for (int i = 0; i < ringLen; ++i) {
            charToIndex.putIfAbsent(ring.charAt(i), new ArrayList<>());
            charToIndex.get(ring.charAt(i)).add(i);
        }
        // base case，单独处理 key 第一个字符 j
        // 对于圆盘上出现的j
        for (Integer idx : charToIndex.get(key.charAt(0))) {
            // 要将指针拨动到0位置，从idx位置开始，需要 idx- 0 或者 ringLen - (idx - 0) 步
            dp[idx][0] = Math.min(idx, ringLen - idx) + 1;
        }
        // 因为我们考虑问题的角度是，对于 key接下来字符，怎么样去转动圆盘，所以，这里让 j 在 外循环
        for (int j = 1; j < keyLen; ++j) {
            // 对于圆盘上存在的多个j
            for (Integer idx : charToIndex.get(key.charAt(j))) {
                // 考虑上一步的来源，也就是需要把之前的操作次数加上。那么之前是哪些来源呢，也就是j的上一个字符是怎么拨动的
                for (Integer k : charToIndex.get(key.charAt(j - 1))) {
                    // 从之前位置转到当前选择的位置
                    int curStep = Math.abs(k - idx);
                    curStep = Math.min(curStep, ringLen - curStep) + 1;
                    // dp[k][j - 1] 表示 处理到 j 之前的多种来源对应的操作步数
                    dp[idx][j] =  Math.min(dp[idx][j], dp[k][j - 1] + curStep);
                }
            }
        }
        // 所有初始位置，输入完 key，得到的所有次数中，选择次数最小
        int res = dp[0][keyLen - 1];
        for (int i = 1; i < ringLen; ++i) {
            res = Math.min(res, dp[i][keyLen - 1]);
        }
        return  res;
    }

    public static void main(String[] args) {
        _514_FreedomTrail obj = new _514_FreedomTrail();
        int steps = obj.findRotateSteps2("godding", "gd");
        System.out.println(steps);
    }
}
