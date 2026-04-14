package example.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ListFilesExample2 {
    public static void main(String[] args) {
        try {
            // 获取当前目录路径
            Path currentPath = Paths.get("C:\\Users\\Zlldr\\Desktop\\高质量考核佐证材料\\高质量考核佐证材料");

            System.out.println("当前目录下的所有内容：");

            // 使用Files.list()读取目录内容
            try (Stream<Path> stream = Files.list(currentPath)) {
                stream.forEach(path -> {
                    if (Files.isRegularFile(path)) {
                        System.out.println(path.getFileName());
                    } else if (Files.isDirectory(path)) {
                        System.out.println("文件夹: " + path.getFileName());
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}