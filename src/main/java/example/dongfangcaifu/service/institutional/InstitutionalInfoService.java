package example.dongfangcaifu.service.institutional;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.institutional.InstitutionalInfoMapper;
import example.dongfangcaifu.src.entity.Plate;
import example.dongfangcaifu.src.entity.institutional.InstitutionalInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InstitutionalInfoService extends ServiceImpl<InstitutionalInfoMapper, InstitutionalInfoEntity> {
    @Autowired
    private InstitutionalPurchaseService institutionalPurchaseService;

    static List<InstitutionalInfoEntity> saveList = new ArrayList<>();

    // 保存机构信息
    public void saveInstitutionalInfo(Integer page,Integer size,String dateBe){
        saveEntity(page,size,dateBe);
        log.info("开始保存数据，保存条数为{}，当前页码为{}",saveList.size(),page);
        addBatchList(saveList);
        //this.saveBatch(saveList);
    }
    private void saveEntity(int page,int size,String dateBe){
        try {

            String urlStr="https://datacenter-web.eastmoney.com/api/data/v1/get?callback=&sortColumns=TOTAL_BUYER_SALESTIMES_1DAY%2COPERATEDEPT_CODE&sortTypes=-1%2C1&pageSize="+size+"&pageNumber="+page+"&reportName=RPT_RATEDEPT_RETURNT_RANKING&columns=ALL&source=WEB&client=WEB&filter=(STATISTICSCYCLE%3D%2202%22)";
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
                InstitutionalInfoEntity institutionalInfoEntity = new InstitutionalInfoEntity();
                institutionalInfoEntity.setInstitutionalCode(one.get("OPERATEDEPT_CODE").toString());
                institutionalInfoEntity.setInstitutionalName(one.get("OPERATEDEPT_NAME").toString());
                saveList.add(institutionalInfoEntity);
                institutionalPurchaseService.saveInstitutional(1,30,one.get("OPERATEDEPT_CODE").toString(),dateBe);
            }
            if (page>=pageNow){
                return;
            }else {
                page++;
                saveEntity(page,size,dateBe);
            }
        }catch (Exception e){
            log.info("保存失败，当前页码为{}",page);
            e.printStackTrace();
        }
    }

    public void addBatchList(@NotNull List<InstitutionalInfoEntity> saveList){

        Set<String> oldSet = this.list().stream().map(InstitutionalInfoEntity::getInstitutionalCode).collect(Collectors.toSet());

        List<InstitutionalInfoEntity> newSave = new ArrayList<>();
        for (InstitutionalInfoEntity p:saveList){
            if (!oldSet.contains(p.getInstitutionalCode())){
                newSave.add(p);
            }
        }

        if (!CollectionUtils.isEmpty(newSave)){
            this.saveBatch(newSave);
            log.info("出现新的机构{}",newSave.toString());
            log.info("出现新的机构{}",newSave.toString());
            log.info("出现新的机构{}",newSave.toString());
            //  触发报警  出现新的板块
        }


    }
}
