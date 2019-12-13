package com.sp.common.util;

import java.util.function.Function;

public class PasswordBreaker {

    public static String findPassword(Function<String, Integer> hashFunction, int passwordLength,
                                      String salt, int saltedPasswordHashValue) {
        PasswordIterator iterator = new PasswordIterator(passwordLength);
        while (iterator.hasNext()) {
            String passwd = iterator.next();
            System.out.println(passwd);
            if (saltedPasswordHashValue == hashFunction.apply(passwd)) {
                return passwd;
            }
        }
        return null;
    }

}
