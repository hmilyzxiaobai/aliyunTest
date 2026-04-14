package example.deepseek.controller;

import com.dtflys.forest.annotation.Post;
import example.deepseek.DeepSeekService;
import example.deepseek.Response.ResponseDeepSeek;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deepseek")
@Slf4j
public class DeepSeekChatController {
    @Autowired
    DeepSeekService deepSeekService;
    @PostMapping("chat")
    public ResponseDeepSeek chat(@RequestBody TestChatDto dto){
        return deepSeekService.chatCompletions(dto.getMessage());
    }
}
