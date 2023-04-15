package com.zmy.zmyserver.util;

import java.util.UUID;

public class TokenUtil {
    public static String newToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
