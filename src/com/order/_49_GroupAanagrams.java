package com.order;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * 2022/4/14 9:07
 *
 * 49. 字母异位词分组
 * 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。
 *
 * 字母异位词 是由重新排列源单词的字母得到的一个新单词，所有源单词中的字母通常恰好只用一次。
 *
 *
 *
 * 示例 1:
 *
 * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
 * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
 * 示例 2:
 *
 * 输入: strs = [""]
 * 输出: [[""]]
 * 示例 3:
 *
 * 输入: strs = ["a"]
 * 输出: [["a"]]
 *
 *
 * 提示：
 *
 * 1 <= strs.length <= 104
 * 0 <= strs[i].length <= 100
 * strs[i] 仅包含小写字母
 */
public class _49_GroupAanagrams {

    /**
     * 核心，所有异构单词对应的 唯一的 【基准单词】
     * 方法一：将每个单词排序得到它每个字母按顺序排放得到的单词，这些单词成为基准
     * 用一个map来保存每个基准单词对应的全部异位词列表
     * 将每个单词添加到它的基准对应的列表中
     *
     * 注意map<String, List> 不能使用 char[]，因为 每次 string.toCharArray()得到的是不同对象，虽然内容相同，但hash值不同
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String word: strs) {
            // 排序得到基准单词
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            // 得到基准对应的列表
            List<String> list = map.getOrDefault(key, new ArrayList<>());
            // 当前单词添加进列表
            list.add(word);
            // 更新map
            map.put(key, list);
        }
        List<List<String>> res = map.values().stream().collect(Collectors.toList());
        // 简化返回
        // new ArrayList<List<String>>(map.values());
        return res;
    }

    /**
     * 方法二：和方法一核心一样，只是不用排序去得到基准，
     * 由于互为字母异位词的两个字符串包含的字母相同，因此两个字符串中的相同字母出现的次数一定是相同的，
     * 故可以将每个字母出现的次数统计出来，再使用字符串表示(字符+次数+字符+次数+字符+次数....，并且是按顺序的)，作为哈希表的键。
     *
     * 由于字符串只包含小写字母，因此对于每个字符串，可以使用长度为 26 的数组记录每个字母出现的次数。
     * 需要注意的是，在使用数组作为哈希表的键时，不同语言的支持程度不同，因此不同语言的实现方式也不同。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/group-anagrams/solution/zi-mu-yi-wei-ci-fen-zu-by-leetcode-solut-gyoc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            // 统计每个字符出现的次数
            int[] counts = new int[26];
            int length = str.length();
            for (int i = 0; i < length; i++) {
                counts[str.charAt(i) - 'a']++;
            }
            // 将每个出现次数大于 0 的字母和出现次数按顺序拼接成字符串，作为哈希表的键
            // 其实和1中直接排序得到基准单词作为键类似
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 26; i++) {
                // 过滤
                if (counts[i] != 0) {
                    // 拼接字符 + 次数
                    sb.append((char) ('a' + i));
                    sb.append(counts[i]);
                }
            }
            String key = sb.toString();
            // 与之前类似
            List<String> list = map.getOrDefault(key, new ArrayList<>());
            list.add(str);
            map.put(key, list);
        }
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        _49_GroupAanagrams obj = new _49_GroupAanagrams();
        obj.groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"});
    }
}
