package com.toyo.vh.controller.print;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 維瑞 on 2016/03/01.
 */
public class CreateZip {

    public static void main() throws Exception {
        CreateZip cz = null;
        try {
            cz = new CreateZip("日本語.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = null;

        for (int i = 0; i < 20; i++) {
            try {
                baos = getFile("日本語" + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cz.addFile("日本語" + i + ".xls", baos.toByteArray());
        }
        cz.zos.close();
    }


    public static ByteArrayOutputStream getFile(String file) throws Exception {

        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(0, file);

        String value = "日本語";

        Row row = sheet.getRow(0);
        if (row == null) {
            row = sheet.createRow(0);
        }

        Cell cell = row.getCell((short) (0));
        if (cell == null) {
            cell = row.createCell((short) (0));
        }

        cell.setCellValue(value);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        workbook.write(baos);

        return baos;
    }

    /** stream **/
    private ZipOutputStream zos = null;

    private CreateZip(String zipFileName) throws Exception {
        zos = new ZipOutputStream(new FileOutputStream(new File(zipFileName)));
//        zos.setEncoding("MS932");
    }

    /*
     * Webの場合はこのコンストラクタを利用。
     *
     * public CreateZip(HttpServletResponse response)throws Exception {
     *     zos = new ZipOutputStream(response.getOutputStream());
     * }
     */
    private void addFile(String entryName, byte[] data) throws Exception {

        zos.putNextEntry(new ZipEntry(entryName));
        zos.write(data, 0, data.length);
        zos.closeEntry();

    }



}
