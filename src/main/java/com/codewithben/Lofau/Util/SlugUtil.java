package com.codewithben.Lofau.Util;



public class SlugUtil {

    private SlugUtil() {
    }

    public static String generate(String name) {

        return name
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }
}
