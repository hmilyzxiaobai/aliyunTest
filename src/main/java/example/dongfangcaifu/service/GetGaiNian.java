package example.dongfangcaifu.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import example.dongfangcaifu.src.entity.Plate;
import example.dongfangcaifu.src.entity.PlateHis;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
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
public class GetGaiNian {

    static final ArrayList<Plate> savePlateList = new ArrayList<>();
    static final ArrayList<PlateHis> savePlateHisList = new ArrayList<>();


    @Autowired
    private PlateHisService plateHisService;
    @Autowired
    private PlateService plateService;

    public  void getAll(){
        savePlateList.clear();
        savePlateHisList.clear();

        // 查询的uuid
        long l = System.currentTimeMillis();
        int total = 0;
       // try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
        try {
            // 设置要发送请求的URL
            String urlString =
                    //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
           //         "https://41.push2.eastmoney.com/api/qt/clist/get?cb=jQuery35104660061245692523_"+System.currentTimeMillis()+"&pn="+page+"&pz="+size+"&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=|0|0|0|web&fid=f26&fs=b:BK0707&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_="+System.currentTimeMillis();
         "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery112307839500841037189"+ l +"&fs=m%3A90%2Bt%3A3%2Bf%3A!50&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf20%2Cf8%2Cf104%2Cf105%2Cf128%2Cf140%2Cf141%2Cf207%2Cf208%2Cf209%2Cf136%2Cf222&fid=f3&pn=1&pz=20&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_="+ l;

            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 获取响应内容
            InputStream inputStream = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // 输出响应内容
            // System.out.println("响应内容：");
            String dataAll = response.toString();
            dataAll=dataAll.replace("jQuery112307839500841037189"+l+"(","");
//            dataAll=dataAll.replaceAll("\\(","");
//            dataAll=dataAll.replaceAll("\\);","");
            String newStr = dataAll.substring(0, dataAll.length() - 2);
            JSON parse = JSONUtil.parse(newStr);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            total = Integer.parseInt(jsonObject.get("total").toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertEntityInfo(total);
    }



    private  void insertEntityInfo(Integer total){
        int size = 40;
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
        for(PlateHis plateHis:savePlateHisList){
            String changePercent = plateHis.getChangePercent();
            Float v = FloatUtils.stringToFloat(changePercent);
            if(v>300){
                log.info("该板块出现增长异动，请及时关注，板块为：{}，板块代码为{}，板块涨幅为：{}，涨跌个数分别为{}，{}"
                        ,plateHis.getConceptName(),plateHis.getConceptCode(),plateHis.getChangePercent(),plateHis.getUpAmount(),plateHis.getDownAmount());
            }


        }
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("当前时间的小时数是: " + hourOfDay);
        if (hourOfDay>15  ){
            log.info("板块信息保存成功");
            plateHisService.addBatch(savePlateHisList);
            plateService.addBatch(savePlateList);
        }
        // 保存

    }


    private  void digui(String page,String size,String df){
        long l = System.currentTimeMillis();
        try {
            // 设置要发送请求的URL
            String urlString =
                    //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
                    "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37104870925910688273_"+ l +"&fs=m%3A90%2Bt%3A3%2Bf%3A!50&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf20%2Cf8%2Cf104%2Cf105%2Cf128%2Cf140%2Cf141%2Cf207%2Cf208%2Cf209%2Cf136%2Cf222&fid=f3&pn="+page+"&pz="+size+"&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_="+ l;
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            dataAll=dataAll.replace("jQuery37104870925910688273_"+l+"(","");
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

                String 涨跌幅度百分比 = jsonObject1.get("f3",String.class);  // 0 深A  1 沪A
                String 涨跌额 = jsonObject1.get("f4",String.class);
                String 换手率 = jsonObject1.get("f8",String.class);

                String 总市值 = jsonObject1.get("f20",String.class);
                String 最新价格 = jsonObject1.get("f2",String.class);
                String 上涨个数 = jsonObject1.get("f104",String.class);
                String 下跌个数 = jsonObject1.get("f105",String.class);

                // 保存实时信息
                addPate(板块名字,板块代码,df);
                addPateHis(板块名字,板块代码,最新价格,涨跌幅度百分比,涨跌额,总市值,换手率,上涨个数,下跌个数,df);
            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private synchronized void addPateHis(String name,
                                         String code,
                                         String price,
                                         String percent,
                                         String changePrice,
                                         String allValue,
                                         String rate,
                                         String up,
                                         String down,
                                         String df){
        PlateHis plateHis = new PlateHis();
        plateHis.setDf(df);
        plateHis.setPrice(price);
        plateHis.setConceptCode(code);
        plateHis.setConceptName(name);
        plateHis.setChangePercent(percent);
        plateHis.setAllValue(allValue);
        plateHis.setTurnoverRate(rate);
        plateHis.setChangePrice(changePrice);
        plateHis.setUpAmount(up);
        plateHis.setDownAmount(down);
        savePlateHisList.add(plateHis);


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
