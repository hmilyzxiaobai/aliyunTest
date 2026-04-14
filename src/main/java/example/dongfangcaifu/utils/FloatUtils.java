package example.dongfangcaifu.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FloatUtils {
    public static Float stringToFloat(String input){
        try {
            return Float.parseFloat(input);
        }catch (Exception e){
            return 0f;
        }
    }


//    public static void main(String[] args){
//        System.out.println(judge(3.5F,3.6F));
//    }

    public static float culExMin(float nowPrice,float minPrice){
        float abs = Math.abs(nowPrice - minPrice);
        BigDecimal result;
        BigDecimal decimal1 = new BigDecimal(Double.toString(abs*100));
        BigDecimal decimal2 = new BigDecimal(Double.toString(minPrice));
        // 进行除法运算，保留两位小数，使用四舍五入的方式
        if (minPrice==0){
            return 0;
        }
        result = decimal1.divide(decimal2, 2, RoundingMode.HALF_UP);
        // 输出结果
        return FloatUtils.stringToFloat(result.toString());
    }
}
