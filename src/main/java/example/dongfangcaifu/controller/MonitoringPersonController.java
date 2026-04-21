package example.dongfangcaifu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.dongfangcaifu.service.MonitoringPersonService;
import example.dongfangcaifu.src.entity.MonitoringPersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitoring-person")
public class MonitoringPersonController {

    @Autowired
    private MonitoringPersonService monitoringPersonService;

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public MonitoringPersonEntity getById(@PathVariable Long id) {
        return monitoringPersonService.getById(id);
    }

    /**
     * 查询所有
     */
    @GetMapping("/list")
    public List<MonitoringPersonEntity> getAll() {
        LambdaQueryWrapper<MonitoringPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPersonEntity::getIsDeleted, 0);
        wrapper.orderByDesc(MonitoringPersonEntity::getId);
        return monitoringPersonService.list(wrapper);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public IPage<MonitoringPersonEntity> getByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<MonitoringPersonEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MonitoringPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPersonEntity::getIsDeleted, 0);
        wrapper.orderByDesc(MonitoringPersonEntity::getId);
        return monitoringPersonService.page(page, wrapper);
    }

    /**
     * 条件分页查询
     */
    @GetMapping("/page/condition")
    public IPage<MonitoringPersonEntity> getByCondition(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String companyName) {
        Page<MonitoringPersonEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MonitoringPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPersonEntity::getIsDeleted, 0);
        if (companyName != null && !companyName.isEmpty()) {
            wrapper.like(MonitoringPersonEntity::getCompanyName, companyName);
        }
        wrapper.orderByDesc(MonitoringPersonEntity::getId);
        return monitoringPersonService.page(page, wrapper);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody MonitoringPersonEntity entity) {
        boolean success = monitoringPersonService.save(entity);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("id", entity.getId());
        return result;
    }

    /**
     * 批量新增
     */
    @PostMapping("/add/batch")
    public boolean addBatch(@RequestBody List<MonitoringPersonEntity> list) {
        return monitoringPersonService.saveBatch(list);
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public boolean update(@RequestBody MonitoringPersonEntity entity) {
        return monitoringPersonService.updateById(entity);
    }

    /**
     * 逻辑删除
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return monitoringPersonService.removeById(id);
    }

    /**
     * 批量逻辑删除
     */
    @DeleteMapping("/batch")
    public boolean batchDelete(@RequestBody List<Long> ids) {
        return monitoringPersonService.removeByIds(ids);
    }

    /**
     * 根据条件更新
     */
    @PutMapping("/update/price")
    public boolean updatePrice(@RequestParam Long id, @RequestParam String price) {
        MonitoringPersonEntity entity = new MonitoringPersonEntity();
        entity.setId(id);
        entity.setMonitoringPrice(price);
        return monitoringPersonService.updateById(entity);
    }

    /**
     * 查询总数
     */
    @GetMapping("/count")
    public long count() {
        LambdaQueryWrapper<MonitoringPersonEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPersonEntity::getIsDeleted, 0);
        return monitoringPersonService.count(wrapper);
    }
}