package example;

import example.gui.JFrameGui;
import example.gui.SplitPaneExample;
import example.mapper.UserInfoMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
@MapperScan({"example.**.mapper"})

public class AliyunApplication {

    public static void main(String[] args) {
        new JFrameGui();
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new SplitPaneExample().setVisible(true);
//            }
//        });
        SpringApplication.run(AliyunApplication.class, args);

    }

}
