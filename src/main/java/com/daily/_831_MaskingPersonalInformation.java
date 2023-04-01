package com.daily;

/**
 * @author wangwei
 * @date 2023/4/1 10:06
 * @description: _831_MaskingPersonalInformation
 *
 * 831. 隐藏个人信息
 * 给你一条个人信息字符串 s ，可能表示一个 邮箱地址 ，也可能表示一串 电话号码 。返回按如下规则 隐藏 个人信息后的结果：
 *
 * 电子邮件地址：
 *
 * 一个电子邮件地址由以下部分组成：
 *
 * 一个 名字 ，由大小写英文字母组成，后面跟着
 * 一个 '@' 字符，后面跟着
 * 一个 域名 ，由大小写英文字母和一个位于中间的 '.' 字符组成。'.' 不会是域名的第一个或者最后一个字符。
 * 要想隐藏电子邮件地址中的个人信息：
 *
 * 名字 和 域名 部分的大写英文字母应当转换成小写英文字母。
 * 名字 中间的字母（即，除第一个和最后一个字母外）必须用 5 个 "*****" 替换。
 * 电话号码：
 *
 * 一个电话号码应当按下述格式组成：
 *
 * 电话号码可以由 10-13 位数字组成
 * 后 10 位构成 本地号码
 * 前面剩下的 0-3 位，构成 国家代码
 * 利用 {'+', '-', '(', ')', ' '} 这些 分隔字符 按某种形式对上述数字进行分隔
 * 要想隐藏电话号码中的个人信息：
 *
 * 移除所有 分隔字符
 * 隐藏个人信息后的电话号码应该遵从这种格式：
 * "***-***-XXXX" 如果国家代码为 0 位数字
 * "+*-***-***-XXXX" 如果国家代码为 1 位数字
 * "+**-***-***-XXXX" 如果国家代码为 2 位数字
 * "+***-***-***-XXXX" 如果国家代码为 3 位数字
 * "XXXX" 是最后 4 位 本地号码
 *
 * 示例 1：
 *
 * 输入：s = "LeetCode@LeetCode.com"
 * 输出："l*****e@leetcode.com"
 * 解释：s 是一个电子邮件地址。
 * 名字和域名都转换为小写，名字的中间用 5 个 * 替换。
 * 示例 2：
 *
 * 输入：s = "AB@qq.com"
 * 输出："a*****b@qq.com"
 * 解释：s 是一个电子邮件地址。
 * 名字和域名都转换为小写，名字的中间用 5 个 * 替换。
 * 注意，尽管 "ab" 只有两个字符，但中间仍然必须有 5 个 * 。
 * 示例 3：
 *
 * 输入：s = "1(234)567-890"
 * 输出："***-***-7890"
 * 解释：s 是一个电话号码。
 * 共计 10 位数字，所以本地号码为 10 位数字，国家代码为 0 位数字。
 * 因此，隐藏后的电话号码应该是 "***-***-7890" 。
 * 示例 4：
 *
 * 输入：s = "86-(10)12345678"
 * 输出："+**-***-***-5678"
 * 解释：s 是一个电话号码。
 * 共计 12 位数字，所以本地号码为 10 位数字，国家代码为 2 位数字。
 * 因此，隐藏后的电话号码应该是 "+**-***-***-7890" 。
 *
 *
 * 提示：
 *
 * s 是一个 有效 的电子邮件或者电话号码
 * 如果 s 是一个电子邮件：
 * 8 <= s.length <= 40
 * s 是由大小写英文字母，恰好一个 '@' 字符，以及 '.' 字符组成
 * 如果 s 是一个电话号码：
 * 10 <= s.length <= 20
 * s 是由数字、空格、字符 '('、')'、'-' 和 '+' 组成
 * 通过次数13,086提交次数28,176
 */
public class _831_MaskingPersonalInformation {


    /**
     * 方法一：模拟
     * 我们首先判断 s 是邮箱还是电话号码。
     *
     * 显然，如果 s 中有字符 ‘@’，那么它是邮箱，否则它是电话号码。
     *
     * 如果  s 是邮箱，我们将 s 的 ‘@’ 之前的部分保留第一个和最后一个字符，中间用 “*****" 代替，并将整个字符串转换为小写。
     *
     * 如果 s 是电话号码，我们只保留 s 中的所有数字。s.replaceAll("[^0-9]", "");
     *
     * 使用首先将最后 10 位本地号码变成 “***-***-XXXX" 的形式，再判断 s 中是否有额外的国际号码。
     *
     * 如果有，则将国际号码之前添加 ‘+’ 号并加到本地号码的最前端。
     *
     * 如果有 10 位数字，则加上前缀位空字符串。
     * 如果有 11 位数字，则加上前缀 “+*-"。
     * 如果有 12 位数字，则加上前缀 “+**-"。
     * 如果有 13 位数字，则加上前缀 “+**"。
     *
     * 简化写法：
     *         final String[] countryCodePrefix = {"", "+*-", "+**-", "+***-"};
     *         s = s.replaceAll("[^0-9]", "");
     *         int n = s.length();
     *         return countryCodePrefix[n - 10] + "***-***-" + s.substring(n - 4);
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/masking-personal-information/solution/yin-cang-ge-ren-xin-xi-by-leetcode-solut-2enf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public String maskPII(String s) {
        // 邮箱
        if (s.contains("@")) {
            String[] info = s.split("@");
            // 姓名 @ 域名
            String name = info[0], domain = info[1];
            char c = name.charAt(0);
            char d = name.charAt(name.length() - 1);
            StringBuilder builder = new StringBuilder();
            // 姓名只保留第一个字符和最后一个字符的小写形式，中间用 ***** 连接
            builder.append(Character.isLowerCase(c) ? c : Character.toLowerCase(c));
            builder.append("*".repeat(5));
            builder.append(Character.isLowerCase(d) ? d : Character.toLowerCase(d));
            // 最后拼接上 @ 和域名的小写形式
            builder.append("@").append(domain.toLowerCase());
            return builder.toString();
        }
        // 电话号码
        // 定义国际代码前缀
        final String[] countryCodePrefix = {"", "+*-", "+**-", "+***-"};
        // 去掉非数字字符
        s = s.replaceAll("[^0-9]", "");
        int n = s.length();
        // 根据国际代码位数得到对应前缀 + 中间部分 + 最后四位
        return countryCodePrefix[n - 10] + "***-***-" + s.substring(n - 4);
    }
}
