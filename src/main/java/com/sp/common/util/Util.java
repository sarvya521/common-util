package com.sp.common.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Util {

    public static void noSuchElement(String errorMessage) {
        throw new NoSuchElementException(errorMessage);
    }

    public static void badArgument(String errorMessage) {
        throw new IllegalArgumentException(errorMessage);
    }

    public static String longToStringWithLength(long value, int length) {
        return String.format("%0" + length + "d", value);
    }

    public static void main(String[] args) {
        //testPasswords();
        //testRange();
        testPasswordBreaker();
    }

    public static void testPasswordBreaker() {
        int passwordLength = 3;
        String salt = "SARVESH";
        String password = "998";//Passwords.generateRandomPassword(passwordLength);
        System.out.println("password=" + password);
        int saltedPasswordHashValue = password.hashCode() + salt.hashCode();
        String passwordFound = PasswordBreaker.findPassword(s -> s.hashCode() + "SARVESH".hashCode(),
            passwordLength, salt, saltedPasswordHashValue);
        System.out.println("check=" + passwordFound.equals(password));
    }

    public static void testPasswords() {
        String password = Passwords.generateRandomPassword(3);
        System.out.println("password=" + password);
        String salt = "SARVESH";
        System.out.println("salt=" + salt);
        String hashPassword = Passwords.hashPassword(password, salt);
        System.out.println("hashPassword=" + hashPassword);
        System.out.println("check=" + Passwords.isExpectedPassword(password, salt, hashPassword));
    }

    public static void testRange() {
        Range range2 = new Range(14, 24, 3);
        Iterator<Integer> iterator2 = range2.iterator();
        while (iterator2.hasNext()) {
            System.out.print(iterator2.next() + " ");
        }
        System.out.println();

        Range range3 = new Range(-4, 24, 3);
        Iterator<Integer> iterator3 = range3.iterator();
        while (iterator3.hasNext()) {
            System.out.print(iterator3.next() + " ");
        }
        System.out.println();

        Range range4 = new Range(-13, -4, 3);
        Iterator<Integer> iterator4 = range4.iterator();
        while (iterator4.hasNext()) {
            System.out.print(iterator4.next() + " ");
        }
        System.out.println();

        Range range5 = new Range(24, 14, 3);
        Iterator<Integer> iterator5 = range5.iterator();
        while (iterator5.hasNext()) {
            System.out.print(iterator5.next() + " ");
        }
        System.out.println();

        Range range6 = new Range(24, -2, 3);
        Iterator<Integer> iterator6 = range6.iterator();
        while (iterator6.hasNext()) {
            System.out.print(iterator6.next() + " ");
        }
        System.out.println();

        Range range7 = new Range(-14, -24, 3);
        Iterator<Integer> iterator7 = range7.iterator();
        while (iterator7.hasNext()) {
            System.out.print(iterator7.next() + " ");
        }
        System.out.println();
    }
}
