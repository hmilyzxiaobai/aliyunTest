package example.dongfangcaifu.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.httpUtils.HttpRefererEnum;
import example.dongfangcaifu.httpUtils.HttpUrlUtils;
import example.dongfangcaifu.src.entity.Plate;
import example.dongfangcaifu.src.entity.PlateCapitalDetailEntity;
import example.dongfangcaifu.src.entity.PlateHis;
import example.dongfangcaifu.utils.DealPrice;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


@Service
@Slf4j
public class ZhubanPlate {


    static final ArrayList<Plate> savePlateList = new ArrayList<>();
    static final ArrayList<PlateCapitalDetailEntity> savePlateHisList = new ArrayList<>();


    @Autowired
    private PlateCapitalDetailService plateCapitalDetailService;
    @Autowired
    private PlateService plateService;
    @Autowired
    private HttpUrlUtils httpUrlUtils;

    public void getAllPlateZhuBan() {
        savePlateList.clear();
        savePlateHisList.clear();

        // 查询的uuid
        long l = System.currentTimeMillis();

        int total = 0;
        try {
            String urlString =
                    "https://push2.eastmoney.com/api/qt/clist/get?cb=jQuery112309263945695750347_" + l + "&fid=f62&po=1&pz=50&pn=1&np=1&fltt=2&invt=2&ut=8dec03ba335b81bf4ebdf7b29ec27d15&fs=m%3A90+s%3A4&fields=f12%2Cf14%2Cf2%2Cf3%2Cf62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf204%2Cf205%2Cf124%2Cf1%2Cf13";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url, HttpRefererEnum.PLATE);

            // 获取响应内容
            InputStream inputStream = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            in.close();
            // 输出响应内容
            // System.out.println("响应内容：");
            String dataAll = response.toString();
            dataAll = dataAll.replace("jQuery112309263945695750347_" + l + "(", "");
//            dataAll=dataAll.replaceAll("\\(","");
//            dataAll=dataAll.replaceAll("\\);","");
            String newStr = dataAll.substring(0, dataAll.length() - 2);
            JSON parse = JSONUtil.parse(newStr);
            JSONObject jsonObject = JSONUtil.parseObj(parse.getByPath("data"));
            total = Integer.parseInt(jsonObject.get("total").toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        insertEntityInfo(total);
    }
    private  void insertEntityInfo(Integer total) {
        int size = 50;
        int page =1;
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,-3);
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");

        for (int index = 0;index<total;){
            index=page*size;
            digui(String.valueOf(page),
                    String.valueOf(size),format);
            System.out.println(page);
            page++;
        }
        //分析
        for(PlateCapitalDetailEntity plateHis:savePlateHisList){
            String changePercent = plateHis.getChangeDetails();
            Float v = FloatUtils.stringToFloat(changePercent);
            if(v>3){
                log.info("该板块出现增长异动，请及时关注，板块为：{}，板块代码为{}，板块涨幅为：{}"
                        ,plateHis.getConceptName(),plateHis.getConceptCode(),plateHis.getChangeDetails());
            }


        }
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("当前时间的小时数是: " + hourOfDay);
        if (hourOfDay>9  ){
            log.info("板块信息保存成功");
            //plateHisService.addBatch(savePlateHisList);
            plateService.addBatch(savePlateList);
        }
        plateCapitalDetailService.remove(Wrappers.<PlateCapitalDetailEntity>lambdaQuery()
                .eq(PlateCapitalDetailEntity::getDateHis,format));
        plateCapitalDetailService.saveBatch(savePlateHisList);
    }

    private  void digui(String page,String size,String df){
        long l = System.currentTimeMillis();
        try {
            // 设置要发送请求的URL
            String urlString =
                    //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
                    "https://push2.eastmoney.com/api/qt/clist/get?cb=jQuery112309263945695750347_" + l + "&fid=f62&po=1&pz="+size+"&pn="+page+"&np=1&fltt=2&invt=2&ut=8dec03ba335b81bf4ebdf7b29ec27d15&fs=m%3A90+s%3A4&fields=f12%2Cf14%2Cf2%2Cf3%2Cf62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf204%2Cf205%2Cf124%2Cf1%2Cf13";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url, HttpRefererEnum.PLATE);
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // 输出响应内容
            //System.out.println("响应内容：");
            String dataAll = response.toString();

            dataAll=dataAll.replace("jQuery112309263945695750347_"+l+"(","");
            String newStr = dataAll.substring(0, dataAll.length() - 2);

            System.out.println(newStr);
            JSON parse = JSONUtil.parse(dataAll);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            dataAll = jsonObject.get("diff").toString();
            System.out.println(dataAll);
            JSONArray objects = JSONUtil.parseArray(dataAll);
            for(int i=0;i<objects.size();i++){
                Object o = objects.get(i);
                JSONObject jsonObject1 = JSONUtil.parseObj(o);
                String 板块代码 = jsonObject1.get("f12",String.class);
                String 板块名字 = jsonObject1.get("f14",String.class);

                String 最新价格 = jsonObject1.get("f2",String.class);

                String 涨跌幅度百分比 = jsonObject1.get("f3",String.class);
                String 总净额 = jsonObject1.get("f62",String.class);
                String 总占比 = jsonObject1.get("f184",String.class);

                String 超大单净额 = jsonObject1.get("f66",String.class);
                String 超大单占比 = jsonObject1.get("f69",String.class);

                String 大单净额 = jsonObject1.get("f72",String.class);
                String 大单占比 = jsonObject1.get("f75",String.class);

                String 中单净额 = jsonObject1.get("f78",String.class);
                String 中单占比 = jsonObject1.get("f81",String.class);

                String 小单净额 = jsonObject1.get("f84",String.class);
                String 小单占比 = jsonObject1.get("f87",String.class);

                // 保存实时信息
                addPate(板块名字,板块代码,df);
                PlateCapitalDetailEntity com = new PlateCapitalDetailEntity();
                com.setConceptName(板块名字);
                com.setConceptCode(板块代码);
                com.setBkType("主板");
                com.setChangeDetails(涨跌幅度百分比);
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
                savePlateHisList.add(com);
            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private synchronized void addPate(String name,String code,String df){
        Plate plate = new Plate();
        plate.setConceptCode(code);
        plate.setConceptName(name);
        plate.setDfStart(df);
        plate.setIsLive("存活");
        savePlateList.add(plate);
    }


}
