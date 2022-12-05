package com.common;

import java.util.Arrays;
import java.util.Random;

/**
 * @author wangwei
 * 2021/7/8 21:26
 */
public class Algorithms {

    static Random random = new Random();

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


    /**
     * 三路归并快速排序
     * @param arr
     */
    public static void threeWayQuickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        threeWayQuickSort(arr, 0, arr.length - 1);
    }

    /**
     * 快速排序
     * @param arr
     * @param low
     * @param high
     */
    private static void quickSort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        // int mid = partition2(arr, low, high);
        // int mid = partition(arr, low, high);
        int mid = randomPartition(arr, low, high);
        quickSort(arr, low, mid - 1);     // <= pivot 区域
        quickSort(arr, mid + 1, high);     // > pivot 区域
    }

    /**
     * 三路归并快速排序
     * @param arr
     * @param low
     * @param high
     */
    private static void threeWayQuickSort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        int[] p = randomThreeWayPartition(arr, low, high);
        // [ p0....p1 ]   ==  pivot
        threeWayQuickSort(arr, low, p[0] - 1);   // < pivot 区域
        threeWayQuickSort(arr, p[1] + 1, high);   // > pivot 区域
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

    /**
     *
     * 快速排序，partition过程，返回 p
     * 从nums[left...right]范围内随机取一个数字作为 pivot
     * 目标：保证 nums[left, p] 都 <= pivot
     *          nums[p + 1, right] 都 > pivot
     *
     * 算法：选择 nums[right] 作为 pivot，去处理nums[left, right-1]，再把pivot放置到中间合适位置
     * 假设我们已经有一个分割点是 p，那么 nums[left,p] 都满足 <= pivot，那么p 之后的位置上的元素都必须大于 pivot
     *
     * 若我们发现p之后某个位置j上出现元素x <= pivot 的情况一，需要将这个 x 划分到左半区域去：
     *      交换 位置j上元素x(这个元素是 <= pivot的) 和 位置 p+1 上元素(这个元素一定是 > pivot 的)
     *      更新 p 为 p + 1；就做到了 x 划分到了 p 左边区域，并保证了 (p+1, j] 位置都是 > pivot 的元素
     *      j 继续前进；直到 j == right 退出，
     *      将 nums right 位置 和 p + 1 位置 元素交换；
     *      p 更新为 p + 1
     *      返回 p + 1
     *
     *      其实，这里的判断条件可以是 如果 x < pivot就进行上述操作，
     *          如果判断条件是 x <= pivot，对于中间部分和pivot一样的数字，也会进行上述过程，最后返回p代表最后一个等于pivot的位置
     *          如果判断条件是 x < pivot，对于中间部分和pivot一样的数字，不会进行上述过程，最后返回p代表第一个等于pivot的位置
     *          因为不管这里是否判断等号，while退出后都会交换p+1位置和right位置，并更新p为p+1，那么 [left, p]一定还是 <= pivot的
     *          假如pivot=3，如果这里不判断等号，最终[2333689]会返回1（第一个3）；如果判断等号，最后会返回3（最后一个3）
     *
     *
     * 情况二就是 j 位置元素 > pivot，那么 j 直接前进即可
     *
     * 初始时让 p 为 left - 1，代表 [...p] 范围 都 <= pivot，然后 让 j 从 left 开始扫描到 right - 1，最后 把pivot放入合适位置
     *
     *
     * 为什么是j前进到right退出，不是j超出right退出呢？
     * 因为 right位置元素本身就拿来做 pivot 了，它肯定是满足 <= pivot的，
     * 如果当j==right时进行判断，nums[j] <= pivot成立，会把 它 和 p + 1 位置元素交换，再更新p为p+1
     * 这一步没问题，但是while结束后还要进行swap(++p, right)
     * 如果刚才交换过来的 arr[p+1]==pivot，那也没问题，但如果交换过来的 arr[p+1]>pivot，那么此时应该返回p-1，因为p位置元素已经大于预定pivot了
     *
     * 所以，为了一致，我们j遍历到right位置就退出！！！
     *
     *
     * 【模板】
     *
     * 所以建议 记住这个模板：划分目标是 nums[left, p] 都 <= pivot ：统一操作：
     *      1. 选择 nums[right] 作为 pivot，
     *      2. 让 p 初始化为 left - 1，代表 nums[...p] 范围 都 <= pivot
     *      3. 让 j 从 left 开始扫描到 right - 1
     *              如果 nums[j] > pivot：j继续前进
     *              否则：（nums[j] < pivot） 如果pivot=3，如果这里不判断等号，最终[2333689]会返回1（第一个3）；如果判断等号，最后会返回3（最后一个3）
     *                  swap(nums, p + 1, j); p = p + 1; j继续前进，简写为 swap(nums, ++p, j); j++;
     *      4. 放置pivot到正确位置
     *              swap(nums, ++p, high);
     *      5. 返回 p
     *
     * 【优化】
     * 为了避免数组本身有序情况下导致partition效率低下，我们初始时随机选择一个位置和right位置进行交换，再让right位置作为pivot进行上述过程！
     * @param nums
     * @param low
     * @param high
     * @return
     */
    private static int randomPartition(int[] nums, int low, int high) {
        if (low == high) {
            return low;
        }
        // 先随机选择一个位置和right位置进行交换，
        int idx = (int) (low + Math.random() * (high - low + 1));
        swap(nums, idx, high);
        // 1. 再让right位置作为pivot进行划分！
        int pivot = nums[high];
        // 2. 让 p 初始化为 left - 1，代表 nums[...p] 范围 都 <= pivot
        // p 之后的位置上的元素都必须大于 pivot
        int p = low - 1;
        // 3. 遍历 p 之后 位置上的元素，直到 high - 1 位置
        for (int j = low; j < high; ++j) {
            // 如果它的确 > pivot，遍历下一个
            // 如果它 <= pivot，它需要划分到p左边去
            if (nums[j] <= pivot) {
                // 交换 p + 1 位置 和 j 位置
                // p 更新 为 p + 1
                // j 前进
                swap(nums, ++p, j);
            }
        }
        // 4. 将 high 位置 的pivot元素放置在 合适位置(p+1)，p 更新 为 p + 1
        swap(nums, ++p, high);
        // 5. 返回 p
        return p;
    }


    /**
     * 上面的partition都是分两半
     * 这里使用三路归并快速排序思想，将 nums[low...high] 划分为 三部分：
     *      [low...p0 - 1]     <   nums
     *      [ po....p1 ]       ==  nums
     *      [p1 + 1...high]    >   nums
     *
     * 思路和上面类似：
     *      1. 仍然选择 high 位置元素为  pivot，对 [low...right-1] 位置元素进行处理，将 pivot 放置到合适位置
     *      2. 初始化 p0 为 low - 1，代表 p0 左边元素 都 < pivot
     *         初始化 p1 为 (right-1) + 1，代表 p1 右边元素 都 > pivot
     *         二者中间(j扫描过的区域)为 == pivot的元素
     *      3. 让 j 从 low 开始扫描，当 j 与 p1 重合时停止(p1及右边已经属于处理过的位置了)，在此过程中：
     *          nums[j] < pivot:
     *              swap(nums, p0 + 1, j);  p0++; j++;
     *          nums[j] == pivot:
     *              j++;
     *          nums[j] > pivot:
     *              swap(nums, p1 - 1, j); p1--; j不变
     *              因为 j 是从左往右扫描的：它能保证的是它及其左边的元素都是<=pivot的，但当你把一个右边未访问过的区域的数字交换到当前位置后
     *              你不能直接前进j，你需要对此位置再进行一次判断；
     *              在第一种情况中，当前把左边一个数字交换到当前位置时，因为这个数字是已经访问处理过的，它一定是满足我划分区域规则的，所以j直接前进
     *      4. 最后，把 high 位置pivot 放入合适位置：
     *          swap(nums, p1, high); p1++;
     *          因为 pivot 本身肯定是属于 == pivot区间的，所以它应该放置在中间区间，从 p1 开始是 > pivot 的区间，所以把它和 p1 位置交换，再 更新p1自增
     *      5. 返回 p0, p1
     *
     * 同样，为了避免最坏情况，先随机选一个位置和high位置元素交换，再进行上述过程
     *
     * @param nums
     * @param low
     * @param high
     * @return
     */
    private static int[] randomThreeWayPartition(int[] nums, int low, int high) {
        if (low == high) {
            return new int[]{low, low};
        }
        // 先随机选择一个位置和right位置进行交换，
        int idx = (int) (low + Math.random() * (high - low + 1));
        swap(nums, idx, high);
        // 1. 再让right位置作为pivot进行划分！
        int pivot = nums[high];
        //    处理 [low, high-1]
        // 2. 初始化 p0 为 low - 1，代表 p0 左边元素 都 < pivot
        //    初始化 p1 为 (right-1) + 1 = right，代表 p1 右边元素 都 > pivot
        //    二者中间(j扫描过的区域)为 == pivot的元素
        int p0 = low - 1, p1 = high;
        // 3. 遍历 [low, p1)  位置上的元素
        //    p1 及之后都是 > pivot 的元素，不用 再访问
        for (int j = low; j < p1; ) {
            // 它应该属于 p1及右边区域
            if (nums[j] > pivot) {
                // 交换 p1 - 1 位置 和 j 位置
                // p1 更新 为 p1 - 1
                // j 不动(交换过来一个未访问过的区域的数字，需要处理，不能直接跳过)
                swap(nums, --p1, j);
            }
            // 它应该属于 p0及左边区域
            else if (nums[j] < pivot) {
                // 交换 p0 + 1 位置 和 j 位置
                // p0 更新 为 p + 1
                // j 前进（交换过来一个已经处理过的数字，一定满足规则，继续前进）
                swap(nums, ++p0, j++);
            } else {
                // 满足 == pivot，直接前进
                j++;
            }
        }
        // 4. 将 high 位置 的pivot元素放置在 合适位置
        //    while结束后， [low...p0]          <    pivot
        //                 [p0 + 1...p1 - 1]   =    pivot
        //                 [p1...high-1]       >    pivot
        //    将pivot放置在 p1 位置，让 p1 右移
        swap(nums, p1++, high);
        // 5. 返回 p
        // 返回目标是 [ p0....p1 ]   ==  pivot，这里做简要调整
        return new int[]{p0 + 1, p1 - 1};
        // 上面两行简化后就是
        // swap(nums, p1, high);   return new int[]{p0 + 1, p1};
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
     * 二分查找 左闭右闭写法
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

    /**
     * 二分查找，左闭右开写法
     * @param arr
     * @param target
     * @return
     */
    public static int binarySearch2(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int low = 0, high = arr.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] < target) {
                low = mid + 1;
            } else if (arr[mid] > target) {
                high = mid;
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
