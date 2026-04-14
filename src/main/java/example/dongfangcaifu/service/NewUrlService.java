package example.dongfangcaifu.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.baidu.ProvideToFinancial;
import example.deepseek.DeepSeekService;
import example.deepseek.Request.Message;
import example.deepseek.Response.ResponseDeepSeek;
import example.deepseek.enums.RoleEnums;
import example.dongfangcaifu.mapper.NewUrlMapper;
import example.dongfangcaifu.service.email.EmailService;
import example.dongfangcaifu.src.entity.NewUrlEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NewUrlService extends ServiceImpl<NewUrlMapper, NewUrlEntity> {
    @Autowired
    private ProvideToFinancial provideToFinancial;
    @Autowired
    private EmailService emailService;

    @Autowired
    private DeepSeekService deepSeekService;

    private void insert(String url,String title,String content,String contentType,String analyseContent){
        NewUrlEntity one = new NewUrlEntity();
        one.setUrl(url);
        one.setTitle(title);
        one.setContent(content);
        one.setContentType(contentType);
        one.setAnalyseContent(analyseContent);
        this.save(one);
    }

    public void insertBatchAndAnalyse(List<NewUrlEntity> saveListInput,String type){
        List<NewUrlEntity> list = this.list(Wrappers.<NewUrlEntity>lambdaQuery().eq(NewUrlEntity::getContentType, type));
        Set<String> setUrl = list.stream().map(NewUrlEntity::getUrl).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(saveListInput)){
            return;
        }
        List<NewUrlEntity> saveNew = new ArrayList<>();
        for(NewUrlEntity one:saveListInput){
            if (!StringUtils.isBlank(one.getUrl())
            &&!setUrl.contains(one.getUrl())){
                saveNew.add(deepSeekDealNew(one));
                log.info("出现新的新闻告警"+one.toString());
                log.info("出现新的新闻告警"+one.toString());
                log.info("出现新的新闻告警"+one.toString());
            }
        }
        // 加入deepseek分析
        // 加入告警

        this.saveBatch(saveNew);

    }



    private NewUrlEntity deepSeekDealNew(NewUrlEntity newUrlEntity){
        try{
            List<Message> messages = new ArrayList<>();
            messages.add(initUser("给你看一则新闻"));
            ResponseDeepSeek responseDeepSeek1 = deepSeekService.chatCompletionsMul(messages);
            String ana1 = responseDeepSeek1.getChoices().get(0).getMessage().getContent();
            messages.add(initSystem(ana1));
            messages.add(initUser(newUrlEntity.getContent()));
            ResponseDeepSeek responseDeepSeek2 = deepSeekService.chatCompletionsMul(messages);
            String ana2 = responseDeepSeek2.getChoices().get(0).getMessage().getContent();
            messages.add(initSystem(ana2));
            messages.add(initUser("以上新闻涉及社会经济哪一板块"));
            ResponseDeepSeek responseDeepSeek3 = deepSeekService.chatCompletionsMul(messages);
            String ana3 = responseDeepSeek3.getChoices().get(0).getMessage().getContent();
            newUrlEntity.setAnalyseContent(ana3);
        }catch (Exception e){
            newUrlEntity.setAnalyseContent("分析失败");
        }
        return newUrlEntity;
    }

    private Message initUser(String content){
        Message message = new Message();
        message.setRole(RoleEnums.USER.getType());
        message.setContent(content);
        return message;
    }
    private Message initSystem(String content){
        Message message = new Message();
        message.setRole(RoleEnums.ASSISTANT.getType());
        message.setContent(content);
        return message;
    }

    public void judgeInsert(String url,String contentType,String title,String content){
        List<NewUrlEntity> list = this.list(Wrappers.<NewUrlEntity>lambdaQuery().eq(NewUrlEntity::getUrl, url)
                .eq(NewUrlEntity::getTitle, title));
        if (!CollectionUtils.isEmpty(list)){
            return;
        }
        // 调用文心一言
//        String s = provideToFinancial.chatToBaiDu("-------------下面这则新闻会对什么行业产生影响，具体到行业板块和公司名字------------------"+content);
//        insert(url,title,content,contentType,s);
//        //  发送邮件
//        emailService.sendEmail("","新闻邮件"+title+"链接为："+url,s);
    }

}
