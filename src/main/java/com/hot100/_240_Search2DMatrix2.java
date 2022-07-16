package com.hot100;

/**
 * @author wangwei
 * @date 2022/4/21 16:55
 * @description: _240_Search2DMatrix2
 *
 * 240. 搜索二维矩阵 II
 * 编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target 。该矩阵具有以下特性：
 *
 * 每行的元素从左到右升序排列。
 * 每列的元素从上到下升序排列。
 *
 *
 * 示例 1：
 *
 *
 * 输入：matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
 * 输出：true
 * 示例 2：
 *
 *
 * 输入：matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 20
 * 输出：false
 *
 *
 * 提示：
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= n, m <= 300
 * -109 <= matrix[i][j] <= 109
 * 每行的所有元素从左到右升序排列
 * 每列的所有元素从上到下升序排列
 * -109 <= target <= 109
 */
public class _240_Search2DMatrix2 {

    /**
     * 方法一：自己想的又复杂又垃圾的二分查找嵌套二分查找
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        return searchMatrix(matrix, 0, matrix.length - 1, target);
    }

    /**
     * 在二维数组matrix的指定范围行数内matrix第minR行，到第maxR行内 查找目标元素target
     * @param matrix
     * @param minR
     * @param maxR
     * @param target
     * @return
     */
    private boolean searchMatrix(int[][] matrix, int minR, int maxR, int target) {
        // 无效范围
        if (minR > maxR) {
            return false;
        }
        // 外围的二分搜索用来判断目标元素应该在当前行上面还是下面部分
        // 由于【第一列】元素是增序，所以可以二分
        int n = matrix[0].length;
        // 使用左包右不包形式的二分搜搜
        int lo = minR, hi = maxR + 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            // 找到
            if (target == matrix[mid][0]) {
                return true;
            }
            // 小于当前行第一列的元素只可能位于 当前行上方
            if (target < matrix[mid][0]) {
                hi = mid;
            } else {
                // 大于当前行第一列元素
                // 如果大于当前行最后一列元素，那么只能在当前行下方部分
                if (target > matrix[mid][n - 1]) {
                    lo = mid + 1;
                    continue;
                }
                // 否则，比 当前行 第一个元素 大， 比 当前行最后一个元素小 ，
                // 【！！！！】【这里有三种情况，改了半小时bug】
                // 这样的元素 可能在 当前行
                // 也可能在 当前行 上方，【好好想想，比如上一行最后一个元素肯定比当前行最后一个小，但同时可以比当前行第一个元素大】
                // 也可能在 当前行 下方  【好好想想，比如下一行第一个元素肯定比当前行第一个大，但同时可以比当前行最后一个元素小】
                // 先看看当前行有没有
                int l = 1, h = n;
                while (l < h) {
                    int mid2 = l + (h - l) / 2;
                    if (target == matrix[mid][mid2]) {
                        return true;
                    } else if (target > matrix[mid][mid2]) {
                        l = mid2 + 1;
                    } else {
                        h = mid2;
                    }
                }
                // 如果没有，那么再分别去上下部分搜索，相当于只排除了一行，垃圾设计
                return searchMatrix(matrix, minR, mid - 1, target) || searchMatrix(matrix, mid + 1, maxR, target);
            }
        }
        return false;
    }


    /**
     * 方法二：最简单的暴力搜索，没想到，傻逼，逐个判断不就完了
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix2(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (matrix[i][j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 最简单的二分搜索，没想到，傻逼，非要搞成方法一那么复杂
     * 不是每一行都是递增有序吗？去每一行进行一次二分搜索不久好了
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix3(int[][] matrix, int target) {
        // 逐行二分搜索
        for (int[] nums : matrix) {
            // 某一行找到了
            if (binarySearch(nums, target) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对有序数组nums进行二分搜索target
     * @param nums
     * @param target
     * @return
     */
    private int binarySearch(int[] nums, int target) {
        int l = 0, h = nums.length;
        while (l < h) {
            int m = l + (h - l) / 2;
            if (nums[m] < target) {
                l = m + 1;
            } else if (nums[m] > target) {
                h = m;
            } else {
                return m;
            }
        }
        return -1;
    }

    /**
     * 方法三：Z 字形遍历，傻逼，肯定想不到
     * 我们可以从矩阵 matrix 的右上角 (0,n−1) 进行搜索。比他大的肯定在下一行，比他小的肯定在它左边和上边，但是因为是从第一行开始的，所以它上边肯定搜索过，只需考虑它左边，那么每次不是左边就是下边，多简单
     * 在每一步的搜索过程中，如果我们位于位置 (x,y)，那么我们希望在以 matrix 的左下角为左下角、以 (x,y) 为右上角的矩阵中进行搜索，
     * 即行的范围为 [x,m−1]，列的范围为 [0, y][0,y]：
     *
     * 如果 matrix[x,y]=target，说明搜索完成；
     *
     * 如果 matrix[x,y]>target，由于每一列的元素都是升序排列的，
     * 那么在当前的搜索矩阵中，所有位于第 y 列的元素都是严格大于target 的，因此我们可以将它们全部忽略，即将 y 减少 1；
     *
     * 如果 matrix[x,y]<target，由于每一行的元素都是升序排列的，
     * 那么在当前的搜索矩阵中，所有位于第 x 行的元素都是严格小于 target 的，因此我们可以将它们全部忽略，即将 x 增加 1。
     *
     * 在搜索的过程中，如果我们超出了矩阵的边界，
     * 那么说明矩阵中不存在 \textit{target}target。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/search-a-2d-matrix-ii/solution/sou-suo-er-wei-ju-zhen-ii-by-leetcode-so-9hcx/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix4(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        // 右上角开始
        int x = 0, y = n - 1;
        while (x != m && y != -1) {
            // 找到了
            if (matrix[x][y] == target) {
                return true;
                // 比它小的肯定在它左边，（上面部分已搜索过）
            } else if (matrix[x][y] > target) {
                y--;
                // 比它大的一定在它下面
            } else {
                x++;
            }
        }
        return false;
    }
}
