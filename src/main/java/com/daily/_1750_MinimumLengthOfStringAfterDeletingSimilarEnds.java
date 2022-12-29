package com.daily;

/**
 * @author wangwei
 * @date 2022/12/29 15:51
 * @description: _1750_MinimumLengthOfStringAfterDeletingSimilarEnds
 *
 * 1750. 删除字符串两端相同字符后的最短长度
 * 给你一个只包含字符 'a'，'b' 和 'c' 的字符串 s ，你可以执行下面这个操作（5 个步骤）任意次：
 *
 * 选择字符串 s 一个 非空 的前缀，这个前缀的所有字符都相同。
 * 选择字符串 s 一个 非空 的后缀，这个后缀的所有字符都相同。
 * 前缀和后缀在字符串中任意位置都不能有交集。
 * 前缀和后缀包含的所有字符都要相同。
 * 同时删除前缀和后缀。
 * 请你返回对字符串 s 执行上面操作任意次以后（可能 0 次），能得到的 最短长度 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "ca"
 * 输出：2
 * 解释：你没法删除任何一个字符，所以字符串长度仍然保持不变。
 * 示例 2：
 *
 * 输入：s = "cabaabac"
 * 输出：0
 * 解释：最优操作序列为：
 * - 选择前缀 "c" 和后缀 "c" 并删除它们，得到 s = "abaaba" 。
 * - 选择前缀 "a" 和后缀 "a" 并删除它们，得到 s = "baab" 。
 * - 选择前缀 "b" 和后缀 "b" 并删除它们，得到 s = "aa" 。
 * - 选择前缀 "a" 和后缀 "a" 并删除它们，得到 s = "" 。
 * 示例 3：
 *
 * 输入：s = "aabccabba"
 * 输出：3
 * 解释：最优操作序列为：
 * - 选择前缀 "aa" 和后缀 "a" 并删除它们，得到 s = "bccabb" 。
 * - 选择前缀 "b" 和后缀 "bb" 并删除它们，得到 s = "cca" 。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 105
 * s 只包含字符 'a'，'b' 和 'c' 。
 * 通过次数14,154提交次数28,449
 */
public class _1750_MinimumLengthOfStringAfterDeletingSimilarEnds {

    /**
     * 双指针
     * 我们设 left 和 right 分别指向当前待删除字符串的起始位置与结束位置，然后按照规则进行删除，
     * 当前可以删除的条件必须满足如下:
     *
     * 只有字符串的长度大于 1 时我们才进行删除，因此可以进行删除的条件一定需要满足 left<right；
     * 只有存在字母相同的前缀与后缀我们才进行删除，因此可以进行删除的条件一定需要满足 s[left]=s[right]。
     *
     * 假设有可以进行删除的前缀和后缀时，则我们将所有字母相同的前缀与后缀全部删除，此时 left 需要向右移动，right 需要向左移动，
     * 并删除字符串中字母相同的前缀与后缀，直到无法删除为止。
     * 最终 left 指向删除后字符串的左起点，right 指向删除后字符串的右终点，剩余的字符串的长度则为 right−left+1。
     *
     * 需要注意的是，如果当 s 的长度大于 1 且 s 中的字符全部相等时，此时需要将 s 全部进行删除，则会出现 right=left−1。
     *
     * 总结算法 : 维护双指针，遍历字符串，左指针l从首到尾，右指针r从尾到首。
     * 仅当字符串的首尾字符相同，删除字符相同的前缀&后缀。
     * 当首尾字符不同时，算法终止，所求的字符串的最短长度就是 r-l+1。
     *
     * 提示 : 前后缀字符相同，字符数量可以不同。
     * 提示 : 思路涉及到尽可能多的删除，称为是贪心。本题也是模拟，因为思路容易想。
     *
     * 作者：hthanget
     * 链接：https://leetcode.cn/problems/minimum-length-of-string-after-deleting-similar-ends/solution/by-hthanget-nru0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int minimumLength(String s) {
        int n = s.length();
        // 初始双指针位置
        int left = 0, right = n - 1;
        // 当前可以删除的条件必须满足如下:
        while (left < right && s.charAt(left) == s.charAt(right)) {
            // 左指针后移匹配更长的相同字符前缀
            char c = s.charAt(left);
            // 取等是针对特殊条件：当 s 的长度大于 1 且 s 中的字符全部相等时，此时需要将 s 全部进行删除，会出现 right=left−1
            while (left <= right && s.charAt(left) == c) {
                left++;
            }
            // 右指针前移匹配更长的相同字符后缀
            while (left <= right && s.charAt(right) == c) {
                right--;
            }
        }
        // 剩余的字符串的长度则为 right−left+1
        return right - left + 1;
    }

    public static void main(String[] args) {
        _1750_MinimumLengthOfStringAfterDeletingSimilarEnds obj = new _1750_MinimumLengthOfStringAfterDeletingSimilarEnds();
        System.out.println(obj.minimumLength("cabaabac"));
    }
}
