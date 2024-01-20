package example.service;

import example.baidu.ChatSendService;
import example.baidu.dto.MessageDto;
import example.baidu.vo.ChatResVoList;
import example.entity.UserChatLog;
import example.manager.UserChatLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private UserChatLogManager userChatLogManager;

    public String chat(String folderInfoId, String question,boolean flag) {

        ChatSendService chatSendService = new ChatSendService();
        List<MessageDto> list = new ArrayList<>();
        if (flag){
            List<UserChatLog> byUser = userChatLogManager.getByUser(folderInfoId);
            for (UserChatLog userChatLog : byUser){
                MessageDto dtoUser= new MessageDto();
                dtoUser.setRole("user");
                dtoUser.setContent(userChatLog.getQuestion());
                MessageDto dtoSys= new MessageDto();
                dtoSys.setRole("assistant");
                dtoSys.setContent(userChatLog.getRequest());
                list.add(dtoUser);list.add(dtoSys);
            }
        }
        MessageDto dto = new MessageDto();
        dto.setRole("user");
        dto.setContent(question);
        list.add(dto);
        try {
            ChatResVoList chat = chatSendService.chat(list, folderInfoId);

            String systemResponse = "问题："+question+"\n"+"回答：" + chat.getMessage()+"\n";

            userChatLogManager.insertData(folderInfoId,question,chat.getMessage());
            return chat.getMessage();
        }catch (Exception e){

        }
        throw new IllegalStateException("连接超时");
    }
}
