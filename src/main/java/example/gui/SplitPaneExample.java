package example.gui;

import cn.hutool.core.util.StrUtil;
import example.baidu.ChatSendService;
import example.baidu.dto.MessageDto;
import example.baidu.vo.ChatResVoList;
import example.entity.UserChatLog;
import example.manager.UserChatLogManager;
import lombok.SneakyThrows;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 聊天界面
 */

public class SplitPaneExample extends JFrame {


    private JTextField inputField;
    private JTextArea responseTextArea = new JTextArea();

    public SplitPaneExample(UserChatLogManager userChatLogManager) {
        ChatSendService chatSendService = new ChatSendService();
        String userId = "zhangll4";
        List<MessageDto> list = new ArrayList<>();

        setTitle("左右分割窗口示例");
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel leftPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        // 初始值 根据名字查询近期数据
        List<UserChatLog> byUser = userChatLogManager.getByUser(userId);
        for (UserChatLog userChatLog : byUser){
            MessageDto dtoUser= new MessageDto();
            dtoUser.setRole("user");
            dtoUser.setContent(userChatLog.getQuestion());
            MessageDto dtoSys= new MessageDto();
            dtoSys.setRole("assistant");
            dtoSys.setContent(userChatLog.getRequest());
            list.add(dtoUser);list.add(dtoSys);
        }
        String s = userChatLogManager.dealString(byUser);
        if (!StrUtil.isBlankIfStr(s)){
            responseTextArea.append(s);
        }

        inputField.addActionListener(new ActionListener() {

            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {

                String inputValue = inputField.getText();
                MessageDto dto = new MessageDto();
                dto.setRole("user");
                dto.setContent(inputValue);
                list.add(dto);

                if (!inputValue.isEmpty()) {
                    ChatResVoList chat = chatSendService.chat(list, userId);

                    String systemResponse = "问题："+inputValue+"\n"+"回答：" + chat.getMessage()+"\n";

                    userChatLogManager.insertData(userId,inputValue,chat.getMessage());

                  //  responseLabel.setText(systemResponse);
                    responseTextArea.append(systemResponse + "\n"); // 追加内容并换行显示
                    inputField.setText(""); // 清空输入框内容
                }
            }
        });
        leftPanel.add(inputField, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        responseTextArea = new JTextArea();
        responseTextArea.setLineWrap(true); // 开启自动换行
        responseTextArea.setWrapStyleWord(true); // 单词换行
        responseTextArea.setText(s);
        JScrollPane scrollPane = new JScrollPane(responseTextArea); // 使用滚动条显示内容
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200);

        add(splitPane);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new SplitPaneExample().setVisible(true);
//            }
//        });
//    }
}
