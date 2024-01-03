package example.manager;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.entity.UserChatLog;
import example.mapper.UserChatLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChatLogManager {

    @Autowired
    private UserChatLogMapper userChatLogMapper;
    public List<UserChatLog> getByUser(String username){
        List<UserChatLog> userChatLogs = userChatLogMapper.selectList(Wrappers.<UserChatLog>lambdaQuery()
                .eq(UserChatLog::getUsername, username).orderByAsc(UserChatLog::getCreateTime).last("limit 30"));
        return userChatLogs;
    }

    public String dealString(List<UserChatLog> userChatLogs){
        if (CollectionUtils.isEmpty(userChatLogs)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (UserChatLog userChat: userChatLogs){
            sb.append("问题：").append(userChat.getQuestion()).append("\n").append("回答：").append(userChat.getRequest()).append("\n");
        }
        return sb.toString();
    }

    public void insertData(String username, String question ,String answer){
        UserChatLog userChatLog = new UserChatLog();
        userChatLog.setUsername(username);
        userChatLog.setQuestion(question);
        userChatLog.setRequest(answer);
        userChatLogMapper.insert(userChatLog);
    }
}
