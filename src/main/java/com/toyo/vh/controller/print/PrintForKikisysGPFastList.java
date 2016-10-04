package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.entity.Buhin;
import com.toyo.vh.entity.Kiki;
import com.toyo.vh.entity.ValveForDL;
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
public class PrintForKikisysGPFastList {

    public static boolean downloadForKikisysForGPFast(java.util.List<ValveForDL> valveForDLList,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(Config.ValveDetailForGP, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.ValveDetailForGPTemplate);
        //XSSFWorkbook wb=new XSSFWorkbook(in1);
        SXSSFWorkbook wb = new SXSSFWorkbook(new XSSFWorkbook(in1),10000);
        //        XSSFSheet sheet=wb.getSheet("data");
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");


        /*データ設定*/
        int rowCount=2;
        int count=1;
        for(ValveForDL valveForDL:valveForDLList){
            /**弁基本情報*/
            //１行目
//            OoxmlFastUtil.setData(sheet,rowCount,0,Integer.toString(count++),null);//NO
            OoxmlFastUtil.setData(sheet,rowCount,1,valveForDL.getValve().getvNo(),null);//弁番号
            OoxmlFastUtil.setData(sheet,rowCount,2,valveForDL.getValve().getBenMeisyo(),null);//弁名称
            OoxmlFastUtil.setData(sheet,rowCount,3,valveForDL.getValve().getKeisikiRyaku(),null);//弁形式
            OoxmlFastUtil.setData(sheet,rowCount,4,valveForDL.getValve().getSousaRyaku(),null);//駆動方式
            OoxmlFastUtil.setData(sheet,rowCount,5, valveForDL.getValve().getClassType(), null);//クラス

            //呼び径の単位のAを削除
            String yobikei=valveForDL.getValve().getYobikeiRyaku();
            if(!yobikei.isEmpty()){
                yobikei=yobikei.replace("A","");
            }
            OoxmlFastUtil.setData(sheet,rowCount,6,yobikei,null);//呼び径
            OoxmlFastUtil.setData(sheet,rowCount,7,valveForDL.getValve().getAturyokuMax(),null);//圧力
            OoxmlFastUtil.setData(sheet,rowCount,8,valveForDL.getValve().getOndoMax(),null);//温度(℃)
            OoxmlFastUtil.setData(sheet,rowCount,9,valveForDL.getValve().getRyutaiRyaku(),null);//流体
            OoxmlFastUtil.setData(sheet,rowCount,10,valveForDL.getValve().getZaisituRyaku(),null);//本体材質


            int tmp=rowCount;
            int nRowStart=rowCount;
            int ColCount=17;
//

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
                    if((Config.KikiBunRuiA.equals(tmpKiki.getKikiBunrui())||"A".equals(tmpKiki.getKikiBunrui())) && kikihontai && ("0".equals(tmpKiki.getKikiDelFlg()))){
                        OoxmlFastUtil.setData(sheet,rowCount,11,tmpKiki.getMakerRyaku(),null);//メーカ
                        OoxmlFastUtil.setData(sheet,rowCount,12,tmpKiki.getKatasikiNo(),null);//型式番号
                        OoxmlFastUtil.setData(sheet,rowCount,16,valveForDL.getValve().getSetBasyo(),null);//設定場所

                        // buhin部分
                        java.util.List<Buhin> tmpBuhin=buhinList.get(tmpKiki);
                        for(int mIndex=0;mIndex<tmpBuhin.size();mIndex++){
                            String buhinburui=tmpBuhin.get(mIndex).getBuhinMei();
                            String Tmphiban=tmpBuhin.get(mIndex).getHinban();
                            String tmpSunpou=tmpBuhin.get(mIndex).getSunpou();
                            if(StringUtil.isNotEmpty(Tmphiban)||StringUtil.isNotEmpty(tmpSunpou)){
                                if(buhinburui.contains(Config.BuhinBunRuiB)){
                                    OoxmlFastUtil.setData(sheet,GasketRow,GaStartCol,Tmphiban,null);//品番
                                    OoxmlFastUtil.setData(sheet,GasketRow++,GaStartCol+1,tmpSunpou,null);//寸法
                                }else if(buhinburui.contains(Config.BuhinBunRuiA)){
                                    OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol,Tmphiban,null);//品番
                                    String sunpou=tmpBuhin.get(mIndex).getSunpou();
                                    String[] vids = sunpou.split("x");
                                    for(int n=0;n<vids.length&&n<3;n++){
                                        String tmpSunpo=vids[n];
                                        if(!tmpSunpo.isEmpty()){
                                            //寸法の単位のHを削除
                                            tmpSunpo=tmpSunpo.replace("H","");
                                            OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+1+(n*2),tmpSunpo,null);//寸法
                                        }
                                        OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+2+(n*2),"*",null);//寸法
                                    }
                                    OoxmlFastUtil.setData(sheet,GPStartRow,25,"H",null);//寸法
                                    GPStartRow=GPStartRow+1;
                                }
                            }
                        }
                        kikihontai=false;


                        if(GasketRow>GPStartRow){
                            rowCount=GasketRow;
                        }else if(GasketRow==GPStartRow && GasketRow>tmp){
                            rowCount=GasketRow;
                        }else if(GasketRow<GPStartRow){
                            rowCount=GPStartRow;
                        }
                        //機器があり、部品がない場合、次の行に行く
                        if(rowCount==tmp){
                            rowCount=rowCount+1;
                            tmp=tmp+1;
                            GasketRow=GasketRow+1;
                            GPStartRow=GPStartRow+1;
                        }
                        if(!kikihontai){
                            break;
                        }
                        //弁本体出力完了、次の機器を出力ために、次にあいてる行に移動
                    }
                }

                //弁本体以外
                for(int nIndex=0;nIndex<kikiList.size();nIndex++){
                    Kiki tmpKiki=kikiList.get(nIndex);
                    if(!(Config.KikiBunRuiA.equals(tmpKiki.getKikiBunrui())||"A".equals(tmpKiki.getKikiBunrui()))){
                        if("0".equals(tmpKiki.getKikiDelFlg())){
                            OoxmlFastUtil.setData(sheet,rowCount,11,tmpKiki.getMakerRyaku(),null);//メーカ
                            OoxmlFastUtil.setData(sheet,rowCount,12,tmpKiki.getKatasikiNo(),null);//型式番号
                            OoxmlFastUtil.setData(sheet,rowCount,16,valveForDL.getValve().getSetBasyo(),null);//設定場所

                            // buhin部分
                            java.util.List<Buhin> tmpBuhin=buhinList.get(tmpKiki);
                            for(int mIndex=0;mIndex<tmpBuhin.size();mIndex++){
                                String buhinburui=tmpBuhin.get(mIndex).getBuhinMei();
                                String Tmphiban=tmpBuhin.get(mIndex).getHinban();
                                String tmpSunpou=tmpBuhin.get(mIndex).getSunpou();
                                if(StringUtil.isNotEmpty(Tmphiban)||StringUtil.isNotEmpty(tmpSunpou)){
                                    if(buhinburui.contains(Config.BuhinBunRuiB)){
                                        OoxmlFastUtil.setData(sheet,GasketRow,GaStartCol,Tmphiban,null);//品番
                                        OoxmlFastUtil.setData(sheet,GasketRow++,GaStartCol+1,tmpSunpou,null);//寸法
                                    }else if(buhinburui.contains(Config.BuhinBunRuiA)){
                                        OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol,Tmphiban,null);//品番
                                        String sunpou=tmpBuhin.get(mIndex).getSunpou();
                                        String[] vids = sunpou.split("x");
                                        for(int n=0;n<vids.length&&n<3;n++){
                                            String tmpSunpo=vids[n];
                                            if(!tmpSunpo.isEmpty()){
                                                //寸法の単位のHを削除
                                                tmpSunpo=tmpSunpo.replace("H","");
                                                OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+1+(n*2),tmpSunpo,null);//寸法
                                            }
                                            OoxmlFastUtil.setData(sheet,GPStartRow,GPStartCol+2+(n*2),"*",null);//寸法
                                        }
                                        OoxmlFastUtil.setData(sheet,GPStartRow,25,"H",null);//寸法
                                        GPStartRow=GPStartRow+1;
                                    }
                                }
                            }
                            //行を飛ばす
                            if(GasketRow>GPStartRow){
                                rowCount=GasketRow;
                            }else if(GasketRow==GPStartRow && GasketRow>tmp){
                                rowCount=GasketRow;
                            }else if(GasketRow<GPStartRow){
                                rowCount=GPStartRow;
                            }
                            //機器があり、部品がない場合、次の行に行く
                            if(rowCount==tmp){
                                rowCount=rowCount+1;
                                tmp=tmp+1;
                                GasketRow=GasketRow+1;
                                GPStartRow=GPStartRow+1;
                            }
                        }

                    }
                }
            }else{
                rowCount=rowCount+1;
            }
            //count を記入
            for(int rowNum=nRowStart;rowNum<=rowCount;rowNum++){
                OoxmlFastUtil.setData(sheet,rowNum,0,Integer.toString(count),null);//NO
            }
            count=count+1;
            //罫線を描画
            OoxmlFastUtil.setRowDataCellStyle(wb,sheet,2,rowCount,0,25,true, style);

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
