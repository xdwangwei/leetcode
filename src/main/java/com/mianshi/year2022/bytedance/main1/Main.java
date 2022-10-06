package com.mianshi.year2022.bytedance.main1;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/9/25 19:06
 * @description: Main
 *
 * 第一行 n 代表有n个族谱关系
 * 接下来n行，每一行类似 a b c，表示a是b的祖先，b是c的祖先
 * 最后一行x，问？排在第x代的成员个数
 *
 * 输出
 * 第一行：第一代的全部成员 a b c d
 * 第二行：第x代的成员个数
 */
public class Main {

    static class Person {
        String name;
        List<Person> children;

        Person parent;

        public Person(String name) {
            this.name = name;
            this.children = new ArrayList<>();
            this.parent = null;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Person && ((Person)obj).name.equals(name);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        Map<String, Person> personMap = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            String line = sc.nextLine();
            String[] infos = line.split(" ");
            for (int j = 0; j < infos.length - 1; ++j) {
                String name = infos[j];
                String child = infos[j + 1];
                Person father = personMap.getOrDefault(name, new Person(name));
                Person ch = personMap.getOrDefault(child, new Person(child));
                father.children.add(ch);
                ch.parent = father;
                personMap.putIfAbsent(name, father);
                personMap.putIfAbsent(child, ch);
            }
        }
        int q = sc.nextInt();
        Queue<Person> queue = new ArrayDeque<>();
        for (Person person : personMap.values()) {
            if (person.parent == null) {
                queue.offer(person);
            }
        }
        int layer = 1;
        while (!queue.isEmpty()) {
            int sz = queue.size();
            if (layer == n) {
                System.out.println(sz);
                break;
            }
            if (layer == 1) {
                for (int i = 0; i < sz; ++i) {
                    Person person = queue.poll();
                    if (i == sz -1) {
                        System.out.println(person.name);
                    } else {
                        System.out.print(person.name + " ");
                    }
                    if (person.children.size() > 0) {
                        for (Person child : person.children) {
                            queue.offer(child);
                        }
                    }
                }
            } else {
                for (int i = 0; i < sz; ++i) {
                    Person person = queue.poll();
                    if (person.children.size() > 0) {
                        for (Person child : person.children) {
                            queue.offer(child);
                        }
                    }
                }
            }
            layer++;
        }
    }
}

// 2
// a b c
// b c d
// 4
