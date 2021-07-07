package com.array;

import java.util.HashMap;

/**
 * @author wangwei
 * 2020/4/4 16:25
 * <p>
 * 判断一个 9x9 的数独是否有效。只需要根据以下规则，验证已经填入的数字是否有效即可。
 * <p>
 * 数字 1-9 在每一行只能出现一次。
 * 数字 1-9 在每一列只能出现一次。
 * 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
 * <p>
 * 数独部分空格内已填入了数字，空白格用 '.' 表示。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/valid-sudoku
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * 说明:
 * <p>
 * 一个有效的数独（部分已被填充）不一定是可解的。
 * 只需要根据以上规则，验证已经填入的数字是否有效即可。
 * 给定数独序列只包含数字 1-9 和字符 '.' 。
 * 给定数独永远是 9x9 形式的。
 */
public class _36_ValidSudoku {
    /**
     * 一个简单的解决方案是遍历该 9 x 9 数独 三 次，以确保：
     * <p>
     * 行中没有重复的数字。
     * 列中没有重复的数字。
     * 3 x 3 子数独内没有重复的数字。
     * <p>
     * 实际上，所有这一切都可以在一次迭代中完成
     * 对于当前正在遍历的位置 board[i][j]，它只会在第 i 行，第 j 列，且它所在的子数独是确定的 (i / 3) * 3 + (j / 3)
     * 想象一下如果只有一行，那么它 所在的子数独就是 j / 3
     * 对于 9 * 9数独，前三行肯定在 子数独 0 ，1 ，2，中间三行就是子数独 3，4，5
     * <p>
     * <p>
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/valid-sudoku/solution/you-xiao-de-shu-du-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    public boolean solution(char[][] board) {
        if (board == null || board.length != 9) return false;
        // 9行
        HashMap<Integer, Integer>[] rowsMap = new HashMap[9];
        // 9列
        HashMap<Integer, Integer>[] colsMap = new HashMap[9];
        // 9个子数独
        HashMap<Integer, Integer>[] sudokusMap = new HashMap[9];
        // 初始化
        for (int i = 0; i < 9; i++) {
            rowsMap[i] = new HashMap<>();
            colsMap[i] = new HashMap<>();
            sudokusMap[i] = new HashMap<>();
        }
        // 遍历81个位置
        for (int i = 0; i < 9; i++) {
            if (board == null || board[i].length != 9) return false;
            for (int j = 0; j < 9; j++) {
                // 当前位置不空
                if (board[i][j] != '.') {
                    int num = board[i][j];
                    // 它在当前行出现的次数 + 1
                    rowsMap[i].put(num, rowsMap[i].getOrDefault(num, 0) + 1);
                    // 它在当前列出现的次数 + 1
                    colsMap[j].put(num, colsMap[j].getOrDefault(num, 0) + 1);
                    // 它在它所在的子数独中出现的次数 + 1
                    int sudokuIndex = (i / 3) * 3 + (j / 3);
                    sudokusMap[sudokuIndex].put(num, sudokusMap[sudokuIndex].getOrDefault(num, 0) + 1);
                    // 判断三者之中任意方向重复出现
                    if (rowsMap[i].get(num) > 1
                            || colsMap[j].get(num) > 1
                            || sudokusMap[sudokuIndex].get(num) > 1)
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * 采用位数组的形式优化方法1
     * 对于每个 board[i][j], 用长度为27的01串保存这个数上一次出现的位置
     * 27位的01串并未溢出int
     * 前九位 表示它的行号，它在哪一行，哪一位就是1，中间九位 表示它所在列，它在那一列，第几个位置就是1
     * 这个数第二次出现时，用一个新的27位串表示它当前所在的三个位置，
     * 如果它与上一次在这三个方向上都未冲突，那么 上一次的值与这一次的值做 与运算，结果一定为0
     * 否则 若是在同一行则前九位出现1，同一列则中间九位出现1，同一子数独则后九位出现1，也就是结果不为0
     * 此时 返回false即可
     * 相反，若未重复(与运算结果为0)，则 两个值做 或运算 ，就可保存上一次和这一次出现的位置
     * 就拿行号来说(前九位)，同一个数字，最多出现九次，每一次出现在不同行，所以前九位最多也就全是1
     *
     * @param board
     * @return
     */
    public boolean solution2(char[][] board) {
        if (board == null || board.length != 9) return false;
        // 用来保存 1-9之前出现在了哪些位置，注意这个值虽然我们是以27位01串表示出来的，它仍然是一个int
        int[] bitMap = new int[9];
        // 遍历81个位置
        for (int i = 0; i < 9; i++) {
            if (board == null || board[i].length != 9) return false;
            for (int j = 0; j < 9; j++) {
                // 当前位置不空
                if (board[i][j] != '.') {
                    // 减 '1' 转为 0-8 或 减 '0' 转为 0-9
                    int num = board[i][j] - '1';
                    // 它之前出现过的位置
                    int prevLocations = bitMap[num];
                    // 表示它现在的位置
                    int sudokuIndex = (i / 3) * 3 + (j / 3);
                    // 前9位表示它在哪个那个子数独，中间9位表示它在哪一列，后9位表示它在哪一行
                    int curLocation = (1 << 18 + sudokuIndex) | (1 << 9 + j) | (1 << i);
                    // 之前的位置与现在的位置做 与运算，按位与 不是 逻辑 与
                    if ((prevLocations & curLocation) == 0){
                        // 没有交集，在行(后九位)、列(中间九位)、子数独(前九位)都未重复出现
                        // 把当前位置合并到它出现过的位置集合中去，按位或
                        bitMap[num] = prevLocations | curLocation;
                    }else
                        // 在某个方向上重复出现(前或中或后九位中某个位置上，之前与现在都是1)
                        return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int num = '5';
        System.out.println(num);
    }
}
