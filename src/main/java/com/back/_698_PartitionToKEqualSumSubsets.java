package com.back;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author wangwei
 * 2022/2/24 11:16
 *
 * 698. 划分为k个相等的子集
 * 给定一个整数数组  nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
 */
public class _698_PartitionToKEqualSumSubsets {

    /**
     * 法一：逐个遍历 nums 数组元素，将每个元素安排到 其中一个子集（桶）中
     * 因为有多种分配方法，所以 回溯
     * @param nums
     * @param k
     * @return
     */
    public boolean canPartitionKSubsets(int[] nums, int k) {
        // 排除一些基本情况
        if (k > nums.length) return false;

        // 划分为一个子集，那就是 nums 本身，
        if (k == 1) {
            return true;
        }
        // 排序。从大到小安排nums元素，让回溯的剪枝条件尽可能多的被触发，以减少时间消耗
        Arrays.sort(nums);

        int sum = Arrays.stream(nums).sum();
        // 若整个数组和无法被均分，则返回 false
        if (sum % k != 0) return false;
        // 均分后每个子集和
        int target = sum / k;
        // 如果数组最小的元素都比target大，那无法划分
        if (nums[0] > target) {
            return false;
        }
        // k 个桶保存k个子集的元素和
        int[] bucket = new int[k];

        // 逐个遍历nums元素，为其寻找合适的桶。
        // nums以排序，先安排大元素，再安排小元素
        return backTrack(nums, nums.length - 1, bucket, target);
    }

    /**
     * 为nums每个元素找到合适的桶
     * @param nums
     * @param index 当前要安排nums[index]这个元素
     * @param bucket 所有桶
     * @param target 每个桶的目标和
     * @return
     */
    private boolean backTrack(int[] nums, int index, int[] bucket, int target) {
        // nums元素已全部被安排完毕
        if (index == -1) {
            // 如果此时每个桶的元素和都达到目标，则返回true，否则返回false
            for (int i = 0; i < bucket.length; ++i) {
                if (bucket[i] != target) {
                    return false;
                }
            }
            return true;
        }
        // 当前安排nums[index]这个元素
        // 逐个尝试将其放入每个桶中
        for (int j = 0; j < bucket.length; ++j) {
            // 剪枝条件：放入这个桶会导致目标和超出，放弃，尝试下一个桶
            // 将nums中元素从大到小进行安排有利于此条件更多的触发，减少时间小号
            if (bucket[j] + nums[index] > target) {
                continue;
            }
            // 做选择：尝试将其放入当前桶
            bucket[j] += nums[index];
            // 判断下一次循环是否能完成，下一个要被安排的元素是[index-1]，nums是有序的，我们从后往前安排
            if (backTrack(nums, index - 1, bucket, target)) {
                return true;
            }
            // 撤销选择：从当前桶中移除
            bucket[j] -= nums[index];

            // 说明 某个 nums元素 放入某个bucket后，一直未找到合适的其他元素凑成target，导致被回溯到清空了这个桶，说明这个元素无法和其他元素组成和为target的子集，那么nums就无法被划分到k个桶，返回false
            if (bucket[j] == 0) {
                return false;
            }
        }
        // 默认，false
        return false;
    }

    /**
     * 法二：逐个处理桶，对于每个桶，从nums中挑选元素凑成target，然后处理下一个桶
     * 因为有多种分配方法，所以 回溯
     *
     * 处理第二个桶时，需要考虑 nums 元素 是否已被第一个桶使用，所以应该 使用 boolean[nums.length]记录每个元素的使用状态
     * 额外考虑：假设target=5，
     *      桶1 中 放 1 和 4，桶2 中 放 2 和 3，然后以此类推，对后面的元素进行穷举，凑出若干个和为 5 的桶（子集）。
     *      但问题是，如果最后发现无法凑出和为 target 的 k 个子集，算法会怎么做？
     *          回溯算法会回溯到第一个桶，重新开始穷举，现在它知道第一个桶里装 1, 4 是不可行的，它会尝试把 2, 3 装到第一个桶里：
     *          现在第一个桶装满了，就开始装第二个桶，算法会装入 1, 4：
     *      好，到这里你应该看出来问题了，这种情况其实和之前的那种情况是一样的。也就是说，到这里你其实已经知道不需要再穷举了，必然凑不出来和为 target 的 k 个子集。
     *      但我们的算法还是会傻乎乎地继续穷举，因为在她看来，第一个桶和第二个桶里面装的元素不一样，那这就是两种不一样的情况呀。
     * 那么我们怎么让算法的智商提高，识别出这种情况，避免冗余计算呢？
     *      你注意这两种情况的 used 数组肯定长得一样，所以 used 数组可以认为是回溯过程中的「状态」。
     *      所以，我们可以用一个 memo 备忘录，在【装满一个桶时】记录当前 used 的状态，如果当前 used 的状态是曾经出现过的，那就不用再继续穷举，从而起到剪枝避免冗余计算的作用。
     *   【注意！】这里，是 在 装满 一个桶时 去判断此时的 used 是否 已出现过，此时才涉及重复情况，其他中间状态就是在凑某个桶的过程，这个过程本身就是一个搜索过程，不应该和备忘录扯上关系
     *
     * 备忘录选择 hashmap，但是 将 boolean数组 used 转为 可作为 map key的类型需要额外耗费时间空间，可能也会导致耗时
     *      这里考虑使用 位图。
     *      题目限制 len(nums) <= 16，所以使用 一个 int 类型变量 used的二进制每一个位 来代表 nums中每个元素的选取状态，最多16位
     *      这样 used 这个变量本身就可以 作为 map 的 key 使用了。
     *
     *
     * 同样，为了减少时间消耗，用 index 记录 此时使用到了nums第几个元素。当一个 bucket 满了之后，让 index=0，凑第二个桶时 就可以从第一个元素开始选了。
     * 其他时候都是在凑某个桶的过程，为了避免重复判断（虽然有used数组，但是if也要时间，我之间限制位置就可以更快）。我们每次从index开始选。
     * @param nums
     * @param k
     * @return
     */
    public boolean canPartitionKSubsets2(int[] nums, int k) {
        // 排除一些基本情况
        if (k > nums.length) return false;

        // 划分为一个子集，那就是 nums 本身，
        if (k == 1) {
            return true;
        }

        // 这种情况下可以不排序，不倒序, 反而会更快。

        // 排序。从大到小安排nums元素，之后倒序选择nums中元素，让回溯的剪枝条件尽可能多的被触发，以减少时间消耗
        Arrays.sort(nums);

        int sum = Arrays.stream(nums).sum();
        // 若整个数组和无法被均分，则返回 false
        if (sum % k != 0) return false;
        // 均分后每个子集和
        int target = sum / k;
        // 如果数组最小的元素都比target大，那无法划分
        if (nums[0] > target) {
            return false;
        }

        HashMap<Integer, Boolean> memo = new HashMap<>();
        // 逐个桶处理，需要填满k个桶。
        return backTrack2(nums, nums.length - 1, k, 0, target, 0, memo);
    }

    /**
     * 逐个处理桶，对于每个桶，从nums中挑选元素凑成target，然后处理下一个桶
     * @param nums
     * @param index 当前从nums哪个元素开始选择
     * @param k 还要处理几个桶，也可以理解为现在在处理那个桶
     * @param curSum 当前桶当前的元素和
     * @param target 桶的目标和
     * @param used   nums元素的选取状态
     * @param memo   备忘录。当某个桶填满时，判断此时的used是否重复出现
     * @return
     */
    private boolean backTrack2(int[] nums, int index, int k, int curSum, int target, int used, HashMap<Integer, Boolean> memo) {
        // 如果只剩下一个桶，代表其他桶都凑好，那么剩下的元素自然可以凑好。所以可以少一轮
        if (k == 1) {
            return true;
        }
        // 当前桶凑好了
        if (curSum == target) {
            // 去凑剩下的桶，是否能成功
            boolean res = backTrack2(nums, nums.length - 1, k - 1, 0, target, used, memo);
            // 记录当前的used状态下，最终的结果，避免之后重复
            // 【只有此时才需要更新备忘录】
            memo.put(used, res);
            return res;
        }
        // 此时的used已出现过，已处理过
        if (memo.containsKey(used)) {
            return memo.get(used);
        }
        // 接下来都是 为 k号桶处理
        // 从指示位置开始尝试将每个元素放入桶中
        for (int i = index; i >= 0; --i) {
            // 如果当前元素已用过
            if (((used >> i) & 1) == 1) {
                continue;
            }
            // 剪枝条件：放入这个桶会导致目标和超出，放弃，尝试下一个桶
            // 将nums中元素从大到小进行安排有利于此条件更多的触发，减少时间小号
            if (curSum + nums[i] > target) {
                continue;
            }
            // 做选择：尝试将其放入当前桶
            curSum += nums[i];
            // 更新used
            used |= 1 << i;
            // 判断下一次循环是否能完成，下一个要被安排的元素是[index-1]，nums是有序的，我们从后往前安排
            if (backTrack2(nums, index - 1, k, curSum, target, used, memo)) {
                return true;
            }
            // 撤销选择：从当前桶中移除
            curSum -= nums[i];
            // 更新used
            used ^= 1 << i;
        }
        // 默认，false
        return false;
    }
}
