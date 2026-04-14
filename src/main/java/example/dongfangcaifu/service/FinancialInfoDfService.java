package example.dongfangcaifu.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.FinancialInfoDfMapper;
import example.dongfangcaifu.src.entity.FinancialInfoDfEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialInfoDfService  extends ServiceImpl<FinancialInfoDfMapper, FinancialInfoDfEntity> {

    public List<FinancialInfoDfEntity> dataByCode(String code){
     return list(Wrappers.<FinancialInfoDfEntity>lambdaQuery().eq(FinancialInfoDfEntity::getCompanyCode,code));
    }

}

