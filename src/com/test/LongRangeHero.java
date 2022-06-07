package com.test;

/**
 * @date 2022/6/5 10:05
 * @description: LongRangeHero
 */
public abstract class LongRangeHero extends Hero {

    private int attack;

    @Override
    public void fight(Assailable assa) {
        super.fight(assa);
    }

    @Override
    public boolean canFightByDistance(Assailable assa) {
        return super.canFightByDistance(assa);
    }
}
