package example.util;

import java.util.concurrent.TimeUnit;

public class Student {
    public static void main(String[] args) throws InterruptedException {
        long l = System.currentTimeMillis();
        System.out.println(l);
        TimeUnit.MILLISECONDS.sleep(60 );
        long l2 = System.currentTimeMillis();
        System.out.println(l2);
    }
}
