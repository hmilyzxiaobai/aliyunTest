package example.dongfangcaifu.service.simulation;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.MonitoringMapper;
import example.dongfangcaifu.src.entity.MonitoringEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MonitoringService extends ServiceImpl<MonitoringMapper, MonitoringEntity> {

    // 1 沪A涨停 2 深A涨停 3 沪A跌停 4 深A跌停 5 深A大涨 6深A大跌

    public void monitor(){
        List<String> search = new ArrayList<>();
        List<MonitoringEntity> monitors = this.list(Wrappers.<MonitoringEntity>lambdaQuery().in(MonitoringEntity::getDateHis, search));

    }
    //
    private void analysis(List<MonitoringEntity> analysis){

        for(MonitoringEntity monitoring:analysis){
            String type = monitoring.getType();
        }
    }

    //  分析沪A
    private void analysisHuZhangTing(MonitoringEntity monitoring){
        //
        String companyCode = monitoring.getCompanyCode();
        List<MonitoringEntity> list = this.list(Wrappers.<MonitoringEntity>lambdaQuery()
                .eq(MonitoringEntity::getCompanyCode, companyCode).last("order by data_dis desc limit 5"));

        // 第一天涨停吗 或者前两天涨停过
        for(MonitoringEntity one:list){
            String dateHis = one.getDateHis();
            String companyCode1 = one.getCompanyCode();

            // 是否是近五天的第一天涨停，如果不是
            // 看是否是在低点涨停，如果是低点涨停，看是否有最近利好
            // 如果是有利好，可能会连续涨停，怎么分析利好，
            //
        }

    }

    // 分析深A
    private void analysisShen(){
        //
    }


    /**
     * 大涨  大跌，但是近期是平盘居多，换手率前三天5以上
     */



}
