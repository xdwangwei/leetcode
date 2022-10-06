package com.daily;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * @date 2022/10/5 12:16
 * @description: _811_SubdomainVisitCount
 *
 * 811. 子域名访问计数
 * 网站域名 "discuss.leetcode.com" 由多个子域名组成。顶级域名为 "com" ，二级域名为 "leetcode.com" ，最低一级为 "discuss.leetcode.com" 。当访问域名 "discuss.leetcode.com" 时，同时也会隐式访问其父域名 "leetcode.com" 以及 "com" 。
 *
 * 计数配对域名 是遵循 "rep d1.d2.d3" 或 "rep d1.d2" 格式的一个域名表示，其中 rep 表示访问域名的次数，d1.d2.d3 为域名本身。
 *
 * 例如，"9001 discuss.leetcode.com" 就是一个 计数配对域名 ，表示 discuss.leetcode.com 被访问了 9001 次。
 * 给你一个 计数配对域名 组成的数组 cpdomains ，解析得到输入中每个子域名对应的 计数配对域名 ，并以数组形式返回。可以按 任意顺序 返回答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：cpdomains = ["9001 discuss.leetcode.com"]
 * 输出：["9001 leetcode.com","9001 discuss.leetcode.com","9001 com"]
 * 解释：例子中仅包含一个网站域名："discuss.leetcode.com"。
 * 按照前文描述，子域名 "leetcode.com" 和 "com" 都会被访问，所以它们都被访问了 9001 次。
 * 示例 2：
 *
 * 输入：cpdomains = ["900 google.mail.com", "50 yahoo.com", "1 intel.mail.com", "5 wiki.org"]
 * 输出：["901 mail.com","50 yahoo.com","900 google.mail.com","5 wiki.org","5 org","1 intel.mail.com","951 com"]
 * 解释：按照前文描述，会访问 "google.mail.com" 900 次，"yahoo.com" 50 次，"intel.mail.com" 1 次，"wiki.org" 5 次。
 * 而对于父域名，会访问 "mail.com" 900 + 1 = 901 次，"com" 900 + 50 + 1 = 951 次，和 "org" 5 次。
 *
 *
 * 提示：
 *
 * 1 <= cpdomain.length <= 100
 * 1 <= cpdomain[i].length <= 100
 * cpdomain[i] 会遵循 "repi d1i.d2i.d3i" 或 "repi d1i.d2i" 格式
 * repi 是范围 [1, 104] 内的一个整数
 * d1i、d2i 和 d3i 由小写英文字母组成
 */
public class _811_SubdomainVisitCount {

    /**
     * 每个计数配对域名的格式都是 d1.d2.d3" 或 d1.d2"。子域名的计数如下：
     *
     * 对于格式 d1.d2.d3"，有三个子域名 "d1.d2.d3"、"d2.d3" 和 "d3"，每个子域名各被访问 rep 次；
     *
     * 对于格式 d1.d2"，有两个子域名 d1.d2" 和 "d2"，每个子域名各被访问 rep 次。
     *
     * 为了获得每个子域名的计数配对域名，需要使用哈希表记录每个子域名的计数。遍历数组 cpdomains，对于每个计数配对域名，获得计数和完整域名，更新哈希表中的每个子域名的访问次数。
     *
     * 遍历数组 cpdomains 之后，遍历哈希表，对于哈希表中的每个键值对，关键字是子域名，值是计数，将计数和子域名拼接得到计数配对域名，添加到答案中。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/subdomain-visit-count/solution/zi-yu-ming-fang-wen-ji-shu-by-leetcode-s-0a6i/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param cpdomains
     * @return
     */
    public List<String> subdomainVisits(String[] cpdomains) {
        Map<String, Integer> domainCountMap = new HashMap<>();
        for (String cpdomain : cpdomains) {
            String[] split = cpdomain.split(" ");
            // 访问次数，
            int count = Integer.parseInt(split[0]);
            // 三级域名
            domainCountMap.put(split[1], domainCountMap.getOrDefault(split[1], 0) + count);
            int idx1 = split[1].indexOf(".");
            int idx2 = split[1].lastIndexOf(".");
            // 一级域名
            domainCountMap.put(split[1].substring(idx2 + 1), domainCountMap.getOrDefault(split[1].substring(idx2 + 1), 0) + count);
            // 二级域名
            if (idx1 < idx2) {
                domainCountMap.put(split[1].substring(idx1 + 1), domainCountMap.getOrDefault(split[1].substring(idx1 + 1), 0) + count);
            }
        }
        // 转为list
        return domainCountMap.entrySet().stream().map(entry -> entry.getValue() + " " + entry.getKey()).collect(Collectors.toList());
    }
}
