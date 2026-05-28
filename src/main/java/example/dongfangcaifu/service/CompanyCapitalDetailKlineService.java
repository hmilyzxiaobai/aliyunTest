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
import example.dongfangcaifu.mapper.CompanyCapitalDetailKlineMapper;
import example.dongfangcaifu.mapper.CompanyHistoryMapper;
import example.dongfangcaifu.src.entity.CompanyCapitalDetailKlineEntity;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanyCapitalDetailKlineService extends ServiceImpl<CompanyCapitalDetailKlineMapper, CompanyCapitalDetailKlineEntity> {
    @Autowired
    private HttpUrlUtils httpUrlUtils;
    @Autowired
    private CompanyInfoService companyInfoService;
    static List<CompanyCapitalDetailKlineEntity> saveKline = new ArrayList<>();
    static boolean flag = true;
    public void saveDetailKines(int i){
        saveKline.clear();
        flag=true;
        List<CompanyInfoEntity> companyInfos = companyInfoService.getAll();
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,-3);
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        System.out.println("timestamp = " + format);
//        List<String> collect = companyInfos.stream().map(CompanyInfoEntity::getCompanyCode).collect(Collectors.toList());
//        i=collect.indexOf("688449");
        for(;i<companyInfos.size();i++){
            System.out.println("到达此节点"+i);
            if (!flag) {
            System.out.println("此节点保存失败"+i);
            break;
            }
            CompanyInfoEntity info = companyInfos.get(i);
            digui(info.getCompanyCode(),format,info.getCompanyName(),changeEnums(info.getAppearMarket()));
        }
        if(i==0){
            this.remove(Wrappers.<CompanyCapitalDetailKlineEntity>lambdaQuery().eq(CompanyCapitalDetailKlineEntity::getDateHis,format));
        }

        // 每批次插入 1000 条（最优大小，可根据数据库调整）

        log.info("结束{}",i);
        this.saveBatch(saveKline);
    }

    private void digui(String code,String df,String companyName,String type){
        try{
            long l = System.currentTimeMillis();
            String urlString =
                    "https://push2.eastmoney.com/api/qt/stock/fflow/kline/get?cb=jQuery1123015214007266826168_"+l+"&lmt=0&klt=1&fields1=f1%2Cf2%2Cf3%2Cf7&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61%2Cf62%2Cf63%2Cf64%2Cf65&ut=b2884a393a59ad64002292a3e90d46a5&secid="+type+"."+code+"&_="+l;
                 //   "https://push2.eastmoney.com/api/qt/stock/fflow/kline/get?cb=jQuery112306081271775131549_"+l+"&lmt=0&klt=1&fields1=f1%2Cf2%2Cf3%2Cf7&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61%2Cf62%2Cf63%2Cf64%2Cf65&ut=b2884a393a59ad64002292a3e90d46a5&secid=0."+code+"&_="+l;
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url,code,HttpRefererEnum.HUDATA);

            connection.setRequestMethod("GET");
            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            connection.disconnect();
            // 输出响应内容
            //System.out.println("响应内容：");
            String dataAll = response.toString();

            dataAll=dataAll.replace("jQuery1123015214007266826168_"+l+"(","");
            String newStr = dataAll.substring(0, dataAll.length() - 2);

            System.out.println(newStr);
            JSON parse = JSONUtil.parse(dataAll);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            String dataRead = "";
            JSONArray objects;
           // String name = jsonObject.get("name").toString();
            if (!jsonObject.isEmpty()){
                dataRead=jsonObject.get("klines").toString();
                //String code = jsonObject.get("code").toString();
                objects = JSONUtil.parseArray(dataRead);
            } else {
                 urlString =
                         "https://push2.eastmoney.com/api/qt/stock/fflow/kline/get?cb=jQuery1123015214007266826168_"+l+"&lmt=0&klt=1&fields1=f1%2Cf2%2Cf3%2Cf7&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61%2Cf62%2Cf63%2Cf64%2Cf65&ut=b2884a393a59ad64002292a3e90d46a5&secid=1."+code+"&_="+l;
                 url = new URL(urlString);
                // 打开连接
                HttpURLConnection connection1 = httpUrlUtils.httpBuildUrlUtils(url,code,HttpRefererEnum.HUDATA);

                connection1.setRequestMethod("GET");
                // 获取响应内容
                BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                StringBuilder response1 = new StringBuilder();
                String inputLine1;
                while ((inputLine1 = in1.readLine()) != null) {
                    response1.append(inputLine1);
                }
                in1.close();
                // 输出响应内容
                //System.out.println("响应内容：");
                dataAll = response1.toString();

                dataAll=dataAll.replace("jQuery1123015214007266826168_"+l+"(","");
                newStr = dataAll.substring(0, dataAll.length() - 2);

                System.out.println(newStr);
                parse = JSONUtil.parse(dataAll);
                jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
                dataRead = jsonObject.get("klines").toString();
                //String code = jsonObject.get("code").toString();

                 objects = JSONUtil.parseArray(dataRead);
                connection1.disconnect();
            }
            if (CollectionUtils.isEmpty(objects)){
                return;
            }
            for(Object o:objects) {
                String string = o.toString();
                String[] split = string.split(",");
                for(int i=0;i<split.length;i++){
                    CompanyCapitalDetailKlineEntity companyCapitalDetailKlineEntity = new CompanyCapitalDetailKlineEntity();
                    companyCapitalDetailKlineEntity.setCompanyName(companyName);
                    companyCapitalDetailKlineEntity.setCompanyCode(code);
                    companyCapitalDetailKlineEntity.setDateHis(df);
                    companyCapitalDetailKlineEntity.setKlineDate(split[i]);
                    i++;
                    //2026-05-15 10:22,1459307.0,727745.0,-2187053.0,4075498.0,-2616191.0"" +
                    companyCapitalDetailKlineEntity.setNetAmount(split[i]);
                    i++;
                    companyCapitalDetailKlineEntity.setLittleOrderAmount(split[i]);
                    i++;
                    companyCapitalDetailKlineEntity.setMiddleOrderAmount(split[i]);
                    i++;
                    companyCapitalDetailKlineEntity.setBigBillAmount(split[i]);
                    i++;
                    companyCapitalDetailKlineEntity.setSuperLargeOrderAmount(split[i]);
                    saveKline.add(companyCapitalDetailKlineEntity);
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
            flag=false;
        }
    }

    private String changeEnums(String content){
        if ("沪A".equals(content)){
            return "1";
        }else return "0";
    }
}
