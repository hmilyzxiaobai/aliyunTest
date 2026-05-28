package example.dongfangcaifu.service;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import example.dongfangcaifu.httpUtils.HttpRefererEnum;
import example.dongfangcaifu.httpUtils.HttpUrlUtils;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.utils.ExcelWriter;
import example.dongfangcaifu.utils.TimeChangeUtils;
import example.util.UpAnalyse;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.type.IntegerType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AnalyseCompanyDm {
    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private HttpUrlUtils httpUrlUtils;
    @Autowired
    private GetNewPrice getNewPrice;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd--HHmm");
    static DateTimeFormatter formatPath = DateTimeFormatter.ofPattern("yyyyMMdd");

    static final String dfSave;
    static final String dfPath;

    static {
        LocalDateTime currentDateTime = LocalDateTime.now();
        dfSave = currentDateTime.format(formatter);
    }
    static {
        LocalDateTime currentDateTime = LocalDateTime.now();
        dfPath = currentDateTime.format(formatPath);
    }

    static List<List<String>> dataExcel = new ArrayList<>();

    public void analyse(Integer typeDm){
        List<CompanyInfoEntity> companyInfos = companyInfoService.getAll();

        for(CompanyInfoEntity info:companyInfos){
            getToday(typeDm,info.getCompanyName(),info.getCompanyCode(),changeEnums(info.getAppearMarket()));
        }

        if (!CollectionUtils.isEmpty(dataExcel)){
            ExcelWriter.ExcelWrite(dataExcel,changeTypeTime(typeDm)+dfSave,dfPath);
        }

    }

    private String changeTypeTime(Integer integer){
        if (integer==0){
            return "不剔除早上";
        }else return "剔除早上情况";
    }


    //@Async
    private void getToday(Integer typeDm,String name, String code, String type){
        try{
            long l = System.currentTimeMillis();
            String urlString =
                    "https://push2.eastmoney.com/api/qt/stock/fflow/kline/get?lmt=0&klt=1&secid="+type+"."+code+"&fields1=f1,f2,f3,f7&fields2=f51,f52,f53,f54,f55,f56&ut=fa5fd1943c7b386f172d6893dbfba10b&cb=jQuery351024995127899668423_"+l+"&_="+l+1;
         //   https://push2.eastmoney.com/api/qt/stock/fflow/kline/get?lmt=0&klt=1&secid=1.603206&fields1=f1,f2,f3,f7&fields2=f51,f52,f53,f54,f55,f56&ut=fa5fd1943c7b386f172d6893dbfba10b&cb=jQuery351024995127899668423_1741877124788&_=1741877124789
            String newStr = getString(urlString, l);
            JSON parse = JSONUtil.parse(newStr);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            String dataRead = jsonObject.get("klines").toString();
            JSONArray objects = JSONUtil.parseArray(dataRead);
            List<Integer> capitals = new ArrayList<>();
            boolean flagTime = false;
            for(Object o:objects){
                String string = o.toString();
                String[] split = string.split(",");

                // 要是下午两点半，可以再跑一下从1点开始的 是否有庄进入

                if (!flagTime){
                    if (TimeChangeUtils.changeDate(split[0])){
                        flagTime=true;
                    }
                }

                String s = split[1];
                if (typeDm==1 && flagTime){
                    capitals.add(dealCapital(s));
                }else {
                    capitals.add(dealCapital(s));
                }

            }
            if (readList(code,capitals)){
                // 写入表格  获取当前价格
                List<String> row = new ArrayList<>();
                String newPrice = getNewPrice.getNewPrice(code, type);
                if (StringUtils.isBlank(newPrice)){
                   return;
                }
                row.add(dfSave);
                row.add(name);
                row.add(code);
                row.add(newPrice);
                addSave(row);
            }
        }catch (Exception e){
          //  e.printStackTrace();
        }
    }

    private synchronized void addSave(List<String> row){
        dataExcel.add(row);
    }

    @NotNull
    private  String getString(String urlString, long l) throws IOException {
        URL url = new URL(urlString);
        // 打开连接
        HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url, HttpRefererEnum.CODE);
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
        dataAll=dataAll.replace("jQuery351024995127899668423_"+ l +"(","");
        String newStr = dataAll.substring(0, dataAll.length() - 2);
        return newStr;
    }

    private String changeEnums(String content){
        if ("沪A".equals(content)){
            return "1";
        }else return "0";
    }

    private  Integer dealCapital(String str){

        String s = str.split("\\.0")[0];
        if (s.length()<4){
            return 0;
        }else {
            String s1 = s.substring(0, s.length() - 4);
            if (s1.equals("-")|| StringUtils.isBlank(s1)){
                return 0;
            }
            return Integer.parseInt(s1);
        }

    }

    private boolean readList(String code,List<Integer> integers){
        if (code.startsWith("688")){
            return false;
        }

        if ( UpAnalyse.analyse(integers)){
            log.info(code+"该股票涨居多");
            return true;
        }
        return false;

//        int up=0;
//        int down=0;
//        for(int i=1;i<integers.size()-1;i++){
//            if (integers.get(i)<integers.get(i-1)){
//                down++;
//            }else up++;
//        }
//        if (up*100>integers.size()*90){
//            if (integers.get(integers.size()-1)<500){
//                return;
//            }
//            log.info(code+"该股票涨居多");
//        }

    }




}
