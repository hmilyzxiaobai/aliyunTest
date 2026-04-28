package example.dongfangcaifu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.service.CompanyHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company-history")
public class CompanyHistoryController {

    @Autowired
    private CompanyHistoryService companyHistoryService;

    /**
     * 分页查询历史数据
     */
    @GetMapping("/page")
    public IPage<CompanyHistoryEntity> getByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String companyCode) {
        Page<CompanyHistoryEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CompanyHistoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyHistoryEntity::getIsDeleted, 0);
        if (companyCode != null && !companyCode.isEmpty()) {
            wrapper.eq(CompanyHistoryEntity::getCompanyCode, companyCode);
        }
        wrapper.orderByDesc(CompanyHistoryEntity::getDateHis);
        return companyHistoryService.page(page, wrapper);
    }

    /**
     * 根据公司代码查询历史
     */
    @GetMapping("/list/{companyCode}")
    public List<CompanyHistoryEntity> getByCompanyCode(@PathVariable String companyCode) {
        return companyHistoryService.getByCompanyCode(companyCode);
    }

    /**
     * 获取最新记录
     */
    @GetMapping("/latest/{companyCode}")
    public CompanyHistoryEntity getLatest(@PathVariable String companyCode) {
        return companyHistoryService.getLatest(companyCode);
    }

    /**
     * 获取价格走势
     */
    @GetMapping("/trend/{companyCode}")
    public List<Map<String, Object>> getPriceTrend(
            @PathVariable String companyCode,
            @RequestParam(defaultValue = "30") int days) {
        return companyHistoryService.getPriceTrend(companyCode, days);
    }

    /**
     * 获取公司统计信息
     */
    @GetMapping("/stats/{companyCode}")
    public Map<String, String> getCompanyStats(@PathVariable String companyCode) {
        return companyHistoryService.getCompanyStats(companyCode);
    }



}