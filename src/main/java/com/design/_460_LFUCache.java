package com.design;

import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @author wangwei
 * 2021/11/24 17:46
 *
 * 请你为 最不经常使用（LFU）缓存算法设计并实现数据结构。
 *
 * 实现 LFUCache 类：
 *
 * LFUCache(int capacity) - 用数据结构的容量 capacity 初始化对象
 * int get(int key) - 如果键存在于缓存中，则获取键的值，否则返回 -1。
 * void put(int key, int value) - 如果键已存在，则变更其值；如果键不存在，请插入键值对。当缓存达到其容量时，则应该在插入新项之前，使最不经常使用的项无效。在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，应该去除 最近最久未使用 的键。
 * 注意「项的使用次数」就是自插入该项以来对其调用 get 和 put 函数的次数之和。使用次数会在对应项被移除后置为 0 。
 *
 * 为了确定最不常使用的键，可以为缓存中的每个键维护一个 使用计数器 。使用计数最小的键是最久未使用的键。
 *
 * 当一个键首次插入到缓存中时，它的使用计数器被设置为 1 (由于 put 操作)。对缓存中的键执行 get 或 put 操作，使用计数器的值将会递增。
 *
 *  
 *
 * 示例：
 *
 * 输入：
 * ["LFUCache", "put", "put", "get", "put", "get", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [3], [4, 4], [1], [3], [4]]
 * 输出：
 * [null, null, null, 1, null, -1, 3, null, -1, 3, 4]
 *
 * 解释：
 * // cnt(x) = 键 x 的使用计数
 * // cache=[] 将显示最后一次使用的顺序（最左边的元素是最近的）
 * LFUCache lFUCache = new LFUCache(2);
 * lFUCache.put(1, 1);   // cache=[1,_], cnt(1)=1
 * lFUCache.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
 * lFUCache.get(1);      // 返回 1
 *                       // cache=[1,2], cnt(2)=1, cnt(1)=2
 * lFUCache.put(3, 3);   // 去除键 2 ，因为 cnt(2)=1 ，使用计数最小
 *                       // cache=[3,1], cnt(3)=1, cnt(1)=2
 * lFUCache.get(2);      // 返回 -1（未找到）
 * lFUCache.get(3);      // 返回 3
 *                       // cache=[3,1], cnt(3)=2, cnt(1)=2
 * lFUCache.put(4, 4);   // 去除键 1 ，1 和 3 的 cnt 相同，但 1 最久未使用
 *                       // cache=[4,3], cnt(4)=1, cnt(3)=2
 * lFUCache.get(1);      // 返回 -1（未找到）
 * lFUCache.get(3);      // 返回 3
 *                       // cache=[3,4], cnt(4)=1, cnt(3)=3
 * lFUCache.get(4);      // 返回 4
 *                       // cache=[3,4], cnt(4)=2, cnt(3)=3
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/lfu-cache
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _460_LFUCache {

    /**
     * 从实现难度上来说，LFU 算法的难度大于 LRU 算法，因为 LRU 算法相当于把数据按照时间排序，这个需求借助链表很自然就能实现，
     * 你一直从链表头部加入元素的话，越靠近头部的元素就是新的数据，越靠近尾部的元素就是旧的数据，
     * 我们进行缓存淘汰的时候只要简单地将头部的元素淘汰掉就行了。
     *
     * 而 LFU 算法相当于是淘汰访问频次最低的数据，如果访问频次最低的数据有多条，需要淘汰最旧的数据。
     * 把数据按照访问频次进行排序，而且频次还会不断变化，这可不容易实现。
     *
     * 根据 LFU 算法的逻辑，我们先列举出算法执行过程中的几个显而易见的事实：
     *
     * 1、调用get(key)方法时，要返回该key对应的val。
     * 2、只要用get或者put方法访问一次某个key，该key的freq就要加一。
     * 3、如果在容量满了的时候进行插入，则需要将freq最小的key删除，如果最小的freq对应多个key，则删除其中最旧的那一个。
     *
     * 好的，我们希望能够在 O(1) 的时间内解决这些需求，可以使用基本数据结构来逐个击破：
     *
     * 1、使用一个HashMap存储key到val的映射，就可以快速计算get(key)。
     * HashMap<Integer, Integer> keyToVal;
     * 2、使用一个HashMap存储key到freq的映射，就可以快速操作key对应的freq。
     * HashMap<Integer, Integer> keyToFreq;
     * 3、这个需求应该是 LFU 算法的核心，所以我们分开说。
     *      * HashMap<Integer, LinkedHashSet<Integer>> freqToKeys;
     *      * int minFreq = 0;
     * 3.1、首先，肯定是需要freq到key的映射，用来找到最小的freq对应的那些keys。
     * 3.2、将freq最小的key删除，那你就得快速得到当前所有key最小的freq是多少。想要时间复杂度 O(1) 的话，肯定不能遍历，那就用一个变量minFreq来记录当前最小的freq吧。
     * 3.3、可能有多个key拥有相同的freq，所以 freq对key是一对多的关系，即一个freq对应一个key的列表。
     * 3.4、希望freq对应的key的列表是存在【时序】的，便于快速查找并删除最旧的key。
     * 3.5、希望能够快速删除key列表中的任何一个key，因为如果频次为freq的某个key被访问，那么它的频次就会变成freq+1，
     *          就应该从freq对应的key列表中删除，加到freq+1对应的key的列表中。
     *      【Stack】 和 【LinkedList】 都能满足时序要求，也能满足存多个值，但是无法快速随机删除
     *      而 【LinkedHashSet】，它满足我们 3.3，3.4，3.5 这几个要求。
     * LinkedHashSet顾名思义，是链表和哈希集合的结合体。
     *  链表不能快速访问链表节点，但是插入元素具有时序；
     *  哈希集合中的元素无序但是可以对元素进行快速的访问和删除。
     *  它俩结合起来就兼具了哈希集合和链表的特性，既可以在 O(1) 时间内访问或删除其中的元素，又可以保持插入的时序，高效实现 3.5 这个需求。
     */
    static class LFUCache {

        // key 到 val 的映射，我们后文称为 KV 表
        HashMap<Integer, Integer> keyToVal;
        // key 到 freq 的映射，我们后文称为 KF 表
        HashMap<Integer, Integer> keyToFreq;
        // freq 到 key 列表的映射，我们后文称为 FK 表
        HashMap<Integer, LinkedHashSet<Integer>> freqToKeys;
        // 记录最小的频次
        int minFreq;
        // 记录 LFU 缓存的最大容量
        int cap;

        public LFUCache(int capacity) {
            keyToVal = new HashMap<>();
            keyToFreq = new HashMap<>();
            freqToKeys = new HashMap<>();
            this.cap = capacity;
            this.minFreq = 0;
        }

        public int get(int key) {
            if (!keyToVal.containsKey(key)) {
                return -1;
            }
            // 增加 key 对应的 freq
            increaseFreq(key);
            return keyToVal.get(key);
        }

        public void put(int key, int val) {
            if (this.cap <= 0) return;

            /* 若 key 已存在，修改对应的 val 即可 */
            if (keyToVal.containsKey(key)) {
                keyToVal.put(key, val);
                // key 对应的 freq 加一
                increaseFreq(key);
                return;
            }

            /* key 不存在，需要插入 */
            /* 容量已满的话需要淘汰一个 freq 最小的 key */
            if (this.cap <= keyToVal.size()) {
                removeMinFreqKey();
            }

            /* 插入 key 和 val，对应的 freq 为 1 */
            // 插入 KV 表
            keyToVal.put(key, val);
            // 插入 KF 表
            keyToFreq.put(key, 1);
            // 插入 FK 表
            freqToKeys.putIfAbsent(1, new LinkedHashSet<>());
            freqToKeys.get(1).add(key);
            // 插入新 key 后最小的 freq 肯定是 1
            this.minFreq = 1;
        }

        /**
         * 更新某个key的freq肯定会涉及FK表和KF表，所以我们分别更新这两个表就行了。
         *
         * 和之前类似，当FK表中freq对应的列表被删空后，需要删除FK表中freq这个映射。如果这个freq恰好是minFreq，说明minFreq变量需要更新。
         *
         * ！！！【能不能快速找到当前的minFreq呢？这里是可以的，因为我们刚才把key的freq加了 1 嘛，所以minFreq也加 1 就行了。】
         * @param key
         */
        private void increaseFreq(int key) {
            int freq = keyToFreq.get(key);
            /* 更新 KF 表 */
            keyToFreq.put(key, freq + 1);
            /* 更新 FK 表 */
            // 将 key 从 freq 对应的列表中删除
            freqToKeys.get(freq).remove(key);
            // 将 key 加入 freq + 1 对应的列表中
            freqToKeys.putIfAbsent(freq + 1, new LinkedHashSet<>());
            freqToKeys.get(freq + 1).add(key);
            // 如果 freq 对应的列表空了，移除这个 freq
            if (freqToKeys.get(freq).isEmpty()) {
                freqToKeys.remove(freq);
                // 如果这个 freq 恰好是 minFreq，更新 minFreq
                if (freq == this.minFreq) {
                    this.minFreq++;
                }
            }
        }

        /**
         * 删除某个键key肯定是要同时修改三个映射表的，借助minFreq参数可以从FK表中找到freq最小的keyList，根据时序，其中第一个元素就是要被淘汰的deletedKey，操作三个映射表删除这个key即可。
         *
         * 但是有个细节问题，如果keyList中只有一个元素，那么删除之后minFreq对应的key列表就为空了，也就是minFreq变量需要被更新。
         * 如何计算当前的minFreq是多少呢？实际上没办法快速计算minFreq，只能线性遍历FK表或者KF表来计算，这样肯定不能保证 O(1) 的时间复杂度。
         *
         * 但是，其实这里没必要更新minFreq变量，因为你想想removeMinFreqKey这个函数是在什么时候调用？在put方法中插入新key时可能调用。
         * 而你回头看put的代码，插入新key时一定会把minFreq更新成 1，所以说即便这里minFreq变了，我们也不需要管它。
         */
        private void removeMinFreqKey() {
            // freq 最小的 key 列表
            LinkedHashSet<Integer> keyList = freqToKeys.get(this.minFreq);
            // 其中最先被插入的那个 key 就是该被淘汰的 key
            int deletedKey = keyList.iterator().next();
            /* 更新 FK 表 */
            keyList.remove(deletedKey);
            if (keyList.isEmpty()) {
                freqToKeys.remove(this.minFreq);
                // 问：这里需要更新 minFreq 的值吗？不需要
            }
            /* 更新 KV 表 */
            keyToVal.remove(deletedKey);
            /* 更新 KF 表 */
            keyToFreq.remove(deletedKey);
        }

    }

    public static void main(String[] args) {
        _460_LFUCache.LFUCache lFUCache = new _460_LFUCache.LFUCache(2);
        lFUCache.put(1, 1);   // cache=[1,_], cnt(1)=1
        lFUCache.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
        lFUCache.get(1);      // 返回 1
        // cache=[1,2], cnt(2)=1, cnt(1)=2
        lFUCache.put(3, 3);   // 去除键 2 ，因为 cnt(2)=1 ，使用计数最小
        // cache=[3,1], cnt(3)=1, cnt(1)=2
        lFUCache.get(2);      // 返回 -1（未找到）
        lFUCache.get(3);      // 返回 3
        // cache=[3,1], cnt(3)=2, cnt(1)=2
        lFUCache.put(4, 4);   // 去除键 1 ，1 和 3 的 cnt 相同，但 1 最久未使用
        // cache=[4,3], cnt(4)=1, cnt(3)=2
        lFUCache.get(1);      // 返回 -1（未找到）
        lFUCache.get(3);      // 返回 3
        // cache=[3,4], cnt(4)=1, cnt(3)=3
        lFUCache.get(4);      // 返回 4
        // cache=[3,4], cnt(4)=2, cnt(3)=3
    }

}
