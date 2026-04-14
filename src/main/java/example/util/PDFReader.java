package example.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class PDFReader {
    public static void main(String[] args) {
        try {
            // 加载PDF文件
            File file = new File("D:\\临时文件\\包4、广州迪澳生物科技有限公司.pdf");
            System.out.println(file.getName());
            PDDocument document = PDDocument.load(file);

            // 创建PDFTextStripper对象
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // 设置起始和结束页码（可选）
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(document.getNumberOfPages());

            // 提取文本
            String text = pdfStripper.getText(document);
            System.out.println(text);

            // 关闭文档
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}