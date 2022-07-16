package com.order;


/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/22 周五 23:39
 *
 * 4. 寻找两个正序数组的中位数
 * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。
 *
 * 算法的时间复杂度应该为 O(log (m+n)) 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums1 = [1,3], nums2 = [2]
 * 输出：2.00000
 * 解释：合并数组 = [1,2,3] ，中位数 2
 * 示例 2：
 *
 * 输入：nums1 = [1,2], nums2 = [3,4]
 * 输出：2.50000
 * 解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5
 *
 *
 *
 *
 * 提示：
 *
 * nums1.length == m
 * nums2.length == n
 * 0 <= m <= 1000
 * 0 <= n <= 1000
 * 1 <= m + n <= 2000
 * -106 <= nums1[i], nums2[i] <= 106
 **/
public class _04_MedianOfTwoSortedArray {

    /**
     * 归并 + 选择 O(m + n)
     * 二分法排除 O(log(m + n))
     * 中位数概念 + 二分搜索 O(min(m, n))
     *
     * 官方题解
     * https://leetcode-cn.com/problems/median-of-two-sorted-arrays/solution/xun-zhao-liang-ge-you-xu-shu-zu-de-zhong-wei-s-114/
     * @param nums1
     * @param nums2
     * @return
     */

    // 在统计中，中位数被用来：
    // 将一个集合划分为两个长度相等的子集，其中一个子集中的元素总是大于另一个子集中的元素
    // 让我们在任一位置 i 将 A 划分成两个部分 A[0],A[1]...A[i-1]  A[i],A[i+1],...A[m-1]
    // 同样, 在任一位置 j 将 B 划分成两个部分 B[0],B[1]...B[j-1]  B[j],B[j+1],...B[n-1]
    // 将 left_A 和 left_B放入一个集合，并将 right_A 和 right_B 放入另一个集合。
    // 如果我们可以确认：len(left_part)=len(right_part)  max(left_part)≤min(right_part)
    // 那么我们已经将 {A,B} 中的所有元素划分为相同长度的两个部分，且其中一部分中的元素总是大于另一部分中的元素
    // median = max(left_part)+min(right_part) / 2 左边最右面那个和右边最左面那个的均值
    // 要确保这两个条件，我们只需要保证：
    // 1. B[j−1]≤A[i] 以及 A[i−1]≤B[j](因为B[j-1]<B[j], A[i-1]<A[i], 数组有序)
    // 2. i + j == m-i + n-j (左边元素个数 == 右边)
    // 所以能通过 i 得到 j, 因此只需要在 A 里面找这个合适的 i 
    // 我们偏向于考虑左半部分, 如果AB某个长为奇数，为了让中间那个元素处于左边，让 i+j = m+n+1
    // 这样一般长就是 (m+n+1)/2，当原来右边长是偶数时，并未有影响(触发取整),为奇数时让左半边偏长
    // j = (m+n+1)/2, 0<=i<=m，为了让 j>0 ,我们让 n >= m，否则把两个数组交换一下

    public static double solution1(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            int[] temp = nums1;
            nums1 = nums2;
            nums2 = temp;
        } // 保证在 n >= m的情况下进行考虑
        int m = nums1.length;
        int n = nums2.length;
        int halfLen = (m + n + 1) / 2;
        int iMin = 0, iMax = m;
        while (iMin <= iMax) { // 采用二分法来找 i 的合适位置
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;
            // 不合适情况(不满足A[i-1] <= B[j] && B[j-1] <= A[j])
            if (i < iMax && nums2[j - 1] > nums1[i]) //排除特殊情况 i=m
                iMin = i + 1; // 调整左边界，这样 i 就会增加 , j 就会减小
            else if (i > iMin && nums1[i - 1] > nums2[j])//排除特殊情况 i=0
                iMax = i - 1;  // 调整右边界，这样 i 就会减小 , j 就会增加
            else { // 找到了合适的i以及临界情况
                int maxLeft = 0;
                if (i == 0) maxLeft = nums2[j - 1];// A的左边部分为空，B的左边最后一个就是左边总和的最后一个
                else if (j == 0) maxLeft = nums1[i - 1];// B的左边为空
                else maxLeft = Math.max(nums1[i - 1], nums2[j - 1]);
                if ((m + n) % 2 == 1) //因为我们考虑偏向左边,所以总和为奇数时，中位数一定是左边最后一个元素
                    return maxLeft;
                // 否则，就是左边最后一个和右边第一个的平均数
                int minRight = 0;
                if (i == m) minRight = nums2[j]; //A的右半部分为空，B的右边第一个就是右边总和的第一个
                else if (j == n) minRight = nums1[i]; //B的右边为空
                else minRight = Math.min(nums1[i], nums2[j]);
                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }

    // 下面这个偏向右边考虑，不用给m+n+1，当m+n为奇数时，中位数一定在右边第一个
    // 如 1,2,3，halflen = 3/2 = 1，所以分割AB左半部分,最多到1，中位数2一定在右边第一个
    public static double solution2(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        if (m > n) { // to ensure m<=n
            int[] temp = A;
            A = B;
            B = temp;
            int tmp = m;
            m = n;
            n = tmp;
        }
        int iMin = 0, iMax = m, halfLen = (m + n) / 2;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;
            if (i < iMax && B[j - 1] > A[i]) {
                iMin = i + 1; // i is too small
            } else if (i > iMin && A[i - 1] > B[j]) {
                iMax = i - 1; // i is too big
            } else { // i is perfect
                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }
                //因为我们考虑偏向右边,所以总和为奇数时，中位数一定是右边第一个元素
                if ((m + n) % 2 == 1) {
                    return minRight;
                }
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }

                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }

    /**
     * 先归并，再去中位数 O(m+n),不合题意O(log(m+n))
     * <p>
     * 也可以双指针
     * 不需要合并两个有序数组，只要找到中位数的位置即可。
     * 由于两个数组的长度已知，因此中位数对应的两个数组的下标之和也是已知的。
     * 维护两个指针，初始时分别指向两个数组的下标 0 的位置，每次将指向较小值的指针后移一位（如果一个指针已经到达数组末尾，则只需要移动另一个数组的指针），
     * 直到到达中位数的位置。
     * <p>
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays/solution/xun-zhao-liang-ge-you-xu-shu-zu-de-zhong-wei-s-114/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public static double solution3(int[] nums1, int[] nums2) {
        int[] merge = merge(nums1, nums2);
        int len = merge.length;
        if (len % 2 == 0) {
            return (merge[len / 2 - 1] + merge[len / 2]) / 2.0; // 最中间两个均值
        } else
            return merge[len / 2]; // 最中间的数
    }

    public static int[] merge(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int[] res = new int[len1 + len2];
        int i = 0, j = 0, k = 0;
        while (i < len1 && j < len2) {
            if (nums1[i] < nums2[j])
                res[k++] = nums1[i++];
            else
                res[k++] = nums2[j++];
        }
        while (i < len1) res[k++] = nums1[i++];
        while (j < len2) res[k++] = nums2[j++];
        return res;
    }

    public static void main(String[] args) {
        System.out.println(solution1(new int[]{1, 3}, new int[]{2}));
        System.out.println(solution1(new int[]{1, 2}, new int[]{3, 4}));
        System.out.println(solution2(new int[]{1, 3}, new int[]{2}));
        System.out.println(solution2(new int[]{1, 2}, new int[]{3, 4}));
        System.out.println(solution3(new int[]{1, 3}, new int[]{2}));
        System.out.println(solution3(new int[]{1, 2}, new int[]{3, 4}));
    }
}
