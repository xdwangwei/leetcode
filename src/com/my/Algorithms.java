package com.my;

/**
 * @author wangwei
 * 2021/7/8 21:26
 */
public class Algorithms {

    /**
     * 快速排序
     * @param arr
     */
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        int mid = partition(arr, low, high);
        // int mid = partition2(arr, low, high);
        quickSort(arr, low, mid - 1);
        quickSort(arr, mid + 1, high);
    }

    /**
     * 快速排序中间过程，一次 partition
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private static int partition(int[] arr, int low, int high) {
        // 哨兵节点已保存
        int pivot = arr[low];
        int i = low, j = high;
        while (i < j) {
            // 哨兵在最左，已保存，左边位置空出，所以先从右向左扫描
            while (i < j && arr[j] > pivot) j--;
            if (i >= j) {
                break;
            }
            // 将右点保存在左边，左指针右移
            arr[i++] = arr[j];
            // 右边那个点已换到左边，位置空出，所以从左向右扫描
            while (i < j && arr[i] < pivot) i++;
            if (i >= j) {
                break;
            }
            // 将左点保存到右边，右指针左移
            arr[j--] = arr[i];
        }
        // 循环退出后 i==j，将哨兵节点放在合适位置
        arr[i] = pivot;
        return i;
    }

    private static int partition2(int[] arr, int low, int high) {
        // 哨兵节点
        int pivot = arr[low];
        int i = low, j = high + 1;
        while (true) {
            // i 右移
            while (arr[++i] < pivot) {
                // 递减数组，直接退出
                if (i == high) break;
            }
            // j 左移
            while (arr[--j] > pivot) {
                // 正序数组，j 退回到0
                if (j == low) break;
            }
            if (i >= j) break;
            // 交换两个不合适的数字
            swap(arr, i, j);
        }
        // 把哨兵放在合适位置
        // 至于这里是 j 还是 i，假设是个顺序数组，那么i在第二个位置停下
        // j会从最右边一直向左走到0，此时只能交换0 0，才能保证数组不改变
        swap(arr, low, j);
        return j;
    }


    /**
     * 交换数组指定位置两个节点
     * @param arr
     * @param i
     * @param j
     */
    public static void swap(int[] arr, int i, int j) {
        if (i == j) return;
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    /**
     * 二分查找
     * @param arr
     * @param target
     * @return
     */
    public static int binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        if (arr.length == 1) {
            return arr[0] == target ? 0 : -1;
        }
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] < target) {
                low = mid + 1;
            } else if (arr[mid] > target) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(-1 % 2); // -1
        // int[] arr = {12,3243,53,33,76,7,1,54};
        int[] arr = {12,9,8,7,6,5};
        // Arrays.sort(arr); // 1 7 12 33 53 54 76 3243
        quickSort(arr);
        System.out.println(binarySearch(arr, 100));
        System.out.println(binarySearch(arr, -1));
        System.out.println(binarySearch(arr, 3245));
        System.out.println(binarySearch(arr, 53));

    }
}
