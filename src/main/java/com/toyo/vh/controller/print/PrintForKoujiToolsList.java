package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.entity.Kouji;
import com.toyo.vh.entity.TenkenRirekiUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/10.
 */
public class PrintForKoujiToolsList {

    /**
     *
     * 懸案リストをダウンロード
     * @param fileName DLファイル名
     *
     * @param kouji 工事
     *
     * @param tenkenRirekiUtilLists 点検リスト
     *
     * @return ICS点検リスト.xlsx
     *
     * */
    public static boolean PrintForKoujiToolsList(String fileName,Kouji kouji,List<TenkenRirekiUtil> tenkenRirekiUtilLists,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.KoujiToolListTemplate);
        SXSSFWorkbook wb=new SXSSFWorkbook(new XSSFWorkbook(in1),600000);
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");
        XSSFCellStyle style16=styleMap.get("normalSizeFalse16");


        /*データ設定*/
        //Kouji名
        setDataToExcelTitle(wb,sheet,styleMap,kouji);

        int rowCount=4;
        int ColCount=0;
        int Noban=1;

        for(int nIndex=0;nIndex<tenkenRirekiUtilLists.size();nIndex++){
            if(tenkenRirekiUtilLists.get(nIndex).getValve()!=null){
                //行高を設定
                SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,rowCount);
                //罫線を描画
                OoxmlFastUtil.setTableDataCellStyle(wb,sheet,rowCount,rowCount+1,ColCount,ColCount+6,false, styleMap);
                //1行目
                TenkenRirekiUtil tenkenRirekiUtil=tenkenRirekiUtilLists.get(nIndex);
                OoxmlFastUtil.setData(sheet, rowCount, ColCount, Noban, style);//番号
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+1,tenkenRirekiUtil.getValve().getvNo(),style);//弁番号
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+2,tenkenRirekiUtil.getValve().getBenMeisyo(),style);//弁名称
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+3,tenkenRirekiUtil.getTenkennaiyo(),style);//点検内容


                OoxmlFastUtil.setData(sheet,rowCount,ColCount+4,tenkenRirekiUtil.getValve().getKougu1M(),style);//工具-メガネ
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+5,tenkenRirekiUtil.getValve().getKougu2S(),style);//工具-スパナ
                OoxmlFastUtil.setData(sheet,rowCount++,ColCount+6,tenkenRirekiUtil.getValve().getKougu3T(),style);//工具-定盤
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+4,tenkenRirekiUtil.getValve().getKougu4L(),style);//工具-六角レンチ
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+5,tenkenRirekiUtil.getValve().getKougu5O(),style);//工具-その他

                //結合
                setToolsMerger(wb,sheet,rowCount-1,rowCount,0,6,styleMap);

                row.setHeightInPoints(20);
                row = OoxmlFastUtil.getRowAnyway(sheet,rowCount);
                row.setHeightInPoints(20);
                rowCount=rowCount+1;
                Noban=Noban+1;
            }
        }
        /*Excel出力*/
        try {
            OutputStream out = res.getOutputStream();
            wb.write(out);
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     *
     * 工事作業ラベルセルなどの結合
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
     * @param  styleMap
     *          罫線style
     */
    public static void setToolsMerger(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                      int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart,nColumnStart,styleMap.get("isTopAndLeft"));//番号
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+1,nColumnStart+1,styleMap.get("isTop"));//弁番号
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+2,nColumnStart+2,styleMap.get("isTop"));//弁名称
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+3,nColumnStart+3,styleMap.get("isTop"));//点検内容
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart,nColumnStart+4,nColumnStart+4,styleMap.get("isTop"));//工具-メガネ
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart,nColumnStart+5,nColumnStart+5,styleMap.get("isTop"));//工具-スパナ
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart,nColumnStart+6,nColumnStart+6,styleMap.get("isTopAndRight"));//工具-定盤

        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+1,nRowStart+1,nColumnStart+4,nColumnStart+4,styleMap.get("isBottom"));//工具-六角レンチ
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+1,nRowStart+1,nColumnStart+5,nColumnStart+6,styleMap.get("isBottomAndRight"));//工具-その他
    }

    /**
     *
     * 弁単位でデータを出力
     *
     * @param wb
     *            ワークブック
     * @param sheet
     *            シート
     * @param  styleMap
     *             style
     * @param  kouji
     *             工事情報
     *
     */
    public static void setDataToExcelTitle(SXSSFWorkbook wb,SXSSFSheet sheet,Map<String,XSSFCellStyle> styleMap,Kouji kouji) {

        //第１行
        int rowNum1=0;
        OoxmlFastUtil.setData(sheet, rowNum1, 0, kouji.getKjMeisyo(), null);//Kouji名
        OoxmlFastUtil.setData(sheet, rowNum1, 5, "担当", null);//担当

        //第2行
        int rowNum2=1;
        OoxmlFastUtil.setData(sheet, rowNum2, 0, "NO", null);//NO
        OoxmlFastUtil.setData(sheet, rowNum2, 1, "弁番号", null);//弁番号
        OoxmlFastUtil.setData(sheet, rowNum2, 2, "弁名称", null);//弁名称
        OoxmlFastUtil.setData(sheet, rowNum2, 3, "点検内容", null);//点検内容
        OoxmlFastUtil.setData(sheet, rowNum2, 4, "工　　　具", null);//工　　　具

        //第3行
        int rowNum3=2;
        OoxmlFastUtil.setData(sheet, rowNum3, 4, "メガネ", null);//メガネ
        OoxmlFastUtil.setData(sheet, rowNum3, 5, "スパナ", null);//スパナ
        OoxmlFastUtil.setData(sheet, rowNum3, 6, "定盤", null);//定盤

        //第4行
        int rowNum4=3;
        OoxmlFastUtil.setData(sheet, rowNum4, 4, "六角レンチ", null);//六角レンチ
        OoxmlFastUtil.setData(sheet, rowNum4, 5, "その他", null);//その他

        //セル結合
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,0,4,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum4,0,0,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum4,1,1,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum4,2,2,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum4,3,3,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum2,4,6,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum4,rowNum4,5,6,null);

        //枠を描画
        OoxmlFastUtil.setRowDataCellStyle(wb,sheet,rowNum1,rowNum4,0,6,false,styleMap.get("normal"));
    }
}
