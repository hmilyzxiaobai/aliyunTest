package example.dongfangcaifu.controller;

import example.dongfangcaifu.service.CompanyInfoService;
import example.dongfangcaifu.service.surveillance.HighUpUp;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
@Slf4j
public class SearchCodeController {
    @Autowired
    private HighUpUp highUpUp;

    @Autowired
    private CompanyInfoService companyInfoService;

    @GetMapping("high")
    public String getBuy(){

        List<CompanyInfoEntity> all = companyInfoService.getAll();

        for(CompanyInfoEntity info:all){
             highUpUp.searchHigh(info.getCompanyCode());
        }
        return "over";

    }

}
