package com.toyo.vh.controller.print;

import com.toyo.vh.controller.Config;
import com.toyo.vh.controller.utilities.OoxmlFastUtil;
import com.toyo.vh.entity.*;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Header;
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
import java.util.List;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/10.
 */
public class PrintForKikisysKenanList {

    /**
     *
     * 懸案リストをダウンロード
     * @param fileName DLファイル名
     *
     * @param valveForDLList 懸案リスト
     *
     * @param isTaiaoFlg 対応フラグ
     *          true:すべて懸案出力
     *          false:未対応懸案のみ出力
     * @return 懸案リスト.xlsx
     *
     * */
    public static boolean downloadForKikisysKenanList(String fileName,List<ValveKenanDL> valveForDLList,boolean isTaiaoFlg,HttpServletResponse res) throws IOException {

        //Excelを読み込む
        /*DLファイル名を設定*/
        String encordeUrl= URLEncoder.encode(fileName, "UTF-8");
        /*コンテキストにDLファイル情報を設定する*/
        res.setContentType("application/msexcel;charset=UTF-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + encordeUrl);

        /*Workbook作成*/
        InputStream in1=new FileInputStream(Config.ValveKenanTemplate);
        SXSSFWorkbook wb=new SXSSFWorkbook(new XSSFWorkbook(in1),600000);
        SXSSFSheet sheet=(SXSSFSheet)wb.getSheetAt(0);
        //style
        Color color=new Color(255, 255, 255);

        Map<String,XSSFCellStyle> styleMap= OoxmlFastUtil.createCellStyle(wb);
        XSSFCellStyle style = styleMap.get("normal");

        //header
        String companyLocation="";
        if(valveForDLList!=null && valveForDLList.size()>0){
            ValveKenanDL valveKenanDL=valveForDLList.get(0);
            companyLocation=valveKenanDL.getValve().getLocationName();
        }
        Header header = sheet.getHeader();
        header.setLeft(HSSFHeader.fontSize((short) 12)+companyLocation);


        /*データ設定*/
        int rowCount=4;
        for(ValveKenanDL valveKenanDL:valveForDLList){
            List<Kiki> tmpKikiList=valveKenanDL.getKikiList();
            //kikiごとにkenanを取得

            Map<String,List<Kenan>> kenanList=valveKenanDL.getKenanList();
            //kouji
            Map<String,Kouji> koujiMap=valveKenanDL.getKoujiMap();
            if(tmpKikiList.size()>0){
                for(int n=0;n<tmpKikiList.size();n++){
                    Kiki kiki=tmpKikiList.get(n);
                    List<Kenan> tmpKenan=kenanList.get(kiki.getKikiId()+"");

                    for(Kenan kenan:tmpKenan){
                        if(isTaiaoFlg || "未対応".equals(kenan.getTaiouFlg())){
                            //すべての懸案を出力　対応と未対応

                            //罫線を描画
//                            OoxmlFastUtil.setTableDataCellStyle(wb,sheet,rowCount,rowCount+3,0,10,true, styleMap);

                            /**弁基本情報*/
                            //１行目
                            OoxmlFastUtil.setData(sheet,rowCount,0,valveKenanDL.getValve().getvNo(),null);//弁番号
                            OoxmlFastUtil.setData(sheet,rowCount,1,valveKenanDL.getValve().getvNoSub(),null);//識番
                            OoxmlFastUtil.setData(sheet,rowCount,2,valveKenanDL.getValve().getLocationName(),null);//設置プラント
                            //2行目
                            OoxmlFastUtil.setData(sheet,rowCount+1,0,valveKenanDL.getValve().getBenMeisyo(),null);//弁名称

                            /**機器基本情報*/
                            OoxmlFastUtil.setData(sheet,rowCount,3,kiki.getKikiNo(),null);//機器番号
                            OoxmlFastUtil.setData(sheet,rowCount,4,kiki.getKikiMei(),null);//機器名称
                            OoxmlFastUtil.setData(sheet,rowCount+2,3,kiki.getKikiBunruiSeq(),null);//機器連番
                            OoxmlFastUtil.setData(sheet,rowCount+2,4,kiki.getSyukan(),null);//主管係

                            /**懸案基本情報*/
                            //１行目
                            if("未対応".equals(kenan.getTaiouFlg())){
                                OoxmlFastUtil.setData(sheet,rowCount,5,"○",null);//未対応}
                            }else{
//                                OoxmlFastUtil.setData(sheet,rowCount,5,"●",style);//対応}
                            }

                            OoxmlFastUtil.setData(sheet,rowCount,6,kenan.getHakkenDate(),null);//発見日付
                            OoxmlFastUtil.setData(sheet,rowCount,7,kenan.getHakkenJyokyo(),null);//発見時の状況
                            OoxmlFastUtil.setData(sheet,rowCount,8,kenan.getJisyo(),null);//機器の事象
                            OoxmlFastUtil.setData(sheet,rowCount,9,kenan.getGensyo(),null);//損傷の状況
                            OoxmlFastUtil.setData(sheet,rowCount,10,kenan.getTaisaku(),null);//改善対策

                            //2行目
                            OoxmlFastUtil.setData(sheet,rowCount+2,6,kenan.getTaisakuDate(),null);//対応日付
                            OoxmlFastUtil.setData(sheet,rowCount+2,7,kenan.getSyotiNaiyou(),null);//処置内容
                            OoxmlFastUtil.setData(sheet,rowCount+2,8,kenan.getBuhin(),null);//部品・箇所
                            OoxmlFastUtil.setData(sheet,rowCount+2,9,kenan.getYouin(),null);//損傷の原因

                            //Mergeセル接合
                            setKenanMerger(wb, sheet, rowCount, rowCount +3,0,10, styleMap);
                            //罫線を描画
                            OoxmlFastUtil.setTableDataCellStyle(wb,sheet,rowCount,rowCount+3,0,10,true, styleMap);
                            //懸案未対応　水平方向設定
                            OoxmlFastUtil.setSingleCellStyle(wb,sheet,rowCount,rowCount+1,5,6, CellStyle.ALIGN_CENTER,CellStyle.VERTICAL_TOP);
                            rowCount=rowCount+4;
                        }
                    }
                }
            }
        }
        //縮小して全体を表示する
        for(int rIndex=5;rIndex<rowCount;rIndex++){
            for(int cIndex=3;cIndex<6;cIndex++){
                OoxmlFastUtil.setShrinkToFitForCell(wb,sheet,rIndex,cIndex);
            }
        }

        //改ページ設定　
        // １ページに最大54行(4行のタイトルを含む)を出力する
        for(int rIndex=44;rIndex<rowCount;rIndex=rIndex+40){
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
     * 懸案セルなどの結合
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

    public static void setKenanMerger(SXSSFWorkbook workbook,SXSSFSheet sheet, int nRowStart,int nRowEnd,
                                    int nColumnStart,int nColumnEnd,Map<String,XSSFCellStyle> styleMap) {
        assert sheet != null;
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+1,nRowStart+3,0,1,null);//弁名称
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+3,2,2,null);//設置プラント

        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,3,3,null);//機器番号
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,4,4,null);//機器名称
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,3,3,null);//機器連番
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,4,4,null);//主管係

        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+3,5,5,null);//未対応
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,6,6,null);//発見日付
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,7,7,null);//発見時の状況
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,8,8,null);//機器の事象
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+1,9,9,null);//損傷の状況
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart,nRowStart+3,10,10,null);//改善対策

        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,6,6,null);//対応日付
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,7,7,null);//処置内容
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,8,8,null);//部品・箇所
        OoxmlFastUtil.setMerger(workbook,sheet,nRowStart+2,nRowStart+3,9,9,null);//損傷の原因

    }
}
