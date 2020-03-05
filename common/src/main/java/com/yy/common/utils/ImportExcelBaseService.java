package com.yy.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class ImportExcelBaseService{

    /**
     * 导入值校验
     * @param sheet 工作表
     * @param row 行
     * @param colNum 列编号
     * @param errorHint 错误提示
     * @return 校验通过返回空，否则抛出异常
     */
    public void validCellValue(Sheet sheet,Row row,int colNum,String errorHint) {
        if("".equals(this.getCellValue(sheet, row, colNum - 1))) {
            throw new RuntimeException("校验 :第" + (row.getRowNum() + 1) + "行" + colNum +"列"+ errorHint + "不能为空");
        }
    }

    /**
     * 从输入流中获取excel工作表
     * @param iStream 输入流
     * @param fileName 带 .xls或.xlsx 后缀的文件名
     * @return 文件名为空返回空;
     *                  格式不正确抛出异常;
     *                  正常返回excel工作空间对象
     */
    public Workbook getWorkbookByInputStream(InputStream iStream, String fileName) {
        Workbook workbook = null;

        try {
            if(null == fileName) {
                return null;
            }

            if(fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(iStream);
            }else if(fileName.endsWith(".xlsx")){
                workbook = new XSSFWorkbook(iStream);
            }else {
                throw new IOException("The document type don't support");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iStream != null){
                try {
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return workbook;
    }

    /**
     * 从Workbook中获取一个sheet,如果没有就创建一个
     * @param workbook 工作空间
     * @param index 第几个sheet
     * @return 返回sheet
     */
    public Sheet getSheetByWorkbook(Workbook workbook,int index) {
        Sheet sheet = workbook.getSheetAt(index);
        if(null == sheet) {
            sheet = workbook.createSheet();
        }

        sheet.setDefaultRowHeightInPoints(20);//行高
        sheet.setDefaultColumnWidth(20);//列宽

        return sheet;
    }

    /**
     * 获取指定sheet指定row中指定column的cell值
     * @param sheet 工作表
     * @param row 行
     * @param column 第几列
     * @return 返回单元格的值或""
     */
    public String getCellValue(Sheet sheet,Row row,int column) {
        if(sheet == null || row == null) {
            return "";
        }

        return this.getCellValue(row.getCell(column));
    }

    /**
     * 获取指定sheet指定row中cell值
     * @param sheet 工作表
     * @param row 行
     * @return 返回整行的一个集合
     */
    public List<String> getCellAllRowValue(Sheet sheet, Row row) {
        if(sheet == null || row == null) {
            return Collections.EMPTY_LIST;
        }
        ArrayList<String> strings = new ArrayList<>();
        short minColIx = row.getFirstCellNum();
        short maxColIx = row.getLastCellNum();
        for(short colIx=minColIx; colIx<maxColIx; colIx++) {
        Cell cell = row.getCell(colIx);
        if(cell == null) {
        continue;
        }
        strings.add(this.getCellValue(cell));
        }
        return strings;
    }

    /**
     * 从单元格中获取单元格的值
     * @param cell 单元格
     * @return 返回值或""
     */
    public String getCellValue(Cell cell) {
        if(cell == null) {
            return "";
        }

        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                Number number = cell.getNumericCellValue();
                String numberStr = String.valueOf(number);

                if(numberStr.endsWith(".0")) {
                    numberStr = numberStr.replace(".0", "");//取整数
                }
                if(numberStr.indexOf("E") >=0 ) {
                    numberStr = new DecimalFormat("#").format(number);//取整数
                }

                return numberStr;
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue().trim();
            case Cell.CELL_TYPE_FORMULA://公式
                return "";
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default :
                break;
        }

        return "";
    }
    /**
     * 解析POI导入Excel中日期格式数据
     * @param currentCell
     * @return currentCellValue
     */
    public static String importByExcelForDate(Cell currentCell) {
        String currentCellValue = "";
        // 判断单元格数据是否是日期
        if ("yyyy/mm;@".equals(currentCell.getCellStyle().getDataFormatString())
                || "m/d/yy".equals(currentCell.getCellStyle().getDataFormatString())
                || "yy/m/d".equals(currentCell.getCellStyle().getDataFormatString())
                || "mm/dd/yy".equals(currentCell.getCellStyle().getDataFormatString())
                || "dd-mmm-yy".equals(currentCell.getCellStyle().getDataFormatString())
                || "yyyy/m/d".equals(currentCell.getCellStyle().getDataFormatString())) {
            if (DateUtil.isCellDateFormatted(currentCell)) {
                // 用于转化为日期格式
                Date d = currentCell.getDateCellValue();
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                currentCellValue = formater.format(d);
            }
        } else {
            // 不是日期原值返回
            currentCellValue = currentCell.toString();
        }
        return currentCellValue;
    }

    /**
     * 判断该行是否为空行
     * @param row 行
     * @return 为空行返回true,不为空行返回false
     */
    public boolean isBlankRow(Row row) {
        if(row == null) {
            return true;
        }

        Iterator<Cell> iter = row.cellIterator();
        while(iter.hasNext()) {
            Cell cell = iter.next();
            if(cell == null) {
                continue;
            }

            String value = this.getCellValue(cell);
            if(!this.isNULLOrBlank(value)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断一个对象是否为空
     * @param obj 对象
     * @return 为空返回true,不为空返回false
     */
    public boolean isNULLOrBlank(Object obj) {
        if(obj != null && !"".equals(obj.toString())) {
            return false;
        }

        return true;
    }
}