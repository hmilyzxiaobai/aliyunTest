package example.dongfangcaifu.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import example.dongfangcaifu.service.email.EmailService;
import example.dongfangcaifu.src.entity.CapitalFlowHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.src.response.CapitalFlowHistoryResponse;
import example.dongfangcaifu.src.response.GouResponse;
import example.dongfangcaifu.src.response.JudgeVo;
import example.dongfangcaifu.utils.ExcelWriter;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GetNowAndSend {

    ExecutorService executorService = Executors.newFixedThreadPool(10);  // 创建一个固定大小的线程池

    private static Set<FinancialInfoDmEntity> savesCodeDfList = new HashSet<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    private SendHisService sendHisService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyHistoryService companyHistoryService;

    @Autowired
    private CompanyInfoService companyInfoService;


    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    @Autowired
    private CompanyOtherService companyOtherService;

    @Autowired
    private HoldOnService holdOnService;

    public synchronized void saveComInfo() {
        // 查询的uuid
        String uuid = "PASSWORD";
        String page="1";
        String size = "1";
        int total = 0;
        try {
            // 设置要发送请求的URL
            String urlString =
            //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
//            "https://41.push2.eastmoney.com/api/qt/clist/get?cb=jQuery35104660061245692523_1761750620605&pn=1&pz=1&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=|0|0|0|web&fid=f26&fs=b:BK0707&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1761750620605";
             "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37106878664776367346_"+System.currentTimeMillis()+"&fs=b%3ABK0707&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_="+System.currentTimeMillis();
           // 沪A https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery371019142280915970267_1769518123106&fs=b%3ABK0707&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn=1&pz=20&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_=1769518123108
           // 深A https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery371019142280915970267_1769518123106&fs=b%3ABK0804&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn=1&pz=20&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_=1769518123110




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
//            dataAll=dataAll.replace(uuid,"");
//            dataAll=dataAll.replaceAll("\\(","");
//            dataAll=dataAll.replaceAll("\\);","");

            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            dataAll = splitTwo[0];

            JSON parse = JSONUtil.parse(dataAll);
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
        String uuid = getUUID();
        for (int index = 0;index<total;){
            index=page*size;
            digui(uuid,String.valueOf(page),
                    String.valueOf(size));
            page++;
        }

        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,-3);
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        System.out.println("timestamp = " + format);
        int index = 0;
        Map<String,Integer> mapScore = new HashMap<>();


        List<List<String>> dataExcel = new ArrayList<>();

        Map<String, String> pressureMap = companyInfoService.getAllPressure();

        Map<String, String> supporeMap = companyInfoService.getAllSupport();

        Map<String,String> messageSupPre = new HashMap<>();



        for(FinancialInfoDmEntity financialInfoDmEntity:savesCodeDfList){

        }


        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("当前时间的小时数是: " + hourOfDay);
        if(hourOfDay>=15     ){
            log.info("沪A数据保存成功");
            List<CompanyHistoryEntity> saveHis = new ArrayList<>();
            List<CapitalFlowHistoryEntity> saveHisCap = new ArrayList<>();
            for(FinancialInfoDmEntity one :savesCodeDfList){
                CompanyHistoryEntity companyHistory = new CompanyHistoryEntity();
                companyHistory.setCompanyName(one.getCompanyName());
                companyHistory.setPrice(one.getNowPrice());
                companyHistory.setDateHis(format);
                companyHistory.setCompanyCode(one.getCompanyCode());
                companyHistory.setChangeDetails(one.getEarnings());
                companyHistory.setLowPrice(one.getLowPrice());
                companyHistory.setHighPrice(one.getTopIncrease());
                companyHistory.setTurnoverRate(one.getTurnoverRate());
                companyHistory.setTradingVolume(one.getTradingVolume());
                companyHistory.setVolumeOfTransaction(one.getVolumeOfTransaction());
                saveHis.add(companyHistory);

                CapitalFlowHistoryEntity capitalFlowHistoryEntity = new CapitalFlowHistoryEntity();
                capitalFlowHistoryEntity.setCapital(one.getCapitalNow());
                capitalFlowHistoryEntity.setCompanyCode(one.getCompanyCode());
                capitalFlowHistoryEntity.setCompanyName(one.getCompanyName());
                capitalFlowHistoryEntity.setDateHis(format);
                capitalFlowHistoryEntity.setChangeDetail(one.getEarnings());
                capitalFlowHistoryEntity.setProportion(one.getProportion());
                saveHisCap.add(capitalFlowHistoryEntity);
            }
            companyHistoryService.saveBatch(saveHis);
            capitalFlowHistoryService.saveBatch(saveHisCap);
        }
        /**
         * 执行计算
         */
    }




    private  void digui(String uuid,String page,String size){
        try {
            // 设置要发送请求的URL
            String urlString =
            //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
           // "https://41.push2.eastmoney.com/api/qt/clist/get?cb=jQuery35104660061245692523_"+System.currentTimeMillis()+"&pn="+page+"&pz="+size+"&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=|0|0|0|web&fid=f26&fs=b:BK0707&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_="+System.currentTimeMillis()
                    "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37106878664776367346_"+System.currentTimeMillis()+"&fs=b%3ABK0707&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_="+System.currentTimeMillis();


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

//            dataAll=dataAll.replace(uuid,"");
//            dataAll=dataAll.replaceAll("\\(","");
//            dataAll=dataAll.replaceAll("\\);","");
            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            dataAll = splitTwo[0];

            JSON parse = JSONUtil.parse(dataAll);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            dataAll = jsonObject.get("diff").toString();
            System.out.println(dataAll);
            JSONArray objects = JSONUtil.parseArray(dataAll);
            for(int i=0;i<objects.size();i++){
                Object o = objects.get(i);
                JSONObject jsonObject1 = JSONUtil.parseObj(o);
                String 最新价格 = jsonObject1.get("f2",String.class);
                String 板块 = jsonObject1.get("f13",String.class);  // 0 深A  1 沪A
                String 涨跌幅 = jsonObject1.get("f3",String.class);
                String 涨跌额 = jsonObject1.get("f4",String.class);
                String 成交量 = jsonObject1.get("f5",String.class);
                String 成交额 = jsonObject1.get("f6",String.class);
                String 成交额振幅 = jsonObject1.get("f7",String.class);
                String 换手率 = jsonObject1.get("f8",String.class);
                String 市盈率 = jsonObject1.get("f9",String.class);
                String 当日最高 = jsonObject1.get("f15",String.class);
                String 当日最低 = jsonObject1.get("f16",String.class);
                String 今天开 = jsonObject1.get("f17",String.class);
                String 昨天收 = jsonObject1.get("f18",String.class);
                String 上市时间  = jsonObject1.get("f26",String.class);
                String 公司名字  = jsonObject1.get("f14",String.class);
                String 公司代码  = jsonObject1.get("f12",String.class);

                // 保存实时信息
                addSaveCodeDfList(公司代码,公司名字,今天开,
                        当日最低,昨天收,涨跌幅,涨跌额,
                        最新价格,当日最高,换手率,成交量,成交额);


            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private synchronized void addSaveCodeDfList(String code,String name
            ,String todayOpen,String todayLow,String yesterdayClose,String earn
            ,String earnAmount, String nowPrice,
            String top,String turnoverRate,String tradingVolume,String volumeOfTransaction){
        /**
         * addSaveCodeDfList(公司代码,公司名字,今天开,当日最低,昨天收,涨跌幅,涨跌额);
         */
        FinancialInfoDmEntity dm = new FinancialInfoDmEntity();
        dm.setCompanyCode(code);
        dm.setCompanyName(name);
        dm.setStartPrice(todayOpen);
        dm.setLowPrice(todayLow);
        dm.setYesterdayPrice(yesterdayClose);
        dm.setEarnings(earn);
        dm.setTopAmount(earnAmount);
        dm.setNowPrice(nowPrice);
        dm.setTopIncrease(top);
        dm.setTurnoverRate(turnoverRate);
        dm.setVolumeOfTransaction(volumeOfTransaction);
        dm.setTradingVolume(tradingVolume);
       // getCapitalNow(code,dm);
        savesCodeDfList.add(dm);
    }

    private String getUUID(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("当前年月日时分秒：" + formattedDateTime);
        String uuid = UUID.randomUUID().toString()+formattedDateTime;
        //  System.out.println(uuid);
        uuid=uuid.toUpperCase();
        return uuid;
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
                System.out.println(data);

                JSONObject jsonObjectNew = JSONUtil.parseObj(data);
                capitalNow = jsonObjectNew.get("data");
            }
            JSONObject dataJson = JSONUtil.parseObj(capitalNow);
            JSONArray diff = JSONUtil.parseArray(dataJson.get("diff"));
            for(Object o :diff){
                JSONObject dataOne = JSONUtil.parseObj(o);
                financialInfoDmEntity.setCapitalNow(dataOne.get("f62").toString());
                financialInfoDmEntity.setTotalCapital(dataOne.get("f6").toString());
                financialInfoDmEntity.setProportion(dataOne.get("f184").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }



    private  float culSupPre(String support,String pressure){
        Float sup = FloatUtils.stringToFloat(support);
        Float pre = FloatUtils.stringToFloat(pressure);
        if (sup.equals(pre) || sup==0 || pre==0 || sup > pre ){
            return 0;
        }

        float increase = pre - sup;

        BigDecimal result;
        BigDecimal decimal1 = new BigDecimal(Double.toString(increase*100));
        BigDecimal decimal2 = new BigDecimal(Double.toString(sup));
        // 进行除法运算，保留两位小数，使用四舍五入的方式
        result = decimal1.divide(decimal2, 2, RoundingMode.HALF_UP);
        // 输出结果

        return FloatUtils.stringToFloat(result.toString());

    }

//    public static void main(String []args){
//
//        float x = culSupPre("34.1", "42.09");
//        System.out.println(x);
//        System.out.printf("%.2f%n",x*0.2);
//        System.out.printf("%.2f%n",x*0.3);
//
//    }

}
