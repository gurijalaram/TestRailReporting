package com.apriori.shared.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class to handle excel files (xls or xlsx)
 */
@Slf4j
public class ExcelService {

    private static Workbook workbook;
    private static Sheet sheet;
    private String xlPath;
    private String xlSheet;

    public ExcelService(String exPath, String exWorksheet) {
        this.xlPath = exPath;
        this.xlSheet = exWorksheet;
        this.initializeExcel();
    }

    public ExcelService(String exPath) {
        this.xlPath = exPath;
        this.initializeExcel();
    }

    /**
     * @return Number of Sheets count
     */
    public int getSheetCount() {
        int sheetCount = 0;
        try {
            sheetCount = this.workbook.getNumberOfSheets();
        } catch (Exception e) {
            log.error(String.format("Sheet ->%s<- not found!!", this.xlSheet.toString()));
        }
        return sheetCount;
    }

    /**
     * returns the row count in a sheet
     */
    public int getRowCount() {
        int number = 0;
        if (null != this.sheet) {
            number = this.sheet.getLastRowNum() + 1;
        }
        return number;
    }

    /**
     * returns number of columns in a sheet
     *
     * @param rowIndex
     * @return column count for identified row
     */
    public int getColumnCount(int rowIndex) {
        Row row = this.sheet.getRow(rowIndex);
        if (null == row) {
            return -1;
        }
        return row.getLastCellNum();
    }

    /**
     * verify whether sheets exists in downloaded report
     *
     * @return true or false
     */
    public boolean isSheetExist() {
        return isSheetExist(this.xlSheet);
    }

    public boolean isSheetExist(String workSheet) {
        int index = this.workbook.getSheetIndex(workSheet);
        if (index == -1) {
            index = this.workbook.getSheetIndex(workSheet.toUpperCase());
            return !(index == -1);
        } else {
            return true;
        }
    }

    /**
     * returns the data from a cell from column and row for matching text starting from provided row.
     *
     * @param colName
     * @param rowNum
     * @return cell value if found or returns empty string
     */
    public String getCellData(String colName, int rowNum) {
        String cellValue = StringUtils.EMPTY;
        try {
            if (rowNum <= 0) {
                return StringUtils.EMPTY;
            }
            if (null != this.sheet.getRow(rowNum)) {
                Iterator<Cell> iterator = (Iterator<Cell>) this.sheet.getRow(rowNum);
                while (iterator.hasNext()) {
                    Cell headerCell = iterator.next();
                    if (null != headerCell && headerCell.getStringCellValue().trim() == colName.trim()) {
                        Cell targetCell = this.sheet.getRow(rowNum - 1).getCell(headerCell.getColumnIndex());
                        cellValue = targetCell.getStringCellValue();
                    }
                }
            }
        } catch (Exception e) {
            log.info("No Matching data found in row " + rowNum + " and column " + colName);
        }
        return cellValue;
    }

    /**
     * returns the data from column index and row index from excel report.
     *
     * @param colNum
     * @param rowNum
     * @return cell value
     */
    public String getCellData(int colNum, int rowNum) {
        String cellValue = StringUtils.EMPTY;
        try {
            Cell targetCell = this.sheet.getRow(rowNum - 1).getCell(colNum);
            if (targetCell.getCellType() == CellType.FORMULA) {
                cellValue = targetCell.getCellFormula();
            } else {
                cellValue = targetCell.getStringCellValue();
            }
        } catch (IllegalStateException e) {
            log.info("No Matching data found in row " + rowNum + " and column " + colNum);
        }
        return cellValue;
    }

    /**
     * This is to identify the row number based the value by checking the entire excel file
     *
     * @param cellValue
     * @return matched cell row number
     */
    public int getFirstCellRowNum(String cellValue) {
        Iterator<Row> rows = this.sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = (Row) rows.next();
            Iterator<Cell> cells = row.cellIterator();
            while (cells.hasNext()) {
                Cell cell = (Cell) cells.next();
                if (cell.toString().contains(cellValue)) {
                    return cell.getRowIndex();
                }
            }
        }
        return -1;
    }

    /**
     * Get Sheet Names from downloaded report file
     *
     * @return
     */
    public List<String> getSheetNames() {
        // for each sheet in the workbook
        List<String> sheetNamesList = new ArrayList<>();
        for (int i = 0; i < this.workbook.getNumberOfSheets(); i++) {
            sheetNamesList.add(this.workbook.getSheetName(i));
        }
        return sheetNamesList;
    }

    /**
     * initialize the excel objects for downloaded report file.
     */
    private void initializeExcel() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(this.xlPath));
            this.workbook = WorkbookFactory.create(fileInputStream);
            this.setWorksheet();
            fileInputStream.close();
        } catch (Exception e) {
            log.error(String.format("Report ->%s<- not found!!", this.xlPath));
        }
    }

    /**
     * Set worksheet
     */
    private void setWorksheet() {
        if (this.xlSheet == null) {
            this.sheet = this.workbook.getSheetAt((this.getSheetCount() >= 1) ? 0 : 1);
            return;
        }
        if (this.isSheetExist()) {
            this.sheet = this.workbook.getSheet(this.xlSheet);
            return;
        }
        log.error(String.format("No Worksheet -%s- found in Excel file: %s", this.xlSheet, this.xlPath));
    }
}