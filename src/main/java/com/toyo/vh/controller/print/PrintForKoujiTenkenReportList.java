package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.entity.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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
public class PrintForKoujiTenkenReportList {

    /**
     *
     * 懸案リストをダウンロード
     * @param fileName DLファイル名
     *
     * @param kouji 工事
     *
     * @param tenkenRirekiMap 点検リスト
     *
     * @return 工事の点検報告書.xlsx
     *
     * */
    public static boolean downloadForKoujiTenkenReportList(String fileName,Kouji kouji,List<Valve> valveList,Map<Valve,List<TenkenRirekiUtil>> tenkenRirekiMap,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.KoujiTenkenReportNewTemplate);
        SXSSFWorkbook wb=new SXSSFWorkbook(new XSSFWorkbook(in1),600000);
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");




        /*データ設定*/
        //Kouji名
        setDataToExcelTitle(wb,sheet,styleMap,kouji);
        int rowCount=2;
        int ColCount=0;


        for(Valve valve:valveList){
            List<TenkenRirekiUtil> tenkenRirekiUtils=tenkenRirekiMap.get(valve);
            if(valve!=null && tenkenRirekiUtils!=null){
                rowCount=setTenkenReport(wb,sheet,rowCount,0,valve,tenkenRirekiUtils,kouji,styleMap);
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
     * @param  nColumnStart
     * 　　　　　　開始列
     * @param  tenkenRirekiUtils
     * 　　　　　　点検リスト
     * @param  kouji
     * 　　　　　　工事
     * @param  styleMap
     *          罫線style
     */

    public static Integer setTenkenReport(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,
                                    int nColumnStart,Valve valve,List<TenkenRirekiUtil> tenkenRirekiUtils,Kouji kouji,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        int firstRow=nRowStart;
        int ColCount=nColumnStart;
        XSSFCellStyle style = styleMap.get("normal");
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        style.setFont(font);//文字太字　と　文字サイズ
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直方向のcenter
        style.setWrapText(true);//折り返して全体を表示する



        //Valve部分
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getvNo(),null);//弁番号
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getBenMeisyo(),null);//弁名称
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getKeisikiRyaku(),null);//弁形式
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getClassType(),null);//クラス
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getYobikeiRyaku(),null);//呼び径
        if(valve.getKenanFlg().equals(Config.KenanFlg)){
            OoxmlFastUtil.setData(sheet,firstRow,12,"○",null);//懸案有
        }else{
            OoxmlFastUtil.setData(sheet,firstRow,12,"",null);//懸案無
        }

        //tenkenkiki部分
        for(int nIndex=0;nIndex<tenkenRirekiUtils.size();nIndex++){
            if(tenkenRirekiUtils.get(nIndex).getTenkenRank().equals("C")){
                OoxmlFastUtil.setData(sheet,firstRow,5,"○",null);//分解点検
            }else if(tenkenRirekiUtils.get(nIndex).getTenkenRank().equals("B")){
                OoxmlFastUtil.setData(sheet,firstRow,6,"○",null);//GP入替
            }
        }

        //罫線を描画
        OoxmlFastUtil.setRowDataCellStyle(workbook,sheet,nRowStart,nRowStart,0,13,false, style);
        //懸案未対応　水平方向設定
        OoxmlFastUtil.setSingleCellStyle(workbook,sheet,nRowStart,nRowStart+1,6,7, CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER);
        OoxmlFastUtil.setSingleCellStyle(workbook,sheet,nRowStart,nRowStart+1,12,13, CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER);
        
        int endRow=firstRow+1;


        return endRow;
    }

    //三つ数字のMax
    public static int max(int a,int b,int c){
        int d = Math.max(a,b);
        int result=Math.max(d,c);
        return  result;
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

        OoxmlFastUtil.setData(sheet, rowNum1, 0, kouji.getLocation(), null);//Kouji　location
        OoxmlFastUtil.setData(sheet, rowNum1, 2, kouji.getKjMeisyo(), null);//Kouji 工事名
        OoxmlFastUtil.setData(sheet, rowNum1, 10, "発令番号", null);//Kouji
        OoxmlFastUtil.setData(sheet, rowNum1, 11, kouji.getKjNo(), null);//Kouji 工事番号

        //セル結合
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,0,1,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,2,9,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,11,13,null);

        //第2行
        int rowNum2=1;
        int nCol=0;
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "弁番号", null);//弁番号
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "弁名称", null);//弁名称
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "弁形式", null);//弁形式
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "クラス", null);//クラス
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "呼び径", null);//呼び径
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "分解点検", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "GP入替", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "駆動部点検", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "付属品点検", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "作動試験調整", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "部品取替", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "機械加工の有無", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "懸案事項の有無", null);//
        OoxmlFastUtil.setData(sheet, rowNum2, nCol++, "備考", null);//




        //枠を描画
        OoxmlFastUtil.setRowDataCellStyle(wb,sheet,rowNum1,rowNum2,0,13,false,styleMap.get("normal"));

        //行高さ設定
//        for(int m=rowNum1;m<=rowNum2;m++){
//            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,m);
//            row.setHeightInPoints(26);
//        }

    }
}
