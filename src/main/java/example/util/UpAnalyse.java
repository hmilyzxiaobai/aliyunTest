package example.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpAnalyse {
    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();
        Integer[] arr = {1,5,4,9,8,15,14,25,24,35};
        list=Arrays.asList(arr);
        init (list);
    }

    public static void init(List<Integer> list){
        List<Integer> one = new ArrayList<>();
        // 第一步 计算差
        for(int i = 0 ;i<list.size()-2;i++){
            int i1 = list.get(i + 1) - list.get(i);
            one.add(i1);
        }
        List<Integer> two = new ArrayList<>();

        for(int i =0 ;i<one.size()-2;i++){
            two.add(one.get(i)+one.get(i+1));
        }
        System.out.println(two.toString());
    }
    public static boolean analyse(List<Integer> list){
        List<Integer> one = new ArrayList<>();
        // 第一步 计算差
        int min = 0;
        int max = 0;
        for(int i = 0 ;i<list.size()-2;i++){
            min = Math.min(list.get(i),min);
            max = Math.max(list.get(i),max);
            int i1 = list.get(i + 1) - list.get(i);
            one.add(i1);

        }
        List<Integer> two = new ArrayList<>();

        int sum = 0;
        for(int i =0 ;i<one.size()-2;i++){
            int i2 = one.get(i) + one.get(i + 1);
            two.add(i2);
            if (i2>=2){
                sum++;
            }
        }

        if (sum*100>two.size()*70 && max-min>2000){
          return true;
        }
        return false;

    }
}
