package com.mianshi.year2020.tecent.April;

import java.util.Scanner;
import java.util.Stack;

/**
 * @author wangwei
 * 2020/4/26 21:16
 *
 * 用两个栈模拟队列
 */
public class Main4 {
    /**
     * ---------  ------------
     * outStack| |  inStack
     * ---------  ------------
     */
    static class MyQueue {

        private Stack<Integer> inStack;
        private Stack<Integer> outStack;

        /**
         * Initialize your data structure here.
         */
        public MyQueue() {
            inStack = new Stack<>();
            outStack = new Stack<>();
        }

        /**
         * Push element x to the back of queue.
         */
        public void add(int x) {
            // instack用于入队
            inStack.push(x);
        }

        /**
         * Removes the element from in front of queue and returns that element.
         */
        public int pop() {
            // 避免outStack为空，而inStack中的元素未调整顺序到outStack中
            peek();
            // outStack用于出队
            return outStack.pop();
        }

        /**
         * Get the front element.
         */
        public int peek() {
            if (outStack.isEmpty()){
                // 把inStack中的元素出队再进入outStack就是正确顺序
                while (!inStack.empty())
                    outStack.push(inStack.pop());
            }
            return outStack.peek();
        }

        /**
         * Returns whether the queue is empty.
         */
        public boolean empty() {
            // 双栈都空才能说明队列空
            return inStack.isEmpty() && outStack.isEmpty();
        }
    }

    public static void main(String[] args) {
        MyQueue queue = new MyQueue();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] info = line.split(" ");
            if (info.length > 1) {
                // 插入
                queue.add(Integer.valueOf(info[1]));
            } else if (line.equals("peek")) {
                    System.out.println(queue.peek());
            } else if (line.equals("poll")) {
                queue.pop();
            }
        }
    }
}

