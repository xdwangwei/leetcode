package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangwei
 * @date 2022/5/24 13:46
 * @description: Test
 */
public class Test {


    public static void main(String[] args) {

        new ArrayList<>();
        new HashMap<>();
        new ConcurrentHashMap<>();
        new Thread();
        // Throwable
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(100);
        treeSet.add(60);
        treeSet.add(830);
        treeSet.add(260);
        treeSet.add(10);
        treeSet.add(105);
        Iterator<Integer> iterator = treeSet.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}
