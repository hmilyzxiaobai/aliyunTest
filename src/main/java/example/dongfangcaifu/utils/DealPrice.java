package example.dongfangcaifu.utils;

import org.jetbrains.annotations.NotNull;

public class DealPrice {

    @NotNull
    public static String dealPrice(String price){
        if (price.contains(".")||price.contains("-")){
            return price;
        }else {
            double num = Double.parseDouble(price) / 100;
            return  String.format("%.2f", num);
        }
    }
}
