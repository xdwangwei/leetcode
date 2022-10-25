package com.offerassult;

import com.common.DLinkedList;
import com.common.DNode;

import java.util.HashMap;

/**
 * @author wangwei
 * @date 2022/10/25 12:43
 * @description: _031_LRU
 *
 * 剑指 Offer II 031. 最近最少使用缓存
 * 运用所掌握的数据结构，设计和实现一个  LRU (Least Recently Used，最近最少使用) 缓存机制 。
 *
 * 实现 LRUCache 类：
 *
 * LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
 * int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
 * void put(int key, int value) 如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字-值」。当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。
 *
 *
 * 示例：
 *
 * 输入
 * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
 * 输出
 * [null, null, null, 1, null, -1, null, -1, 3, 4]
 *
 * 解释
 * LRUCache lRUCache = new LRUCache(2);
 * lRUCache.put(1, 1); // 缓存是 {1=1}
 * lRUCache.put(2, 2); // 缓存是 {1=1, 2=2}
 * lRUCache.get(1);    // 返回 1
 * lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
 * lRUCache.get(2);    // 返回 -1 (未找到)
 * lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
 * lRUCache.get(1);    // 返回 -1 (未找到)
 * lRUCache.get(3);    // 返回 3
 * lRUCache.get(4);    // 返回 4
 *
 *
 * 提示：
 *
 * 1 <= capacity <= 3000
 * 0 <= key <= 10000
 * 0 <= value <= 105
 * 最多调用 2 * 105 次 get 和 put
 *
 *
 * 进阶：是否可以在 O(1) 时间复杂度内完成这两种操作？
 *
 *
 *
 * 注意：本题与主站 146 题相同：https://leetcode-cn.com/problems/lru-cache/
 */
public class _031_LRU {

    static class LRUCache {

        private int capacity;
        // 这是common包中自己实现的双向链表
        private DLinkedList cache;
        // hash表为了解决解决链表查询速度慢，hash表保存这个节点，就可以在链表中直接找到
        private HashMap<Integer, DNode> map;

        public LRUCache(int capacity) {
            map = new HashMap<>();
            cache = new DLinkedList();
            this.capacity = capacity;
        }

        /**
         * O(1)时间复杂度
         *
         * @param key
         * @return
         */
        public int get(int key) {
            // 先从hash表中查找
            DNode DNode = map.get(key);
            // 若不存在
            if (DNode == null) return -1;
            // 存在则返回键值，并把这个节点设置为最近访问
            makeRecently(DNode);
            return DNode.val;
        }

        /**
         * O(1)时间复杂度
         *
         * @param key
         * @return
         */
        public void put(int key, int value) {
            // 先从hash表中查找
            DNode DNode = map.get(key);
            // 若存在，则先删除
            if (DNode != null) {
                deleteKey(DNode);
            }
            // 再添加为最近访问
            addRecently(new DNode(key, value));

            // 如果容量超出初始容量，则删除最近最久未使用节点
            if (map.size() > this.capacity)
                removeLeastRecently();
        }

        // 将某个键提为最近使用的键，也就是放在链表最后面，这个键必须存在
        private void makeRecently(DNode DNode) {
            // 先存链表中删除
            cache.remove(DNode);
            // 再插入到末尾
            cache.addLast(DNode);
        }

        // 添加一个最近使用的节点，也就是在链表末尾添加一个新节点
        private void addRecently(DNode DNode) {
            // 插入链表末尾
            cache.addLast(DNode);
            // 同时要在hash表中保存
            map.put(DNode.key, DNode);
        }

        // 删除某个键值对
        private void deleteKey(DNode DNode) {
            // 从链表中删除
            cache.remove(DNode);
            // 同时要从map中删除
            map.remove(DNode.key);
        }

        // 删除最长时间未使用的键值对，也就是删除链表第一个节点
        private void removeLeastRecently() {
            // 从链表中删除第一个节点
            DNode DNode = cache.removeFirst();
            // 同时要从map中删除
            map.remove(DNode.key);
        }
    }

    public static void main(String[] args) {
        LRUCache obj = new LRUCache(2);
        obj.put(1, 1);
        obj.put(2, 2);
        System.out.println(obj.get(1));
        obj.put(3, 3);
        System.out.println(obj.get(2));
        obj.put(4, 4);
        System.out.println(obj.get(1));
        System.out.println(obj.get(3));
        System.out.println(obj.get(4));
    }
}
