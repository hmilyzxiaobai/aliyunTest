package example.dongfangcaifu.service;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.CompanyInfoMapper;
import example.dongfangcaifu.mapper.FinancialInfoDMMapper;
import example.dongfangcaifu.src.entity.CapitalFlowHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.src.response.CapitalFlowHistoryResponse;
import example.dongfangcaifu.utils.FloatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinancialInfoDmService extends ServiceImpl<FinancialInfoDMMapper, FinancialInfoDmEntity> {

    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    @Autowired
    private CompanyHistoryService companyHistoryService;

    public List<FinancialInfoDmEntity> dateByCode(String code){
        return this.list(Wrappers.<FinancialInfoDmEntity>lambdaQuery()
                .eq(FinancialInfoDmEntity::getCompanyCode,code));
    }

    /**
     * 拿到最新价格  虽然可能不准 但是能拿
     * @param code
     * @return
     */
    public FinancialInfoDmEntity getByCodeOne(String code){
        return this.getOne(Wrappers.<FinancialInfoDmEntity>lambdaQuery().
                eq(FinancialInfoDmEntity::getCompanyCode,code).last("order by id desc  limit 1"));
    }

    public List<FinancialInfoDmEntity> getAfterTwoDownUp(String dateIn){
        List<FinancialInfoDmEntity> list = this.list(Wrappers.<FinancialInfoDmEntity>lambdaQuery().gt(FinancialInfoDmEntity::getCreateTime, dateIn));
        List<FinancialInfoDmEntity> codes = new ArrayList<>();
        for(FinancialInfoDmEntity dm:list){
            if (dm.getCompanyCode().startsWith("668") || dm.getCompanyCode().startsWith("688")){
                continue;
            }
            if (FloatUtils.stringToFloat(dm.getEarnings())>3 && FloatUtils.stringToFloat(dm.getProportion())<0){
                codes.add(dm);
            }
        }
        return codes;

    }

    public List<Map.Entry<String, Float>> getAfterTwo(String dateIn,String dateIn2){

        StringBuilder sb = new StringBuilder();

        String dateInNew = dateIn+ " 14:00:00";

        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        format = format+" "+dateIn;

        /**
         * 构建评分系统
         * 涨的系数  涨跌系数为 n    资金流入百分比系数为 m  资金流入额为 ca
         * 前一天涨的系数为 n1  资金流入百分比为 m1         资金流入额为 ca1
         * 前两天涨的系数为 n2  资金流入百分比为 m2         资金流入额为 ca2
         * 前三天涨价的系数为 n3 资金流入百分比为 m3         资金流入额为 ca3
         *
         *
         * 计算公式    净流入资金/1000 * 占比系数  如果两个都为负数 则结果为负数  如果一正一负  为负数
         *  当天上涨为  系数为 y = (y - ( 大盘 + 板块 ) ) * 10
         *  当天占比百分之70
         *
         */

        List<FinancialInfoDmEntity> financialInfoDmEntities = companyInfoMapper.queryAfterTwo(dateInNew,dateIn2);


        Map<String,Float> mapRes = new HashMap<>();

        for(FinancialInfoDmEntity dm:financialInfoDmEntities) {
            String proportion = dm.getProportion();
//            if (FloatUtils.stringToFloat(proportion)<5){
//                continue;
//            }
            String companyCode = dm.getCompanyCode();
            List<CapitalFlowHistoryEntity> hisData = capitalFlowHistoryService.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                    .eq(CapitalFlowHistoryEntity::getCompanyCode, companyCode).lt(CapitalFlowHistoryEntity::getDateHis,dateIn).last(" order by date_his desc"));
            if (CollectionUtils.isEmpty(hisData)){
                continue;
            }

//
//            List<CompanyHistoryEntity> list = companyHistoryService.list(Wrappers.<CompanyHistoryEntity>lambdaQuery()
//                    .eq(CompanyHistoryEntity::getCompanyCode, companyCode));
//            Map<String, String> datePrice = list.stream().collect(Collectors.toMap(CompanyHistoryEntity::getDateHis, CompanyHistoryEntity::getPrice));
//



            float today = (calculate(dm.getCapitalNow(),dm.getProportion())
                    + FloatUtils.stringToFloat(dm.getEarnings())*10)*7/10;

            CapitalFlowHistoryEntity capitalFlowHistoryEntity = hisData.get(0);

            float after1 = (calculate(capitalFlowHistoryEntity.getCapital(),capitalFlowHistoryEntity.getProportion())
                    + FloatUtils.stringToFloat(capitalFlowHistoryEntity.getChangeDetail())*10)*2/10;

            CapitalFlowHistoryEntity cap1 = hisData.get(1);
            float after2 = (calculate(cap1.getCapital(),cap1.getProportion())
                    + FloatUtils.stringToFloat(cap1.getChangeDetail())*10)*1/10;



            Float allR = today* + after1 + after2;

            mapRes.put(dm.getCompanyCode()+"-"+dm.getCompanyName(),allR);
//
//
//
//            // 应当再加一个逻辑 下一天的是否有高峰点 也可以算做增长点
//            int upUp = 0;
//            int upDown=0;
//            int upDownUpPrice = 0;
//            int in = 0;
//
//            for(int i=0;i<hisData.size()-2;i++){
//                CapitalFlowHistoryEntity one = hisData.get(i);
//                String capital = one.getCapital();
//                Float capitalFloat = FloatUtils.stringToFloat(capital);
//                if (capitalFloat>0){
//                    in++;
//                }else {
//                    continue;
//                }
//                float changeDetail = FloatUtils.stringToFloat(hisData.get(i+1).getChangeDetail());
//                if (changeDetail>0){
//                    upUp++;
//                }else {
//                    upDown++;
//                }
//                try {
//                    if (i<hisData.size()-3){
//                        Float onePrice = FloatUtils.stringToFloat(datePrice.get(one.getDateHis()));
//                        Float afterPrice = FloatUtils.stringToFloat(datePrice.get(hisData.get(i + 2).getDateHis()));
//                        if (afterPrice>onePrice&&changeDetail<0){
//                            upDownUpPrice++;
//                        }
//                    }
//                }catch (Exception e){
//                }
//
//            }
//            if (in*60 > upUp*100 && in*70 > (upUp+upDownUpPrice)*100){
//                continue;
//            }
//            int flagL = 0;
//            int flagR = 0;
//            if (in*60 < upUp*100 ){
//                flagL=1;
//            }
//            if (in*70 < (upUp+upDownUpPrice)*100 ){
//                flagR=1;
//            }
//
//            sb.append("当前股票为：")
//                    .append(dm.getCompanyName())
//                    .append(",股票代码为：")
//                    .append(dm.getCompanyCode()).append(",").append(addMessage(flagL,flagR))
//                    .append(",历史数据为：买入次数为")
//                    .append(in).append(",买入后第二天依旧为涨的次数为").append(upUp)
//                    .append(",买入后第二天为跌的次数为").append(upDown)
//                    .append(",买入后跌，庄家洗盘，第三天涨的次数为").append(upDownUpPrice).append("\n\n");
//
//
        }

        // 对res进行排序 取前十
        return sortMap(mapRes);

        /**
         * 倒反天罡的选取  今天为跌 赌明天上涨，
         */

      //  return sb.toString();
    }


    private Float calculate(String f1,String f2){
        Float aFloat1 = FloatUtils.stringToFloat(f1)/10000000;
        Float aFloat2 = FloatUtils.stringToFloat(f2);
        if (aFloat1<0&&aFloat2<0){
            return 0 - aFloat1*aFloat2*aFloat2;
        }
        return aFloat1*aFloat2*aFloat2;
    }

    private String addMessage(int flag1 ,int flag2){
        if (flag1==1&&flag2==1){
            return "当前股第二天涨比大于70%，而且第三天会继续涨";
        }
        if (flag1==1&&flag2==0){
            return "当前股第二天涨比大于70%";
        }
        if (flag1==0&&flag2==1){
            return "当前股第三天会涨";
        }
        return "数据有些许错误";
    }

    private List<Map.Entry<String, Float>> sortMap(Map<String,Float> map){
        if (CollectionUtils.isEmpty(map)){
            return new ArrayList<>();
        }
        List<Map.Entry<String, Float>> list = new ArrayList<>(map.entrySet());
        list.sort((entry1, entry2) -> Float.compare(entry2.getValue(), entry1.getValue()));
        List<Map.Entry<String, Float>> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<10;i++){
            //Map.Entry<String, Float> entry = list.get(i);
            //sb.append(entry.getKey()).append(",分数值为：").append(entry.getValue()).append("\n\n");
            res.add(list.get(i));
        }
        return res;
    }

}
