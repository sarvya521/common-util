package com.sp.common.util;

import java.util.Iterator;

public class PasswordIterator implements Iterator<String> {
    private final int passwordLength;
    private boolean[] visited;
    private boolean rule1Processed;
    private boolean rule2Processed;
    private boolean rule3Processed;
    private boolean allProcessed;
    private static final String ASCENDING_SEQUENCE = "0123456789";
    private static final String DESCENDING_SEQUENCE = "9876543210";
    private int idx;

    public PasswordIterator(int passwordLength) {
        if (passwordLength < 1 || passwordLength > 9) {
            Util.badArgument("Invalid passwordLength. Please enter passwordLength in between 1 and 9");
        }
        this.passwordLength = passwordLength;
        visited = new boolean[(int) Math.pow(10, passwordLength)];
    }

    public boolean hasNext() {
        return !allProcessed;
    }

    public String next() {
        if (allProcessed) {
            Util.noSuchElement("Iterator extends its limit");
        }

        if (!rule1Processed) {
            String str = String.format("%0" + passwordLength + "d", 0).replace("0", idx + "");
            visited[Integer.parseInt(str)] = true;
            if (idx == 9) {
                rule1Processed = true;
                idx = 0;
            } else {
                idx++;
            }
            return str;
        }

        if (!rule2Processed) {
            String str = ASCENDING_SEQUENCE.substring(idx, idx + passwordLength);
            visited[Integer.parseInt(str)] = true;
            if ((10 - passwordLength) == idx) {
                rule2Processed = true;
                idx = 0;
            } else {
                idx++;
            }
            return str;
        }

        if (!rule3Processed) {
            String str = DESCENDING_SEQUENCE.substring(idx, idx + passwordLength);
            visited[Integer.parseInt(str)] = true;
            if ((10 - passwordLength) == idx) {
                rule3Processed = true;
                idx = 0;
            } else {
                idx++;
            }
            return str;
        }
        while (idx < visited.length - 1) {
            idx++;
            if (!visited[idx]) {
                return Util.longToStringWithLength(idx, passwordLength);
            }
        }
        allProcessed = true;
        return null; // it will never return null
    }

    private int generateMax(int length) {
        return Integer.parseInt(String.format("%0" + length + "d", 0).replace('0', '9'));
    }

    public static void main(String[] args) {
        int length = 3;
        boolean[] arr = new boolean[(int) Math.pow(10, length)];
        System.out.println("-------Rule 1-------");
        for (int i = 0; i <= 9; i++) {
            String str = String.format("%0" + length + "d", 0).replace("0", i + "");
            arr[Integer.parseInt(str)] = true;
            System.out.println(str);
        }
        System.out.println("-------Rule 2-------");
        String ascendingSequence = "0123456789";
        for (int i = 0; i < (11 - length); i++) {
            String str = ascendingSequence.substring(i, i + length);
            arr[Integer.parseInt(str)] = true;
            System.out.println(str);
        }
        System.out.println("-------Rule 3-------");
        String descendingSequence = "9876543210";
        for (int i = 0; i < (11 - length); i++) {
            String str = descendingSequence.substring(i, i + length);
            arr[Integer.parseInt(str)] = true;
            System.out.println(str);
        }
        System.out.println("-------Rule 4-------");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]) {
                System.out.println(i + " visited already");
            } else {
                System.out.println(Util.longToStringWithLength(i, length));
            }
        }
    }
}
