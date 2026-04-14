package example.controller;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;



@RestController
@RequestMapping("hello")
@Slf4j
public class HelloWorld {

    private final String UPLOAD_DIR = "D:\\image\\";



    @PostMapping("yeap")
    public String yeap(){
    log.info("请求访问");
        //获取访问的http端口信息等
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ip = request.getRemoteAddr();
        int port = request.getRemotePort();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        log.info("该网络访问基本信息为：url:{} ,method:{} ,ip:{} ,port:{};访问时间为：{}",url,method,ip,port,formatter.format(date));
        return "欢迎访问";
    }


    @GetMapping("file")
    public String testFile(@RequestParam(value = "id")String id,
                         @RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return "文件不能为空";
        }

        try {
            // 1. 确保目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 2. 生成唯一文件名（避免覆盖）
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = System.currentTimeMillis() + fileExtension;

            // 3. 保存文件
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 返回成功信息（可自定义）
            return String.format("文件上传成功！\n文件名: %s\n大小: %d bytes\n描述: %s",
                    newFilename, file.getSize(), id);
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败: " + e.getMessage();
        }

    }


    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) {
        try {
            // 1. 构建文件路径
            File file = new File(UPLOAD_DIR + filename);

            // 2. 检查文件是否存在
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 3. 创建Resource对象
            Resource resource = new FileSystemResource(file);

            // 4. 设置响应头（强制下载/直接显示）
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // 自动根据扩展名设置类型
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + file.getName() + "\"") // inline直接显示，attachment强制下载
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
