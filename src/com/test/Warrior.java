package com.test;

/**
 * @date 2022/6/5 10:02
 * @description: Warrior
 */
public class Warrior extends Hero {

    public Warrior(final String name, final int hp) {
        this.name = name;
        this.hp = hp;
    }

    @Override
    public void fight(Assailable assa) {
        assa.setHp(assa.getHp() - 10);
    }

    @Override
    public boolean canFightByDistance(Assailable assa) {
        return false;
    }

}
