package com.daily;

/**
 * @author wangwei
 * @date 2022/5/9 10:10
 * @description: _942_DIStringMatch
 *
 *
 * 942. 增减字符串匹配
 * 由范围 [0,n] 内所有整数组成的 n + 1 个整数的排列序列可以表示为长度为 n 的字符串 s ，其中:
 *
 * 如果 perm[i] < perm[i + 1] ，那么 s[i] == 'I'
 * 如果 perm[i] > perm[i + 1] ，那么 s[i] == 'D'
 * 给定一个字符串 s ，重构排列 perm 并返回它。如果有多个有效排列perm，则返回其中 任何一个 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "IDID"
 * 输出：[0,4,1,3,2]
 * 示例 2：
 *
 * 输入：s = "III"
 * 输出：[0,1,2,3]
 * 示例 3：
 *
 * 输入：s = "DDI"
 * 输出：[3,2,0,1]
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 105
 * s 只包含字符 "I" 或 "D"
 */
public class _942_DIStringMatch {

    /**
     * 自己的思路，
     * 首先，数组由0-n共n+1个数字组成
     *  DDI其实代表两相邻数字的大小关系，假设 我初始化所有元素为 0 - n，那么相当于已经蕴含了递增顺序
     *  此时我从后往前遍历s，（这样保证了后面的肯定比前面的大），那么 遇到 ‘I'我直接跳过，因为一定是满足的，
     *  如果遇到 ’D',那么代表 当前位置 应该比 后一个位置 更大，所以我应该交换 这两个位置元素，
     *  但是如果只交换一次，可能存在下面问题：
     *      比如 初始化 0 1 2 3，s = ‘DDI’
     *      倒序遍历到第一个D，   1 和 2 交换， 0 2 1 3
     *      倒序遍历到第二个D，   0 和 2 交换， 2 0 1 3， 此时 0 1 又破坏了 ‘D’ 关系
     *  所以此处的交换应该是一个while，条件是 交换后的位置对应的s字符还是'D‘，那就一直交换，因为I关系一直成立，所以交换是合理的
     * @param s
     * @return
     */
    public int[] diStringMatch(String s) {
        int n = s.length();
        int[] ans = new int[n + 1];
        // 初始化所有元素为 0 - n
        ans[n] = n;
        // 从后往前初始化并遍历s
        for (int i = n - 1; i >= 0; --i) {
            ans[i] = i;
            // I 关系已经成立，只需维护 D 关系
            if (s.charAt(i) == 'D') {
                // 从当前位置开始，破坏D关系，就交换相邻两个位置
                int j = i;
                // 注意不能越界
                while (j < n && s.charAt(j) == 'D') {
                    ans[j] = ans[j] ^ ans[j + 1];
                    ans[j + 1] = ans[j] ^ ans[j + 1];
                    ans[j] = ans[j] ^ ans[j + 1];
                    j++;
                }
            }
        }
        return ans;

    }


    /**
     * 答案：贪心思想
     *
     * 考虑 perm[0] 的值，根据题意：
     *
     * 如果 s[0]==‘I’，那么令 perm[0]=0，则无论 perm[1] 为何值都满足 perm[0]<perm[1]；
     * 如果 s[0]==‘D’，那么令 perm[0]=n，则无论 perm[1] 为何值都满足 perm[0]>perm[1]；
     * 确定好 perm[0] 后，剩余的 n−1 个字符和 n 个待确定的数就变成了一个和原问题相同，但规模为 n−1 的问题。
     * 因此我们可以继续按照上述方法确定 perm[1]：
     * 如果 s[1]=‘I’，那么令 perm[1] 为剩余数字中的最小数；如果 s[1]==‘D’，那么令 perm[1] 为剩余数字中的最大数。
     * 如此循环直至剩下一个数，填入 perm[n] 中。
     *
     * 代码实现时，由于每次都选择的是最小数和最大数，我们可以用两个变量 lo 和 hi 表示当前剩余数字中的最小数和最大数。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/di-string-match/solution/zeng-jian-zi-fu-chuan-pi-pei-by-leetcode-jzm2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int[] diStringMatch2(String s) {
        int n = s.length(), lo = 0, hi = n;
        int[] perm = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            // perm[i]每次去当前可选范围内最小或最大的数字，然后 从 i + 1位置开始就变成 0 - n - 1的子问题
            perm[i] = s.charAt(i) == 'I' ? lo++ : hi--;
        }
        perm[n] = lo; // 最后剩下一个数，此时 lo == hi
        return perm;
    }
}
