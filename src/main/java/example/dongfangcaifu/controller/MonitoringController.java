package example.dongfangcaifu.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import example.dongfangcaifu.service.CompanyCapitalAnalysisHistoryService;
import example.dongfangcaifu.service.simulation.MonitoringService;
import example.dongfangcaifu.service.surveillance.MonitoringUtilsService;
import example.dongfangcaifu.src.dto.LimitUpDownDTO;
import example.dongfangcaifu.src.entity.MonitoringEntity;
import example.dongfangcaifu.utils.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

// 监控指标
@RestController
@RequestMapping("monitoring")
@Slf4j
public class MonitoringController {

    @Autowired
    private CompanyCapitalAnalysisHistoryService companyCapitalAnalysisHistoryService;


    @Autowired
    private MonitoringService monitoringService;


    @Autowired
    private MonitoringUtilsService monitoringUtilsService;

    // 读取excel文件 查看当前价格，如果有的话就告警
    @GetMapping("read")
    private String monitorExcelData(){
        // 拿到excel的数据
        List<List<String>> lists = ExcelReader.printExcelData();

        for(List<String> data:lists){
          //  System.out.println(data.toString());
            monitoringUtilsService.searchData(data.get(0),data.get(1),data.get(2));
        }
        return "执行完成";
    }

    @GetMapping("macd/read")
    private String monitorMacdExcelData(){
        String filePath = "D:\\东方财富分析日报\\macd分析\\20260420\\转正.xlsx";
        // 拿到excel的数据
        List<List<String>> lists = ExcelReader.printExcelData(filePath);
        int indexUp=0;
        int indexDown=0;
        int errorRead=0;
        for(List<String> data:lists){
            //  System.out.println(data.toString());
            int res = monitoringUtilsService.monitorMacdExcelData(data.get(0),data.get(1),data.get(4));
            if (res==3){
                break;
            }
            switch (res){
                case 1:indexUp++;break;
                case 2:indexDown++;break;
                case 3:errorRead++;break;
            }
        }
        log.info("该数据中总数为{}，涨的个数为{}，跌的个数为{}，读取失败个数为{}",lists.size(),indexUp,indexDown,errorRead);
        return "执行完成";
    }

   // @Scheduled(cron = "0 0/15 9-15 * * ?")
    private String monitorExcelDataScheduled(){
        // 拿到excel的数据
        List<List<String>> lists = ExcelReader.printExcelData();

        for(List<String> data:lists){
            //  System.out.println(data.toString());
            monitoringUtilsService.searchData(data.get(0),data.get(1),data.get(2));
        }
        return "执行完成";
    }

    @GetMapping("save")
    private String saveData(){
        log.info("进入循环");
        companyCapitalAnalysisHistoryService.saveCompanyCapitalAnalysisHistory();
        return "成功";
    }

    @GetMapping("get/analysis/now/uplow")
    public String analysisUpLow(){

        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        // 拿到当天数据

        Map<Integer, List<List<String>>> statistics =
                companyCapitalAnalysisHistoryService.statistics(format);
        List<MonitoringEntity> saveList = new ArrayList<>();


        for(int i:statistics.keySet()){
            List<List<String>> data = statistics.get(i);
            if (CollectionUtils.isEmpty(data)){
                continue;
            }
            for(List<String> one:data){
                MonitoringEntity monitoringEntity = new MonitoringEntity();
                monitoringEntity.setCompanyName(one.get(0));
                monitoringEntity.setCompanyCode(one.get(1));
                monitoringEntity.setChangeDetails(one.get(2));
                monitoringEntity.setPrice(one.get(3));
                monitoringEntity.setType(String.valueOf(i));
                monitoringEntity.setDateHis(format);
                saveList.add(monitoringEntity);
            }
        }
        monitoringService.saveBatch(saveList);

        return "成功";
    }

    /**
     * 大涨大跌统计接口
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate 结束日期 yyyy-MM-dd
     * @param type 类型：1沪A涨停 2深A涨停 3沪A跌停 4深A跌停 5深A大涨 6深A大跌
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    @GetMapping("/limit-up-down")
    public LimitUpDownDTO getLimitUpDownList(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return monitoringService.getLimitUpDownList(startDate, endDate, type, pageNum, pageSize);
    }

}
