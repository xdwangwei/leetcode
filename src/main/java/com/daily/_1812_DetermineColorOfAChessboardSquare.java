package com.daily;

/**
 * @author wangwei
 * @date 2022/12/8 18:13
 * @description: _1812_DetermineColorOfAChessboardSquare
 *
 * 1812. 判断国际象棋棋盘中一个格子的颜色
 * 给你一个坐标 coordinates ，它是一个字符串，表示国际象棋棋盘中一个格子的坐标。下图是国际象棋棋盘示意图。
 *
 *
 *
 * 如果所给格子的颜色是白色，请你返回 true，如果是黑色，请返回 false 。
 *
 * 给定坐标一定代表国际象棋棋盘上一个存在的格子。坐标第一个字符是字母，第二个字符是数字。
 *
 *
 *
 * 示例 1：
 *
 * 输入：coordinates = "a1"
 * 输出：false
 * 解释：如上图棋盘所示，"a1" 坐标的格子是黑色的，所以返回 false 。
 * 示例 2：
 *
 * 输入：coordinates = "h3"
 * 输出：true
 * 解释：如上图棋盘所示，"h3" 坐标的格子是白色的，所以返回 true 。
 * 示例 3：
 *
 * 输入：coordinates = "c7"
 * 输出：false
 *
 *
 * 提示：
 *
 * coordinates.length == 2
 * 'a' <= coordinates[0] <= 'h'
 * '1' <= coordinates[1] <= '8'
 * 通过次数32,186提交次数39,258
 */
public class _1812_DetermineColorOfAChessboardSquare {


    /**
     * 给定坐标一定代表国际象棋棋盘上一个存在的格子。
     * 坐标第一个字符是字母，代表所在列，取值 'a' - 'h'。减去 'a' 后取值为， 0，1，2，3.。。。
     * 第二个字符是数字，代表所在行，取值 '1' - '8'。减去 '1' 后取值为 0，1，2，。。。。
     *
     * 观察所给实例：
     *  黑色格子的横纵坐标 奇偶性一致
     *  白色格子的横纵坐标 奇偶性相反
     * @param coordinates
     * @return
     */
    public boolean squareIsWhite(String coordinates) {
        // 纵坐标奇偶性
        int col = ((coordinates.charAt(0) - 'a') & 1);
        // 横坐标奇偶性
        int row = ((coordinates.charAt(1) - '1') & 1);
        // 白色格子，横纵坐标奇偶性相反
        return (col ^ row) != 0;
    }
}
