package example.dongfangcaifu.service.surveillance;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.CapitalFlowHistoryService;
import example.dongfangcaifu.service.CapitalService;
import example.dongfangcaifu.service.CompanyHistoryService;
import example.dongfangcaifu.src.entity.CapitalFlowHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j

public class HighUpUp {
    @Autowired
    private CompanyHistoryService companyHistoryService;

    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    // 根据股票代码寻找
    public void searchHigh(String code){
        // 首先第一步  找到是否是近十天是否全是涨


        int capUpSum=0;
        int upSum =0;

        // 值得买入的好股票。看资金情况
        List<CapitalFlowHistoryEntity> capList = capitalFlowHistoryService.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery().
                eq(CapitalFlowHistoryEntity::getCompanyCode, code).orderByDesc(CapitalFlowHistoryEntity::getDateHis));
       float sumDetail=0;
        for(int i = capList.size()-1;i>=0;i--){
            float capHis = FloatUtils.stringToFloat(capList.get(i).getCapital());
            float detail = FloatUtils.stringToFloat(capList.get(i).getChangeDetail());
            sumDetail+=detail;
            if (detail>5){
                upSum+=2;
            }
            if (detail>0 && detail<3){
                upSum++;
            }
            if (detail < 0 && detail> -2){
                upSum--;
            }
            if (detail<-2){
                upSum-=2;
            }

            if (capHis>0){
                capUpSum++;
            }
        }
        if (sumDetail<10){
            return;
        }
        if (upSum>13 && capUpSum>=13){
            log.info("股票代码"+code+"为超级利好  长期看涨");
        }

        if (upSum>13 && capUpSum>8 && capUpSum<13 ){
            log.info("股票代码"+code+"为庄家操盘多  适合坐空");
        }



    }

}
