package example.dongfangcaifu.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.*;
import example.dongfangcaifu.src.entity.CapitalFlowHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.src.response.GouResponse;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("write")
@Slf4j
public class WriteInfoController {
    @Autowired
    private GetAllInfo getAllInfo;

    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    @Autowired
    private FinancialInfoDmService financialInfoDmService;
    @Autowired
    private CompanyHistoryService companyHistoryService;

    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private CompanyOtherService companyOtherService;

    @Autowired
    private GetNowAndSendShenA getNowAndSendShenA;

    /**
     * 写入全部公司信息
     */
    @GetMapping("write")
    private void writeCompanyAll(){
        getAllInfo.saveComInfo(0);
    }

    /**
     * 只保存 不推送
     */
    @GetMapping("save/dm")
    private void send(){
        getAllInfo.saveComInfo(1);
    }
    /**
     * 每天入库当天历史数据
     */
    @GetMapping("one/day/save")
    private void saveOne(){

    }

    /**
     * 查询持仓是否需要卖出
     */
    @GetMapping("cell")
    private void cell(){

    }

    @Autowired
    private GetNowAndSend getNowAndSend;
    /**
     * 调试
     */
    @GetMapping("send/email")
    public void sendEmail(){
        log.info("执行推送邮箱");
        getNowAndSend.saveComInfo();
        getNowAndSendShenA.saveComInfo();;
    }



    @GetMapping("surveillance/stock/low")
    public void surveillanceLow(){
        log.info("监控低点");

        getNowAndSend.saveComInfo();
    }

    @GetMapping("surveillance/stock/high")
    public void surveillanceHigh(){
        log.info("监控高点");
        getNowAndSend.saveComInfo();
    }

    @GetMapping("gou/list")
    public String gou(){
        /**
         * 全量测试
         */
        List<CompanyInfoEntity> all = companyInfoService.getAll();
        Set<String> collect = all.stream().map(CompanyInfoEntity::getCompanyCode).collect(Collectors.toSet());
        StringBuilder  sb = new StringBuilder();

        for(String code:collect){
            if (code.startsWith("688")){
                continue;
            }
            GouResponse gouResponse = capitalFlowHistoryService.gouCul(financialInfoDmService.getByCodeOne(code));
            log.info("当前股票代码为："+code);
            if (!gouResponse.isGou()){
            }else {
                sb.append(code).append("\n");
            }

        }
        return sb.toString();
    }

    /**
     * 单个
     * @param code
     * @return
     */
    @GetMapping("gou/test")
    public String gouTest(@RequestParam(value = "code") String code){
        GouResponse gouResponse = capitalFlowHistoryService.gouCul(financialInfoDmService.getByCodeOne(code));
        StringBuilder  sb = new StringBuilder();

        if (!gouResponse.isGou()){
            return "不是钩子";
        }else {
            Map<String, String> gouHis = gouResponse.getGouHis();
            for(String key:gouHis.keySet()){
                sb.append(key).append(",").append(gouHis.get(key)).append("\n");
            }
        }
        return sb.toString();

    }

    /**
     * 补一个字段数据
     * @param code
     * @return
     */
    @GetMapping("rebuild/data")
    public String rebuildData(@RequestParam(value = "code") String code,
                              @RequestParam(value = "jquery") String jquery,
                              @RequestParam(value = "cul") String cul){
        boolean b = capitalFlowHistoryService.reConvertData(code,jquery,cul);
        return "成功";
    }

    @GetMapping("rebuild/page/data")
    public String rebuildPage(@RequestParam(value = "page") int page
    ,@RequestParam(value = "jquery") String jquery,@RequestParam(value = "cul") String cul
            ,@RequestParam(value = "ignoreCode") String ignoreCode){
        for(;page<=70;page++){
            String pageData = companyHistoryService.getPageData(String.valueOf(page), "20");
            String[] split = pageData.split(",");
            for(String str:split){
                if (StringUtils.isEmpty(str)){
                    System.out.println(page);
                    //return "结束";
                }
                if (str.equals(ignoreCode)){
                    continue;
                }
                log.info("当前刷取股票代码为："+str);

                boolean flag = capitalFlowHistoryService.reConvertData(str, jquery,cul);

                if (!flag){
                    System.out.println("当前page"+page+"当前代码"+str);
                    return "结束";
                }
            }

        }

        return "结束";


        //boolean b = capitalFlowHistoryService.reConvertData(code);
        //return "成功";
    }

    /**
     * 补全全部字段 数据表为资金历史数据
     */

    @GetMapping("rebuild/all/cap")
    public String rebuildAllCap(){
        capitalFlowHistoryService.checkAllData();
        return "检查全部历史资金流入数据成功";
    }


    /**
     * 补全全部历史涨跌数据
     */
    @GetMapping("rebuild/all/company")
    public String rebuildComAll(){
        companyHistoryService.checkAllData();
        return "检查全部涨跌数据成功";
    }

    /**
     * 补全数据 数据为涨跌历史数据
     */

    @GetMapping("rebuild/data/company")
    public String rebuildCom(@RequestParam(value = "code") String code,@RequestParam(value = "jquery")String jquery){
        String[] split = code.split(",");
        for (String str:split){
            boolean rebuild = companyHistoryService.rebuild(str, jquery);
        }
        return "补全部分涨跌数据成功";
    }

    @GetMapping("page/data")
    public String getPageData(@RequestParam(value = "page") int page,@RequestParam(value = "size")String size){
        for(;page<=28;page++){
            String pageData = companyHistoryService.getPageData(String.valueOf(page), size);
            String[] split = pageData.split(",");
            for(String str:split){

                if (StringUtils.isEmpty(str)){
                    System.out.println(page);
                    return "结束";
                }
                boolean flag = companyHistoryService.rebuild(str, "jQuery351032503703519901184_1763996017287");
                if (!flag){
                    System.out.println("当前page"+page+"当前代码"+str);
                    return "结束";
                }
            }
        }
        return "补全部分涨跌数据成功";
    }

    @Autowired
    private HoldOnService holdOnService;
    @GetMapping("sim/buy/my")
    public String simBuyMy(@RequestParam(value = "code")String code){
        holdOnService.buyByMy(code);
        return "成功";
    }

    // 更新最低点
    @GetMapping("rebuild/other/data")
    public String rebuildOtherData(){
        companyOtherService.rebuildData();
        return "成功";
    }


    @Autowired
    private GetGaiNian getGaiNian;
    @GetMapping("get/all/plate")
    public String getAllConcept(){
        getGaiNian.getAll();
        return "成功";
    }



    @GetMapping("export/low")
    public String exportLow(){
        companyOtherService.exportAllData();
        return "导出成功";
    }

}
