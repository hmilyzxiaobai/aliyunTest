package example.dongfangcaifu.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.CompanyHistoryMapper;
import example.dongfangcaifu.src.dto.MeanSum;
import example.dongfangcaifu.src.entity.*;
import example.dongfangcaifu.src.response.JudgeVo;
import example.dongfangcaifu.utils.ExcelWriteNowDay;
import example.dongfangcaifu.utils.FloatUtils;
import example.dongfangcaifu.utils.TimeUtilsZ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanyHistoryService extends ServiceImpl<CompanyHistoryMapper, CompanyHistoryEntity> {
    @Autowired
    private CompanyInfoService companyInfoService;


    private static final NumberFormat numberFormat = NumberFormat.getInstance();
    static {
        numberFormat.setMaximumFractionDigits(2);
    }

    static List<List<String>> dataExcel = new ArrayList<>();

    //处理历史数据
    public JudgeVo judge(String proportion,String code, String details, String price){
        if (details.contains("-")&& details.length()<2){
            JudgeVo res = new JudgeVo();
            res.setJudgeFlag(false);
            return res;
        }
        float pro = FloatUtils.stringToFloat(proportion);
        float detailsFloat = FloatUtils.stringToFloat(details);

        LambdaQueryWrapper<CompanyHistoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CompanyHistoryEntity::getCompanyCode,code);
        queryWrapper.last("order by date_his desc");

        List<CompanyHistoryEntity> list = this.list(queryWrapper);
        if (price.contains("-")&&price.length()<2){
            JudgeVo res = new JudgeVo();
            res.setJudgeFlag(false);
            res.setMessage("停牌");
            return res;
        }
        float nowPrice = FloatUtils.stringToFloat(price);
        if (CollectionUtils.isEmpty(list)){
            // 补全历史数据
            // 保存
            log.info("当前历史数据为空"+code+"companyHis表");
            log.info("当前历史数据为空"+code+"companyHis表");
            log.info("当前历史数据为空"+code+"companyHis表");
            JudgeVo res = new JudgeVo();
            res.setJudgeFlag(false);
            return res;
           // this.saveBatch(writeOne(code,""));
           //  list = this.list(queryWrapper);
        }



        int doubleUp= 0;
        int doubleDown=0;
        int upDown=0;
        Map<String,Integer> map = new LinkedHashMap<>();
        float before = 0;
        float big=0;

        boolean flag =true;
        int dayDownGo=0;
        String temp = "";
        int size = list.size();

        int index =0;

        float sumThirty = 0;

        MeanSum meanSum = new MeanSum(sumThirty,sumThirty,sumThirty,sumThirty);

        for(CompanyHistoryEntity companyHistory:list){
            String changeDetails = companyHistory.getChangeDetails();
            float change = FloatUtils.stringToFloat(changeDetails);
            if (change>0 && before>0){
                doubleUp++;
            }
            if (change<0 && before<0){
                doubleDown++;
            }
            if (change>0 && before<0){
                upDown++;
            }

            before=change;

            // 连续上升天数变化情况

            // 完成第一部分计算 第二部分计算

            // 到达该阈值后，几天后又掉回到该值以下

            float priceHis = FloatUtils.stringToFloat(companyHistory.getPrice());
            big = Math.max(big,priceHis);
            if (nowPrice>=priceHis && flag){
                temp = companyHistory.getDateHis();
                flag=false;
            }
            if (!flag){
                dayDownGo++;
            }
            if (nowPrice<priceHis && !flag){
                flag=true;
                String k = temp+"---"+companyHistory.getDateHis();
                map.put(k,dayDownGo);
                temp="";
                dayDownGo=0;
            }

            // 第三部分 平均值计算

            index++;
            addSumBefore(index,size,meanSum,companyHistory.getPrice());

        }
        StringBuilder sb = new StringBuilder();

        for(String key:map.keySet()){
            sb.append("时间:").append(key).append(",大于该数值的天数为").append(map.get(key));
            sb.append("\n");
        }

        JudgeVo res = new JudgeVo();
        res.setJudgeFlag(false);
        if (detailsFloat>0 && pro>0){
            res.setJudgeFlag(true);
        }
        String message =
                "当前股票价格为："+nowPrice+"\n"+
                "连续两次上升以上次数为："+doubleUp+";\n"
                +"连续两次下降的次数为："+doubleDown+";\n"
                +"一上一下次数为"+upDown+";\n"
                +"该股票最大值为："+big+";\n"
                +"大于该阈值后大于该数的天数为: "+sb.toString()+"\n"
                +"最近三十天平均值为："+numberFormat.format(meanSum.getSumThirty()/30)+"\n"
                +"最近二十天平均值为："+numberFormat.format(meanSum.getSumTwenty()/20)+"\n"
                +"最近十十天平均值为："+numberFormat.format(meanSum.getSumTen()/10)+"\n"
                +"最近五天平均值为："+numberFormat.format(meanSum.getSumFive()/5)+"\n";
        if (detailsFloat<0){
            if (upDown>2*doubleDown&&upDown>2*doubleUp){
                message=message+"当前为跌,但是根据以往趋势第二天涨价";
                res.setJudgeFlag(true);
            }else {
                message=message+"当前为跌，根据以往判断不敢断定涨";
            }

        }
        res.setMessage(message);
        if (doubleUp>upDown && doubleDown>upDown){
            res.setJudgeFlag(true);
        }
        return res;
    }


    private void addSumBefore(int index,int total
            ,MeanSum meanSum ,String priceFloat){

        if (total - index >30){
            return;
        }
        float price  = FloatUtils.stringToFloat(priceFloat);
        if (total-index<30){
            meanSum.setSumThirty(meanSum.getSumThirty()+price);

        }
        if (total-index<20){
            meanSum.setSumTwenty(meanSum.getSumTwenty()+price);

        }
        if (total-index<10){
            meanSum.setSumTen(meanSum.getSumTen()+price);

        }
        if (total-index<5){
            meanSum.setSumFive(meanSum.getSumFive()+price);

        }
    }

    private boolean checkData(List<CompanyHistoryEntity> list){
        Set<String> collect = list.stream().map(CompanyHistoryEntity::getDateHis).collect(Collectors.toSet());

        return collect.contains("2025-11-17")
                && collect.contains("2025-11-18")
                && collect.contains("2025-11-19")
                && collect.contains("2025-11-20")
                && collect.contains("2025-11-21");

    }

    public void checkAllData(){
        List<CompanyInfoEntity> all = companyInfoService.getAll();
        List<CompanyHistoryEntity> saveList = new ArrayList<>();

        for(CompanyInfoEntity companyInfoEntity:all){
            List<CompanyHistoryEntity> list = this.list(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getCompanyCode, companyInfoEntity.getCompanyCode()));
            // 爬出来的数据
            List<CompanyHistoryEntity> companyHistoryEntities = writeOne(companyInfoEntity.getCompanyCode(),"");
            if (!checkData(companyHistoryEntities)){
                log.info("股票代码："+companyInfoEntity.getCompanyCode()+"导入失败");
                continue;
            }

            Set<String> collect = list.stream().map(CompanyHistoryEntity::getDateHis).collect(Collectors.toSet());
            for(CompanyHistoryEntity one : companyHistoryEntities){
                if (!collect.contains(one.getDateHis())){
                    saveList.add(one);
                }
            }
        }
        if (!CollectionUtils.isEmpty(saveList)){
            this.saveBatch(saveList);
        }
    }

    public boolean rebuild(String code,String jquery){
        // 直接删除
        //this.remove(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getCompanyCode,code));
        List<CompanyHistoryEntity> list = this.list(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getCompanyCode, code));
        List<CompanyHistoryEntity> saveList = new ArrayList<>();

        List<CompanyHistoryEntity> entityList = writeOne(code, jquery);
        if (!checkData(entityList)){
            log.info("股票代码："+code+"导入失败");
            return false;
        }
        Set<String> collect = list.stream().map(CompanyHistoryEntity::getDateHis).collect(Collectors.toSet());
        for(CompanyHistoryEntity one : entityList){
            if (!collect.contains(one.getDateHis())){
                saveList.add(one);
            }
        }
        if (!CollectionUtils.isEmpty(saveList)){
            this.saveBatch(saveList);
        }
        return true;
    }

    private List<CompanyHistoryEntity> writeOne(String code,String jquery){
        List<CompanyHistoryEntity> saveList = new ArrayList<>();
        try{
            String urlString =
                //   "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35103458189631037627_1715506453184&secid=1.600665&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1715506453297";
            "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb="+jquery+"&secid=0."+code+"&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&beg=0&end=20500101&lmt=120&_="+System.currentTimeMillis();
//            https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=&secid=0.301262&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=1000000&_=1758629560953

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
            String data;
            if (!JSONUtil.isJson(dataAll)){
                String[] splitOne = dataAll.split("\\(");
                String[] splitTwo = splitOne[1].split("\\)");
                 data = splitTwo[0];
            }else {
                data=dataAll;
            }

 //           System.out.println(data);
//
//            https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35109306894410132368_1758629560948&secid=0.301262&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1758629560953
//            https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35109306894410132368_1758629560948&secid=0.301262&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1758629841899

            JSONObject jsonObject = JSONUtil.parseObj(data);
            Object klineDataAll = jsonObject.get("data");

            if (Objects.isNull(klineDataAll) || klineDataAll.toString().equals("null")){
                urlString =
                        //   "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35103458189631037627_1715506453184&secid=1.600665&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1715506453297";
                        "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb="+jquery+"&secid=1."+code+"&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&beg=0&end=20500101&lmt=120&_="+System.currentTimeMillis();

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
                if (!JSONUtil.isJson(dataAll)){
                    String[] splitOne = dataAll.split("\\(");
                    String[] splitTwo = splitOne[1].split("\\)");
                    data = splitTwo[0];
                }else {
                    data=dataAll;
                }
             //   System.out.println(data);

                JSONObject jsonObjectNew = JSONUtil.parseObj(data);
                klineDataAll = jsonObjectNew.get("data");
            }
            log.info("\n\n");
            log.info("访问的url为："+urlString);

            JSONObject klineDataAllJson = JSONUtil.parseObj(klineDataAll);
            String name = String.valueOf(klineDataAllJson.get("name"));
            JSONArray objectsArray = JSONUtil.parseArray(klineDataAllJson.get("klines"));

            for(Object o:objectsArray){
                String s = String.valueOf(o);

                String[] split = s.split(",");
                if (!TimeUtilsZ.checkTime(split[0])){
                    continue;
                }

                CompanyHistoryEntity companyHistory = new CompanyHistoryEntity();
                companyHistory.setCompanyCode(code);
                companyHistory.setDateHis(split[0]);
                companyHistory.setPrice(split[2]);
                companyHistory.setChangeDetails(split[9]);
                companyHistory.setCompanyName(name);

                saveList.add(companyHistory);
                //System.out.println(s);
            }

        }catch(Exception e){
          log.info("失败原因{}",e.getMessage());
        }
        return saveList;

        /*
https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35108797847003193817_1758623560061&secid=0.301421&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1758623560064
        时分秒信息
        能拿到历史数据，过往一年的，可根据数据写入进去历史表 补全历史信息
         */
    }

    public String checkAllDataIs(){
        return "";
    }

    //public static void main(String[] args){
    public String getPageData(String page,String size){
        try {
            String urlString =
                    //   "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35103458189631037627_1715506453184&secid=1.600665&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1715506453297";
           // "http://79.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112407616952473227918_1729787152720&pn="+page+"&pz="+size+"&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=|0|0|0|web&fid=f26&fs=b:BK0707&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1729787152721";
           "http://push2.eastmoney.com/api/qt/clist/get?np=1&fltt=1&invt=2&cb=jQuery37106893369046196152_1763981505948&fs=b%3ABK0707&fields=f12%2Cf13%2Cf14%2Cf1%2Cf2%2Cf4%2Cf3%2Cf152%2Cf5%2Cf6%2Cf7%2Cf15%2Cf18%2Cf16%2Cf17%2Cf10%2Cf8%2Cf9%2Cf23%2Cf26&fid=f26&pn="+page+"&pz="+size+"&po=1&dect=1&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_=1763981505950";

            // 沪 A
           // 深A
            //"http://79.push2.eastmoney.com/api/qt/clist/get?cb=jQuery351009672644440647704_1734337981055&pn="+page+"&pz="+size+"&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=|0|0|0|web&fid=f26&fs=b:BK0804&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152&_=1729787152721";
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
             System.out.println("响应内容：");
            String dataAll = response.toString();
            //System.out.println(dataAll);
            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String data = splitTwo[0];
            System.out.println(data);
            JSONArray objects = JSONUtil.parseArray(JSONUtil.parse(JSONUtil.parse(data).getByPath("data")).getByPath("diff"));
            StringBuilder sb = new StringBuilder();
            for(Object o:objects){
                JSONObject jsonObject1 = JSONUtil.parseObj(o);

                sb.append(jsonObject1.get("f12")).append(",");
            }
            System.out.println(sb.toString());
            return sb.toString();
        }catch (Exception e){
            log.info("链接访问错误{}", e.getMessage());
        }
        return "";
    }


    // 验证是否正确

    public String checkUp(List<String> codes,String format){
        StringBuilder sb = new StringBuilder();
        int up=0;
        for(String code:codes){
            CompanyHistoryEntity one = this.getOne(Wrappers.<CompanyHistoryEntity>lambdaQuery()
                    .eq(CompanyHistoryEntity::getCompanyCode, code).gt(CompanyHistoryEntity::getDateHis, format)
                    .last(" limit 1"));
            if (FloatUtils.stringToFloat(one.getChangeDetails())>0){
                sb.append(one.getCompanyName()).append("-").append(one.getCompanyCode()).append("上涨了").append(one.getChangeDetails()).append("\n");
                up++;
            }else {
                sb.append(one.getCompanyName()).append("-").append(one.getCompanyCode()).append("下跌了").append(one.getChangeDetails()).append("\n");
            }
        }
        return "总的成功率为："+up*100/codes.size()+"\n"+sb.toString();
    }


    public int judgeAug(String code,String price){
        List<CompanyHistoryEntity> dateHisDesc = this.list(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getCompanyCode, code).last("order by date_his desc"));
        if (dateHisDesc.size()<20){
            return 0;
        }
        float f4 = FloatUtils.stringToFloat(dateHisDesc.get(0).getPrice())
                +FloatUtils.stringToFloat(dateHisDesc.get(1).getPrice())
                +FloatUtils.stringToFloat(dateHisDesc.get(2).getPrice())
                +FloatUtils.stringToFloat(dateHisDesc.get(3).getPrice());

        float f3 = FloatUtils.stringToFloat(dateHisDesc.get(0).getPrice())
                +FloatUtils.stringToFloat(dateHisDesc.get(1).getPrice())
                +FloatUtils.stringToFloat(dateHisDesc.get(2).getPrice());
        if (f4/4>FloatUtils.stringToFloat(price)){
            // 前4天平均值
            return 2;
        }
        if (f3/3>FloatUtils.stringToFloat(price)){
            // 前三天平均值
            return 1;
        }
        return 0;
    }




    public List<CompanyInfoEntity> buSql(){
        return companyInfoService.getAll();
        //return all;
    }

    public List<CompanyHistoryEntity> getByConceptCode(String conceptCode){
       return baseMapper.associationPlateCompany(conceptCode);
    }

    /**
     * 读取近三天的数据，包含收盘价格，换手率，成交量，成交额
     */

    public String readThreeInfo(String code){
        List<CompanyHistoryEntity> companyHistoryEntities = baseMapper.selectList(Wrappers.<CompanyHistoryEntity>lambdaQuery()
                .eq(CompanyHistoryEntity::getCompanyCode, code).last(" order by date_his desc limit 3"));
        StringBuilder sb = new StringBuilder();


        if (CollectionUtils.isEmpty(companyHistoryEntities)){
            return "历史数据为空";
        }
        for(CompanyHistoryEntity one:companyHistoryEntities){
            sb.append(String.format("\n日期为：%s，收盘价格为：%s," +
                    "换手率为：%s，成交量为：%s，成交额为：%s，涨幅为：%s",
                    one.getDateHis(),one.getPrice(),one.getTurnoverRate(),one.getVolumeOfTransaction(),one.getTradingVolume(),one.getChangeDetails()));
            sb.append("\n");
        }
        return sb.toString();
    }



    private void assCompanyInfo(){
        List<CompanyInfoEntity> all = companyInfoService.getAll();
        QueryWrapper<CompanyHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct COMPANY_CODE");
        this.list(queryWrapper);
    }



}








