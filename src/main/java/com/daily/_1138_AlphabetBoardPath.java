package com.daily;

/**
 * @author wangwei
 * @date 2023/2/12 17:23
 * @description: _1138_AlphabetBoardPath
 *
 * 1138. 字母板上的路径
 * 我们从一块字母板上的位置 (0, 0) 出发，该坐标对应的字符为 board[0][0]。
 *
 * 在本题里，字母板为board = ["abcde", "fghij", "klmno", "pqrst", "uvwxy", "z"]，如下所示。
 *
 *
 *
 * 我们可以按下面的指令规则行动：
 *
 * 如果方格存在，'U' 意味着将我们的位置上移一行；
 * 如果方格存在，'D' 意味着将我们的位置下移一行；
 * 如果方格存在，'L' 意味着将我们的位置左移一列；
 * 如果方格存在，'R' 意味着将我们的位置右移一列；
 * '!' 会把在我们当前位置 (r, c) 的字符 board[r][c] 添加到答案中。
 * （注意，字母板上只存在有字母的位置。）
 *
 * 返回指令序列，用最小的行动次数让答案和目标 target 相同。你可以返回任何达成目标的路径。
 *
 *
 *
 * 示例 1：
 *
 * 输入：target = "leet"
 * 输出："DDR!UURRR!!DDD!"
 * 示例 2：
 *
 * 输入：target = "code"
 * 输出："RR!DDRR!UUL!R!"
 *
 *
 * 提示：
 *
 * 1 <= target.length <= 100
 * target 仅含有小写英文字母。
 * 通过次数8,112提交次数18,223
 */
public class _1138_AlphabetBoardPath {

    /**
     * 由于所有的字符在字母板上的位置固定，按照字母版上的字母排列规律，字符 c 的行号即为 (c-'a')/5，列号即为 (c-'a')%5。
     *
     * 因此从任意字符 a 到字符 b 的路径也是固定的，遵循 “直角转弯”路线。也称为二者在画板中坐标的曼哈顿距离。
     * 假设当前处在字符 a 处，坐标为 (x1,y1)，需要移动到字符 b，坐标为 (x2,y2)，
     * 此时只需先上下移动 |x1-x2|个位置，再左右移动 |y1-y2| 个位置，再执行一次添加操作即可完成字符 b 的添加。
     *
     * 需要注意的是字符 ‘z’ 所在的行只有一列，此时有以下两种特殊情况需要考虑：
     *
     * 从字符 ‘z’ 开始移动到其他字符时，第一步只能上移，需要先往上移动到目标字符所在的行，再向右移动到目标字符所在的列；
     * 由于字符 ‘z’ 所在的行只有一列，从其他字符移动到字符 ‘z’ 时，需要先往左移动到第 0 列，再向下移动到字符 ‘z’ 即可；
     * 对于其他字符的移动指令，可以先上下移动再左右移动或者先左右移动再上下移动均可。
     *
     * 综上所述，每次移动时【优先选择上移和左移】即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/alphabet-board-path/solution/zi-mu-ban-shang-de-lu-jing-by-leetcode-s-c30t/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/alphabet-board-path/solution/zi-mu-ban-shang-de-lu-jing-by-leetcode-s-c30t/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param target
     * @return
     */
    public String alphabetBoardPath(String target) {
        StringBuilder ans = new StringBuilder();
        // 上一步所在位置，初始在 0，0
        int i = 0, j = 0;
        for (int k = 0; k < target.length(); ++k) {
            // 当前需要添加的字符
            int v = target.charAt(k) - 'a';
            // 当前字符在字符板上的位置
            int x = v / 5, y = v % 5;
            // 优先【左移】
            while (j > y) {
                --j;
                // 添加对应操作符
                ans.append('L');
            }
            // 优先【上移】
            while (i > x) {
                --i;
                // 添加对应操作符
                ans.append('U');
            }
            // 右移
            while (j < y) {
                ++j;
                ans.append('R');
            }
            // 下移
            while (i < x) {
                ++i;
                ans.append('D');
            }
            // 添加当前字符
            ans.append("!");
        }
        // 返回
        return ans.toString();
    }
}
