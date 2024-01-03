package example;
import java.io.File;
public class FileTestRead {



    public static void main(String[] args) {
        String folderPath = "\\\\10.192.17.39\\数字研发项目共享\\VTDC\\SOR"; // 替换成你的文件夹路径
        File folder = new File(folderPath);
        int index = 0;
        // 检查文件夹是否存在
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles(); // 获取文件夹中的所有文件

            if (files != null) {
                // 遍历文件数组并输出文件名
                for (File file : files) {
                    index++;
                  //  System.out.println(file.getName());
                }
            } else {
                System.out.println("文件夹为空或无法访问文件夹");
            }
        } else {
            System.out.println("文件夹不存在或不是一个有效的文件夹路径");
        }
        System.out.println(index);
    }


}
