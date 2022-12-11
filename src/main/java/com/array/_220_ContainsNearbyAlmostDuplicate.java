package com.array;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author wangwei
 * 2020/3/31 20:08
 *
 * 220
 * 给定一个整数数组，判断数组中是否有两个不同的索引 i 和 j，
 * 使得nums [i] 和nums [j]的差的绝对值最大为 t，
 * 并且 i 和 j 之间的差的绝对值最大为 k。
 *
 * 示例1:
 *
 * 输入: nums = [1,2,3,1], k = 3, t = 0
 * 输出: true
 * 示例 2:
 *
 * 输入: nums = [1,0,1,1], k = 1, t = 2
 * 输出: true
 */
public class _220_ContainsNearbyAlmostDuplicate {

    /**
     * 方法：滑动窗口 + 有序哈希表
     * 暴力搜索：每个元素的扫描区间为[i-k,i+k] = 2k 范围
     * 实际上，每个元素nums[i] 只需要扫描 [i-k, i) 这个区间即可。
     * 因为 i 向前搜索 [i+1, i+k] 和 [i+1, i+k] 区间内的元素j向后扫描 [j-k, j) 是等价的
     *
     * 因此，nums[i] 只需要 扫描 [i-k, i) 范围内是否存在元素 abs(nums[?] - nums[i]) <= t
     * 要判断这个条件是否成立，我们不需要用所有元素和nums[i]进行比较，只需要找到区间内最接近x的两个元素即可。
     * 也就是 ceiling(x) 和 floor(x) ，只要二者有其一满足判断条件，我们就可返回true，否则，区间内其他元素都无法满足条件
     *
     * 因此，使用 treeSet 存储 大小为 k 的窗口内的元素；
     * 当 nums[i] 的扫描结束时，将 nums[i] 加入 窗口；如果 i >= k，此时需要从窗口内移除 nums[i-k]
     *
     * 时间复杂度：TreeSet 基于红黑树，查找和插入都是 O(logk) 复杂度。整体复杂度为O(nlogk)
     * 空间复杂度：O(k)
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/contains-duplicate-iii/solution/gong-shui-san-xie-yi-ti-shuang-jie-hua-d-dlnv/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @param t
     * @return
     */
    public boolean containsNearbyAlmostDuplicate3(int[] nums, int k, int t) {
        int n = nums.length;
        // 维护大小为k的窗口，对于 nums[i] 维护的是 nums[i-k, i) 的值
        TreeSet<Integer> window = new TreeSet<>();
        // 遍历nums[i]
        for (int i = 0; i < n; ++i) {
            // 在nums[i-k, i)内找到最接近x的两个值，floor和ceiling
            Integer floor = window.floor(nums[i]);
            // 只要有一个满足 与x的差 <= t即可
            // 注释类型转换避免溢出
            if (floor != null && (long) nums[i] - floor <= t) {
                return true;
            }
            Integer ceiling = window.ceiling(nums[i]);
            if (ceiling != null && (long) ceiling - nums[i] <= t) {
                return true;
            }
            // 窗口扩张，当前元素入窗口
            window.add(nums[i]);

            // 若 i 超过k，则从窗口内移除 nums[i-k]; 即左右边界同时移动，维护窗口大小
            if (i >= k) {
                window.remove(nums[i - k]);
            }
        }
        // 默认返回 false
        return false;
    }

    /**
     * 方法：滑动窗口 + 桶排序
     * 运用到了桶排序的思想，
     * 首先还是维护滑动窗口的思想，一个窗口(大小为k)一个窗口考虑。这样下标就是满足约束的，再考虑是否存在元素？与x的差值不超过t
     *
     * 我们把窗口内的数字存在不同编号的桶中。
     * 令桶的大小为 size=t+1（那么一个桶内的元素差绝对不会超过t，间隔一个桶的两个桶内元素差必然超过t），
     *
     * 对于当前数组元素x,我们只需要算出来 x 在哪个桶中。
     * 如果 x 所在桶已经有数字了,那就说明存在和 x 相差小于等于 t 的数。
     * 如果 x 所在桶没有数字，因为与 x 所在桶不相邻的桶中的数字与 x 的差一定大于 t，所以只需要考虑与 x 所在桶相邻的两个桶中的数字与 x的差是否小于等于 t。
     * 如果没有找到和 x 相差小于等于 t 的数, 那么窗口右移(并且将 x 加入窗（桶）),从将窗口中第一个数删除,
     * 接下来需要解决怎么求出一个数所在桶的编号。
     *
     * 桶下标计算方式：
     *     long getIdx(long u) {
     *         return u >= 0 ? u / size : ((u + 1) / size) - 1;
     *     }
     *
     * 【提问】
     * 「桶」什么数据结构存储呢？
     * 我们注意到，桶中其实最多就只会有一个数字（如果有两个数字，桶内这两个数一定满足相差小于等于 t ，直接结束）。
     * 所以我们完全可以用一个 map ，key 表示桶编号，value 表示桶中当前的数字。
     *
     * 同样的，为了防止溢出，所有数字我们都用成了 long。
     *
     * 【1】
     * 为什么 桶大小 size 需要对 t 进行 +1 操作？
     * 目的是为了确保差值小于等于 t 的数能够落到一个桶中。
     *
     * 举个 🌰，假设 [0,1,2,3]，t = 3，显然四个数都应该落在同一个桶。
     *
     * 如果不对 size 进行 t+1 操作的话，那么 [0,1,2] 和 [3] 会被落到不同的桶中，那么为了解决这种错误，我们需要对 t 进行 +1 作为 size 。
     *
     * 这样我们的数轴就能被分割成：
     *
     * 0 1 2 3 | 4 5 6 7 | 8 9 10 11 | 12 13 14 15 | …
     * 并且可以发现：同一桶内元素差值必然<=t；相邻桶间元素差值可能<=t；间隔超过1个桶的两个桶内元素差值必然超过t
     *
     * 总结一下，本质是因为差值为 t 两个数在数轴上相隔距离为 t + 1，它们需要被落到同一个桶中。
     * （其实就是1到5有5个数字，而5-1却等于4，数字个数对应桶大小，数字差值对应桶内元素差值，所有会有差1）
     *
     * 【2】
     * 当明确了 size 的大小之后，关于计算桶下标，对于正数部分我们则有 idx = nums[i] / size。
     *
     * 如何理解负数部分的逻辑？((u + 1) / size) - 1;
     * 由于我们处理正数的时候，处理了数值 0，因此我们负数部分是从 -1 开始的。
     *
     * 还是我们上述 🌰，此时我们有 t = 3 和 size = t + 1 = 4。
     *
     * 考虑 [-4,-3,-2,-1] 的情况，它们应该落在一个桶中。
     *
     * 如果直接复用 idx = nums[i] / size 的话，[-4] 和 [-3,-2,-1] 会被分到不同的桶中。
     *
     * 根本原因是我们处理整数的时候，已经分掉了数值 0。
     *
     * 这时候我们需要先对 nums[i] 进行 +1 操作（即将负数部分在数轴上进行整体右移），即得到 (nums[i] + 1) / size。
     *
     * 这样一来负数部分与正数部分一样，可以被正常分割了。
     *
     * 但由于 0 号桶已经被使用了，我们还需要在此基础上进行 -1，相当于将负数部分的桶下标（idx）往左移，即得到 ((nums[i] + 1) / size) - 1。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/contains-duplicate-iii/solution/gong-shui-san-xie-yi-ti-shuang-jie-hua-d-dlnv/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
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
