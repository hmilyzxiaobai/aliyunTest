package example.util;

import example.dongfangcaifu.utils.FloatUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class Student {
    public static void main(String[] args) throws InterruptedException {
        // 设置开始日期为 10 月 8 日
        LocalDate startDate = LocalDate.of(2024, 10, 24);

        // 设置遍历的天数，假设我们要遍历 10 个工作日
        int workDaysCount = 14;

        // 循环遍历工作日
        LocalDate currentDate = startDate;
        for (int i = 0; i < workDaysCount; i++) {
            // 如果当前日期是周六或周日，跳过
            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                currentDate = currentDate.plusDays(1); // 跳到下一个日期
                i--; // 不增加工作日计数，继续寻找工作日
                continue;
            }

            // 输出当前的工作日
            System.out.println(currentDate);
            // financialInfoDmService.getAfterTwo(dateIn);


            // 进入下一个日期
            currentDate = currentDate.plusDays(1);
        }
    }
}
