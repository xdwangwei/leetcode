package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * @version 1.0.0
 * @date 2021/9/10 21:53
 * @description: TODO
 */
public class Main {

    private List<P> pList = new ArrayList<>();

    public List<P> getpList() {
        return this.pList;
    }

    public void setpList(final List<P> pList) {
        this.pList = pList;
    }

    public static void main(String[] args) {
        P p1 = new P();
        p1.setPages(Arrays.asList(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10));
        P p2 = new P();

        Main obj = new Main();
        obj.setpList(Arrays.asList(p1, p2));

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < obj.getpList().size(); i++) {
                new Thread(obj.getpList().get(i)).start();
            }
        }
    }
}


class P implements Runnable {
    private List<Integer> pages = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    public List<Integer> getPages() {
        return this.pages;
    }

    public void setPages(final List<Integer> pages) {
        this.pages = pages;
    }

    @Override
    public void run() {
        for (int i = 0; i < pages.size(); i++) {
            System.out.println(Thread.currentThread().getName() + "__" + pages.get(i));
        }
    }
}
