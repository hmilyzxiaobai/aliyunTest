package example.dongfangcaifu.service.surveillance;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.CompanyHistoryService;
import example.dongfangcaifu.service.PlateHisService;
import example.dongfangcaifu.service.PlateService;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.src.entity.Plate;
import example.dongfangcaifu.src.entity.PlateHis;
import example.dongfangcaifu.utils.ExcelWriterHisAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class Association {
    @Autowired
    private PlateService plateService;

    @Autowired
    private PlateHisService plateHisService;

    @Autowired
    private CompanyHistoryService companyHistoryService;

    public List<String> associationHis(List<String> ignoreList){
        /**
         * 找到所有板块
         * 找到该板块的历史信息
         * 找到该板块下关联度高的股票
         * 对关联度高的股票进行记录
         * 找到关联优先级及关联程度
         */
        List<Plate> list = plateService.list();
        for(Plate plate:list){
            String conceptCode = plate.getConceptCode();
            List<PlateHis> plateHisList = plateHisService.list(Wrappers.<PlateHis>lambdaQuery().eq(PlateHis::getConceptCode, conceptCode).orderByDesc(PlateHis::getDf));
            List<CompanyHistoryEntity> companyHisByConceptCode = companyHistoryService.getByConceptCode(conceptCode);

            if (CollectionUtils.isEmpty(companyHisByConceptCode)){
                ignoreList.add(plate.getConceptCode());
                continue;
            }
            Map<String,List<CompanyHistoryEntity>> map = new HashMap<>();



            for(CompanyHistoryEntity companyHistory:companyHisByConceptCode){
                if (map.containsKey(companyHistory.getDateHis())){
                    map.get(companyHistory.getDateHis()).add(companyHistory);
                }else {
                    List<CompanyHistoryEntity> one = new ArrayList<>();
                    map.put(companyHistory.getDateHis(),one);
                }
            }

            /**
             * 记录关联度高的股票信息
             */
            for(PlateHis plateHis:plateHisList){
                String df = plateHis.getDf();

                List<CompanyHistoryEntity> companyHistoryEntities = map.get(df);


                if (CollectionUtils.isEmpty(companyHistoryEntities)){
                   log.info("当天{}数据缺失",df);
                    continue;
                }
                //List<List<String>> data,String df,String pathDf
                // 写到excel里面
                log.info("正在写入{}天数据，数据量为：{}条",df,companyHistoryEntities.size());
                List<List<String>> data = new ArrayList<>();
                /**
                 * {"Data Time", "Company Name", "Company Code", "Plate","Plate Name","Code Change","Plate Change","Code Percent","Plate Percent"};
                 */
                for(CompanyHistoryEntity companyHistory:companyHistoryEntities){
                    List<String> one = new ArrayList<>();
                    one.add(companyHistory.getDateHis());
                    one.add(companyHistory.getCompanyName());
                    one.add(companyHistory.getCompanyCode());
                    one.add(plate.getConceptCode());
                    one.add(plate.getConceptName());
                    one.add(companyHistory.getChangeDetails());
                    one.add(plateHis.getChangePercent());
                   // one.add(companyHistory)
                    data.add(one);
                }
                String[] headers = {"Data Time", "Company Name", "Company Code", "Plate","Plate Name","Code Change","Plate Change"};

                ExcelWriterHisAnalysis.ExcelWriteAnalysis(data,df,df,plate.getConceptCode(),plate.getConceptName(),headers);
                log.info("导出{}成功",df);

            }

        }
        return ignoreList;
    }
}
