package example.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.entity.FolderInfo;
import example.mapper.FolderInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderInfoService {

    @Autowired
    private FolderInfoMapper mapper;

    public List<FolderInfo> getByUsername(String username){
        return mapper.selectList(Wrappers.<FolderInfo>lambdaQuery().eq(FolderInfo::getUsername,username).orderByDesc(FolderInfo::getSort));
    }

    public String insertFolder(String username,String folderName, String folderRemark) {
        FolderInfo folderInfo = new FolderInfo();
        folderInfo.setFolderName(folderName);
        folderInfo.setUsername(username);
        folderInfo.setFolderRemark(folderRemark);
        mapper.insert(folderInfo);
        return "新增成功";
    }

    public String updateInfo(Long id, String folderName, String folderRemark) {
        FolderInfo folderInfo = mapper.selectById(id);
        folderInfo.setFolderRemark(folderRemark);
        folderInfo.setFolderName(folderName);
        mapper.updateById(folderInfo);
        return "更新成功";
    }
}
