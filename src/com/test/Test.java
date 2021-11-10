package com.test;

import sun.misc.BASE64Encoder;

/**
 * @author wangwei
 * 2021/7/23 17:09
 */
public class Test {

    public static void main(String[] args) {

        BASE64Encoder encoder = new BASE64Encoder();
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        System.out.println(header);
        // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
        System.out.println(encoder.encode(header.getBytes()));
        String payload = "{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"iat\":1516239022}";
        System.out.println(payload);
        // eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
        System.out.println(encoder.encode(payload.getBytes()));
    }
}
