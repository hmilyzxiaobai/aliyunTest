package example.dongfangcaifu.utils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCompare {
    final static String compareDateStr = "2025-08-01";
    public static boolean dateCompare(String dateStr) {
        //String dateStr = "2026-04-10";


        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 解析字符串为 LocalDate
        LocalDate date = LocalDate.parse(dateStr, formatter);
        LocalDate compareDate = LocalDate.parse(compareDateStr, formatter);

        // 判断是否大于
        return date.isAfter(compareDate);
      //  System.out.println("日期 " + dateStr + " 是否大于 " + compareDateStr + "？ " + date.isAfter(compareDate));
        // 输出：日期 2026-04-10 是否大于 2025-08-01？ true
    }
}