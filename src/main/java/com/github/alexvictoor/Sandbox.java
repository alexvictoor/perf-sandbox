package com.github.alexvictoor;



public class Sandbox {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        long time2 = time +2;
        int toto = (time>time+2) ? first(): second();
        System.out.println("toto " + toto);
    }

    public static int first() {
        System.out.println("first");
        return 1;
    }
    public static int second() {
        System.out.println("second");
        return 2;
    }

}
