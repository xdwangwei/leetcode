package com.test;

/**
 * @date 2022/6/5 9:58
 * @description: Hero
 */
public abstract class Hero implements Assailable {

    protected int id;

    protected String name;

    protected int level;

    protected int x;

    protected int y;

    protected int maxHp;

    protected int hp;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public void fight(Assailable assa) {

    }

    @Override
    public boolean canFightByDistance(Assailable assa) {
        return false;
    }

    public void pk(Assailable assa) {

    }

    public double getDistance(Assailable assa1, Assailable assa2) {
        int x1 = assa1.getX(), y1 = assa1.getY();
        int x2 = assa2.getX(), y2 = assa2.getY();
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
