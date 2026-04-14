package example.dongfangcaifu.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.SendHisMapper;
import example.dongfangcaifu.src.entity.SendHisEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SendHisService extends ServiceImpl<SendHisMapper, SendHisEntity> {

    public boolean judge(String companyName,
                         String companyCode,
                         String message,
                         String df,
                         Integer type){

        SendHisEntity sendHisEntity = baseMapper.selectOne(Wrappers.<SendHisEntity>lambdaQuery()
                .eq(SendHisEntity::getCompanyCode, companyCode)
                .eq(SendHisEntity::getSendType,type).eq(SendHisEntity::getDf, df).last("limit 1"));

        if (Objects.isNull(sendHisEntity)){
            SendHisEntity saveOne = new SendHisEntity();
            saveOne.setDf(df);
            saveOne.setMessage(message);
            saveOne.setCompanyCode(companyCode);
            saveOne.setCompanyName(companyName);
            saveOne.setSendType(type);
            saveOne.setSendStatus(0);

            baseMapper.insert(saveOne);
            return true;
        }
        sendHisEntity.setMessage(message);
        baseMapper.updateById(sendHisEntity);
        return false;
    }

    public void updateDef(){

    }

}
