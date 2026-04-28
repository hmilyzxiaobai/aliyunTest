package example.dongfangcaifu.service.macd;


import com.baomidou.mybatisplus.extension.service.IService;
import example.dongfangcaifu.src.entity.MacdInfoEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MacdInfoService extends IService<MacdInfoEntity> {

    /**
     * 获取最新MACD数据（带信号判断）
     */
    MacdInfoEntity getLatestWithSignal(String companyCode);

    /**
     * 获取MACD历史趋势
     */
    List<MacdInfoEntity> getHistory(String companyCode);

    /**
     * 获取今日金叉股票列表
     */
    List<MacdInfoEntity> getTodayGoldenCross();

    /**
     * 获取今日死叉股票列表
     */
    List<MacdInfoEntity> getTodayDeathCross();

    /**
     * 获取MACD强势股（MACD>0且资金流入）
     */
    List<MacdInfoEntity> getStrongStocks();

    /**
     * 获取各市场MACD统计
     */
    Map<String, Object> getMarketStats();

    /**
     * 批量保存MACD数据
     */
    boolean batchSave(List<MacdInfoEntity> list);

    /**
     * 判断MACD信号
     */
    String analyzeSignal(MacdInfoEntity entity);

    /**
     * 判断趋势
     */
    String analyzeTrend(List<MacdInfoEntity> history);

    /**
     * 获取某公司最近N天的MACD数据
     */
    List<Map<String, Object>> getRecentMacd(String companyCode, int days);

    /**
     * 批量获取多家公司最近N天的MACD数据
     */
    Map<String, List<Map<String, Object>>> getBatchRecentMacd(List<String> companyCodes, int days);


}