package com.test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-13
 */
public class AnnotationOverrideTest {

    static class BiChar {
        private final char c;
        private final char d;

        BiChar(char _c, char _d) {
            c = _c;
            d = _d;
        }
        // 注意：Object 的 equals 方法 参数 是 Object 类型
        // 所以这里不加 @Override 注解，这个方法会被当作 equals 方法的重载
        // 原 equals 方法 比较的 是 == 也就是 地址
        // 此方法不会生效
        public boolean equals(BiChar obj) {
            return c == obj.c && d == obj.d;
        }
        // 而 Object 的 hashCode 方法 本就无参
        // 因此 这里 即便 不加 @Override 注解，也能被正确识别为 方法覆盖
        public int hashCode() {
            return c * 31 + d;
        }
    }

    public static void main(String[] args) {
        // System.out.println(BiChar.class.getName());
        Set<BiChar> set = new HashSet<>();
        // 由于 equals 方法并未被正确覆盖，每个新建对象地址都不同，equals 都不成立所以， 最后 set 的大小是 260
        for (int i = 0; i < 10; i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                set.add(new BiChar(c, c));
            }
        }
        System.out.println(set.size());
    }
}
