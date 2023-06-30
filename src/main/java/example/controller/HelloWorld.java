package example.controller;

import example.util.RobTicketWithDaMai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloWorld {
    @RequestMapping("yeap")
    public String yeap(){
        return "成功打通网络";
    }

    @Autowired
    private RobTicketWithDaMai robTicketWithDaMai;
    @RequestMapping("runTicket")
    public String runTicket(){
        robTicketWithDaMai.start();
        return "成功打通网络";
    }
}
