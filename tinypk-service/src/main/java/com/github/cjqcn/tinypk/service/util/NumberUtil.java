package com.github.cjqcn.tinypk.service.util;

public class NumberUtil {

    private static char[] charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    public static String to62RadixString(long seq) {
        StringBuilder sBuilder = new StringBuilder();
        while (true) {
            int remainder = (int) (seq % 62);
            sBuilder.append(charSet[remainder]);
            seq = seq / 62;
            if (seq == 0) {
                break;
            }
        }
        return sBuilder.reverse().toString();
    }

    public static long radixString(String str) {
        long sum = 0L;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            sum += indexDigits(str.charAt(len - i - 1)) * Math.pow(62, i);
        }
        return sum;
    }

    private static int indexDigits(char ch) {
        for (int i = 0; i < charSet.length; i++) {
            if (ch == charSet[i]) {
                return i;
            }
        }
        return -1;
    }


}
