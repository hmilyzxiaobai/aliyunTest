package example.dongfangcaifu.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.HoldOnMapper;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.src.entity.HoldOnEntity;
import example.dongfangcaifu.src.entity.SendHisEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class HoldOnService extends ServiceImpl<HoldOnMapper, HoldOnEntity> {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");




    public List<HoldOnEntity> all(){
        return this.list(Wrappers.<HoldOnEntity>lambdaQuery().eq(HoldOnEntity::getIsSell, 0));
    }


    public void saveOne(String companyName,String companyCode,String price,String amount){
        LocalDateTime currentDateTime = LocalDateTime.now();

        String df = currentDateTime.format(formatter);

        HoldOnEntity sendHisEntity = baseMapper.selectOne(Wrappers.<HoldOnEntity>lambdaQuery()
                .eq(HoldOnEntity::getCompanyCode, companyCode)
                .eq(HoldOnEntity::getDf, df).last("limit 1"));

        if (Objects.isNull(sendHisEntity)) {
            HoldOnEntity holdOnEntity = new HoldOnEntity();
            holdOnEntity.setHoldOnAmount(amount);
            holdOnEntity.setHoldOnPrice(price);
            holdOnEntity.setIsSell("0");
            holdOnEntity.setCompanyCode(companyCode);
            holdOnEntity.setCompanyName(companyName);
            this.save(holdOnEntity);
        }

    }

    public void cell(Integer id,String cellPrice){
        HoldOnEntity byId = this.getById(id);
        byId.setSellPrice(cellPrice);
        byId.setIsSell("1");
        this.updateById(byId);
    }


    @Autowired
    private CompanyHistoryService companyHistoryService;

    public void buyByMy(String code){
        String[] split = code.split(",");
        for(String str:split){
            if (str.startsWith("688")){
                continue;
            }
            List<CompanyHistoryEntity> dateHisDesc = companyHistoryService.list(Wrappers.<CompanyHistoryEntity>lambdaQuery().eq(CompanyHistoryEntity::getCompanyCode, str).last("order by date_his desc"));
            CompanyHistoryEntity companyHistory = dateHisDesc.get(0);
            saveOne(companyHistory.getCompanyName(),companyHistory.getCompanyCode(),companyHistory.getPrice(),"100");
        }
    }
}
