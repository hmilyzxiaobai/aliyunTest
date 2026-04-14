package example.dongfangcaifu.utils;

import example.dongfangcaifu.service.GetSellPrice;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class ExcelSellWrite {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    static final String dfSave;
    static {
        LocalDateTime currentDateTime = LocalDateTime.now();
        dfSave = currentDateTime.format(formatter);
    }

    @Autowired
    private GetSellPrice getSellPrice;

    public void sell(String path,String fileName){
        // 根据日期分文件夹  免得出错
        String filePath = "D:\\dataExcel\\"+path+"\\"+fileName+".xlsx";

        // 读取

        try {
            FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(fis);

            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String code = String.valueOf(row.getCell(2));

                String sellPrice = getSellPrice.getNewPrice(code);

                //卖出时间
                Cell cell4 = row.getCell(4); // 获取第一个单元格
                if (cell4 == null) {
                    cell4 = row.createCell(4); // 如果单元格不存在则创建
                }
                cell4.setCellValue(dfSave);
                // 价格
                Cell cell5 = row.getCell(5); // 获取第一个单元格
                if (cell5 == null) {
                    cell5 = row.createCell(5); // 如果单元格不存在则创建
                }
                cell5.setCellValue(sellPrice);
                // 涨幅
                Cell cell6 = row.getCell(6); // 获取第一个单元格
                if (cell6 == null) {
                    cell6 = row.createCell(6); // 如果单元格不存在则创建
                }
                cell6.setCellValue(FloatUtils.stringToFloat(sellPrice)-FloatUtils.stringToFloat(String.valueOf(row.getCell(3))));

                CellStyle decimalStyle = workbook.createCellStyle();
                decimalStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

            }
            // 4. 将修改后的工作簿写回文件
            fis.close(); // 先关闭输入流

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);

            // 关闭资源
            fos.close();
            workbook.close();

            System.out.println("Excel文件修改完成！");

        } catch (IOException e) {
            System.err.println("读取Excel文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private String calPercent(String cell3,String cell6){





        return "";
    }
}
