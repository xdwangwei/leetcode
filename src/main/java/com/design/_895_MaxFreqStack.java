package com.design;

import java.util.*;

/**
 * @author wangwei
 * 2021/11/25 9:58
 *
 * 实现 FreqStack，模拟类似栈的数据结构的操作的一个类。
 *
 * FreqStack有两个函数：
 *
 * push(int x)，将整数x推入栈中。
 * pop()，它移除并返回栈中出现最频繁的元素。
 * 如果最频繁的元素不只一个，则移除并返回最接近栈顶的元素。
 *
 *
 * 示例：
 *
 * 输入：
 * ["FreqStack","push","push","push","push","push","push","pop","pop","pop","pop"],
 * [[],[5],[7],[5],[7],[4],[5],[],[],[],[]]
 * 输出：[null,null,null,null,null,null,null,5,7,5,4]
 * 解释：
 * 执行六次 .push 操作后，栈自底向上为 [5,7,5,7,4,5]。然后：
 *
 * pop() -> 返回 5，因为 5 是出现频率最高的。
 * 栈变成 [5,7,5,7,4]。
 *
 * pop() -> 返回 7，因为 5 和 7 都是频率最高的，但 7 最接近栈顶。
 * 栈变成 [5,7,5,4]。
 *
 * pop() -> 返回 5 。
 * 栈变成 [5,7,4]。
 *
 * pop() -> 返回 4 。
 * 栈变成 [5,7]。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-frequency-stack
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _895_MaxFreqStack {


    /**
     * hash表和栈
     *
     * 仔细思考一下 push 和 pop 方法，难点如下：
     *
     * 1、每次 pop 时，必须要知道频率最高的元素是什么。
     *
     * 2、如果频率最高的元素有多个，还得知道哪个是最近 push 进来的元素是哪个。
     *
     * 为了实现上述难点，我们要做到以下几点：
     *
     * 1、肯定要有一个变量 maxFreq 记录当前栈中最高的频率是多少。
     *
     * 2、我们得知道一个频率 freq 对应的元素有哪些，且这些元素要有时间顺序（栈或双端队列）。
     *
     * 3、随着 pop 的调用，每个 val 对应的频率会变化，所以还得维持一个映射记录每个 val 对应的 freq。
     *
     * 实现：
     *
     * 首先，我们容易想到建立 第一个哈希表 cnts 用于记录某个数值的出现次数，cnts[val] = c 含义为数值 val 当前在栈中的出现次数为 c。我们称该哈希表为「计数哈希表」。
     *
     * 再结合每次 pop 需要返回「频率最大的元素，若有多个则返回最考虑栈顶的一个」的要求，
     * 我们还可以 建立第二个哈希 map，
     * 该哈希表以「出现次数 c」为键，以「出现次数均为 c 的元素序列」为值，
     * map[c] = A = [...] 含义为出现次数为 c 的序列为 A，并且序列 A 中的结尾元素为出现次数为 c 的所有元素中最靠近栈顶的元素。
     * 我们称该哈希表为「分桶哈希表」。
     *
     * 最后再额外使用一个变量 maxFreq 记录当前最大出现频数，只有当出现次数为 maxFreq 的元素全部被 pop 后，maxFreq--
     * 因此当我们在某个 pop 操作后发现出现次数为 max 的集合为空时，对 max 进行自减操作即可。
     *
     * 【注意】
     * 假如 push 方法参数顺序是 1、2、1、3、1
     *
     * 1先放入 频率为 1的栈
     * 2放入 频率为 1的栈
     * 1再放入 频率为 2的栈，
     *
     * 此时不要去管频率为1的栈中的1，此时最大频率为2，从频率为2的栈中进行移除
     * 这样当1被pop后，它频率变为1，之后的pop会在频率为1的栈中进行，
     * 由于之前未从其中移除1，所以1、2还是保持了最初的入栈顺序，符合要求
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/maximum-frequency-stack/solution/by-ac_oier-tquk/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    static class FreqStack {

        // 记录 FreqStack 中元素的最大频率
        int maxFreq = 0;
        // 记录 FreqStack 中每个 val 对应的出现频率，后文就称为 VF 表
        Map<Integer, Integer> valToFreq;
        // 记录频率 freq 对应的 val 列表，后文就称为 FV 表
        Map<Integer, Deque<Integer>> freqToVals;

        public FreqStack() {
            valToFreq = new HashMap<>();
            freqToVals = new HashMap<>();
            maxFreq = 0;
        }

        // 在栈中加入一个元素 val, 增加它对应的频次
        public void push(int val) {
            // 修改 VF 表：val 对应的 freq 加一
            int freq = valToFreq.getOrDefault(val, 0) + 1;
            valToFreq.put(val, freq);
            // 修改 FV 表：在 freq 对应的列表加上 val
            freqToVals.putIfAbsent(freq, new ArrayDeque<>());
            freqToVals.get(freq).addLast(val);
            // 更新 maxFreq
            maxFreq = Math.max(maxFreq, freq);
        }

        // 从栈中删除并返回出现频率最高的元素
        // 如果频率最高的元素不止一个，
        // 则返回最近添加的那个元素
        public int pop() {
            // 修改 FV 表：pop 出一个 maxFreq 对应的元素 v ==> v 就是最新的那个达到最大访问频次的元素
            Deque<Integer> vals = freqToVals.get(maxFreq);
            // v 就是最新的那个达到最大访问频次的元素
            int v = vals.removeLast();
            // 修改 VF 表：v 对应的 freq 减一，这里不用去vf表获取它的频次，因为它本身就是从最大频次对应的所有元素中拿出来的，所以它的频次就是maxFreq
            valToFreq.put(v, maxFreq - 1);
            // 如果最大频次对应的所有元素栈空了，那么更新 maxFreq--
            if (vals.isEmpty()) {
                // 为什么直接减1. 假如最大频次是5.对应元素是 a，那么把a pop了，栈空了，a的频次减1了，那不就等同于最大频次减1了
                maxFreq--;
            }
            return v;
        }
    }

    public static void main(String[] args) {
        FreqStack stack = new FreqStack();
        stack.push(5);
        stack.push(7);
        stack.push(5);
        stack.push(7);
        stack.push(4);
        stack.push(5);
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
    }
}
