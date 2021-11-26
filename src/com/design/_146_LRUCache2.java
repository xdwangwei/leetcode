package com.design;

import java.util.LinkedHashMap;

/**
 * @author wangwei
 * 2021/11/24 10:43
 * <p>
 * 直接使用 LinkedHashMap
 *
 * 大多数情况下，只要不涉及线程安全问题，Map基本都可以使用HashMap，不过HashMap有一个问题，就是迭代HashMap的顺序并不是HashMap放置的顺序，
 * 也就是[无序]。HashMap的这一缺点往往会带来困扰，因为有些场景，我们期待一个有序的Map。
 *
 * 这个时候，LinkedHashMap就闪亮登场了，它虽然增加了时间和空间上的开销，但是通过维护一个运行于所有条目的双向链表，
 * LinkedHashMap保证了元素迭代的顺序。该迭代顺序可以是插入顺序或者是访问顺序。
 *
 * LinkedHash在内部维护了一个header指针(双向链表节点)，用于保存第一个插入的节点，插入元素时，除过 继承原来map的操作（往table插入）。
 * 还维护了内部的双向链表，因此通过 keySet.iterator 迭代器访问时实际上拿到的是 双向链表的迭代器，能保证第一次next得到的就是第一个加入链表的节点
 * 也就是说 如果 先移除，再put。新的元素就会保存在链表尾部，也就达到了 LRU的效果。相当于一次 makeRecent
 * 而hashmap。每次put同样值，hash一样，保存在同样位置，iterator得到的也是hash表的iter。因此无需
 *
 * 另外，linkedHashMap 如果在创建时 将 accessOrder‘设置为true。就能实现，每次访问一个已有元素，会将这个元素重新移到链表尾部，也就是makeRecent
 * 这个内部是通过 get方法内部 afterAccess方法实现的，前提是 accessorder 这个值是 true。默认是 false。所以 get 没有makeRecent的逻辑，我们要自己添加
 * 只需要先 remove，再 put 就能实现
 */
public class _146_LRUCache2 {
    int cap;
    LinkedHashMap<Integer, Integer> cache = new LinkedHashMap<>();
    // HashMap<Integer, Integer> cache = new HashMap<>();

    public _146_LRUCache2(int capacity) {
        this.cap = capacity;
    }

    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }
        // 将 key 变为最近使用
        makeRecently(key);
        return cache.get(key);
    }

    public void put(int key, int val) {
        if (cache.containsKey(key)) {
            // 修改 key 的值
            cache.put(key, val);
            // 将 key 变为最近使用
            makeRecently(key);
            return;
        }

        if (cache.size() >= this.cap) {
            // 链表头部就是最久未使用的 key
            int oldestKey = cache.keySet().iterator().next();
            cache.remove(oldestKey);
        }
        // 将新的 key 添加链表尾部
        cache.put(key, val);
    }

    private void makeRecently(int key) {
        int val = cache.get(key);
        // 删除 key，重新插入到队尾
        cache.remove(key);
        cache.put(key, val);
    }

    public static void main(String[] args) {
        _146_LRUCache2 cache = new _146_LRUCache2(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        cache.put(3, 3);    // 该操作会使得关键字 2 作废
        System.out.println(cache.get(2)); // -1
        cache.put(4, 4);    // 该操作会使得关键字 1 作废
        System.out.println(cache.get(1)); // -1
        System.out.println(cache.get(3)); // -1
        System.out.println(cache.get(4)); // -1
    }
}
