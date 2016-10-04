package com.toyo.vh.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.dao.KoujiMapper;
import com.toyo.vh.dao.ReportImageMapper;
import com.toyo.vh.dto.KenanForm;
import com.toyo.vh.dto.KoujiForm;
import com.toyo.vh.dto.SearchResultObject;
import com.toyo.vh.dto.ValveKikiSelectUtil;
import com.toyo.vh.entity.*;
import com.toyo.vh.service.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Lsr on 11/14/14.
 */

@Controller
@RequestMapping("/kouji")
public class KoujiController {

    @Autowired
    KoujiService koujiService;
    @Autowired
    ItemService itemService;
    @Autowired
    KoujirelationService koujirelationService;
    @Autowired
    TenkenRirekiService tenkenRirekiService;
    @Autowired
    KenanService kenanService;
    @Autowired
    ReportImageService reportImageService;
    @Autowired
    UserService userService;
    @Autowired
    LuceneIndexService luceneIndexService;
    @Autowired
    ReportimageKikiSystemService reportimageKikiSystemService;
    @Resource
    KoujiMapper koujiMapper;
    @Resource
    ReportImageMapper reportImageMapper;
    @Autowired
    LocationService locationService;
    @Autowired
    MasterService masterService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpSession session){

        return "/kouji/index";
    }


    /**
     * 工事IDにより、工事及び点検機器リストを取得
     * @param id 工事ID
     *
     * @return String 点検機器リストパス
     *
     * */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getKoujiById(@PathVariable("id")String id, ModelMap modelMap,HttpSession session) throws IOException {

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
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //kouji
        Kouji kouji=new Kouji();
        //点検結果保存リスト作成
        List<TenkenRirekiUtil> tenkenRirekiUtilList = new ArrayList<TenkenRirekiUtil>();

        Gson gson=new Gson();

        //kouji情報取得し、存在する場合、点検機器情報取得
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+id+"End");
        if(searchResultObjectsKJ.size()>0){
            Kouji tmpKouji=new Kouji();
            kouji=tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());

            //koujiIDから点検機器リスト取得
            List<SearchResultObject> searchRusultObjectsTKKJID =luceneIndexService.selectRecord(indexTenkenRireki, "TRKJID" + id + "End");

            if(!CollectionUtils.isEmpty(searchRusultObjectsTKKJID)){

                for(SearchResultObject searchResultObject:searchRusultObjectsTKKJID){
                    TenkenRirekiUtil tmpresult=new TenkenRirekiUtil();
                    TenkenRirekiUtil tenkenRirekiUtil=tmpresult.toTenkenRirekiUtil(searchResultObject.getBody());

                    boolean addFlg=true;

                    //点検関連データ取得
                    List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"KR"+tenkenRirekiUtil.getKoujirelationId()+"End");
                    if(searchResultObjectsKR.size()>0){
                       Koujirelation Tmpkoujirelation=new Koujirelation();
                       Koujirelation koujirelation=Tmpkoujirelation.toKoujirelation(searchResultObjectsKR.get(0).getBody());
                       tenkenRirekiUtil.setKoujirelation(koujirelation);

                       //弁取得
                       List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+koujirelation.getKikisysid()+"End");
                       if(searchResultObjectsVA.size()>0){
                           Valve TmpValve=new Valve();
                           Valve valve=TmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                           tenkenRirekiUtil.setValve(valve);
                       }else{
                           addFlg=false;
                       }
                       //kiki取得
                       List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+koujirelation.getKikiid()+"End");
                       if(searchResultObjectsKiki.size()>0){
                           Kiki TmpKiki=new Kiki();
                           Kiki kiki=TmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                           tenkenRirekiUtil.setKiki(kiki);
                       }else{
                           addFlg=false;
                       }
                        //buhin取得
                        List<Buhin> buhinList=new ArrayList<Buhin>();
                        List<SearchResultObject> searchResultObjectBuhins=luceneIndexService.selectRecord(indexBuhinFile,"KI"+koujirelation.getKikiid()+"End");
                        if(searchResultObjectBuhins.size()>0){
                            for(SearchResultObject searchResultObjectBuhin:searchResultObjectBuhins){
                                Buhin TmpBuhin=new Buhin();
                                Buhin buhin=TmpBuhin.toBuhin(searchResultObjectBuhin.getBody());
                                //20160224 工事点検リストDL時、全部品DLするように指摘されたから、修正しました。
//                                if((buhin.getBuhinMei().contains(Config.BuhinBunRuiA)||buhin.getBuhinMei().contains(Config.BuhinBunRuiB))){
//                                    buhinList.add(buhin);
//                                }
                                buhinList.add(buhin);
                            }
                        }
                        tenkenRirekiUtil.setBuhin(buhinList);
                   }
                    //kouji取得
                    tenkenRirekiUtil.setKouji(kouji);
                    if(addFlg){
                        tenkenRirekiUtilList.add(tenkenRirekiUtil);
                    }
            }
        }
        }

        List<Valve> valveList=getValvesFromKoujiId(id,indexValveFile,indexKoujirelationFile);

        // 弁番号で valveList 昇順ソート
        Collections.sort(valveList,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });

        // 弁番号で tenkenRirekiUtilList 昇順ソート
        Collections.sort(tenkenRirekiUtilList, new Comparator<TenkenRirekiUtil>() {
            public int compare(TenkenRirekiUtil o1, TenkenRirekiUtil o2) {
                //弁番号で
                int comp = StringUtil.compareString(o1.getValve().getvNo(), o2.getValve().getvNo());
                if (comp != 0) {
                    return comp;
                }
                //機器分類で
                comp = StringUtil.compareString(Config.kikiBunruiMap.get(o1.getKiki().getKikiBunrui()), Config.kikiBunruiMap.get(o2.getKiki().getKikiBunrui()));
                if (comp != 0) {
                    return comp;
                }
                return 0;
            }
        });

        modelMap.addAttribute("valveList",valveList);
        modelMap.addAttribute("valveSize",valveList.size());
        session.setAttribute("valveList",valveList);
        //make cache
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("kouji", kouji);
        modelMap.addAttribute("kouji", kouji);
        return "/kouji/index";
    }

    /**
     * 工事検索
     * @param keyword 検索キーワード
     *
     * @return String 取得した工事リスト
     *
     * */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchKoujiByKoujiMei(@ModelAttribute("keyword")String keyword, ModelMap modelMap,HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        List<Kouji> searchKoujiList =new ArrayList<Kouji>();
        List<TenkenRirekiUtil> tenkenRirekiList=null;

        //lucene初期化

        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));

        if(user == null){
            return "login";
        } else {
            //lucene検索　kouji取得
            List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,keyword);
            System.out.println("kouji 件数="+searchResultObjectsKJ.size());
            if(searchResultObjectsKJ.size()>0){
                for(SearchResultObject searchResultObject:searchResultObjectsKJ){
                    Kouji tmpKouji=new Kouji();
                    Kouji kouji=tmpKouji.toKouji(searchResultObject.getBody());

                    if(user.getKengen().equals("6")||user.getKengen().equals("5")){
                        searchKoujiList.add(kouji);
                    } else {
                        if(kouji.getPerson().equals(user.getUserid())){
                            searchKoujiList.add(kouji);
                        }
                    }
                }
            }

            modelMap.addAttribute("searchKoujiList",searchKoujiList);
            modelMap.addAttribute("tenkenRirekiHistory",tenkenRirekiList);

            //get location
            List<Location> locationList = locationService.getAllLocation();
            List<String> nameList = new LinkedList<String>();
            for (int i = 0; i < locationList.size(); i++) {
                String tmpLocation= StringUtil.concatWithDelimit(" ", locationList.get(i).getkCodeL(), locationList.get(i).getkCodeM(), locationList.get(i).getkCodeS());
                if(!nameList.contains(tmpLocation)){
                    nameList.add(tmpLocation);
                }
            }
            modelMap.addAttribute("nameList",nameList);
            session.setAttribute("locationList",locationList);
            modelMap.addAttribute("locationList", locationList);
            return "/list/kouji";
        }

    }

    /**
     * 工事削除し、工事リスト画面へ戻る
     * @param id 工事ID
     *
     * @return String 工事リスト画面パス
     *
     * */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String DeleteKoujiById(@PathVariable("id")String id,ModelMap modelMap,HttpSession session) throws IOException {
        Kouji kouji=(Kouji) session.getAttribute("kouji");
        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        User user = (User) session.getAttribute("user");
        modelMap.addAttribute("user", user);

        //DBから削除
        kouji.setDelFlgkouji("1");//削除フラグを1にする
        koujiService.deleteKouji(kouji);
        koujirelationService.deleteKoujirelationByKoujiid(id);
        tenkenRirekiService.deleteTenkenRirekiByKoujiid(id);

        //session更新
        List<Kouji> koujis=(List<Kouji>)session.getAttribute("locationKoujiSelectedForKouji");
        List<Kouji> koujisNew=new ArrayList<Kouji>();
        for(int i=0;i<koujis.size();i++){
            if(id.equals(koujis.get(i).getId()+"")){
            }else{
                koujisNew.add(koujis.get(i));
            }
        }
        session.setAttribute("locationKoujiSelectedForKouji",koujisNew);
        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));
         //koujiデータ削除
        luceneIndexService.deleteRecord(indexKoujiFile, "KJ" + kouji.getId() + "End");
        //点検データ削除
        if(tenkenRirekiUtilList.size()>0){
           for(TenkenRirekiUtil tenkenRirekiUtil:tenkenRirekiUtilList){
               luceneIndexService.deleteRecord(indexTenkenRireki, "TR" + tenkenRirekiUtil.getId() + "End");
           }
        }
        //点検関係データ削除
        List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+id+"End");
        if(searchResultObjectsKR.size()>0) {
            for(SearchResultObject searchResultObject:searchResultObjectsKR){
                Koujirelation Tmpkoujirelation = new Koujirelation();
                Koujirelation koujirelation = Tmpkoujirelation.toKoujirelation(searchResultObject.getBody());
                luceneIndexService.deleteRecord(indexKoujirelationFile, "KR" + koujirelation.getId() + "End");
            }
        }

        //cacheから削除
        session.setAttribute("tenkenRirekiUtilList",null);
        return "redirect:/";
    }



    /**
     * 工事IDにより、印刷画面へ
     * @param id 工事ID
     *
     * @return String 印刷画面パス
     *
     * */
    @RequestMapping(value = "/{id}/printhtml", method = RequestMethod.GET)
    public String getPrinthtml(@PathVariable("id")String id, ModelMap modelMap, HttpSession session){
        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");

        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //弁IDで唯一にする
        Map<String,TenkenRirekiUtil> printTarget=PrintController.toConvertValveMap(tenkenRirekiUtilList);
        List<TenkenRirekiUtil> tenkenValveList=PrintController.MapConvertToList(printTarget);

        // 弁番号で 昇順ソート
        Collections.sort(tenkenValveList,
                new Comparator<TenkenRirekiUtil>() {
                    @Override
                    public int compare(TenkenRirekiUtil entry1,
                                       TenkenRirekiUtil entry2) {
                        return ( entry1.getValve().getvNo())
                                .compareTo(entry2.getValve().getvNo());
                    }
                });

        //make cache
        session.setAttribute("tenkenValveList",tenkenValveList);
        modelMap.addAttribute("tenkenValveList", tenkenValveList);
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute("tenkenRirekiHistory",tenkenRirekiUtilList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);
        modelMap.addAttribute("tenkenRirekiHistory", tenkenRirekiUtilList);
        modelMap.addAttribute("kouji", kouji);
        session.setAttribute("printMessage","");
        return "kouji/print";
    }

    /**
     * 工事IDにより、GP&ICS印刷画面へ
     * @param id 工事ID
     *
     * @return String GP&ICS印刷画面パス
     *
     * */
    @RequestMapping(value = "/{id}/gpPrinthtml", method = RequestMethod.GET)
    public String getGpPrinthtml(@PathVariable("id")String id, ModelMap modelMap, HttpSession session){
        Kouji kouji=(Kouji) session.getAttribute("kouji");
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        if(kouji==null){
            kouji=koujiService.getKoujiById(id);
        }
        //弁IDで唯一にする
        Map<String,TenkenRirekiUtil> printTarget=PrintController.toConvertValveMap(tenkenRirekiUtilList);
        List<TenkenRirekiUtil> tenkenValveList=PrintController.MapConvertToList(printTarget);

        // 弁番号で 昇順ソート
        Collections.sort(tenkenValveList,
                new Comparator<TenkenRirekiUtil>() {
                    @Override
                    public int compare(TenkenRirekiUtil entry1,
                                       TenkenRirekiUtil entry2) {
                        return ( entry1.getValve().getvNo())
                                .compareTo(entry2.getValve().getvNo());
                    }
                });

        //make cache
        session.setAttribute("tenkenValveList",tenkenValveList);
        modelMap.addAttribute("tenkenValveList", tenkenValveList);
        session.setAttribute("kouji",kouji);
        modelMap.addAttribute("kouji", kouji);
        return "kouji/gpIcs";
    }

    /**
     * 工事IDにより、点検履歴画面へ
     * @param id 工事ID
     *
     * @return String 点検履歴画面パス
     *
     * */
    @RequestMapping(value = "/{id}/history", method = RequestMethod.GET)
    public String getHistoryById(@PathVariable("id")String id, ModelMap modelMap, HttpSession session){
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

        return "/kouji/history";
    }

    /**
     * 工事IDにより、今回の懸案画面へ
     * @param id 工事ID
     *
     * @return String 懸案画面パス
     *
     * */
    @RequestMapping(value = "/{id}/kenan", method = RequestMethod.GET)
    public String getKenanByKoujiId(@PathVariable("id")String id, ModelMap modelMap, HttpSession session) throws IOException {
//        //点検履歴保存
//        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
//        //make cache
//        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
//        modelMap.addAttribute("tenkenRirekiUtilList", tenkenRirekiUtilList);

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
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //kouji 追加
        Kouji kouji = new Kouji();
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+id+"End");
        if(searchResultObjectsKJ.size()>0) {
            Kouji tmpKouji = new Kouji();
            kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
        }
        modelMap.addAttribute("kouji",kouji);
        session.setAttribute("kouji",kouji);


        //**************************This Year********************************
        List<Valve> valvesThisYear=new ArrayList<Valve>();
        Map<String,Valve> valveMapThisYear=new HashMap<String, Valve>();

        //工事IDから懸案がある弁のリストを取得 ユニークため、Mapに保存する
        List<SearchResultObject> searchResultObjectsKoujiIdValves=luceneIndexService.selectRecord(indexKenan,"KJ"+id+"End");
        if(searchResultObjectsKoujiIdValves.size()>0) {
            for (SearchResultObject searchResultObject1 : searchResultObjectsKoujiIdValves) {
                //kenan
                Kenan tmpKenan = new Kenan();
                Kenan kenan = tmpKenan.toKenan(searchResultObject1.getBody());

                List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+kenan.getKikisysId()+"End");
                if(searchResultObjectsVA.size()>0){
                    Valve tmpValve=new Valve();
                    Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                    valveMapThisYear.put(String.valueOf(valve.getKikiSysId()),valve);
                }
            }
        }
        //Mapからリストに変換する
        for (String key : valveMapThisYear.keySet()) {
            Valve valve=valveMapThisYear.get(key);
            valvesThisYear.add(valve);
        }

        // 弁番号で 昇順ソート
        Collections.sort(valvesThisYear,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });

        session.setAttribute("valvesThisYear",valvesThisYear);
        modelMap.addAttribute("valvesThisYear",valvesThisYear);

        return "/kouji/kenan";
    }

    /**
     * 工事IDにより、今回の懸案画面へ
     * @param id 工事ID
     *
     * @return String 懸案画面パス
     *
     * */
    @RequestMapping(value = "/{id}/kenanPast", method = RequestMethod.GET)
    public String getKenanPast(@PathVariable("id")String id, ModelMap modelMap, HttpSession session) throws IOException {

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
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //kouji 追加
        Kouji kouji = new Kouji();
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+id+"End");
        if(searchResultObjectsKJ.size()>0) {
            Kouji tmpKouji = new Kouji();
            kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
        }
        modelMap.addAttribute("kouji",kouji);
        session.setAttribute("kouji",kouji);


        //**************************Last Year********************************
        List<Valve> valvesLastYear=new ArrayList<Valve>();
        Map<String,Valve> valveMapLastYear=new HashMap<String, Valve>();

        //工事IDから所属された弁のリストを取得 ユニークため、Mapに保存する
        List<SearchResultObject> searchResultObjectsAllValves=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+id+"End");
        if(searchResultObjectsAllValves.size()>0) {
            for (SearchResultObject searchResultObject1 : searchResultObjectsAllValves) {
                //koujiRelation
                Koujirelation TmpKoujirelation=new Koujirelation();
                Koujirelation koujirelation=TmpKoujirelation.toKoujirelation(searchResultObject1.getBody());

                List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+koujirelation.getKikisysid()+"End");
                if(searchResultObjectsVA.size()>0){
                    Valve tmpValve=new Valve();
                    Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                    valveMapLastYear.put(String.valueOf(valve.getKikiSysId()),valve);
                }
            }
        }
        //Mapからリストに変換する
        for (String key : valveMapLastYear.keySet()) {
            Valve valve=valveMapLastYear.get(key);
            //懸案があるかどうかチェックする
            List<SearchResultObject> searchResultObjectsKenans=luceneIndexService.selectRecord(indexKenan,"VA"+valve.kikiSysId+"End");
            if(searchResultObjectsKenans.size()>0) {
                for (SearchResultObject searchResultObject1 : searchResultObjectsKenans) {
                    //kenan
                    Kenan tmpKenan = new Kenan();
                    Kenan kenan = tmpKenan.toKenan(searchResultObject1.getBody());

                    if((!id.equals(kenan.getKoujiId()+""))&&((!"対応".equals(kenan.getTaiouFlg())))){
                        valvesLastYear.add(valve);
                        break;
                    }
                }
            }
        }
        // 弁番号で 昇順ソート
        Collections.sort(valvesLastYear,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });
        session.setAttribute("valvesLastYear",valvesLastYear);
        modelMap.addAttribute("valvesLastYear",valvesLastYear);

        return "/kouji/kenanPast";
    }

    /**
     * 弁IDにより、懸案リストを取得
     * @param kikiSysId 弁ID
     * @param koujiId 工事Id
     *
     * @return String 懸案リスト
     *
     * */
    @RequestMapping(value = "/kenanLastYear", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kenanLastYear(@RequestParam("koujiId")String koujiId,@RequestParam("kikiSysId")String kikiSysId, ModelMap modelMap, HttpSession session) throws IOException {

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
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //kouji 追加
        Kouji kouji = new Kouji();
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+koujiId+"End");
        if(searchResultObjectsKJ.size()>0) {
            Kouji tmpKouji = new Kouji();
            kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
        }
        modelMap.addAttribute("kouji",kouji);
        session.setAttribute("kouji",kouji);


        //ValvcIdから過去懸案リストを取得する
        List<Kenan> kenanListForLastYear=new ArrayList<Kenan>();
        List<KenanForm> kenanFormList = new ArrayList<KenanForm>();

        //工事IDから懸案がある弁のリストを取得 ユニークため、Mapに保存する
        List<SearchResultObject> searchResultObjectsKenans=luceneIndexService.selectRecord(indexKenan,"VA"+kikiSysId+"End");
        if(searchResultObjectsKenans.size()>0) {
            for (SearchResultObject searchResultObject1 : searchResultObjectsKenans) {
                //kenan
                Kenan tmpKenan = new Kenan();
                Kenan kenan = tmpKenan.toKenan(searchResultObject1.getBody());

                KenanForm kenanForm = new KenanForm();
                KenanForm TmpkenanForm = new KenanForm();

                kenanForm = TmpkenanForm.copyKenanForm(kenan);

                if((!koujiId.equals(kenan.getKoujiId()+""))&&(!"対応".equals(kenanForm.getTaiouFlg()))){

                    //kiki追加
                    List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+kenan.getKikiId()+"End");
                    if(searchResultObjectsKiki.size()>0){
                        Kiki TmpKiki=new Kiki();
                        Kiki kiki=TmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                        kenanForm.setKiki(kiki);
                    }
                    //valve追加
                    List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+kenan.getKikisysId()+"End");
                    if(searchResultObjectsVA.size()>0){
                        Valve tmpValve=new Valve();
                        Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                        kenanForm.setValve(valve);
                    }

                    kenanListForLastYear.add(kenan);
                    kenanFormList.add(kenanForm);
                }
            }
        }

//        session.setAttribute("kenanFormList",kenanFormList);
//        modelMap.addAttribute("kenanFormList",kenanFormList);
        Gson gson = new Gson();

        return gson.toJson(kenanFormList);
    }

    /**
     * 弁IDにより、懸案リストを取得
     * @param kikiSysId 弁ID
     * @param koujiId 工事Id
     *
     * @return String 懸案リスト
     *
     * */
    @RequestMapping(value = "/kenanNowYear", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kenanNowYear(@RequestParam("koujiId")String koujiId,@RequestParam("kikiSysId")String kikiSysId, ModelMap modelMap, HttpSession session) throws IOException {

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
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //kouji 追加
        Kouji kouji = new Kouji();
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+koujiId+"End");
        if(searchResultObjectsKJ.size()>0) {
            Kouji tmpKouji = new Kouji();
            kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
        }
        modelMap.addAttribute("kouji",kouji);
        session.setAttribute("kouji",kouji);


        //ValvcIdから過去懸案リストを取得する
        List<Kenan> kenanListForNowYear=new ArrayList<Kenan>();
        List<KenanForm> kenanFormList = new ArrayList<KenanForm>();

        //工事IDから懸案がある弁のリストを取得 ユニークため、Mapに保存する
        List<SearchResultObject> searchResultObjectsKenans=luceneIndexService.selectRecord(indexKenan,"VA"+kikiSysId+"End");
        if(searchResultObjectsKenans.size()>0) {
            for (SearchResultObject searchResultObject1 : searchResultObjectsKenans) {
                //kenan
                Kenan tmpKenan = new Kenan();
                Kenan kenan = tmpKenan.toKenan(searchResultObject1.getBody());

                KenanForm kenanForm = new KenanForm();
                KenanForm TmpkenanForm = new KenanForm();

                kenanForm = TmpkenanForm.copyKenanForm(kenan);

                if(koujiId.equals(kenan.getKoujiId()+"")){

                    //kiki追加
                    List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+kenan.getKikiId()+"End");
                    if(searchResultObjectsKiki.size()>0){
                        Kiki TmpKiki=new Kiki();
                        Kiki kiki=TmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                        kenanForm.setKiki(kiki);
                    }
                    //valve追加
                    List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+kenan.getKikisysId()+"End");
                    if(searchResultObjectsVA.size()>0){
                        Valve tmpValve=new Valve();
                        Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                        kenanForm.setValve(valve);
                    }

                    kenanListForNowYear.add(kenan);
                    kenanFormList.add(kenanForm);
                }
            }
        }

        Gson gson = new Gson();

        return gson.toJson(kenanFormList);
    }


    /**
     * 工事IDにより、画像画面へ
     * @param id 工事ID
     *
     * @return String 画像画面パス
     *
     * */
    @RequestMapping(value = "/{id}/image", method = RequestMethod.GET)
    public String image(@PathVariable("id")String id,
                        ModelMap modelMap,
                        HttpSession session){
        Kouji kouji = koujiService.getKoujiById(id);
        List<ReportImage> allReportImageList = reportImageService.getReportImageByKoujiId(id);
        List<ReportimageKikiSystem> reportimageKikiSystemList = reportimageKikiSystemService.getListByKoujiId(id);


        int page = 0;
        int totalPage = allReportImageList.size();
        int set = 0;
        int totalSet = 0;
//        if(totalPage != 0){
//
//            page = 1;
//            set = 1;
//            totalSet = totalPage/6;
//            if(totalPage%6 != 0){
//                totalSet = totalSet + 1;
//            }
//
//            //get reportimage list
//            List<ReportImage> reportImageList = new ArrayList<ReportImage>();
//            int pageInterval = 6;
//            int num = 0;
//            for (int i = (set - 1) * 6; i < allReportImageList.size(); i++, num++) {
//                if(num == pageInterval){
//                    break;
//                }
//                reportImageList.add(allReportImageList.get(i));
//            }
//            modelMap.addAttribute("imageList",reportImageList);
//
//        }

        List<Valve> valveList = (List<Valve>)session.getAttribute("valveList");
        if(CollectionUtils.isEmpty(valveList)){
            valveList = itemService.getKikisysByKoujiId(id);
        }

        List<Valve> firstValveImageList = new ArrayList<Valve>();
        if(allReportImageList.size() > 0) {
            modelMap.addAttribute("firstReportImage", allReportImageList.get(0));
            set=1;
            totalSet=allReportImageList.size();
            //imageの関連弁追加
            List<ReportimageKikiSystem> allRKList = reportimageKikiSystemService.getListByReportimageKikiSystem(id,allReportImageList.get(0).getImagename());
            for (int i = 0; i < allRKList.size(); i++) {
                for (int j = 0; j < valveList.size(); j++) {
                    if (valveList.get(j).getKikiSysId() == allRKList.get(i).getKikiSysId()) {
                        firstValveImageList.add(valveList.get(j));
                    }
                }
            }
            // 弁番号で 昇順ソート
            Collections.sort(firstValveImageList,
                    new Comparator<Valve>() {
                        @Override
                        public int compare(Valve entry1,
                                           Valve entry2) {
                            return (entry1.getvNo())
                                    .compareTo(entry2.getvNo());
                        }
                    });

        }




        session.setAttribute("reportimageKikiSystemList",reportimageKikiSystemList);
        session.setAttribute("reportImageList",allReportImageList);
        session.setAttribute("currentPage", page);
        session.setAttribute("totalPage", totalPage);
        session.setAttribute("currentSet", set);
        session.setAttribute("totalSet", totalSet);
        session.setAttribute("valveList",valveList);
        session.setAttribute("firstValveImageList",firstValveImageList);
        modelMap.addAttribute("kouji",kouji);
        modelMap.addAttribute("valveList",valveList);

        return "/kouji/image";
    }


    /**
     * 工事に弁を追加,追加された弁リストに戻る
     *
     * @param id 工事ID
     * @param idList 弁IDリスト
     *
     * @return String 弁追加画面パス
     *
     * */
    @RequestMapping(value = "/{id}/valve", method = RequestMethod.POST)
    public String addValveList(@PathVariable("id")String id,
                               @RequestParam("idList")String idList,
                               ModelMap modelMap,HttpSession session) throws IOException {
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
        Directory indexKenanFile = FSDirectory.open(new File(indexPath.get("indexKenan")));
        Directory indexTenkenrirekiFile = FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //kouji 取得
        Kouji kouji = new Kouji();
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+id+"End");
        if(searchResultObjectsKJ.size()>0) {
            Kouji tmpKouji = new Kouji();
            kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
        }
        modelMap.addAttribute("kouji",kouji);
        session.setAttribute("kouji",kouji);


        HashSet<String> allIdSet = new HashSet<String>();
        HashSet<String> newIdSet = new HashSet<String>();
        //add
        List<Valve> valveList = new ArrayList<Valve>();
        if(idList.length()>0){
            String[] vids = idList.split(",");
            if(vids.length>0){
                for(int i = 0;i<vids.length;i++){
                    newIdSet.add(vids[i]); // add to compare set
                    Koujirelation koujirelation = new Koujirelation();
                    koujirelation.setKoujiid(Integer.valueOf(id));
                    koujirelation.setKikisysid(Integer.valueOf(vids[i]));
                    koujirelation.setKikiid(0);
                    List<Koujirelation> tempKoujirelation=koujirelationService.getKoujirelationByKoujirelation(koujirelation);

                    //すでに追加したかどうかチェックする
                    if(tempKoujirelation.size()==0){
                        Koujirelation Addkoujirelation=koujirelationService.addKoujirelation(koujirelation);
                        luceneIndexService.insertRecord(indexKoujirelationFile,"KR"+Addkoujirelation.getId()+"End",Addkoujirelation.toText());
                    }
                }
            }
        }


        //KoujiIdからValve一覧取得
        valveList=getValvesFromKoujiId(id,indexValveFile,indexKoujirelationFile);
        for(Valve tmpvalve:valveList){
            allIdSet.add(tmpvalve.getKikiSysId()+"");
        }
//        List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+id+"End");
//        Gson gson=new Gson();
//        if(searchResultObjectsKR.size()>0){
//            for(int i=0;i<searchResultObjectsKR.size();i++){
//                Koujirelation TmpKoujirelation=new Koujirelation();
//                Koujirelation koujirelation=TmpKoujirelation.toKoujirelation(searchResultObjectsKR.get(i).getBody());
//
//                if(koujirelation.getKikiid()==0){
//                    List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+koujirelation.getKikisysid()+"End");
//                    if(searchResultObjectsVA.size()>0){
//                        Valve tmpValve=new Valve();
//                        Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
//                        valveList.add(valve);
//                        allIdSet.add(valve.getKikiSysId()+""); // add to compare set
//                    }
//                }
//            }
//        }

        //remove rest
        List<String> restList = new LinkedList<String>();
        for (String s : allIdSet) {
            if(!newIdSet.contains(s)){
                //remove
                Valve valve = itemService.getKikisysByKikisysId(s);
                //koujirelationid
                List<SearchResultObject> searchResultObjectsKJR=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+id+"End");

                List<Koujirelation> removeIdList = new LinkedList<Koujirelation>();
                for (int i = 0; i < searchResultObjectsKJR.size(); i++) {
                    Koujirelation tmpKoujirelation = new Koujirelation();
                    Koujirelation koujirelation = tmpKoujirelation.toKoujirelation(searchResultObjectsKJR.get(i).getBody());
                    if(koujirelation.getKikisysid() == Integer.valueOf(s)){
                        removeIdList.add(koujirelation);

                        //remove lucene kenan index
                        List<SearchResultObject> searchResultObjectsKA=luceneIndexService.selectRecord(indexKenanFile, "KR" + koujirelation.getId() + "End");
                        for (int j = 0; j < searchResultObjectsKA.size(); j++) {
                            Kenan tmpKenan = new Kenan();
                            Kenan kenan = tmpKenan.toKenan(searchResultObjectsKA.get(j).getBody());
                            luceneIndexService.deleteRecord(indexKenanFile,"KEN"+kenan.getId()+"End");
                            kenanService.deleteKenan(kenan);
                        }

                        //remove lucene rireki index
                        List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenrirekiFile, "KR" + koujirelation.getId() + "End");
                        for (int j = 0; j < searchResultObjectsTR.size(); j++) {
                            TenkenRireki tmpTR = new TenkenRireki();
                            TenkenRireki tr = tmpTR.toTenkenRireki(searchResultObjectsTR.get(j).getBody());
                            luceneIndexService.deleteRecord(indexTenkenrirekiFile,"TR"+tr.getId()+"End");
                            tenkenRirekiService.deleteTenkenRireki(tr);

                        }

                        //remove lucene koujirelation index
                        luceneIndexService.deleteRecord(indexKoujirelationFile,"KR"+koujirelation.getId()+"End");
                        koujirelationService.deleteKoujirelation(koujirelation);

                        //remove reportimage database
                        reportimageKikiSystemService.deleteByKoujiIdAndKikisysId(id,s);
//                        reportImageService.deleteReportImage();
                    }
                }

                Iterator<Valve> iterator = valveList.iterator();
                int removeIndex = -1;
                while(iterator.hasNext()){
                    Valve tmp = iterator.next();
                    if(tmp.getKikiSysId() == valve.getKikiSysId()){
                        removeIndex = valveList.indexOf(tmp);
                        break;
                    }
                }
                if(removeIndex > -1) {
                    valveList.remove(removeIndex);
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

        //session更新  削除された弁を削除する
        List<ValveKikiSelectUtil> valveKikiSelectUtilList = (List<ValveKikiSelectUtil>) session.getAttribute(kouji.getId()+"");
        List<ValveKikiSelectUtil> valveKikiSelectUtilListNew=new ArrayList<ValveKikiSelectUtil>();
        if(!CollectionUtils.isEmpty(valveKikiSelectUtilList)){
            for(int i = 0;i<valveKikiSelectUtilList.size();i++){
                if(!newIdSet.contains(valveKikiSelectUtilList.get(i).getKikiSysId())){
                }else{
                    valveKikiSelectUtilListNew.add(valveKikiSelectUtilList.get(i));
                }
            }
            session.setAttribute(kouji.getId()+"", valveKikiSelectUtilListNew);
        }




        String koujiValveSize=String.valueOf(valveList.size());
        session.setAttribute("koujiValveSize",koujiValveSize);
//        valveList=itemService.getKikisysByKoujiId(id);
        session.setAttribute("kouji",kouji);
        session.setAttribute("valveList",valveList);
        modelMap.addAttribute("kouji",kouji);
        modelMap.addAttribute("valveList",valveList);
        return "redirect:/kouji/"+kouji.getId()+"/kiki";
    }

    /**
     * 工事、バルブより、バルブと機器の関係を取得
     * */

    @RequestMapping(value = "/valveStatus", method = RequestMethod.GET, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String valveStatus(HttpSession session,
                              ModelMap modelMap){
        Kouji kouji = (Kouji)session.getAttribute("kouji");

        List<Valve> valveList = (List<Valve>)session.getAttribute("valveList");
        List<Valve> returnValve = new ArrayList<Valve>();
        for (int i = 0; i < valveList.size(); i++) {
            List<Integer> kikiIdList = koujirelationService.getKikiIdListByKoujiidAndKikisys(kouji.getId()+"",valveList.get(i).getKikiSysId()+"");
            if (kikiIdList.size()>1){
                returnValve.add(valveList.get(i));
            }
        }
        Gson gson = new Gson();
        return gson.toJson(returnValve);
    }
    /**
     * 点検機器の関係データをtenkenRelationテーブルに追加
     * @param id 工事ID
     *
     * @return String 機器追加画面パス
     *
     * */
    @RequestMapping(value = "/{id}/saveValveKikiRelation", method = RequestMethod.GET)
    public String saveValveKikiRelation(@PathVariable("id")String id,
                                        HttpSession session,
                                        ModelMap modelMap) throws IOException {
        //getFromSession
        List<ValveKikiSelectUtil> valveKikiSelectUtilList = (List<ValveKikiSelectUtil>) session.getAttribute(id);

        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        //すでに追加されたデータ
        List<SearchResultObject> searchResultObjectsKoujiRelation=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+id+"End");

        if(searchResultObjectsKoujiRelation.size()<=0){//存在してない場合
            for (int i = 0; i < valveKikiSelectUtilList.size(); i++) {
                if(valveKikiSelectUtilList.get(i).getStatus().equals("active")) {
                    Koujirelation newKoujirelation=new Koujirelation();
                    newKoujirelation.setKikiid(Integer.valueOf(valveKikiSelectUtilList.get(i).getKiki().getKikiId()));
                    newKoujirelation.setKikisysid(Integer.valueOf(valveKikiSelectUtilList.get(i).getKikiSysId()));
                    newKoujirelation.setKoujiid(Integer.parseInt(id));

                    newKoujirelation=koujirelationService.addKoujirelation(newKoujirelation);
                    luceneIndexService.insertRecord(indexKoujirelationFile, "KR" + newKoujirelation.getId() + "End", newKoujirelation.toText());
                }
            }
        }else{
            //koujirelationに該工事データがある場合、存在するかどうかチェック必要
            if(!CollectionUtils.isEmpty(valveKikiSelectUtilList)) {
                for (int i = 0; i < valveKikiSelectUtilList.size(); i++) {
                    if (valveKikiSelectUtilList.get(i).getStatus() == null) {
                        valveKikiSelectUtilList.get(i).setStatus("");
                    }
                    if (valveKikiSelectUtilList.get(i).getStatus().equals("active")) {
                        //選択された機器、工事点検関係表にある場合、何もしない、ない場合は、追加する
                        boolean isHave = false;
                        //すでに追加されたかどうかチェック
                        for (SearchResultObject searchResultObject : searchResultObjectsKoujiRelation) {
                            Koujirelation tmpkoujirelation = new Koujirelation();
                            Koujirelation koujirelation = tmpkoujirelation.toKoujirelation(searchResultObject.getBody());
                            //存在する場合、次のvalveKikiSelectUtilListを判断する
                            if (((String.valueOf(koujirelation.getKikisysid()).equals(valveKikiSelectUtilList.get(i).getKikiSysId()))
                                    && (String.valueOf(koujirelation.getKikiid()).equals(String.valueOf(valveKikiSelectUtilList.get(i).getKiki().getKikiId()))))) {
                                isHave = true;
                                break;
                            }
                        }
                        if (!isHave) {
                            Koujirelation newKoujirelation = new Koujirelation();
                            newKoujirelation.setKikiid(Integer.valueOf(valveKikiSelectUtilList.get(i).getKiki().getKikiId()));
                            newKoujirelation.setKikisysid(Integer.valueOf(valveKikiSelectUtilList.get(i).getKikiSysId()));
                            newKoujirelation.setKoujiid(Integer.parseInt(id));

                            newKoujirelation = koujirelationService.addKoujirelation(newKoujirelation);
                            luceneIndexService.insertRecord(indexKoujirelationFile, "KR" + newKoujirelation.getId() + "End", newKoujirelation.toText());
                        }
                    }else{
                        //選択されない機器、工事点検関係表にある場合、削除する、ない場合は、なにもしない。
                        for (SearchResultObject searchResultObject : searchResultObjectsKoujiRelation) {
                            Koujirelation tmpkoujirelation = new Koujirelation();
                            Koujirelation koujirelation = tmpkoujirelation.toKoujirelation(searchResultObject.getBody());
                            //存在する場合、削除する
                            if (((String.valueOf(koujirelation.getKikisysid()).equals(valveKikiSelectUtilList.get(i).getKikiSysId()))
                                    && (String.valueOf(koujirelation.getKikiid()).equals(String.valueOf(valveKikiSelectUtilList.get(i).getKiki().getKikiId()))))) {
                                //既存データを削除する
                                //点検機器を削除する
                                TenkenRirekiUtil tenkenRirekiUtil=tenkenRirekiService.getTenkenRirekiByKoujirelationId(koujirelation.getId() + "");
                                TenkenRireki tenkenRireki=new TenkenRireki();
                                tenkenRireki.setId(tenkenRirekiUtil.getId());
                                luceneIndexService.deleteRecord(indexTenkenRireki, "TR" + tenkenRirekiUtil.getId() + "End");
                                tenkenRirekiService.deleteTenkenRireki(tenkenRireki);
                                //点検関係表を削除する
                                luceneIndexService.deleteRecord(indexKoujirelationFile,"KR"+koujirelation.getId()+"End");
                                koujirelationService.deleteKoujirelation(koujirelation);
                            }
                        }
                    }
                }
            }
        }

        //IDから工事取得
        Kouji kouji=new Kouji();
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+id+"End");
        if(searchResultObjectsKJ.size()>0){
            Kouji tmpkouji=new Kouji();
            kouji=tmpkouji.toKouji(searchResultObjectsKJ.get(0).getBody());
        }

        //KoujiIdからValve一覧取得
        List<Valve> valveList=new ArrayList<Valve>();
        valveList=getValvesFromKoujiId(id,indexValveFile,indexKoujirelationFile);
//        List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+id+"End");
//        if(searchResultObjectsKR.size()>0){
//            for(int i=0;i<searchResultObjectsKR.size();i++){
//                Koujirelation TmpKoujirelation=new Koujirelation();
//                Koujirelation koujirelation=TmpKoujirelation.toKoujirelation(searchResultObjectsKR.get(i).getBody());
//
//                if(koujirelation.getKikiid()==0){
//                    List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+koujirelation.getKikisysid()+"End");
//                    if(searchResultObjectsVA.size()>0){
//                        Valve tmpValve=new Valve();
//                        Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
//                        valveList.add(valve);
//                    }
//                }
//            }
//        }

        //tenkenRirekiテーブルに点検機器追加
        List<SearchResultObject> searchResultObjectsKoujiRelationFortenkenRireki=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+kouji.getId()+"End");
        if(searchResultObjectsKoujiRelationFortenkenRireki.size()>0) {
            for (SearchResultObject searchResultObject : searchResultObjectsKoujiRelationFortenkenRireki) {
                Koujirelation tmpkoujirelation = new Koujirelation();
                Koujirelation koujirelation = tmpkoujirelation.toKoujirelation(searchResultObject.getBody());
                if(koujirelation.getKikiid() > 0){
                    //tenkenRirekiに追加したかチェック
                    List<SearchResultObject> searchResultObjectsTenkenRirekis=luceneIndexService.selectRecord(indexTenkenRireki,"KR"+koujirelation.getId()+"End");
                    if(searchResultObjectsTenkenRirekis.size()>0){
                    }else{
                        TenkenRireki newtenkenRireki = new TenkenRireki();
                        newtenkenRireki.setKoujiId(kouji.getId());
                        newtenkenRireki.setKikiId(koujirelation.getKikiid());
                        newtenkenRireki.setKoujirelationId(koujirelation.getId());
                        newtenkenRireki.setTenkenkekka("");
                        newtenkenRireki.setTenkenRank("");
                        newtenkenRireki.setTenkennaiyo("");
                        newtenkenRireki.setTenkenBikou("");
                        newtenkenRireki=tenkenRirekiService.addTenkenRireki(newtenkenRireki);
                        luceneIndexService.updateRecord(indexTenkenRireki, "TR" + newtenkenRireki.getId() + "End", newtenkenRireki.toText());
                    }

                }
            }
        }

        session.setAttribute("kouji",kouji);
        session.setAttribute("valveList",valveList);
        modelMap.addAttribute("kouji", kouji);
        modelMap.addAttribute("valveList",valveList);
        return "redirect:/kouji/"+kouji.getId();
//        return "kouji/addResult";
    }

    /**
     * 点検機器をtenkenRirekiテーブルに追加し、
     * 点検リスト画面へ遷移
     * @param koujiId 工事ID
     *
     * @return String 点検リスト画面パス
     *
     * */
    @RequestMapping(value = "/{id}/saveResult", method = RequestMethod.GET)
    public String saveResult(@PathVariable("id")String koujiId,
                             HttpSession session,
                             ModelMap modelMap) throws IOException {

        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //koujiIdから　関係取得
        List<Koujirelation> koujirelationList = new ArrayList<Koujirelation>();
        List<SearchResultObject> searchResultObjectsKoujiRelation=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+koujiId+"End");
        if(searchResultObjectsKoujiRelation.size()>0) {
            for (SearchResultObject searchResultObject : searchResultObjectsKoujiRelation) {
                Koujirelation tmpkoujirelation = new Koujirelation();
                Koujirelation koujirelation = tmpkoujirelation.toKoujirelation(searchResultObject.getBody());
                if(koujirelation.getKikiid() > 0){
                    //tenkenRirekiに追加したかチェック
                    List<SearchResultObject> searchResultObjectsTenkenRirekis=luceneIndexService.selectRecord(indexTenkenRireki,"KR"+koujirelation.getId()+"End");
                    if(searchResultObjectsTenkenRirekis.size()>0){
                    }else{
                        TenkenRireki newtenkenRireki = new TenkenRireki();
                        newtenkenRireki.setKoujiId(Integer.valueOf(koujiId));
                        newtenkenRireki.setKikiId(koujirelation.getKikiid());
                        newtenkenRireki.setKoujirelationId(koujirelation.getId());
                        newtenkenRireki.setTenkenkekka("");
                        newtenkenRireki.setTenkenRank("");
                        newtenkenRireki.setTenkennaiyo("");
                        newtenkenRireki.setTenkenBikou("");
                        newtenkenRireki=tenkenRirekiService.addTenkenRireki(newtenkenRireki);
                        luceneIndexService.updateRecord(indexTenkenRireki, "TR" + newtenkenRireki.getId() + "End", newtenkenRireki.toText());
                    }

                }
            }
        }
        session.removeAttribute(koujiId);
        return "redirect:/kouji/"+koujiId;
    }

    /**
     * 工事情報を更新し、点検指示設定画面へ
     * @param koujiForm 工事
     *
     * @return String 点検指示設定画面パス
     *
     * */
    @RequestMapping(value = "/instruct/updateKouji", method = RequestMethod.POST)
    public String updateKouji(@ModelAttribute("KoujiForm")KoujiForm koujiForm,ModelMap modelMap,HttpSession session) throws IOException {
        //kouji　更新
        koujiService.updateKouji(koujiForm);
        Kouji kouji=koujiService.getKoujiById(Integer.toString(koujiForm.getId()));
        session.setAttribute("kouji",kouji);
        //images bikouに工事名を更新

        reportImageService.updateReportImageBikouByKoujiId(koujiForm.getId(),koujiForm.getKjMeisyo());

        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));

        luceneIndexService.updateRecord(indexKoujiFile, "KJ" + kouji.getId() + "End", kouji.toText());
        //session 更新
        //工事検索リスト更新
        List<Kouji> koujiresult=(List<Kouji>)session.getAttribute("locationKoujiSelectedForKouji");
        List<Kouji> tmpKoujiresult=new ArrayList<Kouji>();
        if(!koujiresult.isEmpty()){
            for(int i=0;i<koujiresult.size();i++){
                if(koujiresult.get(i).getId()==kouji.getId()){
                    tmpKoujiresult.add(kouji);
                }else{
                    tmpKoujiresult.add(koujiresult.get(i));
                }
            }
        }
        session.setAttribute("locationKoujiSelectedForKouji",tmpKoujiresult);

        return "redirect:/kouji/"+koujiForm.getId()+"/instruct";
    }

    /**
     * 工事情報を更新し、点検指示設定画面へ
     * @param koujiForm 工事
     *
     * @return String 点検指示設定画面パス
     *
     * */
    @RequestMapping(value = "/instruct/updateKoujiJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateKoujiJson(@ModelAttribute("koujiForm")String koujiForm,ModelMap modelMap,HttpSession session) throws IOException {
        //koujiFormからkoujiに
        Gson gson = new Gson();
        List<String> koujiData = gson.fromJson(koujiForm, new TypeToken<List<String>>(){}.getType());
        Kouji tmpkouji=new Kouji();
        tmpkouji.setId(Integer.valueOf(koujiData.get(0)));
        tmpkouji.setKjNo(koujiData.get(1));
        tmpkouji.setKjMeisyo(koujiData.get(2));
        tmpkouji.setKjKbn(koujiData.get(8));
        tmpkouji.setBgnYmd(koujiData.get(4));
        tmpkouji.setEndYmd(koujiData.get(5));
        tmpkouji.setNextYmd(koujiData.get(9));
        tmpkouji.setNendo(koujiData.get(6));
        tmpkouji.setSyukan(koujiData.get(10));
        tmpkouji.setGyosyaRyakuA(koujiData.get(11));
        tmpkouji.setLocation(koujiData.get(7));
        tmpkouji.setStatus(koujiData.get(12));
        tmpkouji.setPerson(koujiData.get(3));

        //kouji　更新
        koujiService.updateKouji(tmpkouji);
        Kouji kouji=koujiService.getKoujiById(Integer.toString(tmpkouji.getId()));
        session.setAttribute("kouji",kouji);
        //images bikouに工事名を更新

        reportImageService.updateReportImageBikouByKoujiId(tmpkouji.getId(),tmpkouji.getKjMeisyo());

        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));

        luceneIndexService.updateRecord(indexKoujiFile, "KJ" + kouji.getId() + "End", kouji.toText());
        //session 更新
        //工事検索リスト更新
        List<Kouji> koujiresult=(List<Kouji>)session.getAttribute("locationKoujiSelectedForKouji");
        List<Kouji> tmpKoujiresult=new ArrayList<Kouji>();
        if(!koujiresult.isEmpty()){
            for(int i=0;i<koujiresult.size();i++){
                if(koujiresult.get(i).getId()==kouji.getId()){
                    tmpKoujiresult.add(kouji);
                }else{
                    tmpKoujiresult.add(koujiresult.get(i));
                }
            }
        }
        session.setAttribute("locationKoujiSelectedForKouji",tmpKoujiresult);

        return "";
    }

    /**
     * 工事IDからValveListを取得
     */
    public  List<Valve>  getValvesFromKoujiId(String koujiId,Directory indexValveFile,Directory indexKoujirelationFile){
        //KoujiIdからValve一覧取得
//        long starttime=System.currentTimeMillis();
        List<Valve> valveList = new LinkedList<Valve>();
        List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"TRKJID"+koujiId+"End");
        //工事IDから所属された弁のリストを取得 ユニークため、Mapに保存する
        Map<String,Valve> valveListMap=new HashMap<String, Valve>();

        if(searchResultObjectsKR.size()>0){
            for(int i=0;i<searchResultObjectsKR.size();i++){
                Koujirelation TmpKoujirelation=new Koujirelation();
                Koujirelation koujirelation=TmpKoujirelation.toKoujirelation(searchResultObjectsKR.get(i).getBody());

                List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+koujirelation.getKikisysid()+"End");
                if(searchResultObjectsVA.size()>0){
                    Valve tmpValve=new Valve();
                    Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                    valveListMap.put(String.valueOf(valve.getKikiSysId()),valve);
                }
            }
        }
        //MapからListに変換
        for(String key : valveListMap.keySet()){
            valveList.add(valveListMap.get(key));
        }

//        long endtime=System.currentTimeMillis();
//        System.out.println("工事の弁を取得時間   ="+(endtime-starttime));

        return valveList;
    }
    /**
     * 会社名により、工事を取得
     *
     * @param location 会社名
     *
     * @return String　工事リスト
     * */
    @RequestMapping(value = "/getKoujiByLocation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public  String getKoujiByLocation(@RequestParam("location")String location,ModelMap modelMap,HttpSession session){
        Gson gson=new Gson();

        List<Kouji> koujiresult=new ArrayList<Kouji>();
        //location文字列のスペースを削除
        location=location.trim();
        koujiresult=koujiMapper.findKoujiByLocation(location);
//        List<Kouji> koujis=koujiMapper.findAllKoujiSort();
//        location=location.replaceAll("\\s", "");
//        if(location.length()<1){
//        }else{
//            for(int nIndex=0;nIndex<koujis.size();nIndex++){
//                String tmpLocation=koujis.get(nIndex).getLocation();
//                //location文字列のスペースを削除
//                tmpLocation=tmpLocation.replaceAll("\\s", "");
//                //同じlocationの工事を取得
//                if(tmpLocation.equals(location)){
//                    koujiresult.add(koujis.get(nIndex));
//                }
//            }
//        }
        //location情報を保存
        modelMap.addAttribute("locationNameSelectedForKouji",location);
        modelMap.addAttribute("locationKoujiSelectedForKouji",koujiresult);
        session.setAttribute("locationNameSelectedForKouji",location);
        session.setAttribute("locationKoujiSelectedForKouji",koujiresult);
        return  gson.toJson(koujiresult);
    }

    /**
     * 工事番号　重複するかどうかチェックする 新規
     *
     * @param kjNo 工事番号
     * @param location 会社名
     *
     * @return String　工事番号存在かどうか　Flag
     * */
    @RequestMapping(value = "/getResultForKoujiVNoCheck", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public  String getResultForKoujiVNoCheck(@RequestParam("kjNo")String kjNo,@RequestParam("location")String location,ModelMap modelMap,HttpSession session){

        Kouji kouji=new Kouji();
        kouji.setKjNo(kjNo);
        kouji.setLocation(location);

        List<Kouji> koujiResult=koujiMapper.findKoujiByLocationAndKjNo(kouji);
        if(koujiResult==null || koujiResult.size()==0){
            return "true";//使える
        }else{
            return "false";//使えない
        }
    }

    /**
     * 工事番号　重複するかどうかチェックする 編集
     *
     * @param kjNo 工事番号
     * @param location 会社名
     *
     * @return String　工事番号存在かどうか　Flag
     * */
    @RequestMapping(value = "/getResultForKoujiVNoCheckEdit", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public  String getResultForKoujiVNoCheckEdit(@RequestParam("id")String id,@RequestParam("kjNo")String kjNo,@RequestParam("location")String location,ModelMap modelMap,HttpSession session){

        Kouji kouji=new Kouji();
        kouji.setKjNo(kjNo);
        kouji.setLocation(location);

        List<Kouji> koujiResult=koujiMapper.findKoujiByLocationAndKjNo(kouji);
        if(koujiResult==null || koujiResult.size()==0){
            return "true";//使える
        }else{
            List<Kouji> koujiResultTmp=new ArrayList<Kouji>();
            for(int i=0;i<koujiResult.size();i++){
                if(id.equals(koujiResult.get(i).getId()+"")){
                }else{
                    koujiResultTmp.add(koujiResult.get(i));
                }
            }

            if(koujiResultTmp==null || koujiResultTmp.size()==0){
                return "true";//使える
            }else{
                return "false";//使えない
            }
        }
    }
}
