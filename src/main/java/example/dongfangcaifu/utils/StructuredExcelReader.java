package example.dongfangcaifu.utils;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StructuredExcelReader {

    public static void main(String[] args) {
        String filePath = "D:\\dataExcel\\20250326144252.xlsx";

        try {
            List<VerifyData> verifyDatas = readEmployeesFromExcel(filePath);

            // 打印读取到的数据

            for (VerifyData emp : verifyDatas) {
                System.out.printf(emp.toString());
            }

        } catch (IOException e) {
            System.err.println("读取Excel文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<VerifyData> readEmployeesFromExcel(String filePath) throws IOException {
        List<VerifyData> verifyDataList = new ArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 跳过表头行（假设第一行是表头）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                VerifyData emp = new VerifyData();
                emp.setDateWrite( row.getCell(0).getStringCellValue());
                emp.setName(row.getCell(1).getStringCellValue());
                String stringCellValue = String.valueOf(row.getCell(2));
                emp.setCompanyCode(stringCellValue);
                emp.setBuyPrice(row.getCell(3).getStringCellValue());

                verifyDataList.add(emp);
            }
        }

        return verifyDataList;
    }

    @Data
    static class VerifyData {
        private String dateWrite;
        private String name;
        private String CompanyCode;
        private String buyPrice;
        private String sellDate;
        private String sellPrice;
        private String sellUp;

    }
}