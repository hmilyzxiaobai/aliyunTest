package example.dongfangcaifu.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {
        to = "2953872785@qq.com";
        String[] split = to.split(",");
        for(String str:split){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(str);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("3170085380@qq.com");
        //    mailSender.send(message);
        }

    }
}
