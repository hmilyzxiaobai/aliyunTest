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

import java.util.List;

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
        List<CompanyHistoryNowDayEntity> list = companyHistoryNowDayService.list(Wrappers.<CompanyHistoryNowDayEntity>lambdaQuery().eq(CompanyHistoryNowDayEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list.isEmpty()){
            log.info("检查company当天的历史信息通过，时间为{}",list.get(0).getCreateTime());
        }else {
            log.info("检查不过1");
        }

        log.info("检查company的存档信息");
        List<CompanyHistoryEntity> list1 = companyHistoryService.list(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list1.isEmpty()){
            log.info("检查company当天的存档信息通过,时间为{}",list1.get(0).getCreateTime());
        }else {
            log.info("检查不过2");
        }


        log.info("检查company的kLine图信息");
        List<CompanyCapitalDetailKlineEntity> list2 = companyCapitalDetailKlineService.list(Wrappers.<CompanyCapitalDetailKlineEntity>lambdaQuery().eq(CompanyCapitalDetailKlineEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list2.isEmpty()){
            log.info("检查company当天的kLine图信息通过,时间为{}",list2.get(0).getCreateTime());
        }else {
            log.info("检查不过3");
        }

        log.info("检查company的资金存档信息");
        List<CapitalFlowHistoryEntity> list3 = capitalFlowHistoryService.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery().eq(CapitalFlowHistoryEntity::getDateHis, format).last(" order by date_his desc"));
        if (!list3.isEmpty()){
            log.info("检查company当天的资金存档信息通过,时间为{}",list3.get(0).getCreateTime());
        }else {
            log.info("检查不过4");
        }

        log.info("检查company的暗盘资金存档信息");
        List<CompanyDarkCapitalEntity> list4 = companyDarkCapitalService.list(Wrappers.<CompanyDarkCapitalEntity>lambdaQuery().eq(CompanyDarkCapitalEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list4.isEmpty()){
            log.info("检查company当天的暗盘资金存档信息通过,时间为{}",list4.get(0).getCreateTime());
        }else {
            log.info("检查不过5");
        }

        log.info("检查company的分析存档信息");
        List<CompanyCapitalAnalysisHistoryEntity> list5 = companyCapitalAnalysisHistoryService.list(Wrappers.<CompanyCapitalAnalysisHistoryEntity>lambdaQuery().eq(CompanyCapitalAnalysisHistoryEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list5.isEmpty()){
            log.info("检查company当天的分析存档信息通过,时间为{}",list5.get(0).getCreateTime());
        }else {
            log.info("检查不过6");
        }

        log.info("检查板块资金流入信息");
        List<PlateCapitalDetailEntity> list6 = plateCapitalDetailService.list(Wrappers.<PlateCapitalDetailEntity>lambdaQuery().eq(PlateCapitalDetailEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list6.isEmpty()){
            log.info("检查板块资金流入信息通过,时间为{}",list6.get(0).getCreateTime());
        }else {
            log.info("检查不过7");
        }

        log.info("检查板块资金流入KLine线图信息");
        List<PlateCapitalDetailKlineEntity> list7 = plateCapitalDetailKlineService.list(Wrappers.<PlateCapitalDetailKlineEntity>lambdaQuery().eq(PlateCapitalDetailKlineEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list7.isEmpty()){
            log.info("检查板块资金流入KLine线图信息通过,时间为{}",list7.get(0).getCreateTime());
        }else {
            log.info("检查不过8");
        }

        log.info("检查板块历史信息");
        List<PlateHis> list8 = plateHisService.list(Wrappers.<PlateHis>lambdaQuery().eq(PlateHis::getDf, format).last(" order by df desc"));
        if (!list8.isEmpty()){
            log.info("检查板块历史涨幅表信息通过,时间为{}",list8.get(0).getCreateTime());
        }else {
            log.info("检查不过9");
        }

        log.info("检查板块资金暗盘历史信息");
        List<PlateDarkCapitalEntity> list9 = plateDarkCapitalService.list(Wrappers.<PlateDarkCapitalEntity>lambdaQuery().eq(PlateDarkCapitalEntity::getDateHis, format).last(" order by date_his desc"));

        if (!list9.isEmpty()){
            log.info("检查板块资金暗盘历史信息,时间为{}",list9.get(0).getCreateTime());
        }else {
            log.info("检查不过10");
        }

        log.info("检查macd信息");
        List<MacdInfoEntity> list10 = macdInfoService.list(Wrappers.<MacdInfoEntity>lambdaQuery().eq(MacdInfoEntity::getDf, format).last(" order by df desc"));
        if (!list10.isEmpty()){
            log.info("检查macd信息通过,时间为{}",list10.get(0).getCreateTime());
        }else {
            log.info("检查不过11");
        }

        log.info("检查大涨大跌保存版本");
        List<MonitoringEntity> list11 = monitoringService.list(Wrappers.<MonitoringEntity>lambdaQuery().eq(MonitoringEntity::getDateHis, format).last(" order by date_his desc"));
        if (!list11.isEmpty()){
            log.info("检查大涨大跌保存版本通过,时间为{}",list11.get(0).getCreateTime());
        }else {
            log.info("检查不过12");
        }
    }

}
