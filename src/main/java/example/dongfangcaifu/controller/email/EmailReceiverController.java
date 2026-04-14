//package example.dongfangcaifu.controller.email;
//
//import example.dongfangcaifu.service.email.EmailReceiverService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class EmailReceiverController {
//
//    @Autowired
//    private EmailReceiverService emailReceiverService;
//
//    @GetMapping("/receiveEmail")
//    public String receiveEmail() {
//        emailReceiverService.receiveEmail();
//        return "邮件已接收！";
//    }
//}
