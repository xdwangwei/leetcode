package com.offerassult;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/12/11 9:55
 * @description: _057_BothValueAndIndexDifferenceWithGivenRange
 *
 * 剑指 Offer II 057. 值和下标之差都在给定的范围内
 * 给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
 *
 * 如果存在则返回 true，不存在返回 false。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3,1], k = 3, t = 0
 * 输出：true
 * 示例 2：
 *
 * 输入：nums = [1,0,1,1], k = 1, t = 2
 * 输出：true
 * 示例 3：
 *
 * 输入：nums = [1,5,9,1,5,9], k = 2, t = 3
 * 输出：false
 *
 *
 * 提示：
 *
 * 0 <= nums.length <= 2 * 104
 * -231 <= nums[i] <= 231 - 1
 * 0 <= k <= 104
 * 0 <= t <= 231 - 1
 *
 *
 * 注意：本题与主站 220 题相同： https://leetcode-cn.com/problems/contains-duplicate-iii/
 *
 * 通过次数15,894提交次数45,179
 */
public class _057_BothValueAndIndexDifferenceWithGivenRange {

    /**
     * 方法一暴力搜索：1499ms，有时候过有时候不过
     * 对于nums[i]，与在 nums[i-k, i+k] 范围内的元素 num[j] 进行比较，如果 值差的绝对值 <= k，返回 true
     * 注意下标不要越界
     * 注意 j 不能 等于 i
     * 注意数据转long放置int溢出
     * @param nums
     * @param k
     * @param t
     * @return
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        int n = nums.length;
        // 对于每个nums[i]
        for (int i = 0; i < n; i++) {
            // 先满足下标差约束。在 nums[i-k, i+k] 范围内的元素 num[j] 进行比较，
            for (int j = Math.max(0, i - k); j != i && j < Math.min(n, i + k + 1); ++j) {
                // 再满足值的约束，如果 值差的绝对值 <= k，返回 true
                if (Math.abs((long)nums[i] - nums[j]) <= t) {
                    return true;
                }
            }
        }
        // 默认，返回false
        return false;
    }

    /**
     * 暴力搜索改进 ，击败 96%
     * 方法一大多数情况下会超时，是因为每个元素都要考虑 [i-k, i+k] 范围内元素
     * 考虑通过排序来实现只对一半范围[i+1,i+k]进行搜索
     * 将原数组元素 nums[i] 包装成 <nums[i], i> 即 (值，索引) 形式的键值对，再按照 值 进行排序
     * 那么 对于 新的元素 item[i]，只需要向后遍历，遇到 item[j][0] - item[i][0] > t 时退出即可
     *      对于 item[j][0] - nums[i][0] <= t 时，判断 abs(item[j][1] - item[i][1] <= k) 即可
     * @param nums
     * @param k
     * @param t
     * @return
     */
    public boolean containsNearbyAlmostDuplicate2(int[] nums, int k, int t) {
        List<int[]> list = new ArrayList<>();
        // 包装原数组元素为(值，索引) 形式的键值对，用 int[2] 实现
        for (int i = 0; i < nums.length; i++) {
            list.add(new int[]{nums[i], i});
        }
        // 按照值排序
        list.sort(Comparator.comparingInt(o -> o[0]));
        // 对于 新的元素 item[i]，只需要向后遍历，
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); ++j) {
                // 遇到 item[j][0] - item[i][0] > t 时不用继续遍历
                if ((long)list.get(j)[0] - list.get(i)[0] > t) {
                    break;
                }
                // 当item[j][0] - nums[i][0] <= t 时，判断 abs(item[j][1] - item[i][1] <= k)
                if (Math.abs(list.get(j)[1] - list.get(i)[1]) <= k) {
                    return true;
                }
            }
        }
        // 默认返回false
        return false;
    }


    /**
     * 方法三：滑动窗口 + 有序哈希表
     * 在方法二中，为了将每个元素的扫描区间从[i-k,i+k] 缩减为 [i+1,k]，我们借助了排序
     * 实际上，我们不需要这个过程
     * 每个元素nums[i] 只需要扫描 [i-k, i) 这个区间即可。
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
     * 方法四：滑动窗口 + 桶排序
     * 上述解法无法做到线性的原因是：我们需要在大小为 k 的滑动窗口所在的「有序集合」中找到与 u 接近的数。
     *
     * 和之前一样，窗口仍然维护nums[i] 左边 k 个数字，这样下标就是满足约束的
     * 再考虑是否存在元素？与x的差值不超过t
     * 对于窗口内k个元素
     * 令桶的大小为 size=t+1（那么一个桶内的元素差绝对不会超过t，间隔一个桶的两个桶内元素差必然超过t），
     * 对于nums[i] = u，计算其所在桶编号：
     *      如果该桶内已有元素，说明窗口内必然存在 [u−t,u+t] 范围的数字，返回 true
     *      如果该桶内不存在元素，则检查相邻两个桶内是否存在元素，如果存在，判断是否满足 [u−t,u+t] ，如有 返回 true
     *      放置当前元素入桶内，若i>=k，则需要从窗口内移除nums[i-k]，即删除nums[i-k]所在桶
     *
     * 桶下标计算方式：
     *     long getIdx(long u) {
     *         return u >= 0 ? u / size : ((u + 1) / size) - 1;
     *     }
     *
     * 【提问】
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
     */
    long size;
    public boolean containsNearbyAlmostDuplicate4(int[] nums, int k, int t) {
        int n = nums.length;
        Map<Long, Long> map = new HashMap<>();
        // 桶大小
        size = t + 1L;
        for (int i = 0; i < n; i++) {
            long u = nums[i] * 1L;
            // 找到其所在桶
            long idx = getIdx(u);
            // 桶已存在，桶内已经元素，说明前面必然有 [u - t, u + t] 范围的数字
            if (map.containsKey(idx)) return true;
            // 检查相邻的桶是否存在，若存在，判断桶内数字与自身的差值是否 <= t
            // 间隔超过1的桶内元素差必然超过t，不用考虑
            long l = idx - 1, r = idx + 1;
            if (map.containsKey(l) && u - map.get(l) <= t) return true;
            if (map.containsKey(r) && map.get(r) - u <= t) return true;
            // 建立目标桶，放入当前元素
            map.put(idx, u);
            // 移除下标范围不在 [max(0, i - k), i) 内的桶
            // 维护窗口大小k
            if (i >= k) map.remove(getIdx(nums[i - k] * 1L));
        }
        return false;
    }
    // 计算元素所在桶下标
    long getIdx(long u) {
        return u >= 0 ? u / size : ((u + 1) / size) - 1;
    }


    public static void main(String[] args) {
        _057_BothValueAndIndexDifferenceWithGivenRange obj = new _057_BothValueAndIndexDifferenceWithGivenRange();
        System.out.println(obj.containsNearbyAlmostDuplicate(new int[]{0, 10, 22, 15, 0, 5, 22, 12, 1, 5}, 3, 3));
        System.out.println(obj.containsNearbyAlmostDuplicate2(new int[]{1, 5, 9, 1, 5, 9}, 2, 3));
    }
}
