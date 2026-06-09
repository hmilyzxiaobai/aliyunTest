package example.dongfangcaifu.service.dark;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.CompanyDarkCapitalMapper;
import example.dongfangcaifu.src.entity.CompanyDarkCapitalEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CompanyDarkCapitalService extends ServiceImpl<CompanyDarkCapitalMapper, CompanyDarkCapitalEntity> {
    // 保存暗盘个股资金

    static  List<CompanyDarkCapitalEntity> savesCodeDfList = new ArrayList<>();

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    static boolean conFlag=true;


    public void saveDarkCompany(int page,int size,String code,String df){
        try{
            String urlString = "https://quotederivates.eastmoney.com/datacenter/darktrade?version=100&cver=100&date="+df+"&StartPage="+page+"&NumPerPage="+size+"&sortflag=6&desc=1&market=&datetype=";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
           // connection.setRequestProperty("Accept-Charset", "GBK, UTF-8");

            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            connection.disconnect();
            String dataAll = response.toString();
            JSONObject dataAllJson= JSONUtil.parseObj( dataAll);
            System.out.println(dataAll);
            if (dataAllJson.get("errmsg").toString().equals("成功")){
                System.out.println("获取u成功数据");
                int total = Integer.parseInt(dataAllJson.get("2").toString());
                saveEntityList(total,page,size,code,df);
            }

        }catch (Exception e){

        }
    }

    private void saveEntityList(int total,int page,int size,String code,String df){
        int index=page*size;
        savesCodeDfList.clear();
        conFlag=true;
        for (;index<total;){
            index=page*size;
            digui(String.valueOf(page),
                    String.valueOf(size),code,df);
            page++;
            if (!conFlag){
                log.info("保存进度为：{}",index);
                break;
            }
        }
        log.info("保存暗盘资金结束，保存数据为：{}",savesCodeDfList.size());
        this.remove(Wrappers.<CompanyDarkCapitalEntity>lambdaQuery().eq(CompanyDarkCapitalEntity::getDateHis,savesCodeDfList.get(0).getDateHis()));
        this.saveBatch(savesCodeDfList);
    }

    private void digui(String page,String size,String code,String df){
        try {
            String urlString = "https://quotederivates.eastmoney.com/datacenter/darktrade?version=100&cver=100&date="+df+"&StartPage="+page+"&NumPerPage="+size+"&sortflag=6&desc=1&market=&datetype=";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // connection.setRequestProperty("Accept-Charset", "GBK, UTF-8");

            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            connection.disconnect();
            String dataAll = response.toString();
            JSONObject dataAllJson= JSONUtil.parseObj( dataAll);
            System.out.println(dataAll);

            if (dataAllJson.get("errmsg").toString().equals("成功")){
                System.out.println("获取u成功数据");
                String dateStr = dataAllJson.get("1").toString();
                StringBuilder sb = new StringBuilder();
                sb.append(dateStr, 0, 4)
                        .append("-")
                        .append(dateStr, 4, 6)
                        .append("-")
                        .append(dateStr, 6, 8);
                String dataHis = sb.toString();
                JSONArray objects = JSONUtil.parseArray(dataAllJson.get("data"));
                for( int indexCode=0;indexCode<objects.size();indexCode++){
                    Object o = objects.get(indexCode);
                    JSONObject jsonOne = JSONUtil.parseObj(o);
                    CompanyDarkCapitalEntity companyDarkCapital = new CompanyDarkCapitalEntity();
                    companyDarkCapital.setCompanyCode(jsonOne.get("4").toString());
                    companyDarkCapital.setCompanyName(jsonOne.get("16").toString());
                    companyDarkCapital.setDarkAmount(jsonOne.get("6").toString());
                    companyDarkCapital.setDarkActive(String.format("%.2f", Float.parseFloat(jsonOne.get("11").toString())*100));
                    companyDarkCapital.setBrightAmount(jsonOne.get("7").toString());
                    companyDarkCapital.setNetAmount(jsonOne.get("8").toString());
                    companyDarkCapital.setChangeDetail(String.format("%.2f", Float.parseFloat(jsonOne.get("14").toString())*100));
                    companyDarkCapital.setDateHis(dataHis);
                    savesCodeDfList.add(companyDarkCapital);
                }
            }
        }catch (Exception e){
            log.info("当前页面为：{}",page);
            conFlag=false;
        }
    }

}
