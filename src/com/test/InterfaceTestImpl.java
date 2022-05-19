package com.test;

/**
 * @author wangwei
 * @date 2022/5/19 9:25
 * @description: InterfaceTestImpl
 */
public class InterfaceTestImpl implements InterfaceTest {

    private String name;

    private double ascore;

    private double bscore;

    public InterfaceTestImpl(final String name, final double ascore, final double bscore) {
        this.name = name;
        this.ascore = ascore;
        this.bscore = bscore;
    }

    @Override
    public double finalScore() {
        return ascore * A +bscore * B;
    }
}
