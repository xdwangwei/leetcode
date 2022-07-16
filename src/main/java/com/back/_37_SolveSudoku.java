package com.back;

/**
 * @author wangwei
 * 2020/4/5 10:07
 * <p>
 * 编写一个程序，通过已填充的空格来解决数独问题。
 * <p>
 * 一个数独的解法需遵循如下规则：
 * <p>
 * 数字1-9在每一行只能出现一次。
 * 数字1-9在每一列只能出现一次。
 * 数字1-9在每一个以粗实线分隔的3x3宫内只能出现一次。
 * 空白格用'.'表示
 * <p>
 * 给定的数独序列只包含数字 1-9 和字符 '.' 。
 * 你可以假设给定的数独只有唯一解。
 * 给定数独永远是 9x9 形式的
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/sudoku-solver
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * 威胁你公众号：
 * 回溯(全排列，N皇后)：https://mp.weixin.qq.com/s?__biz=MzAxODQxMDM0Mw==&mid=2247484709&idx=1&sn=1c24a5c41a5a255000532e83f38f2ce4&chksm=9bd7fb2daca0723be888b30345e2c5e64649fc31a00b05c27a0843f349e2dd9363338d0dac61&mpshare=1&scene=23&srcid=0405hTaS61xxLOeUDgolcEFX&sharer_sharetime=1586073487274&sharer_shareid=0cb47ba9a357b8a621896b6cb6d3f8ca#rd
 * 回溯(数独)：https://mp.weixin.qq.com/s?__biz=MzAxODQxMDM0Mw==&mid=2247485097&idx=1&sn=a5e82da8646cd8985de6b2b0950de4e2&chksm=9bd7f8a1aca071b7b72e23013bc2a7c528ee913fded9278e2058bc98d7c746e439737d7abb5b&mpshare=1&scene=23&srcid=0405xVoXUNnmvZsAnLr4GnAE&sharer_sharetime=1586073508761&sharer_shareid=0cb47ba9a357b8a621896b6cb6d3f8ca#rd
 */
public class _37_SolveSudoku {
    /**
     * 找到全部结果的回溯框架
     void backTrack(路径，选择列表) {
         if(满足结束条件) {
         res.add(路径);
         return;
         }
         for 选择 in 选择列表：{
         做选择;
         backTrack(路径，选择列表);
         撤销本次选择，进行下一次
         }
     }*/

    /**
     * 找到一种结果即可结束的回溯框架
     boolean backTrack(路径，选择列表) {
         if(满足结束条件) {
         res.add(路径);
         return true;
         }
         for 选择 in 选择列表：{
         做选择;
         if(backTrack(路径，选择列表)) return;
         撤销本次选择，进行下一次
         }
     }*/

    /**
     * 回溯
     * 回溯解法：https://leetcode-cn.com/problems/sudoku-solver/solution/zi-cong-wo-xue-hui-liao-hui-su-suan-fa-zhong-yu-hu/
     * 逐行，从左到右，在每一个位置上试探1-9，成功就进入下一个位置，失败就取消本次选择，做下一个选择
     * 当前行试探完毕就换行，直到换到最后一行
     *
     * @param board
     */
    public void solution(char[][] board) {
        // 非法数独
        if (board == null || board.length != 9 || board[0] == null || board[0].length != 9)
            return;
        // 回溯法解决
        backTrace(board, 0, 0);
    }

    private boolean backTrace(char[][] board, int row, int col){
        int n = board.length; // 9
        // 当前行已全部试探过，换到下一行第一个位置
        if (col == 9)
            backTrace(board, row + 1, 0);
        // 满足结束条件，全部行全部位置都已试探过
        if (row == n)
            // 最后一行最后一个位置[8][8]试探过后会试探[8][9]，会执行[9][0]，返回
            return true;
        // 这个位置数字已给出，不需要试探，直接试探下一个位置
        if (board[row][col] != '.')
            return backTrace(board, row, col + 1);
        // 遍历可选择列表(各选择之间并列)
        for (char c = '1'; c <= '9'; c++){
            // 排除不合法的选择
            if (!isValid(board, row, col, c))
                continue;
            // 做选择
            board[row][col] = c;
            // 进行下一步试探，发现当前选择能成功进行下去，就继续往下
            if (backTrace(board, row, col + 1))
                return true;
            // 撤销本次选择，并列进行下一次选择的试探
            board[row][col] = '.';
        }
        // 这个位置把1-9都试过了，都无法继续下去，说明上一次的选择失败，需要回溯
        return false;
    }

    /**
     * 判断 board[row][col]位置放入字符 ch,是否合理
     * 也就判断这个字符有没有在 同一行，同一列，同一个子数独中出现过
     * 行列比较容易，就是一个for循环
     * 而对于 给定的 board[i][j]，它所在的子数独的索引是 (i / 3) * 3 + j / 3
     *
     * 要扫描这个子数独中的全部9个元素，for循环可以这样写
     * boardIndex = (i / 3) * 3 + j / 3
     * for(int k = 0; k < 9; k++){
     *      board[(i/3)*3 + k/3][(j/3)*3 + k % 3]
     * }
     * 因为 i和j是确定的，所以 i / 3 * 3可以确定他所在的子数独在第一个三行，还是第二个三行，还是第三个三行
     * j / 3 * 3可以确定它所在的子数独是前三列还是中三列还是后三列，
     * 相当于这两个只是确定了这个【子数独的左上角坐标】，而需要借助 k 完全对这个9个位置的扫描
     *
     * @param board
     * @param row
     * @param col
     * @param ch
     * @return
     */
    private boolean isValid(char[][] board, int row, int col, char ch) {
        // 三个方向，任一方向重复，ch就不能放在这个位置
        for (int k = 0; k < 9; k++) {
            // 同一行九个位置已出现 ch
            if (board[row][k] == ch) return false;
            // 同一列九个位置中已出现 ch
            if (board[k][col] == ch) return false;
            // 同一个子数独九个位置中已出现 ch
            if (board[(row / 3) * 3 + k / 3][(col / 3) * 3 + k % 3] == ch) return false;
        }
        return true;
    }

}
