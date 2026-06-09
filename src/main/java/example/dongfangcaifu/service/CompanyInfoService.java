package example.dongfangcaifu.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import example.dongfangcaifu.mapper.CompanyInfoMapper;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;
import example.dongfangcaifu.src.entity.Plate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompanyInfoService extends ServiceImpl<CompanyInfoMapper, CompanyInfoEntity> {

    static Set<Plate> savePlateList = new HashSet<>();


    public List<CompanyInfoEntity> getAll(){
        return this.list(Wrappers.<CompanyInfoEntity>lambdaQuery().eq(CompanyInfoEntity::getInMarket,"是"));
    }


    public List<CompanyInfoEntity> queryDistinct(){
        QueryWrapper<CompanyInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct COMPANY_CODE");
        return this.list(queryWrapper);
    }
    //其他的冗余了
    public List<FinancialInfoDmEntity> queryBuy(String nowDate){
        return baseMapper.queryAllUpBuy(nowDate);
    }


    public List<String> queryUpUp(String dfOne,String dfTwo){
        return baseMapper.queryUpUp(dfOne,dfTwo);
    }


    public String writePlates(){
        List<CompanyInfoEntity> list = this.list();

        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,-3);
        String df = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");

        for(CompanyInfoEntity one:list){
            String plateCode = one.getPlateCode();
            log.info("当前代码{}，当前板块{}",one.getCompanyCode(),one.getPlateCode());
            if (StringUtils.isBlank(plateCode)){
                log.info("导入该板块信息{}",one.toString());
                readUrl(one,changeEnums(one.getAppearMarket()),one.getCompanyCode(),df);
            }
        }
        // 抓取板块
        this.updateBatchById(list);
        //plateService.addBatch(Collections.singletonList((Plate) savePlateList));
        return "成功";
    }
    private String changeEnums(String content){
        if ("沪A".equals(content)){
            return "1";
        }else return "0";
    }

    private void readUrl(CompanyInfoEntity info,String type,String code,String df){
        try {
            long l = System.currentTimeMillis();
            String urlString =
            "https://push2.eastmoney.com/api/qt/slist/get?fltt=1&invt=2&cb=jQuery351024995127899668423_"+l+"&fields=f14%2Cf12%2Cf13%2Cf3%2Cf152%2Cf4%2Cf128%2Cf140%2Cf141&secid="+type+"."+code+"&ut=fa5fd1943c7b386f172d6893dbfba10b&pi=0&po=1&np=1&pz=14&spt=3&wbp2u=%7C0%7C0%7C0%7Cweb&_="+l;

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
            dataAll=dataAll.replace("jQuery351024995127899668423_"+l+"(","");
            String newStr = dataAll.substring(0, dataAll.length() - 2);
            JSON parse = JSONUtil.parse(newStr);
            JSONObject jsonObject= JSONUtil.parseObj( parse.getByPath("data"));
            String dataRead = jsonObject.get("diff").toString();
            JSONArray objects = JSONUtil.parseArray(dataRead);
            List<String> plateCodes= new ArrayList<>();
            List<String> plateNames= new ArrayList<>();
            for(Object o:objects){
                JSON one = JSONUtil.parse(o);
                String plateCode = one.getByPath("f12").toString();
                String plateName = one.getByPath("f14").toString();
                plateCodes.add(plateCode);
                plateNames.add(plateName);
                init(plateCode,plateName,df);
            }
            info.setCompanyNature(dealList(plateNames));
            info.setPlateCode(dealList(plateCodes));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void init(String code,String name,String df){
        Plate plate = new Plate();
        plate.setConceptCode(code);
        plate.setConceptName(name);
        plate.setDfStart(df);
        plate.setIsLive("存活");
        savePlateList.add(plate);
    }


    private String dealList(List<String> list){
        String str = list.toString();
        return str.substring(1,str.length()-1);
    }


    /**
     * 抓取是否超过压力值或者支撑位
     */


    // 获取压力位
    public Map<String,String> getAllPressure(){
        List<CompanyInfoEntity> all = getAll();
        Map<String,String> res = new HashMap<>();
        all.forEach(m->{
            res.put(m.getCompanyCode(),m.getPressure());
        });
        return res;
    }


    // 获取支撑位
    public Map<String,String> getAllSupport(){
        List<CompanyInfoEntity> all = getAll();
        Map<String,String> res = new HashMap<>();
        all.forEach(m->{
            res.put(m.getCompanyCode(),m.getSupport());
        });
        return res;
    }

}
