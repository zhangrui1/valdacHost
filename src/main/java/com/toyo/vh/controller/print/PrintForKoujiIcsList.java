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
public class PrintForKoujiIcsList {

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
    public static boolean PrintForKoujiIcsList(String fileName,Kouji kouji,List<TenkenRirekiUtil> tenkenRirekiUtilLists,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.KoujiIcsListTemplate);
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

        int rowCount=3;
        int ColCount=0;
        for(int nIndex=0;nIndex<tenkenRirekiUtilLists.size();nIndex++){
            if(tenkenRirekiUtilLists.get(nIndex).getValve()!=null){
                //行高を設定
                SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,rowCount);
                //罫線を描画
                OoxmlFastUtil.setRowDataCellStyle(wb,sheet,rowCount,rowCount,ColCount,ColCount+6,false, style);
                //1行目
                TenkenRirekiUtil tenkenRirekiUtil=tenkenRirekiUtilLists.get(nIndex);
                OoxmlFastUtil.setData(sheet, rowCount, ColCount, rowCount - 2, style);//弁番号
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+1,tenkenRirekiUtil.getValve().getvNo(),style);//弁番号
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+2,tenkenRirekiUtil.getValve().getBenMeisyo(),style);//弁名称
                OoxmlFastUtil.setData(sheet,rowCount,ColCount+3,tenkenRirekiUtil.getTenkennaiyo(),style);//点検内容
                String icstmp=tenkenRirekiUtil.getValve().getIcs();
                if(icstmp.isEmpty()){
                    OoxmlFastUtil.setData(sheet,rowCount,ColCount+4,"□",style16);//適合
                    OoxmlFastUtil.setData(sheet,rowCount,ColCount+5,"□",style16);//近似
                }else{
                    if(icstmp.startsWith("A")||icstmp.startsWith("a")){
                        OoxmlFastUtil.setData(sheet,rowCount,ColCount+4,"■",style16);//適合
                        OoxmlFastUtil.setData(sheet,rowCount,ColCount+5,"□",style16);//近似
                    }else if(icstmp.startsWith("B")||icstmp.startsWith("b")){
                        OoxmlFastUtil.setData(sheet,rowCount,ColCount+4,"□",style16);//適合
                        OoxmlFastUtil.setData(sheet,rowCount,ColCount+5,"■",style16);//近似
                    }else{
                        OoxmlFastUtil.setData(sheet,rowCount,ColCount+4,"□",style16);//適合
                        OoxmlFastUtil.setData(sheet,rowCount,ColCount+5,"□",style16);//近似
                    }
                }

                OoxmlFastUtil.setData(sheet,rowCount,ColCount+6,tenkenRirekiUtil.getValve().getIcs(),style);//ＩＣＳ番号

                row.setHeightInPoints(20);
                rowCount=rowCount+1;
            }
        }
        //縮小して全体を表示する
        for(int rIndex=3;rIndex<rowCount;rIndex++){
            OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rIndex, 1);
            OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rIndex, 2);
            OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rIndex, 6);
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
        OoxmlFastUtil.setData(sheet, rowNum2, 4, "ＩＣＳ番号", null);//ＩＣＳ番号

        //第3行
        int rowNum3=2;
        OoxmlFastUtil.setData(sheet, rowNum3, 4, "適合", null);//適合
        OoxmlFastUtil.setData(sheet, rowNum3, 5, "近似", null);//近似
        OoxmlFastUtil.setData(sheet, rowNum3, 6, "番号", null);//番号

        //セル結合
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,0,4,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum3,0,0,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum3,1,1,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum3,2,2,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum3,3,3,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum2,4,6,null);

        //枠を描画
        OoxmlFastUtil.setRowDataCellStyle(wb,sheet,rowNum1,rowNum3,0,6,false,styleMap.get("normal"));
    }

}
