package example.dongfangcaifu.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import example.dongfangcaifu.service.CompanyCapitalAnalysisHistoryService;
import example.dongfangcaifu.service.simulation.MonitoringService;
import example.dongfangcaifu.service.surveillance.MonitoringUtilsService;
import example.dongfangcaifu.src.entity.MonitoringEntity;
import example.dongfangcaifu.utils.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Scheduled(cron = "0 0/15 9-15 * * ?")
    private String monitorExcelDataScheduled(){
        // 拿到excel的数据
        List<List<String>> lists = ExcelReader.printExcelData();

        for(List<String> data:lists){
            //  System.out.println(data.toString());
            monitoringUtilsService.searchData(data.get(0),data.get(1),data.get(2));
        }
        return "执行完成";
    }
    // 监测三天涨跌停的数据

    @GetMapping("mon")
    private String monitorExcelYesterday(){

        return "";
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
     * 设置连续跌多少天的阈值  平盘为-1---+1  换手率达到3
     * 设置买入点卖出点，AI问答是否达到拐点
     * 买点接入AI 根据AI回答的判断是否可买入还是继续增加阈值写回excel
     * 每天更新excel表，接入飞书
     */
}
