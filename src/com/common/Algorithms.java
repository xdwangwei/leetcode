package com.common;

import java.util.Arrays;
import java.util.Random;

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
        int mid = partition2(arr, low, high);
        // int mid = partition(arr, low, high);
        quickSort(arr, low, mid - 1);
        quickSort(arr, mid + 1, high);
    }

    /**
     * 这种partition的方式是 每次去扫描空位对面位置那个指针，遇到一个不合适元素就把它挪到对面的空位上，
     * 特点是 ： 遇到一个 处理一个，不管是while循环内部，还是while循环退出后，return之前，都是把另一个值放到某个位置，是单个元素的处理，而不是两个元素的交换
     */
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

    /**
     * 这种partition相当于一次性找到左右两边各一个不合适元素，直接将二者交换，就一次性都放置到正确位置了
     * 所以不管是while内部，还是while退出后，return之前，都是 两两交换，不是单个元素的赋值
     * 但是有个问题：这种写法，while内部，i和j移动的判断条件中，必须是 nums[i] <= pivot 和 nums[j] >= pivot，这个 = 必不可少
     * 否则，若没有=，若 此时 nums[i] 和 nums[j] 都等于 pivot，那么 两个指针都停下，然后会进行 swap，交换完之后，二者还是相等的，
     * 那么，进入下一次while，然后两个指针的判断条件都又不成立，然后都停下，又进行swap，于是陷入死循环
     * 也就是说不加=，可能出现双指针都不移动的情况。
     * 当然，也可以在for循环内部，对这种情况进行特殊处理。保证至少有一个指针在移动。
     *   while () {
     while() j--
     while() i++
     if (i >= j) break
     swap(i, j)
     // 特殊处理
     if(nums[i] == pivot) j--
     if(nums[j] == pivot) i++
     }
     * 而第一种partition，遇到一个不合适的就处理，每一次都会有一个指针在移动，所以不用考虑这种情况。

     * 但是二者有一个共同点是：若选择左边第一个元素作为枢轴元素，那么while内先进行右指针的递减扫描，再进行左指针的递增扫描
     */
    private static int partition2(int[] arr, int start, int end) {
        // 以左边第一个元素作为哨兵
        int pivot = arr[start];
        int i = start, j = end;
        while (i < j) {
            // 左边第一个元素已保存至pivot，此时我们先从右扫描，找到右边第一个不合适元素
            while (i < j && arr[j] >= pivot) {
                j--;
            }
            // 找到左边第一个不合适元素
            while (i < j && arr[i] <= pivot) {
                i++;
            }
            // 一次交换将这两个元素都放置到正确位置上
            if (i < j) {
                swap(arr, i, j);
            }
        }
        // 结束时，i == j，此时已经完成[start:i]都<=pivot,[i:end]都>=pivot
        // 但是pivot还没放放置到合适位置，需要再完成一次交换，因为nums[i]已经<=pivot了，所以直接交换这两个不会破坏
        swap(arr, start, i);
        return i;
    }


    /**
     * 第三种partition，也是一对交换
     * 枢轴元素取最左，但还是从左指针开始扫描，注意 是 先 自增i，再比较(最左边元素已保存，不用比较)，也就是 arr[++i] < pivot
     *                 那么对应 j 指针 就是 arr[--j] > pivot，但是刚开始时 j = j = high + 1，这样保证 先减一后j就是最右边元素，否则会被漏掉
     *
     * 注意退出条件 和 最后 的交换
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private static int partition3(int[] arr, int low, int high) {
        // 哨兵节点
        int pivot = arr[low];
        int i = low, j = high + 1;
        // 注意退出条件
        while (true) {
            // i 右移
            // 保证 nums[lo..i] 都小于 pivot
            while (arr[++i] < pivot) {
                // 递减数组，直接退出
                if (i == high) break;
            }
            // j 左移
            // 保证 nums[j..hi] 都大于 pivot
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

    Random random = new Random();

    /**
     *
     * 快速排序，partition过程
     * 以nums[right]为pivot，返回j，保证 nums[left, j] 都 < pivot=nums[right]
     * @param nums
     * @param low
     * @param high
     * @return
     */
    private int randomPartition(int[] nums, int low, int high) {
        if (low == high) return low;
        // 避免最坏时间复杂度，先随机选择一个位置和 预定的pivot元素位置交换
        int randIndex = low + random.nextInt(high - low + 1);
        swap(nums, randIndex, high);

        // 选取最右边元素为pivot，j用于顺序扫描当前范围内的全部元素
        // i 用于保证 nums[low, i]位置元素都 满足 <= pivot
        // 也就是 每一次 nums[j] < pivot ，就把nums[j]交换到i位置去，保证<pivot的元素全部放置在左边部分
        int pivot = nums[high], i = low - 1, j = low;
        while (j <= high) {
            // 每一次 nums[j] < pivot ，就把nums[j]交换到i位置去，保证<pivot的元素全部放置在左边部分
            if (nums[j] < pivot) {
                swap(nums, ++i, j);
            }
            ++j;
        }
        // 把pivot放置在正确位置
        swap(nums, ++i, high);
        // nums[low, i]位置 都满足 <= pivot
        return i;
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
        int[] arr1 = {7, 1, -1, 9, 28, 5, 63, 0, -23, 7, 28, 12, 4, 97, 8, -9};
        quickSort(arr1);
        Arrays.stream(arr1).forEach(System.out::println);

        System.out.println(binarySearch(arr, 100));
        System.out.println(binarySearch(arr, -1));
        System.out.println(binarySearch(arr, 3245));
        System.out.println(binarySearch(arr, 53));

    }
}
