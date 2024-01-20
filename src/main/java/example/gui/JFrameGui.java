package example.gui;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.entity.UserInfo;
import example.manager.UserChatLogManager;
import example.mapper.UserInfoMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * 登录界面
 */

public class JFrameGui extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserChatLogManager userChatLogManager;

    public JFrameGui() {
        setTitle("登录");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("登录");

        loginButton.addActionListener(this);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        // 在这里进行登录验证，这里只是简单的输出用户名和密码
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUsername, username).eq(UserInfo::getPassword, password));
        if (Objects.isNull(userInfo)){
            throw new IllegalAccessException("校验错误");
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SplitPaneExample(userChatLogManager).setVisible(true);
            }
        });
    }


}
