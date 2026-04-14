package example.dongfangcaifu.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ExcelWriteOne {

    public static void writeExcel(List<String> headers,
                                  List<List<String>> data){
        Workbook workbook = new XSSFWorkbook();

        // 创建工作表
        Sheet sheet = workbook.createSheet("最低点数据导出");

        // 创建表头行
        Row headerRow = sheet.createRow(0);

        // 创建表头单元格样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // 设置表头
        // String[] headers = {"Data Time", "Company Name", "Company Code", "Plate","Plate Name","Code Change","Plate Change","Code Percent","Plate Percent"};
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue((String) data.get(i).get(j));
            }
        }

        // 自动调整列宽
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // 指定输出文件夹和文件名
        String outputFolder = "D:\\东方财富分析日报\\"; // 替换为你想要的文件夹路径
        folderCreatorLegacy(outputFolder);
        String fileName = "最低点数据导出"+".xlsx";
        String fullPath = outputFolder + File.separator + fileName;
        try {
            // 确保文件夹存在
            Files.createDirectories(Paths.get(outputFolder));

            // 写入文件
            FileOutputStream outputStream = new FileOutputStream(fullPath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            System.out.println("Excel文件已成功生成到: " + fullPath);
        } catch (IOException e) {
            System.err.println("生成Excel文件时出错: " + e.getMessage());
            e.printStackTrace();
        }

    }
    private static void folderCreatorLegacy(String folderPath){
        // 替换为你的文件夹路径
        File folder = new File(folderPath);
        // 检查文件夹是否存在
        if (!folder.exists()) {
            // mkdir() 只创建单级目录，mkdirs() 创建多级目录
            boolean created = folder.mkdirs();

            if (created) {
                System.out.println("文件夹创建成功: " + folder.getAbsolutePath());
            } else {
                System.out.println("文件夹创建失败: " + folder.getAbsolutePath());
            }
        } else {
            System.out.println("文件夹已存在: " + folder.getAbsolutePath());
        }

    }}
