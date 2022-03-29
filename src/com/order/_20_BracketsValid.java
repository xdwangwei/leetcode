package com.order;

import java.lang.annotation.ElementType;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Stack;

/**
 * @Author: wangwei
 * @Description: 括号匹配
 * @Time: 2019/12/11 周三 08:22
 *
 * 20. 有效的括号
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 *
 * 有效字符串需满足：
 *
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 *
 *
 * 示例 1：
 *
 * 输入：s = "()"
 * 输出：true
 * 示例 2：
 *
 * 输入：s = "()[]{}"
 * 输出：true
 * 示例 3：
 *
 * 输入：s = "(]"
 * 输出：false
 * 示例 4：
 *
 * 输入：s = "([)]"
 * 输出：false
 * 示例 5：
 *
 * 输入：s = "{[]}"
 * 输出：true
 **/
public class _20_BracketsValid {

	/**
	 * 使用栈，
	 * 遇到 { ( [ 直接入栈，遇到右括号，先判断栈顶是否是与之对应的左括号，若是，则出栈，否则，不匹配
	 * 最后，如果栈不空，说明是这种情况（右括号多于左括号） ())，那么也不匹配
	 * @param s
	 * @return
	 */
	public boolean solution1(String s) {
		Deque<Character> stack = new ArrayDeque<>();
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
				case '(':
				case '[':
				case '{':
					stack.push(s.charAt(i));
					break;
				case ')':
					if (stack.isEmpty()) return false;
					if ('(' != stack.pop()) return false;
					break;
				case ']':
					if (stack.isEmpty()) return false;
					if ('[' != stack.pop()) return false;
					break;
				case '}':
					if (stack.isEmpty()) return false;
					if ('{' != stack.pop()) return false;
					break;
				default:
					break;
			}
		}
		return stack.isEmpty();
	}

	/**
	 * 利用Map简化case中的比较和重复代码
	 * @param s
	 * @return
	 */
	public boolean solution2(String s) {
		
		HashMap<Character, Character> bracketsMap = new HashMap<>();
		bracketsMap.put(')', '(');
		bracketsMap.put(']', '[');
		bracketsMap.put('}', '{');

		Deque<Character> stack = new ArrayDeque<>();
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// 如果是 ) ] }
			if (bracketsMap.containsKey(c)) {
				// 栈顶元素和它对应的另一半括号是否相同
				char pop = stack.isEmpty() ? '#' : stack.pop();
				if (pop != bracketsMap.get(c)) return false;
			} else {
				// 如果是 [ { ( 就入栈
				stack.push(c);
			}
		}

		return stack.isEmpty();
	}
}
