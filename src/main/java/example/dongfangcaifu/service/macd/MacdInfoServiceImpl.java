package example.dongfangcaifu.service.macd;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.MacdInfoMapper;
import example.dongfangcaifu.src.entity.MacdInfoEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MacdInfoServiceImpl
        extends ServiceImpl<MacdInfoMapper, MacdInfoEntity>
        implements MacdInfoService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public MacdInfoEntity getLatestWithSignal(String companyCode) {
        MacdInfoEntity entity = this.baseMapper.selectLatestByCompanyCode(companyCode);
        if (entity != null) {
            entity.setSignal(analyzeSignal(entity));
        }
        return entity;
    }

    @Override
    public List<MacdInfoEntity> getHistory(String companyCode) {
        return this.baseMapper.selectHistoryByCompanyCode(companyCode);
    }

    @Override
    public List<MacdInfoEntity> getTodayGoldenCross() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        return this.baseMapper.selectGoldenCross(today);
    }

    @Override
    public List<MacdInfoEntity> getTodayDeathCross() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        return this.baseMapper.selectDeathCross(today);
    }

    @Override
    public List<MacdInfoEntity> getStrongStocks() {
        LambdaQueryWrapper<MacdInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MacdInfoEntity::getIsDeleted, 0);
        wrapper.apply("CAST(macd AS DECIMAL(10,4)) > 0");
        wrapper.apply("net_amount > 0");
        wrapper.orderByDesc(MacdInfoEntity::getNetAmount);
        wrapper.last("LIMIT 50");

        List<MacdInfoEntity> list = this.list(wrapper);
        list.forEach(entity -> entity.setSignal(analyzeSignal(entity)));
        return list;
    }

    @Override
    public Map<String, Object> getMarketStats() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        List<Map<String, Object>> stats = this.baseMapper.selectMarketStats(today);

        Map<String, Object> result = new HashMap<>();
        result.put("date", today);
        result.put("stats", stats);

        // 计算总数
        int total = stats.stream().mapToInt(s -> ((Number) s.get("count")).intValue()).sum();
        result.put("total", total);

        return result;
    }

    @Override
    public boolean batchSave(List<MacdInfoEntity> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        // 设置df分区字段
        String today = LocalDate.now().format(DATE_FORMATTER);
        list.forEach(entity -> {
            if (!StringUtils.hasText(entity.getDf())) {
                entity.setDf(today);
            }
        });
        return this.saveBatch(list);
    }

    @Override
    public String analyzeSignal(MacdInfoEntity entity) {
        if (entity == null || !StringUtils.hasText(entity.getDif()) || !StringUtils.hasText(entity.getDea())) {
            return "数据不足";
        }

        try {
            double dif = Double.parseDouble(entity.getDif());
            double dea = Double.parseDouble(entity.getDea());
            double macd = Double.parseDouble(entity.getMacd());

            if (dif > dea && macd > 0) {
                return "🟢 金叉多头";
            } else if (dif > dea && macd < 0) {
                return "🟡 金叉酝酿";
            } else if (dif < dea && macd < 0) {
                return "🔴 死叉空头";
            } else if (dif < dea && macd > 0) {
                return "🟠 死叉减弱";
            } else {
                return "⚪ 震荡";
            }
        } catch (NumberFormatException e) {
            return "数据异常";
        }
    }

    @Override
    public String analyzeTrend(List<MacdInfoEntity> history) {
        if (history == null || history.size() < 3) {
            return "数据不足";
        }

        try {
            // 取最近3天的MACD值
            List<Double> macdValues = history.stream()
                    .limit(3)
                    .map(e -> Double.parseDouble(e.getMacd()))
                    .collect(Collectors.toList());

            boolean isRising = macdValues.get(0) < macdValues.get(1) && macdValues.get(1) < macdValues.get(2);
            boolean isFalling = macdValues.get(0) > macdValues.get(1) && macdValues.get(1) > macdValues.get(2);

            if (isRising && macdValues.get(2) > 0) {
                return "📈 强势上涨";
            } else if (isRising) {
                return "📈 上涨趋势";
            } else if (isFalling && macdValues.get(2) < 0) {
                return "📉 加速下跌";
            } else if (isFalling) {
                return "📉 下跌趋势";
            } else {
                return "🔄 震荡整理";
            }
        } catch (NumberFormatException e) {
            return "数据异常";
        }
    }

    public List<Map<String, Object>> getRecentMacd(String companyCode, int days) {
        return baseMapper.selectRecentMacd(companyCode, days);
    }


    public Map<String, List<Map<String, Object>>> getBatchRecentMacd(List<String> companyCodes, int days) {
        if (companyCodes == null || companyCodes.isEmpty()) {
            return new HashMap<>();
        }

        // 使用 MyBatis 批量查询
        List<Map<String, Object>> allData = baseMapper.selectBatchRecentMacd(companyCodes, days);

        // 按公司代码分组
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        for (Map<String, Object> data : allData) {
            String companyCode = (String) data.get("company_code");
            result.computeIfAbsent(companyCode, k -> new ArrayList<>()).add(data);
        }

        // 对每个公司的数据按日期排序（最新的在前）
        for (List<Map<String, Object>> list : result.values()) {
            list.sort((a, b) -> {
                String dfA = (String) a.get("df");
                String dfB = (String) b.get("df");
                return dfB.compareTo(dfA);
            });
        }

        return result;
    }


}
