package com.test;


/**
 * @date 2022/6/5 10:21
 * @description: TestAssailable
 */
public class TestAssailable {

    public static void main(String[] args) {
        Warrior h1 = new Warrior("hero1", 100);
        Warrior h2 = new Warrior("hero2", 80);
        Warrior h3 = new Warrior("hero3", 60);

        h1.fight(h2);
        System.out.println("hero1：" + h1.getHp() + ", hero2: " + h2.getHp());

        h2.fight(h3);
        System.out.println("hero2：" + h2.getHp() + ", hero3: " + h3.getHp());

        h3.fight(h1);
        System.out.println("hero3：" + h3.getHp() + ", hero1: " + h1.getHp());

        h1.fight(h3);
        System.out.println("hero1：" + h1.getHp() + ", hero3: " + h3.getHp());
    }
}
