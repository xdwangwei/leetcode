package com.demo;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * @author xidian.wangwei@gmail.com
 * Created on 2022-07-16
 */
public class CommonTest {

    @Test
    public void testDoublePrecision() {
        int qps = 100000, numOfInterface = 10;
        double[] weight = new double[numOfInterface];
        Random random = new Random();
        double total = 0.0;
        // 随机10个uri
        for (int i = 0; i < numOfInterface; i++) {
            // 每个uri的权重
            weight[i] = 8 + random.nextDouble() * 2;
            total += weight[i];
        }
        // 保证权重总和为100.00
        weight[numOfInterface - 1] = weight[numOfInterface - 1] + 100.00 - total;
        total = 0.0;
        for (int i = 0; i < numOfInterface; i++) {
            total += weight[i];
        }
        System.out.println(total);
        System.out.println(Double.compare(total, 100.00));
    }

    @Test
    public void testReflection() throws ClassNotFoundException {
        // BiChar biChar = new BiChar('c', 'd');
        Class<?> cls = Class.forName("com.test.AnnotationOverrideTest");
        // Class<?> cls = Class.forName("com.test.AnnotationOverrideTest$BiChar");
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor.getName());
            System.out.println(Modifier.toString(constructor.getModifiers()));
            System.out.println(constructor.getDeclaringClass());
        }
        System.out.println("*".repeat(100));
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
            System.out.println(Modifier.toString(field.getModifiers()));
        }
    }


    static class Node {
        int age;
        String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return age == node.age && Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(age, name);
        }

        Node(int age, String name) {
            this.age = age;
            this.name = name;
        }

    }

    @Test
    public void testHashcodeAndEquals() {
        Set<Node> set = new HashSet<>();
        set.add(new Node(18, "zhangsan"));
        System.out.println(set.contains(new Node(18, "zhangsan")));

        Set<int[]> set2 = new HashSet<>();
        set2.add(new int[]{1, 2});
        System.out.println(set2.contains(new int[]{1, 2}));
        System.out.println(set2.contains(new int[]{2, 1}));
    }
}
