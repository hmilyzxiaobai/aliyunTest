package example.dongfangcaifu.service.email;

import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
public class EmailReceiverService {

    public void receiveEmail() {
        String host = "imap.qq.com"; // QQ 邮箱 IMAP 服务器
        String username = "3170085380@qq.com";
        String password = "rxnwknvhrnkqdfdb"; // 注意：使用授权码而非密码

        Properties properties = new Properties();
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");

        Session session = Session.getInstance(properties);
        try {
            Store store = session.getStore("imap");
            store.connect(username, password);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                System.out.println("Subject: " + message.getSubject());
                Address address = message.getFrom()[0];
                System.out.println("From: " + emailExtractor(address.toString()));
                System.out.println("Content: " + getContent(message));
            }

            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getContent(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    content.append(bodyPart.getContent());
                }
            }
            return content.toString();
        }
        return "不支持的邮件格式";
    }

    public String emailExtractor (String input) {
       // String input = "?utf-8?B?5bygIOejiuejig==?= <Zlldream@hotmail.com>";

        // 定义正则表达式
        String regex = "<([^>]+)>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // 获取邮箱地址
            String email = matcher.group(1);
            System.out.println("提取到的邮箱地址: " + email);
            return email;
        } else {
            System.out.println("未找到邮箱地址");
        }
        return "";
    }

}
