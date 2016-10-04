package com.toyo.vh.controller;

import com.itextpdf.text.DocumentException;
import com.toyo.vh.controller.print.*;
import com.toyo.vh.controller.utilities.PrintUtil;
import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.dao.KoujiMapper;
import com.toyo.vh.dto.SearchResultObject;
import com.toyo.vh.entity.*;
import com.toyo.vh.service.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by zr on 01/08/15.
 */

@Controller
@RequestMapping("/print")
public class PrintController {

    @Autowired
    KoujiService koujiService;
    @Autowired
    TenkenRirekiService tenkenRirekiService;
    @Autowired
    KenanService kenanService;
    @Autowired
    ItemService itemService;
    @Autowired
    KoujirelationService koujirelationService;
    @Autowired
    LuceneIndexService luceneIndexService;
    @Autowired
    ImageService imageService;
    @Autowired
    IcsService icsService;
    @Resource
    KoujiMapper koujiMapper;
    /**
     *
     * 工事IDにより、点検リストをダウンロード
     * @param id 工事ID
     *
     * @return 点検リスト.txt
     *
     * */
    @RequestMapping(value = "/printTenken/{id}", method = RequestMethod.POST)
    public String printTenken(@PathVariable("id")String id, @RequestParam("idListForPrintTenken")String idListForPrintTenken,ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException {

        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //tenkenRirekiを弁ごとにわかる
        Map<String,List<TenkenRirekiUtil>> tenkenRirekiMap=new HashMap<String, List<TenkenRirekiUtil>>();
        for(int nIndex=0;nIndex<tenkenRirekiUtilList.size();nIndex++){
            List<TenkenRirekiUtil> tenkenRirekiUtilTemp=new ArrayList<TenkenRirekiUtil>();
            String tmpKikisysId=tenkenRirekiUtilList.get(nIndex).getValve().getKikiSysId()+"";
            if(!tmpKikisysId.isEmpty()){
                if(tenkenRirekiMap.containsKey(tmpKikisysId)){
                    tenkenRirekiUtilTemp=tenkenRirekiMap.get(tmpKikisysId);
                }
                tenkenRirekiUtilTemp.add(tenkenRirekiUtilList.get(nIndex));
                tenkenRirekiMap.put(tenkenRirekiUtilList.get(nIndex).getValve().getKikiSysId()+"",tenkenRirekiUtilTemp);
            }
        }
        //弁番号をユニークにする
        //idlistから弁リストを取得
        List<Valve> valveList=new ArrayList<Valve>();
        if(idListForPrintTenken.length()>0) {
            String[] vids = idListForPrintTenken.split(",");
            for(String kikisysId:tenkenRirekiMap.keySet()){
                if(Arrays.asList(vids).contains(kikisysId)){//弁IDがある場合は、listに追加する
                    valveList.add(tenkenRirekiMap.get(kikisysId).get(0).getValve());
                }
            }
        }
        // 弁番号で 昇順ソート
        Collections.sort(valveList,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });


       /* Excel出力 */
        String filename=kouji.getKjMeisyo()+"-"+Config.KoujiTenkenList;
        PrintForKoujiTenkenList.downloadForKoujiTenkenList(filename, kouji, valveList,tenkenRirekiMap, res);
        return "kouji/print";
    }

    /**
     *
     * 工事IDにより、工事の点検報告書をダウンロード
     * @param id 工事ID
     *
     * @return 工事の点検報告書.txt
     *
     * */
    @RequestMapping(value = "/printReport/{id}", method = RequestMethod.POST)
    public String printReport(@PathVariable("id")String id,@RequestParam("idListForPrintReport")String idListForPrintReport, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException {

        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //tenkenRirekiを弁ごとにわかる
        Map<Valve,List<TenkenRirekiUtil>> tenkenRirekiMap=new HashMap<Valve, List<TenkenRirekiUtil>>();
        for(int nIndex=0;nIndex<tenkenRirekiUtilList.size();nIndex++){
            List<TenkenRirekiUtil> tenkenRirekiUtilTemp=new ArrayList<TenkenRirekiUtil>();
            Valve tmpValve=tenkenRirekiUtilList.get(nIndex).getValve();
            if(tmpValve!=null){
                if(tenkenRirekiMap.containsKey(tmpValve)){
                    tenkenRirekiUtilTemp=tenkenRirekiMap.get(tmpValve);
                }
                tenkenRirekiUtilTemp.add(tenkenRirekiUtilList.get(nIndex));
                tenkenRirekiMap.put(tenkenRirekiUtilList.get(nIndex).getValve(),tenkenRirekiUtilTemp);
            }
        }
        //弁番号をユニークにする
        //idlistから弁リストを取得
        List<Valve> valveList=new ArrayList<Valve>();
        if(idListForPrintReport.length()>0) {
            String[] vids = idListForPrintReport.split(",");
            for(Valve valve:tenkenRirekiMap.keySet()){
                if(Arrays.asList(vids).contains(valve.getKikiSysId()+"")){//弁IDがある場合は、listに追加する
                    valveList.add(valve);
                }
            }
        }


        // 弁番号で 昇順ソート
        Collections.sort(valveList,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });


       /* Excel出力 */
        String filename=kouji.getKjMeisyo()+"-"+Config.KoujiTenkenReport;
        PrintForKoujiTenkenReportList.downloadForKoujiTenkenReportList(filename, kouji,valveList, tenkenRirekiMap, res);
        return "kouji/print";
    }

    /**
     *
     * 工事IDにより、該工事に所属する機器の過去の未対応懸案リストをダウンロード
     * @param id 工事ID
     *
     * @return 未懸案リスト.xlsx
     *
     * */
    @RequestMapping(value = "/printKenan/{id}", method = RequestMethod.POST)
    public String printKenan(@PathVariable("id")String id, @RequestParam("idListForPrintKenan")String idListForPrintKenan,ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException {

        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //*********懸案出力
        List<Kenan> kenanList=new ArrayList<Kenan>();
        Map<String,Valve> valveMapLastYear=new HashMap<String, Valve>();

        //idListにより 弁リストを取得
        if(idListForPrintKenan.length()>0) {
            String[] vids = idListForPrintKenan.split(",");
            for(int i=0;i<vids.length;i++){
                //valve 取得
                List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+vids[i]+"End");
                if(searchResultObjectsVA.size()>0){
                    Valve tmpValve=new Valve();
                    Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                    if(valve.getKenanFlg().equals(Config.KenanFlg)){
                        //懸案がある場合のみ　追加する
                        valveMapLastYear.put(String.valueOf(valve.getKikiSysId()),valve);
                    }
                }
            }
        }
        //弁番号から懸案リストを取得する
        List<ValveKenanDL> valveKenanDLList=new ArrayList<ValveKenanDL>();
        for (String key : valveMapLastYear.keySet()) {
            Valve valve=valveMapLastYear.get(key);
            //懸案があるかどうかチェックする
            List<SearchResultObject> searchResultObjectsKenans=luceneIndexService.selectRecord(indexKenan,"VA"+valve.kikiSysId+"End");
            if(searchResultObjectsKenans.size()>0) {
                ValveKenanDL tmp=new ValveKenanDL();
                //弁IDから懸案を取得
                tmp= PrintUtil.getKenanByKikisysId(valve.kikiSysId+"",indexPath,luceneIndexService);
                valveKenanDLList.add(tmp);
            }
        }
        //懸案がある場合出力、ない場合メッセージを表示する
        String printMessage="";
        if(valveKenanDLList.size()>0) {

            // 弁番号で 昇順ソート
            Collections.sort(valveKenanDLList,
                    new Comparator<ValveKenanDL>() {
                        @Override
                        public int compare(ValveKenanDL entry1,
                                           ValveKenanDL entry2) {
                            return ( entry1.getValve().getvNo())
                                    .compareTo(entry2.getValve().getvNo());
                        }
                    });

            /* Excel出力 */
            String filename=kouji.getKjMeisyo()+"-未対応のみ-"+Config.KoujiKenan;
            PrintForKikisysKenanList.downloadForKikisysKenanList(filename, valveKenanDLList, false, res);
        }else{
            printMessage="過去未対応懸案はありません";
        }
        session.setAttribute("printMessage",printMessage);
        modelMap.addAttribute("printMessage", printMessage);
        return "kouji/print";
    }

    /**
     *
     * 工事IDにより、該工事に所属する機器の全ての未対応懸案リストをダウンロード
     * @param id 工事ID
     *
     * @return 未懸案リスト.txt
     *
     * */
    @RequestMapping(value = "/printKenanReport/{id}", method = RequestMethod.POST)
    public String printKenanReport(@PathVariable("id")String id,@RequestParam("kenanFlgDL")String kenanFlgDL,@RequestParam("idListForPrintKenanReport")String idListForPrintKenanReport, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException {
        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //*********懸案出力
        List<Kenan> kenanList=new ArrayList<Kenan>();
        Map<String,Valve> valveMapLastYear=new HashMap<String, Valve>();

        //idListにより 弁リストを取得
        if(idListForPrintKenanReport.length()>0) {
            String[] vids = idListForPrintKenanReport.split(",");
            for(int i=0;i<vids.length;i++){
                //valve 取得
                List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+vids[i]+"End");
                if(searchResultObjectsVA.size()>0){
                    Valve tmpValve=new Valve();
                    Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
//                    if(valve.getKenanFlg().equals(Config.KenanFlg)){//全て懸案リスト取得必要
                        //懸案がある場合のみ　追加する
                        valveMapLastYear.put(String.valueOf(valve.getKikiSysId()),valve);
//                    }
                }
            }
        }

        //弁番号から懸案リストを取得する
        List<ValveKenanDL> valveKenanDLList=new ArrayList<ValveKenanDL>();
        for (String key : valveMapLastYear.keySet()) {
            Valve valve=valveMapLastYear.get(key);
            //懸案があるかどうかチェックする
            List<SearchResultObject> searchResultObjectsKenans=luceneIndexService.selectRecord(indexKenan,"VA"+valve.kikiSysId+"End");
            if(searchResultObjectsKenans.size()>0) {
                ValveKenanDL tmp=new ValveKenanDL();
                //弁IDから懸案を取得
                tmp= PrintUtil.getKenanByKikisysId(valve.kikiSysId+"",indexPath,luceneIndexService);
                valveKenanDLList.add(tmp);
            }
        }

        //懸案がある場合出力、ない場合メッセージを表示する
        String printMessage="";

            // 弁番号で 昇順ソート
            Collections.sort(valveKenanDLList,
                    new Comparator<ValveKenanDL>() {
                        @Override
                        public int compare(ValveKenanDL entry1,
                                           ValveKenanDL entry2) {
                            return ( entry1.getValve().getvNo())
                                    .compareTo(entry2.getValve().getvNo());
                        }
                    });

            /* Excel出力 */
            String filename="懸案リスト";
            boolean kenanFlgDl=true;
            if("true".equals(kenanFlgDL)){
                kenanFlgDl=true;//全部(対応、未対応)
                filename=kouji.getKjMeisyo()+"-全部-"+Config.KoujiKenan;
            }else{
                kenanFlgDl=false;//未対応懸案のみ
                filename=kouji.getKjMeisyo()+"-未対応のみ-"+Config.KoujiKenan;
            }

            PrintForKikisysKenanList.downloadForKikisysKenanList(filename,valveKenanDLList,kenanFlgDl, res);

        session.setAttribute("printMessage",printMessage);
        modelMap.addAttribute("printMessage", printMessage);

        return "kouji/print";
    }

    /**
     * 工事IDにより、該工事に所属する機器リストをダウンロード
     *
     * @param id 工事ID
     *
     * @return 作業ラベル.txt
     * */
    @RequestMapping(value = "/printSagyo/{id}", method = RequestMethod.POST)
    public String printSagyo(@PathVariable("id")String id, ModelMap modelMap, @RequestParam("idListForPrintSagyo")String idListForPrintSagyo, HttpSession session,HttpServletResponse res) throws IOException {

        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //選択した弁のみ出力
        List<TenkenRirekiUtil> tenkenRirekiUtilListNew =new ArrayList<TenkenRirekiUtil>();
        //idListにより 弁リストを取得
        if(idListForPrintSagyo.length()>0) {
            String[] vids = idListForPrintSagyo.split(",");

            for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
                //valve ID 取得
                String kikisysId = tenkenRirekiUtilList.get(i).getValve().getKikiSysId() + "";
                if(Arrays.asList(vids).contains(kikisysId)){//弁IDがある場合は、listに追加する
                    tenkenRirekiUtilListNew.add(tenkenRirekiUtilList.get(i));
                }
            }
        }


       /* Excel出力 */
        String filename=kouji.getKjMeisyo()+"-"+Config.KoujiSagyoRabel;
        PrintForKoujiSagyoList.downloadForKoujiSagyonList(filename, kouji,tenkenRirekiUtilListNew, res);
        return "kouji/print";
    }
    /**
     * 工事IDにより、該工事に点検する機器のICS点検リストをダウンロード
     *
     * @param id 工事ID
     *
     * @return ICS点検リスト
     * */
    @RequestMapping(value = "/printIcs/{id}", method = RequestMethod.POST)
    public String printIcs(@PathVariable("id")String id, @RequestParam("idListForPrintIcs")String idListForPrintIcs,ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException {

        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //選択した弁のみ出力
        List<TenkenRirekiUtil> tenkenRirekiUtilListNew =new ArrayList<TenkenRirekiUtil>();
        //idListにより 弁リストを取得
        if(idListForPrintIcs.length()>0) {
            String[] vids = idListForPrintIcs.split(",");

            for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
                //valve ID 取得
                String kikisysId = tenkenRirekiUtilList.get(i).getValve().getKikiSysId() + "";
                if(Arrays.asList(vids).contains(kikisysId)){//弁IDがある場合は、listに追加する
                    tenkenRirekiUtilListNew.add(tenkenRirekiUtilList.get(i));
                }
            }
        }
       /* Excel出力 */
        String filename=kouji.getKjMeisyo()+"-"+Config.KoujiIcsList;
        PrintForKoujiIcsList.PrintForKoujiIcsList(filename, kouji, tenkenRirekiUtilListNew, res);
        return "kouji/print";
    }
    /**
     * 工事IDにより、該工事の工具点検リストをダウンロード
     *
     * @param id 工事ID
     *
     * @return 工具点検リスト
     * */
    @RequestMapping(value = "/printTool/{id}", method = RequestMethod.POST)
    public String printTool(@PathVariable("id")String id, @RequestParam("idListForPrintTool")String idListForPrintTool, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException {

        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //選択した弁のみ出力
        List<TenkenRirekiUtil> tenkenRirekiUtilListNew =new ArrayList<TenkenRirekiUtil>();
        //idListにより 弁リストを取得
        if(idListForPrintTool.length()>0) {
            String[] vids = idListForPrintTool.split(",");

            for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
                //valve ID 取得
                String kikisysId = tenkenRirekiUtilList.get(i).getValve().getKikiSysId() + "";
                if(Arrays.asList(vids).contains(kikisysId)){//弁IDがある場合は、listに追加する
                    tenkenRirekiUtilListNew.add(tenkenRirekiUtilList.get(i));
                }
            }
        }

       /* Excel出力 */
        String filename=kouji.getKjMeisyo()+"-"+Config.KoujiToolList;
        PrintForKoujiToolsList.PrintForKoujiToolsList(filename, kouji, tenkenRirekiUtilListNew, res);
        return "kouji/print";
    }
    /**
     * 工事IDにより、該工事に所属する弁リストをダウンロード
     *
     * @param id 工事ID
     *
     * @return 弁番号ラベル.txt
     * */
    @RequestMapping(value = "/printValve/{id}", method = RequestMethod.POST)
    public String printValve(@PathVariable("id")String id,@RequestParam("naikeiFlg")String naikeiFlg,@RequestParam("idListForValveRabel")String idListForValveRabel, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException {

        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //点検機器のIDリストを取得する　対象部品を抽出だめ
        List<Integer> tenkenKikiIdList=new ArrayList<Integer>();
        for(int i=0;i<tenkenRirekiUtilList.size();i++){
            tenkenKikiIdList.add(tenkenRirekiUtilList.get(i).getKiki().getKikiId());
        }
        //idlistから弁リストを取得
        List<Valve> valveList=new ArrayList<Valve>();
        if(idListForValveRabel.length()>0) {
            String[] vids = idListForValveRabel.split(",");
            if (vids.length > 0) {
                for (int i = 0; i < vids.length; i++) {
                    //弁IDにより、弁、部品情報を取得する
                    List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+vids[i]+"End");
                    if(searchResultObjectsVA.size()>0){
                        //valve 設定
                        Valve tmpValve=new Valve();
                        Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());

                        //部品 設定
                        if(naikeiFlg.equals("1")){
                            List<Buhin> buhins=new ArrayList<Buhin>();
                            List<SearchResultObject> searchResultObjectsBuhin=luceneIndexService.selectRecord(indexBuhinFile,"VA"+vids[i]+"End");
                            String tmpBuhinSunpo="";
                            String tmpBuhinHinban="";

                            for (SearchResultObject searchResultObjectbuhin : searchResultObjectsBuhin) {
                                Buhin tmpbuhin=new Buhin();
                                Buhin buhin=tmpbuhin.toBuhin(searchResultObjectbuhin.getBody());
                                //パッキンのみ出力する
                                if(tenkenKikiIdList.contains(buhin.getKikiidBuhin()) && buhin.getSizaiName().equals(Config.BuhinBunRuiC)){
                                    tmpBuhinSunpo=tmpBuhinSunpo+buhin.getSunpou()+"  ";
                                    tmpBuhinHinban=tmpBuhinHinban+buhin.getHinban()+"   ";
                                }
                            }
                            //部品寸法、品番情報を出力ために　一時的にFutai項目に設定、ＤＢには反映しない
                            valve.setFutai(tmpBuhinSunpo);
                            valve.setKougu1M(tmpBuhinHinban);
                        }
                        valveList.add(valve);
                    }
                }
            }
        }


        // 弁番号で 昇順ソート
        Collections.sort(valveList,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });

        //弁ID,部品リストを紐づけ
        /* Excel出力 */
        String filename="";
        if(naikeiFlg.equals("1")){
            filename=kouji.getKjMeisyo()+"-"+Config.KoujiValveRabelNaikei;
        }else{
            filename=kouji.getKjMeisyo()+"-"+Config.KoujiValveRabel;
        }

        PrintForKoujiValveRabel.downloadForKoujiValveRabel(filename, kouji, naikeiFlg,valveList, res);

        return "kouji/print";
    }


    /**
     *
     * 装填明細書GP画像をダウンロード
     * @param id 工事ID
     *
     * */
    @RequestMapping(value = "/printGP/{id}", method = RequestMethod.POST)
    public ModelAndView printGP(@PathVariable("id")String id,@RequestParam("idList")String idList, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));


        //IDから工事取得
        Kouji kouji=(Kouji) session.getAttribute("kouji");
        if(kouji==null){
            List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+id+"End");
            if(searchResultObjectsKJ.size()>0){
                Kouji tmpkouji=new Kouji();
                kouji=tmpkouji.toKouji(searchResultObjectsKJ.get(0).getBody());
            }
        }
        //点検履歴取得
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        //画像Path保存する
        Map<String,Image> printTargeImages=new HashMap<String, Image>();
//        List<Image> printTargeImages=new ArrayList<Image>();

        //make cache
        String fileName=new String(kouji.getKjMeisyo()+"-GP.pdf");
        OutputStream os=null;
        res.setContentType("application/msword");
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        os=res.getOutputStream();

        String dim="\t";
        ArrayList<String> kikiIds = new ArrayList<String>();
        //データ出力
        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++) {
                    //弁IDから画像一覧取得
                    List<Image> images=imageService.getImagesByKikisysId(vids[i]);
                    if(isNotEmpty(images)){
                        for(Image tmpImage:images){
                            if(tmpImage.getImagesyu().contains(Config.ImageTypeGP)){
                                printTargeImages.put(tmpImage.getImagename(),tmpImage);
                            }
                        }
                    }
                }
            }
        }
        //画像をPDFに出力
        Pdfview pdfview=new Pdfview();
        pdfview.setAttributesMap(printTargeImages);
        return  new ModelAndView(pdfview);
    }
    /**
     *
     * 装填明細書GP画像があるか　チェックする
     * @param idList 弁リスト
     * @return  isGP 画像がある場合はtrue、ない場合はfalse
     *
     * */
    @RequestMapping(value = "/checkPrintGP", method = RequestMethod.POST)
    @ResponseBody
    public String checkPrintGP(@RequestParam("idList")String idList, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));

        //点検履歴取得
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        //画像Path保存する
        Map<String,Image> printTargeImages=new HashMap<String, Image>();

        ArrayList<String> kikiIds = new ArrayList<String>();
        //データ出力
        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++) {
                    //弁IDから画像一覧取得
                    List<Image> images=imageService.getImagesByKikisysId(vids[i]);
                    if(isNotEmpty(images)){
                        for(Image tmpImage:images){
                            if(tmpImage.getImagesyu().contains(Config.ImageTypeGP)){
                                printTargeImages.put(tmpImage.getImagename(),tmpImage);
                            }
                        }
                    }
                }

            }
        }

        String isGP="false";
        if(printTargeImages.size()>0){
            isGP="true";
        }
        return isGP;
    }
    /**
     *
     * ICS画像をダウンロード
     * @param id 工事ID
     *
     * */
    @RequestMapping(value = "/printICS/{id}", method = RequestMethod.POST)
    public ModelAndView printICS(@PathVariable("id")String id,@RequestParam("idList")String idList, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));


        //IDから工事取得
        Kouji kouji=(Kouji) session.getAttribute("kouji");
        if(kouji==null){
            List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+id+"End");
            if(searchResultObjectsKJ.size()>0){
                Kouji tmpkouji=new Kouji();
                kouji=tmpkouji.toKouji(searchResultObjectsKJ.get(0).getBody());
            }
        }

        //画像Path保存する
        Map<String,String> printTargeImages=new HashMap<String, String>();

        //make cache
        String fileName=new String(kouji.getKjMeisyo()+"-ICS.pdf");
        OutputStream os=null;
        res.setContentType("application/msword");
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        os=res.getOutputStream();

        String dim="\t";
        ArrayList<String> kikiIds = new ArrayList<String>();
        //データ出力
        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++) {
                    //弁IDからValueを取得
                    List<SearchResultObject> searchResultObjectsKikisys = luceneIndexService.selectRecord(indexValveFile, "VA" + vids[i] + "End");

                    if (searchResultObjectsKikisys.size() > 0) {
                        Valve tmpValve=new Valve();
                        Valve valve=tmpValve.toValve(searchResultObjectsKikisys.get(0).getBody());
                        //ValueのICS番号から,画像Pathを取得
                        if(valve.getIcs().length()>0){
                            Ics ics=icsService.getIcsByIcsNum(valve.getIcs().trim());
                            if(ics!=null && ics.getImagepath().length()>0){
                                printTargeImages.put(valve.getKikiSysId()+"",ics.getImagepath());
                            }
                        }
                    }
                }
            }
        }

        PdfviewForIcs pdfviewForIcs=new PdfviewForIcs();
        pdfviewForIcs.setAttributesMap(printTargeImages);
        return  new ModelAndView(pdfviewForIcs);
    }

    /**
     *
     * ICS画像があるかどうかチェックする
     * @param idList 弁IDリスト
     *
     * @return isIcs ある場合はtrue,ない場合はfalse
     *
     * */
    @RequestMapping(value = "/checkPrintICS", method = RequestMethod.POST)
    @ResponseBody
    public String checkPrintICS(@RequestParam("idList")String idList, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));

        //画像Path保存する
        Map<String,String> printTargeImages=new HashMap<String, String>();

        ArrayList<String> kikiIds = new ArrayList<String>();
        //データ出力
        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++) {
                    //弁IDからValueを取得
                    List<SearchResultObject> searchResultObjectsKikisys = luceneIndexService.selectRecord(indexValveFile, "VA" + vids[i] + "End");

                    if (searchResultObjectsKikisys.size() > 0) {
                        Valve tmpValve=new Valve();
                        Valve valve=tmpValve.toValve(searchResultObjectsKikisys.get(0).getBody());
                        //ValueのICS番号から,画像Pathを取得
                        if(valve.getIcs().length()>0){
                            Ics ics=icsService.getIcsByIcsNum(valve.getIcs().trim());
                            if(ics!=null && ics.getImagepath().length()>0){
                                printTargeImages.put(valve.getKikiSysId()+"",ics.getImagepath());
                            }
                        }
                    }
                }
            }
        }

        String isIcs="false";
        if(printTargeImages.size()>0){
            isIcs="true";
        }
        return isIcs;
    }
    /**
     *
     * 弁の特別加工記録明細書を印刷
     * @param idListTokuBetuKako 弁IDリスト
     * @param koujiId 工事ID（）
     *
     * */
    @RequestMapping(value = "/printTokuBetuKakoKirokuForKikisysList", method = RequestMethod.POST)
    public void printTokuBetuKakoKirokuForKikisysList(@RequestParam("idListTokuBetuKako")String idListTokuBetuKako,@RequestParam("koujiId")String koujiId, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        String idList=idListTokuBetuKako;
        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        //kouji取得
        Kouji  kouji=new Kouji();
        if(StringUtil.isNotEmpty(koujiId)){
            kouji=koujiService.getKoujiById(koujiId);
        }

        /* get Data */
        List<ValveForDL> valveForDLList=new ArrayList<ValveForDL>();

        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                   ValveForDL tmp=new ValveForDL();
                    tmp= PrintUtil.getKikiByKikisysId(vids[i], indexPath, luceneIndexService);
                    valveForDLList.add(tmp);
                }
            }
        }

        /* Excel出力 */
        PrintForKikisysTokuBetuKakoKiroku.downloadForKikisysTokuBetuKakoKirokuList(valveForDLList,kouji,res);

    }
    /**
     *
     * 弁の指示書を印刷
     * @param idListSijisyo 弁IDリスト
     * @param koujiIdSijisyo 工事ID（）
     *
     * */
    @RequestMapping(value = "/printSijisyoForKikisysList", method = RequestMethod.POST)
    public void printSijisyoForKikisysList(@RequestParam("idListSijisyo")String idListSijisyo,@RequestParam("koujiIdSijisyo")String koujiIdSijisyo, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        String idList=idListSijisyo;
        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        //kouji取得
        Kouji  kouji=new Kouji();
        if(StringUtil.isNotEmpty(koujiIdSijisyo)){
            kouji=koujiService.getKoujiById(koujiIdSijisyo);
        }

        /* get Data */
        List<ValveForDL> valveForDLList=new ArrayList<ValveForDL>();

        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    ValveForDL tmp=new ValveForDL();
                    tmp= PrintUtil.getKikiByKikisysId(vids[i], indexPath, luceneIndexService);
                    valveForDLList.add(tmp);
                }
            }
        }

        /* Excel出力 */
        PrintForKikisysSijisyo.downloadForKikisysSijisyoList(valveForDLList,kouji,res);

    }
    /**
     *
     * 弁の指示書を印刷
     * @param idListKanriDaiTyo 弁IDリスト
     * @param koujiIdKanriDaiTyo 工事ID（）
     *
     * */
    @RequestMapping(value = "/printKanriDaiTyoForKikisysList", method = RequestMethod.POST)
    public void printKanriDaiTyoForKikisysList(@RequestParam("idListKanriDaiTyo")String idListKanriDaiTyo,@RequestParam("koujiIdKanriDaiTyo")String koujiIdKanriDaiTyo, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        String idList=idListKanriDaiTyo;
        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        //kouji取得
        Kouji  kouji=new Kouji();
        if(StringUtil.isNotEmpty(koujiIdKanriDaiTyo)){
            kouji=koujiService.getKoujiById(koujiIdKanriDaiTyo);
        }

        /* get Data */
        List<ValveForDL> valveForDLList=new ArrayList<ValveForDL>();

        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    ValveForDL tmp=new ValveForDL();
                    tmp= PrintUtil.getKikiByKikisysId(vids[i], indexPath, luceneIndexService);
                    valveForDLList.add(tmp);
                }
            }
        }

        // 弁番号で 昇順ソート
        Collections.sort(valveForDLList,
                new Comparator<ValveForDL>() {
                    @Override
                    public int compare(ValveForDL entry1,
                                       ValveForDL entry2) {
                        return ( entry1.getValve().getvNo())
                                .compareTo(entry2.getValve().getvNo());
                    }
                });

        /* Excel出力 */
        PrintForKikisysKanriDaiTyo.downloadForKikisysKanriDaiTyoList(valveForDLList,kouji,res);

    }
    /**
     *
     * 検察結果から、弁、機器、部品リストを印刷
     * @param idList 弁IDリスト
     *
     * */
    @RequestMapping(value = "/printKikisys", method = RequestMethod.POST)
    public void printKikisys(@RequestParam("idList")String idList, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        /* get Data */
        List<ValveForDL> valveForDLList=new ArrayList<ValveForDL>();

        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    ValveForDL tmp=new ValveForDL();
                    tmp= PrintUtil.getKikiByKikisysId(vids[i], indexPath, luceneIndexService);
                    valveForDLList.add(tmp);
                }
            }
        }
        // 弁番号で 昇順ソート
        Collections.sort(valveForDLList,
                new Comparator<ValveForDL>() {
                    @Override
                    public int compare(ValveForDL entry1,
                                       ValveForDL entry2) {
                        return ( entry1.getValve().getvNo())
                                .compareTo(entry2.getValve().getvNo());
                    }
                });

        /* Excel出力 */
        PrintForKikisysDetailList.downloadForKikisysDetailList(valveForDLList,res);

    }
    /**
     *
     * 検察結果から、弁の台帳リストをダウンロードする
     * @param idList 弁IDリスト
     * @param companyLocation 弁会社名
     *
     * */
    @RequestMapping(value = "/printDaityoForKikisysList", method = RequestMethod.POST)
    public void printDaityoForKikisysList(@RequestParam("idList")String idList, @RequestParam("companyLocation")String companyLocation,ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        /* get Data */
        List<ValveForDL> valveForDLList=new ArrayList<ValveForDL>();//弁基本情報
        String tenkentei[]=new String[40]; //点検履歴　定
        String tenkenOther[]=new String[40]; //点検履歴　その他
        Map<String,String[]> tenkenrireki=new HashMap<String,String[]>();//点検履歴 0に非石綿化　1に未対応懸案　２に最終評価

        //点検履歴　年度内に定期検査、または定期自主検査が実施されたら「定」が付きます
        List<Kouji> koujis=koujiMapper.findKoujiByLocation(companyLocation);
        if(koujis!=null && koujis.size()>0){
            for(Kouji tmpkouji:koujis){
                Integer yearCol2=ymdToYear(tmpkouji.getEndYmd());
                if(tmpkouji.getKjKbn().contains("定")){
                    tenkentei[yearCol2]="定";
                }else{
                    tenkenOther[yearCol2]="他";
                }
            }
        }
        //現在年度を取得　点検履歴は現年度を含めて最新の１０年間を表示
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strDate = sdf.format(calendar.getTime());
        String thisYear=strDate.substring(0,4)+"/"+strDate.substring(4,6)+"/"+strDate.substring(6,8);
        int thisYearInt=ymdToYear(thisYear);
        int tenYearBefore=thisYearInt-10;

        /* 弁基本情報を取得*/
        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    ValveForDL tmp=new ValveForDL();
                    tmp= PrintUtil.getKikiByKikisysId(vids[i], indexPath, luceneIndexService);
                    //弁機器をFilter(弁本体と駆動部のみにする)
                    List<Kiki> tmpKikis=tmp.getKikiList();
                    List<Kiki> resultKikis=new ArrayList<Kiki>();//出力対象の機器しか保存しない
                    for(Kiki kiki:tmpKikis){
                        TenkenRirekiUtil tenkenRirekiUtilForLastResult=new TenkenRirekiUtil();
                        if(Config.KikiBunRuiA.equals(kiki.getKikiBunrui())||Config.KikiBunRuiB.equals(kiki.getKikiBunrui())||"A".equals(kiki.getKikiBunrui())||"B".equals(kiki.getKikiBunrui())){
                            //点検履歴取得
//                            List<TenkenRirekiUtil> tenkenRirekis=tenkenRirekiService.getTenkenRirekiByKikiId(kiki.getKikiId()+"");
                            List<SearchResultObject> resultsTenkenRireki=luceneIndexService.selectRecord(indexTenkenRireki,"KI"+kiki.getKikiId()+"End");
                            List<TenkenRirekiUtil> tenkenRirekis=new ArrayList<TenkenRirekiUtil>();
                            if(!CollectionUtils.isEmpty(resultsTenkenRireki)){
                                for(SearchResultObject tmpTOFor:resultsTenkenRireki){
                                    TenkenRirekiUtil tmpTenkenRireki=new TenkenRirekiUtil();
                                    TenkenRirekiUtil tmpTenkenRireki2=tmpTenkenRireki.toTenkenRirekiUtil(tmpTOFor.getBody());
                                    tenkenRirekis.add(tmpTenkenRireki2);
                                }
                            }
                            String[] tenkenRank=new String[40]; //点検ランク 0に対象外の点検ランク 2040年まで保存できる
                            for (int yIndex = 0; yIndex < 39; yIndex++){//点検ランク初期化
                                tenkenRank[yIndex]="";
                            }

                            for(TenkenRirekiUtil tenkenRirekiUtil:tenkenRirekis){
                                Integer yearTenkenCol2=ymdToYear(tenkenRirekiUtil.getTenkenDate());
                                tenkenRank[yearTenkenCol2]=tenkenRirekiUtil.getTenkenRank();

                                //最終評価＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
                                if(tenkenRirekiUtilForLastResult.getId()>0){
                                    String tmp1=tenkenRirekiUtil.getTenkenDate();
                                    String tmp2=tenkenRirekiUtilForLastResult.getTenkenDate();
                                    if(tmp1.compareTo(tmp2)>0 && tenkenRirekiUtil.getTenkenkekka().length()>0){
                                        tenkenRirekiUtilForLastResult=tenkenRirekiUtil;
                                    }
                                }else{
                                    tenkenRirekiUtilForLastResult=tenkenRirekiUtil;
                                }
                            }
                            //最終評価を保存
                            tenkenRank[1]=tenkenRirekiUtilForLastResult.getTenkenkekka();

                            //非石綿化＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
                            //buhinを取得する  A、Nがある場合出力する
                            List<SearchResultObject> resultsBuhin=luceneIndexService.selectRecord(indexBuhinFile,"KI"+kiki.getKikiId()+"End");
//                            List<Buhin> buhinPas=new ArrayList<Buhin>();//パッキンリストを保存
//                            List<Buhin> buhinGas=new ArrayList<Buhin>();//ガスケットリストを保存
//                            Buhin buhinPasForLast=new Buhin();//最新のパッキンを保存
//                            Buhin buhinGasForLast=new Buhin();//最新のガスケットを保存

                            if(!CollectionUtils.isEmpty(resultsBuhin)){
                                tenkenRank[3]="";//パッキン の場合
                                tenkenRank[4]="";//ガスケット
                                for(SearchResultObject tmpTOFor:resultsBuhin){
                                    Buhin tmpBuhin=new Buhin();
                                    Buhin tmpBuhin2=tmpBuhin.toBuhin(tmpTOFor.getBody());
                                    //パッキン の場合
                                    if(tmpBuhin2.getBuhinMei().contains(Config.BuhinBunRuiA)){
                                        String tmpAsbkbn=tmpBuhin2.getAsbKbn();
                                        if(tmpAsbkbn!=null){
                                            tenkenRank[3]= tenkenRank[3]+tmpAsbkbn;
                                        }
                                    }else if(tmpBuhin2.getBuhinMei().contains(Config.BuhinBunRuiB)){//ガスケット の場合
                                        String tmpAsbkbn2=tmpBuhin2.getAsbKbn();
                                        if(tmpAsbkbn2!=null){
                                            tenkenRank[4]= tenkenRank[4]+tmpAsbkbn2;
                                        }
                                    }
                                }
                            }
                            // 未対応懸案確認＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
                            //20160225 弁に懸案フラグがるため、この判断は必要ない
//                            List<SearchResultObject> searchResultObjectsKenan=luceneIndexService.selectRecord(indexKenan,"KI"+kiki.getKikiId()+"End");
//                            if(searchResultObjectsKenan.size()>0) {
//                                for (SearchResultObject searchResultObject1 : searchResultObjectsKenan) {
//                                    Kenan tmpKenan = new Kenan();
//                                    Kenan kenan = tmpKenan.toKenan(searchResultObject1.getBody());
//                                    if(kenan.getTaiouFlg().equals(Config.KenanNoTaiyo)){
//                                        tenkenRank[2]="○";
//                                        break;
//                                    }
//                                }
//                            }
                            //点検履歴結果を保存する＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
                            tenkenrireki.put(kiki.getKikiId()+"",tenkenRank);
                            resultKikis.add(kiki);
                        }
                    }
                    List<Buhin> resultBuhins=new ArrayList<Buhin>();//部品情報は出力しないから、空にする
                    tmp.setBuhins(resultBuhins);
                    tmp.setKikiList(resultKikis);//出力対象の機器しか保存しない
                    valveForDLList.add(tmp);
                }
            }
        }


        // 弁番号で 昇順ソート
        Collections.sort(valveForDLList,
                new Comparator<ValveForDL>() {
                    @Override
                    public int compare(ValveForDL entry1,
                                       ValveForDL entry2) {
                        return ( entry1.getValve().getvNo())
                                .compareTo(entry2.getValve().getvNo());
                    }
                });

        /* Excel出力 */
        PrintForKikisysDaityoFastList.downloadForKikisysDaityoList(valveForDLList,tenkentei,tenkenOther,tenkenrireki,companyLocation,tenYearBefore,res);

    }
    /**
     *
     * 検察結果から、弁、機器、部品リストを印刷　GP仕様出力
     * 室長専用
     * @param idList 弁IDリスト
     *
     * */
    @RequestMapping(value = "/printGPForKikisysList", method = RequestMethod.POST)
    public void printGPForKikisysList(@RequestParam("idList")String idList, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        /* get Data */
        List<ValveForDL> valveForDLList=new ArrayList<ValveForDL>();

        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    ValveForDL tmp=new ValveForDL();
                    tmp= PrintUtil.getKikiByKikisysId(vids[i], indexPath, luceneIndexService);
                    valveForDLList.add(tmp);
                }
            }
        }
        // 弁番号で 昇順ソート
        Collections.sort(valveForDLList,
                new Comparator<ValveForDL>() {
                    @Override
                    public int compare(ValveForDL entry1,
                                       ValveForDL entry2) {
                        return ( entry1.getValve().getvNo())
                                .compareTo(entry2.getValve().getvNo());
                    }
                });

        /* Excel出力 */
        PrintForKikisysGPFastList.downloadForKikisysForGPFast(valveForDLList,res);

    }
    /**
     *
     * 検察結果から、懸案を印刷
     * @param idList 弁IDリスト
     *
     * */
    @RequestMapping(value = "/printKenanForKikisysList", method = RequestMethod.POST)
    public void printKenanForKikisys(@RequestParam("idList")String idList, @RequestParam("kenanFlgDL")String kenanFlgDL,ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
//        long kenanStartTime=System.currentTimeMillis();
//        System.out.println("kenanStartTime   =  "+kenanStartTime);
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        /* get Data */
        List<ValveKenanDL> valveKenanDLList=new ArrayList<ValveKenanDL>();

        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    ValveKenanDL tmp=new ValveKenanDL();
                    //弁IDから懸案を取得
                    tmp= PrintUtil.getKenanByKikisysId(vids[i],indexPath,luceneIndexService);
                    valveKenanDLList.add(tmp);
                }
            }
        }

        // 弁番号で 昇順ソート
        Collections.sort(valveKenanDLList,
                new Comparator<ValveKenanDL>() {
                    @Override
                    public int compare(ValveKenanDL entry1,
                                       ValveKenanDL entry2) {
                        return ( entry1.getValve().getvNo())
                                .compareTo(entry2.getValve().getvNo());
                    }
                });

//        long kenanDBEndTime=System.currentTimeMillis();
//        System.out.println("kenanDBEndTime   =  "+(kenanDBEndTime-kenanStartTime));
        /* Excel出力 */
        String filename="懸案リスト";
        boolean kenanFlgDl=true;
        if("true".equals(kenanFlgDL)){
            kenanFlgDl=true;//全部(対応、未対応)
            filename="全部-"+Config.ValveKenan;
        }else{
            kenanFlgDl=false;//未対応懸案のみ
            filename="未対応のみ-"+Config.ValveKenan;
        }

        PrintForKikisysKenanList.downloadForKikisysKenanList(filename,valveKenanDLList,kenanFlgDl, res);
//        session.setAttribute("message","処理完了");
//        long kenanPrintEndTime=System.currentTimeMillis();
//        System.out.println("kenanPrintEndTime   =  "+(kenanPrintEndTime-kenanDBEndTime));
    }

    /**
     *
     * 選択された弁リストに、懸案があるかどうかチェックする
     *
     * @param idList 弁IDリスト
     *
     * @return isKenan　ある場合はtrue,ない場合はfalse
     *
     * */
    @RequestMapping(value = "/checkKenanForKikisysList", method = RequestMethod.POST)
    @ResponseBody
    public String checkKenanForKikisys(@RequestParam("idList")String idList, ModelMap modelMap, HttpSession session,HttpServletResponse res) throws IOException, DocumentException {

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        /* get Data */
        List<ValveKenanDL> valveKenanDLList=new ArrayList<ValveKenanDL>();

        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    ValveKenanDL tmp=new ValveKenanDL();
                    //弁IDから懸案を取得
                    tmp= PrintUtil.getKenanByKikisysId(vids[i],indexPath,luceneIndexService);
                    valveKenanDLList.add(tmp);
                }
            }
        }
        String isKenan="false";
        for(ValveKenanDL valveKenanDL:valveKenanDLList){
            if(valveKenanDL.getKenanList().size()>0){
                isKenan="true";
            }
        }
        return  isKenan;
    }

    /**
     * データ出力　改行あり
     * @param list 出力用データ
     * @param writer Writer
     * @param dim デリミタ
     * */
    @ResponseBody
    public static void writerList(List<String> list,PrintWriter writer,String dim){
        writer.println(concatWithDelimit(dim,list));
    }

    /**
     * 指定文字列が空文字列であるかどうかの検証
     * */

    public static boolean isNotEmpty(String value){
        return value!=null && value.length()!=0;
    }

    public static boolean isNotEmpty(List list){
        return list!=null && list.size()!=0;
    }

    /**
     * 指定文字列が空文字列でないか検証
     * */
    public static boolean isEmpty(String value){
        return value==null || value.length()==0;
    }

    public static boolean isEmpty(List list){
        return list==null || list.size()==0;
    }

    /**
     * 指定リストを結合する
     * @param  dim 結合区切り
     * @param  list 対象リスト
     *
     * @return 結合した文字列
     * */
    public static String concatWithDelimit(String dim,List<String> list){
        StringBuffer sb=new StringBuffer("");
        for(String value:list){
            if(isNotEmpty(value)){
                if(sb.length()!=0){
                    sb.append(dim);
                }
                sb.append(value);
            }
        }
        return sb.toString();
    }
    /**
     * 指定リストを結合する
     * @param  dim 結合区切り
     * @param  buff 対象文字列を格納する配列
     *
     * @return 結合後の文字列
     * */
    public static String concatWithDelimit(String dim,String... buff){
        StringBuffer sb=new StringBuffer("");
        for(String valve:buff){
            if(isNotEmpty(valve)){
                if(sb.length()!=0){
                   sb.append(dim);
                }
                sb.append(valve);
            }
        }
        return sb.toString();
    }

    /**
     * ダブルクォートでエンクォール
     * @param value 文字列
     * @return エンクォール済み文字列
     *
     * */
    public static String enquote(String value){
        if(isNotEmpty(value)){
            return "\""+value+"\"";
        }else{
            return "\"\"";
        }
    }

    /**
     * ValveのIDをKey
     */
    public static Map<String,TenkenRirekiUtil> toConvertValveMap(List<TenkenRirekiUtil> tenkenRirekiUtilList){
        Map<String,TenkenRirekiUtil> map=new HashMap<String, TenkenRirekiUtil>();
        for(TenkenRirekiUtil tenkenRirekiUtil:tenkenRirekiUtilList){
            if(isNotEmpty(String.valueOf(tenkenRirekiUtil.getValve().getKikiSysId()))){
                map.put(String.valueOf(tenkenRirekiUtil.getValve().getKikiSysId()),tenkenRirekiUtil);
            }
        }
        return  map;
    }

    /**
     * KikiのIDをKey
     */
    public static Map<String,TenkenRirekiUtil> toConvertKikiMap(List<TenkenRirekiUtil> tenkenRirekiUtilList){
        Map<String,TenkenRirekiUtil> map=new HashMap<String, TenkenRirekiUtil>();
        for(TenkenRirekiUtil tenkenRirekiUtil:tenkenRirekiUtilList){
            if(isNotEmpty(String.valueOf(tenkenRirekiUtil.getKiki().getKikiId()))){
                map.put(String.valueOf(tenkenRirekiUtil.getKiki().getKikiId()),tenkenRirekiUtil);
            }
        }
        return  map;
    }

    /**
     * MapからListに変更
     */
    public static List<TenkenRirekiUtil>  MapConvertToList(Map<String,TenkenRirekiUtil>  mapTarget){
        List<TenkenRirekiUtil> lists=new ArrayList<TenkenRirekiUtil>();
        if(!mapTarget.isEmpty()){
            for (String key : mapTarget.keySet()) {
                lists.add(mapTarget.get(key));
            }
        }
        return  lists;
    }

    /**
     * MapからListに変更
     */
    public static List<Valve>  MapConvertToValveList(Map<String,Valve>  mapTarget){
        List<Valve> lists=new ArrayList<Valve>();
        if(!mapTarget.isEmpty()){
            for (String key : mapTarget.keySet()) {
                lists.add(mapTarget.get(key));
            }
        }
        return  lists;
    }

    /**
     * YYYY/MM/DD　から年度を取得する
     * 年度とは日本の会計単位で、４月１日から翌年３月３１日までです
     */
    public static Integer ymdToYear(String ymd){
        String year2="0";
        if(ymd.length()>7){
            String year=ymd.substring(0,4);
            String month=ymd.substring(5,7);
            if("01".equals(month)||"02".equals(month)||"03".equals(month)){
                year=(Integer.parseInt(year)-1)+"";
            }
            year2=year.substring(2,4);
            if(Integer.parseInt(year2)<4 || Integer.parseInt(year2)>40){
                year2="0";
            }
        }
        return  Integer.parseInt(year2);
    }
    /**
     * YYYY/MM/DD　文字列からカレンダに変換
     */
    public static Calendar ymdToCalendar(String ymd){
        Calendar calendar=Calendar.getInstance();

        if(ymd.length()>10){
            String year=ymd.substring(0,4);
            String month=ymd.substring(5,7);
            String day=ymd.substring(8,10);

            calendar.set(Calendar.YEAR,Integer.parseInt(year));
            calendar.set(Calendar.MONTH,Integer.parseInt(month));
            calendar.set(Calendar.DATE,Integer.parseInt(day));
        }else{
            calendar.set(Calendar.YEAR,1900);
            calendar.set(Calendar.MONTH,01);
            calendar.set(Calendar.DATE,01);
        }
        System.out.println(calendar);
        return  calendar;
    }
}