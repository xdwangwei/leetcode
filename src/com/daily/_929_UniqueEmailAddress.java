package com.daily;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/6/4 20:26
 * @description: _929_UniqueEmailAddress
 *
 * 929. 独特的电子邮件地址
 * 每个 有效电子邮件地址 都由一个 本地名 和一个 域名 组成，以 '@' 符号分隔。除小写字母之外，电子邮件地址还可以含有一个或多个 '.' 或 '+' 。
 *
 * 例如，在 alice@leetcode.com中， alice 是 本地名 ，而 leetcode.com 是 域名 。
 * 如果在电子邮件地址的 本地名 部分中的某些字符之间添加句点（'.'），则发往那里的邮件将会转发到本地名中没有点的同一地址。请注意，此规则 不适用于域名 。
 *
 * 例如，"alice.z@leetcode.com” 和 “alicez@leetcode.com” 会转发到同一电子邮件地址。
 * 如果在 本地名 中添加加号（'+'），则会忽略第一个加号后面的所有内容。这允许过滤某些电子邮件。同样，此规则 不适用于域名 。
 *
 * 例如 m.y+name@email.com 将转发到 my@email.com。
 * 可以同时使用这两个规则。
 *
 * 给你一个字符串数组 emails，我们会向每个 emails[i] 发送一封电子邮件。返回实际收到邮件的不同地址数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：emails = ["test.email+alex@leetcode.com","test.e.mail+bob.cathy@leetcode.com","testemail+david@lee.tcode.com"]
 * 输出：2
 * 解释：实际收到邮件的是 "testemail@leetcode.com" 和 "testemail@lee.tcode.com"。
 * 示例 2：
 *
 * 输入：emails = ["a@leetcode.com","b@leetcode.com","c@leetcode.com"]
 * 输出：3
 *
 * 提示：
 *
 * 1 <= emails.length <= 100
 * 1 <= emails[i].length <= 100
 * emails[i] 由小写英文字母、'+'、'.' 和 '@' 组成
 * 每个 emails[i] 都包含有且仅有一个 '@' 字符
 * 所有本地名和域名都不为空
 * 本地名不会以 '+' 字符作为开头
 */
public class _929_UniqueEmailAddress {


    /**
     * hashset
     *
     * 逐个处理
     *
     * 根据题意，我们需要将每个邮件地址的本地名按照规则转换，具体来说：
     *
     * 去掉本地名中第一个加号之后的部分（包括加号）；
     * 去掉本地名中所有的句点。
     * 转换后得到了实际的邮件地址。
     *
     * 为了计算不同地址的数目，我们可以用一个哈希表记录所有的邮件地址，答案为哈希表的长度。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/unique-email-addresses/solution/du-te-de-dian-zi-you-jian-di-zhi-by-leet-f178/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param emails
     * @return
     */
    public int numUniqueEmails(String[] emails) {
        // 去重 地址集合
        Set<String> emailSet = new HashSet<>();
        for (String email : emails) {
            int i = email.indexOf('@');
            // 去掉本地名第一个加号之后的部分
            String local = email.substring(0, i).split("\\+")[0];
            // 去掉本地名中所有的句点
            local = local.replace(".", "");
            // 加入集合
            emailSet.add(local + email.substring(i));
        }
        return emailSet.size();
    }
}
