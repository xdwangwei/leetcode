package com.design;

import java.util.*;

/**
 * @author wangwei
 * 2021/12/7 18:51
 * <p>
 * 实现RandomizedSet 类：
 * <p>
 * RandomizedSet() 初始化 RandomizedSet 对象
 * bool insert(int val) 当元素 val 不存在时，向集合中插入该项，并返回 true ；否则，返回 false 。
 * bool remove(int val) 当元素 val 存在时，从集合中移除该项，并返回 true ；否则，返回 false 。
 * int getRandom() 随机返回现有集合中的一项（测试用例保证调用此方法时集合中至少存在一个元素）。每个元素应该有 相同的概率 被返回。
 * 你必须实现类的所有函数，并满足每个函数的 平均 时间复杂度为 O(1) 。
 * <p>
 *  
 * <p>
 * 示例：
 * <p>
 * 输入
 * ["RandomizedSet", "insert", "remove", "insert", "getRandom", "remove", "insert", "getRandom"]
 * [[], [1], [2], [2], [], [1], [2], []]
 * 输出
 * [null, true, false, true, 2, true, false, 2]
 * <p>
 * 解释
 * RandomizedSet randomizedSet = new RandomizedSet();
 * randomizedSet.insert(1); // 向集合中插入 1 。返回 true 表示 1 被成功地插入。
 * randomizedSet.remove(2); // 返回 false ，表示集合中不存在 2 。
 * randomizedSet.insert(2); // 向集合中插入 2 。返回 true 。集合现在包含 [1,2] 。
 * randomizedSet.getRandom(); // getRandom 应随机返回 1 或 2 。
 * randomizedSet.remove(1); // 从集合中移除 1 ，返回 true 。集合现在包含 [2] 。
 * randomizedSet.insert(2); // 2 已在集合中，所以返回 false 。
 * randomizedSet.getRandom(); // 由于 2 是集合中唯一的数字，getRandom 总是返回 2
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/insert-delete-getrandom-o1
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _380_InsertDeleteGetRandomInO1 {

    /**
     * 我们需要在平均复杂度为 O(1) 实现以下操作：
     *
     * insert\remove\getRadom
     *
     * 对于 插入：
     *      数组、链表、hash表，都可以O(1)插入
     * 对于 随机返回一项(等概率)
     *      哈希表 x, 首先，它不具备返回随机返回元素API，其次，它里面的结构可不是连续的，而且还存在拉链等解决冲突的策略，所以没办法等概率随机
     *      对于数组：连续存储结构，能够通过 Random.ranInt(nums.length)得到，并且是等概率的
     *      对于链表，它也是连续的，也能做到等概率 _382_, 但是时间复杂度是O(N)
     *              int i = 0, res = 0;
     *              while (p != null) {
     *                  // 以 1/i 的概率选择当前节点(替换了之前的)
     *                  if (r.nextInt(++i) == 0) {
     *                      res = p.val;
     *                  }
     *                  p = p.next;
     *               }
     * 对于 删除：链表可以O(1)，但是定位到这个元素需要O(n)
     *           哈希表可以O(1)，但是 它无法 随机
     *          数组实际上不可以，但是我们可以这样：
     *              找到要删除的元素后，把它和最后一个元素调换位置，然后删除最后一个元素
     *              插入时也是从后面插入，这样就可以达到O(1)了
     *              为了保证获取到这个元素位置也是O(1)，需要一个 映射，保存每个元素到索引位置的映射
     *
     * 总结：
     *      要实现等概率随机，一定要用连续存储结构，链表是O(N)
     *      数组只在最后一个位置添加和删除，所以应该是一个动态数组，那么ArrayList就可以
     *          它支持随机获取某个位置的元素，也支持把某个位置设置成固定值，因为它底层就是数组，这两个操作数组都是可以完成的
     */
    class RandomizedSet {
        // 保存所有元素与它在数组中索引位置的映射关系
        HashMap<Integer, Integer> valToIndex;
        // 动态数组，只操作末尾
        List<Integer> nums;
        // 随机数生成器
        Random random = new Random();

        public RandomizedSet() {
            valToIndex = new HashMap<>();
            nums = new ArrayList<>();
        }

        public boolean insert(int val) {
            // 已存在
            if (valToIndex.containsKey(val)) {
                return false;
            }
            // 末尾插入
            nums.add(val);
            // 保存映射关系
            valToIndex.put(val, nums.size() - 1);
            return true;
        }

        public boolean remove(int val) {
            // 不存在
            if (!valToIndex.containsKey(val)) {
                return false;
            }
            // 这个元素的索引
            Integer index = valToIndex.get(val);
            // 数组最后一个元素
            Integer last = nums.get(nums.size() - 1);
            // 交换这两个元素，因为val要删除，所以只把 last 换到 index 位置就可以
            nums.set(index, last);
            // 删除最后一个元素
            valToIndex.put(last, index);
            // 更新这个元素和索引的映射
            nums.remove(nums.size() - 1);
            // 从映射表中删除 val的映射
            valToIndex.remove(val);
            return true;
        }

        public int getRandom() {
            // random.nextInt(N)  --> [0, N)
            return nums.get(random.nextInt(nums.size()));
        }
    }
}
