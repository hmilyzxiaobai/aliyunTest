package example.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.entity.VersionInfo;
import example.mapper.VersionInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionInfoService  {
    @Autowired
    VersionInfoMapper mapper;


    public String compareVersion(String version){
        VersionInfo versionInfo = mapper.selectOne(Wrappers.<VersionInfo>lambdaQuery().orderByAsc(VersionInfo::getSort).last("limit 1"));
        if (versionInfo.getVersion().equals(version)){
            return "";
        }
        return versionInfo.getRemark();
    }
}
