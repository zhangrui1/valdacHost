package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.entity.*;
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
public class PrintForKoujiSagyoList {

    /**
     *
     * 懸案リストをダウンロード
     * @param fileName DLファイル名
     *
     * @param kouji 工事
     *
     * @param tenkenRirekiUtilLists 点検リスト
     *
     * @return 懸案リスト.xlsx
     *
     * */
    public static boolean downloadForKoujiSagyonList(String fileName,Kouji kouji,List<TenkenRirekiUtil> tenkenRirekiUtilLists,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.KoujiSagyoRabelTemplate);
        SXSSFWorkbook wb=new SXSSFWorkbook(new XSSFWorkbook(in1),600000);
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");


        /*データ設定*/
        int rowCount=0;
        int ColCount=1;
        for(int nIndex=0;nIndex<tenkenRirekiUtilLists.size();nIndex++){
            //1行目 左
            TenkenRirekiUtil tenkenRirekiUtil=tenkenRirekiUtilLists.get(nIndex);
            setSagyo(wb,sheet,rowCount,ColCount,tenkenRirekiUtil,kouji,styleMap);
            setSagyo(wb,sheet,rowCount,ColCount+5,tenkenRirekiUtil,kouji,styleMap);

            rowCount=rowCount+11;
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
     * @param  nColumnStart
     * 　　　　　　開始列
     * @param  tenkenRirekiUtil
     * 　　　　　　点検リスト
     * @param  kouji
     * 　　　　　　工事
     * @param  styleMap
     *          罫線style
     */

    public static void setSagyo(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,
                                    int nColumnStart,TenkenRirekiUtil tenkenRirekiUtil,Kouji kouji,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        int firstRow=nRowStart;
        int ColCount=nColumnStart;
        XSSFCellStyle style = styleMap.get("normalSize8");

        if(tenkenRirekiUtil.getValve()!=null){
            OoxmlFastUtil.setData(sheet,firstRow,ColCount,tenkenRirekiUtil.getValve().getvNo(),null);//弁番号
            OoxmlFastUtil.setData(sheet,firstRow++,ColCount+2,kouji.getEndYmd(),null);//工事終了日
            OoxmlFastUtil.setData(sheet,firstRow,ColCount,tenkenRirekiUtil.getValve().getBenMeisyo(),null);//弁名称

            firstRow=firstRow+2;
            OoxmlFastUtil.setData(sheet,firstRow,ColCount,"場所",null);//弁設置場所
            OoxmlFastUtil.setData(sheet,firstRow++,ColCount+1,tenkenRirekiUtil.getValve().getSetBasyo(),null);//弁設置場所

            OoxmlFastUtil.setData(sheet,firstRow,ColCount,"形式",null);//弁形式
            OoxmlFastUtil.setData(sheet,firstRow,ColCount+1,tenkenRirekiUtil.getValve().getKeisikiRyaku(),null);//弁形式略称
            OoxmlFastUtil.setData(sheet,firstRow,ColCount+2,"駆動",null);//弁駆動
            OoxmlFastUtil.setData(sheet,firstRow++,ColCount+3,tenkenRirekiUtil.getValve().getSousaRyaku(),null);//弁駆動略称

            OoxmlFastUtil.setData(sheet,firstRow,ColCount,"クラス",null);//弁クラス
            OoxmlFastUtil.setData(sheet,firstRow,ColCount+1,tenkenRirekiUtil.getValve().getClassType(),null);//弁クラス略称
            OoxmlFastUtil.setData(sheet,firstRow,ColCount+2,"呼び径",null);//弁呼び径
            OoxmlFastUtil.setData(sheet,firstRow++,ColCount+3,tenkenRirekiUtil.getValve().getYobikeiRyaku(),null);//弁呼び径略称
        }else{
            firstRow=firstRow+6;
        }

        if(tenkenRirekiUtil.getKiki()!=null){
            OoxmlFastUtil.setData(sheet,firstRow,ColCount,"機器名",null);//機器名
            OoxmlFastUtil.setData(sheet,firstRow++,ColCount+1,tenkenRirekiUtil.getKiki().getKikiMei(),null);//機器名

            OoxmlFastUtil.setData(sheet,firstRow,ColCount,"メーカ",null);//メーカ
            OoxmlFastUtil.setData(sheet,firstRow,ColCount+1,tenkenRirekiUtil.getKiki().getMakerRyaku(),null);//メーカ略称
            OoxmlFastUtil.setData(sheet,firstRow++,ColCount+2,tenkenRirekiUtil.getKiki().getMaker(),null);//メーカ
        }else{
            firstRow=firstRow+2;
        }

        OoxmlFastUtil.setData(sheet,firstRow,ColCount,"点検",null);//点検
        OoxmlFastUtil.setData(sheet,firstRow++,ColCount+1,tenkenRirekiUtil.getTenkenRank(),null);//点検

        OoxmlFastUtil.setData(sheet,firstRow,ColCount,"指示",styleMap.get("isBottomAndLeftSize12"));//指示
        OoxmlFastUtil.setData(sheet,firstRow,ColCount+1,tenkenRirekiUtil.getTenkennaiyo(),styleMap.get("isBottomAndRightSize12"));//指示
        SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,firstRow);
        row.setHeightInPoints(16);
        SXSSFRow rowNull = OoxmlFastUtil.getRowAnyway(sheet,firstRow+1);
        rowNull.setHeightInPoints(14);
        //罫線を描画
        OoxmlFastUtil.setRowDataCellStyle(workbook,sheet,nRowStart,nRowStart+9,ColCount,ColCount+3,true, style);
        //結合
        setSagyoMerger(workbook,sheet,nRowStart,nRowStart+9,ColCount,ColCount+3,styleMap);

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
    public static void setSagyoMerger(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                      int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart,nColumnStart,nColumnStart+1,styleMap.get("normalSize8"));//弁番号
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart,nColumnStart+2,nColumnStart+3,styleMap.get("normalSize8"));//工事完了日
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+1,nRowStart+2,nColumnStart,nColumnStart+3,styleMap.get("normalSize8"));//弁名称
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+3,nRowStart+3,nColumnStart+1,nColumnStart+3,styleMap.get("normalSize8"));//設置場所
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+6,nRowStart+6,nColumnStart+1,nColumnStart+3,styleMap.get("normalSize8"));//機器名
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+7,nRowStart+7,nColumnStart+2,nColumnStart+3,styleMap.get("normalSize8"));//メーカ
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+8,nRowStart+8,nColumnStart+1,nColumnStart+3,styleMap.get("normalSize8"));//点検
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+9,nRowStart+9,nColumnStart,nColumnStart,styleMap.get("normalSize12"));//指示
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+9,nRowStart+9,nColumnStart+1,nColumnStart+3,styleMap.get("normalSize12"));//指示

        for(int i=nRowStart;i<nRowEnd;i++){
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,i);
            row.setHeightInPoints(11);
        }
    }
}
