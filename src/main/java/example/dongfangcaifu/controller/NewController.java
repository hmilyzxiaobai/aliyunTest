package example.dongfangcaifu.controller;

import example.dongfangcaifu.news.MIITNews;
import example.dongfangcaifu.news.interfaceFile.NewReadALl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("new")
@Slf4j
public class NewController {
    @Autowired
    private NewReadALl newReadALl;

    @Autowired
    MIITNews miitNews;
    @GetMapping("all")
    public String getAll() throws Exception{
        newReadALl.execute();
        return "成功";
    }

}
