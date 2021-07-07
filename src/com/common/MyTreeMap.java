package com.common;

import java.util.*;

/**
 * @author wangwei
 * 2020/4/23 23:32
 *
 * 阉割版的TreeMap红黑树，只是把源码拿过来了
 * 主要是为了研究 put 源代码
 */
public class MyTreeMap<K, V> extends AbstractMap<K,V>{

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private transient MyTreeMap.Entry<K, V> root;

    /**
     * The comparator used to maintain order in this tree map, or
     * null if it uses the natural ordering of its keys.
     *
     * @serial
     */
    private final Comparator<? super K> comparator;

    /**
     * The number of entries in the tree
     */
    private transient int size = 0;

    /**
     * The number of structural modifications to the tree.
     */
    private transient int modCount = 0;

    /**
     * Constructs a new, empty tree map, using the natural ordering of its
     * keys.  All keys inserted into the map must implement the {@link
     * Comparable} interface.  Furthermore, all such keys must be
     * <em>mutually comparable</em>: {@code k1.compareTo(k2)} must not throw
     * a {@code ClassCastException} for any keys {@code k1} and
     * {@code k2} in the map.  If the user attempts to put a key into the
     * map that violates this constraint (for example, the user attempts to
     * put a string key into a map whose keys are integers), the
     * {@code put(Object key, Object value)} call will throw a
     * {@code ClassCastException}.
     */
    public MyTreeMap() {
        comparator = null;
    }

    /**
     * Constructs a new, empty tree map, ordered according to the given
     * comparator.  All keys inserted into the map must be <em>mutually
     * comparable</em> by the given comparator: {@code comparator.compare(k1,
     * k2)} must not throw a {@code ClassCastException} for any keys
     * {@code k1} and {@code k2} in the map.  If the user attempts to put
     * a key into the map that violates this constraint, the {@code put(Object
     * key, Object value)} call will throw a
     * {@code ClassCastException}.
     *
     * @param comparator the comparator that will be used to order this map.
     *        If {@code null}, the {@linkplain Comparable natural
     *        ordering} of the keys will be used.
     */
    public MyTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    /**
     * Constructor method
     * @param m
     */
    public MyTreeMap(Map<? extends K, ? extends V> m) {
        comparator = null;
        putAll(m);
    }

    /**
     * Constructs a new tree map containing the same mappings and
     * using the same ordering as the specified sorted map.  This
     * method runs in linear time.
     *
     * @param  m the sorted map whose mappings are to be placed in this map,
     *         and whose comparator is to be used to sort this map
     * @throws NullPointerException if the specified map is null
     */
    public MyTreeMap(SortedMap<K, ? extends V> m) {
        comparator = m.comparator();
        try {
            buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
        } catch (java.io.IOException cannotHappen) {
        } catch (ClassNotFoundException cannotHappen) {
        }
    }


    /**
     * Copies all of the mappings from the specified map to this map.
     * These mappings replace any mappings that this map had for any
     * of the keys currently in the specified map.
     *
     * @param map mappings to be stored in this map
     * @throws ClassCastException   if the class of a key or value in
     *                              the specified map prevents it from being stored in this map
     * @throws NullPointerException if the specified map is null or
     *                              the specified map contains a null key and this map does not
     *                              permit null keys
     */
    public void putAll(Map<? extends K, ? extends V> map) {
        int mapSize = map.size();
        if (size==0 && mapSize!=0 && map instanceof SortedMap) {
            Comparator<?> c = ((SortedMap<?,?>)map).comparator();
            if (c == comparator || (c != null && c.equals(comparator))) {
                ++modCount;
                try {
                    buildFromSorted(mapSize, map.entrySet().iterator(),
                            null, null);
                } catch (java.io.IOException cannotHappen) {
                } catch (ClassNotFoundException cannotHappen) {
                }
                return;
            }
        }
        super.putAll(map);
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key}.)
     * @throws ClassCastException   if the specified key cannot be compared
     *                              with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     *                              and this map uses natural ordering, or its comparator
     *                              does not permit null keys
     */
    public V put(K key, V value) {
        // 保存根节点
        MyTreeMap.Entry<K,V> t = root;
        // 如果树是空的
        if (t == null) {
            compare(key, key); // type (and possibly null) check
            // 那么当前节点直接作为根节点
            root = new MyTreeMap.Entry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        // 定义一个临时节点，保存节点的爸爸
        MyTreeMap.Entry<K,V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        // 我们构造一个TreeMap时可以传入一个自己实现的比较器，作为节点直接比较大小的规则
        // 下面这个if和else就是根据我们是不是指定了自定义比较器来进行不同的比较规则
        // 先是确定比较规则，再进行比较，代码基本一样，我们看第一部分即可
        if (cpr != null) {
            // 如果自己定义了比较器
            do {
                // 从根节点往下，根据二叉排序树的特点，找到新的节点应该插入的合适位置
                parent = t;
                // 比较当前节点和新的节点的键值
                cmp = cpr.compare(key, t.key);
                // 如果新的节点键值小于当前节点
                if (cmp < 0)
                    // 那么我们就去当前节点的左子树中找合适位置
                    t = t.left;
                // 如果新的节点键值比当前节点大
                else if (cmp > 0)
                    // 我们就去当前节点的右子树中找它的合适位置
                    t = t.right;
                // 如果新的键值和当前节点的键值相等，
                else
                    // 则用新的节点值覆盖这个节点值，就直接返回，无后续操作
                    return t.setValue(value);
            } while (t != null);
        }
        // 跳过这个else，无非是用了另一个比较器，过程都一样
        else {
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        // 当找到合适位置后，parent中保存的是它的爸爸
        MyTreeMap.Entry<K,V> e = new MyTreeMap.Entry<>(key, value, parent);
        // 根据键值大小，决定它要作为它爸爸的左孩子还是右孩子
        if (cmp < 0)
            // 作为左孩子
            parent.left = e;
        else
            // 作为右孩子
            parent.right = e;
        // 插入之后，修复这颗红黑树
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }

    /**
     * Balancing operations.
     *
     * Implementations of rebalancings during insertion and deletion are
     * slightly different than the CLR version.  Rather than using dummy
     * nilnodes, we use a set of accessors that deal properly with null.  They
     * are used to avoid messiness surrounding nullness checks in the main
     * algorithms.
     */

    private static <K,V> boolean colorOf(MyTreeMap.Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }

    private static <K,V> MyTreeMap.Entry<K,V> parentOf(MyTreeMap.Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }

    private static <K,V> void setColor(MyTreeMap.Entry<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private static <K,V> MyTreeMap.Entry<K,V> leftOf(MyTreeMap.Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }

    private static <K,V> MyTreeMap.Entry<K,V> rightOf(MyTreeMap.Entry<K,V> p) {
        return (p == null) ? null: p.right;
    }

    /** From CLR */
    private void rotateLeft(MyTreeMap.Entry<K,V> p) {
        if (p != null) {
            MyTreeMap.Entry<K,V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /** From CLR */
    private void rotateRight(MyTreeMap.Entry<K,V> p) {
        if (p != null) {
            MyTreeMap.Entry<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    /** From CLR */
    /**
     * 回顾一下四种旋转情况：
     * 新插入节点【x】，它爸爸是【xx】，它爷爷是【xxx】
     * 1. 【xx】是【xxx】的左孩子：
     *      1.1 【x】是【xx】的左孩子
     *           以 【xx】为中心右旋
     *      1.2.【x】是【xx】的右孩子
     *           先以【xx】为中心【左旋】，再以【xxx】为中心【右旋】
     * 2. 【xx】是【xxx】的右孩子：
     *      2.1 【x】是【xx】的左孩子
     *           先以【xx】为中心【右旋】，再以【xxx】为中心【左旋】
     *      2.2.【x】是【xx】的右孩子
     *           先以【xx】为中心【左旋】
     * @param x
     */
    private void fixAfterInsertion(MyTreeMap.Entry<K,V> x) {
        // 新节点【51】标记为红色
        x.color = RED;
        // 如果它的爸爸是黑色，会跳转while，不进行变色也不用旋转，直接返回
        // 如果他的爸爸【49】是红色
        while (x != null && x != root && x.parent.color == RED) {
            // 如果它的爸爸是 左孩子
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                // 得到它的叔叔
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));

                // 如果它的叔叔【43】也是红色，那么只需要变色不需要旋转
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK); // 把它爸爸【49】变成黑色
                    setColor(y, BLACK); // 把它叔叔【43】变成黑色
                    setColor(parentOf(parentOf(x)), RED); // 把它爷爷【45】变成红色
                    x = parentOf(parentOf(x)); // 它跳转成它爷爷【45】，继续向上进行变色
                    // 下一次循环就是
                    // 【45】的爸爸【56】是红色
                    // if判断，【56】的兄弟【68】也是红色
                    // 把【56】和【68】都变成黑色
                    // 把【45】的爷爷【60】变成红色
                // 如果它的叔叔是黑色，那么需要变色+旋转
                } else {
                    // 如果它是右孩子
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        // 那么先以它爸爸为中心左旋
                        rotateLeft(x);
                    }
                    // 把它爸爸变成黑色
                    setColor(parentOf(x), BLACK);
                    // 把它爷爷变成红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 再以它爷爷为中心右旋
                    rotateRight(parentOf(parentOf(x)));
                }
            // 如果它的爸爸是右孩子
            } else {
                // 得到它的叔叔
                MyTreeMap.Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                // 如果它的叔叔也是红色，那么只需要变色，不需要旋转
                if (colorOf(y) == RED) {
                    // 把他爸爸变成黑色
                    setColor(parentOf(x), BLACK);
                    // 把他叔叔变成黑色
                    setColor(y, BLACK);
                    // 把他爷爷变成红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 它去到它爷爷的位置，继续向上重复变色过程
                    x = parentOf(parentOf(x));
                // 他的叔叔是黑色，需要旋转+变色
                } else {
                    // 如果它是左孩子
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        // 先以它爸爸为中心右旋
                        rotateRight(x);
                    }
                    // 把它爸爸变成黑色
                    setColor(parentOf(x), BLACK);
                    // 把他爷爷变成红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 再以它爷爷为中心左旋
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        // 根节点强制为黑色
        root.color = BLACK;
    }

    /**
     * Delete node p, and then rebalance the tree.
     */
    private void deleteEntry(MyTreeMap.Entry<K,V> p) {
        modCount++;
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            MyTreeMap.Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        MyTreeMap.Entry<K,V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    /** From CLR */
    private void fixAfterDeletion(MyTreeMap.Entry<K,V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                MyTreeMap.Entry<K,V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                MyTreeMap.Entry<K,V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

    /**
     * Returns the successor of the specified Entry, or null if no such.
     */
    static <K,V> MyTreeMap.Entry<K,V> successor(MyTreeMap.Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            MyTreeMap.Entry<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            MyTreeMap.Entry<K,V> p = t.parent;
            MyTreeMap.Entry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }

    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }


    /**
     * TreeMap中的节点结构
     * @param <K>
     * @param <V>
     */
    static final class Entry<K, V> implements Map.Entry<K, V> {
        K key; // 键
        V value; // 值
        MyTreeMap.Entry<K, V> left; // 左节点
        MyTreeMap.Entry<K, V> right; // 右节点
        MyTreeMap.Entry<K, V> parent; // 父亲节点
        boolean color = BLACK; // 默认是黑色

        /**
         * Make a new cell with given key, value, and parent, and with
         * {@code null} child links, and BLACK color.
         */
        Entry(K key, V value, MyTreeMap.Entry<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        /**
         * Returns the key.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value associated with the key.
         *
         * @return the value associated with the key
         */
        public V getValue() {
            return value;
        }

        /**
         * Replaces the value currently associated with the key with the given
         * value.
         *
         * @return the value associated with the key before this method was
         * called
         */
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

            return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
        }

        public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + "=" + value;
        }
    }

    /**
     * Test two values for equality.  Differs from o1.equals(o2) only in
     * that it copes with {@code null} o1 properly.
     */
    static final boolean valEquals(Object o1, Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    /**
     * Find the level down to which to assign all nodes BLACK.  This is the
     * last `full' level of the complete binary tree produced by
     * buildTree. The remaining nodes are colored RED. (This makes a `nice'
     * set of color assignments wrt future insertions.) This level number is
     * computed by finding the number of splits needed to reach the zeroeth
     * node.  (The answer is ~lg(N), but in any case must be computed by same
     * quick O(lg(N)) loop.)
     */
    private static int computeRedLevel(int sz) {
        int level = 0;
        for (int m = sz - 1; m >= 0; m = m / 2 - 1)
            level++;
        return level;
    }

    /**
     * Compares two keys using the correct comparison method for this TreeMap.
     */
    @SuppressWarnings("unchecked")
    final int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparator.compare((K)k1, (K)k2);
    }

    /**
     * Linear time tree building algorithm from sorted data.  Can accept keys
     * and/or values from iterator or stream. This leads to too many
     * parameters, but seems better than alternatives.  The four formats
     * that this method accepts are:
     *
     *    1) An iterator of Map.Entries.  (it != null, defaultVal == null).
     *    2) An iterator of keys.         (it != null, defaultVal != null).
     *    3) A stream of alternating serialized keys and values.
     *                                   (it == null, defaultVal == null).
     *    4) A stream of serialized keys. (it == null, defaultVal != null).
     *
     * It is assumed that the comparator of the TreeMap is already set prior
     * to calling this method.
     *
     * @param size the number of keys (or key-value pairs) to be read from
     *        the iterator or stream
     * @param it If non-null, new entries are created from entries
     *        or keys read from this iterator.
     * @param str If non-null, new entries are created from keys and
     *        possibly values read from this stream in serialized form.
     *        Exactly one of it and str should be non-null.
     * @param defaultVal if non-null, this default value is used for
     *        each value in the map.  If null, each value is read from
     *        iterator or stream, as described above.
     * @throws java.io.IOException propagated from stream reads. This cannot
     *         occur if str is null.
     * @throws ClassNotFoundException propagated from readObject.
     *         This cannot occur if str is null.
     */
    private void buildFromSorted(int size, Iterator<?> it,
                                 java.io.ObjectInputStream str,
                                 V defaultVal)
            throws  java.io.IOException, ClassNotFoundException {
        this.size = size;
        root = buildFromSorted(0, 0, size-1, computeRedLevel(size),
                it, str, defaultVal);
    }

    /**
     * Recursive "helper method" that does the real work of the
     * previous method.  Identically named parameters have
     * identical definitions.  Additional parameters are documented below.
     * It is assumed that the comparator and size fields of the TreeMap are
     * already set prior to calling this method.  (It ignores both fields.)
     *
     * @param level the current level of tree. Initial call should be 0.
     * @param lo the first element index of this subtree. Initial should be 0.
     * @param hi the last element index of this subtree.  Initial should be
     *        size-1.
     * @param redLevel the level at which nodes should be red.
     *        Must be equal to computeRedLevel for tree of this size.
     */
    @SuppressWarnings("unchecked")
    private final MyTreeMap.Entry<K,V> buildFromSorted(int level, int lo, int hi,
                                                     int redLevel,
                                                     Iterator<?> it,
                                                     java.io.ObjectInputStream str,
                                                     V defaultVal)
            throws  java.io.IOException, ClassNotFoundException {
        /*
         * Strategy: The root is the middlemost element. To get to it, we
         * have to first recursively construct the entire left subtree,
         * so as to grab all of its elements. We can then proceed with right
         * subtree.
         *
         * The lo and hi arguments are the minimum and maximum
         * indices to pull out of the iterator or stream for current subtree.
         * They are not actually indexed, we just proceed sequentially,
         * ensuring that items are extracted in corresponding order.
         */

        if (hi < lo) return null;

        int mid = (lo + hi) >>> 1;

        MyTreeMap.Entry<K,V> left  = null;
        if (lo < mid)
            left = buildFromSorted(level+1, lo, mid - 1, redLevel,
                    it, str, defaultVal);

        // extract key and/or value from iterator or stream
        K key;
        V value;
        if (it != null) {
            if (defaultVal==null) {
                Map.Entry<?,?> entry = (Map.Entry<?,?>)it.next();
                key = (K)entry.getKey();
                value = (V)entry.getValue();
            } else {
                key = (K)it.next();
                value = defaultVal;
            }
        } else { // use stream
            key = (K) str.readObject();
            value = (defaultVal != null ? defaultVal : (V) str.readObject());
        }

        MyTreeMap.Entry<K,V> middle =  new MyTreeMap.Entry<>(key, value, null);

        // color nodes in non-full bottommost level red
        if (level == redLevel)
            middle.color = RED;

        if (left != null) {
            middle.left = left;
            left.parent = middle;
        }

        if (mid < hi) {
            MyTreeMap.Entry<K,V> right = buildFromSorted(level+1, mid+1, hi, redLevel,
                    it, str, defaultVal);
            middle.right = right;
            right.parent = middle;
        }

        return middle;
    }
}
