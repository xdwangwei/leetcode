package com.daily;

/**
 * @author wangwei
 * @date 2023/2/1 18:17
 * @description: _2325_DecodeTheMessage
 * 2325. 解密消息
 * 给你字符串 key 和 message ，分别表示一个加密密钥和一段加密消息。解密 message 的步骤如下：
 *
 * 使用 key 中 26 个英文小写字母第一次出现的顺序作为替换表中的字母 顺序 。
 * 将替换表与普通英文字母表对齐，形成对照表。
 * 按照对照表 替换 message 中的每个字母。
 * 空格 ' ' 保持不变。
 * 例如，key = "happy boy"（实际的加密密钥会包含字母表中每个字母 至少一次），据此，可以得到部分对照表（'h' -> 'a'、'a' -> 'b'、'p' -> 'c'、'y' -> 'd'、'b' -> 'e'、'o' -> 'f'）。
 * 返回解密后的消息。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：key = "the quick brown fox jumps over the lazy dog", message = "vkbs bs t suepuv"
 * 输出："this is a secret"
 * 解释：对照表如上图所示。
 * 提取 "the quick brown fox jumps over the lazy dog" 中每个字母的首次出现可以得到替换表。
 * 示例 2：
 *
 *
 *
 * 输入：key = "eljuxhpwnyrdgtqkviszcfmabo", message = "zwx hnfx lqantp mnoeius ycgk vcnjrdb"
 * 输出："the five boxing wizards jump quickly"
 * 解释：对照表如上图所示。
 * 提取 "eljuxhpwnyrdgtqkviszcfmabo" 中每个字母的首次出现可以得到替换表。
 *
 *
 * 提示：
 *
 * 26 <= key.length <= 2000
 * key 由小写英文字母及 ' ' 组成
 * key 包含英文字母表中每个字符（'a' 到 'z'）至少一次
 * 1 <= message.length <= 2000
 * message 由小写英文字母和 ' ' 组成
 * 通过次数11,811提交次数14,366
 */
public class _2325_DecodeTheMessage {

    /**
     * 方法一：数组 或 哈希表
     *
     * 思路与算法
     *
     * 简单来说就是对26个英文字母重新建立映射即可（改变顺序）。
     *
     * 具体地，我们使用一个哈希表存储替换表，随后对字符串 key 进行遍历。
     *
     * 当我们遍历到一个不为空格且未在哈希表中出现的字母时，就将当前字母和 cur 作为键值对加入哈希表中。
     * 这里的 cur 即为替换之后的字母，它的初始值为字母 ‘a’，
     * 当哈希表中每添加一个键值对后，cur 就会变为下一个字母。
     *
     * 在这之后，我们再对字符串 message 进行遍历，用每个位置字符对应的映射字符替换当前字符 就可以得到答案。
     *
     * 因为空格不用考虑，只有26个小写英文字母，用数组作为哈希表即可
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/decode-the-message/solution/jie-mi-xiao-xi-by-leetcode-solution-wckx/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param key
     * @param message
     * @return
     */
    public String decodeMessage(String key, String message) {
        // 记录字符对应的新的排序，
        int[] map = new int[26];
        // 因为每个字符只考虑它第一次出现的顺序，因为序号从1开始，只有对应需要为0时，才为字符排序，否则说明是多次出现
        int id = 1;
        // 建立排序
        for (char c : key.toCharArray()) {
            // 跳过空格，只为每个字符第一次出现时赋值序号，序号自增
            if (c != ' ' && map[c - 'a'] == 0) {
                map[c - 'a'] = id++;
            }
        }
        // 记录结果
        char[] ans = new char[message.length()];
        // 遍历
        for (int i = 0; i < message.length(); ++i) {
            char c = message.charAt(i);
            // 对于非空格小写字符
            if (c != ' ') {
                // 按照序号得到其对应的映射字符，序号从1开始，这里需要恢复到0，再 +'a' 即可
                ans[i] = (char) (map[c - 'a'] - 1 + 'a');
            } else {
                ans[i] = ' ';
            }
        }
        // 返回
        return new String(ans);
    }
}
