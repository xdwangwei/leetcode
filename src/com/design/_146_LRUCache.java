package com.design;

import com.common.DLinkedList;
import com.common.DNode;

import java.util.HashMap;

/**
 * @author wangwei
 * 2020/7/31 11:28
 *
 * 不借助java默认实现的 LinkedHashMap
 * 使用自己实现的双向链表和HashMap实现LRU算法
 */
public class _146_LRUCache {

    private int capacity;
    private DLinkedList cache;
    // hash表为了解决解决链表查询速度慢，hash表保存这个节点，就可以在链表中直接找到
    private HashMap<Integer, DNode> map;

    public _146_LRUCache(int capacity) {
        map = new HashMap<>();
        cache = new DLinkedList();
        this.capacity = capacity;
    }

    /**
     * O(1)时间复杂度
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

        // 如果容量超出初始容量，则删除最近未使用节点
        if (map.size() > this.capacity)
            removeLeastRecently();
    }

    // 将某个键提为最近使用的键，也就是放在链表最后面，这个键必须存在
    private void makeRecently(DNode DNode){
        // 先存链表中删除
        cache.remove(DNode);
        // 再插入到末尾
        cache.addLast(DNode);
    }

    // 添加一个最近使用的节点，也就是在链表末尾添加一个新节点
    private void addRecently(DNode DNode){
        // 插入链表末尾
        cache.addLast(DNode);
        // 同时要在hash表中保存
        map.put(DNode.key, DNode);
    }

    // 删除某个键值对
    private void deleteKey(DNode DNode){
        // 从链表中删除
        cache.remove(DNode);
        // 同时要从map中删除
        map.remove(DNode.key);
    }

    // 删除最长时间未使用的键值对，也就是删除链表第一个节点
    private void removeLeastRecently(){
        // 从链表中删除第一个节点
        DNode DNode = cache.removeFirst();
        // 同时要从map中删除
        map.remove(DNode.key);
    }
}
