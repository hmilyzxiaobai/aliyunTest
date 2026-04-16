package example.util;

import java.util.Arrays;

public class Test {
    public static void main(String[] args){
        String str = "1,3,4,5";
        String[] split = str.split(",");
        System.out.println(Arrays.asList(split).indexOf("4"));
    }

    public static int  add(int a ,int b){
        return a+b;
    }
}
