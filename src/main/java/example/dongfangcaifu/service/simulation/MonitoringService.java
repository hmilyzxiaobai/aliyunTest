package example.dongfangcaifu.service.simulation;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.MonitoringMapper;
import example.dongfangcaifu.src.dto.LimitUpDownDTO;
import example.dongfangcaifu.src.entity.MonitoringEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public LimitUpDownDTO getLimitUpDownList(String startDate, String endDate, Integer type, Integer pageNum, Integer pageSize) {
        LimitUpDownDTO result = new LimitUpDownDTO();

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 查询列表数据
        List<MonitoringEntity> list = baseMapper.selectLimitUpDownList(startDate, endDate, type, offset, pageSize);

        // 转换为DTO并设置类型名称
        List<LimitUpDownDTO.MonitoringRecordDTO> records = new ArrayList<>();
        for (MonitoringEntity entity : list) {
            LimitUpDownDTO.MonitoringRecordDTO dto = new LimitUpDownDTO.MonitoringRecordDTO();
            dto.setId(entity.getId());
            dto.setCompanyName(entity.getCompanyName());
            dto.setCompanyCode(entity.getCompanyCode());
            dto.setPrice(entity.getPrice());
            dto.setChangeDetails(entity.getChangeDetails());
            dto.setDateHis(entity.getDateHis());
            dto.setType(Integer.parseInt(entity.getType()));
            dto.setTypeName(getTypeName(Integer.parseInt(entity.getType())));
            records.add(dto);
        }

        // 查询总数
        Long total = baseMapper.selectCount(startDate, endDate, type);

        // 查询统计数据
        Map<String, Object> statsMap = baseMapper.selectStats(startDate, endDate);

        // 组装统计数据
        LimitUpDownDTO.StatsDTO stats = new LimitUpDownDTO.StatsDTO();
        stats.setShUp(getIntValue(statsMap, "sh_up"));
        stats.setSzUp(getIntValue(statsMap, "sz_up"));
        stats.setShDown(getIntValue(statsMap, "sh_down"));
        stats.setSzDown(getIntValue(statsMap, "sz_down"));
        stats.setSzLargeUp(getIntValue(statsMap, "sz_large_up"));
        stats.setSzLargeDown(getIntValue(statsMap, "sz_large_down"));
        result.setStats(stats);
        result.setRecords(records);
        result.setTotal(total);

        return result;
    }
    private Integer getIntValue(Map<String, Object> map, String key) {
        Integer value = Integer.parseInt(map.get(key).toString());
        return value != null ? value : 0;
    }
    /**
     * 根据类型code获取类型名称
     */
    private String getTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "沪A涨停";
            case 2: return "深A涨停";
            case 3: return "沪A跌停";
            case 4: return "深A跌停";
            case 5: return "深A大涨";
            case 6: return "深A大跌";
            default: return "其他";
        }
    }



}
