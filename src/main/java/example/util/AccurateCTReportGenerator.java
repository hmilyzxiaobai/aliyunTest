package example.util;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.math.BigInteger;

public class AccurateCTReportGenerator {
    public static void main(String[] args) throws Exception {
        // 创建新文档
        XWPFDocument doc = new XWPFDocument();

        // === 1. 医院标题 ===
        XWPFParagraph hospitalPara = doc.createParagraph();
        hospitalPara.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun hospitalRun = hospitalPara.createRun();
        hospitalRun.setText("深圳恒生医院");
        hospitalRun.setBold(true);
        hospitalRun.setFontSize(16);
        hospitalRun.setFontFamily("宋体");
        hospitalRun.addBreak();

        // === 2. 报告单标题 ===
        XWPFRun reportRun = hospitalPara.createRun();
        reportRun.setText("CT检查报告单");
        reportRun.setBold(true);
        reportRun.setFontSize(14);
        reportRun.setFontFamily("宋体");
        reportRun.addBreak();
        reportRun.addBreak(); // 额外空行

        // === 3. 病人信息表格 ===
        XWPFTable table = doc.createTable(9, 2);
        table.setWidth("100%");

        // 设置表格样式 - 无边框
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);

        // 填充表格数据
        fillTableRow(table, 0, "病人号：", "0039748192");
        fillTableRow(table, 1, "检查号：", "7642668");
        fillTableRow(table, 2, "检查设备：", "RS80A");
        fillTableRow(table, 3, "姓名：", "苏靖雯");
        fillTableRow(table, 4, "性别：", "女");
        fillTableRow(table, 5, "年龄：", "31岁");
        fillTableRow(table, 6, "科室：", "骨科");
        fillTableRow(table, 7, "床号：", "");
        fillTableRow(table, 8, "住院号：", "");

        // === 4. 检查信息行 ===
        XWPFParagraph infoPara = doc.createParagraph();
        infoPara.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun infoRun = infoPara.createRun();
        infoRun.setFontFamily("宋体");
        infoRun.setText("检查日期：2025-06-14");
        infoRun.addTab();
        infoRun.setText("报告日期：2025-06-14");
        infoRun.addTab();
        infoRun.setText("检查部位：腰部平扫+三维成像");
        infoRun.addBreak();
        infoRun.addBreak(); // 空行

        // === 5. 影像学表现标题 ===
        XWPFParagraph titlePara = doc.createParagraph();
        titlePara.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun titleRun = titlePara.createRun();
        titleRun.setFontFamily("宋体");
        titleRun.setText("影像学表现：");
        titleRun.setBold(true);
        titleRun.addBreak();

        // === 6. 分隔线 ===
        addDividerLine(doc);

        // === 7. 影像学表现内容 ===
        XWPFParagraph contentPara = doc.createParagraph();
        contentPara.setAlignment(ParagraphAlignment.BOTH);
        contentPara.setIndentationFirstLine(600); // 首行缩进
        XWPFRun contentRun = contentPara.createRun();
        contentRun.setFontFamily("宋体");
        contentRun.setText("腰部平扫+三维成像示：腰椎曲度存在，L3、L4、L5椎体前缘局部骨突出，边缘欠光整，硬膜囊受压，双侧附件未见异常，余结构未见异常改变。");
        contentRun.addBreak();

        // === 8. 分隔线 ===
        addDividerLine(doc);

        // === 9. 影像意见标题 ===
        XWPFParagraph opinionTitlePara = doc.createParagraph();
        opinionTitlePara.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun opinionTitleRun = opinionTitlePara.createRun();
        opinionTitleRun.setFontFamily("宋体");
        opinionTitleRun.setText("影像意见：");
        opinionTitleRun.setBold(true);
        opinionTitleRun.addBreak();

        // === 10. 影像意见内容 ===
        XWPFParagraph opinionPara = doc.createParagraph();
        opinionPara.setAlignment(ParagraphAlignment.BOTH);
        opinionPara.setIndentationFirstLine(600); // 首行缩进
        XWPFRun opinionRun = opinionPara.createRun();
        opinionRun.setFontFamily("宋体");
        opinionRun.setText("L3、L4、L5椎体突出；");
        opinionRun.addBreak();
        opinionRun.setText("请结合临床。");

        // 保存文档
        FileOutputStream out = new FileOutputStream("精准CT检查报告单.docx");
        doc.write(out);
        out.close();
        doc.close();

        System.out.println("格式精确的CT检查报告单生成完成！");
    }

    // 填充表格行（保持原格式）
    private static void fillTableRow(XWPFTable table, int row, String label, String value) {
        table.getRow(row).getCell(0).setText(label);
        table.getRow(row).getCell(1).setText(value);

        // 统一设置字体
        for (XWPFParagraph p : table.getRow(row).getCell(0).getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                r.setFontFamily("宋体");
            }
        }
        for (XWPFParagraph p : table.getRow(row).getCell(1).getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                r.setFontFamily("宋体");
            }
        }
    }

    // 添加分隔线
    private static void addDividerLine(XWPFDocument doc) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        r.setFontFamily("宋体");
        r.setText("————————————————————————————————————————");
        r.addBreak();
    }
}