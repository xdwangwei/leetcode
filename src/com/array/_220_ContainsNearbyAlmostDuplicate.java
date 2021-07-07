package com.array;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author wangwei
 * 2020/3/31 20:08
 *
 * 给定一个整数数组，判断数组中是否有两个不同的索引 i 和 j，
 * 使得 nums [i] 和 nums [j] 的差的绝对值最大为 t，
 * 并且 i 和 j 之间的差的绝对值最大为 k。
 *
 * 示例 1:
 *
 * 输入: nums = [1,2,3,1], k = 3, t = 0
 * 输出: true
 * 示例 2:
 *
 * 输入: nums = [1,0,1,1], k = 1, t = 2
 * 输出: true
 */
public class _220_ContainsNearbyAlmostDuplicate {

    // hash表思想参考 _219_ContainsDuplicatePlus
    // 最后一个测试用例会超时
    public boolean solution(int[] nums, int t, int k){
        if (nums == null || nums.length  < 2)
            return false;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++){
            if (isExist(set, nums[i], t))
                return true;
            set.add(nums[i]);
            if (set.size() > k)
                set.remove(nums[i - k]);
        }
        return false;
    }

    /**
     * 集合中是否存在某个元素和目标元素的差的绝对值小于等于maxDis
     * 缺点：需要遍历集合中全部元素
     * @param set
     * @param item
     * @param maxDis
     * @return
     */
    private boolean isExist(Set<Integer> set, int item, int maxDis){
        for (Integer s : set) {
            if (Math.abs((long)s - item) <= maxDis)
                return true;
        }
        return false;
    }

    /**
     * 改进方法一中每次判断都要遍历集合全部元素，采用自平衡二叉树，借助其ceiling和floor方法
     * 注意ceiling()和floor()找不到时返回null值
     * 对于每个数组元素x，遍历集合
     * 在 set 上查找大于等于x的最小的数，如果s - x <= t则返回 true
     * 在 set 上查找小于等于x的最大的数，如果x - g <= t则返回 true
     * 在 set 中插入x
     * 如果树的大小超过了k, 则移除最早加入树的那个数。
     *
     * 换种思路，也就死说，在set中寻找 是否存在落在区间 x - t, x + t 内的数
     * 可以采用 c = ceiling(x - t), c 是集合内 大于等于 x - t 的最小的那个，如果c不超过x + t，就符合需求
     * 也可采用 f = floor(x + t), f 是集合内 小于等于 x + t 的最大的那个， 如果f不小于x - t， 则符合需求
     *
     * 若考虑最大整数，最小负数及其他溢出情况，以long形式保存即可
     *
     * @param nums
     * @param t
     * @param k
     * @return
     */
    public boolean solution2(int[] nums, int t, int k){
        if (nums == null || nums.length  < 2)
            return false;
        // 自平衡二叉树
        TreeSet<Integer> set = new TreeSet<>();
        Integer ceil, floor;
        for (int i = 0; i < nums.length; i++){
            // 从集合中找出 大于nums[i]的全部元素中的最小值
            // 对于 [-1,2147483647] 可能出现减法溢出
            ceil = set.ceiling(nums[i]);
            if (ceil != null && ceil - nums[i] <= t)
                return true;

            // 从集合中找出 小于nums[i]的全部元素中的最大值
            // 对于 [-1,2147483647] 可能出现减法溢出
            floor = set.floor(nums[i]);
            if (floor != null && nums[i] - floor <= t)
                return true;

            set.add(nums[i]);
            if (set.size() > k)
                set.remove(nums[i - k]);
        }
        return false;
    }

    /**
     * 改进二的另一种解法
     *
     * 换种思路，也就死说，在set中寻找 是否存在落在区间 x - t, x + t 内的数
     * 可以采用 c = ceiling(x - t), c 是集合内 大于等于 x - t 的最小的那个，如果c不超过x + t，就符合需求
     * 也可采用 f = floor(x + t), f 是集合内 小于等于 x + t 的最大的那个， 如果f不小于x - t， 则符合需求
     * @param nums
     * @param t
     * @param k
     * @return
     */
    public boolean solution3(int[] nums, int t, int k){
        if (nums == null || nums.length  < 2)
            return false;
        // 自平衡二叉树
        TreeSet<Long> set = new TreeSet<>();
        Long ceil;
        for (int i = 0; i < nums.length; i++){
            // 从集合中找出 大于nums[i] - x的全部元素中的最小值
            ceil = set.ceiling((long)nums[i] - t);
            // 它必须存在并且不能超出 x + t
            if (ceil != null && ceil < (long)nums[i] + t)
                return true;

            set.add((long)nums[i]);
            if (set.size() > k)
                set.remove((long)nums[i - k]);
        }
        return false;
    }

    /**
     * 方法四：
     * 运用到了桶排序的思想，
     * 首先还是维护滑动窗口的思想，一个窗口(大小为k)一个窗口考虑。
     * 不同之处在于，我们把窗口内的数字存在不同编号的桶中。
     * 设计一些桶，让他们分别包含区间 ..., [0,t], [t+1, 2t+1], ......
     * 每个桶内存的数字范围是 t + 1 个数
     * 这样做的好处是，桶内任意两个数之间的差一定是小于等于 t 的。
     *
     * 对于当前数组元素x,我们只需要算出来 x 在哪个桶中。
     * 如果 x 所在桶已经有数字了,那就说明存在和 x 相差小于等于 t 的数。
     * 如果 x 所在桶没有数字，因为与 x 所在桶不相邻的桶中的数字与 x 的差一定大于 t，所以只需要考虑与 x 所在桶相邻的两个桶中的数字与 x的差是否小于等于 t。
     * 如果没有找到和 x 相差小于等于 t 的数, 那么窗口右移(并且将 x 加入窗（桶）),从将窗口中第一个数删除,
     * 接下来需要解决怎么求出一个数所在桶的编号。
     *
     * 「桶」什么数据结构存储呢？
     * 我们注意到，桶中其实最多就只会有一个数字（如果有两个数字，桶内这两个数一定满足相差小于等于 t ，直接结束）。
     * 所以我们完全可以用一个 map ，key 表示桶编号，value 表示桶中当前的数字。
     *
     * 同样的，为了防止溢出，所有数字我们都用成了 long。
     *
     */
    public boolean solution4(int[] nums, int k, int t){
        if (nums == null || nums.length  < 2)
            return false;
        // 桶，滑动窗口
        HashMap<Long, Long> map = new HashMap<>();
        long bucketId;
        // 桶内元素之差一定不超过t，桶大小应为 t + 1，先排除 t = -1，t < 0全排除
        long bucketSize = t + 1;
        for (int i = 0; i < nums.length; i++){
            // 当前元素应放在哪个桶里
            bucketId = getBucketId(nums[i], bucketSize);
            // 这个桶里已有元素
            if (map.containsKey(bucketId))
                return true;
            // 前一个桶里存在元素，比较两者之差
            if (map.containsKey(bucketId - 1) && nums[i] - map.get(bucketId - 1) <= t)
                return true;
            // 后一个桶里存在元素，比较两者之差
            if (map.containsKey(bucketId + 1) && map.get(bucketId + 1) - nums[i] <= t)
                return true;

            // 不相邻的桶中元素必不满足相差绝对值小于等于t
            map.put(bucketId, (long)nums[i]);
            if (i >= k) // 窗口已超过k，去掉第一个元素（第一个元素所在的桶）
                map.remove(getBucketId(nums[i - k], bucketSize));
        }
        return false;
    }

    /**
     * 当前元素应该放入第几个桶(从0开始)
     * @param value
     * @param bucketSize
     * @return
     */
    private long getBucketId(long value, long bucketSize){
        // In Java, `-3 / 5 = 0` and but we need `-3 / 5 = -1`.
        // 这个 value 为什么要加 1
        // 若桶大小为11， 0号桶 0-10，1号桶 11-21，-1号桶应该存放 -11 - -1
        // 如果后面不减1 ， -10 - -1之间的数 / 11 = 0， 都会放入0号桶，我们需要其放入 -1号桶
        // 对于-11，若不加1， -11 / 11 = -1， -1 -1 = -2 ，-11会放入-2号桶
        if (value < 0)
            return (value + 1) / bucketSize - 1;
        else
            return value / bucketSize;
    }

    public static void main(String[] args) {
        new _220_ContainsNearbyAlmostDuplicate().solution4(new int[]{1,2,3,1}, 3,0);
    }
}
