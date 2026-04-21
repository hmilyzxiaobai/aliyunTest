package example.dongfangcaifu.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.httpUtils.HttpRefererEnum;
import example.dongfangcaifu.httpUtils.HttpUrlUtils;
import example.dongfangcaifu.mapper.CapitalFlowHistoryMapper;
import example.dongfangcaifu.src.entity.CapitalFlowHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.src.response.CapitalFlowHistoryResponse;
import example.dongfangcaifu.src.response.GouResponse;
import example.dongfangcaifu.utils.FloatUtils;
import example.dongfangcaifu.utils.TimeUtilsZ;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CapitalFlowHistoryService extends ServiceImpl<CapitalFlowHistoryMapper, CapitalFlowHistoryEntity > {


    @Autowired
    private CompanyInfoService companyInfoService;


    @Autowired
    private CompanyHistoryService companyHistoryService;

    @Autowired
    private HttpUrlUtils httpUrlUtils;

    static List<CompanyHistoryEntity> saveInfoList = new ArrayList<>();
    public CapitalFlowHistoryResponse judgeByCapitalHis(FinancialInfoDmEntity financialInfoDmEntity){
        CapitalFlowHistoryResponse res = new CapitalFlowHistoryResponse();
        String code = financialInfoDmEntity.getCompanyCode();

        Float capitalNow = FloatUtils.stringToFloat(financialInfoDmEntity.getCapitalNow());

        List<CapitalFlowHistoryEntity> hisData = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                .eq(CapitalFlowHistoryEntity::getCompanyCode, code).last("order by date_his desc"));

        if (CollectionUtils.isEmpty(hisData)){
            // 为空则补全数据
            log.info("数据为空 补全数据"+code+"capitalHis表");
            log.info("数据为空 补全数据"+code+"capitalHis表");
            log.info("数据为空 补全数据"+code+"capitalHis表");
            CapitalFlowHistoryResponse resSvq =new CapitalFlowHistoryResponse();
            resSvq.setBuy(false);
            return resSvq;
           // this.saveBatch(reWriteHis(code,"",""));
        }

        // 再加一个追涨比 当天买入为正并且涨 第二天不管是买入还是卖出为涨的


        int upAndIn = 0;
        int in = 0;
        int up=0;
        StringBuilder sb = new StringBuilder();
        for (CapitalFlowHistoryEntity one :hisData){
            float change = FloatUtils.stringToFloat(one.getChangeDetail());
            if (change>0){
                up++;
            }
            float proportion = FloatUtils.stringToFloat(one.getProportion());
            if (proportion>0){
                in++;
                sb.append(one.getDateHis())
                        .append("注入资金为:")
                        .append(one.getCapital())
                        .append(",占比为:")
                        .append(one.getProportion())
                        .append(",当天涨幅情况为：")
                        .append(one.getChangeDetail())
                        .append("\n");
            }
            if (change>0&& proportion>0){
                upAndIn++;
            }
        }
        StringBuilder to = new StringBuilder();
        to.append("总的上涨天数为：")
                .append(up)
                .append(",其中买入为正的天数为：")
                .append(upAndIn)
                .append("\n")
                .append("买入的总天数为：")
                .append(in)
                .append("\n");
        if (upAndIn>=(in*0.85)){
            res.setBuy(true);
            String message= "买涨比为："+changeIntData(upAndIn,in)+",买入上涨超过百分之85 ,值得买进\n";
            message=message+to.toString()+sb.toString();
            res.setMessage(message);
        }else if(upAndIn>=(in*0.70) && upAndIn<(in*0.85)){
            res.setBuy(true);
            String message= "买涨比为："+changeIntData(upAndIn,in)+",买入上涨超过百分之70 ,谨慎买入\n";
            message=message+to.toString()+sb.toString();
            res.setMessage(message);
        }else {
            res.setBuy(false);
            String message= "买涨比为："+changeIntData(upAndIn,up)+",买入上涨小于百分之70 ,不值得\n";
            message=message+to.toString()+sb.toString();
            res.setMessage(message);
        }
        // 追涨买入
        if (hisData.size()<5){
            return res;
        }
       float f3= FloatUtils.stringToFloat(hisData.get(0).getCapital())+
                    FloatUtils.stringToFloat(hisData.get(1).getCapital())+
                       FloatUtils.stringToFloat(hisData.get(2).getCapital())
                    +capitalNow;
        float f5= f3+
                FloatUtils.stringToFloat(hisData.get(3).getCapital())+
                FloatUtils.stringToFloat(hisData.get(4).getCapital());



        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("当前时间的小时数是: " + hourOfDay);
        if (hourOfDay>= 14){
            // 大于下午两点 基本稳盘，可以开始看哪些值得买进
            // 查看第二天涨停情况 或者第三天情况
           if (f5>10000000 && f3>10000000){
               res.setBuy(true);
               String messageAdd= "时间大于下午2点 可以追涨买入\n\n";
               res.setMessage(messageAdd+res.getMessage());
           }
        }


        return res;
    }


    // 查看连续上涨的股票或者连续买入的股票
    public String getUpContinuous(){
        StringBuilder sb = new StringBuilder();
        List<CompanyInfoEntity> all = companyInfoService.getAll();
        for(CompanyInfoEntity companyInfoEntity:all){
            List<CapitalFlowHistoryEntity> hisData = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                    .eq(CapitalFlowHistoryEntity::getCompanyCode, companyInfoEntity.getCompanyCode()).last("order by date_his desc"));
            if (CollectionUtils.isEmpty(hisData)|| hisData.size()<3){
                continue;
            }
            float f3= FloatUtils.stringToFloat(hisData.get(0).getCapital())+
                    FloatUtils.stringToFloat(hisData.get(1).getCapital())+
                    FloatUtils.stringToFloat(hisData.get(2).getCapital());

            float f5= f3+
                    FloatUtils.stringToFloat(hisData.get(3).getCapital())+
                    FloatUtils.stringToFloat(hisData.get(4).getCapital());
            if (f5>10000000 && f3>10000000){
                sb.append("当前股票为：")
                        .append(companyInfoEntity.getCompanyName())
                        .append("股票代码为：").append(companyInfoEntity.getCompanyCode())
                        .append("连续持续买进 可以买入\n\n\n");
            }
        }
        return sb.toString();
    }

    private String changeIntData(int i,int n){
        float num= (float)i/n;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        return df.format(num);
    }

    /**
     * 检查数据并补齐
     */

    public void checkAllData(){
        List<CompanyInfoEntity> all = companyInfoService.getAll();
        List<CapitalFlowHistoryEntity> saveList = new ArrayList<>();

        for(CompanyInfoEntity companyInfoEntity:all){
            List<CapitalFlowHistoryEntity> list = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery().eq(CapitalFlowHistoryEntity::getCompanyCode, companyInfoEntity.getCompanyCode()));
            // 爬出来的数据
            List<CapitalFlowHistoryEntity> capitalFlowHistoryEntities = reWriteHis(companyInfoEntity.getCompanyCode(),"jQuery35108712474089916968","fa5fd1943c7b386f172d6893dbfba10b");
            Set<String> collect = list.stream().map(CapitalFlowHistoryEntity::getDateHis).collect(Collectors.toSet());
            for(CapitalFlowHistoryEntity one : capitalFlowHistoryEntities){
                if (!collect.contains(one.getDateHis())){
                    saveList.add(one);
                }
            }

        }
        if (!CollectionUtils.isEmpty(saveList)){
            this.saveBatch(saveList);
        }
    }

    private boolean checkData(List<CapitalFlowHistoryEntity> list){
        Set<String> collect = list.stream().map(CapitalFlowHistoryEntity::getDateHis).collect(Collectors.toSet());

        return collect.contains("2026-04-15")
                && collect.contains("2026-04-14")
                && collect.contains("2026-04-13")
                && collect.contains("2026-04-10")
                && collect.contains("2026-04-09");

    }


    /**
     * 补数据
     * @param code
     */

    public boolean reConvertData(String code,String jquery,String cul){
        this.remove(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery().eq(CapitalFlowHistoryEntity::getCompanyCode,code));
        if (StringUtils.isBlank(code)){
            return true;
        }
        List<CapitalFlowHistoryEntity> entityList = reWriteHis(code, jquery,cul);
        if (!checkData(entityList)){
            log.info("股票代码："+code+"导入失败");
            return false;
        }
        this.saveBatch(entityList);
        return true;
    }

    private List<CapitalFlowHistoryEntity> reWriteHis(String code,String jquery,String ult){
        List<CapitalFlowHistoryEntity> saveList = new ArrayList<>();

        try {
            String urlString = "https://push2his.eastmoney.com/api/qt/stock/fflow/daykline/get?cb="+jquery+"_"+System.currentTimeMillis()+"&lmt=0&klt=101&fields1=f1%2Cf2%2Cf3%2Cf7&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61%2Cf62%2Cf63%2Cf64%2Cf65&ut="+ult+"&secid=1."+code+"&_="+System.currentTimeMillis();

            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url,code, HttpRefererEnum.HUDATA);
            // 设置请求方法为GET
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
           // System.out.println(data);

            JSONObject jsonObject = JSONUtil.parseObj(data);
            Object capitalNowList = jsonObject.get("data");

            if (Objects.isNull(capitalNowList) || capitalNowList.toString().equals("null")){
                urlString =
                        //   "https://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery35103458189631037627_1715506453184&secid=1.600665&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&klt=101&fqt=1&end=20500101&lmt=120&_=1715506453297";
                        "https://push2his.eastmoney.com/api/qt/stock/fflow/daykline/get?cb="+jquery+"&lmt=0&klt=101&fields1=f1%2Cf2%2Cf3%2Cf7&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61%2Cf62%2Cf63%2Cf64%2Cf65&ut="+ult+"&secid=0."+code+"&_="+System.currentTimeMillis();

                url = new URL(urlString);
                // 打开连接
                connection = httpUrlUtils.httpBuildUrlUtils(url,code,HttpRefererEnum.CODE);
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
             //   System.out.println(data);

                JSONObject jsonObjectNew = JSONUtil.parseObj(data);
                capitalNowList = jsonObjectNew.get("data");
            }
            log.info("访问url为："+urlString);
            JSONObject lines = JSONUtil.parseObj(capitalNowList);
            String  codeH = lines.get("code").toString();
            String  name = lines.get("name").toString();
            JSONArray jsonArray = JSONUtil.parseArray(lines.get("klines"));
            for(Object o:jsonArray){
                String str = String.valueOf(o);
                String[] split = str.split(",");
                if (!TimeUtilsZ.checkTime(split[0])){
                    continue;
                }
                CapitalFlowHistoryEntity capitalFlowHistoryEntity = new CapitalFlowHistoryEntity();
                capitalFlowHistoryEntity.setCapital(split[1]);
                capitalFlowHistoryEntity.setCompanyCode(codeH);
                capitalFlowHistoryEntity.setCompanyName(name);
                capitalFlowHistoryEntity.setDateHis(split[0]);
                capitalFlowHistoryEntity.setChangeDetail(split[12]);
                capitalFlowHistoryEntity.setProportion(split[6]);
                saveList.add(capitalFlowHistoryEntity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return saveList;
    }

    /**
     * 钩子触发逻辑
     * 当天资金流入为正 但是下跌
     * 或者当天资金流出 但是占比不多，下跌幅度不大  不到跌停，明天高位抛售，跟进抛售抄底
     * 前两天资金流入为正 上升
     *
     */
    public GouResponse gouCul(FinancialInfoDmEntity infoDmEntity){
        GouResponse res = new GouResponse();
        res.setGou(false);
        // 过滤停牌
        if (infoDmEntity.getEarnings().length()<2 && infoDmEntity.getEarnings().contains("-")){
            return res;
        }

        float earnNow = FloatUtils.stringToFloat(infoDmEntity.getEarnings());
//        if (earnNow>4){
//            return res;
//            // 如果
//        }



        List<CapitalFlowHistoryEntity> hisList = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                .eq(CapitalFlowHistoryEntity::getCompanyCode, infoDmEntity.getCompanyCode())
                .last("order by date_his desc "));

//        for(CapitalFlowHistoryEntity one :hisList){
//            if ((one.getChangeDetail().contains("-") && one.getChangeDetail().length()<2)
//                    || (one.getCapital().contains("-") && one.getCapital().length()<2)
//                    || (one.getProportion().contains("-") && one.getProportion().length()<2)){
//                one.setProportion("0");
//                one.setCapital("0");
//                one.setChangeDetail("0");
//            }
//        }

        // 这个是比例  分别为 资金流入比 资金数 上下跌幅度
        if (CollectionUtils.isEmpty(hisList) || hisList.size()<3){
            return res;
        }

        float c = FloatUtils.stringToFloat(infoDmEntity.getCapitalNow());
        float o = FloatUtils.stringToFloat(infoDmEntity.getProportion());


        float o0 = FloatUtils.stringToFloat(hisList.get(0).getProportion());
        float c0 = FloatUtils.stringToFloat(hisList.get(0).getCapital());
        float d0 = FloatUtils.stringToFloat(hisList.get(0).getChangeDetail());

        float o1 = FloatUtils.stringToFloat(hisList.get(1).getProportion());
        float c1 = FloatUtils.stringToFloat(hisList.get(1).getCapital());
        float d1 = FloatUtils.stringToFloat(hisList.get(1).getChangeDetail());

        float all = c+c0+c1;


        if (c>0 && c0>0 && c1>0 && (
                earnNow < 0) || (earnNow>5 && o>10)){
            // 三天均流进  但是跌 买进  或者三天都是流入正 今天涨价 而且幅度大于10
            res.setGou(true);
        }
        if (c<0 && c0>0 && c1>0 ){
            float abs = 0-c;
            if ((c0+c1)>3*abs){
                // 很多都没跑 买入
                res.setGou(true);

            }
        }


//
//
//        if ( o0>5 && d0<0 && o1>5 && d1<0){
//            // 为一天钩子
//            res.setGou(true);
//            res.setGouLen(2);
//        } else if (o0>5 && d0<0){
//            // 为两天钩子
//            res.setGou(true);
//            res.setGouLen(1);
//        }else {
//            return res;
//        }
//
//
//
//        /**
//         * 两涨一跌再涨为钩
//         */
//        if (CollectionUtils.isEmpty(hisList)){
//            return   res;
//        }
//        NodeCapHis nodeCapHis = buildNode(hisList);
//        if (Objects.isNull(nodeCapHis)){
//            return   res;
//        }
//
//        Map<String,String> map = new LinkedHashMap<>();
//
//        while (!Objects.isNull(nodeCapHis.getNext() )){
//            if (Objects.isNull(nodeCapHis.getBefore()) ){
//                nodeCapHis=nodeCapHis.next;
//                continue;
//            }
//
//            CapitalFlowHistoryEntity now = nodeCapHis.getNow();
//            if (FloatUtils.stringToFloat(now.getChangeDetail())>0){
//                nodeCapHis=nodeCapHis.next;
//                continue;
//            }
//            NodeCapHis next = nodeCapHis.getNext();
//            // 如果第二天依旧跌 则不为钩子
//            CapitalFlowHistoryEntity nextNow = next.getNow();
//            if (FloatUtils.stringToFloat(nextNow.getChangeDetail())<0){
//                nodeCapHis=nodeCapHis.next;
//                continue;
//            }
//
//            String dateG = "";
//            boolean flagBefore = true;
//            boolean isG = false;
//            int lenG = 0;
//            float pro = 0;
//            // 判断前两天是否资金注入并涨
//            NodeCapHis nodeCapHisCopy = nodeCapHis;
//
//            while (flagBefore){
//                if (Objects.isNull(nodeCapHisCopy)|| Objects.isNull(nodeCapHisCopy.getBefore())){
//                    break;
//                }
//                NodeCapHis before = nodeCapHisCopy.getBefore();
//                CapitalFlowHistoryEntity beforeNow = before.getNow();
//                float aFloat = FloatUtils.stringToFloat(beforeNow.getProportion());
//                if (aFloat<=0){
//                    flagBefore=false;
//                    continue;
//                }
//
//                isG=true;
//                lenG++;
//                pro+=aFloat;
//                dateG = beforeNow.getDateHis();
//
//                nodeCapHisCopy=nodeCapHisCopy.next;
//            }
//            if (isG){
//                dateG = dateG+"-"+now.getDateHis();
//                map.put(dateG,"时间周期为："+lenG+",占比数为"+pro);
//            }
//            if (Objects.isNull(nodeCapHis.next)){
//                break;
//            }
//            nodeCapHis=nodeCapHis.next;
//        }
//        res.setGouHis(map);
        return   res;
    }

    private NodeCapHis buildNode(List<CapitalFlowHistoryEntity> list){
        if (list.size() == 0) return null;  // 如果数组为空，返回空链表

        // 创建头节点
        NodeCapHis head = new NodeCapHis(list.get(list.size()-1));
        NodeCapHis current = head;

        // 构建链表
        for (int i = list.size()-2; i >=0; i--) {
            NodeCapHis newNode = new NodeCapHis(list.get(i));
            current.next = newNode;
            newNode.before = current;
            current = newNode;
        }

        return head;
    }
    /*
    private NodeCapHis buildNode(List<CapitalFlowHistoryEntity> list){
        if (CollectionUtils.isEmpty(list) || list.size()<2){
            return null;
        }
        NodeCapHis head = new NodeCapHis();
        head.now=list.get(list.size()-1);
        head.before = null;

        NodeCapHis before = head;

        boolean flag =true;
        NodeCapHis nodeCapHis = new NodeCapHis();

        NodeCapHis nodeCapHisNext = new NodeCapHis();
        for (int i= list.size()-2;i>=0;i--){
         //   NodeCapHis nodeCapHis = new NodeCapHis();
            nodeCapHis.setNow(list.get(i));
            nodeCapHis.before=before;
            if (flag){
                head.next=nodeCapHis;
                flag=false;
            }

            if (i==0){
                nodeCapHis.next=null;
            }else {
                //NodeCapHis nodeCapHisNext ;
                nodeCapHisNext.setNow(list.get(i-1));
                nodeCapHis.before=before;
                nodeCapHis.next=nodeCapHisNext;
            }
            before=nodeCapHis;

        }
        return head;
    }

     */
    @Data

    static
    class NodeCapHis{
        CapitalFlowHistoryEntity now;
        NodeCapHis before;
        NodeCapHis next;

        @Override
        public String toString(){
            return "";
        }
        NodeCapHis(CapitalFlowHistoryEntity value) {
            this.now = value;
            this.next = null;
            this.before = null;
        }

    }



    //查看当天流出为负 但为涨价 第二天还是为涨的股票

    public void test(){
        List<String> dateHis = new ArrayList<>();
        String dateStr = "2024-11-18,2024-11-15,2024-11-14,2024-11-13,2024-11-12,2024-11-11,2024-11-08,2024-11-07,2024-11-06,2024-11-05,2024-11-04,2024-11-01,2024-10-30,2024-10-31,2024-10-29,2024-10-28,2024-10-25,2024-10-24,2024-10-23,2024-10-22,2024-10-21,2024-10-18,2024-10-17";
        dateHis=Arrays.asList(dateStr.split(","));

        for(int i=dateHis.size()-1; i>1 ;i--){
            List<CapitalFlowHistoryEntity> list = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                    .eq(CapitalFlowHistoryEntity::getDateHis, dateHis.get(i)));

            Map<String,Float> codePrice = new HashMap<>();

            List<CompanyHistoryEntity> companyHistoryEntities = companyHistoryService.list(Wrappers.<CompanyHistoryEntity>lambdaQuery()
                    .eq(CompanyHistoryEntity::getDateHis,dateHis.get(i)));

            companyHistoryEntities.forEach(e->{
                codePrice.put(e.getCompanyCode(),FloatUtils.stringToFloat(e.getPrice()));
            });

            List<String> codes = new ArrayList<>();
            for(CapitalFlowHistoryEntity com:list){
                if (com.getCompanyCode().startsWith("668") || com.getCompanyCode().startsWith("688")){
                    continue;
                }

                if (FloatUtils.stringToFloat(com.getChangeDetail())>0
                        &&FloatUtils.stringToFloat(com.getCapital())<0){
                    codes.add(com.getCompanyCode());

                }
            }
            searchDownUp(codes,codePrice,dateHis.get(i-1));
        }

    }



    private void searchDownUp(List<String> codes,Map<String,Float> codePrice,String dateHis){
        List<CompanyHistoryEntity> list = companyHistoryService.list(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getDateHis, dateHis));
        Map<String,CompanyHistoryEntity> map = new HashMap<>();
        list.forEach(e->{
            map.put(e.getCompanyCode(),e);
        });

        // 先看大概
        int size = codes.size();
        int up=0;
        int upUp=0;
        for(String code:codes){
            CompanyHistoryEntity companyHistory = map.get(code);
            try {
                if (FloatUtils.stringToFloat(companyHistory.getChangeDetails())>0){
                    up++;
                }
                if (FloatUtils.stringToFloat(companyHistory.getHighPrice())>codePrice.get(code)){
                    upUp++;
                }
            }catch (Exception e){
            }
        }
        System.out.println("总数为"+size+",其中上涨为"+up+",当天最高峰为上涨为"+upUp);
        System.out.println("上涨百分比为"+up*100/size+",当天最高峰为上涨百分比为"+upUp*100/size);

    }



    public void SearchGui(){
        List<CompanyInfoEntity> all = companyInfoService.getAll();
        // 流入正 连续两天 第三天为正 但是跌 第四天涨不涨
        int sum=0;
        int up=0;
        int down = 0;
        for(CompanyInfoEntity one :all){
            if(one.getCompanyCode().startsWith("668")|| one.getCompanyCode().startsWith("688")){
                continue;
            }
            List<CapitalFlowHistoryEntity> dateHisDesc = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                    .eq(CapitalFlowHistoryEntity::getCompanyCode, one.getCompanyCode()).last("order by date_his desc"));

            for(int i= 0; i<dateHisDesc.size()-5;i++){
                // 第一天
                Float changeD1 = FloatUtils.stringToFloat(dateHisDesc.get(i).getChangeDetail());
                Float cap1 = FloatUtils.stringToFloat(dateHisDesc.get(i).getCapital());

                Float changeD2 = FloatUtils.stringToFloat(dateHisDesc.get(i+1).getChangeDetail());
                Float cap2 = FloatUtils.stringToFloat(dateHisDesc.get(i+1).getCapital());

                Float changeD3 = FloatUtils.stringToFloat(dateHisDesc.get(i+2).getChangeDetail());
                Float cap3 = FloatUtils.stringToFloat(dateHisDesc.get(i+2).getCapital());

                if ( changeD1>0&&cap1>0
                        &&changeD2>0&&cap2>0
                        &&changeD3>0&&cap3>0) {
                    sum++;
                    if (FloatUtils.stringToFloat(dateHisDesc.get(i+4).getChangeDetail())>0
                    || FloatUtils.stringToFloat(dateHisDesc.get(i+5).getChangeDetail())>0){
                        up++;
                    }else {
                        down++;
                    }
                }
            }
        }
        System.out.println("总数为："+sum+",上升次数为"+up+",下降次数为"+down);
    }

    public int judgeUpMul(String name ,String code){
        List<CapitalFlowHistoryEntity> dateHisDesc = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                .eq(CapitalFlowHistoryEntity::getCompanyCode, code).last("order by date_his desc"));
        if (dateHisDesc.size()<20){
            return 0;
        }
        // 横盘
        judgeSmooth(name,code,dateHisDesc);
        //三天流入为正

        if (FloatUtils.stringToFloat(dateHisDesc.get(0).getCapital())>0 &&
        FloatUtils.stringToFloat(dateHisDesc.get(1).getCapital())>0 &&
        FloatUtils.stringToFloat(dateHisDesc.get(2).getCapital())>0 &&
        FloatUtils.stringToFloat(dateHisDesc.get(3).getCapital())>0 ) {
            //  对应四天流入为正
            return 4;
        }

        if (FloatUtils.stringToFloat(dateHisDesc.get(0).getCapital())>0 &&
                FloatUtils.stringToFloat(dateHisDesc.get(1).getCapital())>0 &&
                FloatUtils.stringToFloat(dateHisDesc.get(2).getCapital())>0 ) {
            //  对应三天流入为正
            return 3;
        }
        return 0;
    }

    public void judgeSmooth(String name,
                            String code
    ,List<CapitalFlowHistoryEntity> dateHisDesc){
        if (code.startsWith("668")){
            return;
        }
        //三天流入为正
        if (FloatUtils.stringToFloat(dateHisDesc.get(0).getCapital())>0 &&
                FloatUtils.stringToFloat(dateHisDesc.get(1).getCapital())>0 &&
                FloatUtils.stringToFloat(dateHisDesc.get(2).getCapital())>0 ) {
            //  对应三天流入为正
            float o1 = FloatUtils.stringToFloat(dateHisDesc.get(0).getChangeDetail());
            float o2 = FloatUtils.stringToFloat(dateHisDesc.get(1).getChangeDetail());
            float o3 = FloatUtils.stringToFloat(dateHisDesc.get(2).getChangeDetail());
            if ((o1< 2 && o1> -2) &&
                    (o3 < 2 &&o2> -2)&&
                    (o2< 2 && o3 >-2)){
                System.out.println("该股票近期流入较多，但是上升和下降很平滑，可以考虑买入"+name+code);
            }
        }
    }



    // 直接判断是否连续涨
    public void judgeUpUpUp(FinancialInfoDmEntity dm){
        String companyCode = dm.getCompanyCode();
        // 历史
        if (companyCode.startsWith("668") || companyCode.startsWith("688")){
            return;
        }
        LambdaQueryWrapper<CapitalFlowHistoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CapitalFlowHistoryEntity::getCompanyCode,companyCode);
        queryWrapper.last("order by date_his desc");
        List<CapitalFlowHistoryEntity> list = this.list(queryWrapper);
        if (list.size()<3){
            return;
        }
        if (FloatUtils.stringToFloat(dm.getEarnings())>0 && FloatUtils.stringToFloat(dm.getCapitalNow())>0
            && FloatUtils.stringToFloat(list.get(0).getChangeDetail())>0
                && FloatUtils.stringToFloat(list.get(1).getChangeDetail())>0
                && (FloatUtils.stringToFloat(list.get(0).getCapital())>0
                && FloatUtils.stringToFloat(list.get(1).getCapital())>0 )
        ){
            System.out.println("追涨买入"+dm.getCompanyCode()+dm.getCompanyName());
            System.out.println("追涨买入"+dm.getCompanyCode()+dm.getCompanyName());
        }
    }


    public boolean judgeUpSureUpSure(FinancialInfoDmEntity dm ){
        String capitalNow = dm.getCapitalNow();
        if (FloatUtils.stringToFloat(capitalNow)<0){
            return false;
        }
        String companyCode = dm.getCompanyCode();
        List<CapitalFlowHistoryEntity> list = this.list(Wrappers.<CapitalFlowHistoryEntity>lambdaQuery()
                .eq(CapitalFlowHistoryEntity::getCompanyCode, companyCode).last(" order by date_his desc limit 2"));

        if (FloatUtils.stringToFloat(list.get(0).getCapital())>0
            &&FloatUtils.stringToFloat(list.get(0).getChangeDetail())<0
            &&FloatUtils.stringToFloat(list.get(1).getCapital())>0
            &&FloatUtils.stringToFloat(list.get(1).getChangeDetail())>0
        ){
            return true;
        }else return false;
    }
}
