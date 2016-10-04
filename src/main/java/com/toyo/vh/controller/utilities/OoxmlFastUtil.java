package com.toyo.vh.controller.utilities;


import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/09.
 */
public class OoxmlFastUtil {
    /**
     * セルに文字列を出力する
     *
     * @param sheet
     *            シート
     * @param nRow
     *            出力行番号
     * @param nColumn
     *            出力列番号
     * @param object
     *            出力データ
     * @param style
     *            セルスタイル
     */
    public static void setData(SXSSFSheet sheet, int nRow, int nColumn,
                               Object object, XSSFCellStyle style) {
        setData(sheet, nRow, nColumn, object, style, "0");
    }

    /**
     * セルに文字列を出力する
     *
     * @param sheet
     *            シート
     * @param nRow
     *            出力行番号
     * @param nColumn
     *            出力列番号
     * @param object
     *            出力データ
     * @param style
     *            セルスタイル
     * @param zeroValue
     *            値が0の時に設定する値
     */
    public static void setData(SXSSFSheet sheet, int nRow, int nColumn,
                               Object object, XSSFCellStyle style, String zeroValue) {
        assert sheet != null && object != null;
        SXSSFRow row = getRowAnyway(sheet, nRow);
        SXSSFCell cell = getCellAnyway(row, nColumn);
        setData(cell, object, style, zeroValue);
    }

    /**
     * セルに文字列を出力する
     *
     * @param cell
     *            対象セル
     * @param object
     *            出力データ
     * @param style
     *            セルスタイル
     * @param zeroValue
     *            値が0の時に設定する値
     */
    private static void setData(SXSSFCell cell, Object object,
                                XSSFCellStyle style, String zeroValue) {
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (object instanceof String) {
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            cell.setCellValue((String) object);
        } else if (object instanceof Integer) {
            Integer integer = (Integer) object;
            if (0 == integer) {
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(zeroValue);
            } else {
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue((Integer) object);
            }
        } else if (object instanceof Double) {
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
            if (Double.isNaN((Double) object)) {
                cell.setCellValue(zeroValue);
            } else {
                Double value = (Double) object;
                if (0 == value.compareTo((Double) 0.0)) {
                    cell.setCellValue(zeroValue);
                } else {
                    cell.setCellValue((Double) object);
                }
            }
        }
    }
    /**
     * 指定した行番号の<b>XSSFRow</b>を取得する。有効範囲外の行の場合は新規作成する。
     *
     * @param sheet
     *            <b>Sheet</b>
     * @param nRow
     *            取得したい行の行番号
     * @return <b>SXSSFRow</b>
     */
    public static SXSSFRow getRowAnyway(SXSSFSheet sheet, int nRow) {
        assert sheet != null;
        SXSSFRow row = (SXSSFRow)sheet.getRow(nRow);
        if (row == null) {
            row = (SXSSFRow)sheet.createRow(nRow);
        }
        return row;

    }

    /**
     * 指定した列番号の<b>XSSFCell</b>を取得する。有効範囲外のセルの場合は新規作成する。
     *
     * @param row
     *            <b>XSSFRow</b>
     * @param nColumn
     *            取得したいセルの列番号
     * @return <b>SXSSFCell</b>
     */
    public static SXSSFCell getCellAnyway(SXSSFRow row, int nColumn) {
        assert row != null;
        SXSSFCell cell = (SXSSFCell)row.getCell(nColumn);
        if (cell == null) {
            cell = (SXSSFCell)row.createCell(nColumn);
        }
        return cell;
    }

    public static Map<String,XSSFCellStyle> createCellStyle(SXSSFWorkbook wb){
        Map<String,XSSFCellStyle> styleMap=new HashMap<String, XSSFCellStyle>();
        Color color=new Color(255, 255, 255);
        Short fontSize8=7;
        Short fontSize10=10;
        Short fontSize12=12;
        Short fontSize16=16;
        styleMap.put("normal", createTableDataCellStyle(wb,false,false,false,false,color,fontSize10,false));
        styleMap.put("normalSize8", createTableDataCellStyle(wb,false,false,false,false,color,fontSize8,false));
        styleMap.put("normalSize12", createTableDataCellStyle(wb,false,false,false,false,color,fontSize12,false));
        styleMap.put("normalSize16", createTableDataCellStyle(wb,false,false,false,false,color,fontSize16,true));
        styleMap.put("normalSizeFalse16", createTableDataCellStyle(wb,false,false,false,false,color,fontSize16,false));
        styleMap.put("isTop", createTableDataCellStyle(wb,true,false,false,false,color,fontSize10,false));
        styleMap.put("isTopSize16", createTableDataCellStyle(wb,true,false,false,false,color,fontSize16,true));
        styleMap.put("isBottom", createTableDataCellStyle(wb,false,true,false,false,color,fontSize10,false));
        styleMap.put("isLeft", createTableDataCellStyle(wb,false,false,true,false,color,fontSize10,false));
        styleMap.put("isLeftSize16", createTableDataCellStyle(wb,false,false,true,false,color,fontSize16,true));
        styleMap.put("isRight", createTableDataCellStyle(wb,false,false,false,true,color,fontSize10,false));
        styleMap.put("isTopAndLeft", createTableDataCellStyle(wb,true,false,true,false,color,fontSize10,false));
        styleMap.put("isTopAndLeftSize16", createTableDataCellStyle(wb,true,false,true,false,color,fontSize16,true));
        styleMap.put("isTopAndRight", createTableDataCellStyle(wb,true,false,false,true,color,fontSize10,false));
        styleMap.put("isTopAndRightSize16", createTableDataCellStyle(wb,true,false,false,true,color,fontSize16,true));
        styleMap.put("isBottomAndLeft", createTableDataCellStyle(wb,false,true,true,false,color,fontSize10,false));
        styleMap.put("isBottomAndLeftSize12", createTableDataCellStyle(wb,false,true,true,false,color,fontSize12,true));
        styleMap.put("isBottomAndLeftSize16", createTableDataCellStyle(wb,false,true,true,false,color,fontSize16,true));
        styleMap.put("isBottomAndRight", createTableDataCellStyle(wb,false,true,false,true,color,fontSize10,false));
        styleMap.put("isBottomAndRightSize12", createTableDataCellStyle(wb,false,true,false,true,color,fontSize12,true));
        styleMap.put("isBottomAndRightSize16", createTableDataCellStyle(wb,false,true,false,true,color,fontSize16,true));
        styleMap.put("isLeftAndRight", createTableDataCellStyle(wb,false,false,true,true,color,fontSize10,false));

        return  styleMap;
    }

    /**
     * 罫線スタイルの<b>CellStyle</b>を生成
     *
     * @param workbook
     *            ワークブック
     * @param backgroundColor
     *            背景色
     * @return <b>CellStyle</b>
     */
    public static XSSFCellStyle createTableDataCellStyle(SXSSFWorkbook workbook,boolean isTop,boolean isBottom,boolean isLeft,boolean isRight,
                                                         Color backgroundColor,short fontSize,boolean bold) {
        XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);// 必ずsetFillForegroundColorの直前に記述すること
        style.setFillForegroundColor(new XSSFColor(backgroundColor));
        style.setAlignment(CellStyle.ALIGN_LEFT);//水平方法の位置
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);//折り返して全体を表示する
//        style.setShrinkToFit(true);//縮小して全体を表示する

        //文字サイズ設定
        XSSFFont font = (XSSFFont)workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");

        font.setFontHeightInPoints(fontSize);
        if (bold) {
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        }
        style.setFont(font);

        if(isTop){
            style.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
        }else{
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        }
        if(isBottom){
            style.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
        }else{
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        }
        if(isLeft){
            style.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
        }else{
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        }
        if(isRight){
            style.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
        }else{
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        }
        return style;
    }
    /**
     * 罫線スタイルの<b>CellStyle</b>を生成
     * セル結合
     *
     * @param workbook
     *            ワークブック
     * @param sheet
     *            シート
     * @param  nRowStart
     *             開始行
     * @param  nRowEnd
     * 　　　　　　終了行
     * @param  nColumnStart
     * 　　　　　　開始列
     * @param  nColumnEnd
     * 　　　　　　終了列
     * @param  style
     *          罫線style
     */
    public static void setMerger(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                 int nColumnStart,int nColumnEnd,XSSFCellStyle style) {
        assert sheet != null;
        sheet.addMergedRegion(new CellRangeAddress(nRowStart,nRowEnd,nColumnStart,nColumnEnd));
        SXSSFRow row = getRowAnyway(sheet, nRowStart);
        SXSSFCell cell = getCellAnyway(row, nColumnStart);
        cell.setCellStyle(style);
    }
    /**
     * 罫線スタイルの<b>CellStyle</b>を生成
     * 四角の範囲に周囲太線、中は細線を描画する
     *
     * @param workbook
     *            ワークブック
     * @param sheet
     *            シート
     * @param  nRowStart
     *             開始行
     * @param  nRowEnd
     * 　　　　　　終了行
     * @param  nColumnStart
     * 　　　　　　開始列
     * @param  nColumnEnd
     * 　　　　　　終了列
     * @param isBorder
     *            罫線描画フラグ
     * @param  styleMap
     *          罫線style
     */
    public static void setTableDataCellStyle(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                             int nColumnStart,int nColumnEnd, boolean isBorder,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;

        //Range内のすべてセルに罫線を描画
        for(int rIndex=nRowStart;rIndex<=nRowEnd;rIndex++){
            SXSSFRow row = getRowAnyway(sheet, rIndex);
            for(int cIndex=nColumnStart;cIndex<=nColumnEnd;cIndex++){
                SXSSFCell cell=getCellAnyway(row,cIndex);
                cell.setCellStyle(styleMap.get("normal"));
            }
        }
        //初行のTopのみ太罫線
        SXSSFRow row = getRowAnyway(sheet, nRowStart);

        SXSSFCell cell=getCellAnyway(row,nColumnStart);
        cell.setCellStyle(styleMap.get("isTopAndLeft"));
        for(int cIndex=nColumnStart+1;cIndex<nColumnEnd;cIndex++){
            cell=getCellAnyway(row,cIndex);
            cell.setCellStyle(styleMap.get("isTop"));
        }
        cell=getCellAnyway(row,nColumnEnd);
        cell.setCellStyle(styleMap.get("isTopAndRight"));

        //間の行
        for(int cIndexCenter=nRowStart+1;cIndexCenter<nRowEnd;cIndexCenter++){
            row = getRowAnyway(sheet, cIndexCenter);

            cell=getCellAnyway(row,nColumnStart);
            cell.setCellStyle(styleMap.get("isLeft"));

            cell=getCellAnyway(row,nColumnEnd);
            cell.setCellStyle(styleMap.get("isRight"));

        }

        //最後の行のBottomのみ太罫線
        SXSSFRow rowEnd = getRowAnyway(sheet, nRowEnd);
        cell=getCellAnyway(rowEnd,nColumnStart);
        cell.setCellStyle(styleMap.get("isBottomAndLeft"));

        for(int cIndex=nColumnStart+1;cIndex<nColumnEnd;cIndex++){
            cell=getCellAnyway(rowEnd,cIndex);
            cell.setCellStyle(styleMap.get("isBottom"));
        }
        cell=getCellAnyway(rowEnd,nColumnEnd);
        cell.setCellStyle(styleMap.get("isBottomAndRight"));
    }

    /**
     * 上線は太線のセル行を探す
     *
     * @param nRow
     *            行データ
     * @return 有効列数
     */
    public static int getRowForBold(SXSSFSheet sheet,int nRow,int pageRowNum) {
        int nRowIndex=nRow;
        for(nRowIndex=nRow;nRowIndex>(nRow-pageRowNum);nRowIndex--){
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet, nRowIndex);
            if (row != null) {
                SXSSFCell cell = getCellAnyway(row, 0);
                CellStyle styletmp = cell.getCellStyle();
                if (styletmp.getBorderTop()==(XSSFCellStyle.BORDER_MEDIUM)) {
                    break;
                }
            }
        }
        return nRowIndex;
    }
    /**
     * 空行を探す
     *
     * @param nRow
     *            行データ
     * @return 有効列数
     */
    public static int getNullRow(SXSSFSheet sheet,int nRow,int pageRowNum,int nColNum) {
        for(int nRowIndex=nRow;nRowIndex>(nRow-pageRowNum);nRowIndex--){
            Boolean nullFlg=checkNullRow(sheet,nRowIndex,nColNum);
            if(nullFlg){
                return  nRowIndex;
            }
        }
        return nRow-pageRowNum;
    }
    public static Boolean checkNullRow(SXSSFSheet sheet,int nRowNum,int nColNum) {
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet, nRowNum);
            boolean trueFlg=true;
            if (row != null) {
                for(int nCol=0;nCol<nColNum;nCol++){
                    SXSSFCell cell = getCellAnyway(row, nCol);
                    if(cell.getStringCellValue()==null || cell.getStringCellValue()==""){

                    }else{
                        trueFlg=false;
                        return trueFlg;
                    }
                }
            }
        return trueFlg;
    }
    /**
     * セルの縮小して全体を表示する
     *
     * @param nRow
     *            行データ
     * @return 有効列数
     */
    public static void setShrinkToFitForCell(SXSSFWorkbook wb,SXSSFSheet sheet,int nRow,int nCol) {

        SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet, nRow);
        SXSSFCell cell= OoxmlFastUtil.getCellAnyway(row, nCol);
        CellStyle styletmp = cell.getCellStyle();
        CellStyle newStyletmp=wb.createCellStyle();
        newStyletmp.cloneStyleFrom(styletmp);
        newStyletmp.setWrapText(false);//折り返して全体を表示する
        newStyletmp.setShrinkToFit(true);//縮小して全体を表示する
        cell.setCellStyle(newStyletmp);
    }
    /**
     * 罫線スタイルの<b>CellStyle</b>を生成
     * 1行のみ描画する
     *
     * @param workbook
     *            ワークブック
     * @param sheet
     *            シート
     * @param  nRowStart
     *             開始行
     * @param  nRowEnd
     * 　　　　　　終了行
     * @param  nColumnStart
     * 　　　　　　開始列
     * @param  nColumnEnd
     * 　　　　　　終了列
     * @param isBorder
     *            罫線描画フラグ
     * @param  style
     *          罫線style
     */
    public static void setRowDataCellStyle(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                           int nColumnStart,int nColumnEnd, boolean isBorder,XSSFCellStyle style) {
        assert sheet != null;

        //Range内のすべてセルに罫線を描画
        for(int rIndex=nRowStart;rIndex<=nRowEnd;rIndex++){
            SXSSFRow row = getRowAnyway(sheet, rIndex);
            for(int cIndex=nColumnStart;cIndex<=nColumnEnd;cIndex++){
                SXSSFCell cell=getCellAnyway(row,cIndex);
                cell.setCellStyle(style);
            }
        }
    }
    /**
     * 罫線スタイルの<b>CellStyle</b>を生成
     * 1行のみ描画する
     *
     * @param workbook
     *            ワークブック
     * @param sheet
     *            シート
     * @param  nRow
     *             行
     * @param  nColumn
     * 　　　　　　列
     * @param isBold
     *            太字フラグ
     * @param fontSize
     *            文字サイズ
     * @param  fontHeight
     *             行高
     */
    public static void setCellStyleForLabel(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRow,int nColumn,
                                            boolean isBold,short fontSize,float fontHeight) {
        assert sheet != null;

        //style設定
        CellStyle style=workbook.createCellStyle();
        Font font = workbook.createFont();
        if (isBold) {
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//文字太字
        }
        font.setFontHeightInPoints((short)fontSize);//文字サイズ
//        font.setFontName(DEFAULT_FONT_NAME);
        style.setFont(font);//文字太字　と　文字サイズ

        style.setAlignment(CellStyle.ALIGN_GENERAL);//水平方向の標準
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);//垂直方向の上詰め
        style.setWrapText(true);//折り返して全体を表示する


        //セルに罫線を描画
        SXSSFRow row = getRowAnyway(sheet, nRow);
        SXSSFCell cell=getCellAnyway(row,nColumn);
        cell.setCellStyle(style);
        row.setHeightInPoints(fontHeight);//行高設定
    }
    /**
     * 文字の水平、垂直の位置設定
     * 1セルのみ
     *
     * @param workbook
     *            ワークブック
     * @param sheet
     *            シート
     * @param  nRowStart
     *             開始行
     * @param  nRowEnd
     *             終了行
     * @param  nColumnStart
     * 　　　　　　開始列
     * @param  nColumnEnd
     *             終了列
     * @param Alignment
     *            水平方向の位置
     * @param Vertical
     *            垂直方向の位置
     */
    public static  void setSingleCellStyle(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                           int nColumnStart,int nColumnEnd,short Alignment,short Vertical){
        for(int i=nRowStart;i<nRowEnd;i++) {
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet, i);
            //center middleに設定
            for (int cIndex = nColumnStart; cIndex < nColumnEnd; cIndex = cIndex + 1) {
                SXSSFCell cell = OoxmlFastUtil.getCellAnyway(row, cIndex);
                XSSFCellStyle oldStyle = (XSSFCellStyle) cell.getCellStyle();
                // Workbookから新たなセルスタイルを作成する
                CellStyle newStyle = workbook.createCellStyle();
                //
                // 元のセルスタイルのクローンを作成する
                newStyle.cloneStyleFrom(oldStyle);
                // クローンに罫線を追加設定する
                newStyle.setAlignment(Alignment);//水平方法の位置
                newStyle.setVerticalAlignment(Vertical);
                // セルに新たなセルスタイルにクローンを設定する
                cell.setCellStyle(newStyle);
            }
        }
    }
}
