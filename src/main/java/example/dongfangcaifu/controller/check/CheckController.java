package example.dongfangcaifu.controller.check;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.*;
import example.dongfangcaifu.service.dark.CompanyDarkCapitalService;
import example.dongfangcaifu.service.dark.PlateDarkCapitalService;
import example.dongfangcaifu.service.macd.MacdInfoService;
import example.dongfangcaifu.service.simulation.MonitoringService;
import example.dongfangcaifu.src.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("check")
@Slf4j
public class CheckController {
    //每天检查数据的
    @Autowired
    private CompanyHistoryNowDayService companyHistoryNowDayService;
    @Autowired
    private CompanyHistoryService companyHistoryService;
    @Autowired
    private CompanyCapitalDetailKlineService companyCapitalDetailKlineService;
    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;
    @Autowired
    private CompanyDarkCapitalService companyDarkCapitalService;
    @Autowired
    private CompanyCapitalAnalysisHistoryService companyCapitalAnalysisHistoryService;
    @Autowired
    private PlateCapitalDetailService plateCapitalDetailService;
    @Autowired
    private PlateCapitalDetailKlineService plateCapitalDetailKlineService;
    @Autowired
    private PlateHisService plateHisService;
    @Autowired
    private MacdInfoService macdInfoService;
    @Autowired
    private PlateDarkCapitalService plateDarkCapitalService;
    @Autowired
    private MonitoringService monitoringService;


    @GetMapping("check")
    public void check(@RequestParam(value = "format")String format){
        log.info("检查company当天的历史信息");
        if (!companyHistoryNowDayService.list(Wrappers.<CompanyHistoryNowDayEntity>lambdaQuery().eq(CompanyHistoryNowDayEntity::getDateHis, format)).isEmpty()){
            log.info("检查company当天的历史信息通过");
        }else {
            log.info("检查不过1");
        }
        log.info("检查company的存档信息");
        if (!companyHistoryService.list(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getDateHis, format)).isEmpty()){
            log.info("检查company当天的存档信息通过");
        }else {
            log.info("检查不过2");
        }
        log.info("检查company的kLine图信息");
        if (!companyCapitalDetailKlineService.list(Wrappers.<CompanyCapitalDetailKlineEntity>lambdaQuery().eq(CompanyCapitalDetailKlineEntity::getDateHis, format)).isEmpty()){
            log.info("检查company当天的kLine图信息通过");
        }else {
            log.info("检查不过3");
        }

        log.info("检查company的资金存档信息");
        if (!capitalFlowHistoryService.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery().eq(CapitalFlowHistoryEntity::getDateHis, format)).isEmpty()){
            log.info("检查company当天的资金存档信息通过");
        }else {
            log.info("检查不过4");
        }

        log.info("检查company的暗盘资金存档信息");
        if (!companyDarkCapitalService.list(Wrappers.<CompanyDarkCapitalEntity>lambdaQuery().eq(CompanyDarkCapitalEntity::getDateHis, format)).isEmpty()){
            log.info("检查company当天的暗盘资金存档信息通过");
        }else {
            log.info("检查不过5");
        }

        log.info("检查company的分析存档信息");
        if (!companyCapitalAnalysisHistoryService.list(Wrappers.<CompanyCapitalAnalysisHistoryEntity>lambdaQuery().eq(CompanyCapitalAnalysisHistoryEntity::getDateHis, format)).isEmpty()){
            log.info("检查company当天的分析存档信息通过");
        }else {
            log.info("检查不过6");
        }

        log.info("检查板块资金流入信息");
        if (!plateCapitalDetailService.list(Wrappers.<PlateCapitalDetailEntity>lambdaQuery().eq(PlateCapitalDetailEntity::getDateHis, format)).isEmpty()){
            log.info("检查板块资金流入信息通过");
        }else {
            log.info("检查不过7");
        }

        log.info("检查板块资金流入KLine线图信息");
        if (!plateCapitalDetailKlineService.list(Wrappers.<PlateCapitalDetailKlineEntity>lambdaQuery().eq(PlateCapitalDetailKlineEntity::getDateHis, format)).isEmpty()){
            log.info("检查板块资金流入KLine线图信息通过");
        }else {
            log.info("检查不过8");
        }

        log.info("检查板块历史信息");
        if (!plateHisService.list(Wrappers.<PlateHis>lambdaQuery().eq(PlateHis::getDf, format)).isEmpty()){
            log.info("检查板块历史涨幅表信息通过");
        }else {
            log.info("检查不过9");
        }

        log.info("检查板块资金暗盘历史信息");
        if (!plateDarkCapitalService.list(Wrappers.<PlateDarkCapitalEntity>lambdaQuery().eq(PlateDarkCapitalEntity::getDateHis, format)).isEmpty()){
            log.info("检查板块历史涨幅表信息通过");
        }else {
            log.info("检查不过10");
        }

        log.info("检查macd信息");
        if (!macdInfoService.list(Wrappers.<MacdInfoEntity>lambdaQuery().eq(MacdInfoEntity::getDf, format)).isEmpty()){
            log.info("检查macd信息通过");
        }else {
            log.info("检查不过11");
        }

        log.info("检查大涨大跌保存版本");
        if (!monitoringService.list(Wrappers.<MonitoringEntity>lambdaQuery().eq(MonitoringEntity::getDateHis, format)).isEmpty()){
            log.info("检查大涨大跌保存版本通过");
        }else {
            log.info("检查不过12");
        }
    }

}
