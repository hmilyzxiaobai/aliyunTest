package example.taobao.controller;


import example.taobao.service.TaoBaoGet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("taobao/search")
@Slf4j
public class SearchTaoBao {

    @Autowired
    private TaoBaoGet taoBao;
    @GetMapping("search")
    public String deal(
        //    @RequestParam(value = "name") String name
    ){
        return taoBao.search("name");
    }

}
