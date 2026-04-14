package example.dongfangcaifu.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.CompanyOtherMapper;
import example.dongfangcaifu.src.entity.*;
import example.dongfangcaifu.utils.ExcelWriteOne;
import example.dongfangcaifu.utils.FloatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CompanyOtherService extends ServiceImpl<CompanyOtherMapper, CompanyOtherEntity> {

    @Autowired
    private CompanyHistoryService companyHistoryService;

    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    @Autowired
    private CompanyInfoService companyInfoService;



    @Async
    private void updateLow(CompanyInfoEntity info,CompanyOtherEntity companyOtherEntity){

        String companyCode = info.getCompanyCode();

        List<CompanyHistoryEntity> dateHisDesc = companyHistoryService.list(Wrappers.<CompanyHistoryEntity>lambdaQuery()
                .eq(CompanyHistoryEntity::getCompanyCode, companyCode).last("order by date_his desc"));
        float min=999999;
        float avg;
        float high=0;
        float cap;
        float sum=0;
        int len = 0;
        len=Math.min(25,dateHisDesc.size());
        for(int i = 0;i<len-1;i++){
            min= FloatUtils.stringToFloat(dateHisDesc.get(i).getLowPrice())==0?
                    Math.min(min,FloatUtils.stringToFloat(dateHisDesc.get(i).getPrice()))
                    :Math.min(min,FloatUtils.stringToFloat(dateHisDesc.get(i).getLowPrice()));
            high= FloatUtils.stringToFloat(dateHisDesc.get(i).getHighPrice())==0?
                    Math.max(min,FloatUtils.stringToFloat(dateHisDesc.get(i).getPrice()))
                    :Math.max(min,FloatUtils.stringToFloat(dateHisDesc.get(i).getHighPrice()));
            sum+=FloatUtils.stringToFloat(dateHisDesc.get(i).getPrice());
        }
        avg=sum/len;

        List<CapitalFlowHistoryEntity> capHis = capitalFlowHistoryService.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                .eq(CapitalFlowHistoryEntity::getCompanyCode, companyCode).last("order by date_his desc"));
        if (capHis.size()<6){
            return;
        }
        cap=FloatUtils.stringToFloat(capHis.get(0).getCapital())+
                FloatUtils.stringToFloat(capHis.get(1).getCapital())+
                FloatUtils.stringToFloat(capHis.get(2).getCapital())+
                FloatUtils.stringToFloat(capHis.get(3).getCapital())+
                FloatUtils.stringToFloat(capHis.get(4).getCapital());

        //   CompanyOtherEntity companyOtherEntity = new CompanyOtherEntity();
        companyOtherEntity.setCompanyName(info.getCompanyName());
        companyOtherEntity.setCompanyCode(info.getCompanyCode());
        companyOtherEntity.setRecentAvg(String.valueOf(avg));
        companyOtherEntity.setRecentMin(String.valueOf(min));
        companyOtherEntity.setRecentCapital(floatToString(cap));
        companyOtherEntity.setRecentHigh(String.valueOf(high));
        System.out.println("成功更新");
        this.updateById(companyOtherEntity);
    }

    public void rebuildData(){
//        List<CompanyInfoEntity> all = companyInfoService.list(Wrappers.<CompanyInfoEntity>lambdaQuery().eq(CompanyInfoEntity::getAppearMarket,"沪A"));
        List<CompanyInfoEntity> all = companyInfoService.list(Wrappers.<CompanyInfoEntity>lambdaQuery().eq(CompanyInfoEntity::getAppearMarket,"深A"));
        for(CompanyInfoEntity info:all){
           CompanyOtherEntity companyOtherEntity = this.getOne(Wrappers.<CompanyOtherEntity>lambdaQuery().eq(CompanyOtherEntity::getCompanyCode, info.getCompanyCode()));
            if (Objects.isNull(companyOtherEntity)){
              //  continue;
                companyOtherEntity=new CompanyOtherEntity();
            }
            updateLow(info,companyOtherEntity);
           // saveList.add(companyOtherEntity);
        }
        //this.saveBatch(saveList);
   //     this.updateBatchById(saveList);

    }

    private String floatToString(float number){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }
    static  int indexc = 0;
    public boolean judgeLow(List<List<String>>dateExcel,FinancialInfoDmEntity dm){
        String companyCode = dm.getCompanyCode();
        CompanyOtherEntity one = this.getOne(Wrappers.<CompanyOtherEntity>lambdaQuery().eq(CompanyOtherEntity::getCompanyCode, companyCode));
        indexc++;
      //  System.out.println("indexc:"+indexc);
        if (Objects.isNull(one)){
            return true;
        }

        Float avg = FloatUtils.stringToFloat(one.getRecentAvg());
        Float min = FloatUtils.stringToFloat(one.getRecentMin());
        Float high = FloatUtils.stringToFloat(one.getRecentHigh());
        Float capital = FloatUtils.stringToFloat(one.getRecentCapital());

        Float nowPrice = FloatUtils.stringToFloat(dm.getNowPrice());
        Float nowCapital = FloatUtils.stringToFloat(dm.getCapitalNow());


        if (nowPrice>min){
            float earning = FloatUtils.culExMin(nowPrice, min);
            if (earning < 2 || earning > 9){
                 return false;
            }
            if (capital<0){
                return false;
            }
            float earningUp = FloatUtils.culExMin(high, nowPrice);
            if (earningUp < 15){
                return false;
            }

            //  需要找今天流入的值和前两天流入的值 至少要有一天是正
            if (nowCapital >0){
                // 当天的流入为正，又是最低点 可以买入

                List<String> row = new ArrayList<>();

                row.add(new Date().toString());
                row.add(dm.getCompanyName());
                row.add(dm.getCompanyCode());
                row.add(dm.getNowPrice());
                dateExcel.add(row);
                System.out.println("该股票是为当前最低点 而且今天有资金流入 可以直接买入"+"股票代码为："+companyCode+",公司名称为："+dm.getCompanyName());
                System.out.println("该股票是为当前最低点 而且今天有资金流入 可以直接买入"+"股票代码为："+companyCode+",公司名称为："+dm.getCompanyName());
            }else {
                List<CapitalFlowHistoryEntity> list = capitalFlowHistoryService.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                        .eq(CapitalFlowHistoryEntity::getCompanyCode, dm.getCompanyCode()).last("order by date_his desc limit 3"));
                if (FloatUtils.stringToFloat(list.get(0).getCapital())>0
                     || FloatUtils.stringToFloat(list.get(1).getCapital())>0){
               //     System.out.println("该股票是为当前最低点 而且最近有资金流入 可以直接买入"+"股票代码为："+companyCode+",公司名称为："+dm.getCompanyName());
               //     System.out.println("该股票是为当前最低点 而且最近有资金流入 可以直接买入"+"股票代码为："+companyCode+",公司名称为："+dm.getCompanyName());
                    List<String> row = new ArrayList<>();
                    row.add(new Date().toString());
                    row.add(dm.getCompanyName());
                    row.add(dm.getCompanyCode());
                    row.add(dm.getNowPrice());
                    dateExcel.add(row);
                }
            }
        }
        judgeMin(dm.getCompanyCode(),dm.getCompanyName(),nowPrice,min,avg,high);
        return false;
    }
    private void judgeMin(String code,String name,float nowPrice,float min,float avg,float high){
        float v = nowPrice - min;
        float v1 = high - min;
        String max = String.format("%.4f", v1/min);
        String nowC = String.format("%.4f", v/min);
        if (nowPrice<avg &&
                FloatUtils.stringToFloat(max)>0.2 && FloatUtils.stringToFloat(nowC)<5){
            System.out.println("该股票当前比较低 考虑实际情况买入"+"股票代码为："+code+",公司名称为："+name);
        }
    }

    public void exportAllData(){
        List<CompanyOtherEntity> list = this.list();
        List<List<String>> data = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("公司名称");
        header.add("公司代码");
        header.add("最低点");
        for(CompanyOtherEntity one:list){
            List<String> dataOne = new ArrayList<>();
            dataOne.add(one.getCompanyName());
            dataOne.add(one.getCompanyCode());
            dataOne.add(one.getRecentMin());
            data.add(dataOne);
        }
        ExcelWriteOne.writeExcel(header,data);
    }


}
