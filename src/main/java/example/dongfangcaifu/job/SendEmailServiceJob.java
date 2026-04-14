//package example.dongfangcaifu.job;
//
//
//import example.dongfangcaifu.service.FinancialInfoDmService;
//import example.dongfangcaifu.service.GetAllInfo;
//import example.dongfangcaifu.service.GetNowAndSend;
//import example.dongfangcaifu.service.HoldOnService;
//import example.dongfangcaifu.service.email.EmailService;
//import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
//import example.dongfangcaifu.src.entity.HoldOnEntity;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.List;
//
//@Service
//@Slf4j
//public class SendEmailServiceJob {
//    @Autowired
//    private GetAllInfo getAllInfo;
//    @Autowired
//    private HoldOnService holdOnService;
//
//    @Autowired
//    private GetNowAndSend getNowAndSend;
//    @Autowired
//    private FinancialInfoDmService financialInfoDmService;
//    @Autowired
//    private EmailService emailService;
//
//
//    @Scheduled(cron = "0 10,20,30,40,50 * ? * MON-FRI") // 任务将在周一到周五的每个小时的0分钟和30分钟执行
//    public void send(){
//        log.info("执行更新刷取股票实时信息代码");
//        getAllInfo.saveComInfo(1);
//    }
//
//    @Scheduled(cron = "0 15,25,35,45,55 * ? * MON-FRI") // 周一到周五，每天早上9点到4点，每10分钟执行一次
//    public void sendEmail(){
//        log.info("执行推送邮箱");
//        getNowAndSend.saveComInfo();
//    }
//
//    // 执行导入公司配置
//    @Scheduled(cron = "0 0 9 * * ?") // 每天早上9点执行
//    public void writeCompanyAll(){
//        log.info("每天抓取所有公司信息");
//        getAllInfo.saveComInfo(0);
//    }
//
//
//    @Scheduled(cron = "0 10,40 * ? * MON-FRI") // 周一到周五，每天早上9点到4点，每10分钟执行一次
//    /**
//     * 查询持仓是否需要卖出
//     */
//    public void cell(){
//        log.info("执行是否支持卖出定时任务");
//        List<HoldOnEntity> all = holdOnService.all();
//        if (CollectionUtils.isEmpty(all)){
//            return;
//        }
//        for(HoldOnEntity hold:all){
//            String companyCode = hold.getCompanyCode();
//            FinancialInfoDmEntity byCodeOne = financialInfoDmService.getByCodeOne(companyCode);
//            if (byCodeOne.getNowPrice().contains("-")){
//                continue;
//            }
//
//            float v = Float.parseFloat(byCodeOne.getNowPrice());
//
//            float buy = Float.parseFloat(hold.getHoldOnPrice());
//
//            if (judge(v,buy)){
//                if (v>buy){
//                    log.info( "上升超过浮动比例 卖出");
//                  //  emailService.sendEmail("zlldream@hotmail.com", "股票上涨 卖出", byCodeOne.getCompanyName()+"上升超过浮动比例 卖出!");
//                }else {
//                    log.info( "下降亏损超过浮动比例 卖出");
//                  //  emailService.sendEmail("zlldream@hotmail.com", "股票下跌过多 尽快卖出", byCodeOne.getCompanyName()+"下降超过浮动比例 卖出!");
//                }
//                hold.setSellPrice(byCodeOne.getNowPrice());
//                hold.setIsSell("1");
//                holdOnService.updateById(hold);
//
//
//            }
//        }
//    }
//    private static boolean judge(float v,float buy){
//        float abs = Math.abs(v - buy);
//        BigDecimal result;
//        BigDecimal com= BigDecimal.valueOf(0.03);
//        BigDecimal decimal1 = new BigDecimal(Double.toString(abs));
//        BigDecimal decimal2 = new BigDecimal(Double.toString(buy));
//        // 进行除法运算，保留两位小数，使用四舍五入的方式
//        result = decimal1.divide(decimal2, 2, RoundingMode.HALF_UP);
//        // 输出结果
//        System.out.println(result.toString());
//        return result.compareTo(com)>0;
//    }
//
//
//    /**
//     * 钩子计算
//     */
//
//
//
//
//}
