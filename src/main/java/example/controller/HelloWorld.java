package example.controller;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("hello")
@Slf4j
public class HelloWorld {
    @PostMapping("yeap")
    public String yeap(){
    log.info("请求访问");
        //获取访问的http端口信息等
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ip = request.getRemoteAddr();
        int port = request.getRemotePort();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        log.info("该网络访问基本信息为：url:{} ,method:{} ,ip:{} ,port:{};访问时间为：{}",url,method,ip,port,formatter.format(date));
        return "欢迎访问";
    }

    public void openWindow(){

    }


}
