package example.dongfangcaifu.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import example.dongfangcaifu.service.CapitalFlowHistoryService;
import example.dongfangcaifu.service.CompanyHistoryService;
import example.dongfangcaifu.service.FinancialInfoDmService;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("buy")
@Slf4j
public class BuyController {

    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    @Autowired
    private FinancialInfoDmService financialInfoDmService;

    @Autowired
    private CompanyHistoryService companyHistoryService;

    /**
     * 连续买进的
     */
    @GetMapping("buy/continuous")
    public String getBuy(){
        return capitalFlowHistoryService.getUpContinuous();
    }

    @GetMapping("follow")
    public List<Map.Entry<String, Float>> fellow(@RequestParam(value = "dateIn") String dateIn
    ,@RequestParam(value = "dateInAfter") String dateInAfter){
        return financialInfoDmService.getAfterTwo(dateIn,dateInAfter);
    }

    @GetMapping("follow/down")
    public List<FinancialInfoDmEntity> fellowDown(@RequestParam(value = "dateIn") String dateIn
             ){
        return financialInfoDmService.getAfterTwoDownUp(dateIn );
    }


    @GetMapping("check")
    public void fellowCheck(@RequestParam(value = "dateIn") String dateIn){
        // 设置开始日期为 10 月 8 日
        LocalDate startDate = LocalDate.of(2024, 10, 24);

        // 设置遍历的天数，假设我们要遍历 10 个工作日
        int workDaysCount = 16;

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
            List<Map.Entry<String, Float>> afterTwo = financialInfoDmService.getAfterTwo(currentDate.toString(), currentDate.plusDays(1).toString() + " 01:00:00");

            List<String> codes = new ArrayList<>();
            for(Map.Entry<String, Float> one:afterTwo){
                System.out.println(one.getValue()+" "+one.getKey());
                String[] split = one.getKey().split("-");
                codes.add(split[0]);
            }
            if (!CollectionUtils.isEmpty(codes)){
                System.out.println(companyHistoryService.checkUp(codes,currentDate.toString()));
                ;
            }



            // 进入下一个日期
            currentDate = currentDate.plusDays(1);
        }
    }

    @GetMapping("test/check")
    public String testCheck(){
        List<String> codes = new ArrayList<>();
        codes.add("600667");
        codes.add("603260");
        codes.add("600416");
        codes.add("603659");
        codes.add("600509");
        codes.add("600984");
        codes.add("603711");
        codes.add("601777");
        codes.add("600745");
        codes.add("603612");

        System.out.println(companyHistoryService.checkUp(codes,"2024-10-25"));
        return "";
    }

}
