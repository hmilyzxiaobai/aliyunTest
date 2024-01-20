package example.controller;

import example.entity.FolderInfo;
import example.service.FolderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("folder/manager")
@Slf4j
public class FolderInfoController {

    @Autowired
    private FolderInfoService folderInfoService;

    @PostMapping("getFolder")
    public List<FolderInfo> page(@RequestParam String userName) throws IllegalAccessException {
        return folderInfoService.getByUsername(userName);
    }


    @PostMapping("insert")
    public String insert(@RequestParam String userName,@RequestParam String folderName,@RequestParam String folderRemark) throws IllegalAccessException {
        return folderInfoService.insertFolder(userName,folderName, folderRemark);
    }
    @PostMapping("update")
    public String update(@RequestParam Long id,@RequestParam String folderName,@RequestParam String folderRemark) throws IllegalAccessException {
        return folderInfoService.updateInfo(id,folderName, folderRemark);
    }

}
