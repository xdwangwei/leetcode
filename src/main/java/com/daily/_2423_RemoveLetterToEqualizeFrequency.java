package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/4/29 20:23
 * @description: _2423_RemoveLetterToEqualizeFrequency
 *
 * 2423. 删除字符使频率相同
 * 给你一个下标从 0 开始的字符串 word ，字符串只包含小写英文字母。你需要选择 一个 下标并 删除 下标处的字符，使得 word 中剩余每个字母出现 频率 相同。
 *
 * 如果删除一个字母后，word 中剩余所有字母的出现频率都相同，那么返回 true ，否则返回 false 。
 *
 * 注意：
 *
 * 字母 x 的 频率 是这个字母在字符串中出现的次数。
 * 你 必须 恰好删除一个字母，不能一个字母都不删除。
 *
 *
 * 示例 1：
 *
 * 输入：word = "abcc"
 * 输出：true
 * 解释：选择下标 3 并删除该字母，word 变成 "abc" 且每个字母出现频率都为 1 。
 * 示例 2：
 *
 * 输入：word = "aazz"
 * 输出：false
 * 解释：我们必须删除一个字母，所以要么 "a" 的频率变为 1 且 "z" 的频率为 2 ，要么两个字母频率反过来。所以不可能让剩余所有字母出现频率相同。
 *
 *
 * 提示：
 *
 * 2 <= word.length <= 100
 * word 只包含小写英文字母。
 * 通过次数15,320提交次数64,496
 */
public class _2423_RemoveLetterToEqualizeFrequency {

    /**
     * 方法：统计字符出现频率 + 对频率值的分布进行分类讨论
     *
     * 题目分析
     *
     * 这道题是要对字符串中每个字母的出现次数（频率freq）进行处理。
     * 目标是删除一个字母使得剩下字母的出现频率都相同，也就是字母的频率情况只有一种。
     * 那么我们需要来看在删除一个字母之前，什么样的情况可以达到目标：
     *
     * 如果 字符串的字母的出现频率多于两种，如abbccc，字母出现的频率有{1,2,3}三种情况。
     *      无论删除哪个字母，都无法使得所有字母出现的频率一样。
     * 如果 字符串的字母的出现频率只有一种，已经满足目标。但是题目要求必须删掉一个字母，因此删掉的字母不能影响已有的平衡：
     *      除非所有字母的出现频率都为1，这样去掉任意一个字母都不会影响剩下的字母的出现频率。
     *      或者只存在一种字母，这样因为只有一种字母，去掉任意一个字母它自己都可以满足条件。如aaaaaaa，去掉一个a可以满足。
     * 如果 字符串的字母的出现频率有两种，假如为 a、b，且 a < b
     *      要么【只有一个】字母的出现频率是b，且刚好比其他字母的出现频率a多1次，那么删掉一个这个字母可以使频率统一。
     *          如aaabbcc，字母出现的频率有{2,3}两种情况，频率多的字母去掉一个刚好满足目标。
     *      要么【只有一个】字母的出现频率a刚好为1，其他字母出现频率相同为b，那么删掉这个字母也可以使频率统一。
     *          如abbbccc，字母出现的频率有{1,3}两种情况，去掉频率为1的字母刚好满足目标。
     *
     * 实现
     *
     * 首先我们需要统计每个字母的出现频率freq
     * 其次使用哈希表freqCount记录有多少种频率以及每个频率对应了多少种字母，哈希表的尺寸就是出现频率的情况数。
     * 然后对哈希表的尺寸进行分类讨论：
     * 哈希表尺寸大于2，无法满足目标条件；
     * 哈希表尺寸等于1的，就要去看唯一的这个频率是否为1（即key是否为1）或者是否只有一种字母（即value是否为1）
     * 哈希表尺寸等于2的，从哈希表中找到较大频率max_freq和较小频率min_freq。
     * 判断小频率min_freq是否为1且是否只有一种字母是这个频率 或者 大频率刚好比小频率多1且是否只有一个字母是大频率。
     *
     * 作者：lxk1203
     * 链接：https://leetcode.cn/problems/remove-letter-to-equalize-frequency/solution/javapython3tong-ji-pin-lu-dui-pin-lu-fen-1ynl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：lxk1203
     * 链接：https://leetcode.cn/problems/remove-letter-to-equalize-frequency/solution/javapython3tong-ji-pin-lu-dui-pin-lu-fen-1ynl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param word
     * @return
     */
    public boolean equalFrequency(String word) {
        // 统计每个字母的出现频率
        int[] freq = new int[26];
        for(int i = 0; i < word.length(); i++){
            freq[word.charAt(i) - 'a']++;
        }
        // 哈希表freqCount记录有多少种不同频率以及每种频率对应了多少种字母
        Map<Integer, Integer> freqCount = new HashMap<>();
        for(int f: freq){
            // 频率不为0的才是出现了的字母
            if(f > 0){
                freqCount.put(f, freqCount.getOrDefault(f, 0) + 1);
            }
        }
        // 出现频率多于两种 无法满足目标，如 abbccc
        if(freqCount.size() > 2){
            return false;
        }
        // 出现频率只有一种，除非所有字母都只出现一次，或者一共只有一种字母
        if(freqCount.size() == 1){
            // 这里相当于判断 entrys[0] 的 k,v, 除非 k == 1（所有字母都只出现一次）或 v==1（word中只有一种字母）
            for(int f: freqCount.keySet()) {
                return f == 1 || freqCount.get(f) == 1;
            }
        }
        // 出现频率有两种 找较大频率b和较小频率a
        int a = 0, b = 0;
        for(int f: freqCount.keySet()){
            if (b == 0) {
                b = f;
            } else if (f > b) {
                a = b;
                b = f;
            }
        }
        // 要么小频率a为1且只有一个字符是小频率a，如 abbcc，此时删掉a即可，像abccdd这种就不可以
        // 要么大频率b等于小频率a+1且只有一个字符是大频率b，如abcc，此时删掉c即可，但对于abcccddd这种就不可以
        return (a == 1 && freqCount.get(a) == 1)
                || (b == a + 1 && freqCount.get(b) == 1);
    }

    public static void main(String[] args) {
        _2423_RemoveLetterToEqualizeFrequency obj = new _2423_RemoveLetterToEqualizeFrequency();
        System.out.println(obj.equalFrequency("ceeeec"));
    }
}
