package com.test;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-16
 *
 * 列出常见类，快速进入源码
 */
public class QuickToFrequentlyUsedClassSourceCode {

    public static void main(String[] args) {
        new LongAdder();
        new LongAccumulator((a, b) -> a + b, 0);
        new ThreadLocal<Integer>();
        ThreadLocal.withInitial(() -> 0);
    }
}
