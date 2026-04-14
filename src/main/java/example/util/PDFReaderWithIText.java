package example.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;

public class PDFReaderWithIText {
    public static void main(String[] args) {
        try {
            PdfReader reader = new PdfReader("D:\\临时文件\\包4、广州迪澳生物科技有限公司.pdf");
            int pages = reader.getNumberOfPages();

            StringBuilder text = new StringBuilder();

            for (int i = 1; i <= pages; i++) {
                text.append(PdfTextExtractor.getTextFromPage(reader, i));
            }

            System.out.println(text.toString());
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}