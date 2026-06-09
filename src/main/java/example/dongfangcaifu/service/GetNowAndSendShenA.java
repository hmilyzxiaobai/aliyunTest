package example.dongfangcaifu.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.httpUtils.HttpRefererEnum;
import example.dongfangcaifu.httpUtils.HttpUrlUtils;
import example.dongfangcaifu.service.email.EmailService;
import example.dongfangcaifu.src.entity.*;
import example.dongfangcaifu.src.response.CapitalFlowHistoryResponse;
import example.dongfangcaifu.src.response.GouResponse;
import example.dongfangcaifu.src.response.JudgeVo;
import example.dongfangcaifu.utils.DealPrice;
import example.dongfangcaifu.utils.FloatUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class GetNowAndSendShenA {

    @Value("${day.flag}")
    private String dayFlag;


    ExecutorService executorService = Executors.newFixedThreadPool(10);  // 创建一个固定大小的线程池

    private static List<FinancialInfoDmEntity> savesCodeDfList = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static boolean conFlag =true;
    @Autowired
    private SendHisService sendHisService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private CompanyHistoryService companyHistoryService;
    @Autowired
    private CompanyHistoryNowDayService companyHistoryNowDayService;


    @Autowired
    private CapitalFlowHistoryService capitalFlowHistoryService;

    @Autowired
    private CompanyOtherService companyOtherService;

    @Autowired
    private HoldOnService holdOnService;

    @Autowired
    private HttpUrlUtils httpUrlUtils;


    public synchronized void saveComInfo(int page,int size,String code) {
        // 查询的uuid
        String uuid = "PASSWORD";
        int index =0 ;
        int total = 0;
        try {
            // 设置要发送请求的URL
            String urlString =
                    "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37107591229855109933_1776222019251&fs=b%3ABK0804&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=9250355984212214%7C0%7C1%7C0%7Cweb&_=1776222019270";


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
            String dataAll = response.toString();

            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            dataAll = splitTwo[0];

            JSON parse = JSONUtil.parse(dataAll);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            total = Integer.parseInt(jsonObject.get("total").toString());
            dataAll = jsonObject.get("diff").toString();
            System.out.println(dataAll);
            JSONArray objects = JSONUtil.parseArray(dataAll);
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<objects.size();i++){
                Object o = objects.get(i);
                JSONObject one = JSONUtil.parseObj(o);
                sb.append(one.get("f12").toString()).append(",");
            }
            index = Math.max(index,Arrays.asList(sb.toString().split(",")).indexOf(code));

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertEntityInfo(total,page,size,index);
    }
    private  void insertEntityInfo(Integer total,int page,int size,int indexCode){
        int flagpage=page;
        conFlag=true;
        savesCodeDfList.clear();
        int index=page*size;
        for (;index<total;){
            index=page*size;
            digui(String.valueOf(page),
                    String.valueOf(size),indexCode);
            page++;
            if (!conFlag){
                break;
            }
        }
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,-3);
        String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
        System.out.println("timestamp = " + format);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("当前时间的小时数是: " + hourOfDay);
        List<CompanyHistoryNowDayEntity> datList = new ArrayList<>();

        if (dayFlag.equals("1") && hourOfDay<15){
            // 保存当天临时数据
            log.info("临时深A数据保存成功");
            List<CapitalFlowHistoryEntity> saveHisCap = new ArrayList<>();
            List<CompanyHistoryEntity> saveHis = new ArrayList<>();

            for(FinancialInfoDmEntity one :savesCodeDfList){
                CompanyHistoryNowDayEntity companyHistoryNowDayEntity = getCompanyHistoryNowDayEntity(one, format);
                datList.add(companyHistoryNowDayEntity);

                CompanyHistoryEntity companyHistory = getCompanyHistoryEntity(one, format);
                saveHis.add(companyHistory);
                CapitalFlowHistoryEntity capitalFlowHistoryEntity = getCapitalFlowHistoryEntity(one, format);
                saveHisCap.add(capitalFlowHistoryEntity);
            }
            log.info("临时当天在递归后执行保存程序，保存的数量为{}",datList.size());
            log.info("临时当天深A数据保存成功，当前页面{}",page);
            companyHistoryNowDayService.remove(Wrappers.<CompanyHistoryNowDayEntity>lambdaQuery()
                    .eq(CompanyHistoryNowDayEntity::getDateHis,format));
            companyHistoryNowDayService.saveBatch(datList);
            capitalFlowHistoryService.remove(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                    .eq(CapitalFlowHistoryEntity::getDateHis,format));
            capitalFlowHistoryService.saveBatch(saveHisCap);
            companyHistoryService.saveBatch(saveHis);

        }else {
            if (hourOfDay >= 15) {
                List<CompanyHistoryEntity> saveHis = new ArrayList<>();
                List<CapitalFlowHistoryEntity> saveHisCap = new ArrayList<>();
                for (FinancialInfoDmEntity one : savesCodeDfList) {
                    CompanyHistoryEntity companyHistory = getCompanyHistoryEntity(one, format);
                    saveHis.add(companyHistory);
                    CompanyHistoryNowDayEntity companyHistoryNowDayEntity = getCompanyHistoryNowDayEntity(one, format);
                    datList.add(companyHistoryNowDayEntity);
                    CapitalFlowHistoryEntity capitalFlowHistoryEntity = getCapitalFlowHistoryEntity(one, format);
                    saveHisCap.add(capitalFlowHistoryEntity);
                }
                log.info("深A数据保存成功，当前页面{}", page);

                if (flagpage==1){
                    companyHistoryNowDayService.remove(Wrappers.<CompanyHistoryNowDayEntity>lambdaQuery()
                            .eq(CompanyHistoryNowDayEntity::getDateHis,format));
                    capitalFlowHistoryService.remove(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                            .eq(CapitalFlowHistoryEntity::getDateHis,format));
                    companyHistoryService.remove(Wrappers.<CompanyHistoryEntity>lambdaQuery()
                            .eq(CompanyHistoryEntity::getDateHis,format));

                }
                companyHistoryNowDayService.saveBatch(datList);
                companyHistoryService.saveBatch(saveHis);

                capitalFlowHistoryService.saveBatch(saveHisCap);

            }
        }
        /**
         * 执行计算
         */
    }

    @NotNull
    private static CapitalFlowHistoryEntity getCapitalFlowHistoryEntity(FinancialInfoDmEntity one, String format) {
        CapitalFlowHistoryEntity capitalFlowHistoryEntity = new CapitalFlowHistoryEntity();
        capitalFlowHistoryEntity.setCapital(one.getCapitalNow());
        capitalFlowHistoryEntity.setCompanyCode(one.getCompanyCode());
        capitalFlowHistoryEntity.setCompanyName(one.getCompanyName());
        capitalFlowHistoryEntity.setDateHis(format);
        capitalFlowHistoryEntity.setChangeDetail(one.getEarnings());
        capitalFlowHistoryEntity.setProportion(one.getProportion());
        return capitalFlowHistoryEntity;
    }

    @NotNull
    private static CompanyHistoryNowDayEntity getCompanyHistoryNowDayEntity(FinancialInfoDmEntity one, String format) {
        CompanyHistoryNowDayEntity companyHistoryNowDayEntity = new CompanyHistoryNowDayEntity();
        companyHistoryNowDayEntity.setCompanyName(one.getCompanyName());
        companyHistoryNowDayEntity.setPrice(one.getNowPrice());
        companyHistoryNowDayEntity.setDateHis(format);
        companyHistoryNowDayEntity.setCompanyCode(one.getCompanyCode());
        companyHistoryNowDayEntity.setChangeDetails(one.getEarnings());
        companyHistoryNowDayEntity.setLowPrice(one.getLowPrice());
        companyHistoryNowDayEntity.setHighPrice(one.getTopIncrease());
        companyHistoryNowDayEntity.setTurnoverRate(one.getTurnoverRate());
        companyHistoryNowDayEntity.setTradingVolume(one.getTradingVolume());
        companyHistoryNowDayEntity.setVolumeOfTransaction(one.getVolumeOfTransaction());
        return companyHistoryNowDayEntity;
    }

    @NotNull
    private static CompanyHistoryEntity getCompanyHistoryEntity(FinancialInfoDmEntity one, String format) {
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
        return companyHistory;
    }


    private  void digui(String page,String size,int indexCode){
        try {
            // 设置要发送请求的URL
            String urlString =
                    //        String.format("https://70.push2.eastmoney.com/api/qt/clist/get?cb=%S&pn=%S&pz=%S&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1714361372410",uuid,page,size);
                    "https://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37107591229855109933_1776222019251&fs=b%3ABK0804&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=9250355984212214%7C0%7C1%7C0%7Cweb&_=1776222019270";


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

            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            dataAll = splitTwo[0];

            JSON parse = JSONUtil.parse(dataAll);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            if (JSONUtil.isNull(jsonObject)||JSONUtil.isNull(jsonObject.get("diff"))){
                return;
            }
            dataAll = jsonObject.get("diff").toString();
            System.out.println(dataAll);
            JSONArray objects = JSONUtil.parseArray(dataAll);
            for(;indexCode<objects.size();indexCode++){
                Object o = objects.get(indexCode);
                JSONObject jsonObject1 = JSONUtil.parseObj(o);
                String 最新价格 = DealPrice.dealPrice(jsonObject1.get("f2",String.class));;
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
                if (!conFlag){
                    log.info("当前页数：{}，当前代码{}",page,公司代码);
                    return;
                }
                // 保存实时信息
                addSaveCodeDfList(公司代码,公司名字,今天开,当日最低,昨天收,
                        涨跌幅,涨跌额,最新价格,当日最高,换手率,成交量,成交额);
            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            conFlag=false;
            log.info("当前页数：{}",page);

            e.printStackTrace();
        }
    }
    private synchronized void addSaveCodeDfList(String code,String name
            ,String todayOpen,String todayLow,String yesterdayClose,String earn
            ,String earnAmount,String nowPrice,
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
        dm.setEarnings(String.valueOf(FloatUtils.stringToFloat(earn)/100));
        dm.setTopAmount(earnAmount);
        dm.setNowPrice(nowPrice);
        dm.setTopIncrease(top);
        dm.setTurnoverRate(String.valueOf(FloatUtils.stringToFloat(turnoverRate)/100));
        dm.setTradingVolume(tradingVolume);
        dm.setVolumeOfTransaction(volumeOfTransaction);
        getCapitalNow(code,dm);
        if (!conFlag){
            return;
        }
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
                    "https://push2delay.eastmoney.com/api/qt/ulist.np/get?cb=jQuery112304668133973971953_1729232254223&fltt=2&secids=0."+code+"&fields=f62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf64%2Cf65%2Cf70%2Cf71%2Cf76%2Cf77%2Cf82%2Cf83%2Cf164%2Cf166%2Cf168%2Cf170%2Cf172%2Cf252%2Cf253%2Cf254%2Cf255%2Cf256%2Cf124%2Cf6%2Cf278%2Cf279%2Cf280%2Cf281%2Cf282&ut=b2884a393a59ad64002292a3e90d46a5&_=1729232254224";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url, HttpRefererEnum.HUDATA);
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
                        "https://push2delay.eastmoney.com/api/qt/ulist.np/get?cb=jQuery112304668133973971953_1729232254223&fltt=2&secids=1."+code+"&fields=f62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75%2Cf78%2Cf81%2Cf84%2Cf87%2Cf64%2Cf65%2Cf70%2Cf71%2Cf76%2Cf77%2Cf82%2Cf83%2Cf164%2Cf166%2Cf168%2Cf170%2Cf172%2Cf252%2Cf253%2Cf254%2Cf255%2Cf256%2Cf124%2Cf6%2Cf278%2Cf279%2Cf280%2Cf281%2Cf282&ut=b2884a393a59ad64002292a3e90d46a5&_=1729232254224";

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
            conFlag=false;
          //  throw new RuntimeException();
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
}
