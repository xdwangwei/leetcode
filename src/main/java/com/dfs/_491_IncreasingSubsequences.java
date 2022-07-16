package com.dfs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * 2022/4/15 12:37
 * <p>
 * 给你一个整数数组 nums ，找出并返回所有该数组中不同的递增子序列，递增子序列中 至少有两个元素 。你可以按 任意顺序 返回答案。
 * <p>
 * 数组中可能含有重复元素，如出现两个整数相等，也可以视作递增序列的一种特殊情况。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [4,6,7,7]
 * 输出：[[4,6],[4,6,7],[4,6,7,7],[4,7],[4,7,7],[6,7],[6,7,7],[7,7]]
 * 示例 2：
 * <p>
 * 输入：nums = [4,4,3,2,1]
 * 输出：[[4,4]]
 *  
 * <p>
 * 提示：
 * <p>
 * 1 <= nums.length <= 15
 * -100 <= nums[i] <= 100
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/increasing-subsequences
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _491_IncreasingSubsequences {

    // 保存所有结果并去重
    Set<List<Integer>> set;

    /**
     * dfs，或者说回溯，找到所有递增子序列，长度>=2均可，使用set去重，最后返回list
     * 具体实现：
     * 从nums[]每个位置开始，找寻从当前位置往后的递增子序列，对于nums[i]来说，下一个元素nums[j]要>=nums[i]，每次可以任取一个这样的nums[j]拼接在nums[i]后面
     * 相当于 一个广度遍历
     * 枚举nums[]每个位置，对于任意一个开始位置，枚举这个位置后面所有大于等于它本身的元素拼接在它后面，重复递进这个过程
     * 为什么要枚举每个开始位置？因为我可以直接放弃nums[i]从j开始啊，如果只考虑从i开始，那肯定漏掉从后面开始的序列了。
     * <p>
     * 所以也可以这样总结：
     * 对于每一个元素 nums[i]: 可以放弃它，直接进入下一个dfs(i+1, nums, temp)
     * 也可以选择它，但是选择它的前提是 它得大于等于上一个被选择的元素，也就是 if (nums[i] >= temp.last) dfs(i + 1, nums, temp)
     * 并且此时需要回溯，因为对于temp.last来说，后面这些可选元素是并列的，所以必须回溯
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> findSubsequences(int[] nums) {
        set = new HashSet<>();
        // 枚举每一个开始位置
        for (int i = 0; i < nums.length; ++i) {
            // 找到从当前位置开始的全部递增子序列
            dfs(nums, i, new ArrayList<>());
        }
        // set --> list
        return set.stream().collect(Collectors.toList());
    }

    /**
     * 找到从 nums[i] 开始的全部递增子序列
     *
     * @param nums
     * @param i
     * @param temp 当前递增子序列
     */
    private void dfs(int[] nums, int i, List<Integer> temp) {
        // 先加入自己
        temp.add(nums[i]);
        // 长度大于2的子序列都需要保存
        if (temp.size() >= 2) {
            set.add(new ArrayList<>(temp));
        }
        // nums[i] 后面可以接这些 nums[j] (nums[j] >= nums[i])
        // 这些可选元素之间应该是并列关系
        for (int j = i + 1; j < nums.length; ++j) {
            if (nums[j] >= nums[i]) {
                // 要保证下一次dfs之前，撤销选择，也就是 dfs函数内部最后面 temp.removeLast()
                // 具体在哪里回溯与函数的写法有关
                dfs(nums, j, temp);
            }
        }
        // 撤销选择，保证返回到上一步的for循环时的并列关系
        temp.remove(temp.size() - 1);
    }

    /**
     * 所以也可以这样总结：、
     * 对于每一个元素 nums[i]: 可以放弃它，直接进入下一个dfs(i+1, nums, temp)
     * 也可以选择它，但是选择它的前提是 它得大于等于上一个被选择的元素，也就是 if (nums[i] >= temp.last) dfs(i + 1, nums, temp)
     * 并且此时需要回溯，因为对于temp.last来说，后面这些可选元素是并列的，所以必须回溯
     * 并且如果当前元素和上一个被选元素相同，那么不同考虑 不选它，因为选择上个元素时已经做过这个选择了
     * <p>
     * 这种情况下就保证了每一次的选择都是合法的，并且不会重复
     * 因为如果有两个相同的元素，我们会考虑这样四种情况：
     *
     * 前者被选择，后者被选择
     * 前者被选择，后者不被选择
     * 前者不被选择，后者被选择
     * 前者不被选择，后者不被选择
     * 其中第二种情况和第三种情况其实是等价的，我们这样限制之后，舍弃了第二种，保留了第三种，于是达到了去重的目的。
     *
     *
     * 注意这种写法的递归出口
     *
     * @param nums
     * @return
     */
    List<List<Integer>> res;

    public List<List<Integer>> findSubsequences2(int[] nums) {
        res = new ArrayList<>();
        // last初始化为一个负数
        dfs2(0, -10000, nums, new ArrayList<>());
        return res;
    }

    /**
     * 对于每一个元素 nums[i]: 可以放弃它，直接进入下一个dfs(i+1, nums, temp)
     * 也可以选择它，但是选择它的前提是 它得大于等于上一个被选择的元素，也就是 if (nums[i] >= temp.last) dfs(i + 1, nums, temp)
     *      并且此时需要回溯，因为对于temp.last来说，后面这些可选元素是并列的，所以必须回溯
     *      并且如果当前元素和上一个被选元素相同，那么不同考虑 不选它，因为选择上个元素时已经做过这个选择了
     *
     *  相当于得到一颗二叉树
     * @param i 当前待处理的数字
     * @param last 上一个被选择的数
     * @param nums
     * @param temp 当前子序列
     */
    private void dfs2(int i, int last, int[] nums, List<Integer> temp) {
        // 每次dfs走到头时，判断temp，避免重复执行（做选择1次，进入深层递归，撤销回来又成立1次）
        // 每个元素两个选择最终一定走完所有元素才开始从最底层往最高层回溯，所以这里不会漏掉情况
        // 比如 4567 到头，此时把 4567 加进去，不会 出现 67 这种遗漏，因为当不选择4和5时最终还是要走到最后一个位置，所以没问题
        // 就是说，从树根开始的每条路径一定都会到叶子节点层，所以我在这里判断没问题
        if (i == nums.length) {
            // 这里必须要有外围的if条件，否则 做选择 和 撤销选择来回 会两次导致 这个if成立
            if (temp.size() >= 2) {
                res.add(new ArrayList<>(temp));
            }
        }
        // 当前元素有两种选择
        // 当它 >= 上一个被选元素时，可以拼接，但是需要回溯
        if (nums[i] >= last) {
            temp.add(nums[i]);
            // nums[i] 成了新的 last
            dfs2(i + 1, nums[i], nums, temp);
            temp.remove(temp.size() - 1);
        }
        // 当它和last相等时，就不用考虑 不选它的情况了，
        if (nums[i] != last) {
            // last未改变
            dfs2(i + 1, last, nums, temp);
        }
    }

    public static void main(String[] args) {
        _491_IncreasingSubsequences obj = new _491_IncreasingSubsequences();
        List<List<Integer>> res = obj.findSubsequences(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 1, 1, 1, 1});
        for (List<Integer> re : res) {
            System.out.println(re);
        }
    }
}
