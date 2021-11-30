package com.binarysearch;

/**
 * @author wangwei
 * 2020/4/3 22:
 * <p>
 * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
 * <p>
 * 你的算法时间复杂度必须是 O(log n) 级别。
 * <p>
 * 如果数组中不存在目标值，返回 [-1, -1]。
 * <p>
 * 示例 1:
 * <p>
 * 输入: nums = [5,7,7,8,8,10], target = 8
 * 输出: [3,4]
 * 示例 2:
 * <p>
 * 输入: nums = [5,7,7,8,8,10], target = 6
 * 输出: [-1,-1]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * 【关于二分查找的细节，请参考】
 * 链接：https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array/solution/er-fen-cha-zhao-suan-fa-xi-jie-xiang-jie-by-labula/
 */
public class _34_FindTwoPositionForTargetInSortedArray {

    /**
     * 先二分查找找到目标元素，再向左向右扫描找到最多边和最右边的相等元素，但要考虑这个元素没有重复值的情况
     * 最坏情况下，{3,3,3,3,3,3,}，时间复杂度O(n)
     * @param nums
     * @param target
     * @return
     */
    public int[] solution1(int[] nums, int target) {
        int res[] = new int[]{-1, -1};
        if (nums == null || nums.length == 0) return res;
        if (nums.length == 1) return nums[0] == target ? new int[]{0, 0} : res;
        int low = 0, high = nums.length - 1, mid;
        while (low <= high) {
            mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                int L = mid, R = mid;
                // 找到最左边和target相等的元素
                while (R + 1 < nums.length && nums[R + 1] == target) R++;
                // 找到最友边和target相等的元素
                while (L - 1 >= 0 && nums[L - 1] == target) L--;
                // 若唯一，没有重复出现，则 L = R = mid未改变
                res[0] = L;
                res[1] = R;
                return res;
            } else if (nums[mid] < target)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return res;
    }

    /**
     * 改变是 left 参数的引入，它是一个 boolean 类型的变量，
     * 指示我们在遇到 target == nums[mid] 时应该做什么。
     * 如果 left 为 true ，那么我们缩小右边界，继续在更小的左半部分中找target
     * 考虑如果我们在下标为 i处遇到了 target ，最左边的 target一定不会出现在下标大于 i 的位置，
     * 所以我们永远不需要考虑右子区间。当求最右下标时，道理同样适用。
     *
     *
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array/solution/zai-pai-xu-shu-zu-zhong-cha-zhao-yuan-su-de-di-yi-/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 这种写法的技巧在于：
     * 1. 当向左查找时 left一定为true,此时若nums[mid] == target，会执行 high = mid
     * 而这种二分法的while退出条件为 low == high，也就是while退出时，low可能就是正确的索引(此时就是上面发生的的nums[mid]==target，high=mid)
     * 所以判断结果时如果low==num.length || nums[low] != target就是未找到，否则low就是结果
     * 2. 而当向右查找target时，left传入false,即便nums[mid] == target，执行的也是 low = mid + 1
     * 而退出条件是low==high，也就是说while退出时，low-1才有可能是结果
     * 所以判断结果如果low==0 || nums[low-1] != target就是没找到，否则low-1就是答案
     *
     * 或者就不要整这种骚操作，把找左元素和找右元素分开写成两个函数
     *
     * @param nums
     * @param target
     * @return
     */
    public static int binarySearch(int[] nums, int target, boolean left) {
        int low = 0, high = nums.length, mid;
        while (low < high) {
            mid = low + (high - low) / 2;
            // 中间值比目标值大，有边界左移
            // 若中间值等于target，但是传入的left为真，也就是要找到重复元素中最左边的target，此时区间也要左移
            // 注意 短路或 运算
            if (nums[mid] > target || (nums[mid] == target && left))
                high = mid;
            else
                low = mid + 1;
        }
        // 这种二分写法，{1，3，5}找1会返回0,找4会返回2，找6会返回3(数组长度)
        //因此，判断未找到的方式是，res == nums.length || nums[res] != target
        return low;
    }
    public int[] solution2(int[] nums, int target){
        int res[] = {-1,-1};
        // 在有重复值的有序nums中寻找最左侧的target
        int leftIndex = binarySearch(nums, target, true);
        // 寻找失败
        if (leftIndex == nums.length || nums[leftIndex] != target)
            return res;
        // 寻找成功
        res[0] = leftIndex;
        // 找到最右边那个target
        // 因为最左边的target已经找到，再找另一个target时只需按照正常二分查找去找即可
        // 此时找到的下标一定不会小于leftIndex，注意此处的-1
        // 本来向右查找也可以能不到结果，但向左查找时未找到已经返回[-1,-1]了，所以能执行下来，它肯定存在
        res[1] = binarySearch(nums, target, false) - 1;
        return res;
    }

    public static void main(String[] args) {
        // System.out.println(binarySearch(new int[]{1,3,5,7,8,9},5));;
        System.out.println(binarySearch(new int[]{1,3,5,7,8,9},3,true));;
        System.out.println(binarySearch(new int[]{1,3,5,7,8,9},1,true));;
        System.out.println(binarySearch(new int[]{1,3,7,7,8,9},7,true));;
        // System.out.println(binarySearch(new int[]{1,3,5,7,8,9},9));;
        // System.out.println(binarySearch(new int[]{1,3,5,7,8,9},-1));;
        // System.out.println(binarySearch(new int[]{1,3,5,7,8,9},10));;
        // System.out.println(binarySearch(new int[]{1,3,5,7,8,9},4,false));;
        System.out.println(binarySearch(new int[]{3,3,4,5,6},3,false));;
        System.out.println(binarySearch(new int[]{1,3,5,7,7,7},7,false));;
        System.out.println(binarySearch(new int[]{1,3,5,7,8,9},10,false));;
    }
}
