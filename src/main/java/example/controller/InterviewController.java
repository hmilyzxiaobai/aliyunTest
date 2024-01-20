package example.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dto.LoginDto;
import example.entity.UserInfo;
import example.service.VersionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("interview")
@Slf4j
public class InterviewController {


    @Autowired
    private VersionInfoService versionInfoService;
    @PostMapping("login")
    public String login(@RequestParam String version  ) throws IllegalAccessException {
       return versionInfoService.compareVersion(version);
    }
}
