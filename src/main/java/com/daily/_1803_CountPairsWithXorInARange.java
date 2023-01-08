package com.daily;

/**
 * @author wangwei
 * @date 2023/1/7 21:26
 * @description: _1803_CountPairsWithXorInARange
 *
 * 1803. 统计异或值在范围内的数对有多少
 * 给你一个整数数组 nums （下标 从 0 开始 计数）以及两个整数：low 和 high ，请返回 漂亮数对 的数目。
 *
 * 漂亮数对 是一个形如 (i, j) 的数对，其中 0 <= i < j < nums.length 且 low <= (nums[i] XOR nums[j]) <= high 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,4,2,7], low = 2, high = 6
 * 输出：6
 * 解释：所有漂亮数对 (i, j) 列出如下：
 *     - (0, 1): nums[0] XOR nums[1] = 5
 *     - (0, 2): nums[0] XOR nums[2] = 3
 *     - (0, 3): nums[0] XOR nums[3] = 6
 *     - (1, 2): nums[1] XOR nums[2] = 6
 *     - (1, 3): nums[1] XOR nums[3] = 3
 *     - (2, 3): nums[2] XOR nums[3] = 5
 * 示例 2：
 *
 * 输入：nums = [9,8,4,2,1], low = 5, high = 14
 * 输出：8
 * 解释：所有漂亮数对 (i, j) 列出如下：
 *     - (0, 2): nums[0] XOR nums[2] = 13
 *     - (0, 3): nums[0] XOR nums[3] = 11
 *     - (0, 4): nums[0] XOR nums[4] = 8
 *     - (1, 2): nums[1] XOR nums[2] = 12
 *     - (1, 3): nums[1] XOR nums[3] = 10
 *     - (1, 4): nums[1] XOR nums[4] = 9
 *     - (2, 3): nums[2] XOR nums[3] = 6
 *     - (2, 4): nums[2] XOR nums[4] = 5
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 2 * 104
 * 1 <= nums[i] <= 2 * 104
 * 1 <= low <= high <= 2 * 104
 * 通过次数14,950提交次数26,794
 */
public class _1803_CountPairsWithXorInARange {

    /**
     *
     * 字典树、前缀树
     *
     * 点1：
     *      对于 统计运算结果 大于等于 x，小于等于 y 的xxx个数 类题目，可将双端限制条件改为单侧限制，
     *      比如本题求解有多少对数字的异或运算结果处于 [low,high] 之间，
     *      为了方便求解，我们用 f(x) 来表示有多少对数字的异或运算结果小于 x，
     *      这时问题变为求解 f(high + 1) − f(low)。
     * 点2：
     *      对于在数组中挑选两个元素异或进行后序统计个数、求取最值等操作类题目，可以考虑使用二分叉的前缀树
     *      由于异或操作是二进制数值按位异或，每个位置异或结果不是1就是0，可以采用二叉树形式表示所有二进制位的异或结果
     *      由于整形数字本质上都是32或64位的二进制树，因此代表抑或结果的二叉树最多32、64层。
     *      对于高位相同的数字可认为具有相同的二进制前缀，因此采用前缀数。
     *
     * 在本题中，对于每个元素 a[i]，需要统计有多少元素 a[j] (j < i) 满足 a[i] xor a[j] < x，
     *
     * 由于数组中的元素都在 [1,2×10^4] 的范围内，20000 < 2^15 = 32768
     * 那么我们可以将每一个数字表示为一个长度为 15 位的二进制数字（若不满 15 位，认为其最高位之前存在若干个前导 0 即可）；
     * 这 15 个二进制位从低位到高位依次编号为 0,1,⋯,14。
     *
     * 我们从最高位第 14 个二进制位开始，依次计算有多少元素与 a[i] 的异或运算结果小于 x 的 a[j]；
     *
     * 对于任意一个使得 a[i] ^ a[j] < x 条件成立的 a[j]，二者的 异或结果 res 的二进制表示中
     * 必然存在一个 k，使得 res 的二进制表示中的第 14 位到第 k+1 位与 x 相同，而第 k 位却小于。
     * 即，字典树中从根到某节点node的路径 与 num 的异或值小于 x，(在 node节点处分出了胜负，node到上一层节点这个路径值就是第k位)
     *
     * 那么，对于任意 以此路径为前缀 (与 a[j]有共同[14...k]部分二进制前缀) 的数字，都满足 与 a[i] 的异或值 < x
     * 因此字典树中每个节点除了0、1左右孩子外，还需要保存经过当前节点的数字个数，
     * 也就是 具有 以 从根到当前节点形成的路径 为二进制的前缀 的数字个数
     * 这样，当遍历到前缀树的某一层时，假设当前路径代表的前缀和 a[i] 的异或结果满足了小于x的限制，
     * 那么不用遍历后序节点，就能知道有多少数字前缀和此路径相同，也就能得到满足限制条件的 a[j] 个数
     *
     * 因此字典树的每个节点记录一个数字cnt，表示有多少个数字以根结点到该节点路径为前缀。
     * 为了计算它，我们需要在添加一个数字到字典树时，将路径上的所有节点的数字都加 1。
     *
     * 为了避免重复统计，对于 a[i] 需要统计 a[0...i-1] 中有多少数字符合要求，
     * 因此，保证遍历到 a[i] 时，字典树只维护了 a[0...i-1] 这些数字，
     *
     * 【算法】
     * 此时，整体算法流程为：
     *      逐个遍历 a[i]，对于每个 a[i]
     *      1.在字典树中去查找满足和 a[i] 异或值 < x 的前缀，从而得到 待求解数字的个数
     *      2.然后，将 a[i] 插入字典树
     *
     * 那么，对于某个 num=a[i]，如何在 字典树 root 中求解 满足与其异或值 < x 的前缀(数字个数)呢？
     * 我们可以按照num的二进制位从高到底开始遍历，对应的路径即在字典树的根结点开始遍历
     * 假设我们当前遍历到了第 k 个二进制位，对应数节点为node：假设 a 是 num 的第k个二进制位，c 是 x 的二进制位
     * 假设返回值为 res
     * 因为c是异或结果的限制条件，我们要希望和a的异或结果 < c
     *
     * 如果 c = 1 && a = 1:
     *      若选择 node 的 0 分支，0 和 a 异或为1，和 c 持平，需要继续往下搜索，寻找更低的异或结果小于c的二进制位
     *      若选择 node 的 1 分支，1 和 a 异或为0，比 c 小，具有此前缀的所有数字和num的异或结果都比x小，res+=node.cnt
     *
     * 如果 c = 1 && a = 0:
     *      若选择 node 的 0 分支，0 和 a 异或为1，
     *      若选择 node 的 0 分支，1 和 a 异或为0，比 c 小，具有此前缀的所有数字和num的异或结果都比x小，res+=node.cnt
     *      若选择 node 的 1 分支，1 和 a 异或为0，和 c 持平，需要继续往下搜索，寻找更低的异或结果小于c的二进制位
     *
     * 如果 c = 0 && a = 1:
     *      若选择 node 的 0 分支，0 和 a 异或为1，比 c 大，不用再向下搜索，舍弃
     *      若选择 node 的 1 分支，0 和 a 异或为0，和 c 持平，需要继续往下搜索，寻找更低的异或结果小于c的二进制位
     * 如果 c = 0 && a = 0:
     *      若选择 node 的 0 分支，0 和 a 异或为0，和 c 持平，需要继续往下搜索，寻找更低的异或结果小于c的二进制位
     *      若选择 node 的 1 分支，0 和 a 异或为1，比 c 大，不用再向下搜索，舍弃
     *
     * 整个过程中，如果遇到搜索节点为null，结束
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/count-pairs-with-xor-in-a-range/solution/tong-ji-yi-huo-zhi-zai-fan-wei-nei-de-sh-cu18/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 字典树
    class Trie {
        // 当前二进制为0、1 二分叉
        Trie[] childs;
        // 有多少个数字以根结点到该节点路径为二进制前缀。
        int cnt;
        Trie() {
            childs = new Trie[2];
            cnt = 0;
        }
    }

    // 数据取值 < 20000，最多需要 15 个二进制位，因此最高比特位为14位
    private static final int HIGH_BIT = 14;

    /**
     * 将数字 num 以二进制形式插入 root为根的字典数中
     * @param root
     * @param num
     */
    private void insert(Trie root, int num) {
        // 从高到底遍历当前数字二进制位
        for(int i = HIGH_BIT; i >= 0; --i) {
            // 为1还是0
            int bit = (num >> i) & 1;
            // 对应节点不存在，就初始化
            if (root.childs[bit] == null) {
                root.childs[bit] = new Trie();
            }
            // 跳到对应节点
            root = root.childs[bit];
            // 以当前路径为前缀的数字个数+1
            // 注意，当前二进制位是路径，路径下才是节点，因此是下方节点的cnt更新，不是上方节点的cnt更新
            root.cnt++;
        }
    }

    /**
     * 在 字典树root 中 寻找满足 与 指定 num 异或值 小于 limit 的路径前缀（以此为前缀的数字个数）
     *
     * @param root
     * @param num
     * @param limit
     * @return
     */
    private int countNumbersXorSmallerThan(Trie root, int num, int limit) {
        // 以此路径为前缀的数字个数
        int ans = 0;
        // 从高到底遍历二进制比特位，对应在树中从root往下遍历
        // 当节点为空，就结束
        for(int i = HIGH_BIT; i >= 0 && root != null; --i) {
            // num当前二进制位值
            int a = (num >> i) & 1;
            // limit当前二进制位值
            // 目标是与num的异或值小于limit，即对于每个二进制位，与a的异或值 < c，若相等就继续向下
            int c = (limit >> i) & 1;
            if (c == 1) {
                // c = 1，a = 0
                if (a == 0) {
                    // 选择路径0，与a的异或值为0，小于c，那么以此路径为前缀的所有数字都满足与num的异或值小于limit
                    // 累加当前节点的cnt
                    // 加if是遍历空指针
                    if (root.childs[0] != null) {
                        ans += root.childs[0].cnt;
                    }
                    // 选择路径1，与a的异或值为1，与c持平，需要继续向下搜索
                    root = root.childs[1];
                // c = 1，a = 1
                } else {
                    // 选择路径1，与a的异或值为0，小于c，那么以此路径为前缀的所有数字都满足与num的异或值小于limit
                    // 累加当前节点的cnt
                    // 加if是遍历空指针
                    if (root.childs[1] != null) {
                        ans += root.childs[1].cnt;
                    }
                    // 选择路径0，与a的异或值为1，与c持平，需要继续向下搜索
                    root = root.childs[0];
                }
            } else {
                // c = 0，a = 0
                if (a == 0) {
                    // 选择路径1，与a的异或值为1，大于c，放弃
                    // 选择路径0，与a的异或值为0，与c持平，需要继续向下搜索
                    root = root.childs[0];
                // c = 0，a = 1
                } else {
                    // 选择路径0，与a的异或值为1，大于c，放弃
                    // 选择路径1，与a的异或值为0，与c持平，需要继续向下搜索
                    root = root.childs[1];
                }
            }
        }
        // 返回
        return ans;
    }

    /**
     * 求 nums 中，存在 多少 pair （i < j） ，满足 nums[i] ^ nums[j] < limit
     * @param nums
     * @param limit
     * @return
     */
    private int countPairsXorSmallerThan(int[] nums, int limit) {
        // 前缀树 保证遍历到 a[i] 时，字典树只维护了 a[0...i-1] 这些数字，
        Trie root = new Trie();
        int ans = 0;
        // 逐个遍历 a[i]，对于每个 a[i]
        for (int num : nums) {
            // 1.在字典树中去查找满足和 a[i] 异或值 < x 的前缀，从而得到 满足要求的a[j]个数
            ans += countNumbersXorSmallerThan(root, num, limit);
            // 2.然后，将 a[i] 插入字典树
            insert(root, num);
        }
        return ans;
    }

    /**
     * 求 nums 中，存在 多少 pair （i < j） ，满足 low <= nums[i] ^ nums[j] <= high
     *
     * 等价于
     *
     * 求 nums 中，存在 多少 pair （i < j） ，满足 nums[i] ^ nums[j] < high + 1
     * 【减去】
     * 求 nums 中，存在 多少 pair （i < j） ，满足 nums[i] ^ nums[j] < low
     * @param nums
     * @param low
     * @param high
     * @return
     */
    public int countPairs(int[] nums, int low, int high) {
        return countPairsXorSmallerThan(nums, high + 1) - countPairsXorSmallerThan(nums, low);
    }

}
