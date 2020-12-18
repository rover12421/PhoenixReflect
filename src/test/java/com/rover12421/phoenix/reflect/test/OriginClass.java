package com.rover12421.phoenix.reflect.test;

public class OriginClass {
    static String s1 = "s1";
    public int i1 = 100;

    private static void say(String info) {
        System.out.println(info);
    }

    public static String getS1() {
        return s1;
    }

    public String test(int i1, String str, Integer i2, Object ... any) {
        String ret = "i1=" + i1 + ", str=" + str + ", i2=" + i2 + ", any=[";
        for (Object o : any) {
            ret += ", " + o;
        }
        ret += "]";
        return ret;
    }
}
