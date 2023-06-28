package example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloWorld {
    @RequestMapping("yeap")
    public String yeap(){
        return "成功打通网络";
    }
}
