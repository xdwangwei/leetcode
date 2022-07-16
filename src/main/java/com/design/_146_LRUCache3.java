package com.design;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangwei
 * 2020/7/31 7:47
 *
 * 运用你所掌握的数据结构，设计和实现一个 LRU (最近最少使用) 缓存机制。它应该支持以下操作： 获取数据 get 和 写入数据 put 。
 *
 * 获取数据 get(key) - 如果关键字 (key) 存在于缓存中，则获取关键字的值（总是正数），否则返回 -1。
 * 写入数据 put(key, value) - 如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字/值」。当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。
 *
 *
 *
 * 进阶:
 *
 * 你是否可以在O(1) 时间复杂度内完成这两种操作？
 *
 *
 * 示例:
 *
 * LRUCache cache = new LRUCache( 2 缓存容量 )
 * cache.put(1,1);
 * cache.put(2,2);
 * cache.get(1);       // 返回  1
 * cache.put(3,3);    // 该操作会使得关键字 2 作废
 * cache.get(2);       // 返回 -1 (未找到)
 * cache.put(4,4);    // 该操作会使得关键字 1 作废
 * cache.get(1);       // 返回 -1 (未找到)
 * cache.get(3);       // 返回  3
 * cache.get(4);       // 返回  4
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/lru-cache
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */

/**
 * 使用java默认实现的 LinkedHashMap
 */
public class _146_LRUCache3 extends LinkedHashMap<Integer, Integer> {

    /**
     * 要让 put 和 get 方法的时间复杂度为 O(1)，我们可以总结出 cache 这个数据结构必要的条件：
     * 1、显然 cache 中的元素必须有时序，以区分最近使用的和久未使用的数据，当容量满了之后要删除最久未使用的那个元素腾位置。
     * 2、我们要在 cache 中快速找某个 key 是否已存在并得到对应的 val；
     * 3、每次访问 cache 中的某个 key，需要将这个元素变为最近使用的，也就是说 cache 要支持在任意位置快速插入和删除元素。
     * 那么，什么数据结构同时符合上述条件呢？哈希表查找快，但是数据无固定顺序；链表有顺序之分，插入删除快，但是查找慢。所以结合一下，形成一种新的数据结构：哈希链表 LinkedHashMap。
     * LRU 缓存算法的核心数据结构就是哈希链表，双向链表和哈希表的结合体。
     *
     * 1、如果我们每次默认从链表尾部添加元素，那么显然越靠尾部的元素就是最近使用的，越靠头部的元素就是最久未使用的。
     * 2、对于某一个 key，我们可以通过哈希表快速定位到链表中的节点，从而取得对应 val。
     * 3、链表显然是支持在任意位置快速插入和删除的，改改指针就行。
     * 只不过传统的链表无法按照索引快速访问某一个位置的元素，
     * 而这里借助哈希表，可以通过 key 快速映射到任意一个链表节点，然后进行插入和删除。
     */

    /**
     * LinkedHashMap = HashMap + DoublyLinkedList
     * HashMap 大家都清楚，底层是 数组 + 红黑树 + 链表 （不清楚也没有关系），同时其是无序的，
     * 而 LinkedHashMap 刚好就比 HashMap 多这一个功能，就是其提供 【有序】，
     * 并且，LinkedHashMap的有序可以按两种顺序排列，一种是按照【插入】的顺序，一种是按照【读取】的顺序
     *
     * LinkedHashMap 的构造函数
     * 其主要是两个构造方法，一个是继承 HashMap ，
     * 一个是可以选择 accessOrder 的值(默认 false，代表按照插入顺序排序)来确定是按插入顺序还是读取顺序排序。
     *
     * 默认是按照插入顺序，需要在构造方法中传参指定 accessOrder = true 调整为 按 读取顺序
     *
     * （这个题目的示例就是告诉我们要按照【读取】的顺序进行排序），
     *
     * 而其内部是靠 建立一个【双向链表】 来维护这个顺序的，
     * 【在每次插入、删除后，都会自动调用一个函数来进行 双向链表的顺序维护 】，
     *
     * 准确的来说，是有三个函数来做这件事，这三个函数都统称为 回调函数 ，
     * 这三个函数分别是：可以看源码
     *
     * 1. void afterNodeAccess(Node<K,V> p) { } 这个方法内部会将被访问的节点移到列表尾部
     *      其作用就是在访问元素之后，将该元素放到双向链表的尾巴处(所以这个函数只有在按照【读取】的顺序的时候才会执行)，
     * 2. void afterNodeRemoval(Node<K,V> p) { }
     *      其作用就是在map删除元素之后，将元素从双向链表中删除，很优美的方式在双向链表中删除节点！
     * 3. void afterNodeInsertion(boolean evict) { }
     *      在插入新元素之后，需要回调函数判断是否需要移除一直不用的某些元素，一般是移除第一个元素
     *      这个函数内部有个判断条件 if (evict && (first = head) != null && removeEldestEntry(first)) {
     *      而 removeEldestEntry() 默认直接返回false。所以【我们需要】重写此方法，就得继承LinkedHashMap
     *
     * 作者：jeromememory
     * 链接：https://leetcode-cn.com/problems/lru-cache/solution/yuan-yu-linkedhashmapyuan-ma-by-jeromememory/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    private int capacity;

    /**
     * 构造函数，指定初始容量
     * @param capacity
     */
    public _146_LRUCache3(final int capacity) {
        // 指定 accessOrder 为 true，让内部的双向链表按照节点访问顺序维护
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    /**
     * O(1)时间复杂度
     * @param key
     * @return
     */
    public int get(int key) {
        /**
         * 内部按照访问顺序维护，执行get或getOrDefault方法会自动执行 afterNodeAccess
         * 这个方法内部会将被访问的节点移到列表尾部
         */
        return super.getOrDefault(key, -1);
    }

    /**
     * O(1)时间复杂度
     * @param key
     * @return
     */
    public void put(int key, int value) {
        /**
         * put方法会将新节点插入双向链表尾部
         * 如果这个key已存在，会覆盖旧址，并执行 afterNodeAccess，将其移至双向链表尾部
         * 此方法执行后会自动执行 afterNodeInsertion，方法内部会去判断是否需要需要删除第一个节点，
         * 判断条件由 removeEldestEntry 决定，默认直接返回false。
         * 我们重写他，若链表已有节点个数 > 初始化时指定容量，就让他返回true，删除头结点，实现LRU
         */
        super.put(key, value);
    }

    /**
     * put方法执行后会自动执行 afterNodeInsertion，方法内部会去判断是否需要需要删除第一个节点，
     * 判断条件由 removeEldestEntry 决定，默认直接返回false。
     * 重写他，若链表已有节点个数 > 初始化时指定容量，就让他返回true，删除头结点，实现LRU
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > this.capacity;
    }

}
