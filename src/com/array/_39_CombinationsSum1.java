package com.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2020/4/5 18:53
 * <p>
 * 给定一个无重复元素的数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 * <p>
 * candidates 中的数字【可以无限制重复被选取。】
 * <p>
 * 说明：
 * <p>
 * 所有数字（包括 target）都是正整数。
 * 解集不能包含重复的组合。 
 * 示例 1:
 * <p>
 * 输入: candidates = [2,3,6,7], target = 7,
 * 所求解集为:
 * [
 * [7],
 * [2,2,3]
 * ]
 * <p>
 * 示例 2:
 * <p>
 * 输入: candidates = [2,3,5], target = 8,
 * 所求解集为:
 * [
 *   [2,2,2,2],
 *   [2,3,3],
 *   [3,5]
 * ]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/combination-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _39_CombinationsSum1 {

    public List<List<Integer>> solution1(int[] nums, int target) {
        List<List<Integer>> resList = new ArrayList<>();
        backTrack(nums, target, 0, new ArrayList<>(), resList);
        return resList;
    }

    /**
     * 回溯法，从目标开始 如[2,3,5] 8
     * 可以查看官方解答给出的搜索树：https://leetcode-cn.com/problems/combination-sum/solution/hui-su-suan-fa-jian-zhi-python-dai-ma-java-dai-m-2/
     * 从顶向下搜索，每一步都可以选择 2/3/5，分别再向下递进，如果能到底层 target = 0,说明当前路径有效，加入结果集
     * 否则 这个选择无效，回溯到上一步重新选择
     *
     * 未做剪枝，也就是每次都可以选择[2,3,5] 造成结果重复
     * [2,2,2,2] [2,3,3] [3,2,3] [3,3,2] [3,5] [5,3]
     *
     * 剪枝：比如第一次选择2，向下进行选择就只选择 大于等于自己的选择(2,3,5)等于是因为元素可重复
     *          第一次选择3，向下进行选择就只选择 大于等于自己的选择(3,5),不要选择2，选择2我就可以把它归结到第一次选择2的搜索分支中去，也就是会重复
     *          第一次选择5，向下进行搜索就只选择 大于等于自己的选择5，不要选择2，3，选则2就可以归入以2开始的分支，选择3就可以归入以3开始的分支
     *      也就是说，【当前选择时不要选择之前选择过的选择】，就不会出现 当前选择进行下去的路径可以归入之前某个分支
     *      更好的办法是，可以对已有选择排序，如2，3，5
     *      第一次可选2，3，5 分支多
     *      第二次可选3，5
     *      第三次可选5，这样分支会越来越少效率更高，而且也避免重复
     * @param nums
     * @param target
     * @param tempList
     * @param resList
     */
    private void backTrackWithoutCut(int[] nums, int target, ArrayList<Integer> tempList, List<List<Integer>> resList) {
        // 终止条件，此路不同
        if (target < 0) return;
        // 此路可达
        if (target == 0){
            resList.add(new ArrayList<>(tempList));
            return;
        }
        // 进行选择
        for (int i = 0; i < nums.length; i++){
            // 做选择，选择nums[i]
            tempList.add(nums[i]);
            // 试探下一步
            backTrackWithoutCut(nums, target - nums[i], tempList, resList);
            // 撤销选择，试探下一个选择
            tempList.remove(tempList.size() - 1);
        }
    }

    /**
     * 带剪枝的回溯
     * 比如可选范围是[2,3,5]
     * begin 是本次选择时，从哪个位置开始选，
     * 即便数组无序 [3,2,5]，也可以避免之前提到的重复情况，无非就死不能达到分支数目越来越少的效果
     * @param nums
     * @param target
     * @param tempList
     * @param resList
     */
    private void backTrack(int[] nums, int target, int begin, ArrayList<Integer> tempList, List<List<Integer>> resList) {
        // 终止条件，此路不同
        if (target < 0) return;
        // 此路可达
        if (target == 0){
            resList.add(new ArrayList<>(tempList));
            return;
        }
        // 进行选择，缩小可选范围，避免重复
        for (int i = begin; i < nums.length; i++){
            // 做选择，选择nums[i]
            tempList.add(nums[i]);
            // 试探下一步，因为元素可以重复，所以某一个节点扩展的下层节点还可以选自己
            // 但这个节点的兄弟节点向下扩展时不能选则这个节点已经选过的，避免重复
            backTrack(nums, target - nums[i], i, tempList, resList);
            // 撤销选择，试探下一个选择
            tempList.remove(tempList.size() - 1);
        }
    }


    public static void main(String[] args) {
        new _39_CombinationsSum1().solution1(new int[]{2,3,5}, 8);
    }
}
