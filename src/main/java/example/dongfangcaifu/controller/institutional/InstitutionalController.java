package example.dongfangcaifu.controller.institutional;


import example.dongfangcaifu.service.institutional.InstitutionalInfoService;
import example.dongfangcaifu.service.institutional.InstitutionalPurchaseService;
import example.dongfangcaifu.src.entity.institutional.InstitutionalInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("institutional")
@Slf4j
public class InstitutionalController {

    @Autowired
    private InstitutionalPurchaseService institutionalPurchaseService;

    @Autowired
    private InstitutionalInfoService institutionalInfoService;

    @GetMapping("/save")
    public String saveInstitution(@RequestParam(value = "page") int page,
                                  @RequestParam(value = "size")int size){
        List<InstitutionalInfoEntity> list = institutionalInfoService.list();
        int length = list.size();;
        for(int i = 0 ;i<length;i++){
            log.info("开始执行操作，总数为{}，当前为{}",length,i);
            institutionalPurchaseService.saveInstitutional(page,size,list.get(i).getInstitutionalCode());
        }
        return "结束";
    }

    @GetMapping("/save/info")
    public String saveInstitutionInfo(@RequestParam(value = "page") int page,
                                  @RequestParam(value = "size")int size){
        institutionalInfoService.saveInstitutionalInfo(page,size);
        return "结束";
    }
}
