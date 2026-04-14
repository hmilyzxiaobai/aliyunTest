package example.dongfangcaifu.job;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.CapitalService;
import example.dongfangcaifu.service.CompanyInfoService;
import example.dongfangcaifu.service.FinancialInfoDfService;
import example.dongfangcaifu.service.HoldOnService;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDfEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.src.entity.HoldOnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateAll {

    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private FinancialInfoDfService financialInfoDfService;

    @Autowired
    private HoldOnService holdOnService;
    @Autowired
    private CapitalService capitalService;



    private void calculate(){
        List<CompanyInfoEntity> all = companyInfoService.getAll();
    }

    private void calculateContinuous(String code){
        List<FinancialInfoDfEntity> financialInfoDfEntities = financialInfoDfService.dataByCode(code);
        // 天维度
        // 周维度
    }

    public void buy(String date){
        List<FinancialInfoDmEntity> financialInfoDmEntities = companyInfoService.queryBuy(date);
        List<HoldOnEntity> list = holdOnService.list(Wrappers.<HoldOnEntity>lambdaQuery().eq(HoldOnEntity::getIsSell, 0));
        /*
        select * from financial_info_dm where earnings > 0 and earnings < 10 and company_code in (
        select  company_code from company_info where appear_market = '深A' and create_time > '时间'
        );

         */



    }

    // 计算方法

    /*
    1、是否连续上升
    2、相关板块是否上升
    3、成交量，当前涨跌和成交量量差比
    4、尾盘成交量
    5、是否处于低谷  即正弦函数底端
    6、是否有相关重大新闻披露
    7、所在行业是否稳定
    8、上下游行业波动性
    9、被操盘可能性指标
     */
}
