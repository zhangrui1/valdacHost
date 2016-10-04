package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlUtil;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.Kouji;
import com.toyo.vh.entity.ValveForDL;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by zhangrui on 2015/04/10.
 */
public class PrintForKikisysSijisyo {

    public static boolean downloadForKikisysSijisyoList(java.util.List<ValveForDL> valveForDLList,Kouji kouji,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(Config.ValveSijisyo, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.ValveSijisyoTemplate);
        XSSFWorkbook wb=new XSSFWorkbook(in1);
        XSSFSheet sheet=wb.getSheetAt(0);
        int nSheet = 1;
        /*データ設定*/
        for(int nIndex=0;nIndex<valveForDLList.size();nIndex++){
            ValveForDL valveForDL=valveForDLList.get(nIndex);

            //sheet copy
            XSSFSheet tmpSheet = OoxmlUtil.cloneSheet(wb,1,"data",(nIndex+1)+valveForDL.getValve().getvNo());

            /**工事情報*/
            OoxmlUtil.setData(tmpSheet,3,21, kouji.getKjMeisyo(), null);//工事名
            OoxmlUtil.setData(tmpSheet,9,24, kouji.getSyukan(), null);//工事主管係

            /**弁基本情報*/
            OoxmlUtil.setData(tmpSheet,6,3, valveForDL.getValve().getvNo(), null);//弁番号
            OoxmlUtil.setData(tmpSheet,6,12,valveForDL.getValve().getBenMeisyo(),null);//弁名称

            OoxmlUtil.setData(tmpSheet,11,3,valveForDL.getValve().getYobikeiRyaku(),null);//呼び径
            OoxmlUtil.setData(tmpSheet,13,3,valveForDL.getValve().getClassType(),null);//クラス
            OoxmlUtil.setData(tmpSheet,14,3,valveForDL.getValve().getKeisikiRyaku(),null);//型式
            OoxmlUtil.setData(tmpSheet,15,3,valveForDL.getValve().getSousaRyaku(),null);//駆動方式
            OoxmlUtil.setData(tmpSheet,16,3,valveForDL.getValve().getZaisituRyaku(),null);//本体材質

            OoxmlUtil.setData(tmpSheet,10,10,valveForDL.getValve().getRyutaiRyaku(),null);//流体

            OoxmlUtil.setData(tmpSheet,10,14,valveForDL.getValve().getAturyokuMax(),null);//圧力
            OoxmlUtil.setData(tmpSheet,12,14,valveForDL.getValve().getTani(),null);//圧力単位

            OoxmlUtil.setData(tmpSheet,10,18,valveForDL.getValve().getOndoMax(),null);//温度(℃)

            //location
            String location=valveForDL.getValve().getLocationName();
            String location_part="";
            String[] vids = location.split(" ");
            if(vids.length>1){
                location_part=vids[1].replace("発電所","");
            }
            OoxmlUtil.setData(tmpSheet,3,3,valveForDL.getValve().getLocationName()+"  様",null);//Location
            OoxmlUtil.setData(tmpSheet,11,24,location_part,null);//Location　発電所

            //kiki
            java.util.List<Kiki> kikiList=valveForDL.getKikiList();
            if(kikiList.size()>0){
                for(Kiki kiki:kikiList){
                    if(Config.KikiBunRuiA.equals(kiki.getKikiBunrui())||"A".equals(kiki.getKikiBunrui())){
                        OoxmlUtil.setData(tmpSheet,9,3,kiki.getMakerRyaku(),null);//メーカー
                        break;
                    }
                }
            }
        }

        //templateシートを削除
        OoxmlUtil.removeSheet(wb, "data");
        //印刷設定
        int sheetNum=wb.getNumberOfSheets();
        for(int nIndex=0;nIndex<sheetNum;nIndex++){
            XSSFSheet tmpSheet=wb.getSheetAt(nIndex);
            PrintSetup ps=tmpSheet.getPrintSetup();
            ps.setFitHeight((short)1);//ページ設定の拡大縮小印刷で縦が１ページに収まるようにに設定する
            ps.setFitWidth((short)1);//ページ設定の拡大縮小印刷で横が１ページに収まるように設定する

            wb.setPrintArea(nIndex,"$A$1:$AQ$63");
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
  }
