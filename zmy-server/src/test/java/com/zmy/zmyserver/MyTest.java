package com.zmy.zmyserver;

import cn.hutool.crypto.digest.DigestUtil;

public class MyTest {
    public static void main(String[] args) {

        String md5 = DigestUtil.md5Hex("123123");

        System.out.println(md5);

    }
}
