package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlUtil;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.ValveForDL;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.xssf.usermodel.*;

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
public class PrintForKikisysDaityoList {

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
        XSSFWorkbook wb=new XSSFWorkbook(in1);
//        XSSFSheet sheet=wb.getSheet("data");
        XSSFSheet sheet=wb.getSheetAt(0);
        //footer設定
        Footer footer = sheet.getFooter();
        footer.setLeft(companyLocation);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");


        /*データ設定*/
        //タイトル設定　点検履歴の定
        //点検履歴　　開始列　tenkenCol
        int tenkenCol=10;
        for(int i=tenYearBefore;i<(tenYearBefore+11);i++){
            OoxmlUtil.setData(sheet,3,tenkenCol,String.format("%1$02d", i),style);//点検履歴の定
            OoxmlUtil.setData(sheet,4,tenkenCol,tenkentei[i],style);//点検履歴の定
            OoxmlUtil.setData(sheet,5,tenkenCol++,tenkenOther[i],style);//点検履歴のその他
        }
        int rowCount=6;
        for(int nIndex=0;nIndex<valveForDLList.size();nIndex++){
            ValveForDL valveForDL=valveForDLList.get(nIndex);
            //データを書き込む
            rowCount=setDataToExcel(wb,sheet,rowCount,valveForDL,style,styleMap,tenkenrireki,tenYearBefore);
        }

        //改ページ設定　
        // １ページに最大54行(4行のタイトルを含む)を出力する
        for(int rIndex=46;rIndex<rowCount;rIndex=rIndex+40){
            XSSFRow row = OoxmlUtil.getRowAnyway(sheet, rIndex);
            XSSFCell cell=OoxmlUtil.getCellAnyway(row,0);

            int spitPage=OoxmlUtil.getRowForBold(sheet,rIndex,20);
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
    public static Integer setDataToExcel(XSSFWorkbook wb,XSSFSheet sheet,int rowCount,ValveForDL valveForDL,XSSFCellStyle style,Map<String,XSSFCellStyle> styleMap,Map<String,String[]> tenkenrireki,Integer tenYearBefore) {
        assert sheet != null;

        /**弁基本情報*/
        //１行目
        OoxmlUtil.setData(sheet,rowCount,0,valveForDL.getValve().getvNo(),null);//弁番号
        OoxmlUtil.setData(sheet,rowCount,1,valveForDL.getValve().getvNoSub(),null);//識番
        OoxmlUtil.setData(sheet,rowCount,2,valveForDL.getValve().getSetBasyo(),null);//設定場所
        OoxmlUtil.setData(sheet,rowCount,3,valveForDL.getValve().getKeisikiRyaku(),null);//弁形式
        OoxmlUtil.setData(sheet,rowCount,4,valveForDL.getValve().getAturyokuMax(),null);//圧力
        OoxmlUtil.setData(sheet,rowCount,5,valveForDL.getValve().getSzHouRyaku(),null);//接続入口
        OoxmlUtil.setData(sheet,rowCount,6,valveForDL.getValve().getFutai(),null);//付帯
        //2行目
        OoxmlUtil.setData(sheet,rowCount+1,0,valveForDL.getValve().getBenMeisyo(),null);//弁名称
        OoxmlUtil.setData(sheet,rowCount+1,3,valveForDL.getValve().getSousaRyaku(),null);//駆動方式
        OoxmlUtil.setData(sheet,rowCount+1,4,valveForDL.getValve().getTani(),null);//圧力単位
        OoxmlUtil.setData(sheet,rowCount+1,5,valveForDL.getValve().getZaisituRyaku(),null);//本体材質
        //3行目
        OoxmlUtil.setData(sheet,rowCount+2,2,valveForDL.getValve().getKeitou(),null);//系統
        OoxmlUtil.setData(sheet,rowCount+2,3,valveForDL.getValve().getClassType(),null);//クラス
        OoxmlUtil.setData(sheet,rowCount+2,4,valveForDL.getValve().getOndoMax(),null);//温度(℃)
        //4行目
        OoxmlUtil.setData(sheet,rowCount+3,3,valveForDL.getValve().getYobikeiRyaku(),null);//呼び径
        OoxmlUtil.setData(sheet,rowCount+3,4,valveForDL.getValve().getRyutaiRyaku(),null);//流体
        int tmp=rowCount+5;
        int nRowStart=rowCount;

        //弁結合
        setKikisysMerger(wb, sheet, nRowStart, nRowStart+4, 0, 25, styleMap);

        //kiki
        java.util.List<Kiki> kikiList=valveForDL.getKikiList();
        if(kikiList.size()>0){
            for(Kiki kiki:kikiList){
                if(Config.BuhinBunRuiA.equals(kiki.getKikiBunrui())||Config.BuhinBunRuiB.equals(kiki.getKikiBunrui())||"A".equals(kiki.getKikiBunrui())||"B".equals(kiki.getKikiBunrui())){
                    OoxmlUtil.setData(sheet,rowCount,6,kiki.getKikiMei(),null);//機器名称
                    OoxmlUtil.setData(sheet,rowCount,7,kiki.getMakerRyaku(),null);//メーカー
                    OoxmlUtil.setData(sheet,rowCount,8,kiki.getKatasikiNo(),null);//型式番号
                    //点検履歴
                    String[] tenkenteiRank=tenkenrireki.get(kiki.getKikiId()+""); //点検履歴
                    int tenkenCol=10;//点検履歴 開始列
                    if(tenkenteiRank!=null){
                        for(int i=tenYearBefore;i<(tenYearBefore+11);i++){
                            OoxmlUtil.setData(sheet,rowCount,tenkenCol++,tenkenteiRank[i],null);//点検履歴
                        }
                    }
                    //最終評価
                    OoxmlUtil.setData(sheet,rowCount,24,tenkenteiRank[1],null);//最終評価
                    OoxmlUtil.setData(sheet,rowCount,23,tenkenteiRank[2],null);//懸案
                    //非石綿化---------
                    //パッキン
                    if(tenkenteiRank[3].contains(Config.AsbkbnA)){
                        OoxmlUtil.setData(sheet,rowCount,21,Config.AsbkbnA,null);//パッキンA
                    }
                    if(tenkenteiRank[3].contains(Config.AsbkbnN)){
                        OoxmlUtil.setData(sheet,rowCount+1,21,Config.AsbkbnN,null);//パッキンN
                    }
                    //ガスケット
                    if(tenkenteiRank[4].contains(Config.AsbkbnA)){
                        OoxmlUtil.setData(sheet,rowCount,22,Config.AsbkbnA,null);//ガスケットA
                    }
                    if(tenkenteiRank[4].contains(Config.AsbkbnN)){
                        OoxmlUtil.setData(sheet,rowCount+1,22,Config.AsbkbnN,null);//ガスケットN
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
            //kiki結合
            setKikiMerger(wb, sheet, i, i+1, 6, 6, styleMap);//機器名称
            i=i+1;
        }
        //罫線を描画
        OoxmlUtil.setTableDataCellStyle(wb,sheet,nRowStart,rowCount,0,25,true, styleMap);
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

    public static void setKikisysMerger(XSSFWorkbook workbook,XSSFSheet sheet, int nRowStart,int nRowEnd,
                                    int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlUtil.setMerger(workbook,sheet,nRowStart+1,nRowStart+3,nColumnStart,nColumnStart+1,null);//弁名称結合
        OoxmlUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+2,nColumnStart+2,null);//弁設置場所結合
        OoxmlUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,nColumnStart+2,nColumnStart+2,null);//弁系統結合
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

    public static void setKikiMerger(XSSFWorkbook workbook,XSSFSheet sheet, int nRowStart,int nRowEnd,
                                        int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart,nColumnStart,null);//機器名称
        OoxmlUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+1,nColumnStart+1,null);//メーカ
        OoxmlUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+2,nColumnStart+2,null);//型式番号
        for(int i=3;i<15;i++){
            OoxmlUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+i,nColumnStart+i,null);//点検履歴
        }
        for(int i=17;i<20;i++){
            OoxmlUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+i,nColumnStart+i,null);//点検履歴
        }
    }
}
