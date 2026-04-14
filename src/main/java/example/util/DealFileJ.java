package example.util;
import example.dongfangcaifu.utils.FloatUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DealFileJ {

    static List<String> pathName = new ArrayList<>();

    public static void main(String[] args) {

                // 指定E盘路径
                File eDrive = new File("E:\\");

                // 创建 Excel 工作簿
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("文件夹结构");

            int rowIndex =1;

                // 获取 E: 盘下的所有文件夹
                File[] files = eDrive.listFiles();
                if (files != null) {
                    for (File file : files) {

                         //判断是否为文件夹
                        if (file.getName().length()!=4){
                            continue;
                        }
                        if (FloatUtils.stringToFloat(file.getName())<=1020){
                            continue;
                        }
                        if (file.isDirectory()) {
                            // 获取子文件夹中的文件
                            File[] subFiles = file.listFiles();
                            if (subFiles != null) {
                                for (File subFile : subFiles) {
                                    if (subFile.isDirectory()) {
                                        // 将文件夹名称和文件名称添加到 Excel 表格中
                                        Row row = sheet.createRow(rowIndex++);
                                        row.createCell(0).setCellValue("2025"+file.getName());
                                        row.createCell(1).setCellValue("局综合处");
                                        row.createCell(2).setCellValue(subFile.getName());
//                                        row.createCell(2).setCellValue("传防处");
//                                        row.createCell(3).setCellValue("张磊磊 86891186");
//                                        row.createCell(4).setCellValue("电子文档");
                                    }
                                }
                            }
                        }
                    }
                }

                // 将工作簿输出到文件
                try (FileOutputStream fileOut = new FileOutputStream("20251099.xlsx")) {
                    workbook.write(fileOut);
                    System.out.println("Excel 文件已生成！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }


    public static void listFilesRecursively(File folder) {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    listFilesRecursively(file); // 递归调用
                } else {
                    pathName.add(file.getName());
                    System.out.println("文件: " + file.getAbsolutePath());
                }
            }
        }
    }
}

