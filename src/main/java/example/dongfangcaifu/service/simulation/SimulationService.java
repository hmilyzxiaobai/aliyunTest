package example.dongfangcaifu.service.simulation;


import cn.hutool.core.date.DateUtil;
import example.dongfangcaifu.mapper.CompanyInfoMapper;
import example.dongfangcaifu.service.FinancialInfoDmService;
import example.dongfangcaifu.service.HoldOnService;
import example.dongfangcaifu.service.SendHisService;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.src.entity.HoldOnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SimulationService {
    // 模拟炒股 将今天的上涨股票买入

    @Autowired
    private FinancialInfoDmService financialInfoDmService;

    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Autowired
    private HoldOnService holdOnService;

    public String buy(){
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        List<String> strings = companyInfoMapper.queryBySend(format);
        // 买入
        StringBuilder sb = new StringBuilder();

        float f = 0;

        for(String code:strings){
            FinancialInfoDmEntity byCodeOne = financialInfoDmService.getByCodeOne(code);
            HoldOnEntity holdOnEntity = new HoldOnEntity();
            holdOnEntity.setHoldOnAmount("100");
            holdOnEntity.setCompanyCode(byCodeOne.getCompanyCode());
            holdOnEntity.setHoldOnPrice(byCodeOne.getNowPrice());
            holdOnEntity.setCompanyName(byCodeOne.getCompanyName());
            holdOnEntity.setIsSell("0");
            holdOnService.save(holdOnEntity);
            float v = Float.parseFloat(byCodeOne.getNowPrice());
            f= f+v*100;
            sb.append("买入股票为：").append(byCodeOne.getCompanyName()).append("\n");
        }
        return "总共买进股票钱为："+f+"；分别买入的股票为：\n"+sb.toString();
    }
    // 计算营收


}
