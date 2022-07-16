package com.test;

public class MonitorV2ClientTest {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100000; i++) {
            new Thread(() -> {
                MonitorClientV2 instance = MonitorClientV2.getInstance(1);
            }).start();
        }
        Thread.sleep(1000);
        System.out.println("*".repeat(20));
        for (int i = 0; i < 100000; i++) {
            new Thread(() -> {
                MonitorClientV2 instance = MonitorClientV2.getInstanceSync(2);
            }).start();
        }
        Thread.sleep(10000);
    }
}