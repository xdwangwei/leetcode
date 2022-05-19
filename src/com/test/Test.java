package com.test;


/**
 * @author wangwei
 * 2021/7/23 17:09
 */
public class Test {

    public static void main(String[] args) {
        InterfaceTest obj = new InterfaceTestImpl("zhangsan", 99, 88);
        System.out.println(obj.finalScore());
    }
}
