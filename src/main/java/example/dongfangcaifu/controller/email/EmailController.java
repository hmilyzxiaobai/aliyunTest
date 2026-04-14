package example.dongfangcaifu.controller.email;

import example.dongfangcaifu.service.email.EmailReceiverService;
import example.dongfangcaifu.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailReceiverService emailReceiverService;

    @GetMapping("/send")
    public String sendEmail() {
        emailService.sendEmail("2953872785@qq.com", "Test Subject", "Hello, this is a test email!");
        return "Email sent successfully!";
    }

    @GetMapping("/receiveEmail")
    public String receiveEmail() {
        emailReceiverService.receiveEmail();
        return "邮件已接收！";
    }
}

