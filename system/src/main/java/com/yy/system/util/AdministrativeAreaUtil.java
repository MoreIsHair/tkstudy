package com.yy.system.util;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YY
 * @date 2019/11/8
 * @description 省市区工具类
 */
@Slf4j
public class AdministrativeAreaUtil {
    /**
     * 读取json文件，返回json串
     * @param fileName 绝对路径
     * @return
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            if (!jsonFile.exists()){
                log.error("该路径{}下文件不存在",fileName);
                return jsonStr;
            }
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取json文件，返回对象
     * @param fileName 当前项目下的json文件名
     * @return
     */
    public static List<AdministrativeAreaParam> readJsonFileToObject(String fileName) {
        String path = System.getProperty("user.dir");
        log.debug("当前项目路径：{}",path);
        fileName = path + File.separator + fileName;
       try {
           return JSONArray.parseArray(readJsonFile(fileName), AdministrativeAreaParam.class);
       }catch (Exception e){
           log.error("json转换异常");
       }
       return Collections.EMPTY_LIST;
    }

    /**
     * 读取项目所在目录下的json文件生成省市区excel文件
     * @param jsonFileName 项目所在目录下的json文件文件名，结构为name，code，child为List
     * @param outExcelPath 输出文件路径（为空则为项目所在目录下的《省市区.xls》）
     *                     "C:\\Users\\Administrator\\Desktop\\省市区.xls";
     */
    public static void buildAdministrativeAreaExcel(String jsonFileName,String outExcelPath) throws IOException {
        if (StringUtils.isBlank(jsonFileName)){
            log.error("文件名称为空");
            return;
        }
        List<AdministrativeAreaParam> administrativeAreaParams = AdministrativeAreaUtil.readJsonFileToObject(jsonFileName);
        // 所有省（表头）
        List<String> collect1 = administrativeAreaParams.stream().map(AdministrativeAreaParam::getName).collect(Collectors.toList());
        // 生成省市信息
        List<HashMap<String, List<String>>> collect = administrativeAreaParams.stream().map(m -> {
            HashMap<String, List<String>> stringListHashMap = new HashMap<>();
            stringListHashMap.put(m.getName(),m.getChild()==null?
                    Collections.emptyList():m.getChild().stream().map(AdministrativeAreaParam::getName).collect(Collectors.toList()));
            return stringListHashMap;
        }).collect(Collectors.toList());
        log.debug(administrativeAreaParams.toString());
        // 输出文件路径
        String path = System.getProperty("user.dir");
        //String filePath = "C:\\Users\\Administrator\\Desktop\\省市区.xls";
        if (StringUtils.isBlank(outExcelPath)){
            outExcelPath = path + File.separator + "省市区.xls";
        }
        //工作空间
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第1张工作表
        HSSFSheet sheet = workbook.createSheet("省市信息导出");
        //第2张工作表
        HSSFSheet sheet2 = workbook.createSheet("乡县信息导出");
        //行高
        sheet.setDefaultRowHeightInPoints(20);
        //列宽
        sheet.setDefaultColumnWidth(20);

        //行高
        sheet2.setDefaultRowHeightInPoints(20);
        //列宽
        sheet2.setDefaultColumnWidth(20);
        //行表头
        HSSFRow headRow = sheet.createRow(0);
        HSSFRow row1 = sheet.createRow(1);

        int headRowColumn =0;
        for(String l:collect1){
            //赋值
            headRow.createCell(headRowColumn).setCellValue(l);
            headRowColumn++;
        }
        //创建行数据体
        int rownum = 1;
        for (int i = 0; i < 100; i++) {
            // 一个省下面最多不可能超过100个市
            HSSFRow	bodyRow = sheet.createRow(sheet.getLastRowNum() + 1);
            int bodyRowColumn = 0;
            for (HashMap<String, List<String>> stringListHashMap : collect) {
                for (Map.Entry<String, List<String>> stringListEntry : stringListHashMap.entrySet()) {
                    List<String> strings = stringListHashMap.get(stringListEntry.getKey());
                    // 设置数量
                    row1.createCell(bodyRowColumn).setCellValue(strings.size());
                    // 通过行数与集合元素下标的对应关系取值
                    bodyRow.createCell(bodyRowColumn++).setCellValue(strings.size()>rownum-1?strings.get(rownum-1):"");
                }
            }
            rownum++;
        }
        // 组装乡县信息
        HashMap<String, List<String>> stringListHashMap = new HashMap<>();
        // 省
        administrativeAreaParams.forEach(m->{
            if (m.getChild()!=null){
                // 市
                m.getChild().forEach(a->{
                    ArrayList<String> strings = new ArrayList<>();
                    if (a.getChild()!=null){
                        // 区县
                        a.getChild().forEach(c-> strings.add(c.getName()));
                    }else {
                        if (a.getChildArray()!=null){
                            strings.addAll(a.getChildArray());
                        }
                    }
                    stringListHashMap.put(a.getName(),strings);
                });
            }
        });
        int sheet2RowNum = 0;
        for (Map.Entry<String, List<String>> stringListEntry : stringListHashMap.entrySet()) {
            HSSFRow row = sheet2.createRow(sheet2RowNum++);
            // 乡县所在的市
            row.createCell(0).setCellValue(stringListEntry.getKey());
            // 市中区县的数量
            row.createCell(1).setCellValue(stringListEntry.getValue().size());
            for (String s : stringListEntry.getValue()) {
                row.createCell(row.getLastCellNum()).setCellValue(s);
            }
        }
        FileOutputStream out = null;
        try {
            //写文件
            File f = new File(outExcelPath);
            //不存在则新增
            if(!f.getParentFile().exists()){
                f.getParentFile().mkdirs();
            }
            if(!f.exists()){
                f.createNewFile();
            }
            out = new FileOutputStream(f);
            out.flush();
            workbook.write(out);
        }catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (out !=null){
                out.close();
            }
        }
    }

}
