package com.yy.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Component
public class ExportExcelBaseService {
    /**
     * 获取设置好的样式
     * @param workbook 工作空间
     * @return
     */
    public HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//单元格-垂直居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//单元格-水平居中

        cellStyle.setFillPattern(HSSFCellStyle.DIAMONDS);//背景色-方块填充
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前背景色-天蓝
        cellStyle.setFillBackgroundColor(HSSFColor.LIGHT_YELLOW.index);//后背景色-浅黄


        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_SLANTED_DASH_DOT);//底边框样式-倾斜断点
        cellStyle.setBottomBorderColor(HSSFColor.DARK_RED.index);//底边框颜色-暗红

        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));//日期显示格式
//        headRowCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));

        cellStyle.setFont(this.getFont(workbook));//设置字体

        return cellStyle;
    }
    /**
     * 获取设置好的样式
     * @param workbook 工作空间
     * @return
     */
    public HSSFCellStyle getSimpleCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));//日期显示格式
//        headRowCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        cellStyle.setFont(this.getSimpleFont(workbook));//设置字体

        return cellStyle;
    }

    /**
     * 生成下拉菜单（此方法不使用用下拉数据超过255，数据则过大需要创建隐藏sheet）
     * @param parMap 由列号以及列所在下拉数据的集合组成的map
     * @param sheet 操作的sheet
     * @param firstRow 设置的开始行号
     */
    public void buildSelect(Map<Integer,List<String>> parMap, HSSFSheet sheet,int firstRow){
        //设置默认下拉内容
        if(parMap!=null && parMap.size()>0) {
            for (Map.Entry<Integer, List<String>> integerListEntry : parMap.entrySet()) {
                //设置下拉控制的范围
                Integer key = integerListEntry.getKey();
                CellRangeAddressList regions = new CellRangeAddressList(firstRow, 999, key, key);
                // 生成下拉框内容
                String[] strings = new String[integerListEntry.getValue().size()];
                integerListEntry.getValue().toArray(strings);
                DVConstraint constraint = DVConstraint.createExplicitListConstraint(strings);
                // 绑定下拉框和作用区域
                HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
                // 对sheet页生效
                sheet.addValidationData(data_validation);
            }
        }
    }

    /**
     * 添加输入数据校验
     * @param sheet
     * @param min
     * @param max
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     */
    public void excelRuleNumberBetween(Sheet sheet, int min, int max, int firstRow, int lastRow, int firstCol, int lastCol){
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //设置行列范围
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        //设置数据
        DataValidationConstraint constraint = helper.createIntegerConstraint(DataValidationConstraint.OperatorType.BETWEEN,
                String.valueOf(min),String.valueOf(max));
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        dataValidation.createErrorBox("输入值类型或大小有误", String.format("请输入%s~%s之间的数值",min,max));
        //处理Excel兼容性问题
        if(dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置单元格某列为某种属性
     * @param workbook
     * @param sheet
     * @param cellNum
     * @param formatString
     */
    public void setColumnDataFormat(Workbook workbook,Sheet sheet,int cellNum,String formatString){
        CellStyle css = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        css.setDataFormat(format.getFormat(formatString));
        //sheet.setColumnWidth(cellNum, 20);
        sheet.setDefaultColumnStyle(cellNum,css);
    }

    /**
     * 获取设置好的字体
     * @param workbook 工作空间
     * @return
     */
    public HSSFFont getFont(HSSFWorkbook workbook) {
        HSSFFont fontStyle = workbook.createFont();
        fontStyle.setFontName("宋体");//名称-宋体
        fontStyle.setFontHeightInPoints((short)13);//高度-13
        fontStyle.setColor(HSSFColor.WHITE.index);//颜色-白色
        fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        fontStyle.setItalic(true);//斜体
        fontStyle.setUnderline(HSSFFont.U_SINGLE);//下划线

        return fontStyle;
    }

    /**
     * 修改默认字体arial->宋体
     * @param workbook
     */
    public void updateHeadRowFont(HSSFWorkbook workbook){
        HSSFFont fontAt = workbook.getFontAt((short) 0);
        fontAt.setFontHeightInPoints((short) 13);
        fontAt.setCharSet(HSSFFont.DEFAULT_CHARSET);
        fontAt.setFontName("宋体");
    }
    /**
     * 获取设置好的字体
     * @param workbook 工作空间
     * @return
     */
    public HSSFFont getSimpleFont(HSSFWorkbook workbook) {
        HSSFFont fontStyle = workbook.createFont();
        fontStyle.setFontName("宋体");//名称-宋体
        fontStyle.setFontHeightInPoints((short)13);//高度-13
        fontStyle.setColor(HSSFColor.RED.index);//颜色-红色
        return fontStyle;
    }

    /**
     * 通过流的方式输出excle到页面
     * @param response 响应
     * @param workbook 工作空间
     * @param fileName 文件名
     */
    public void outExcelStream(HttpServletResponse response, Workbook workbook, String fileName){
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
            workbook.write(os);
            os.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //删除单个文件夹
    public void deleteFileDir(String fileName) {
        File file = new File(fileName);
        DeleteAll(file);
    }


    public void DeleteAll(File dir) {
        if (dir.isFile()) {
            dir.delete();
            return;

        } else {
            File[] files = dir.listFiles();
            for (File file : files) {

                DeleteAll(file);
            }
        }

        dir.delete();
    }
}
