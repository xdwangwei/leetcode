package com.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangwei
 * 2021/11/14 11:39
 * <p>
 * 给你一个嵌套的整数列表 nestedList 。每个元素要么是一个整数，要么是一个列表；该列表的元素也可能是整数或者是其他列表。请你实现一个迭代器将其扁平化，使之能够遍历这个列表中的所有整数。
 * <p>
 * 实现扁平迭代器类 NestedIterator ：
 * <p>
 * NestedIterator(List<NestedInteger> nestedList) 用嵌套列表 nestedList 初始化迭代器。
 * int next() 返回嵌套列表的下一个整数。
 * boolean hasNext() 如果仍然存在待迭代的整数，返回 true ；否则，返回 false 。
 * 你的代码将会用下述伪代码检测：
 * <p>
 * initialize iterator with nestedList
 * res = []
 * while iterator.hasNext()
 * append iterator.next() to the end of res
 * return res
 * 如果 res 与预期的扁平化列表匹配，那么你的代码将会被判为正确。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：nestedList = [[1,1],2,[1,1]]
 * 输出：[1,1,2,1,1]
 * 解释：通过重复调用 next 直到 hasNext 返回 false，next 返回的元素的顺序应该是: [1,1,2,1,1]。
 * 示例 2：
 * <p>
 * 输入：nestedList = [1,[4,[6]]]
 * 输出：[1,4,6]
 * 解释：通过重复调用 next 直到 hasNext 返回 false，next 返回的元素的顺序应该是: [1,4,6]。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/flatten-nested-list-iterator
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _341_FlattenNestedListIterator {


    /**
     * 方法一：构造函数内部，通过dfs将所有 NestedIterator 中的整数按顺序加入一个list，最终操作这个list
     */
    class NestedIterator implements Iterator<Integer> {

        private Iterator<Integer> iterator;

        public NestedIterator(List<NestedInteger> nestedList) {
            LinkedList<Integer> list = new LinkedList<>();
            // dfs 将遍历 全部 NestedInteger。按顺序加入 list
            for (NestedInteger nestedInteger : nestedList) {
                traverse(nestedInteger, list);
            }

            this.iterator = list.iterator();
        }

        /**
         * dfs
         *
         * @param nestedInteger
         * @param list
         */
        private void traverse(NestedInteger nestedInteger, LinkedList<Integer> list) {
            // 当前是个 整数，直接加入
            if (nestedInteger.isInteger()) {
                list.addLast(nestedInteger.getInteger());
            } else {
                // 当前是个列表
                List<NestedInteger> nestedList = nestedInteger.getList();
                for (NestedInteger child : nestedList) {
                    // 逐个 dfs
                    traverse(child, list);
                }
            }
        }

        @Override
        public Integer next() {
            return iterator.next();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
    }

    /**
     * 方法一的不足在于需要在构造函数中把整个 List<NestedInteger> 全部遍历完
     * 实际上我们可以选择每次只遍历一部分，因为在调用 next 之间会先调用hasNext。
     * 我们可以在hasNext中进行判断，如果当前第一个元素是个 NestedIterator。那就遍历把，拆分它，把元素一个个加入list
     * 保证list的第一个元素是个整数，然后 next 只需呀返回list第一个元素就行了
     */
    class NestedIterator2 implements Iterator<Integer> {

        // 内部维护
        private LinkedList<NestedInteger> resList;

        public NestedIterator2(List<NestedInteger> nestedList) {
            // 转存进这个list
            resList = new LinkedList<>(nestedList);
        }

        @Override
        public Integer next() {
            // 每次只需要返回当前列表第一个元素即可，hasNext保证它是一个Integer
            return resList.removeFirst().getInteger();
        }

        @Override
        public boolean hasNext() {
            // 如果list不空，就确保它的第一个元素为 Integer， 否则就进行拆分，逐个加入list。注意倒着拆分，再头插，就能恢复原序
            // 这里用while，加入 list 第一个 是 list，那么拆出去加入头，while继续满足，会一直拆，直到变成整数。不能用if
            while (!resList.isEmpty() && !resList.getFirst().isInteger()) {
                // 列表第一个元素是个 List<NestedInteger>
                List<NestedInteger> list = resList.removeFirst().getList();
                // 倒着遍历，再从头插，保证顺序不被打乱
                for (int i = list.size() - 1; i >= 0; --i) {
                    resList.addFirst(list.get(i));
                }
            }
            // 如果list不空，就是有元素
            return !resList.isEmpty();
        }
    }

    interface NestedInteger {

        // @return true if this NestedInteger holds a single integer, rather than a nested list.
        public boolean isInteger();

        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger();

        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return empty list if this NestedInteger holds a single integer
        public List<NestedInteger> getList();
    }
}
