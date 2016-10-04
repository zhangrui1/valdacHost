package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.ValveForDL;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.xssf.streaming.SXSSFCell;
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
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/10.
 */
public class PrintForKikisysDaityoFastList {

    /**
     *
     * 弁単位でデータを出力
     *
     * @param valveForDLList
     *            弁リスト
     * @param tenkentei
     *            工事種別　定期点検
     * @param  tenkenOther
     *             工事種別　その他
     * @param  tenkenrireki
     *             点検履歴
     * @param  tenYearBefore
     *             開始年度
     * @param  companyLocation
     *             会社名
     *@return 　Excelファイル
     * 　　　　　
     *
     */
    public static boolean downloadForKikisysDaityoList(java.util.List<ValveForDL> valveForDLList,String tenkentei[],String tenkenOther[],Map<String,String[]> tenkenrireki,String companyLocation,Integer tenYearBefore,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(Config.ValveDaityo, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.ValveDaityoTemplate);
        SXSSFWorkbook wb = new SXSSFWorkbook(new XSSFWorkbook(in1),60000);
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //footer設定
        Footer footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.fontSize((short) 12)+companyLocation);


        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");


        /*データ設定*/
        //タイトル設定　点検履歴の定
        int rowCount=3;
        setDataToExcelTitle(wb,sheet,rowCount,styleMap,tenkentei,tenkenOther,tenYearBefore);

        rowCount=6;
        for(int nIndex=0;nIndex<valveForDLList.size();nIndex++){
            ValveForDL valveForDL=valveForDLList.get(nIndex);
            //データを書き込む
            rowCount=setDataToExcel(wb,sheet,rowCount,valveForDL,style,styleMap,tenkenrireki,tenYearBefore);
        }

        //ページタイトル設定
//        sheet.setRepeatingRowsAndColumns(0,-1,-1,0,5);
//        sheet.setRepeatingRows();
        //改ページ設定　
        // １ページに最大54行(4行のタイトルを含む)を出力する
        for(int rIndex=46;rIndex<rowCount;rIndex=rIndex+40){
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet, rIndex);
            SXSSFCell cell=OoxmlFastUtil.getCellAnyway(row,0);

            int spitPage=OoxmlFastUtil.getRowForBold(sheet,rIndex,20);
            if(spitPage>1){
                sheet.setRowBreak(spitPage-1);
            }
            rIndex=spitPage;
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
     * @param  rowCount
     *             開始行
     * @param  tenkentei
     *             点検 定
     * @param  tenkenOther
     *             点検　その他
     * @param  tenYearBefore
     *             開始年度
     *@return 　rowCount
     * 　　　　　終了行
     *
     */
    public static void setDataToExcelTitle(SXSSFWorkbook wb,SXSSFSheet sheet,int rowCount,Map<String,XSSFCellStyle> styleMap,String tenkentei[],String tenkenOther[],Integer tenYearBefore) {
        assert sheet != null;

        //3行
        int nCol=0;
        int nRow=2;
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"弁番号",null);//弁番号
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"識番",null);//識番
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"設置場所",null);//設置場所
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"弁形式",null);//弁形式
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"圧力",null);//圧力
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"接続入口",null);//接続入口
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"機器名称",null);//機器名称
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"メーカー",null);//メーカー
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"型式番号",null);//型式番号
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"標準周期",null);//標準周期
        OoxmlFastUtil.setData(sheet,nRow,nCol++,"点検履歴",null);//点検履歴
        OoxmlFastUtil.setData(sheet,nRow,21,"非石綿化",null);//非石綿化
        OoxmlFastUtil.setData(sheet,nRow,23,"未対応懸案",null);//未対応懸案
        OoxmlFastUtil.setData(sheet,nRow,24,"最終評価",null);//最終評価

        //4行
        nCol=0;
        nRow=3;
        OoxmlFastUtil.setData(sheet,nRow,0,"弁名称",null);//弁名称
        OoxmlFastUtil.setData(sheet,nRow,3,"駆動方式",null);//駆動方式
        OoxmlFastUtil.setData(sheet,nRow,4,"圧力単位",null);//圧力単位
        OoxmlFastUtil.setData(sheet,nRow,5,"本体材質",null);//本体材質
//        OoxmlFastUtil.setData(sheet,nRow,21,"石綿",null);//非石綿化

        //5行
        nCol=0;
        nRow=4;
        OoxmlFastUtil.setData(sheet,nRow,2,"系統",null);//系統
        OoxmlFastUtil.setData(sheet,nRow,3,"クラス",null);//クラス
        OoxmlFastUtil.setData(sheet,nRow,4,"温度(℃)",null);//温度(℃)
        OoxmlFastUtil.setData(sheet,nRow,9,"A.  B.  C",null);//A.  B.  C
//        OoxmlFastUtil.setData(sheet,nRow,21,"化",null);//非石綿化

        //6行
        nCol=0;
        nRow=5;
        OoxmlFastUtil.setData(sheet,nRow,3,"呼び径",null);//呼び径
        OoxmlFastUtil.setData(sheet,nRow,4,"流体",null);//流体
        OoxmlFastUtil.setData(sheet,nRow,21,"グ",null);//グ
        OoxmlFastUtil.setData(sheet,nRow,22,"ガ",null);//ガ


        //点検履歴　　開始列　tenkenCol
        int tenkenCol=10;
        for(int i=tenYearBefore;i<(tenYearBefore+11);i++){
            OoxmlFastUtil.setData(sheet,3,tenkenCol, String.format("%1$02d", i), null);//点検履歴の定
            OoxmlFastUtil.setData(sheet,4,tenkenCol,tenkentei[i],null);//点検履歴の定
            OoxmlFastUtil.setData(sheet,5,tenkenCol++,tenkenOther[i],null);//点検履歴のその他
        }

        //結合
        OoxmlFastUtil.setMerger(wb,sheet,3,5,0,1,null);//弁名称
        OoxmlFastUtil.setMerger(wb,sheet,2,3,2,2,null);//設置場所
        OoxmlFastUtil.setMerger(wb,sheet,4,5,2,2,null);//系統
        OoxmlFastUtil.setMerger(wb,sheet,2,5,6,6,null);//機器名称
        OoxmlFastUtil.setMerger(wb,sheet,2,5,7,7,null);//メーカー
        OoxmlFastUtil.setMerger(wb,sheet,2,5,8,8,null);//型式番号
        OoxmlFastUtil.setMerger(wb,sheet,2,3,9,9,null);//標準周期
        OoxmlFastUtil.setMerger(wb,sheet,2,2,10,20,null);//点検履歴
        OoxmlFastUtil.setMerger(wb,sheet,2,4,21,22,null);//非石綿化
        OoxmlFastUtil.setMerger(wb,sheet,2,5,23,23,null);//未対応懸案
        OoxmlFastUtil.setMerger(wb,sheet,2,5,24,24,null);//最終評価
        OoxmlFastUtil.setMerger(wb,sheet,2,5,25,25,null);//最終評価
        //罫線を描画
        OoxmlFastUtil.setTableDataCellStyle(wb,sheet,2,5,0,25,true, styleMap);
        //縮小して全体を表示する
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, 2, 1);
        OoxmlFastUtil.setShrinkToFitForCell(wb,sheet,2,7);
        //行高を設定
        for(int i=2;i<6;i++){
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,i);
            row.setHeightInPoints(15);
        }
        //点線
        OoxmlFastUtil.setSingleCellStyle(wb,sheet,2,6,6,25,CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER);//圧力単位
        OoxmlFastUtil.setSingleCellStyle(wb,sheet,3,4,4,5,CellStyle.ALIGN_RIGHT,CellStyle.VERTICAL_CENTER);//その他
    }
    /**
     *
     * 弁単位でデータを出力
     *
     * @param wb
     *            ワークブック
     * @param sheet
     *            シート
     * @param  rowCount
     *             開始行
     * @param  valveForDL
     *             弁データ
     * @param  style
     *             normal
     * @param  styleMap
     *             すべてのstyle
     * @param  tenkenrireki
     *             点検履歴
     * @param  tenYearBefore
     *             開始年度
     *@return 　rowCount
     * 　　　　　終了行
     *
     */
    public static Integer setDataToExcel(SXSSFWorkbook wb,SXSSFSheet sheet,int rowCount,ValveForDL valveForDL,XSSFCellStyle style,Map<String,XSSFCellStyle> styleMap,Map<String,String[]> tenkenrireki,Integer tenYearBefore) {
        assert sheet != null;

        /**弁基本情報*/
        //１行目
        OoxmlFastUtil.setData(sheet,rowCount,0,valveForDL.getValve().getvNo(),null);//弁番号
        OoxmlFastUtil.setData(sheet,rowCount,1, StringUtil.nullCheck(valveForDL.getValve().getvNoSub()),null);//識番
        OoxmlFastUtil.setData(sheet,rowCount,2,valveForDL.getValve().getSetBasyo(),null);//設定場所
        OoxmlFastUtil.setData(sheet,rowCount,3,valveForDL.getValve().getKeisikiRyaku(),null);//弁形式
        OoxmlFastUtil.setData(sheet,rowCount,4,valveForDL.getValve().getAturyokuMax(),null);//圧力
        OoxmlFastUtil.setData(sheet,rowCount,5,valveForDL.getValve().getSzHouRyaku(),null);//接続入口
//        OoxmlFastUtil.setData(sheet,rowCount,6,valveForDL.getValve().getFutai(),null);//付帯
        //2行目
        OoxmlFastUtil.setData(sheet,rowCount+1,0,valveForDL.getValve().getBenMeisyo(),null);//弁名称
        OoxmlFastUtil.setData(sheet,rowCount+1,3,valveForDL.getValve().getSousaRyaku(),null);//駆動方式
        OoxmlFastUtil.setData(sheet,rowCount+1,4,valveForDL.getValve().getTani(),null);//圧力単位
        OoxmlFastUtil.setData(sheet,rowCount+1,5,valveForDL.getValve().getZaisituRyaku(),null);//本体材質
        //3行目
        OoxmlFastUtil.setData(sheet,rowCount+2,2,valveForDL.getValve().getKeitou(),null);//系統
        OoxmlFastUtil.setData(sheet,rowCount+2,3,valveForDL.getValve().getClassType(),null);//クラス
        OoxmlFastUtil.setData(sheet,rowCount+2,4,valveForDL.getValve().getOndoMax(),null);//温度(℃)
        //4行目
        OoxmlFastUtil.setData(sheet,rowCount+3,3,valveForDL.getValve().getYobikeiRyaku(),null);//呼び径
        OoxmlFastUtil.setData(sheet,rowCount+3,4,StringUtil.nullCheck(valveForDL.getValve().getRyutaiRyaku()),null);//流体
        //
//        System.out.println("result"+valveForDL.getValve().getKenanFlg().equals("1"));
        if(valveForDL.getValve().getKenanFlg().equals("1")){
            OoxmlFastUtil.setData(sheet,rowCount,23,"○",null);//懸案
        }
        int tmp=rowCount+4;
        int nRowStart=rowCount;

        //弁結合
        setKikisysMerger(wb, sheet, nRowStart, nRowStart+3, 0, 25, styleMap);

        //kiki
        java.util.List<Kiki> kikiList=valveForDL.getKikiList();
        if(kikiList.size()>0){
            for(Kiki kiki:kikiList){
                if(Config.KikiBunRuiA.equals(kiki.getKikiBunrui())||Config.KikiBunRuiB.equals(kiki.getKikiBunrui())||"A".equals(kiki.getKikiBunrui())||"B".equals(kiki.getKikiBunrui())){
                    OoxmlFastUtil.setData(sheet,rowCount,6,kiki.getKikiMei(),null);//機器名称
                    OoxmlFastUtil.setData(sheet,rowCount,7,kiki.getMakerRyaku(),null);//メーカー
                    OoxmlFastUtil.setData(sheet,rowCount,8,kiki.getKatasikiNo(),null);//型式番号
                    //点検履歴
                    String[] tenkenteiRank=tenkenrireki.get(kiki.getKikiId()+""); //点検履歴
                    int tenkenCol=10;//点検履歴 開始列
                    if(tenkenteiRank!=null){
                        for(int i=tenYearBefore;i<(tenYearBefore+11);i++){
                            OoxmlFastUtil.setData(sheet,rowCount,tenkenCol++,tenkenteiRank[i],null);//点検履歴
                        }
                    }
                    //最終評価
                    OoxmlFastUtil.setData(sheet,rowCount,24,tenkenteiRank[1],null);//最終評価
                    //非石綿化---------
                    //パッキン
                    if(tenkenteiRank[3].contains(Config.AsbkbnA)){
                        OoxmlFastUtil.setData(sheet,rowCount,21,Config.AsbkbnA,null);//パッキンA
                    }
                    if(tenkenteiRank[3].contains(Config.AsbkbnN)){
                        OoxmlFastUtil.setData(sheet,rowCount+1,21,Config.AsbkbnN,null);//パッキンN
                    }
                    //ガスケット
                    if(tenkenteiRank[4].contains(Config.AsbkbnA)){
                        OoxmlFastUtil.setData(sheet,rowCount,22,Config.AsbkbnA,null);//ガスケットA
                    }
                    if(tenkenteiRank[4].contains(Config.AsbkbnN)){
                        OoxmlFastUtil.setData(sheet,rowCount+1,22,Config.AsbkbnN,null);//ガスケットN
                    }
                    //kiki結合
//                        setKikiMerger(wb, sheet, rowCount, rowCount+1, 6, 6, styleMap);//機器名称
                    rowCount=rowCount+2;
                }
            }
        }
        if(rowCount<=tmp){
            rowCount=tmp;
        }

        //空の機器行でも結合する
        for(int i=nRowStart;i<rowCount-1;i++){
            //機器名称から　空の行は全部Merge
            boolean nullflg=true;//空行フラグ
                SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet, i);
                for(int cIndex=6;cIndex<25;cIndex=cIndex+1){
                    SXSSFCell cell=OoxmlFastUtil.getCellAnyway(row,cIndex);
                    if(cell.getStringCellValue().length()>1){
                        nullflg=false;
                    }
                }
            if(nullflg){
                OoxmlFastUtil.setMerger(wb,sheet,i,i+1,6,25,null);//弁名称結合
            }else{
                //kiki結合
                setKikiMerger(wb, sheet, i, i+1, 6, 6, styleMap);//機器名称
            }
            i=i+1;
        }
        //罫線を描画
        if(rowCount-nRowStart>4){
            //kiki 二つ以上ある場合、弁基本情報部分は結合する
            OoxmlFastUtil.setMerger(wb,sheet,nRowStart+4,rowCount-1,0,5,null);
        }

        OoxmlFastUtil.setTableDataCellStyle(wb,sheet,nRowStart,rowCount-1,0,25,true, styleMap);
        //行高を設定
        for(int i=nRowStart;i<rowCount-1;i++){
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,i);
            row.setHeightInPoints(13);
        }
        //点線
//        OoxmlFastUtil.setSingleCellStyle(wb,sheet,nRowStart,nRowStart+4,11,25,CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_CENTER);//その以外
        OoxmlFastUtil.setSingleCellStyle(wb,sheet,nRowStart+1,nRowStart+2,4,5,CellStyle.ALIGN_RIGHT,CellStyle.VERTICAL_CENTER);//圧力単位
        return rowCount;
    }
    /**
     *
     * 弁セルなどの結合
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

    public static void setKikisysMerger(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                    int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+1,nRowStart+3,nColumnStart,nColumnStart+1,null);//弁名称結合
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+2,nColumnStart+2,null);//弁設置場所結合
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,nColumnStart+2,nColumnStart+2,null);//弁系統結合
    }
    /**
     *
     * 機器セルなどの結合
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

    public static void setKikiMerger(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                        int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart,nColumnStart,null);//機器名称
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+1,nColumnStart+1,null);//メーカ
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+2,nColumnStart+2,null);//型式番号
        for(int i=3;i<15;i++){
            OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+i,nColumnStart+i,null);//点検履歴
        }
        for(int i=17;i<20;i++){
            OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+i,nColumnStart+i,null);//点検履歴
        }
    }


}
