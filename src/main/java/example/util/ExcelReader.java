package example.util;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelReader {

 //   public static void main(String[] args) {
    public static  Map<String,String> readerExcel(String fileName) {
        String filePath = "D:\\excelFile\\"+fileName+".xlsx"; // 替换为你的Excel文件路径
        Map<String,String> data = new HashMap<>();

        try {
            List<List<String>> excelData = readExcel(filePath);


            // 打印读取的数据

            for(int i = 2 ; i<excelData.get(0).size();i++){
                String codeStr = excelData.get(0).get(i);
                String code = regex(codeStr);
                if ("支撑位".equals(excelData.get(1).get(1))){
                    data.put(code,excelData.get(1).get(i)+","+excelData.get(2).get(i));
                }else {
                    data.put(code,excelData.get(2).get(i)+","+excelData.get(1).get(i));
                }
            }
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static List<List<String>> readExcel(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();

        FileInputStream inputStream = new FileInputStream(new File(filePath));
        Workbook workbook = null;

        try {
            // 根据文件扩展名创建不同的Workbook实例
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                throw new IllegalArgumentException("不支持的文件格式");
            }

            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历所有行
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();

                // 遍历所有单元格
                for (Cell cell : row) {
                    // 根据单元格类型获取值
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                rowData.add(cell.getDateCellValue().toString());
                            } else {
                                rowData.add(String.valueOf(cell.getNumericCellValue()));
                            }
                            break;
                        case BOOLEAN:
                            rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            rowData.add(cell.getCellFormula());
                            break;
                        case BLANK:
                            rowData.add("");
                            break;
                        default:
                            rowData.add("未知类型");
                    }
                }
                data.add(rowData);
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            inputStream.close();
        }

        return data;
    }


    private static String regex(String input){
    //    String input = "平安银行(000001.SZ)";

        // 正则匹配 6 位数字
        Pattern pattern = Pattern.compile("\\((\\d{6})\\.");
        Matcher matcher = pattern.matcher(input);
        String stockCode ="";

        if (matcher.find()) {
            stockCode = matcher.group(1);
            System.out.println(stockCode); // 输出: 000001
        }
    return stockCode;
    }
}