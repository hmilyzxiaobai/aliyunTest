package example.dongfangcaifu.service.institutional;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.institutional.InstitutionalPurchaseMapper;
import example.dongfangcaifu.src.entity.institutional.InstitutionalPurchaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InstitutionalPurchaseService extends ServiceImpl<InstitutionalPurchaseMapper,InstitutionalPurchaseEntity> {
    static List<InstitutionalPurchaseEntity> saveList = new ArrayList<>();

    public void saveInstitutional(Integer page,Integer size,String code,String dateBe){
        saveList.clear();
        saveEntity(page,size,code,dateBe);
        log.info("开始保存数据，保存条数为{}，当前页码为{}",saveList.size(),page);
        this.saveBatch(saveList);
    }
    private void saveEntity(int page,int size,String code,String dateBe){
        try {
            //  https://datacenter-web.eastmoney.com/api/data/v1/get?callback=&sortColumns=TRADE_DATE%2CSECURITY_CODE&sortTypes=-1%2C1&pageSize=50&pageNumber=2&             reportName=RPT_OPERATEDEPT_TRADE_DETAILSNEW&columns=ALL&filter=(OPERATEDEPT_CODE%3D%2210634757%22)&source=WEB&client=WEB
            String urlStr="https://datacenter-web.eastmoney.com/api/data/v1/get?callback=&sortColumns=TRADE_DATE%2CSECURITY_CODE&sortTypes=-1%2C1&pageSize="+size+"&pageNumber="+page+"&reportName=RPT_OPERATEDEPT_TRADE_DETAILSNEW&columns=ALL&filter=(OPERATEDEPT_CODE%3D%22"+code+"%22)&source=WEB&client=WEB";
            URL url = new URL(urlStr);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // connection.setRequestProperty("Accept-Charset", "GBK, UTF-8");

            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
            JSONObject result = JSONUtil.parseObj(dataAllJson.get("result"));
            int pageNow = Integer.parseInt(result.get("pages").toString());
            JSONArray objects = JSONUtil.parseArray(result.get("data"));
            for(int i=0;i<objects.size();i++){
                Object o = objects.get(i);
                JSONObject one = JSONUtil.parseObj(o);
                InstitutionalPurchaseEntity institutionalPurchase = new InstitutionalPurchaseEntity();
                //获取的买入时间

                String dateGet = one.get("TRADE_DATE").toString().split(" ")[0];
                // 拿到的时间阈值20
                LocalDate d1 = LocalDate.parse(dateGet);
                // 预定的时间阈值19
                LocalDate d2 = LocalDate.parse(dateBe);
                if (d1.isBefore(d2)){
                    page=11;
                    break;
                }

                institutionalPurchase.setActBuy(one.get("ACT_BUY").toString());
                institutionalPurchase.setActSell(one.get("ACT_SELL").toString());
                institutionalPurchase.setNetAmt(one.get("NET_AMT").toString());
                institutionalPurchase.setExplanation(one.get("EXPLANATION").toString());
                institutionalPurchase.setCompanyCode(one.get("SECURITY_CODE").toString());
                institutionalPurchase.setCompanyName(one.get("SECURITY_NAME_ABBR").toString());
                institutionalPurchase.setInstitutionalCode(one.get("OPERATEDEPT_CODE").toString());
                institutionalPurchase.setInstitutionalName(one.get("OPERATEDEPT_NAME").toString());
                institutionalPurchase.setTradeDate(dateGet);
                institutionalPurchase.setD1CloseAdjchrate(one.get("D1_CLOSE_ADJCHRATE").toString());
                institutionalPurchase.setD2CloseAdjchrate(one.get("D2_CLOSE_ADJCHRATE").toString());
                institutionalPurchase.setD3CloseAdjchrate(one.get("D3_CLOSE_ADJCHRATE").toString());
                institutionalPurchase.setD5CloseAdjchrate(one.get("D5_CLOSE_ADJCHRATE").toString());
                institutionalPurchase.setD10CloseAdjchrate(one.get("D10_CLOSE_ADJCHRATE").toString());
                institutionalPurchase.setD20CloseAdjchrate(one.get("D20_CLOSE_ADJCHRATE").toString());
                institutionalPurchase.setD30CloseAdjchrate(one.get("D30_CLOSE_ADJCHRATE").toString());
                saveList.add(institutionalPurchase);
            }
            if (page>=pageNow||page>=10){
                return;
            }else {
                page++;
                saveEntity(page,size,code,dateBe);
            }
        }catch (Exception e){
            log.info("保存失败，当前页码为{}",page);
            e.printStackTrace();
        }
    }
}
