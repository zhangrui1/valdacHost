package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlUtil;
import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.Kouji;
import com.toyo.vh.entity.ValveForDL;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/10.
 */
public class PrintForKikisysKanriDaiTyo {

    public static boolean downloadForKikisysKanriDaiTyoList(java.util.List<ValveForDL> valveForDLList,Kouji kouji,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String fileName=Config.KoujiKanriDaiTyoList;
        if(StringUtil.isNotEmpty(kouji.getKjMeisyo())){
            fileName=kouji.getKjMeisyo()+".xlsm";
        }
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.KoujiKanriDaiTyoListTemplate);
        XSSFWorkbook wb=new XSSFWorkbook(in1);
        XSSFSheet sheet=wb.getSheetAt(0);
        int nSheet = 1;

        int rowActive=1;
        //location
        String location=kouji.getLocation();
        String location_part="";
        String location_part2="";
        String[] vids = location.split(" ");
        if(vids.length>1){
            location_part=vids[1].replace("発電所","");
        }
        if(vids.length>2){
            location_part2=vids[2].replace("号機", "");
        }

        /*データ設定*/
        for(int nIndex=0;nIndex<valveForDLList.size();nIndex++){
            ValveForDL valveForDL=valveForDLList.get(nIndex);

            OoxmlUtil.setData(sheet,rowActive,0, rowActive, null);//番号
            OoxmlUtil.setData(sheet,rowActive,3,location,null);//Location　表示用
            OoxmlUtil.setData(sheet,rowActive,4,location_part2,null);//Location　号機

            OoxmlUtil.setData(sheet,rowActive,5, valveForDL.getValve().getvNo(), null);//弁番号
            OoxmlUtil.setData(sheet,rowActive,6, valveForDL.getValve().getBenMeisyo(), null);//弁名称

            /**工事情報　指示書作成ため用*/
            int colNum=21;
            OoxmlUtil.setData(sheet,rowActive,colNum++, kouji.getKjMeisyo(), null);//工事名
            OoxmlUtil.setData(sheet, rowActive, colNum++, kouji.getSyukan(), null);//工事主管係
            /**弁基本情報 指示書作成ため用*/
            OoxmlUtil.setData(sheet,rowActive,colNum++,location+"  様",null);//Location 指示書用
            OoxmlUtil.setData(sheet,rowActive,colNum++,location_part,null);//Location　発電所

            OoxmlUtil.setData(sheet,rowActive,colNum++, valveForDL.getValve().getvNo(), null);//弁番号
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getBenMeisyo(),null);//弁名称

            //kiki メーカー
            java.util.List<Kiki> kikiList=valveForDL.getKikiList();
            if(kikiList.size()>0){
                for(Kiki kiki:kikiList){
                    if(Config.KikiBunRuiA.equals(kiki.getKikiBunrui())||"A".equals(kiki.getKikiBunrui())){
                        OoxmlUtil.setData(sheet,rowActive,colNum++,kiki.getMakerRyaku(),null);//メーカー
                        break;
                    }
                }
            }

            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getYobikeiRyaku(),null);//呼び径
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getClassType(),null);//クラス
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getKeisikiRyaku(),null);//型式
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getSousaRyaku(),null);//駆動方式
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getZaisituRyaku(),null);//本体材質

            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getRyutaiRyaku(),null);//流体
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getAturyokuMax(),null);//圧力
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getTani(),null);//圧力単位
            OoxmlUtil.setData(sheet,rowActive,colNum++,valveForDL.getValve().getOndoMax(),null);//温度(℃)

            rowActive=rowActive+1;
        }
        //罫線を描画
        Map<String,XSSFCellStyle> styleMap= OoxmlUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");
        OoxmlUtil.setRowDataCellStyle(wb, sheet, 1, rowActive+30, 0, 19, true, style);
        //ハイパーリンク設定
        for(int i=1;i<rowActive+30;i++){
            XSSFCreationHelper helper= wb.getCreationHelper();
            XSSFHyperlink link=helper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
            link.setAddress("data!A"+(i+1));
            XSSFRow row = OoxmlUtil.getRowAnyway(sheet, i);
            XSSFCell cell = OoxmlUtil.getCellAnyway(row, 18);
            cell.setCellValue("指示書作成");
            cell.setHyperlink(link);
        }
        for(int i=1;i<rowActive+30;i++){
            XSSFCreationHelper helper= wb.getCreationHelper();
            XSSFHyperlink link=helper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
            link.setAddress("data!A"+(i+1));
            XSSFRow row = OoxmlUtil.getRowAnyway(sheet, i);
            XSSFCell cell = OoxmlUtil.getCellAnyway(row, 19);
            cell.setCellValue("特別加工記録明細書");
            cell.setHyperlink(link);
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
