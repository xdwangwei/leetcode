package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/1/18 18:42
 * @description: _1825_FindingMKAverage
 *
 * 1825. 求出 MK 平均值
 * 给你两个整数 m 和 k ，以及数据流形式的若干整数。你需要实现一个数据结构，计算这个数据流的 MK 平均值 。
 *
 * MK 平均值 按照如下步骤计算：
 *
 * 如果数据流中的整数少于 m 个，MK 平均值 为 -1 ，否则将数据流中最后 m 个元素拷贝到一个独立的容器中。
 * 从这个容器中删除最小的 k 个数和最大的 k 个数。
 * 计算剩余元素的平均值，并 向下取整到最近的整数 。
 * 请你实现 MKAverage 类：
 *
 * MKAverage(int m, int k) 用一个空的数据流和两个整数 m 和 k 初始化 MKAverage 对象。
 * void addElement(int num) 往数据流中插入一个新的元素 num 。
 * int calculateMKAverage() 对当前的数据流计算并返回 MK 平均数 ，结果需 向下取整到最近的整数 。
 *
 *
 * 示例 1：
 *
 * 输入：
 * ["MKAverage", "addElement", "addElement", "calculateMKAverage", "addElement", "calculateMKAverage", "addElement", "addElement", "addElement", "calculateMKAverage"]
 * [[3, 1], [3], [1], [], [10], [], [5], [5], [5], []]
 * 输出：
 * [null, null, null, -1, null, 3, null, null, null, 5]
 *
 * 解释：
 * MKAverage obj = new MKAverage(3, 1);
 * obj.addElement(3);        // 当前元素为 [3]
 * obj.addElement(1);        // 当前元素为 [3,1]
 * obj.calculateMKAverage(); // 返回 -1 ，因为 m = 3 ，但数据流中只有 2 个元素
 * obj.addElement(10);       // 当前元素为 [3,1,10]
 * obj.calculateMKAverage(); // 最后 3 个元素为 [3,1,10]
 *                           // 删除最小以及最大的 1 个元素后，容器为 [3]
 *                           // [3] 的平均值等于 3/1 = 3 ，故返回 3
 * obj.addElement(5);        // 当前元素为 [3,1,10,5]
 * obj.addElement(5);        // 当前元素为 [3,1,10,5,5]
 * obj.addElement(5);        // 当前元素为 [3,1,10,5,5,5]
 * obj.calculateMKAverage(); // 最后 3 个元素为 [5,5,5]
 *                           // 删除最小以及最大的 1 个元素后，容器为 [5]
 *                           // [5] 的平均值等于 5/1 = 5 ，故返回 5
 *
 *
 * 提示：
 *
 * 3 <= m <= 105
 * 1 <= k*2 < m
 * 1 <= num <= 105
 * addElement 与 calculateMKAverage 总操作次数不超过 105 次。
 * 通过次数9,136提交次数23,372
 */
public class _1825_FindingMKAverage {


    /**
     * 三个有序集合 + 队列
     *
     * 我们使用三个有序集合 s1、s2、s3 分别保存最小的 k 个元素、中间的 m−2k 个元素和最大的 k 个元素；
     * 使用 sum2 保存 s2 中所有元素之和；使用队列 q 保存顺序加入的元素（及时移除队首实现 只保留最后的 m 个元素）。
     *
     * 对于有序集合可以选择 优先队列、treeset、treemap
     * 因为我们需要快速获取队列最小和最大元素，因此需要使用基于红黑树的 treeset 或 treemap
     * 因为元素可能会重复出现，而set不允许重复，因此我们使用 treemap
     * treemap中，key 是元素本身；value 是元素出现次数；当 value=0时，就移除这个key，代表当前集合中不包含此元素
     * 此时不能通过 size() 获取元素个数，需要单独维护变量来统计实际元素个数
     *
     * addElement 函数：
     *
     * 我们先将新增加的元素 num 放入队列 q 中，然后对 q 的元素数目进行判断：
     *     如果加入num后 q 的元素数目小于等于 m（前m个元素）：将新增加的元素 num 插入有序集合 s2 中，并且更新 sum2 += num；
     *          并且，如果此时恰好是加入了第m个元素，那么我们需要分别将 最小的 k 个元素和最大的 k 个元素移动到 s1 和 s3中，
     *          同时相应地更新 sum2
     *     如果加入num后， q 的元素数目等于 m+1（此时是第m+1个元素）：
     *          那么我们先根据num大小将其加入对应s中，使s1、s2、s3维护了后m+1个元素
     *          然后我们再找出倒数第m+1个元素，将其从s中移除，实现s1、s2、s3只维护最后m个元素
     *          具体做法为：
     *              第一步：加入num
     *                如果 num 小于 s1 的最大元素，那么我们将它插入 s1 中，
     *                  然后为了维持s1只保留最小的k个元素，将 s1 的最大元素移动到 s2 中，同时相应地更新 sum2；
     *                如果 num 大于 s3 的最大元素，那么我们将它插入 s3 中，
     *                  然后为了维持s3只保留最大的k个元素，将 s3 的最小元素移动到 s2 中，同时相应地更新 sum2；
     *                否则我们将 num 插入 s2 中，并且更新 sum2 += num
     *              第二步：移除倒数第m+1个元素
     *                  我们要删除的元素x实际上就是此时的队首元素（队列维护最后m个元素，加入一个元素就移除队首）
     *                  以上操作之后，s2 的元素数目额外多出 1 个，
     *                  因此当删除操作发生在 s1 或 s3 时，需要从 s2 移动元素以保持元素数目的平衡。
     *
     *                  我们从队列 q 的队头取出首元素 x。对于待删除的元素 x：
     *                  如果 x 在 s2 中，我们将它从 s2 中删除，并更新 sum2 -= x；
     *                  如果 x 在 s1 中，我们将它从 s1 中删除，然后将 s2 中最小的元素移动到 s1 中；
     *                  如果 x 在 s3 中，我们将它从 s3 中删除，然后将 s2 中最大的元素移动到 s3 中；
     *
     * calculateMKAverage 函数：
     *
     *      如果队列 q 的元素数目小于 m，直接返回 −1；否则返回 ⌊sum2 / (m-2k)⌋
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/finding-mk-average/solution/qiu-chu-mk-ping-jun-zhi-by-leetcode-solu-sos6/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class MKAverage {
        private int m, k;
        // 保存按顺序加入的元素，但只保留后m个，因此会及时移除队首
        private Queue<Integer> q;
        // 保存后m个元素中最小的k个元素
        private TreeMap<Integer, Integer> s1;
        // 保存后m个元素中最大的k个元素
        private TreeMap<Integer, Integer> s3;
        // 保存后m个元素中中间的m-2k个元素
        private TreeMap<Integer, Integer> s2;
        // 保存s1、s3中元素个数，因为相同数字只对应一个key，因此无法通过size()获取元素个数，需要单独计数
        // 三者元素个数总和为m，不用同时维护三个
        private int size1, size3;
        // s2中元素和
        private int sum2;

        /**
         * 构造函数，初始化
         * @param m
         * @param k
         */
        public MKAverage(int m, int k) {
            this.m = m;
            this.k = k;
            this.q = new ArrayDeque<>();
            this.s1 = new TreeMap<>();
            this.s2 = new TreeMap<>();
            this.s3 = new TreeMap<>();
            this.size1 = 0;
            this.size3 = 0;
            this.sum2 = 0;
        }

        /**
         * 加入元素
         * @param num
         */
        public void addElement(int num) {
            // 先加入队列
            q.offer(num);
            // 如果是前m个元素
            if (q.size() <= m) {
                // 都保存到s2中
                s2.put(num, s2.getOrDefault(num, 0) + 1);
                // 更新s2的元素和
                sum2 += num;
                // 当元素个数到达m时，
                if (q.size() == m) {
                    // 将s2中最小的k个元素移入s1，最大的k个元素移入s3
                    // 这里并不是从s2中移出去2k个元素，而是保证s1、s3大小都是k（s2大小为m-2k）
                    while (size1 < k) {
                        // 选择s2最小元素移入s2
                        int firstNum = s2.firstKey();
                        // 更新s1中此元素个数
                        s1.put(firstNum, s1.getOrDefault(firstNum, 0) + 1);
                        // 更新s1元素个数
                        size1++;
                        // 更新s2元素和
                        sum2 -= firstNum;
                        // 更新s2中此元素个数
                        s2.put(firstNum, s2.get(firstNum) - 1);
                        // 如果此元素个数减到0，从s2中移除这个key，因为之后会调用containsKey方法，所以必须移除无效key
                        if (s2.get(firstNum) == 0) {
                            s2.remove(firstNum);
                        }
                    }
                    // 维护s3大小为k
                    while (size3 < k) {
                        // 从s2中移除最大的元素到s3中
                        int lastNum = s2.lastKey();
                        // 更新s3中此元素个数
                        s3.put(lastNum, s3.getOrDefault(lastNum, 0) + 1);
                        // 更新s3中全部元素个数
                        size3++;
                        // 更新s2元素和
                        sum2 -= lastNum;
                        // 更新s2中此元素个数
                        s2.put(lastNum, s2.get(lastNum) - 1);
                        // 及时清除无效key
                        if (s2.get(lastNum) == 0) {
                            s2.remove(lastNum);
                        }
                    }
                }
                return;
            }
            // sz == m + 1
            // 当前是第m+1个元素，s1、s2、s3维护的是后m个元素的情况
            // 因此，我们先按照num的大小，将其加入对应s中，再找到倒数第m+1个元素，将其中s中移除出去，就实现了迭代更新s
            // 1. 将num加入s1
            // num应该加入s1
            if (num < s1.lastKey()) {
                // 更新s1中num的个数
                s1.put(num, s1.getOrDefault(num, 0) + 1);
                // 为了维护s1元素个数为k，此时需要将s1中最大的元素移动到s2
                int lastNum = s1.lastKey();
                // 更新s2中此元素的个数
                s2.put(lastNum, s2.getOrDefault(lastNum, 0) + 1);
                // 更新s2元素和
                sum2 += lastNum;
                // 更新s1中此元素个数
                s1.put(lastNum, s1.get(lastNum) - 1);
                // 及时清理无效key
                if (s1.get(lastNum) == 0) {
                    s1.remove(lastNum);
                }
            // num应该加入s3
            } else if (num > s3.firstKey()) {
                // 更新s3中num的个数
                s3.put(num, s3.getOrDefault(num, 0) + 1);
                // 为了维护s3的个数为k，需要将s3中最小的元素移动到s2
                int firstNum = s3.firstKey();
                // 更新s2中此元素个数
                s2.put(firstNum, s2.getOrDefault(firstNum, 0) + 1);
                // 更新s2元素累加和
                sum2 += firstNum;
                // 更新s3中此元素个数
                s3.put(firstNum, s3.get(firstNum) - 1);
                // 及时清理无效key
                if (s3.get(firstNum) == 0) {
                    s3.remove(firstNum);
                }
            // num应该放入s2
            } else {
                // 更新s2中num的个数
                s2.put(num, s2.getOrDefault(num, 0) + 1);
                // 更新s2元素累加和
                sum2 += num;
            }
            // 2. 我们只保留后m个元素，因此需要从s1、s2、s3中移除第倒数第m+1个元素
            // 队首元素x即为要移除的元素
            int x = q.poll();
            // 如果x在s1中
            if (s1.containsKey(x)) {
                // 从s1中移除x
                // 更新s1中x的个数
                s1.put(x, s1.get(x) - 1);
                // 清理无效key
                if (s1.get(x) == 0) {
                    s1.remove(x);
                }
                // 为了维护s1元素个数为k，此时需要将s2中最小的元素移入s1
                // 因为在第一步加入num后，相当于此时s1、s2、s3维护了m+1个元素，
                // 因此当s1中移除一个元素时，我们可以直接从s2移除最小元素到s1，就能实现s1、s2、s3个数分别为k、m-2k、k
                // 选择s2最小key
                int firstNum = s2.firstKey();
                // 更新s1中此元素个数
                s1.put(firstNum, s1.getOrDefault(firstNum, 0) + 1);
                // 更新s2元素累加和
                sum2 -= firstNum;
                // 更新s2中此元素个数
                s2.put(firstNum, s2.get(firstNum) - 1);
                // 清理无效key
                if (s2.get(firstNum) == 0) {
                    s2.remove(firstNum);
                }
            // x在s3中
            } else if (s3.containsKey(x)) {
                // 从s3中移除x
                // 更新s3中x的个数
                s3.put(x, s3.get(x) - 1);
                // 清理无效key
                if (s3.get(x) == 0) {
                    s3.remove(x);
                }
                // 为了维护s3元素个数为k，需要将s2中最大元素移入s3
                int lastNum = s2.lastKey();
                // 更新s3中此元素个数
                s3.put(lastNum, s3.getOrDefault(lastNum, 0) + 1);
                // 更新s2元素累加和
                sum2 -= lastNum;
                // 更新s2此元素个数
                s2.put(lastNum, s2.get(lastNum) - 1);
                // 清理无效key
                if (s2.get(lastNum) == 0) {
                    s2.remove(lastNum);
                }
            // x在s2中
            } else {
                // 从s2中移除x，此时s2元素个数一定是m-2k+1
                // 更新s2中x的个数
                s2.put(x, s2.get(x) - 1);
                // 清理无效key
                if (s2.get(x) == 0) {
                    s2.remove(x);
                }
                // 更新s2中元素累加和
                sum2 -= x;
            }
        }

        /**
         * 计算中间m-2k个元素的平均值，向下取整
         * @return
         */
        public int calculateMKAverage() {
            // 如果元素个数没有超过m，则返回-1，否则返回s2（共m-2k个元素，累加和为sum2）中元素平均值
            return q.size() < m ? -1 : sum2 / (m - 2 * k);
        }
    }

    private void test() {
        MKAverage obj = new MKAverage(3, 1);
        obj.addElement(3);
        obj.addElement(1);
        System.out.println(obj.calculateMKAverage());
        obj.addElement(10);
        System.out.println(obj.calculateMKAverage());
        obj.addElement(5);
        obj.addElement(5);
        obj.addElement(5);
        System.out.println(obj.calculateMKAverage());
    }

    public static void main(String[] args) {
        _1825_FindingMKAverage obj = new _1825_FindingMKAverage();
        obj.test();
    }
}
