package example.dongfangcaifu.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.*;
import example.dongfangcaifu.service.email.EmailService;
import example.dongfangcaifu.src.entity.CapitalFlowHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {


    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    @Autowired
    private AnalyseCompanyDm analyseCompanyDm;
    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GetNowAndSendShenA getNowAndSendShenA;


    @GetMapping("test")
    public String ceshiHsk(){
        return "hello 测试成功";
    }
    public String test() {
        StringBuilder sb = new StringBuilder();

        List<CompanyInfoEntity> all = companyInfoService.getAll();
        int allImy =0;
        int allUp= 0;
        for (CompanyInfoEntity companyInfo:all) {
            String code = companyInfo.getCompanyCode();
            if (code.startsWith("688")){
                continue;
            }

//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//            System.out.println("当前时间的小时数是: " + hourOfDay);

            int upW = 0;
            int inW = 0;

            // 大于下午两点 基本稳盘，可以开始看哪些值得买进
            // 查看第二天涨停情况 或者第三天情况

            List<CapitalFlowHistoryEntity> hisData = capitalFlowHistoryService.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                    .eq(CapitalFlowHistoryEntity::getCompanyCode, code).last("order by date_his desc"));
            if (CollectionUtils.isEmpty(hisData)){
                continue;
            }
            allImy++;
            if (FloatUtils.stringToFloat(hisData.get(0).getChangeDetail())>0){
                allUp++;
            }
            float f = FloatUtils.stringToFloat(hisData.get(0).getCapital());
            if (f<0){
                continue;
            }
            for (int i = hisData.size() - 1; i >= 1; i--) {
                CapitalFlowHistoryEntity capitalFlowHistoryEntity = hisData.get(i);
                float v = FloatUtils.stringToFloat(capitalFlowHistoryEntity.getCapital());
                if (v > f) {
                    upW++;
                    // 历史上当天买进的大于今天买进的 看看第二天是否涨
                    if (FloatUtils.stringToFloat(hisData.get(i - 1).getChangeDetail()) > 0) {
                        // 必定为涨价
                        inW++;
                    }
                }

            }

            if (inW*100>upW*85){
                System.out.println("当前股票代码为："+ code);
                System.out.println("买入并且第二天涨的次数为" + inW);
                System.out.println("买入大于该值的次数为" + upW);

                }
            if (inW>6){
                sb.append(companyInfo.getCompanyName()).append("代码为：")
                        .append(companyInfo.getCompanyCode())
                        .append("买入并且第二天涨的次数为")
                        .append(inW)
                        .append("买入大于该值的次数为")
                        .append(upW).append("\n\n");
            }
        }
        System.out.println("今天能买的总数为："+allImy);
        System.out.println("今天涨的总数为："+allUp);
        sb.append("今天能买的总数为：").append(allImy).append("\n")
                .append("今天涨的总数为：").append(allUp);
        emailService.sendEmail("2953872785@qq.com","每天汇总",sb.toString());
        return sb.toString();
    }


    @GetMapping("test/dao")
    public void testDao(){
        capitalFlowHistoryService.SearchGui();
    }

    @GetMapping("test/shen")
    public void testShen(){
      //  getNowAndSendShenA.saveComInfo();
    }

    @Autowired
    private GetAllInfoShenA getAllInfoShenA;

    @GetMapping("test/info/shen")
    public void testShenInfo(){
        getAllInfoShenA.saveComInfo(0);
    }

    @GetMapping("write/plate")
    public String writePlate(){
       return companyInfoService.writePlates();
    }

    @GetMapping("up/dm")
    public String upDm(){
         analyseCompanyDm.analyse(0);
        return "完成";
    }
    @GetMapping("up/dm/two")
    public String upDmTwo(){
        analyseCompanyDm.analyse(1);
        return "完成";

    }

}
