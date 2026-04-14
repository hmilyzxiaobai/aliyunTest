package example.dongfangcaifu.news.interfaceFile;

import example.dongfangcaifu.service.NewUrlService;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NewReadALl {
    @Autowired
    NewUrlService newUrlService;

    public void execute(){
        Reflections reflections = new Reflections("example.dongfangcaifu.news", new SubTypesScanner(false));
        Set<Class<? extends NewInterFace>> subTypes = reflections.getSubTypesOf(NewInterFace.class);
        for (Class<? extends NewInterFace> clazz : subTypes) {
            try {
                NewInterFace instance = clazz.getDeclaredConstructor().newInstance();
                instance.readALl(newUrlService);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
