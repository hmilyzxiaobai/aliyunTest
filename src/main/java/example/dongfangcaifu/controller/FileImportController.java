package example.dongfangcaifu.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.CompanyInfoService;
import example.dongfangcaifu.service.surveillance.Association;
import example.dongfangcaifu.src.CompanyInfoDto;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("import")
@Slf4j
public class FileImportController {
    @Autowired
    private Association association;

    @PostMapping("import/all")
    public List<String> importFileAll(@RequestParam List<String> ignoreList){
        return association.associationHis(ignoreList);
    }



}
