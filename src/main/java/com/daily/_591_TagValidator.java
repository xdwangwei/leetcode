package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/5/2 20:57
 * @description: _591_TagValidator
 * 591. 标签验证器
 * 给定一个表示代码片段的字符串，你需要实现一个验证器来解析这段代码，并返回它是否合法。合法的代码片段需要遵守以下的所有规则：
 *
 * 代码必须被合法的闭合标签包围。否则，代码是无效的。
 * 闭合标签（不一定合法）要严格符合格式：<TAG_NAME>TAG_CONTENT</TAG_NAME>。其中，<TAG_NAME>是起始标签，</TAG_NAME>是结束标签。起始和结束标签中的 TAG_NAME 应当相同。当且仅当 TAG_NAME 和 TAG_CONTENT 都是合法的，闭合标签才是合法的。
 * 合法的 TAG_NAME 仅含有大写字母，长度在范围 [1,9] 之间。否则，该 TAG_NAME 是不合法的。
 * 合法的 TAG_CONTENT 可以包含其他合法的闭合标签，cdata （请参考规则7）和任意字符（注意参考规则1）除了不匹配的<、不匹配的起始和结束标签、不匹配的或带有不合法 TAG_NAME 的闭合标签。否则，TAG_CONTENT 是不合法的。
 * 一个起始标签，如果没有具有相同 TAG_NAME 的结束标签与之匹配，是不合法的。反之亦然。不过，你也需要考虑标签嵌套的问题。
 * 一个<，如果你找不到一个后续的>与之匹配，是不合法的。并且当你找到一个<或</时，所有直到下一个>的前的字符，都应当被解析为 TAG_NAME（不一定合法）。
 * cdata 有如下格式：<![CDATA[CDATA_CONTENT]]>。CDATA_CONTENT 的范围被定义成 <![CDATA[ 和后续的第一个 ]]>之间的字符。
 * CDATA_CONTENT 可以包含任意字符。cdata 的功能是阻止验证器解析CDATA_CONTENT，所以即使其中有一些字符可以被解析为标签（无论合法还是不合法），也应该将它们视为常规字符。
 * 合法代码的例子:
 *
 * 输入: "<DIV>This is the first line <![CDATA[<div>]]></DIV>"
 *
 * 输出: True
 *
 * 解释:
 *
 * 代码被包含在了闭合的标签内： <DIV> 和 </DIV> 。
 *
 * TAG_NAME 是合法的，TAG_CONTENT 包含了一些字符和 cdata 。
 *
 * 即使 CDATA_CONTENT 含有不匹配的起始标签和不合法的 TAG_NAME，它应该被视为普通的文本，而不是标签。
 *
 * 所以 TAG_CONTENT 是合法的，因此代码是合法的。最终返回True。
 *
 *
 * 输入: "<DIV>>>  ![cdata[]] <![CDATA[<div>]>]]>]]>>]</DIV>"
 *
 * 输出: True
 *
 * 解释:
 *
 * 我们首先将代码分割为： start_tag|tag_content|end_tag 。
 *
 * start_tag -> "<DIV>"
 *
 * end_tag -> "</DIV>"
 *
 * tag_content 也可被分割为： text1|cdata|text2 。
 *
 * text1 -> ">>  ![cdata[]] "
 *
 * cdata -> "<![CDATA[<div>]>]]>" ，其中 CDATA_CONTENT 为 "<div>]>"
 *
 * text2 -> "]]>>]"
 *
 *
 * start_tag 不是 "<DIV>>>" 的原因参照规则 6 。
 * cdata 不是 "<![CDATA[<div>]>]]>]]>" 的原因参照规则 7 。
 * 不合法代码的例子:
 *
 * 输入: "<A>  <B> </A>   </B>"
 * 输出: False
 * 解释: 不合法。如果 "<A>" 是闭合的，那么 "<B>" 一定是不匹配的，反之亦然。
 *
 * 输入: "<DIV>  div tag is not closed  <DIV>"
 * 输出: False
 *
 * 输入: "<DIV>  unmatched <  </DIV>"
 * 输出: False
 *
 * 输入: "<DIV> closed tags with invalid tag name  <b>123</b> </DIV>"
 * 输出: False
 *
 * 输入: "<DIV> unmatched tags with invalid tag name  </1234567890> and <CDATA[[]]>  </DIV>"
 * 输出: False
 *
 * 输入: "<DIV>  unmatched start tag <B>  and unmatched end tag </C>  </DIV>"
 * 输出: False
 * 注意:
 *
 * 为简明起见，你可以假设输入的代码（包括提到的任意字符）只包含数字, 字母, '<','>','/','!','[',']'和' '。
 * 通过次数8,975提交次数18,067
 */
public class _591_TagValidator {


    /**
     * 简单模拟题，。
     * 由于标签具有 [最先开始的标签最后结束] 的特性，因此我们可以考虑使用一个栈存储当前开放的标签。
     *
     * 除此之外，我们还需要考虑 cdata 以及一般的字符，二者都可以使用 遍历 + 判断 的方法直接进行验证。
     *
     * 我们可以对字符串 code 进行一次遍历。在遍历的过程中，根据遍历到位置 i 的当前字符，采取对应的判断：
     *
     * 如果当前的字符为<，那么需要考虑下面的四种情况：
     *
     *      如果下一个字符为 /，那么说明我们遇到了一个结束标签。我们需要定位下一个 > 的位置 j，此时 code[i+2..j−1] 就是该结束标签的名称。
     *      我们需要判断该名称与当前栈顶的名称是否匹配，如果匹配，说明名称的标签已经闭合，我们需要将当前栈顶的名称弹出。
     *      同时根据规则 1，我们需要保证整个 code 被闭合标签包围，因此如果栈中已经没有标签，但是 j 并不是 code 的末尾，那么说明后续还会有字符，它们不被闭合标签包围。
     *
     *      如果下一个字符为 !，那么说明我们遇到了一个cdata，我们需要继续往后读 7 个字符，判断其是否为 [CDATA[。
     *      在这之后，我们定位下一个]]> 的位置 j，此时 code[i+9..j−1] 就是 cdata 中的内容，它不需要被解析，所以我们也不必进行任何验证。
     *      需要注意的是，根据规则 1，栈中需要存在至少一个开放的标签。
     *
     *      如果下一个字符为大写字母，那么说明我们遇到了一个开始标签。我们需要定位下一个 > 的位置 j，此时 code[i+2..j−1] 就是该开始标签的名称。
     *      我们需要判断该名称是否恰好由 1 至 9 个大写字母组成，如果是，说明该标签合法，我们需要将该名称放入栈顶。
     *
     *      除此之外，如果不存在下一个字符，或者下一个字符不属于上述三种情况，那么 code 是不合法的。
     *
     * 如果当前的字符为其它字符，那么根据规则 1，栈中需要存在至少一个开放的标签。
     *
     * 在遍历完成后，我们还需要保证此时栈中没有任何（还没有结束的）标签。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/tag-validator/solution/biao-qian-yan-zheng-qi-by-leetcode-solut-fecy/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param code
     * @return
     */
    public boolean isValid(String code) {

        // 利用 栈 结构
        Deque<String> stack = new ArrayDeque<>();
        int j = 0;

        while (j < code.length()) {
            char c = code.charAt(j);
            // tag 开始 或者 <![CDATA
            if (c == '<') {
                // 对于 '<' 或者 '<A></A><'这种，最后一个字符是一个 <
                if (j == code.length() - 1) {
                    return false;
                }
                // < 开头，如果是 <![CDATA
                if (code.charAt(j + 1) == '!') {
                    // 有效串只能包裹在 tag 中
                    if (stack.isEmpty()) {
                        return false;
                    }
                    // <! 开头，后面又不是 [CDATA[，无效
                    if (!code.startsWith("[CDATA[", j + 2)) {
                        return false;
                    }
                    // <![CDATA 的结尾位置
                    int index = code.indexOf("]]>", j + 9);
                    // 不存在对应收尾，无效
                    if (index < 0) {
                        return false;
                    }
                    // 跳过这部分
                    j = index + 3;
                // < 开头的 闭合 tag
                } else if (code.charAt(j + 1) == '/') {
                    // 闭合tag的完整 tagName </tagname>
                    int index = code.indexOf(">", j + 2);
                    if (index < 0) {
                        return false;
                    }
                    // 必须要有与之匹配的 打开tag，
                    if (stack.isEmpty() || !code.substring(j + 2, index).equals(stack.peek())) {
                        return false;
                    }
                    // 抵消掉
                    stack.pop();
                    // 跳过
                    j = index + 1;
                    // [!!!如果此时栈空了，也就是外围再没有包裹的tag了，除非已经处理完了，否则就是类似<A></A>aaa这种，无效]
                    if (stack.isEmpty() && j < code.length()) {
                        return false;
                    }
                // < 开头的 开tag，<tag>
                } else {
                    // 得到tagname
                    int index = code.indexOf(">", j + 1);
                    if (index < 0) {
                        return false;
                    }
                    String tagname = code.substring(j + 1, index);
                    // tagname必须满足长度在1-9，全部是大写字母
                    if (tagname.length() < 1 || tagname.length() > 9) {
                        return false;
                    }
                    for (int k = 0; k < tagname.length(); ++k) {
                        if (!Character.isUpperCase(tagname.charAt(k))) {
                            return false;
                        }
                    }
                    // 入栈
                    stack.push(tagname);
                    // 跳过 <tagname>
                    j = index + 1;
                }
            } else {
                // 对于 < 以外的其他字符，只能 是 在 tag 的包裹中，否则无效
                if (stack.isEmpty()) {
                    return false;
                }
                // 一次性跳过这些字符 <A>abcdeft sfaf sfasf  sfa</A>
                while (j < code.length() && code.charAt(j) != '<') {
                    j++;
                }
            }
        }
        // 最后必须完美抵消，不然会有 <A> <B><C></C></B> 这种无效情况漏掉
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println("<<<[[[]]>".indexOf("[[["));
        _591_TagValidator obj = new _591_TagValidator();
        System.out.println(obj.isValid("<DIV>This is the first line <![CDATA[<div>]]></DIV>"));
    }
}
