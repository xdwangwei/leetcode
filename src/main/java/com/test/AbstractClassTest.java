package com.test;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-11
 */
abstract class AbstractClassTest {

    public void sayHello() {
        System.out.println("begin...");
        sayHelloInternal();
        System.out.println("end...");
    }

    private void sayHelloInternal() {
        System.out.println("internal private method");
    }
}
