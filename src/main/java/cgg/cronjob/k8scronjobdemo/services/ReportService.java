package cgg.cronjob.k8scronjobdemo.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import cgg.cronjob.k8scronjobdemo.dto.User;

@Service
public class ReportService {

    public byte[] generateReport() throws IOException {
        List<User> userList = readUsersFromCsv();
        System.out.println("Data loaded from server " + userList.size());

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet(); // SXSSFSheet instead of Sheet

        writeHeaderLine(sheet, workbook); // Pass workbook to header method

        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (User user : userList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getFirstName(), style);
            createCell(row, columnCount++, user.getLastName(), style);
            createCell(row, columnCount++, user.getEmail(), style);
            createCell(row, columnCount++, user.getGender(), style);
            createCell(row, columnCount++, user.getIpAddress(), style);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } finally {
            bos.close();
            workbook.dispose(); // Ensure to dispose SXSSFWorkbook to free resources
        }
        return bos.toByteArray();
    }

    public List<User> readUsersFromCsv() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("users.csv").getInputStream()))) {
            CsvToBean<User> csvToBean = new CsvToBeanBuilder<User>(reader)
                    .withType(User.class)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
            return null;
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(String.valueOf(value)); // Convert value to String
        }
        cell.setCellStyle(style);
    }

    private void writeHeaderLine(SXSSFSheet sheet, SXSSFWorkbook workbook) { // Adjust method signature
        Row headerRow = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        createCell(headerRow, 0, "USER_ID", style);
        createCell(headerRow, 1, "FIRST_NAME", style);
        createCell(headerRow, 2, "LAST_NAME", style);
        createCell(headerRow, 3, "EMAIL", style);
        createCell(headerRow, 4, "GENDER", style);
        createCell(headerRow, 5, "IP_ADDRESS", style);
    }
}
