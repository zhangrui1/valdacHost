package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.entity.Kouji;
import com.toyo.vh.entity.Valve;
import org.apache.poi.ss.usermodel.CellStyle;
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
public class PrintForKoujiValveRabel {

    /**
     *
     * 工事の弁ラベルをダウンロード
     * @param fileName DLファイル名
     *
     * @param kouji 工事
     *
     * @param valveList 弁リスト
     *
     * @return 弁ラベル.xlsx
     *
     * */
    public static boolean downloadForKoujiValveRabel(String fileName,Kouji kouji,String naikeiFlg, List<Valve> valveList,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.KoujiValveRabelTemplate);
        SXSSFWorkbook wb=new SXSSFWorkbook(new XSSFWorkbook(in1),600000);
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");


        /*データ設定*/
        int rowCount=0;
        int ColCount=0;
        int num=0;
        for(int nIndex=0;nIndex<valveList.size();nIndex++){
            //1行目
            Valve valve=valveList.get(nIndex);
            if(valve!=null){
                setValve(wb,sheet,rowCount,num,valve,kouji,naikeiFlg,styleMap);
                num=num+1;
            }

            if(num%4==0){
                num=0;
                rowCount=rowCount+4;
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
     * 工事作業ラベルセルスタイル設定
     *
     * @param workbook
     *            ワークブック
     * @param sheet
     *            シート
     * @param  nRowStart
     *             開始行
     * @param  nColumnStart
     * 　　　　　　開始列
     * @param  valve
     * 　　　　　　弁
     * @param  kouji
     * 　　　　　　工事
     * @param  styleMap
     *          罫線style
     */

    public static void setValve(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,
                                    int nColumnStart,Valve valve,Kouji kouji,String naikeiFlg,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        int firstRow=nRowStart;
        int ColCount=nColumnStart;
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_GENERAL);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);

        OoxmlFastUtil.setData(sheet,firstRow,ColCount,valve.getvNo(),null);//弁番号

        OoxmlFastUtil.setData(sheet,firstRow+1,ColCount,valve.getBenMeisyo(),null);//弁名称
        //内径について　"1"は出力、０は出力しない
        if(naikeiFlg.equals("1")){
            //部品寸法、品番の情報出力
            OoxmlFastUtil.setData(sheet,firstRow+2,ColCount,valve.getKougu1M(),null);//部品　,品番
            OoxmlFastUtil.setData(sheet,firstRow+3,ColCount,valve.getFutai(),null);//部品　パッキン寸法
        }else{
            OoxmlFastUtil.setData(sheet,firstRow+2,ColCount,"",null);//部品　,品番
            OoxmlFastUtil.setData(sheet,firstRow+3,ColCount,"",null);//部品　パッキン寸法
        }


        Short fontSize=24;//弁番号
        Short fontSize2=14;//弁名称
        float rowHeight=35;//弁番号
        float rowHeight2=40;//弁名称
        float rowHeight3=20;//部品　パッキン寸法,品番
        //罫線を描画
        OoxmlFastUtil.setCellStyleForLabel(workbook,sheet,nRowStart,ColCount,true,fontSize,rowHeight);
        OoxmlFastUtil.setCellStyleForLabel(workbook,sheet,nRowStart+1,ColCount,false,fontSize2,rowHeight2);
        OoxmlFastUtil.setCellStyleForLabel(workbook,sheet,nRowStart+2,ColCount,false,fontSize2,rowHeight3);
        OoxmlFastUtil.setCellStyleForLabel(workbook,sheet,nRowStart+3,ColCount,false,fontSize2,rowHeight3);
    }

}
