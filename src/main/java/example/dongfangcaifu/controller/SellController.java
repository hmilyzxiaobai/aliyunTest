package example.dongfangcaifu.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.mapper.HoldOnMapper;
import example.dongfangcaifu.service.FinancialInfoDmService;
import example.dongfangcaifu.service.HoldOnService;
import example.dongfangcaifu.service.simulation.SimulationService;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.src.entity.HoldOnEntity;
import example.dongfangcaifu.utils.ExcelSellWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("sell")
@Slf4j
public class SellController {

    @Autowired
    private HoldOnService holdOnService;

    @Autowired
    private FinancialInfoDmService financialInfoDmService;

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private ExcelSellWrite excelSellWrite;

    @GetMapping("sell")
    private void sell(){
        List<HoldOnEntity> all = holdOnService.all();
        if (CollectionUtils.isEmpty(all)){
            return;
        }
        for(HoldOnEntity hold:all){
            String companyCode = hold.getCompanyCode();
            FinancialInfoDmEntity byCodeOne = financialInfoDmService.getByCodeOne(companyCode);

            float v = Float.parseFloat(byCodeOne.getNowPrice());

            float buy = Float.parseFloat(hold.getHoldOnPrice());
            log.info("当前股票单价为："+v+"买入的价格为"+buy);
            if (judge(v,buy)){
                if (v>buy){
                   log.info( "上升超过浮动比例 卖出");
                }else {
                    log.info( "下降亏损超过浮动比例 卖出");
                }
                hold.setSellPrice(byCodeOne.getNowPrice());
                hold.setIsSell("1");
                hold.setUpdateTime("");
                holdOnService.updateById(hold);
            }
        }
    }



    /**
     * 上下逆差超过0.03 则卖出
     * @param v
     * @param buy
     * @return
     */
    private static boolean judge(float v,float buy){
        float abs = Math.abs(v - buy);
        BigDecimal result;
        BigDecimal com= BigDecimal.valueOf(0.03);
        BigDecimal decimal1 = new BigDecimal(Double.toString(abs));
        BigDecimal decimal2 = new BigDecimal(Double.toString(buy));
        // 进行除法运算，保留两位小数，使用四舍五入的方式
        result = decimal1.divide(decimal2, 2, RoundingMode.HALF_UP);
        // 输出结果
        System.out.println(result.toString());
        return result.compareTo(com)>0;
    }


    /**
     * 买入
     */
    @GetMapping("buy")
    public String buy(){
        return simulationService.buy();
    }

    // 计算盈亏
    @GetMapping("cul")
    public String cul(@RequestParam(value = "date") String date){


        List<HoldOnEntity> list = holdOnService.list(Wrappers.<HoldOnEntity>lambdaQuery().eq(HoldOnEntity::getIsSell, 1)
                .gt(HoldOnEntity::getUpdateTime, date));

        StringBuilder sb = new StringBuilder();

        float sum = 0;
        for (HoldOnEntity onEntity:list){
            float sell = Float.parseFloat(onEntity.getSellPrice());
            float buy = Float.parseFloat(onEntity.getHoldOnPrice());
            float yk= (sell-buy)*Float.parseFloat(onEntity.getHoldOnAmount());
            sb.append("当前股票为：").
                    append(onEntity.getCompanyName()).append(",股票代码为：").
                    append(onEntity.getCompanyCode()).append(",买入时候价格为：")
                    .append(onEntity.getHoldOnPrice()).append(",卖出价格为")
                    .append(onEntity.getSellPrice()).append(",盈亏为：").append(yk).append("\n");
            sum+=yk;
        }


        String s = sb.append("总的盈亏为：").append(sum).toString();
        System.out.println(s);
        return s;
    }


    @GetMapping("sell/all")
    public String cellAll(){
        List<HoldOnEntity> all = holdOnService.all();
        if (CollectionUtils.isEmpty(all)){
            return "";
        }
        for(HoldOnEntity hold:all){
            String companyCode = hold.getCompanyCode();
            FinancialInfoDmEntity byCodeOne = financialInfoDmService.getByCodeOne(companyCode);

            float v = Float.parseFloat(byCodeOne.getNowPrice());

            float buy = Float.parseFloat(hold.getHoldOnPrice());
            log.info("当前股票单价为："+v+"买入的价格为"+buy);
            hold.setSellPrice(byCodeOne.getNowPrice());
            hold.setIsSell("1");
           // hold.setUpdateTime("");
            holdOnService.updateById(hold);
        }
        return "全部卖出";
    }


    @PostMapping("excel")
    public String sellExcel(@RequestParam(value = "path")String path,
                            @RequestParam(value = "fileName")String fileName){
        excelSellWrite.sell(path,fileName);
        return "";
    }
}
