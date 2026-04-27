package com.semy.service;

import com.semy.entities.Book;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ExcelReportServiceTest {

    private ExcelReportService excelReportService;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        excelReportService = new ExcelReportService();
        tempFile = File.createTempFile("loan_report_test_", ".xlsx");
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testGenerateLoanReport_CreatesFileAndWritesCorrectData() throws Exception {
        String email = "testuser@example.com";
        String endDate = "2025-12-31";

        Book book = new Book();
        book.setTitle("Test Kitabı");

        excelReportService.generateLoanReport(tempFile.getAbsolutePath(), email, book, endDate);

        try (FileInputStream fis = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            assertEquals(2, sheet.getLastRowNum() + 1);

            Row dataRow = sheet.getRow(1);
            assertEquals(email, dataRow.getCell(0).getStringCellValue());
            assertEquals("Test Kitabı", dataRow.getCell(1).getStringCellValue());
            assertEquals(endDate, dataRow.getCell(2).getStringCellValue());

            workbook.close();
        }
    }

    @Test
    void testGenerateLoanReport_AppendsToExistingFile() throws Exception {
        String email1 = "first@example.com";
        String email2 = "second@example.com";

        Book book = new Book();
        book.setTitle("Kitap");

        excelReportService.generateLoanReport(tempFile.getAbsolutePath(), email1, book, "2025-11-01");
        excelReportService.generateLoanReport(tempFile.getAbsolutePath(), email2, book, "2025-12-01");

        try (FileInputStream fis = new FileInputStream(tempFile)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            assertEquals(3, sheet.getLastRowNum() + 1);

            Row row1 = sheet.getRow(1);
            Row row2 = sheet.getRow(2);

            assertEquals(email1, row1.getCell(0).getStringCellValue());
            assertEquals(email2, row2.getCell(0).getStringCellValue());

            workbook.close();
        }
    }
}
