package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.entity.Buhin;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.ValveForDL;
import org.apache.poi.hssf.usermodel.HSSFHeader;
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
public class PrintForKikisysDetailList {

    public static boolean downloadForKikisysDetailList(java.util.List<ValveForDL> valveForDLList,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(Config.ValveDetail, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.ValveDetailTemplate);
        SXSSFWorkbook wb=new SXSSFWorkbook(new XSSFWorkbook(in1),600000);
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");

        //header
        String companyLocation="";
        if(valveForDLList!=null){
            ValveForDL valveForDL=valveForDLList.get(0);
            companyLocation=valveForDL.getValve().getLocationName();
        }
        Footer footer = sheet.getFooter();
        footer.setLeft(HSSFHeader.fontSize((short) 12)+companyLocation);

        /*データ設定*/
        int rowCount=4;
        for(int nIndex=0;nIndex<valveForDLList.size();nIndex++){
            ValveForDL valveForDL=valveForDLList.get(nIndex);
            /**弁基本情報*/
            //１行目
            OoxmlFastUtil.setData(sheet,rowCount,0,valveForDL.getValve().getvNo(),null);//弁番号
            OoxmlFastUtil.setData(sheet,rowCount,1,valveForDL.getValve().getvNoSub(),null);//識番
            OoxmlFastUtil.setData(sheet,rowCount,2,valveForDL.getValve().getSetBasyo(),null);//設定場所
            OoxmlFastUtil.setData(sheet,rowCount,3,valveForDL.getValve().getKeisikiRyaku(),null);//弁形式
            OoxmlFastUtil.setData(sheet,rowCount,4,valveForDL.getValve().getAturyokuMax(),null);//圧力
            OoxmlFastUtil.setData(sheet,rowCount,5,valveForDL.getValve().getSzHouRyaku(),null);//接続入口
            OoxmlFastUtil.setData(sheet,rowCount,6,valveForDL.getValve().getFutai(),null);//付帯
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
            OoxmlFastUtil.setData(sheet,rowCount+3,4,valveForDL.getValve().getRyutaiRyaku(),null);//流体
            int tmp=rowCount+5;
            int nRowStart=rowCount;

            //弁結合
            setKikisysMerger(wb, sheet, nRowStart, nRowStart+4, 0, 15, styleMap);

            //kiki
            java.util.List<Kiki> kikiList=valveForDL.getKikiList();
            Map<Kiki, java.util.List<Buhin>> buhinList=valveForDL.getBuhinList();
            if(kikiList.size()>0){
                for(Kiki kiki:kikiList){
                    //削除された機器はDLしない  2016/05/11
                    if("0".equals(kiki.getKikiDelFlg())){
                        OoxmlFastUtil.setData(sheet,rowCount,7,kiki.getKikiMei(),null);//機器名称
                        OoxmlFastUtil.setData(sheet,rowCount,8,kiki.getMakerRyaku(),null);//メーカー
                        OoxmlFastUtil.setData(sheet,rowCount,9,kiki.getKatasikiNo(),null);//型式番号
                        OoxmlFastUtil.setData(sheet,rowCount+1,9,kiki.getSerialNo(),null);//シリアル番号
                        OoxmlFastUtil.setData(sheet,rowCount,10,kiki.getBikou(),null);//備考

                        int buhinStart=rowCount;
                        java.util.List<Buhin> buhins=buhinList.get(kiki);
                        if(buhins.size()>0){
                            for(Buhin buhin:buhins){
                                OoxmlFastUtil.setData(sheet,rowCount,11,buhin.getSiyouKasyo(),null);//使用箇所
                                OoxmlFastUtil.setData(sheet,rowCount,12,buhin.getBuhinMei(),null);//部品名
                                OoxmlFastUtil.setData(sheet,rowCount,13,buhin.getHinban(),null);//品番
                                OoxmlFastUtil.setData(sheet,rowCount,14,buhin.getSunpou(),null);//概略寸法
                                OoxmlFastUtil.setData(sheet,rowCount,15,buhin.getSuryo(),null);//数量
                                rowCount=rowCount+1;
                            }
                        }else{
                            rowCount=rowCount+1;
                        }
                        //kiki接合
                        if((rowCount-1)==buhinStart){//機器の部品は0 or 1の場合は
                            setAllKikiMerger(wb, sheet, buhinStart, rowCount, 7, 10, styleMap);
                            rowCount=rowCount+1;
                        }else{//機器の部品は>=2の場合は
                            setAllKikiMerger(wb, sheet, buhinStart, rowCount - 1, 7, 10, styleMap);
                        }


                    }
                    }

            }else{
                rowCount=rowCount+1;
            }

            rowCount=rowCount+1;

            if(rowCount<=tmp){
                rowCount=tmp;
            }
            //罫線を描画
            OoxmlFastUtil.setTableDataCellStyle(wb,sheet,nRowStart,rowCount,0,15,true, styleMap);
        }
        //縮小して全体を表示する
        if(rowCount<300000){
            for(int rIndex=4;rIndex<rowCount;rIndex++){
                //セルごとに設定するD,E,F列
                OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rIndex, 3);
                for(int cIndex=3;cIndex<6;cIndex++){
                    OoxmlFastUtil.setShrinkToFitForCell(wb,sheet,rIndex,cIndex);
                }
//            //セルごとに設定するL-O列
                for(int cIndex=11;cIndex<16;cIndex++){
                    OoxmlFastUtil.setShrinkToFitForCell(wb,sheet,rIndex,cIndex);
                }
                System.out.println("rIndex=  "+rIndex);
            }
            //改ページ設定　
            // １ページに最大54行(4行のタイトルを含む)を出力する
            for(int rIndex=54;rIndex<rowCount;rIndex=rIndex+50){
                SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet, rIndex);
                SXSSFCell cell=OoxmlFastUtil.getCellAnyway(row,0);

                int spitPage=OoxmlFastUtil.getRowForBold(sheet,rIndex,40);
                if(spitPage>1){
                    sheet.setRowBreak(spitPage-1);
                }
                rIndex=spitPage;
                System.out.println("spitPage=  "+spitPage);
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
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+1,nRowStart+3,nColumnStart,nColumnStart+1,null);
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,nColumnStart+2,nColumnStart+2,null);
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,nColumnStart+2,nColumnStart+2,null);
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowEnd-1,6,6,null);

    }
    /**
     *
     * 機器セルなどの結合
     */
    public static void setAllKikiMerger(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                        int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowEnd,nColumnStart,nColumnStart,null);
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowEnd,nColumnStart+1,nColumnStart+1,null);
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+1,nRowEnd,nColumnStart+2,nColumnStart+2,null);
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowEnd,nColumnStart+3,nColumnStart+3,null);

    }
}
