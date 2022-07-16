package com.test;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-13
 */
public enum TestEnum {

    SINGLE_TONE(1),

    DOUBLE_TONE(1);

    private final int val;

    TestEnum(int val) {
        this.val = val;
    }

    public static void main(String[] args) {
        System.out.println("hello enum....");
    }
}
