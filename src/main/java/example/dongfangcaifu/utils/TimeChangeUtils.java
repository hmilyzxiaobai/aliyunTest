package example.dongfangcaifu.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeChangeUtils {

    public static boolean changeDate(String timeStr ){
        try {
            // 1. 定义时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // 2. 将字符串解析为LocalDateTime对象
            LocalDateTime dateTime = LocalDateTime.parse(timeStr, formatter);

            // 3. 获取小时数
            int hour = dateTime.getHour();

            // 4. 判断是否大于12点
            if (hour > 12) {
              //  System.out.println("时间 " + timeStr + " 大于12点");
                return true;
            } else if (hour == 12) {
               // System.out.println("时间 " + timeStr + " 正好是12点");
                return true;
            } else {
                //System.out.println("时间 " + timeStr + " 小于12点");
                return false;
            }

//            // 5. 可选：转换为其他格式输出
//            String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分"));
//            System.out.println("格式化后的时间: " + formattedTime);

        } catch (DateTimeParseException e) {
            System.err.println("时间格式不正确: " + e.getMessage());
        }
        return false;
    }
}
