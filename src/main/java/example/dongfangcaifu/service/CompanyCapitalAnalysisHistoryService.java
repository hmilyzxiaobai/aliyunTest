package example.dongfangcaifu.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.httpUtils.HttpRefererEnum;
import example.dongfangcaifu.httpUtils.HttpUrlUtils;
import example.dongfangcaifu.mapper.CompanyCapitalAnalysisHistoryMapper;
import example.dongfangcaifu.src.entity.CompanyCapitalAnalysisHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.utils.DealPrice;
import example.dongfangcaifu.utils.ExcelWriteNowDay;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class CompanyCapitalAnalysisHistoryService extends ServiceImpl<CompanyCapitalAnalysisHistoryMapper, CompanyCapitalAnalysisHistoryEntity> {

    @Autowired
    private HttpUrlUtils httpUrlUtils;

    static List<CompanyCapitalAnalysisHistoryEntity> saveList = new ArrayList<>();

    public synchronized void saveCompanyCapitalAnalysisHistory() {

        saveList.clear();

        int total = 0;
        try {
            // 设置要发送请求的URL
            String urlString =
                    //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
                    "https://push2.eastmoney.com/api/qt/clist/get?cb=jQuery11230018427465295197898_1768379163740&fid=f62&po=1&pz=50&pn=1&np=1&fltt=2&invt=2&ut=8dec03ba335b81bf4ebdf7b29ec27d15&fs=m%3A0%2Bt%3A6%2Bf%3A!2%2Cm%3A0%2Bt%3A13%2Bf%3A!2%2Cm%3A0%2Bt%3A80%2Bf%3A!2%2Cm%3A1%2Bt%3A2%2Bf%3A!2%2Cm%3A1%2Bt%3A23%2Bf%3A!2%2Cm%3A0%2Bt%3A7%2Bf%3A!2%2Cm%3A1%2Bt%3A3%2Bf%3A!2&fields=f12%2Cf14%2Cf2%2Cf3%2Cf62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf204%2Cf205%2Cf124%2Cf1%2Cf13";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url, HttpRefererEnum.CODE);
            // 设置请求方法为GET

            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String dataAll = response.toString();

            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String data = splitTwo[0];

            JSON parse = JSONUtil.parse(data);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            total = Integer.parseInt(jsonObject.get("total").toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 保存数据
        insertEntityData(total);
    }

    private void insertEntityData(int total){
        int page=1;
        int size = 50;

        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,-3);
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");

        for (int index = 0;index<total;){
            index=page*size;
            digui(String.valueOf(page),
                    String.valueOf(size),format);
            page++;
        }
        // 防止有脏数据
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("当前时间的小时数是: " + hourOfDay);
        // 删除当天数据
        this.remove(Wrappers.<CompanyCapitalAnalysisHistoryEntity>lambdaQuery().eq(CompanyCapitalAnalysisHistoryEntity::getDateHis,format));
        if (hourOfDay>=9){
            this.saveBatch(saveList);
        }
    }


    private  void digui(String page,String size,String df){
        try {
            // 设置要发送请求的URL
            String urlString =
                    //         String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
                    "https://push2.eastmoney.com/api/qt/clist/get?cb=jQuery11230018427465295197898_1768379163740&fid=f62&po=1&pz="+size+"&pn="+page+"&np=1&fltt=2&invt=2&ut=8dec03ba335b81bf4ebdf7b29ec27d15&fs=m%3A0%2Bt%3A6%2Bf%3A!2%2Cm%3A0%2Bt%3A13%2Bf%3A!2%2Cm%3A0%2Bt%3A80%2Bf%3A!2%2Cm%3A1%2Bt%3A2%2Bf%3A!2%2Cm%3A1%2Bt%3A23%2Bf%3A!2%2Cm%3A0%2Bt%3A7%2Bf%3A!2%2Cm%3A1%2Bt%3A3%2Bf%3A!2&fields=f12%2Cf14%2Cf2%2Cf3%2Cf62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf204%2Cf205%2Cf124%2Cf1%2Cf13";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url,HttpRefererEnum.CODE);
            // 设置请求方法为GET

            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String dataAll = response.toString();
            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String data = splitTwo[0];
            log.info("第{}页数据为：{}",page,data);
            JSON parse = JSONUtil.parse(data);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            dataAll = jsonObject.get("diff").toString();
            JSONArray objects = JSONUtil.parseArray(dataAll);
            for(int i=0;i<objects.size();i++){
                Object o = objects.get(i);
                JSONObject jsonObject1 = JSONUtil.parseObj(o);
                addSaveList(jsonObject1,df);
            }
            // 关闭连接
            connection.disconnect();

        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void addSaveList(JSONObject jsonObject1,String df){
        CompanyCapitalAnalysisHistoryEntity com = new CompanyCapitalAnalysisHistoryEntity();
        String 最新价格 = DealPrice.dealPrice(jsonObject1.get("f2",String.class));;
        String 板块 = jsonObject1.get("f13",String.class);  // 0 深A  1 沪A
        String 涨跌幅 = jsonObject1.get("f3",String.class);
        String 公司名字  = jsonObject1.get("f14",String.class);
        String 公司代码  = jsonObject1.get("f12",String.class);
        String 总净额  = jsonObject1.get("f62",String.class);
        String 总占比  = jsonObject1.get("f184",String.class);
        String 超大单净额  = jsonObject1.get("f66",String.class);
        String 超大单占比  = jsonObject1.get("f69",String.class);
        String 大单净额  = jsonObject1.get("f72",String.class);
        String 大单占比  = jsonObject1.get("f75",String.class);
        String 中单净额  = jsonObject1.get("f78",String.class);
        String 中单占比  = jsonObject1.get("f81",String.class);
        String 小单净额  = jsonObject1.get("f84",String.class);
        String 小单占比  = jsonObject1.get("f87",String.class);
        com.setCompanyName(公司名字);
        com.setCompanyCode(公司代码);
        com.setBkType(板块);
        com.setChangeDetails(涨跌幅);
        com.setPrice(最新价格);
        com.setNetAmount(总净额);
        com.setNetProportion(总占比);
        com.setSuperLargeOrderAmount(超大单净额);
        com.setSuperLargeOrderProportion(超大单占比);
        com.setBigBillAmount(大单净额);
        com.setBigBillProportion(大单占比);
        com.setMiddleOrderAmount(中单净额);
        com.setMiddleOrderProportion(中单占比);
        com.setLittleOrderAmount(小单净额);
        com.setLittleOrderProportion(小单占比);
        com.setDateHis(df);
        saveList.add(com);
    }


    // 分析数据


    public Map<Integer,List<List<String>>> statistics(String format){
//        Calendar calendar =  Calendar.getInstance();
//        calendar.setTime(new Date());
//        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
//        // 拿到当天数据

        Map<Integer,List<List<String>>> res =new HashMap<>();
        List<CompanyCapitalAnalysisHistoryEntity> historyList = this.list(Wrappers.<CompanyCapitalAnalysisHistoryEntity>lambdaQuery().eq(CompanyCapitalAnalysisHistoryEntity::getDateHis, format));
        // 对今天进行记录并放到表格中
        List<List<String>> listSUp = new ArrayList<>();
        List<List<String>> listSUpLarge = new ArrayList<>();
        List<List<String>> listSLowLarge = new ArrayList<>();
        List<List<String>> listSLow = new ArrayList<>();
        List<List<String>> listHUp = new ArrayList<>();
        List<List<String>> listHLow = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        headers.add("公司名称");
        headers.add("公司代码");
        headers.add("涨幅");
        headers.add("价格");
        headers.add("30天最高点");
        headers.add("30天最低点");
        for(CompanyCapitalAnalysisHistoryEntity one:historyList){
            String changeDetails = one.getChangeDetails();
            Float v = FloatUtils.stringToFloat(changeDetails);
            // 分层次记录
            int type = 0;
            if (9.5<v&&v<11){
                // 记录涨停  沪A
                type=1;
            }else if(19.5<v&&v<21){
                // 记录港股涨停 深A
                type=2;
            }else if(12.5<v&&v<16){
            // 记录深A大涨
            type=5;
            }
            if (-9.5>v&&v>-11){
                // 记录跌停 沪A
                type=3;
            }else if(-19.5>v&&v>-21){
                // 记录港股跌停 深A
                type=4;
            }else if(-10.5>v&&v>-16){
                //  深A大跌
                type=6;
            }

            if (type==0){
                continue;
            }else {
                List<String> row = new ArrayList<>();
                row.add(one.getCompanyName());
                row.add(one.getCompanyCode());
                row.add(changeDetails);
//               CompanyOtherEntity other = baseMapper.selectOther(one.getCompanyCode());
//               if (Objects.isNull(other)){
//                   log.info(one.getCompanyCode()+one.getCompanyName());
//                   continue;
//               }
                row.add("0") ;
                row.add("0");
                if (type==1){
                    listHUp.add(row);
                }
                if (type==2){
                    listSUp.add(row);
                }
                if (type==3){
                    listHLow.add(row);
                }
                if (type==4){
                    listSLow.add(row);
                }
                if (type==5){
                    listSUpLarge.add(row);
                }
                if (type==6){
                    listSLowLarge.add(row);
                }
            }
            // 写入到excel  分两种情况保存，如果10涨停大概率连续涨停 20涨停不太可能连续涨停
        }
        ExcelWriteNowDay.writeExcel(headers,listHUp,1,format);
        ExcelWriteNowDay.writeExcel(headers,listSUp,2,format);
        ExcelWriteNowDay.writeExcel(headers,listHLow,3,format);
        ExcelWriteNowDay.writeExcel(headers,listSLow,4,format);
        ExcelWriteNowDay.writeExcel(headers,listSUpLarge,5,format);
        ExcelWriteNowDay.writeExcel(headers,listSLowLarge,6,format);
        // 1 沪A涨停 2 深A涨停 3 沪A跌停 4 深A跌停 5 深A大涨 6深A大跌
        res.put(1,listHUp);
        res.put(2,listSUp);
        res.put(3,listHLow);
        res.put(4,listSLow);
        res.put(5,listSUpLarge);
        res.put(6,listSLowLarge);

        return res;
    }
}
