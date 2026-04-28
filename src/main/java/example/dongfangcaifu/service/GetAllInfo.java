package example.dongfangcaifu.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import example.dongfangcaifu.httpUtils.HttpRefererEnum;
import example.dongfangcaifu.httpUtils.HttpUrlUtils;
import example.dongfangcaifu.service.email.EmailService;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.utils.DealPrice;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GetAllInfo {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    // 格式化时间并打印

    @Autowired
    private BridgeCompanyService bridgeCompanyService;

    /**
     * 基本保存 但没去重
     */
    @Autowired
    private CompanyInfoService companyInfoService;


    @Autowired
    private CompanyHistoryService companyHistoryService;

    /**
     * 每天信息
     */
    @Autowired
    private FinancialInfoDmService financialInfoDmService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HttpUrlUtils httpUrlUtils;

    private static List<FinancialInfoDmEntity> savesCodeDfList = new ArrayList<>();

    private static List<CompanyInfoEntity> saveComList = new ArrayList<>();
    private synchronized void addSaveList(String companyName,String code,String type,String registrationDate){
        CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
        companyInfoEntity.setCompanyName(companyName);
        companyInfoEntity.setCompanyCode(code);
        companyInfoEntity.setAppearMarket(type);
        companyInfoEntity.setInMarket("是");
        companyInfoEntity.setRegistrationDate(registrationDate);
        //companyInfoEntity.setCompanyNature();
        saveComList.add(companyInfoEntity);
    }

    private synchronized void addSaveCodeDfList(String df,String code,String name
            ,String todayOpen,String todayLow,String yesterdayClose,String earn
            ,String earnAmount,String nowPrice){
        /**
         * addSaveCodeDfList(公司代码,公司名字,今天开,当日最低,昨天收,涨跌幅,涨跌额);
         */
        FinancialInfoDmEntity dm = new FinancialInfoDmEntity();
        dm.setCompanyCode(code);
        dm.setCompanyName(name);
        dm.setStartPrice(todayOpen);
        dm.setLowPrice(todayLow);
        dm.setYesterdayPrice(yesterdayClose);
        dm.setEarnings(String.valueOf(FloatUtils.stringToFloat(earn)/100));
        dm.setTopAmount(earnAmount);
        dm.setNowPrice(nowPrice);
        dm.setDf(df);
        getCapitalNow(code,dm);
        savesCodeDfList.add(dm);
    }

    public synchronized void saveComInfo(Integer type) {
        // 查询的uuid
        String uuid = "PASSWORD";
        String page="1";
        String size = "1";
        int total = 0;
        try {
            // 设置要发送请求的URL
            String urlString =
//                    "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37105249360089472066_"+System.currentTimeMillis()+"&fs=b%3ABK0707&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=9250355984212214%7C0%7C1%7C0%7Cweb&_="+System.currentTimeMillis();
            "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37107591229855109933_1776222019251&fs=b%3ABK0707&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=9250355984212214%7C0%7C1%7C0%7Cweb&_=1776222019258";

            URL url = new URL(urlString);
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url, HttpRefererEnum.CODE);
            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // 输出响应内容
           // System.out.println("响应内容：");
            String dataAll = response.toString();

            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String data = splitTwo[0];

            JSON parse = JSONUtil.parse(data);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            total = Integer.parseInt(jsonObject.get("total").toString());
            System.out.println(dataAll);
            // 关闭连接
            httpUrlUtils.disconnect(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertEntityInfo(total,type);
    }

    private  void insertEntityInfo(Integer total,Integer type){
        int size = 20;
        int page =1;
        String uuid = getUUID();
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,-3);
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        for (int index = 0;index<total;){
            index=page*size;
            digui(format,uuid,String.valueOf(page),
                    String.valueOf(size));
            page++;
        }

        // 查找公司代码的时候就不要插入冗余代码了

        // 这里冗余在一起了 要做分开写 当等于0时 保存信息 其他则进行计算
        if (type==0){
            List<CompanyInfoEntity> all = companyInfoService.queryDistinct();
            List<CompanyInfoEntity> saveComListNew = new ArrayList<>();
            if (!CollectionUtils.isEmpty(all)){
                Set<String> collect = all.stream().map(CompanyInfoEntity::getCompanyCode).collect(Collectors.toSet());
                for(CompanyInfoEntity one:saveComList){
                    if (!collect.contains(one.getCompanyCode())){
                        saveComListNew.add(one);
                    }
                }
            }else {
                saveComListNew=saveComList;
            }
            companyInfoService.saveBatch(saveComListNew);
        }else {
             financialInfoDmService.saveBatch(savesCodeDfList);
            /**
             * 执行计算
             */
        }


    }

    private  void digui(String df,String uuid,String page,String size){
        try {
            // 设置要发送请求的URL
            String urlString =
           //         String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
                    "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37107591229855109933_1776222019251&fs=b%3ABK0707&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=9250355984212214%7C0%7C1%7C0%7Cweb&_=1776222019258";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url, HttpRefererEnum.CODE);
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
//            dataAll=dataAll.replace(uuid,"");
//            dataAll=dataAll.replaceAll("\\(","");
//            dataAll=dataAll.replaceAll("\\);","");

            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String data = splitTwo[0];

            JSON parse = JSONUtil.parse(data);

           // JSON parse = JSONUtil.parse(dataAll);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            dataAll = jsonObject.get("diff").toString();
            System.out.println(dataAll);
            JSONArray objects = JSONUtil.parseArray(dataAll);
            for(int i=0;i<objects.size();i++){
                Object o = objects.get(i);
                JSONObject jsonObject1 = JSONUtil.parseObj(o);
                String 最新价格 = DealPrice.dealPrice(jsonObject1.get("f2",String.class));
                String 板块 = jsonObject1.get("f13",String.class);  // 0 深A  1 沪A
                String 涨跌幅 = jsonObject1.get("f3",String.class);
                String 涨跌额 = jsonObject1.get("f4",String.class);
                String 成交量 = jsonObject1.get("f5",String.class);
                String 成交额 = jsonObject1.get("f6",String.class);
                String 成交额振幅 = jsonObject1.get("f7",String.class);
                String 换手率 = jsonObject1.get("f8",String.class);
                String 市盈率 = jsonObject1.get("f9",String.class);
                String 当日最高 = DealPrice.dealPrice(jsonObject1.get("f15",String.class));
                String 当日最低 = DealPrice.dealPrice(jsonObject1.get("f16",String.class));;
                String 今天开 = jsonObject1.get("f17",String.class);
                String 昨天收 = jsonObject1.get("f18",String.class);
                String 上市时间  = jsonObject1.get("f26",String.class);
                String 公司名字  = jsonObject1.get("f14",String.class);
                String 公司代码  = jsonObject1.get("f12",String.class);
                String 净流入 = jsonObject1.get("f62",String.class);
                // 保存实时信息
            //    addSaveCodeDfList(df,公司代码,公司名字,今天开,当日最低,昨天收,涨跌幅,涨跌额,最新价格);
                // 大盘信息  资金流入情况

                // 再取url保存


                // 保存公司信息
                addSaveList(jsonObject1.get("f14",String.class),jsonObject1.get("f12",String.class)
                        ,convertEnum(jsonObject1.get("f13",String.class)),jsonObject1.get("f26",String.class));

            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void getAllCOdeAndWrite(){
        List<CompanyInfoEntity> list = companyInfoService.list();
        for(CompanyInfoEntity info:list){
            // 爬取实时数据
           // writeOne(info.getCompanyCode());
        }
    }

    /*
    https://quote.eastmoney.com/zs000001.html
    大盘的信息及线  后续可以抓取历史数据
    http://quote.eastmoney.com/center/gridlist.html#hs_a_board
    获取A股的全部信息
    http://quote.eastmoney.com/sz300819.html
    个股的详情
     */


    /**
     * 工具类 获取UUID
     * @return
     */
    private String getUUID(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("当前年月日时分秒：" + formattedDateTime);
        String uuid = UUID.randomUUID().toString()+formattedDateTime;
        //  System.out.println(uuid);
        uuid=uuid.toUpperCase();
        return uuid;
    }

    private String convertEnum(String type){
        switch (type){
            case "0":return "深A";
            case "1":return "沪A";
            default:return "";
        }
    }

    private void getCapitalNow(String code,FinancialInfoDmEntity financialInfoDmEntity){
        try{
            String urlString =
                    "https://push2delay.eastmoney.com/api/qt/ulist.np/get?cb=jQuery112304668133973971953_1729232254223&fltt=2&secids=1."+code+"&fields=f62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf64%2Cf65%2Cf70%2Cf71%2Cf76%2Cf77%2Cf82%2Cf83%2Cf164%2Cf166%2Cf168%2Cf170%2Cf172%2Cf252%2Cf253%2Cf254%2Cf255%2Cf256%2Cf124%2Cf6%2Cf278%2Cf279%2Cf280%2Cf281%2Cf282&ut=b2884a393a59ad64002292a3e90d46a5&_=1729232254224";
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
            // System.out.println("响应内容：");
            String dataAll = response.toString();
            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String data = splitTwo[0];
            System.out.println(data);

            JSONObject jsonObject = JSONUtil.parseObj(data);
            Object capitalNow = jsonObject.get("data");

            if (Objects.isNull(capitalNow) || capitalNow.toString().equals("null")){
                urlString =
                        //   "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35103458189631037627_1715506453184&secid=1.600665&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1715506453297";
                        "https://push2delay.eastmoney.com/api/qt/ulist.np/get?cb=jQuery112304668133973971953_1729232254223&fltt=2&secids=0."+code+"&fields=f62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf64%2Cf65%2Cf70%2Cf71%2Cf76%2Cf77%2Cf82%2Cf83%2Cf164%2Cf166%2Cf168%2Cf170%2Cf172%2Cf252%2Cf253%2Cf254%2Cf255%2Cf256%2Cf124%2Cf6%2Cf278%2Cf279%2Cf280%2Cf281%2Cf282&ut=b2884a393a59ad64002292a3e90d46a5&_=1729232254224";

                url = new URL(urlString);
                // 打开连接
                connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法为GET
                connection.setRequestMethod("GET");
                // 获取响应内容
                BufferedReader inNew = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseNew = new StringBuilder();
                String inputLineNew;
                while ((inputLineNew = inNew.readLine()) != null) {
                    responseNew.append(inputLineNew);
                }
                in.close();
                // 输出响应内容
                // System.out.println("响应内容：");
                dataAll = responseNew.toString();
                splitOne = dataAll.split("\\(");
                splitTwo = splitOne[1].split("\\)");
                data = splitTwo[0];
              //  System.out.println(data);

                JSONObject jsonObjectNew = JSONUtil.parseObj(data);
                capitalNow = jsonObjectNew.get("data");
            }
            JSONObject dataJson = JSONUtil.parseObj(capitalNow);
            JSONArray diff = JSONUtil.parseArray(dataJson.get("diff"));
            for(Object o :diff){
                JSONObject dataOne = JSONUtil.parseObj(o);
                String f62 = dataOne.get("f62").toString();
                String substring62;
                if (f62.length()>4){
                    substring62  = f62.substring(0, f62.length() - 6);
                }else {
                    substring62="0";
                }
                financialInfoDmEntity.setCapitalNow(substring62);
                String f6 = dataOne.get("f6").toString();
                String substring6 = f6.substring(0, f6.length() - 6);
                financialInfoDmEntity.setTotalCapital(substring6);
                financialInfoDmEntity.setProportion(dataOne.get("f184").toString());


            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}

