package com.hot100;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/4/28 16:03
 * @description: _448_FindAllNumbersDisappearedInAnArray
 *
 * 448. 找到所有数组中消失的数字
 * 给你一个含 n 个整数的数组 nums ，其中 nums[i] 在区间 [1, n] 内。请你找出所有在 [1, n] 范围内但没有出现在 nums 中的数字，并以数组的形式返回结果。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [4,3,2,7,8,2,3,1]
 * 输出：[5,6]
 * 示例 2：
 *
 * 输入：nums = [1,1]
 * 输出：[2]
 *
 *
 * 提示：
 *
 * n == nums.length
 * 1 <= n <= 105
 * 1 <= nums[i] <= n
 * 进阶：你能在不使用额外空间且时间复杂度为 O(n) 的情况下解决这个问题吗? 你可以假定返回的数组不算在额外空间内。
 */
public class _448_FindAllNumbersDisappearedInAnArray {

    /**
     *
     * 原地hash
     *
     * 如果长度为n的数组整好存储的是 1-n，那么 i 位置 的数字应该等于 i + 1
     * 也就是 数组元素 x ，应该 存储在 x - 1 位置
     *
     * 我们可以用一个哈希表记录数组 \textit{nums}nums 中的数字，由于数字范围均在 [1,n][1,n] 中，记录数字后我们再利用哈希表检查 [1,n][1,n] 中的每一个数是否出现，从而找到缺失的数字。
     *
     *
     * 由于 nums 的数字范围均在 [1,n] 中，我们可以利用这一范围之外的数字，来表达「是否存在」的含义。
     *
     * 具体来说，遍历 nums，每遇到一个数 x，就让 nums[x−1] 增加 n。
     * 由于 nums 中所有数均在 [1,n] 中，如果每个位置都被映射到了(1-n每个数字都存在)增加以后，这些数必然大于 n。
     * 最后我们遍历 nums，若 nums[i] 未大于 n(这个位置未被映射到过)，就说明没有遇到过数 i+1。这样我们就找到了缺失的数字。
     *
     * 注意，当我们遍历到某个位置时，其中的数可能已经被增加过，因此需要对 n 取模来还原出它本来的值。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/find-all-numbers-disappeared-in-an-array/solution/zhao-dao-suo-you-shu-zu-zhong-xiao-shi-d-mabl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public List<Integer> findDisappearedNumbers(int[] nums) {
        int n = nums.length;
        // 每个元素x应该存储在i-1位置，这样元素 1-n 正好 存储到 0 - n-1
        for (int num : nums) {
            // 其中的数可能已经被增加过，因此需要对 n 取模来还原出它本来的值。
            int x = (num - 1) % n;
            // 给它映射的位置加n
            nums[x] += n;
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            // 这个位置没有过映射
            if (nums[i] <= n) {
                // 即不存在数字i+1
                res.add(i + 1);
            }
        }
        return res;
    }
}
