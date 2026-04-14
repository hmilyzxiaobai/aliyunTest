package example.dongfangcaifu.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.PlateMapper;
import example.dongfangcaifu.service.email.EmailService;
import example.dongfangcaifu.src.entity.Plate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlateService  extends ServiceImpl<PlateMapper, Plate> {

    @Autowired
    private EmailService emailService;
    // 对比增加 待完善
    public void addBatch(List<Plate> saveList){

        Set<String> oldSet = this.list().stream().map(Plate::getConceptCode).collect(Collectors.toSet());

        List<Plate> newSave = new ArrayList<>();
        for (Plate p:saveList){
            if (!oldSet.contains(p.getConceptCode())){
                newSave.add(p);
            }
        }

        if (!CollectionUtils.isEmpty(newSave)){
            this.saveBatch(newSave);
            log.info("出现新的概念板块{}",newSave.toString());
            log.info("出现新的概念板块{}",newSave.toString());
            log.info("出现新的概念板块{}",newSave.toString());
            //  触发报警  出现新的板块
            emailService.sendEmail("2953872785@qq.com", "出现新的概念板块", newSave.toString());
        }


    }


}
