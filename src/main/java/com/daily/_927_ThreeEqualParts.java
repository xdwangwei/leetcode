package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/10/6 16:14
 * @description: _927_ThreeEqualParts
 *
 * 927. 三等分
 * 给定一个由 0 和 1 组成的数组 arr ，将数组分成  3 个非空的部分 ，使得所有这些部分表示相同的二进制值。
 *
 * 如果可以做到，请返回任何 [i, j]，其中 i+1 < j，这样一来：
 *
 * arr[0], arr[1], ..., arr[i] 为第一部分；
 * arr[i + 1], arr[i + 2], ..., arr[j - 1] 为第二部分；
 * arr[j], arr[j + 1], ..., arr[arr.length - 1] 为第三部分。
 * 这三个部分所表示的二进制值相等。
 * 如果无法做到，就返回 [-1, -1]。
 *
 * 注意，在考虑每个部分所表示的二进制时，应当将其看作一个整体。例如，[1,1,0] 表示十进制中的 6，而不会是 3。此外，前导零也是被允许的，所以 [0,1,1] 和 [1,1] 表示相同的值。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr = [1,0,1,0,1]
 * 输出：[0,3]
 * 示例 2：
 *
 * 输入：arr = [1,1,0,1,1]
 * 输出：[-1,-1]
 * 示例 3:
 *
 * 输入：arr = [1,1,0,0,1]
 * 输出：[0,2]
 *
 *
 * 提示：
 *
 * 3 <= arr.length <= 3 * 10^4
 * arr[i] 是 0 或 1
 */
public class _927_ThreeEqualParts {

    /**
     *
     * 根据题目描述，我们可以知道要将arr数组分成3份，并且对于这3份由0和1组成的二进制值要相同。
     * 因为是二进制，由0和1组成，所以，如果二进制相同的话，那么这3个分组中，1的个数一定是相同的。
     *
     * cnt 必须是 3 的倍数，否则无法将数组三等分，可以提前返回 [−1,−1]。如果 cnt 为 0，那么意味着数组中所有元素都为 0，直接返回 [0,n−1] 即可。
     * 我们将 cnt 除以 3，得到每一份中 1 的数量，然后找到每一份的第一个 1 在数组 arr 中的位置，分别记为 i, j, k。
     *
     *
     * 0 1 1 0 0 0 1 1 0 0 0 0 0 1 1 0 0
     *   ^         ^             ^
     *   i         j             k
     *
     * “1”我们校验完毕之后，我们再来看看“0”。
     * 对于二进制俩说，每个二进制数字第一个1前面的0是不影响结果的，我们要考虑的是第一个1后面的1和0的个数以及位置
     *
     * 所以我们从 i, j, k 开始往后同时遍历每一部分，判断三部分对应的值是否相等，是则继续遍历，直至 k 到达 arr 末尾。
     *
     *
     * 0 1 1 0 0 0 1 1 0 0 0 0 0 1 1 0 0
     *           ^         ^             ^
     *           i         j             k
     * 遍历结束时，若 k=n，说明满足三等分，返回此时的 [i−1,j] 作为答案，否则返回 [−1,−1]。
     *
     * 时间复杂度 O(n)，空间复杂度 O(1)。其中 nn 表示 arr 的长度。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/three-equal-parts/solution/by-lcbin-7ir1/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr
     * @return
     */
    public int[] threeEqualParts(int[] arr) {
        int n = arr.length;
        // 因为只有1和0，所以求1的个数和求和是一样的
        int sum = Arrays.stream(arr).sum();
        // 数组元素全是0，
        if (sum == 0) {
            return new int[]{0, n - 1};
        }
        // 数组元素中1的无法分为三部分
        if (sum % 3 != 0) {
            return new int[]{-1, -1};
        }
        int part = sum / 3, i = 0, j = 0, k = 0, cur = 0;
        for (int v = 0; v < n; ++v) {
            if (arr[v] == 1) {
                // 第一个1的位置，注意cur=0表示第一个1的位置，所以cur=part表示第part+1个位置
                if (cur == 0) {
                    i = v;
                    // 第二组中第一个1的位置，也就是第1+part个1的位置
                } else if (cur == part) {
                    j = v;
                    // 第三组中第一个1的位置，也就是第1+2*part个1的位置
                } else if (cur == part * 2) {
                    k = v;
                }
                cur++;
            }
        }
        // 每部分第一个1及之后的数字应该都相同
        while (k < n && arr[i] == arr[j] && arr[j] == arr[k]) {
            i++;
            j++;
            k++;
        }
        // 顺利遍历到末尾时，i 指向了 第二组第一个元素，j 指向了 第三组第一个元素，按照返回要求，[i-1, j]
        // 否则，肯定存在，12组或23组第1个后面某个位置数字不等
        return k == n ? new int[]{i - 1, j} : new int[]{-1, -1};
    }

    public static void main(String[] args) {
        _927_ThreeEqualParts obj = new _927_ThreeEqualParts();
        System.out.println(obj.threeEqualParts(new int[]{1, 0, 1, 0, 1}));
    }
}
