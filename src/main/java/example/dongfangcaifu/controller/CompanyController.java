package example.dongfangcaifu.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.CompanyInfoService;
import example.dongfangcaifu.src.CompanyInfoDto;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("company")
@Slf4j
public class CompanyController {
    @Autowired
    private CompanyInfoService companyInfoService;

    @PostMapping("update/data")
    public String updateData(@RequestBody CompanyInfoDto dto){
        CompanyInfoEntity one = companyInfoService.getOne(Wrappers.<CompanyInfoEntity>lambdaQuery()
                .eq(CompanyInfoEntity::getCompanyCode, dto.getCompanyCode()));
        one.setSupport(dto.getSup());
        one.setPressure(dto.getPre());
        companyInfoService.updateById(one);
        return "成功";
    }

    @PostMapping("search")
    private CompanyInfoDto search(@RequestBody CompanyInfoDto dto){
        CompanyInfoEntity one = companyInfoService.getOne(Wrappers.<CompanyInfoEntity>lambdaQuery()
                .eq(CompanyInfoEntity::getCompanyCode, dto.getCompanyCode()));
        dto.setCompanyCode(one.getCompanyCode());
        dto.setPre(one.getPressure());
        dto.setSup(one.getSupport());
        return dto;
    }


    @PostMapping("update/batch")
    private String updateBatch(@RequestParam(value = "fileName")String fileName){
        Map<String, String> dataMap = ExcelReader.readerExcel(fileName);
        List<CompanyInfoEntity> listEntity = companyInfoService.list(Wrappers.<CompanyInfoEntity>lambdaQuery()
                .in(CompanyInfoEntity::getCompanyCode, dataMap.keySet()));
        for(CompanyInfoEntity one:listEntity){
            String companyCode = one.getCompanyCode();
            String s = dataMap.get(companyCode);
            one.setSupport(s.split(",")[0]);
            one.setPressure(s.split(",")[1]);
        }
        companyInfoService.updateBatchById(listEntity);
        String[] split = fileName.split("-");

        return split[1]+"-"+(Integer.parseInt(split[1])+30);
    }

}
