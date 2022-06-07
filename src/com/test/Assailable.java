package com.test;

/**
 * @date 2022/6/5 9:56
 * @description: Assailable
 */
public interface Assailable {

    public String getName();

    public int getX();

    public int getY();

    public int getHp();

    public void setHp(int hp);

    public void fight(Assailable assa);

    public boolean canFightByDistance(Assailable assa);
}
