package com.common;

import java.nio.BufferUnderflowException;

/**
 * @author wangwei
 * 2020/9/6 22:22
 *
 * 自己实现的二叉堆  数组，有效位置 1 - currentSize，最小堆
 */
public class BinaryHeap<AnyType extends Comparable<? super AnyType>> {

    private static final int DEFAULT_CAPACITY = 10; // 默认容量

    private int currentSize; // 堆中元素个数

    private AnyType[] array; // 数组实现二叉堆

    // 构造函数
    public BinaryHeap() {
        // 使用默认大小初始化
        this(DEFAULT_CAPACITY);
    }

    public BinaryHeap(int capacity) {
        // 堆中无元素
        currentSize = 0;
        // 为数组分配空间
        array = (AnyType[]) new Comparable[capacity + 1];
    }

    // 传入数组，转为二叉堆
    public BinaryHeap(AnyType[] items) {
        currentSize = items.length;
        array = (AnyType[]) new Comparable[(currentSize + 2) * 11 / 10];
        int i = 1;
        // 顺序放入数组
        for (AnyType item : items) {
            array[i++] = item;
        }
        // 调整，满足堆序
        buildHeap();
    }

    // 找到堆中最小元素，堆顶元素
    public AnyType findMin() {
        if (isEmpty()) {
            throw new BufferUnderflowException();
        }
        return array[1];
    }

    // 删除堆中最小元素，堆顶
    public AnyType deleteMin() {
        if (isEmpty()) {
            throw new BufferUnderflowException();
        }
        // 找到堆顶
        AnyType min = findMin();
        // 把最后一个位置元素挪到堆顶，堆中元素个数减1
        array[1] = array[currentSize--];
        // 堆顶元素下沉到合适位置
        percolateDown(1);

        return min;
    }

    // 调整array，使满足堆有序
    private void buildHeap() {
        // 从最后一个非叶子节点开始下沉
        for (int i = currentSize / 2; i >= 1; i--) {
            percolateDown(i);
        }
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void makeEmpty() {
        currentSize = 0;
    }

    // 将空穴(元素)下沉到合适位置
    private void percolateDown(int hole) {
        int child;
        AnyType temp = array[hole];
        // 在有效范围内
        for (; 2 * hole <= currentSize; hole = child) {
            // 选择左右孩子中更小的那个，先选择左孩子
            child = hole * 2;
            // 如果右孩子存在，并且比左孩子更小
            if (child != currentSize
                    && array[child + 1].compareTo(array[child]) < 0) {
                // 选择右孩子
                child++;
            }
            // 如果当前位置，更小的孩子比x还小，则让小孩子挪到hole的位置
            if (array[child].compareTo(temp) < 0) {
                array[hole] = array[child];
            } else {
                break; // 找到合适位置
            }
        }
        array[hole] = temp;
    }

    // 向堆中加入新元素
    // 可以先放在最后一个位置，然后逐层上浮(每次交换要3条赋值语句)
    // 选择，先找到合适位置，再放入x
    public void insert(AnyType x) {
        // 容量已满，先扩容
        if (currentSize == array.length - 1) {
            enlargeArray(2 * array.length + 1);
        }
        // 暂时放在原最后一个位置的后面
        int hole = ++currentSize;
        // 一路向上，找到不破坏堆序的位置停下
        // 如果 x 某个中间值，那么 在某个位置 必然会打破 x.compareTo(array[hole / 2]) < 0，
        // 如果新加入的元素x是新的最小值，那么某一时刻hole必然会为1
        // 要打破条件 x.compareTo(array[1/2] = array[0]) < 0，所以提前把x赋给array[0]; array[0] = x;
        // 否则就需要在for循环内部对此情况单独判断
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2];
        }
        // 把x放在合适位置
        array[hole] = x;
    }

    // 扩容
    private void enlargeArray(int newSize) {
        AnyType[] old = array;
        array = (AnyType[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++) {
            array[i] = old[i];
        }
    }

    public static void main(String[] args) {
        int numItems = 10000;
        BinaryHeap<Integer> h = new BinaryHeap<>();
        int i = 37;

        for(i = 37; i != 0; i = (i + 37) % numItems)
            h.insert(i);
        for(i = 1; i < numItems; i++)
            if(h.deleteMin() != i)
                System.out.println("Oops! " + i);
    }

}
