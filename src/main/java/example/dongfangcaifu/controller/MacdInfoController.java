package example.dongfangcaifu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.dongfangcaifu.service.macd.MacdInfoService;
import example.dongfangcaifu.src.entity.MacdInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/macd")
public class MacdInfoController {

    @Autowired
    private MacdInfoService macdInfoService;

    /**
     * 分页查询MACD数据
     */
    @GetMapping("/page")
    public IPage<MacdInfoEntity> getByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String companyCode,
            @RequestParam(required = false) String market) {
        Page<MacdInfoEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MacdInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MacdInfoEntity::getIsDeleted, 0);
        if (companyCode != null && !companyCode.isEmpty()) {
            wrapper.eq(MacdInfoEntity::getCompanyCode, companyCode);
        }
        if (market != null && !market.isEmpty()) {
            wrapper.eq(MacdInfoEntity::getMarket, market);
        }
        wrapper.orderByDesc(MacdInfoEntity::getDf);
        return macdInfoService.page(page, wrapper);
    }

    /**
     * 获取最新MACD数据（带信号）
     */
    @GetMapping("/latest/{companyCode}")
    public MacdInfoEntity getLatest(@PathVariable String companyCode) {
        return macdInfoService.getLatestWithSignal(companyCode);
    }

    /**
     * 获取MACD历史趋势
     */
    @GetMapping("/history/{companyCode}")
    public List<MacdInfoEntity> getHistory(@PathVariable String companyCode) {
        return macdInfoService.getHistory(companyCode);
    }

    /**
     * 今日金叉股票
     */
    @GetMapping("/golden-cross")
    public List<MacdInfoEntity> getGoldenCross() {
        return macdInfoService.getTodayGoldenCross();
    }

    /**
     * 今日死叉股票
     */
    @GetMapping("/death-cross")
    public List<MacdInfoEntity> getDeathCross() {
        return macdInfoService.getTodayDeathCross();
    }

    /**
     * 强势股（MACD>0且资金流入）
     */
    @GetMapping("/strong-stocks")
    public List<MacdInfoEntity> getStrongStocks() {
        return macdInfoService.getStrongStocks();
    }

    /**
     * 市场统计
     */
    @GetMapping("/market-stats")
    public Map<String, Object> getMarketStats() {
        return macdInfoService.getMarketStats();
    }

    /**
     * 新增MACD数据
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody MacdInfoEntity entity) {
        // 自动设置df分区
        if (entity.getDf() == null || entity.getDf().isEmpty()) {
            entity.setDf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }
        boolean success = macdInfoService.save(entity);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("id", entity.getId());
        return result;
    }

    /**
     * 批量新增
     */
    @PostMapping("/add/batch")
    public boolean addBatch(@RequestBody List<MacdInfoEntity> list) {
        return macdInfoService.batchSave(list);
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public boolean update(@RequestBody MacdInfoEntity entity) {
        return macdInfoService.updateById(entity);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return macdInfoService.removeById(id);
    }

    /**
     * MACD综合分析（某公司的完整分析报告）
     */
    @GetMapping("/analysis/{companyCode}")
    public Map<String, Object> getAnalysis(@PathVariable String companyCode) {
        MacdInfoEntity latest = macdInfoService.getLatestWithSignal(companyCode);
        List<MacdInfoEntity> history = macdInfoService.getHistory(companyCode);
        String trend = macdInfoService.analyzeTrend(history);

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("companyCode", companyCode);
        analysis.put("latestData", latest);
        analysis.put("trend", trend);
        analysis.put("historyCount", history.size());

        if (latest != null) {
            analysis.put("signal", latest.getSignal());
            analysis.put("currentPrice", latest.getPrice());
            analysis.put("macdValue", latest.getMacd());
        }

        return analysis;
    }

    /**
     * 批量获取股票最近N天的MACD数据
     */
    @PostMapping("/batch-macd")
    public Map<String, List<Map<String, Object>>> getBatchMacd(
            @RequestBody List<String> companyCodes,
            @RequestParam(defaultValue = "3") int days) {
        return macdInfoService.getBatchRecentMacd(companyCodes, days);
    }
}