package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlUtil;
import com.toyo.vh.entity.Buhin;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.ValveForDL;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
public class PrintForKikisysGPList {

    public static boolean downloadForKikisysForGP(java.util.List<ValveForDL> valveForDLList,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(Config.ValveDetailForGP, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.ValveDetailForGPTemplate);
        XSSFWorkbook wb=new XSSFWorkbook(in1);
//        XSSFSheet sheet=wb.getSheet("data");
        XSSFSheet sheet=wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");


        /*データ設定*/
        int rowCount=2;
        int count=1;
        for(ValveForDL valveForDL:valveForDLList){
            /**弁基本情報*/
            //１行目
            OoxmlUtil.setData(sheet,rowCount,0,Integer.toString(count),null);//NO
            OoxmlUtil.setData(sheet,rowCount,1,valveForDL.getValve().getvNo(),null);//弁番号
            OoxmlUtil.setData(sheet,rowCount,2,valveForDL.getValve().getBenMeisyo(),null);//弁名称
            OoxmlUtil.setData(sheet,rowCount,3,valveForDL.getValve().getKeisikiRyaku(),null);//弁形式
            OoxmlUtil.setData(sheet,rowCount,4,valveForDL.getValve().getSousaRyaku(),null);//駆動方式
            OoxmlUtil.setData(sheet,rowCount,5,valveForDL.getValve().getClassType(),null);//クラス
            //呼び径の単位のAを削除
            String yobikei=valveForDL.getValve().getYobikeiRyaku();
            if(!yobikei.isEmpty()){
                yobikei=yobikei.replace("A","");
            }
            OoxmlUtil.setData(sheet,rowCount,6,yobikei,style);//呼び径
            OoxmlUtil.setData(sheet,rowCount,7,valveForDL.getValve().getAturyokuMax(),null);//圧力
            OoxmlUtil.setData(sheet,rowCount,8,valveForDL.getValve().getOndoMax(),null);//温度(℃)
            OoxmlUtil.setData(sheet,rowCount,9,valveForDL.getValve().getRyutaiRyaku(),null);//流体
            OoxmlUtil.setData(sheet,rowCount,10,valveForDL.getValve().getZaisituRyaku(),null);//本体材質


            int tmp=rowCount;
            int nRowStart=rowCount;
            int ColCount=17;


            //kiki
            java.util.List<Kiki> kikiList=valveForDL.getKikiList();
            Map<Kiki, java.util.List<Buhin>> buhinList=valveForDL.getBuhinList();
            if(kikiList.size()>0){
                //kiki部分-弁本体
                Integer GPStartRow=rowCount;
                Integer GasketRow=rowCount;
                Integer GPStartCol=ColCount+2;
                Integer GaStartCol=ColCount;
                for(int nIndex=0;nIndex<kikiList.size();nIndex++){
                    boolean kikihontai=true;
                    Kiki tmpKiki=kikiList.get(nIndex);
                    if(Config.KikiBunRuiA.equals(tmpKiki.getKikiBunrui())&&kikihontai){
                        OoxmlUtil.setData(sheet,rowCount,11,tmpKiki.getMakerRyaku(),null);//メーカ
                        OoxmlUtil.setData(sheet,rowCount,12,tmpKiki.getKatasikiNo(),null);//型式番号
                        OoxmlUtil.setData(sheet,rowCount,16,valveForDL.getValve().getSetBasyo(),null);//設定場所

                        // buhin部分
                        java.util.List<Buhin> tmpBuhin=buhinList.get(tmpKiki);
                        for(int mIndex=0;mIndex<tmpBuhin.size();mIndex++){
                            String buhinburui=tmpBuhin.get(mIndex).getBuhinMei();
                            if(buhinburui.contains(Config.BuhinBunRuiB)){
                                OoxmlUtil.setData(sheet,GasketRow,GaStartCol,tmpBuhin.get(mIndex).getHinban(),null);//品番
                                OoxmlUtil.setData(sheet,GasketRow++,GaStartCol+1,tmpBuhin.get(mIndex).getSunpou(),null);//寸法
                            }else if(buhinburui.contains(Config.BuhinBunRuiA)){
                                OoxmlUtil.setData(sheet,GPStartRow,GPStartCol,tmpBuhin.get(mIndex).getHinban(),null);//品番
                                String sunpou=tmpBuhin.get(mIndex).getSunpou();
                                String[] vids = sunpou.split("x");
                                for(int n=0;n<vids.length&&n<3;n++){
                                    String tmpSunpo=vids[n];
                                    if(!tmpSunpo.isEmpty()){
                                        //寸法の単位のHを削除
                                        tmpSunpo=tmpSunpo.replace("H","");
                                        OoxmlUtil.setData(sheet,GPStartRow,GPStartCol+1+(n*2),tmpSunpo,null);//寸法
                                    }
                                    OoxmlUtil.setData(sheet,GPStartRow,GPStartCol+2+(n*2),"*",null);//寸法

                                }
                                OoxmlUtil.setData(sheet,GPStartRow,25,"H",null);//寸法の単位
                                GPStartRow=GPStartRow+1;
                            }
                        }
                        kikihontai=false;
                    }
                    //弁本体出力完了、次の機器を出力ために、次にあいてる行に移動

                    if(GasketRow>=GPStartRow){
                        rowCount=GasketRow;
                    }else{
                        rowCount=GPStartRow;
                    }
                    //機器があり、部品がない場合、次の行に行く
                    if(rowCount==tmp){
                        rowCount=rowCount+1;
                    }
                }
                //弁本体以外
                for(int nIndex=0;nIndex<kikiList.size();nIndex++){
                    boolean kikihontai=true;
                    Kiki tmpKiki=kikiList.get(nIndex);
                    if(!Config.KikiBunRuiA.equals(tmpKiki.getKikiBunrui())&&kikihontai){
                        OoxmlUtil.setData(sheet,rowCount,11,tmpKiki.getMakerRyaku(),null);//メーカ
                        OoxmlUtil.setData(sheet,rowCount,12,tmpKiki.getKatasikiNo(),null);//型式番号
                        OoxmlUtil.setData(sheet,rowCount,16,valveForDL.getValve().getSetBasyo(),null);//設定場所

                        // buhin部分
                        java.util.List<Buhin> tmpBuhin=buhinList.get(tmpKiki);
                        for(int mIndex=0;mIndex<tmpBuhin.size();mIndex++){
                            String buhinburui=tmpBuhin.get(mIndex).getBuhinMei();
                            if(buhinburui.contains(Config.BuhinBunRuiB)){
                                OoxmlUtil.setData(sheet,GasketRow,GaStartCol,tmpBuhin.get(mIndex).getHinban(),null);//品番
                                OoxmlUtil.setData(sheet,GasketRow++,GaStartCol+1,tmpBuhin.get(mIndex).getSunpou(),null);//寸法
                            }else if(buhinburui.contains(Config.BuhinBunRuiA)){
                                OoxmlUtil.setData(sheet,GPStartRow,GPStartCol,tmpBuhin.get(mIndex).getHinban(),null);//品番
                                String sunpou=tmpBuhin.get(mIndex).getSunpou();
                                String[] vids = sunpou.split("x");
                                for(int n=0;n<vids.length&&n<3;n++){
                                    String tmpSunpo=vids[n];
                                    if(!tmpSunpo.isEmpty()){
                                        //寸法の単位のHを削除
                                        tmpSunpo=tmpSunpo.replace("H","");
                                        OoxmlUtil.setData(sheet,GPStartRow,GPStartCol+1+(n*2),tmpSunpo,null);//寸法
                                    }
                                    OoxmlUtil.setData(sheet,GPStartRow,GPStartCol+2+(n*2),"*",null);//寸法
                                }
                                OoxmlUtil.setData(sheet,GPStartRow,25,"H",null);//寸法
                                GPStartRow=GPStartRow+1;
                            }
                        }
                        kikihontai=false;
                    }
                    //行を飛ばす
                    if(GasketRow>=GPStartRow){
                        rowCount=GasketRow;
                    }else{
                        rowCount=GPStartRow;
                    }
                    //機器があり、部品がない場合、次の行に行く
                    if(rowCount==tmp){
                        rowCount=rowCount+1;
                    }
                }
            }else{
                rowCount=rowCount+1;
            }

            //No列　部品複数がある場合、1-1,1-2のように出力
            int num=2;
            for(int nIndex=nRowStart+1;nIndex<rowCount;nIndex++){
                OoxmlUtil.setData(sheet,nIndex,0,Integer.toString(count)+"-"+Integer.toString(num++),null);//NO
            }
            count=count+1;
            //罫線を描画
            OoxmlUtil.setRowDataCellStyle(wb,sheet,2,rowCount,0,25,true, styleMap.get("normal"));

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
