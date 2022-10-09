package com.demo;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
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
}
