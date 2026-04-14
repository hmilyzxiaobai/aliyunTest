package example.deepseek;

import example.deepseek.Request.Message;
import example.deepseek.Request.RequestDto;
import example.deepseek.Response.ResponseDeepSeek;
import example.deepseek.enums.RoleEnums;
import example.deepseek.interfaceForest.ForestInterfaceDeepSeek;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DeepSeekService {

    @Autowired
    private ForestInterfaceDeepSeek forestInterfaceDeepSeek;
    final static String bearer= "Bearer sk-b3ebb6885aaa45fdbb1f0dc9b309ac50";


    public ResponseDeepSeek chatCompletions(String question){
        RequestDto instance = RequestDto.getInstance();
        List<Message> messageList = new ArrayList<>();
        Message message = new Message();
        message.setRole(RoleEnums.USER.getType());
        message.setContent(question);
        messageList.add(message);
        instance.setMessages(messageList);
        ResponseDeepSeek responseDeepSeek =
                forestInterfaceDeepSeek.chatCompletions(bearer,instance);
        System.out.println(responseDeepSeek);
        log.info(responseDeepSeek.getId());
        return responseDeepSeek;
    }


    public ResponseDeepSeek chatCompletionsMul(List<Message> messages){
        RequestDto instance = RequestDto.getInstance();
//        List<Message> messageList = new ArrayList<>();
//        Message message = new Message();
//        message.setRole(RoleEnums.USER.getType());
//        message.setContent(question);
//        messageList.add(message);
        instance.setMessages(messages);
        ResponseDeepSeek responseDeepSeek =
                forestInterfaceDeepSeek.chatCompletions(bearer,instance);
        System.out.println(responseDeepSeek);
        log.info(responseDeepSeek.getId());
        return responseDeepSeek;
    }
}
