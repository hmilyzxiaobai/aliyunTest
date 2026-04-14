package example.dongfangcaifu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import example.dongfangcaifu.src.entity.CompanyInfoEntity;
import example.dongfangcaifu.src.entity.FinancialInfoDmEntity;

import java.util.List;

public interface CompanyInfoMapper extends BaseMapper<CompanyInfoEntity> {

    List<FinancialInfoDmEntity> queryAllUpBuy(String nowDate);


    List<String> queryBySend(String df);

    List<FinancialInfoDmEntity> queryAfterTwo(String df,String df2);


    List<String> queryUpUp (String dfOne,String dfTwo);
}
