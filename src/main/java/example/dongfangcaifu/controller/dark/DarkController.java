package example.dongfangcaifu.controller.dark;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dongfangcaifu.service.dark.CompanyDarkCapitalService;
import example.dongfangcaifu.service.dark.PlateDarkCapitalService;
import example.dongfangcaifu.src.entity.CompanyDarkCapitalEntity;
import example.dongfangcaifu.src.entity.PlateDarkCapitalEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dark")
@Slf4j
public class DarkController {
    @Autowired
    private CompanyDarkCapitalService capitalService;

    @Autowired
    private PlateDarkCapitalService plateDarkCapitalService;


    @GetMapping("/company")
    public void companyDark(@RequestParam(value = "page") int page,
                              @RequestParam(value = "size")int size,
                              @RequestParam(value = "code")String code,
                            @RequestParam(value = "df")String df){
        log.info("保存公司的暗盘资金信息");
        String[] split = df.split(",");
        for(int i=0;i<split.length;i++){
            log.info("开始保存日期为：{}",split[i]);
            capitalService.saveDarkCompany(page,size,code,split[i]);
        }
    }

    @GetMapping("/plate")
    public void plateDark(@RequestParam(value = "page") int page,
                              @RequestParam(value = "size")int size,
                              @RequestParam(value = "code")String code,
                            @RequestParam(value = "df")String df,
                          @RequestParam(value = "dfRemove")String dfRemove){
        String[] split = df.split(",");
       // log.info("保存暗盘资金结束，保存数据为：{}",savesCodeDfList.size());
        plateDarkCapitalService.remove(Wrappers.<PlateDarkCapitalEntity>lambdaQuery().eq(PlateDarkCapitalEntity::getDateHis,dfRemove));

        for(int i=0;i<split.length;i++){
            log.info("开始保存日期为：{}",split[i]);
            log.info("保存行业板块的暗盘资金信息");
            plateDarkCapitalService.saveDarkPlate(page,size,code,split[i],"2");
            log.info("保存行业成功，接下来开始保存概念");
            plateDarkCapitalService.saveDarkPlate(page,size,code,split[i],"3");
            log.info("保存概念成功");
        }



    }
}
