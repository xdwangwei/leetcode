package com.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2020/4/5 18:53
 * 给定一个数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 *
 * candidates 中的每个数字在每个组合中只能使用一次。
 *
 * 说明：
 *
 * 所有数字（包括目标数）都是正整数。
 * 解集【不能包含重复的组合】。 
 * 示例 1:
 *
 * 输入: candidates = [10,1,2,7,6,1,5], target = 8,
 * 所求解集为:
 * [
 *   [1, 7],
 *   [1, 2, 5],
 *   [2, 6],
 *   [1, 1, 6]
 * ]
 * 示例 2:
 *
 * 输入: candidates = [2,5,2,1,2], target = 5,
 * 所求解集为:
 * [
 *   [1,2,2],
 *   [5]
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/combination-sum-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _40_CombinationsSum2 {

    public List<List<Integer>> solution1(int[] nums, int target) {
        List<List<Integer>> resList = new ArrayList<>();
        backTrack(nums, target, 0, new ArrayList<>(), resList);
        return resList;
    }

    /**
     * 回溯法，从目标开始 如[1,2,3,4,5] 8
     * 可以查看官方解答给出的搜索树：https://leetcode-cn.com/problems/combination-sum/solution/hui-su-suan-fa-jian-zhi-python-dai-ma-java-dai-m-2/
     * 从顶向下搜索，每一步都可以选择 2/3/5，分别再向下递进，如果能到底层 target = 0,说明当前路径有效，加入结果集
     * 否则 这个选择无效，回溯到上一步重新选择
     *
     * 未做剪枝，也就是每次都可以选择[2,3,5] 造成结果重复
     * [1,3,4] [3,1,4] [1,4,3] [4,1,3] [4,3,1] [3,5] [5,3]
     *
     * 剪枝：比如第一次选择1，向下进行选择就只选择 大于 自己的选择(3,4,5) 不取等于是因为元素不可重复
     *          第一次选择3，向下进行选择就只选择 大于 自己的选择(4，5),不要选择1,2，比如选择1我就可以把它归结到第一次选择1的搜索分支中去，也就是会重复
     *          第一次选择4，向下进行搜索就只选择 大于 自己的选择5，不要选择1，2，3，选则2就可以归入以2开始的分支，选择3就可以归入以3开始的分支
     *      也就是说，【当前选择时不要选择之前选择过的选择】，就不会出现 当前选择进行下去的路径可以归入之前某个分支
     *      更好一点的是，可以对已有选择排序，如1，2，3，4，5
     *      第一次可选1，2，3，4，5 分支多
     *      第二次可选2，3，4，5
     *      第三次可选3，4，5，这样分支会越来越少效率更高，而且也避免重复
     * @param nums
     * @param target
     * @param tempList
     * @param resList
     */


    /**
     * 带剪枝的回溯
     * 比如可选范围是[1,2,3,4,5]
     * begin 是本次选择时，从哪个位置开始选，
     * 即便数组无序 [3,2,1,4,5]，也可以避免之前提到的重复情况，无非就死不能达到分支数目越来越少的效果
     *
     * 需要注意的是原数组中可能有重复元素 [1,2,2,3] 甚至数组无序还重复 [1,3,2,1,3]
     * 即便这次从1开始，下次从2开始，第三次从3开始，但因为nums[2]==nums[3]，所以也是重复路径
     *
     * 所以先排序 变为 1，1，2，3，3，每次【从begin开始做选择】，并要排除掉之后重复的数组元素，排序过后重复元素是连续的
     * for(int i = begin; i < nums.length; i++){
     *     if(i > begin && nums[i] == nums[i-1]) continue; 小剪枝，过滤重复
     * }
     * @param nums
     * @param target
     * @param tempList
     * @param resList
     */
    private void backTrack(int[] nums, int target, int begin, ArrayList<Integer> tempList, List<List<Integer>> resList) {
        // 满足结束条件 此路可达
        if (target == 0){
            resList.add(new ArrayList<>(tempList));
            return;
        }
        // 进行选择，缩小可选范围，避免重复
        for (int i = begin; i < nums.length; i++){
            // 去除无用选择，此路不同，大剪枝
            if (target - nums[i]< 0) return;
            // 排除数组内重复元素干扰 小剪枝
            if (i > begin && nums[i] == nums[i - 1])
                continue;
            // 做选择，选择nums[i]
            tempList.add(nums[i]);
            // 试探下一步，因为元素不可以重复，所以某一个节点扩展的下层节点也不可以选自己选择的，因为用一个数不能重复出现
            // 但这个节点的兄弟节点向下扩展时不能选则这个节点已经选过的，避免重复路径
            backTrack(nums, target - nums[i], i + 1, tempList, resList);
            // 撤销选择，试探下一个选择
            tempList.remove(tempList.size() - 1);
        }
    }


    public static void main(String[] args) {
        new _40_CombinationsSum2().solution1(new int[]{2,3,5}, 8);
    }
}
