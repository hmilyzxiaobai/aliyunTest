package example.baidu;

import example.baidu.dto.MessageDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestFin {
    public static void main(String[] args){
        ProvideToFinancial financial = new ProvideToFinancial();
        String userId = UUID.randomUUID().toString();
        // 构建内容
        List<MessageDto> list = new ArrayList<>();
        MessageDto dto1 = new MessageDto();
        dto1.setRole("user");
        dto1.setContent("中国卫通 最近利好和利坏的新闻有哪些");
        list.add(dto1);
        String answer1 = financial.chatToBaiDu(list, userId);
        System.out.println(answer1);
        MessageDto dto2 = new MessageDto();
        dto2.setRole("user");
        dto2.setContent(answer1);
        list.add(dto2);
        MessageDto dto3 = new MessageDto();
        dto3.setRole("user");
        dto3.setContent("会对中国卫通产生什么样的影响 增加还是降低");
        list.add(dto3);
        String answer2 = financial.chatToBaiDu(list, userId);
        System.out.println(answer2);
    }
}
