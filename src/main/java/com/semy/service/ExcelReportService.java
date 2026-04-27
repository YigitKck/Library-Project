package com.semy.service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import com.semy.entities.Book;
import java.io.*;

@Service
// Excel e mail, kitap ismi ve kiralama bitiş tarihi yazdırılacak
public class ExcelReportService {

    public void generateLoanReport(String filePath, String userMail, Book book, String endDate) throws IOException, InvalidFormatException {
        Workbook workbook;
        Sheet sheet;

        File file = new File(filePath);
        boolean fileExists = file.exists();

        if (fileExists && file.length() > 0) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(fis);
            }
            sheet = workbook.getSheetAt(0);
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Rapor");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("E-mail");
            header.createCell(1).setCellValue("Kitap İsmi");
            header.createCell(2).setCellValue("Kira Bitiş Tarihi");
        }

        int lastRowNum = sheet.getLastRowNum();
        Row row = sheet.createRow(lastRowNum + 1);
        row.createCell(0).setCellValue(userMail);
        row.createCell(1).setCellValue(book.getTitle());
        row.createCell(2).setCellValue(endDate);

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }
}
