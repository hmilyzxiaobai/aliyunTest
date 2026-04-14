package example.dongfangcaifu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TimeUtilsZ {

    public static boolean checkTime(String dateInput){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "2024-01-01";
        try {
            Date date = formatter.parse(dateString);
            Date dateInputChange = formatter.parse(dateInput);

            int result = date.compareTo(dateInputChange);
//            if (result < 0) {
//                System.out.println("date1在date2之前");
//            } else if (result == 0) {
//                System.out.println("date1与date2相等");
//            } else {
//                System.out.println("date1在date2之后");
//            }
            return result<=0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getRandom(){
        Random random = new Random();
        int randomNum = random.nextInt(90000000) + 10000000; // 生成6位随机数
        return String.valueOf(randomNum);
    }
    public static void main(String [] args){
        Random random = new Random();
        int randomNum = random.nextInt(90000000) + 10000000; // 生成6位随机数
        String randomStr = String.valueOf(randomNum);
        System.out.println(randomStr);

    }

}
