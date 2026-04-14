package example.dongfangcaifu.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Test {
    public static void main(String[] args){
        String str = "2025-03-27 09:31,4000.0,-107025.0,103025.0,4000.0,0.0";
        String s = str.split(",")[0];
        changeDate(s);
    }
    private static void changeDate(String timeStr ){
        try {
            // 1. 定义时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // 2. 将字符串解析为LocalDateTime对象
            LocalDateTime dateTime = LocalDateTime.parse(timeStr, formatter);

            // 3. 获取小时数
            int hour = dateTime.getHour();

            // 4. 判断是否大于12点
            if (hour > 12) {
                System.out.println("时间 " + timeStr + " 大于12点");
            } else if (hour == 12) {
                System.out.println("时间 " + timeStr + " 正好是12点");
            } else {
                System.out.println("时间 " + timeStr + " 小于12点");
            }

            // 5. 可选：转换为其他格式输出
            String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分"));
            System.out.println("格式化后的时间: " + formattedTime);

        } catch (DateTimeParseException e) {
            System.err.println("时间格式不正确: " + e.getMessage());
        }
    }

}
