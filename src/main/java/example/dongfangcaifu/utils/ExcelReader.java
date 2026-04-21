package example.dongfangcaifu.utils;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    static String filePath = "D:\\东方财富分析日报\\监控指标个人版\\monitoring.xlsx";  // 支持 .xls 和 .xlsx

    /**
     * 按行读取Excel文件
     * @param filePath 文件路径
     * @return 行数据列表
     */
    private static List<List<String>> readExcelByRow(String filePath) {
        List<List<String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            // 读取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();

                // 遍历每一列
                for (Cell cell : row) {
                    // 根据单元格类型获取值
                    String cellValue = getCellValue(cell);
                    rowData.add(cellValue);
                }

                // 如果行有数据，添加到结果列表
                if (!rowData.isEmpty()) {
                    data.add(rowData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 获取单元格值
     * @param cell 单元格
     * @return 字符串形式的单元格值
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        String value = "";
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue().toString();
                } else {
                    // 防止科学计数法
                    cell.setCellType(CellType.STRING);
                    value = cell.getStringCellValue();
                }
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                try {
                    value = cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        value = String.valueOf(cell.getNumericCellValue());
                    } catch (Exception e2) {
                        value = cell.getCellFormula();
                    }
                }
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
                value = "ERROR";
                break;
            default:
                value = "";
        }
        return value.trim();
    }

    /**
     * 读取Excel并打印
     */
    public static List<List<String>> printExcelData() {
        List<List<String>> data = readExcelByRow(filePath);
        List<List<String>> res = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) {
           // System.out.print("第 " + (i + 1) + " 行: ");
            List<String> one = new ArrayList<>();

            for (String cell : data.get(i)) {
                System.out.print(cell + "\t");
                one.add(cell);
            }
            res.add(one);
            System.out.println();
        }
        return res;

    }
    public static List<List<String>> printExcelData(String filePathInput) {
        List<List<String>> data = readExcelByRow(filePathInput);
        List<List<String>> res = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) {
            // System.out.print("第 " + (i + 1) + " 行: ");
            List<String> one = new ArrayList<>();

            for (String cell : data.get(i)) {
                System.out.print(cell + "\t");
                one.add(cell);
            }
            res.add(one);
            System.out.println();
        }
        return res;

    }

//    public static void main(String[] args) {
//
//    }
}
