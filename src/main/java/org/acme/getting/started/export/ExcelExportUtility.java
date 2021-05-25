package org.acme.getting.started.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class ExcelExportUtility<E extends Object> {
    /**
     * @param columns
     */

    private static void fillHeader(XSSFSheet sheet, String[] columns) {
        Row row = sheet.createRow(0);

        for (int cellnum = 0; cellnum < columns.length; cellnum++) {
            row.createCell(cellnum).setCellValue(columns[cellnum]);
        }

    }

    /**
     * @param columns
     * @param dataList
     * @return
     */
    public static final <E> XSSFWorkbook exportExcel(String[] columns, List<E> dataList) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Validated Data");
        fillHeader(sheet, columns);
        fillDataV2(sheet, dataList);
        return wb;
    }

    public static final <T> void fillDataV2(XSSFSheet sheet, List<T> data) throws Exception {
        Class<? extends Object> classz = data.get(0).getClass();
        int rowCount = 0;
        List<String> fieldNames = getFieldNamesForClass(classz);
        for (T t : data) {
            Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            for (String fieldName : fieldNames) {
                Cell cell = row.createCell(columnCount);
                Method method = null;
                try {
                    method = classz.getMethod("get" + capitalize(fieldName));
                } catch (NoSuchMethodException nme) {
                    method = classz.getMethod("get" + fieldName);
                }
                Object value = method.invoke(t, (Object[]) null);
                if (value != null) {
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    }
                }
                columnCount++;
            }
        }
    }

    private static List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
        List<String> fieldNames = new ArrayList<String>();
        Class<?> current = clazz;
        do {
            try {
                Field[] fields = current.getDeclaredFields();
                for (Field f : fields) {
                    fieldNames.add(f.getName());
                }
            } catch (Exception e) {
            }
        } while ((current = current.getSuperclass()) != null);
        return fieldNames;
    }

    private static String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * @param dataList
     */
    protected abstract void fillData(List<E> dataList);

}
