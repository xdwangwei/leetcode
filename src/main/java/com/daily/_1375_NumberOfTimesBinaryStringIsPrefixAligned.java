package com.daily;

/**
 * @author wangwei
 * @date 2023/6/13 10:56
 * @description: _1375_NumberOfTimesBinaryStringIsPrefixAligned
 *
 * 1375. 二进制字符串前缀一致的次数
 * 给你一个长度为 n 、下标从 1 开始的二进制字符串，所有位最开始都是 0 。我们会按步翻转该二进制字符串的所有位（即，将 0 变为 1）。
 *
 * 给你一个下标从 1 开始的整数数组 flips ，其中 flips[i] 表示对应下标 i 的位将会在第 i 步翻转。
 *
 * 二进制字符串 前缀一致 需满足：在第 i 步之后，在 闭 区间 [1, i] 内的所有位都是 1 ，而其他位都是 0 。
 *
 * 返回二进制字符串在翻转过程中 前缀一致 的次数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：flips = [3,2,4,1,5]
 * 输出：2
 * 解释：二进制字符串最开始是 "00000" 。
 * 执行第 1 步：字符串变为 "00100" ，不属于前缀一致的情况。
 * 执行第 2 步：字符串变为 "01100" ，不属于前缀一致的情况。
 * 执行第 3 步：字符串变为 "01110" ，不属于前缀一致的情况。
 * 执行第 4 步：字符串变为 "11110" ，属于前缀一致的情况。
 * 执行第 5 步：字符串变为 "11111" ，属于前缀一致的情况。
 * 在翻转过程中，前缀一致的次数为 2 ，所以返回 2 。
 * 示例 2：
 *
 * 输入：flips = [4,1,2,3]
 * 输出：1
 * 解释：二进制字符串最开始是 "0000" 。
 * 执行第 1 步：字符串变为 "0001" ，不属于前缀一致的情况。
 * 执行第 2 步：字符串变为 "1001" ，不属于前缀一致的情况。
 * 执行第 3 步：字符串变为 "1101" ，不属于前缀一致的情况。
 * 执行第 4 步：字符串变为 "1111" ，属于前缀一致的情况。
 * 在翻转过程中，前缀一致的次数为 1 ，所以返回 1 。
 *
 *
 * 提示：
 *
 * n == flips.length
 * 1 <= n <= 5 * 104
 * flips 是范围 [1, n] 中所有整数构成的一个排列
 * 通过次数27,387提交次数40,285
 */
public class _1375_NumberOfTimesBinaryStringIsPrefixAligned {

    /**
     * 一、思考
     *
     * 初始时 所有位最开始都是 0
     * 前缀一致 需满足：在第 i 步之后，在 闭 区间 [1, i] 内的所有位都是 1 ，而其他位都是 0 。
     *
     * 由于 [1,i] 内恰好有 i 个整数，如果第 i 步「前缀一致」，说明前 i 步的 flips[i] 恰好包含了 1 到 i 的所有数字。
     * 由于每一步只能翻转一个位置，此时必然只反转了[1...i]，满足「其他位都是 0」的要求。（例如示例 1 的前 4 个数和前 5 个数。）
     *
     * 如何判断在第 i 步是否找到了 [1,i] 内的所有整数呢？
     *
     * 二、解惑
     *
     * 由于题目保证 flips 是范围 [1,n] 中所有整数构成的一个排列」，所以前 i 个数互不相同。
     * 如果前 i 个数的最大值等于 i，则说明找到了 [1,i] 内的所有整数。（如果有一个数没找到，那这个数必然大于 i，与最大值等于 i 矛盾。）
     * 或者 前 i 个 flips[i] 之和恰好等于 1+2+...+i=(i+1)*i/2。如果没有找到，必然比 (i+1)*i/2 要大。
     *
     * 三、算法
     *
     * 遍历 flips，维护前 i 个数的最大值 mx。如果 mx=i+1 就把答案加一。
     * （注意代码中的数组下标需要从 0 开始，而题目描述是从 1 开始的。）
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/number-of-times-binary-string-is-prefix-aligned/solution/qiao-miao-li-yong-xing-zhi-wei-hu-zui-da-79yx/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param flips
     * @return
     */
    public int numTimesAllBlue(int[] flips) {
        int ans = 0, mx = 0;
        for (int i = 0; i < flips.length; i++) {
            // flips[1...i] 的最大值
            mx = Math.max(mx, flips[i]);
            // 前 i 步的 flips[i] 恰好包含了 1 到 i 的所有数字。
            // i 是下标，i + 1 是元素个数
            if (i + 1 == mx) {
                ans++;
            }
        }
        return ans;
    }
}
