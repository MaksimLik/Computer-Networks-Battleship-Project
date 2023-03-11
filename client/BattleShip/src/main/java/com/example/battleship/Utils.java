package com.example.battleship;

public class Utils {

    static String clearString(String str) {
        StringBuilder filtered = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetterOrDigit(str.charAt(i))) {
                filtered.append(str.charAt(i));
            }
        }

        return filtered.toString();
    }
}
