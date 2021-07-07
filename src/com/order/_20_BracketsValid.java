package com.order;

import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Stack;

/**
 * @Author: wangwei
 * @Description: 括号匹配
 * @Time: 2019/12/11 周三 08:22
 **/
public class _20_BracketsValid {
	
	
	public boolean solution1(String s) {
		Stack<Character> stack = new Stack<>();
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
		if (!stack.empty()) return false;
		return true;
	}
	
	/* 利用Map简化case中的比较和重复代码 */
	public boolean solution2(String s) {
		
		HashMap<Character, Character> bracketsMap = new HashMap<>();
		bracketsMap.put(')', '(');
		bracketsMap.put(']', '[');
		bracketsMap.put('}', '{');
		
		Stack<Character> stack = new Stack<>();
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// 如果是 ) ] }
			if (bracketsMap.containsKey(c)) {
				// 栈顶元素和它对应的另一半括号是否相同
				char pop = stack.empty() ? '#' : stack.pop();
				if (pop != bracketsMap.get(c)) return false;
			} else {
				// 如果是 [ { ( 就入栈
				stack.push(c);
			}
		}
		
		if (!stack.empty()) return false;
		return true;
	}
}
