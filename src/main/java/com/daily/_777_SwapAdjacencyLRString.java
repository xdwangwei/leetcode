package com.daily;

/**
 * @author wangwei
 * @date 2022/10/5 12:52
 * @description: _777_SwapAdjacencyLRString
 *
 * 777. 在LR字符串中交换相邻字符
 * 在一个由 'L' , 'R' 和 'X' 三个字符组成的字符串（例如"RXXLRXRXL"）中进行移动操作。一次移动操作指用一个"LX"替换一个"XL"，或者用一个"XR"替换一个"RX"。现给定起始字符串start和结束字符串end，请编写代码，当且仅当存在一系列移动操作使得start可以转换成end时， 返回True。
 *
 *
 *
 * 示例 :
 *
 * 输入: start = "RXXLRXRXL", end = "XRLXXRRLX"
 * 输出: True
 * 解释:
 * 我们可以通过以下几步将start转换成end:
 * RXXLRXRXL ->
 * XRXLRXRXL ->
 * XRLXRXRXL ->
 * XRLXXRRXL ->
 * XRLXXRRLX
 *
 *
 * 提示：
 *
 * 1 <= len(start) = len(end) <= 10000。
 * start和end中的字符串仅限于'L', 'R'和'X'。
 */
public class _777_SwapAdjacencyLRString {


    /**
     * 每次移动操作将 “XL" 替换成 “LX"，或将 “RX" 替换成 “XR"，等价于如下操作：
     *
     * 如果一个字符 ‘L’ 左侧的相邻字符是 ‘X’，则将字符 ‘L’ 向左移动一位，
     *
     * 如果一个字符 ‘R’ 右侧的相邻字符是 ‘X’，则将字符 ‘R’ 向右移动一位，
     *
     * 由于每次移动操作只是交换两个相邻字符，不会增加或删除字符，因此如果可以经过一系列移动操作将 start 转换成 end，则 start 和 end 满足每一种字符的数量分别相同
     *
     * L 往左移动（L 左边为 X 时才能移动），R 往右移动（R 右边是 X 时才能移动），但 L 无法穿过 R，R 也无法穿过 L。
     *
     * 所以：当忽略所有字符X后，start 和 end 剩下的内容应该是一样的，
     * 也就是说，原start和end中，L和R的数量相等，并且相对顺序也一致，并且，start中的L出现的位置一定比end中对应L的位置靠右；start中的R出现的位置一定比end中对应R的位置靠左
     *
     * 用 n 表示 start 和 end 的长度，用 i 和 j 分别表示 start 和 end 中的下标，从左到右遍历 start 和 end，跳过所有的 ‘X’，当 i 和 j 都小于 n 时，比较非 ‘X’ 的字符：
     *
     * 如果 start[i] != end[j]，则 start 和 end 中的当前字符不匹配，返回 false；
     *
     * 如果 start[i]=end[j]，则当前字符是 ‘L’ 时应有 i≥j，当前字符是 ‘R’ 时应有 i≤j，如果当前字符与两个下标的关系不符合该规则，返回 false。
     *
     * 如果 i 和 j 中有一个下标大于等于 n，则有一个字符串已经遍历到末尾，继续遍历另一个字符串中的其余字符，如果其余字符中出现非 ‘X’ 的字符，则该字符不能与任意字符匹配，返回 false。
     *
     * 如果 start 和 end 遍历结束之后没有出现不符合移动操作的情况，则说明可以经过一系列移动操作将 start 转换成end，返回 true。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/swap-adjacent-in-lr-string/solution/zai-lrzi-fu-chuan-zhong-jiao-huan-xiang-rjaw8/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param start
     * @param end
     * @return
     */
    public boolean canTransform(String start, String end) {
        // 两串长度一致
        int n = start.length();
        // start 和 end 中 L 和 R 数量一样，且 逻辑顺序一致
        int i = 0, j = 0;
        while (i < n && j < n) {
            // 忽略所有 X
            while (i < n && start.charAt(i) == 'X') {
                i++;
            }
            while (j < n && end.charAt(j) == 'X') {
                j++;
            }
            if (i < n && j < n) {
                // 对应位置字符不一致
                if (start.charAt(i) != end.charAt(j)) {
                    return false;
                }
                char c = start.charAt(i);
                // start中的L出现的位置一定比end中对应L的位置靠右；start中的R出现的位置一定比end中对应R的位置靠左
                if ((c == 'L' && i < j) || (c == 'R' && i > j)) {
                    return false;
                }
                i++;
                j++;
            }
        }
        // 某一个串已经遍历到末尾，另一个串剩余部分不能出现非X字符
        if (i < n) {
            while (i < n) {
                if (start.charAt(i++) != 'X') {
                    return false;
                }
            }
        }
        if (j < n) {
            while (j < n) {
                if (end.charAt(j++) != 'X') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        _777_SwapAdjacencyLRString obj = new _777_SwapAdjacencyLRString();
        System.out.println(obj.canTransform("RXXLRXRXL", "XRLXXRRLX"));
    }
}
