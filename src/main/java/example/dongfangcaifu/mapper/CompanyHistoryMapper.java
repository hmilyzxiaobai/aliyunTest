package example.dongfangcaifu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import example.dongfangcaifu.src.entity.CompanyHistoryEntity;
import example.dongfangcaifu.src.entity.CompanyOtherEntity;

import java.util.List;

public interface CompanyHistoryMapper extends BaseMapper<CompanyHistoryEntity> {

    List<CompanyHistoryEntity> associationPlateCompany(String conceptCode);

    CompanyOtherEntity selectOther(String companyCode);

}
