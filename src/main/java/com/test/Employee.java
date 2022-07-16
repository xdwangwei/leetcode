package com.test;


import java.io.Serializable;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-14
 */
public class Employee extends Person implements Serializable {

    Employee(String name) {
        super(name);
    }

    public String name;
    public String address;
    public transient int SSN;

    public int number;

    public void mailCheck() {
        System.out.println("Mailing a check to " + name + " " + address);
    }
}