package example.controller;

import example.manager.UserChatLogManager;
import example.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("message/send")
@Slf4j
public class MessageSendController {

    @Autowired
    private UserChatLogManager userChatLogManager;

    @Autowired
    private ChatService chatService;

    @PostMapping("get/page/message")
    public String messagePage(@RequestParam String folderInfoId) throws IllegalAccessException {
       return userChatLogManager.dealString(userChatLogManager.getByUser(folderInfoId));
    }

    @PostMapping("send/message")
    public String sendMessage(@RequestParam String folderInfoId, @RequestParam String question) throws IllegalAccessException {
        return chatService.chat(folderInfoId,question,true);
    }

    @PostMapping("send/message/new")
    public String sendMessageNew(@RequestParam String folderInfoId ,@RequestParam String question) throws IllegalAccessException {
        return chatService.chat(folderInfoId,question,false);
    }

}
