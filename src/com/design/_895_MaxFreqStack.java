package com.design;

import java.util.HashMap;
import java.util.Stack;

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

    static class FreqStack {

        // 记录 FreqStack 中元素的最大频率
        int maxFreq = 0;
        // 记录 FreqStack 中每个 val 对应的出现频率，后文就称为 VF 表
        HashMap<Integer, Integer> valToFreq = new HashMap<>();
        // 记录频率 freq 对应的 val 列表，后文就称为 FV 表
        HashMap<Integer, Stack<Integer>> freqToVals = new HashMap<>();

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
            freqToVals.putIfAbsent(freq, new Stack<>());
            freqToVals.get(freq).push(val);
            // 更新 maxFreq
            maxFreq = Math.max(maxFreq, freq);
        }

        // 从栈中删除并返回出现频率最高的元素
        // 如果频率最高的元素不止一个，
        // 则返回最近添加的那个元素
        public int pop() {
            // 修改 FV 表：pop 出一个 maxFreq 对应的元素 v ==> v 就是最新的那个达到最大访问频次的元素
            Stack<Integer> vals = freqToVals.get(maxFreq);
            // v 就是最新的那个达到最大访问频次的元素
            int v = vals.pop();
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
