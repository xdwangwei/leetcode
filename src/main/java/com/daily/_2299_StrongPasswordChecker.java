package com.daily;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/1/19 11:39
 * @description: _2299_StrongPasswordChecker
 *
 * 2299. 强密码检验器 II
 * 如果一个密码满足以下所有条件，我们称它是一个 强 密码：
 *
 * 它有至少 8 个字符。
 * 至少包含 一个小写英文 字母。
 * 至少包含 一个大写英文 字母。
 * 至少包含 一个数字 。
 * 至少包含 一个特殊字符 。特殊字符为："!@#$%^&*()-+" 中的一个。
 * 它 不 包含 2 个连续相同的字符（比方说 "aab" 不符合该条件，但是 "aba" 符合该条件）。
 * 给你一个字符串 password ，如果它是一个 强 密码，返回 true，否则返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：password = "IloveLe3tcode!"
 * 输出：true
 * 解释：密码满足所有的要求，所以我们返回 true 。
 * 示例 2：
 *
 * 输入：password = "Me+You--IsMyDream"
 * 输出：false
 * 解释：密码不包含数字，且包含 2 个连续相同的字符。所以我们返回 false 。
 * 示例 3：
 *
 * 输入：password = "1aB!"
 * 输出：false
 * 解释：密码不符合长度要求。所以我们返回 false 。
 *
 *
 * 提示：
 *
 * 1 <= password.length <= 100
 * password 包含字母，数字和 "!@#$%^&*()-+" 这些特殊字符。
 * 通过次数13,817提交次数21,107
 */
public class _2299_StrongPasswordChecker {

    /**
     * 模拟 + 判断
     * 遍历 password 字符，判断是否包含 大写、小写、数字、特殊字符
     * @param password
     * @return
     */
    public boolean strongPasswordCheckerII(String password) {
        // 长度必须 >= 8
        if (password.length() < 8) {
            return false;
        }
        // 特殊字符集合
        Set<Character> specials = new HashSet<>() {{
            add('!');
            add('@');
            add('#');
            add('$');
            add('%');
            add('^');
            add('&');
            add('*');
            add('(');
            add(')');
            add('-');
            add('+');
        }};
        int n = password.length();
        // 是否包含小写、大写、数字、特殊字符
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;
        for (int i = 0; i < n; ++i) {
            // 连续两个字符不能相同
            if (i > 0 && password.charAt(i) == password.charAt(i - 1)) {
                return false;
            }
            char ch = password.charAt(i);
            // 小写字符
            if (Character.isLowerCase(ch)) {
                hasLower = true;
            // 大写字符
            } else if (Character.isUpperCase(ch)) {
                hasUpper = true;
            // 数字字符
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            // 特殊字符
            } else if (specials.contains(ch)) {
                hasSpecial = true;
            }
        }
        // 必须全部满足
        return hasLower && hasUpper && hasDigit && hasSpecial;
    }
}
