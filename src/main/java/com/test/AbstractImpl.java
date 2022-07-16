package com.test;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-11
 */
public class AbstractImpl extends AbstractClassTest {

    public static void main(String[] args) {
        AbstractImpl obj = new AbstractImpl();
        obj.sayHello();
        System.out.println(Double.compare(100.00000000000001, 100.00));
        System.out.println(Math.round(100.00000000000001));
        System.out.println(Double.compare(Math.round(100.00000000000001), 100.00));
    }
}
