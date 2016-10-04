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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/10.
 */
public class PrintForKoujiTenkenList {

    /**
     *
     * 懸案リストをダウンロード
     * @param fileName DLファイル名
     *
     * @param kouji 工事
     *
     * @param tenkenRirekiMap 点検リスト
     *
     * @return 懸案リスト.xlsx
     *
     * */
    public static boolean downloadForKoujiTenkenList(String fileName,Kouji kouji, List<Valve> valveList,Map<String,List<TenkenRirekiUtil>> tenkenRirekiMap,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.KoujiTenkenListTemplate);
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
            List<TenkenRirekiUtil> tenkenRirekiUtils=tenkenRirekiMap.get(valve.getKikiSysId()+"");
            if(valve!=null){
                rowCount=setTenkenRireki(wb,sheet,rowCount,0,valve,tenkenRirekiUtils,kouji,styleMap);
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

    public static Integer setTenkenRireki(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,
                                    int nColumnStart,Valve valve,List<TenkenRirekiUtil> tenkenRirekiUtils,Kouji kouji,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        int firstRow=nRowStart;
        int ColCount=nColumnStart;
        XSSFCellStyle style = styleMap.get("normal");

        //Valve部分
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getvNo(),null);//弁番号
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getBenMeisyo(),null);//弁名称
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getSetBasyo(),null);//設置場所
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getKeisikiRyaku(),null);//弁形式
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getSousaRyaku(),null);//駆動方式
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getClassType(),null);//クラス
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getYobikeiRyaku(),null);//呼び径
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getAturyokuMax(),null);//圧力
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getOndoMax(),null);//温度
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getRyutaiRyaku(),null);//流体
        OoxmlFastUtil.setData(sheet,firstRow,ColCount++,valve.getZaisituRyaku(),null);//本体材質

        //kiki部分-弁本体
        Integer GPStartRow=firstRow;
        Integer GasketRow=firstRow;
        Integer GPStartCol=ColCount+3;
        Integer GaStartCol=ColCount+8;

        //kiki分類により、Sortする
        List<TenkenRirekiUtil> vksuListA = new LinkedList<TenkenRirekiUtil>();
        List<TenkenRirekiUtil> vksuListB = new LinkedList<TenkenRirekiUtil>();
        List<TenkenRirekiUtil> vksuListC = new LinkedList<TenkenRirekiUtil>();
        List<TenkenRirekiUtil> vksuListD = new LinkedList<TenkenRirekiUtil>();
        List<TenkenRirekiUtil> vksuListE = new LinkedList<TenkenRirekiUtil>();

        for(int i=0;i<tenkenRirekiUtils.size();i++){
            String kikibunrui=tenkenRirekiUtils.get(i).getKiki().getKikiBunrui();
            if(Config.KikiBunRuiA.equals(kikibunrui)){
                vksuListA.add(tenkenRirekiUtils.get(i));
            }else if(Config.KikiBunRuiB.equals(kikibunrui)){
                vksuListB.add(tenkenRirekiUtils.get(i));
            }else if(Config.KikiBunRuiC.equals(kikibunrui)){
                vksuListC.add(tenkenRirekiUtils.get(i));
            }else if(Config.KikiBunRuiD.equals(kikibunrui)){
                vksuListD.add(tenkenRirekiUtils.get(i));
            }else{ //４種類以外の場合
                vksuListE.add(tenkenRirekiUtils.get(i));
            }
        }
        //Listを結合する
        List<TenkenRirekiUtil> vksuListResult = new LinkedList<TenkenRirekiUtil>();
        vksuListResult.addAll(vksuListA);
        vksuListResult.addAll(vksuListB);
        vksuListResult.addAll(vksuListC);
        vksuListResult.addAll(vksuListD);
        vksuListResult.addAll(vksuListE);

        tenkenRirekiUtils=vksuListResult;

        for(int nIndex=0;nIndex<vksuListResult.size();nIndex++){
            Kiki tmpKiki=vksuListResult.get(nIndex).getKiki();
                System.out.println("弁本体="+tmpKiki.getKikiId()+"   ;"+tmpKiki.getKikiBunrui());
                OoxmlFastUtil.setData(sheet,firstRow,ColCount,tmpKiki.getMakerRyaku(),null);//メーカ
                OoxmlFastUtil.setData(sheet,firstRow,ColCount+1,tmpKiki.getKatasikiNo(),null);//型式番号
                OoxmlFastUtil.setData(sheet,firstRow++,ColCount+2,tenkenRirekiUtils.get(nIndex).getTenkennaiyo(),null);//点検内容

                // buhin部分
                List<Buhin> tmpBuhin=tenkenRirekiUtils.get(nIndex).getBuhin();
                for(int mIndex=0;mIndex<tmpBuhin.size();mIndex++){
                    String buhinburui=tmpBuhin.get(mIndex).getBuhinMei();
                    if(buhinburui.contains(Config.BuhinBunRuiA)){
                        OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol,tmpBuhin.get(mIndex).getHinban(),null);//品番
                        String sunpou=tmpBuhin.get(mIndex).getSunpou();
                        //×を半角に変更
//                        sunpou=sunpou.replace("ｘ","x");
//                        sunpou=sunpou.replace("×","x");
//                        sunpou=sunpou.replace("＊","x");
//                        sunpou=sunpou.replace("*","x");
//
//                        String[] vids = sunpou.split("x");
//                        for(int n=0;n<vids.length&&n<3;n++){
//                            OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+1+n,vids[n],null);//寸法
//                        }
                        //寸法を四つに分けた
                        OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+1,tmpBuhin.get(mIndex).getSunpouL(),null);//寸法L
                        OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+2,tmpBuhin.get(mIndex).getSunpouW(),null);//寸法W
                        OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+3,tmpBuhin.get(mIndex).getSunpouH(),null);//寸法H

                        OoxmlFastUtil.setData(sheet,GPStartRow++,GPStartCol+4,tmpBuhin.get(mIndex).getSuryo(),null);//数量
                    }else{
                        OoxmlFastUtil.setData(sheet,GasketRow,GaStartCol,tmpBuhin.get(mIndex).getHinban(),null);//品番
                        OoxmlFastUtil.setData(sheet,GasketRow,GaStartCol+1,tmpBuhin.get(mIndex).getSunpou(),null);//寸法
                        OoxmlFastUtil.setData(sheet,GasketRow++,GaStartCol+2,tmpBuhin.get(mIndex).getSuryo(),null);//数量
                    }
                }
        }

        //罫線を描画
        int endRow=max(firstRow,GPStartRow,GasketRow);
        OoxmlFastUtil.setRowDataCellStyle(workbook,sheet,nRowStart,endRow,0,21,false, style);
        //行高さ設定
        for(int m=nRowStart;m<endRow;m++){
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,m);
            row.setHeightInPoints(36);
        }

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
        OoxmlFastUtil.setData(sheet, rowNum1, 4, kouji.getKjMeisyo(), null);//Kouji 工事名
        OoxmlFastUtil.setData(sheet, rowNum1, 14, "発令番号", null);//Kouji
        OoxmlFastUtil.setData(sheet, rowNum1, 15, kouji.getKjNo(), null);//Kouji 工事番号
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,0,3,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,4,13,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum1,rowNum1,15,21,null);

        //第2行
        int rowNum2=1;
        OoxmlFastUtil.setData(sheet, rowNum2, 0, "弁番号", null);//弁番号
        OoxmlFastUtil.setData(sheet, rowNum2, 1, "弁名称", null);//弁名称
        OoxmlFastUtil.setData(sheet, rowNum2, 2, "設置場所", null);//設置場所
        OoxmlFastUtil.setData(sheet, rowNum2, 3, "弁形式", null);//弁形式
        OoxmlFastUtil.setData(sheet, rowNum2, 4, "駆動方式", null);//駆動方式
        OoxmlFastUtil.setData(sheet, rowNum2, 5, "クラス", null);//クラス
        OoxmlFastUtil.setData(sheet, rowNum2, 6, "呼び径", null);//呼び径
        OoxmlFastUtil.setData(sheet, rowNum2, 7, "圧力", null);//圧力
        OoxmlFastUtil.setData(sheet, rowNum2, 8, "温度", null);//温度
        OoxmlFastUtil.setData(sheet, rowNum2, 9, "流体", null);//流体
        OoxmlFastUtil.setData(sheet, rowNum2, 10, "本体材質", null);//本体材質
        OoxmlFastUtil.setData(sheet, rowNum2, 11, "メーカ", null);//メーカ
        OoxmlFastUtil.setData(sheet, rowNum2, 12, "型式番号", null);//型式番号
        OoxmlFastUtil.setData(sheet, rowNum2, 13, "点検内容", null);//点検内容
        OoxmlFastUtil.setData(sheet, rowNum2, 14, "グランドパッキン", null);//グランドパッキン
        OoxmlFastUtil.setData(sheet, rowNum2, 19, "ガスケット", null);//ガスケット

        //セル結合
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum2,14,18,null);
        OoxmlFastUtil.setMerger(wb,sheet,rowNum2,rowNum2,19,21,null);



        //縮小して全体を表示する
        int nCol=2;
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, nCol++);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, 14);
        OoxmlFastUtil.setShrinkToFitForCell(wb, sheet, rowNum2, 19);

        //枠を描画
        OoxmlFastUtil.setRowDataCellStyle(wb,sheet,rowNum1,rowNum2,0,21,false,styleMap.get("normal"));
        //行高さ設定
        for(int m=rowNum1;m<=rowNum2;m++){
            SXSSFRow row = OoxmlFastUtil.getRowAnyway(sheet,m);
            row.setHeightInPoints(26);
        }

    }
}
