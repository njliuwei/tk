package com.fhsoft.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @Classname com.fhsoft.util.ExcelContentParser
 * @Description 
 *
 * @author lw
 * @Date 2015-11-16 上午9:14:21
 *
 */
public class ExcelContentParser<T>
{
    private Workbook wb;

    private static final String newExcelPrefx = ".xlsx";

    /**
     * 
     * @Description 
     * @param list
     * @param c
     * @param in
     * @param propertyNames
     * @param fileName
     * @param beginRowNum
     * @throws Exception
     * @Date 2015-11-16 上午10:54:40
     */
    public void parseExcel(List<T> list, Class<T> c, InputStream in, String[] propertyNames, String fileName,
            Integer beginRowNum) throws Exception
    {
        init(in, fileName);
        doParse(list, c, propertyNames, beginRowNum);
    }

    private void init(InputStream in, String fileName) throws Exception
    {
        try
        {
            if(fileName.indexOf(newExcelPrefx) > 0)
            {
                // 2007以上版本excel
                wb = new XSSFWorkbook(in);
            }
            else
            {// 老版本excel
                POIFSFileSystem fs = new POIFSFileSystem(in);
                wb = new HSSFWorkbook(fs);
            }
        }
        catch(Exception ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                in.close();
            }
            catch(IOException ex)
            {
                throw ex;
            }
        }
    }

    /**
     * 
     * @Description 
     * @param list
     * @param c
     * @param propertyNames
     * @param beginRowNum
     * @throws Exception
     * @Date 2015-11-16 上午10:54:51
     */
    private void doParse(List<T> list, Class<T> c, String[] propertyNames, Integer beginRowNum) throws Exception
    {
        Sheet sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();

        if(beginRowNum == null || beginRowNum < 0)
        {
            beginRowNum = 0;
        }
        for(int row = beginRowNum; row <= rowNum; row++)
        {
            doParseRow(sheet, row, list, c, propertyNames);
        }
    }

    /**
     * 
     * @Description 
     * @param sheet
     * @param row
     * @param list
     * @param c
     * @param propertyNames
     * @throws Exception
     * @Date 2015-11-16 上午10:55:00
     */
    @SuppressWarnings("unchecked")
    private void doParseRow(Sheet sheet, int row, List<T> list, Class<T> c, String[] propertyNames) throws Exception
    {
        T bean = c.newInstance();

        Row dataRow = sheet.getRow(row);
        if(dataRow == null)
            return;

        if(bean instanceof Map)
        {
            Map<String, Object> map = (Map<String, Object>)bean;
            for(int j = dataRow.getFirstCellNum(); j < dataRow.getLastCellNum() && j<propertyNames.length; ++j)
            {
                Object cellValue = getCellValue(dataRow.getCell(j));
                String cellStr = (cellValue == null ? "" : cellValue.toString().trim());
                if(cellValue instanceof Number)
                {
                    cellStr = cellStr.endsWith(".0") ? cellStr.substring(0, cellStr.length() - 2) : cellStr;
                }

                if(cellStr.contains("E") && (cellStr.length() - cellStr.lastIndexOf("E")) < 5)
                {
                    try
                    {
                        DecimalFormat df = new DecimalFormat("#");
                        cellStr = df.format(Double.parseDouble(cellStr));
                    }
                    catch(NumberFormatException e)
                    {
                    }
                }
                cellStr = cellStr.replace("　", " ").trim();// 替换全角的空格为半角的空格并trim
                if(cellValue instanceof Number)
                    cellStr = cellStr.endsWith(".0") ? cellStr.substring(0, cellStr.length() - 2) : cellStr;

                map.put(propertyNames[j], cellStr);
            }
        }
        else
        {
            for(int j = dataRow.getFirstCellNum(); j < dataRow.getLastCellNum() && j<propertyNames.length; ++j)
            {
                convertExcelType(bean, propertyNames[j], getCellValue(dataRow.getCell(j)));
            }
        }

        list.add(bean);
    }

    /**
     * 
     * @Description 
     * @param cell
     * @return
     * @Date 2015-11-16 上午10:55:09
     */
    private Object getCellValue(Cell cell)
    {
        Object value = null;
        if(cell != null)
        {
            int cellType = cell.getCellType();
            CellStyle style = cell.getCellStyle();
            short format = style.getDataFormat();
            switch(cellType)
            {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    double numTxt = cell.getNumericCellValue();
                    if(format == 22 || format == 14)
                        value = HSSFDateUtil.getJavaDate(numTxt);
                    else
                        value = numTxt;
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    boolean booleanTxt = cell.getBooleanCellValue();
                    value = booleanTxt;
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    value = null;
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    // HSSFFormulaEvaluator eval = new HSSFFormulaEvaluator(
                    // (HSSFWorkbook) wb);
                    // eval.evaluateInCell(cell);
                    // value = getCellValue(cell);
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    RichTextString rtxt = cell.getRichStringCellValue();
                    if(rtxt == null)
                    {
                        break;
                    }
                    String txt = rtxt.getString();
                    value = txt;
                    break;
                default:
                    // System.out.println(cell.getColumnIndex()+" col cellType="+cellType);
            }
        }
        return value;

    }

    /**
     * 
     * @Description 
     * @param t
     * @param propertyName
     * @param value
     * @throws Exception
     * @Date 2015-11-16 上午10:55:17
     */
    private void convertExcelType(T t, String propertyName, Object value) throws Exception
    {
        String cellStr = (value == null ? "" : value.toString().trim());

        Class<?> fieldType = t.getClass().getDeclaredField(propertyName).getType();
        if(String.class.equals(fieldType))
        {
            if(cellStr.contains("E") && (cellStr.length() - cellStr.lastIndexOf("E")) < 5)
            {
                try
                {
                    DecimalFormat df = new DecimalFormat("#");
                    cellStr = df.format(Double.parseDouble(cellStr));
                }
                catch(NumberFormatException e)
                {
                }
            }
            cellStr = cellStr.replace("　", " ").trim();// 替换全角的空格为半角的空格并trim
            if(value instanceof Number)
                cellStr = cellStr.endsWith(".0") ? cellStr.substring(0, cellStr.length() - 2) : cellStr;

            setValueToBean(t, propertyName, cellStr);
        }
        else if(Float.class.equals(fieldType) || fieldType == Float.TYPE)
        {
            Float floatTemp = toFloat(cellStr);
            if(floatTemp == null || floatTemp.equals(Float.NaN))
                return;

            setValueToBean(t, propertyName, floatTemp);
        }
        else if(Integer.class.equals(fieldType) || fieldType == Integer.TYPE)
        {
            DecimalFormat df = new DecimalFormat("#");
            cellStr = df.format(Double.parseDouble(cellStr));

            setValueToBean(t, propertyName, Integer.valueOf(cellStr));
        }
        else if(Long.class.equals(fieldType) || fieldType == Long.TYPE)
        {
            setValueToBean(t, propertyName, Math.round(Double.valueOf(cellStr)));
        }
        else if(Double.class.equals(fieldType) || fieldType == Double.TYPE)
        {
            Double doubleTemp = toDouble(cellStr);
            if(doubleTemp == null || doubleTemp.equals(Double.NaN))
                return;

            setValueToBean(t, propertyName, doubleTemp);
        }
        else if(Date.class.equals(fieldType))
        {
            Date useDate = null;
            if(value instanceof Number)
            {// excel中用户的日期是’自定义‘格式的时候 取出来是个数字类型的
             // 所以在这要转换一下
                useDate = HSSFDateUtil.getJavaDate((Double)value);
            }
            else if(value instanceof Date)
            {
                useDate = (Date)value;
            }
            else
            {
                // 2010.8.100:00:00:0
                String pattern = "yyyy-MM-dd";
                if((cellStr.indexOf(".") == 4 || cellStr.indexOf(".") == 2 || cellStr.indexOf("/") == 4 || cellStr.indexOf("/") == 2))
                {
                    cellStr = cellStr.replace(".", "-").replace("/", "-");
                }
                // 有填入 2010.8.100:00:00:0数据的情况 所以作如下处理
                if(cellStr.indexOf(":") != -1)
                {
                    pattern = "yyyy-MM-dd hh:mm:ss";
                    cellStr = cellStr.substring(0, cellStr.indexOf(":") - 2) + " "
                            + cellStr.substring(cellStr.indexOf(":") - 2);

                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                useDate = simpleDateFormat.parse(cellStr);// 出现用户自定义格式的情况：obj.getClass().equals(Date.class)为false
            }
            setValueToBean(t, propertyName, useDate);
        }
    }

    /**
     * 
     * @Description 
     * @param t
     * @param name
     * @param value
     * @throws Exception
     * @Date 2015-11-16 上午10:55:38
     */
    private void setValueToBean(T t, String name, Object value) throws Exception
    {
        BeanUtils.setProperty(t, name, value);
    }

    /**
     * 
     * @Description 
     * @param obj
     * @return
     * @Date 2015-11-16 上午10:55:38
     */
    private Float toFloat(Object obj)
    {
        if(obj == null || "".equals(obj))
            return null;
        if(obj instanceof Number)
            return ((Number)obj).floatValue();
        try
        {
            return Float.parseFloat(obj.toString().replace(" ", ""));
        }
        catch(Exception e)
        {
        }
        return Float.NaN;
    }

    /**
     * 
     * @Description 
     * @param obj
     * @return
     * @Date 2015-11-16 上午10:55:38
     */
    public static Double toDouble(Object obj)
    {
        if(obj == null || "".equals(obj))
            return null;
        if(obj instanceof Number)
            return ((Number)obj).doubleValue();
        try
        {
            return Double.parseDouble(obj.toString().trim());
        }
        catch(Exception e)
        {
        }
        return 0d;
    }

}
