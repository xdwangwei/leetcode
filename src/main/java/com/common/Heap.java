package com.common;

/**
 * @author wangwei
 * 2022/3/17 15:44
 */
public class Heap {

    public static final int DEFAULT_CAPACITY = 16;

    private int[] elements;

    private int currentSize;

    public Heap() {
        this(DEFAULT_CAPACITY);
    }

    public Heap(int capacity) {
        elements = new int[capacity + 1];
        currentSize = 0;
    }

    public Heap(int[] nums) {
        elements = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            elements[i + 1] = nums[i];
        }
        currentSize = nums.length;
        buildHeap();
    }

    private void buildHeap() {
        for (int i = currentSize / 2; i >= 1; i--){
            sink(i);
        }
    }

    private void sink(int hole) {
        int child;
        int temp = elements[hole];
        for (; 2 * hole <= currentSize; hole = child) {
            child = 2 * hole;
            if (child != currentSize && elements[child + 1] < elements[child]) {
                child++;
            }
            if (elements[child] < temp) {
                elements[hole] = elements[child];
            }
        }
        elements[hole] = temp;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void makeEmpty() {
        currentSize = 0;
    }

    private int getMin() {
        return elements[1];
    }

    public int delMin() {
        int min = getMin();
        elements[1] = elements[currentSize--];
        sink(1);
        return min;
    }

    public void insert(int x) {
        int hole = ++currentSize;
        for (elements[0] = x; x < elements[hole / 2]; hole /= 2) {
            elements[hole] = elements[hole / 2];
        }
        elements[hole] = x;
    }

    public static void main(String[] args) {
        int numItems = 100;
        Heap h = new Heap(numItems);
        for(int i = 37; i != 0; i = (i + 37) % numItems) {
            h.insert(i);
        }

        for(int i = 1; i < numItems; i++)
            System.out.println(h.delMin());
            // if(h.delMin() != i)
            //     System.out.println("Oops! " + i);
    }
}
