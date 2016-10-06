package com.toyo.vh.controller;

import com.google.gson.Gson;
import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.dao.ItemMapper;
import com.toyo.vh.dto.*;
import com.toyo.vh.entity.*;
import com.toyo.vh.service.*;
import org.apache.lucene.queryparser.classic.ParseException;
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
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Lsr on 11/22/14.
 */

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    KoujiService koujiService;

    @Autowired
    LuceneIndexService luceneIndexService;

    @Autowired
    KoujirelationService koujirelationService;

    @Autowired
    LocationService locationService;

    @Resource
    ItemMapper itemMapper;

    /**
     * 弁番号より、弁のJSONデータを取得
     *
     * @param  vNo 検索キーワード
     * @param  koujiId 工事ID
     *
     * @return  String 弁リスト
     *
     * */
    @RequestMapping(value = "/getKikiSysIdByVNoJson", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKikiSysIdByVNoJson(@RequestParam("vNo")String vNo,@RequestParam("koujiId")String koujiId,
                                     HttpSession session, ModelMap modelMap) throws IOException {



        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));


        //IDから工事取得
        Kouji kouji=new Kouji();
        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+koujiId+"End");
        if(searchResultObjectsKJ.size()>0){
            Kouji tmpkouji=new Kouji();
            kouji=tmpkouji.toKouji(searchResultObjectsKJ.get(0).getBody());
        }

        System.out.println("検索文字："+vNo);

        //弁番号で検索
        List<Valve> valveList = new ArrayList<Valve>();
        List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,vNo);
        System.out.println(searchResultObjectsVA.size());
        if(searchResultObjectsVA.size()>0){
            for(SearchResultObject searchResultObject:searchResultObjectsVA){
                Valve tmpValve=new Valve();
                Valve valve=tmpValve.toValve(searchResultObject.getBody());
                if(valve.getLocationName().equals(kouji.getLocation())){
                    valveList.add(valve);
                }
            }
        }
        session.setAttribute("valveList",valveList);
        Gson gson = new Gson();

        return gson.toJson(valveList);
    }


    /**
     * 弁ID,工事IDより、弁を取得
     *
     * @param  koujiId 工事ID
     * @param  kikiSysId 弁ID
     * @param  syukan 主管係ID
     *
     * @return  String
     *
     * */
    @RequestMapping(value = "/getKikiByKikiSysId", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKikiByKikiSysId(@RequestParam("koujiId")String koujiId,
                                     @RequestParam("kikiSysId")String kikiSysId,
                                     @RequestParam("syukan")String syukan,
                                     HttpSession session) throws IOException {
        //getFromSession
        List<ValveKikiSelectUtil> valveKikiSelectUtilList = (List<ValveKikiSelectUtil>) session.getAttribute(koujiId);
        List<ValveKikiSelectUtil> vksuList = new LinkedList<ValveKikiSelectUtil>();

        //初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //如果没有缓存
        if(CollectionUtils.isEmpty(valveKikiSelectUtilList)) {
            valveKikiSelectUtilList = new ArrayList<ValveKikiSelectUtil>();

            //弁IDから機器システム関連表からデータ取得して、機器ID一覧取得
            List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"VA"+kikiSysId+"End");
//            System.out.println("kiki  件数="+searchResultObjectsKiki.size());
            List<Kiki> kikis=new ArrayList<Kiki>();

            for(SearchResultObject tmpSearchResultObject :searchResultObjectsKiki){
                Kiki tmpKiki=new Kiki();
                Kiki kiki=tmpKiki.toKiki(tmpSearchResultObject.getBody());
                //valveKikiSelectUtil作成
                ValveKikiSelectUtil valveKikiSelectUtil = new ValveKikiSelectUtil();
                valveKikiSelectUtil.setKiki(kiki);
                valveKikiSelectUtil.setId(koujiId);
                valveKikiSelectUtil.setKikiSysId(kikiSysId);

                //Koujirelation を確認する
                List<Integer> kikiList = koujirelationService.getKikiIdListByKoujiidAndKikisys(koujiId, kikiSysId);
                if(kikiList.contains(kiki.getKikiId())){
                    valveKikiSelectUtil.setStatus("active");
                } else {
                    valveKikiSelectUtil.setStatus("");
                }
                if("1".equals(kiki.getKikiDelFlg())){
                    //機器が削除された場合  この工事に点検履歴がある場合のみ表示される
                    if("active".equals(valveKikiSelectUtil.getStatus())){
                        //この工事に点検履歴がある場合のみ表示される
                        valveKikiSelectUtilList.add(valveKikiSelectUtil);
                    }
                }else{
                    valveKikiSelectUtilList.add(valveKikiSelectUtil);
                }
            }

            for (int i = 0; i < valveKikiSelectUtilList.size(); i++) {
                if(valveKikiSelectUtilList.get(i).getKikiSysId().equals(kikiSysId)){
                    vksuList.add(valveKikiSelectUtilList.get(i));
                }
            }
        } else {
            //有缓存session  sessionから当弁の情報取得
            for (int i = 0; i < valveKikiSelectUtilList.size(); i++) {
                if(valveKikiSelectUtilList.get(i).getKikiSysId().equals(kikiSysId)){
                    vksuList.add(valveKikiSelectUtilList.get(i));
                }
            }
            //无缓存kikiSysId ない場合はDBから取得
            if(vksuList.size() < 1){
                //弁IDから機器ID一覧取得
                List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"VA"+kikiSysId+"End");
                System.out.println("kiki  件数="+searchResultObjectsKiki.size());
                List<Kiki> kikis=new ArrayList<Kiki>();

                for(SearchResultObject tmpSearchResultObject :searchResultObjectsKiki){
                    Kiki tmpKiki=new Kiki();
                    Kiki kiki=tmpKiki.toKiki(tmpSearchResultObject.getBody());
                    //valveKikiSelectUtil作成
                    ValveKikiSelectUtil valveKikiSelectUtil = new ValveKikiSelectUtil();
                    valveKikiSelectUtil.setKiki(kiki);
                    valveKikiSelectUtil.setId(koujiId);
                    valveKikiSelectUtil.setKikiSysId(kikiSysId);

                    //Koujirelation を確認する
                    List<Integer> kikiList = koujirelationService.getKikiIdListByKoujiidAndKikisys(koujiId, kikiSysId);
                    if(kikiList.contains(kiki.getKikiId())){
                        valveKikiSelectUtil.setStatus("active");
                    } else {
                        valveKikiSelectUtil.setStatus("");
                    }
                    if("1".equals(kiki.getKikiDelFlg())){
                        //機器が削除された場合  この工事に点検履歴がある場合のみ表示される
                        if("active".equals(valveKikiSelectUtil.getStatus())){
                            //この工事に点検履歴がある場合のみ表示される
                            valveKikiSelectUtilList.add(valveKikiSelectUtil);
                        }
                    }else{
                        valveKikiSelectUtilList.add(valveKikiSelectUtil);
                    }
                }
                for (int i = 0; i < valveKikiSelectUtilList.size(); i++) {
                    if(valveKikiSelectUtilList.get(i).getKikiSysId().equals(kikiSysId)){
                        vksuList.add(valveKikiSelectUtilList.get(i));
                    }
                }
            }
        }


        session.setAttribute(koujiId, valveKikiSelectUtilList);
        Gson gson = new Gson();

        //kiki分類により、Sortする
        List<ValveKikiSelectUtil> vksuListA = new LinkedList<ValveKikiSelectUtil>();
        List<ValveKikiSelectUtil> vksuListB = new LinkedList<ValveKikiSelectUtil>();
        List<ValveKikiSelectUtil> vksuListC = new LinkedList<ValveKikiSelectUtil>();
        List<ValveKikiSelectUtil> vksuListD = new LinkedList<ValveKikiSelectUtil>();
        List<ValveKikiSelectUtil> vksuListE = new LinkedList<ValveKikiSelectUtil>();

        for(int i=0;i<vksuList.size();i++){
            String kikibunrui=vksuList.get(i).getKiki().getKikiBunrui();
            if(Config.KikiBunRuiA.equals(kikibunrui)){
                vksuListA.add(vksuList.get(i));
            }else if(Config.KikiBunRuiB.equals(kikibunrui)){
                vksuListB.add(vksuList.get(i));
            }else if(Config.KikiBunRuiC.equals(kikibunrui)){
                vksuListC.add(vksuList.get(i));
            }else if(Config.KikiBunRuiD.equals(kikibunrui)){
                vksuListD.add(vksuList.get(i));
            }else{ //４種類以外の場合
                vksuListE.add(vksuList.get(i));
            }
        }
        //Listを結合する
        List<ValveKikiSelectUtil> vksuListResult = new LinkedList<ValveKikiSelectUtil>();
        vksuListResult.addAll(vksuListA);
        vksuListResult.addAll(vksuListB);
        vksuListResult.addAll(vksuListC);
        vksuListResult.addAll(vksuListD);
        vksuListResult.addAll(vksuListE);

        return gson.toJson(vksuListResult);
    }

    /**
     * 点検機器追加時　Status設定
     *
     * @param  koujiId 工事ID
     * @param  kikiSysId 弁ID
     * @param  kikiId  機器ID
     * @param  status　選択状態
     *
     * @return  String
     *
     * */
    @RequestMapping(value = "/saveStatusToSession", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveStatusToSession(@RequestParam("koujiId")String koujiId,
                                      @RequestParam("kikiSysId")String kikiSysId,
                                      @RequestParam("kikiId")String kikiId,
                                      @RequestParam("status")String status,
                                      HttpSession session){
        //getFromSession
        List<ValveKikiSelectUtil> valveKikiSelectUtilList = (List<ValveKikiSelectUtil>) session.getAttribute(koujiId);
        for(int i = 0;i<valveKikiSelectUtilList.size();i++){
            String vId = valveKikiSelectUtilList.get(i).getKikiSysId();
            String kId = valveKikiSelectUtilList.get(i).getKiki().getKikiId()+"";
            if(kId.equals(kikiId) && vId.equals(kikiSysId)){
                valveKikiSelectUtilList.get(i).setStatus(status);
                break;
            }
        }
        session.setAttribute(koujiId, valveKikiSelectUtilList);

        return "";
    }

    /**
     * 弁IDにより、弁と機器情報を取得
     *
     * @param kikiSysId 弁ID
     * @param kikiOrBenFlg 検索種類フラグ
     *
     * @return valve　弁情報
     * 　　　　 kikiList 機器情報
     * */
    @RequestMapping(value = "/{kikiSysId}/{kikiOrBenFlg}/valve", method = RequestMethod.GET)
    public String getItemByKikisysid(@PathVariable String kikiSysId,@PathVariable String kikiOrBenFlg,ModelMap modelMap, HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        Valve valve=new Valve();
        List<Kiki> kikiList=new ArrayList<Kiki>();
        List<Kiki> tmpKikiList=new ArrayList<Kiki>();

        //valve検索
        List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiSysId+"End");
        if(!CollectionUtils.isEmpty(resultsValve)){
            Valve tmpValve=new Valve();
            valve=tmpValve.toValve(resultsValve.get(0).getBody());
        }

        //Kiki検索
        List<SearchResultObject> resultsKiki = luceneIndexService.selectRecord(indexKikiFile, "VA"+kikiSysId+"End");
        if(!CollectionUtils.isEmpty(resultsKiki)){
            for(SearchResultObject tmpSearchResultObject:resultsKiki){
                Kiki tmpKiki=new Kiki();
                Kiki kiki=tmpKiki.toKiki(tmpSearchResultObject.getBody());
                if("0".equals(kiki.getKikiDelFlg())){
                    tmpKikiList.add(kiki);
                }
            }
        }
        //kikiListをソートする
        List<Kiki> kikilistA =new ArrayList<Kiki>();
        List<Kiki> kikilistB =new ArrayList<Kiki>();
        List<Kiki> kikilistC =new ArrayList<Kiki>();
        List<Kiki> kikilistD =new ArrayList<Kiki>();
        List<Kiki> kikilistE =new ArrayList<Kiki>();

        //Listに変換する
        for(int i=0;i<tmpKikiList.size();i++){
            String kikibunrui=tmpKikiList.get(i).getKikiBunrui();
            if(Config.KikiBunRuiA.equals(kikibunrui)){
                kikilistA.add(tmpKikiList.get(i));
            }else if(Config.KikiBunRuiB.equals(kikibunrui)){
                kikilistB.add(tmpKikiList.get(i));
            }else if(Config.KikiBunRuiC.equals(kikibunrui)){
                kikilistC.add(tmpKikiList.get(i));
            }else if(Config.KikiBunRuiD.equals(kikibunrui)){
                kikilistD.add(tmpKikiList.get(i));
            }else{ //４種類以外の場合
                kikilistE.add(tmpKikiList.get(i));
            }
        }
        //Listを結合する
        kikiList.addAll(kikilistA);
        kikiList.addAll(kikilistB);
        kikiList.addAll(kikilistC);
        kikiList.addAll(kikilistD);
        kikiList.addAll(kikilistE);

        //キーワードを取得
        String keyword = (String)session.getAttribute("KikisysSearchKeyword");
        modelMap.addAttribute("KikisysSearchKeyword",keyword);
        session.setAttribute("KikisysSearchKeyword",keyword);
        modelMap.addAttribute("valve",valve);
        modelMap.addAttribute("kikiList",kikiList);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
       //1場合は、弁検索へ戻る、２場合は、機器検索へ戻る、３場合は、部品検索へ戻る 4複数検索へ戻る
        if(kikiOrBenFlg.equals("1")){
            return "valve/valvekiki";
        }else if(kikiOrBenFlg.equals("2")){
            return "kikiSearch/kikivalvekiki";
        }else if(kikiOrBenFlg.equals("3")){
            return "buhinSearch/buhinvalvekiki";
        }else{
            return "valveMultSearch/valveMultvalvekiki";
        }
    }

    /**
     * 機器IDにより、情報を取得
     *
     * @param kikiSysId 弁ID
     * @param kikiId    機器ID
     * @param kikiOrBenFlg 検索種類フラグ
     *
     * @return String　機器編集画面パス
     * */
    @RequestMapping(value = "/{kikiSysId}/{kikiId}/{kikiOrBenFlg}/valve", method = RequestMethod.GET)
    public String getKikiByKikiId(@PathVariable String kikiSysId,
                                  @PathVariable String kikiId,@PathVariable String kikiOrBenFlg,
                                  ModelMap modelMap,
                                  HttpSession session) throws IOException {

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        Valve valve=new Valve();
        Kiki kiki=new Kiki();
        List<Buhin> buhinList=new ArrayList<Buhin>();
        //valveIdにより、Valveを取得する
        List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiSysId+"End");
        if(!CollectionUtils.isEmpty(resultsValve)){
            Valve tmpValve=new Valve();
            valve=tmpValve.toValve(resultsValve.get(0).getBody());
        }
        //KikiIdにより、Kikiを取得する
        List<SearchResultObject> resultsKiki = luceneIndexService.selectRecord(indexKikiFile, "KI"+kikiId+"End");
        if(!CollectionUtils.isEmpty(resultsKiki)){
            Kiki tmpKiki=new Kiki();
            kiki=tmpKiki.toKiki(resultsKiki.get(0).getBody());
        }
        //BuhinIdにより、Buhinを取得する
        List<SearchResultObject> resultsBuhin = luceneIndexService.selectRecord(indexBuhinFile, "KI"+kikiId+"End");
        if(!CollectionUtils.isEmpty(resultsBuhin)){
            for(SearchResultObject tmpSearchResultObject:resultsBuhin) {
                Buhin tmpBuhin = new Buhin();
                Buhin buhin = tmpBuhin.toBuhin(tmpSearchResultObject.getBody());
                buhinList.add(buhin);
            }
        }

        modelMap.addAttribute("valve",valve);
        modelMap.addAttribute("kiki",kiki);
        modelMap.addAttribute("buhinList",buhinList);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);

        //1場合は、弁検索へ戻る、２場合は、機器検索へ戻る、３場合は、部品検索へ戻る
        if(kikiOrBenFlg.equals("1")){
            return "valve/kikibuhin";
        }else if(kikiOrBenFlg.equals("2")){
            return "kikiSearch/kikikikibuhin";
        }else if(kikiOrBenFlg.equals("3")){
            return "buhinSearch/buhinkikibuhin";
        }else{
            return "valveMultSearch/valveMultkikibuhin";
        }
    }

    /**
     * 部品　情報取得
     *
     * @param buhinId   部品ID
     *
     * @return String　 部品編集画面パス
     * */
    @RequestMapping(value = "/getBuhin",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getBuhin(@RequestParam("buhinId")String buhinId,
                           ModelMap modelMap,
                           HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        Buhin buhin=new Buhin();
        //buhinIDにより、部品情報取得
        List<SearchResultObject> resultsBuhin = luceneIndexService.selectRecord(indexBuhinFile, "BU"+buhinId+"End");
        if(!CollectionUtils.isEmpty(resultsBuhin)){
            Buhin tmpBuhin=new Buhin();
            buhin=tmpBuhin.toBuhin(resultsBuhin.get(0).getBody());
        }

        Gson gson = new Gson();
        return gson.toJson(buhin);
    }

    /**
     * keywordにより、弁を検索する
     * valveList画面へ遷移
     *
     * @param keyword 検索キーワード
     * @return String アイテム一覧
     * */
    @RequestMapping(value = "/valveSearch", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String valveSearch(@RequestParam("keyword")String keyword, @RequestParam("locationNameSelect")String locationNameSelect, ModelMap modelMap,HttpSession session) throws IOException, ParseException {

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath.size()<5) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //valve精確検索のため
        Valve valveSearchForSeikak = (Valve)session.getAttribute("valveSearchForSeikak");
        //keywordにより検索 結果リストを初期化
        List<Valve> valveResults = new ArrayList<Valve>();
        //valve検索
        if(keyword.contains("*")||keyword.contains("＊")){
            Valve valve=new Valve();
            valve.setLocationName(locationNameSelect);
            valveResults=itemMapper.findByLocationName(valve);
        }else{
            List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, keyword);
            if(resultsValve!=null && (resultsValve.size()>0)){
                for(int i=0;i<resultsValve.size();i++){
                    Valve tmpValve=new Valve();
                    Valve valve=tmpValve.toValve(resultsValve.get(i).getBody());
                    //会社名でFilter
                    if((valve.getLocationName().contains(locationNameSelect)) && (!("1".equals(valve.getDelFlg())))){
                        valveResults.add(valve);
                    }
                }
            }
        }

        // 弁番号で 昇順ソート
        Collections.sort(valveResults,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });

        modelMap.addAttribute("valveResultsForKikisys",valveResults);
        session.setAttribute("valveResultsForKikisys",valveResults);
        modelMap.addAttribute("KikisysSearchKeyword",keyword);
        session.setAttribute("KikisysSearchKeyword",keyword);

        //get location
        User user=(User)session.getAttribute("user");
        List<Location> locationList =new ArrayList<Location>();
        if("admin".equals(user.getDepartment())){
            locationList = locationService.getAllLocation();
        }else{
            locationList = locationService.getLocationByUserDepartment(user.getDepartment());
        }



        List<String> nameList = new LinkedList<String>();
        for (int i = 0; i < locationList.size(); i++) {
            String tmpLocation= StringUtil.concatWithDelimit(" ", locationList.get(i).getkCodeL(), locationList.get(i).getkCodeM(), locationList.get(i).getkCodeS());
            if(!nameList.contains(locationList.get(i).getkCodeL())){
                nameList.add(locationList.get(i).getkCodeL());
            }
            if(!nameList.contains(tmpLocation)){
                nameList.add(tmpLocation);
            }
        }
        //sessionにすでにある場合はsessionを使う
        if(locationNameSelect==""){
            locationNameSelect="全部会社名";
        }
        String kikiOrBenFlg="1";
        modelMap.addAttribute("nameList",nameList);
        session.setAttribute("locationList",locationList);
        session.setAttribute("valveSearchitemNum",valveResults.size());
        session.setAttribute("locationNameSelectedForValve",locationNameSelect);
        modelMap.addAttribute("locationList", locationList);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
        session.setAttribute("valveSearchForSeikak",valveSearchForSeikak);
        //選択された弁設定
//        String saveSelectedForValve =(String)session.getAttribute("saveSelectedForValve");
        session.setAttribute("saveSelectedForValve","");

        return "list/valve";
    }

    /**
     * 会社名により、弁を取得
     *
     * @param location 会社名
     *
     * @return String　工事リスト
     * */
    @RequestMapping(value = "/getValveByLocation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public  String getValveByLocation(@RequestParam("location")String location,ModelMap modelMap,HttpSession session){

        Gson gson=new Gson();
//        List<Valve> valves= itemMapper.findAllValve();
        List<Valve> valveresult=new ArrayList<Valve>();
        //両端のスペースを削除
        location=location.trim();
        Valve valve=new Valve();
        valve.setLocationName(location);
        valveresult=itemMapper.findByLocationName(valve);   //locationのスペースがない場合
        //location文字列のスペースを削除
//        location=location.replaceAll("\\s", "");
//        if(location.length()<1){
//        }else{
//            for(int nIndex=0;nIndex<valves.size();nIndex++){
//                String tmpLocation=valves.get(nIndex).getLocationName();
//                //location文字列のスペースを削除
//                tmpLocation=tmpLocation.replaceAll("\\s", "");
//                //同じlocationの工事を取得
//                if(tmpLocation.equals(location)){
//                    valveresult.add(valves.get(nIndex));
//                }
//            }
//        }

        //location情報を保存
        modelMap.addAttribute("locationNameSelectedForValve",location);
        modelMap.addAttribute("locationValveSelectedForValve",valveresult);
        session.setAttribute("locationNameSelectedForValve",location);
        session.setAttribute("locationValveSelectedForValve",valveresult);
        session.setAttribute("message","");
        modelMap.addAttribute("message","");
        return  gson.toJson(valveresult);
    }

    /**
     * keywordにより、弁、機器を検索する
     * アイテム一覧画面に遷移
     *
     * @param keyword 検索キーワード
     * @return String アイテム一覧
     * */
    @RequestMapping(value = "/kikiSearch", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String kikiSearch(@RequestParam("keyword")String keyword, @RequestParam("locationNameSelect")String locationNameSelect,@RequestParam("kikiRadioSelect")String kikiRadioSelect,ModelMap modelMap,HttpSession session) throws IOException, ParseException {

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath.size()<5) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //kiki精確検索のため
        Kiki kikiSearchForSeikak = (Kiki)session.getAttribute("kikiSearchForSeikak");

        //keywordにより検索 結果リストを初期化
        List<Valve> valveResults = new ArrayList<Valve>();
        List<Kiki> kikiResults = new ArrayList<Kiki>();
        List<ValveForDL> itemResults=new ArrayList<ValveForDL>(); //kikiResultsに弁情報追加
        Map<String,ValveForDL> itemUnitMap=new HashMap<String, ValveForDL>();//弁をユニークにするため

//        System.out.println("kikiRadioSelect="+kikiRadioSelect);
        long begintime=System.currentTimeMillis();
        //Kiki検索
        List<SearchResultObject> resultsKiki = luceneIndexService.selectRecord(indexKikiFile, keyword);
        if(resultsKiki!=null && (resultsKiki.size()>0)){
            for(int i=0;i<resultsKiki.size();i++){

                Kiki tmpKiki=new Kiki();
                Kiki kiki=tmpKiki.toKiki(resultsKiki.get(i).getBody());
                //kiki条件判断
                String tmpString=resultsKiki.get(i).getBody();
                String words[] = tmpString.split("\t,");
                if(StringUtil.isNotEmpty(kikiRadioSelect)){
                    //機器検索項目がある場合、
                    //小文字に変換してから検索する
                    String tmpLower=words[Integer.parseInt(kikiRadioSelect)];
                    tmpLower=tmpLower.toLowerCase();
                    if(tmpLower.contains(keyword)){
                        //キーワードが含まれた場合のみ追加する
                        kikiResults.add(kiki);
                    }
                }else{
                    //機器検索項目がない場合、すべて追加
                    kikiResults.add(kiki);
                }
            }
        }

        //kikiに弁の情報を追加する
        for(int i=0;i<kikiResults.size();i++){
            ValveForDL item=new ValveForDL();
            Valve valve=new Valve();
            List<Kiki> kikis=new ArrayList<Kiki>();
            kikis.add(kikiResults.get(i));
            //valve検索
            List<SearchResultObject> resultValves = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiResults.get(i).getKikisysidKiki()+"End");
            if(resultValves!=null && (resultValves.size()>0)){
                Valve tmpValve=new Valve();
                valve=tmpValve.toValve(resultValves.get(0).getBody());
            }
            item.setValve(valve);
            item.setKikiList(kikis);
            //Mapにすでに存在場合、追加しない
            if(itemUnitMap.get(valve.getKikiSysId()+"")==null && (valve.getLocationName().contains(locationNameSelect))){
                itemUnitMap.put(valve.getKikiSysId()+"",item);
                itemResults.add(item);
            }
        }
        //sessionにすでにある場合はsessionを使う
        if(locationNameSelect==""){
            locationNameSelect="全部会社名";
        }
        long endtime=System.currentTimeMillis();
//        System.out.println("search time="+(endtime-begintime));
        String kikiOrBenFlg="2";
        modelMap.addAttribute("kikiSearchitemResults",itemResults);
        modelMap.addAttribute("kikiSearchitemNum",itemResults.size());
        modelMap.addAttribute("locationKikiSearchSelected",locationNameSelect);
        session.setAttribute("kikiSearchitemResults",itemResults);
        session.setAttribute("kikiSearchitemNum",itemResults.size());
        session.setAttribute("keywordMessage",keyword);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
        session.setAttribute("locationKikiSearchSelected",locationNameSelect);
        session.setAttribute("kikiSearchForSeikak",kikiSearchForSeikak);
        session.setAttribute("saveSelectedForKiki","");
        return "list/valvekikiList";
    }
    /**
     * keywordにより、弁,部品情報を検索する
     * 部品検索一覧画面に遷移
     *
     * @param keyword 検索キーワード
     * @return String アイテム一覧
     * */
    @RequestMapping(value = "/buhinSearch", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String buhinSearch(@RequestParam("keyword")String keyword, @RequestParam("locationNameSelect")String locationNameSelect,@RequestParam("buhinRadioSelect")String buhinRadioSelect,ModelMap modelMap,HttpSession session) throws IOException, ParseException {

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath.size()<5) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //kiki精確検索のため
        Buhin buhinSearchForSeikak = (Buhin)session.getAttribute("buhinSearchForSeikak");

        //keywordにより検索 結果リストを初期化
        List<Valve> valveResults = new ArrayList<Valve>();
        List<Buhin> buhinResults = new ArrayList<Buhin>();
        List<ValveForDL> buhinResultList=new ArrayList<ValveForDL>(); //buhinResultsに弁情報追加
        Map<String,ValveForDL> itemUnitMap=new HashMap<String, ValveForDL>();//弁をユニークにするため

        long begintime=System.currentTimeMillis();
        //Buhin検索
        List<SearchResultObject> resultsBuhin = luceneIndexService.selectRecord(indexBuhinFile, keyword);
        long tmptime2=System.currentTimeMillis();
        System.out.println("Lucene検索時間"+(tmptime2-begintime));
        if(resultsBuhin!=null && (resultsBuhin.size()>0)){
            for(int i=0;i<resultsBuhin.size();i++){

                Buhin tmpBuhin=new Buhin();
                Buhin buhin=tmpBuhin.toBuhin(resultsBuhin.get(i).getBody());

                //kiki条件判断
                String tmpString=resultsBuhin.get(i).getBody();
                String words[] = tmpString.split("\t,");
                if(StringUtil.isNotEmpty(buhinRadioSelect)){
                    //部品検索項目がある場合、
                    //小文字に変換してから検索する
                    String tmpLower=words[Integer.parseInt(buhinRadioSelect)];
                    tmpLower=tmpLower.toLowerCase();
                    if(tmpLower.contains(keyword)){
                        //キーワードが含まれた場合のみ追加する
                        buhinResults.add(buhin);
                    }
                }else{
                    //部品検索項目がない場合、すべて追加
                    buhinResults.add(buhin);
                }
            }
        }
        long tmptime3=System.currentTimeMillis();
        System.out.println("部品取得時間"+(tmptime3-tmptime2));
        //buhinに弁の情報を追加する
        for(int i=0;i<buhinResults.size();i++){
                ValveForDL item=new ValveForDL();
                Valve valve=new Valve();
                List<Buhin> buhins=new ArrayList<Buhin>();
                buhins.add(buhinResults.get(i));
                //valve検索
                List<SearchResultObject> resultValves = luceneIndexService.selectRecord(indexValveFile, "VA"+buhinResults.get(i).getKikisysidBuhin()+"End");
                if(resultValves!=null && (resultValves.size()>0)){
                    Valve tmpValve=new Valve();
                    valve=tmpValve.toValve(resultValves.get(0).getBody());
                }
                item.setValve(valve);
                item.setBuhins(buhins);
                //Mapにすでに存在場合、追加しない
                if(itemUnitMap.get(valve.getKikiSysId()+"")==null && (valve.getLocationName().contains(locationNameSelect))){
                    itemUnitMap.put(valve.getKikiSysId()+"",item);
                    buhinResultList.add(item);
                }
        }
        long tmptime4=System.currentTimeMillis();
        System.out.println("弁取得時間"+(tmptime4-tmptime3));
        //sessionにすでにある場合はsessionを使う
        if(locationNameSelect==""){
            locationNameSelect="全部会社名";
        }
        long endtime=System.currentTimeMillis();
        System.out.println("search time="+(endtime-begintime));
        String kikiOrBenFlg="3";
        modelMap.addAttribute("buhinSearchitemResults",buhinResultList);
        modelMap.addAttribute("buhinSearchitemNum",buhinResultList.size());
        modelMap.addAttribute("locationBuhinSearchSelected",locationNameSelect);
        session.setAttribute("buhinSearchitemResults",buhinResultList);
        session.setAttribute("buhinSearchitemNum",buhinResultList.size());
        session.setAttribute("buhinKeywordMessage",keyword);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
        session.setAttribute("locationBuhinSearchSelected",locationNameSelect);
        session.setAttribute("saveSelectedForBuhin","");
        return "list/valvebuhinList";
    }
    /**
     * 弁検索ページ　弁Filter情報保存する
     * */

    @RequestMapping(value = "/valveFilter", method = RequestMethod.GET, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String valveFilter(@RequestParam("filterKeyword")String filterKeyword,HttpSession session) throws UnsupportedEncodingException {
        //文字化けを修正する
        filterKeyword = new String(filterKeyword.getBytes("ISO_8859_1"), "UTF-8");
        session.setAttribute("filterValve",filterKeyword);
        return "true";
    }
    /**
     * 弁検索ページ　機器Filter情報保存する
     * */

    @RequestMapping(value = "/valveKikiFilter", method = RequestMethod.GET, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String valveKikiFilter(@RequestParam("filterKeyword")String filterKeyword,HttpSession session) throws UnsupportedEncodingException {
        //文字化けを修正する
        filterKeyword = new String(filterKeyword.getBytes("ISO_8859_1"), "UTF-8");
        session.setAttribute("filterValveKiki",filterKeyword);
        return "true";
    }
    /**
     * 弁検索ページ　部品Filter情報保存する
     * */

    @RequestMapping(value = "/valveKikiBuhinFilter", method = RequestMethod.GET, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String valveKikiBuhinFilter(@RequestParam("filterKeyword")String filterKeyword,HttpSession session) throws UnsupportedEncodingException {
        //文字化けを修正する
        filterKeyword = new String(filterKeyword.getBytes("ISO_8859_1"), "UTF-8");
        session.setAttribute("filterValveKikiBuhin",filterKeyword);
        return "true";
    }

    /**
     * 工事検索ページ　工事Filter情報保存する
     * */

    @RequestMapping(value = "/koujiFilter", method = RequestMethod.GET, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String koujiFilter(@RequestParam("filterKeyword")String filterKeyword,HttpSession session) throws UnsupportedEncodingException {
        //文字化けを修正する
        filterKeyword = new String(filterKeyword.getBytes("ISO_8859_1"), "UTF-8");

        session.setAttribute("filterKouji",filterKeyword);
        return "true";
    }

    /**
     * 弁 精確検索
     *
     * @param valveForm 弁情報
     *
     * @return String　弁情報編集画面パス
     * */
    @RequestMapping(value = "/valveSearchForSeikak", method = RequestMethod.POST)
    public String valveSearchForSeikak(@ModelAttribute("ValveForm")ValveForm valveForm, ModelMap modelMap,HttpSession session) throws IOException {
        //valveFormからValveに変更
        Valve valve = new Valve();
        valve.makeupValveByForm(valveForm);
        //keywordにより検索 結果リストを初期化
        List<Valve> valveResults = new ArrayList<Valve>();
        valveResults=itemService.getItemByValve(valve);

        // 弁番号で 昇順ソート
        Collections.sort(valveResults,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });

        modelMap.addAttribute("valveResultsForKikisys",valveResults);
        session.setAttribute("valveResultsForKikisys",valveResults);
        session.setAttribute("valveSearchitemNum",valveResults.size());
        session.setAttribute("valveSearchForSeikak",valve);
        session.setAttribute("saveSelectedForValve","");
        return "list/valve";
    }

    /**
     * 機器 精確検索
     *
     * @param kikiForm 機器情報
     *
     * @return String　機器情報編集画面パス
     * */
    @RequestMapping(value = "/kikiSearchForSeikak", method = RequestMethod.POST)
    public String kikiSearchForSeikak(@ModelAttribute("KikiForm")KikiForm kikiForm, ModelMap modelMap,HttpSession session) throws IOException {

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath.size()<5) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //valveFormからValveに変更
        Kiki kiki = new Kiki();
        kiki.makeupValveByForm(kikiForm);
        //keywordにより検索 結果リストを初期化
        List<Kiki> kikiResults = new ArrayList<Kiki>();
        kikiResults=itemService.getItemByKiki(kiki);
        List<ValveForDL> itemResults=new ArrayList<ValveForDL>(); //kikiResultsに弁情報追加
        Map<String,ValveForDL> itemUnitMap=new HashMap<String, ValveForDL>();//弁をユニークにするため

        //kikiに弁の情報を追加する
        for(int i=0;i<kikiResults.size();i++){
            ValveForDL item=new ValveForDL();
            Valve valve=new Valve();
            List<Kiki> kikis=new ArrayList<Kiki>();
            kikis.add(kikiResults.get(i));
            //valve検索
            List<SearchResultObject> resultValves = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiResults.get(i).getKikisysidKiki()+"End");
            if(resultValves!=null && (resultValves.size()>0)){
                Valve tmpValve=new Valve();
                valve=tmpValve.toValve(resultValves.get(0).getBody());
            }
            item.setValve(valve);
            item.setKikiList(kikis);
            //Mapにすでに存在場合、追加しない
//            System.out.println("valve ID="+valve.getKikiSysId());
            if(itemUnitMap.get(valve.getKikiSysId()+"")==null && (valve.getLocationName().contains(kiki.bikou))){
                itemUnitMap.put(valve.getKikiSysId()+"",item);
                itemResults.add(item);
            }
        }

        session.setAttribute("kikiSearchitemResults",itemResults);
        session.setAttribute("kikiSearchitemNum",itemResults.size());
        session.setAttribute("kikiSearchForSeikak",kiki);
        session.setAttribute("saveSelectedForKiki","");
        return "list/valvekikiList";
    }

    /**
     * 部品 精確検索
     *
     * @param buhinForm 部品
     *
     * @return String　部品情報編集画面パス
     * */
    @RequestMapping(value = "/buhinSearchForSeikak", method = RequestMethod.POST)
    public String buhinSearchForSeikak(@ModelAttribute("BuhinForm")BuhinForm buhinForm, ModelMap modelMap,HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath.size()<5) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //valveFormからValveに変更
        Buhin buhin = new Buhin();
        buhin.makeupValueByForm(buhinForm);

        //keywordにより検索 結果リストを初期化
        List<Buhin> buhinResults = new ArrayList<Buhin>();
        buhinResults=itemService.getItemByBuhin(buhin);
        List<ValveForDL> buhinResultList=new ArrayList<ValveForDL>(); //buhinResultsに弁情報追加
        Map<String,ValveForDL> itemUnitMap=new HashMap<String, ValveForDL>();//弁をユニークにするため

        //buhinに弁の情報を追加する
        for(int i=0;i<buhinResults.size();i++){
            ValveForDL item=new ValveForDL();
            Valve valve=new Valve();
            List<Buhin> buhins=new ArrayList<Buhin>();
            buhins.add(buhinResults.get(i));
            //valve検索
            List<SearchResultObject> resultValves = luceneIndexService.selectRecord(indexValveFile, "VA"+buhinResults.get(i).getKikisysidBuhin()+"End");
            if(resultValves!=null && (resultValves.size()>0)){
                Valve tmpValve=new Valve();
                valve=tmpValve.toValve(resultValves.get(0).getBody());
            }
            item.setValve(valve);
            item.setBuhins(buhins);
            //Mapにすでに存在場合、追加しない
            if(itemUnitMap.get(valve.getKikiSysId()+"")==null && (valve.getLocationName().contains(buhin.bikou))){
                itemUnitMap.put(valve.getKikiSysId()+"",item);
                buhinResultList.add(item);
            }
        }

        session.setAttribute("buhinSearchitemResults",buhinResultList);
        session.setAttribute("buhinSearchitemNum",buhinResultList.size());
        session.setAttribute("buhinSearchForSeikak",buhin);
        session.setAttribute("saveSelectedForBuhin","");

        return "list/valvebuhinList";
    }

    /**
     * 弁 精確検索
     *
     * @param valveMultForm 弁 機器 部品情報
     *
     * @return String　弁情報編集画面パス
     * */
    @RequestMapping(value = "/valveMultSearchForSeikak", method = RequestMethod.POST)
    public String valveMultSearchForSeikak(@ModelAttribute("ValveMultForm")ValveMultForm valveMultForm, ModelMap modelMap,HttpSession session) throws IOException {
        //valveFormからValveに変更
        ValveMult valveMult = new ValveMult();
        Valve valve=new Valve();
        Kiki kiki=new Kiki();
        Buhin buhin=new Buhin();

        valveMult=valveMult.makeupValveMultByForm(valveMultForm);
        valve=valveMult.makeupValveByForm(valveMultForm);
        kiki=valveMult.makeupKikiByForm(valveMultForm);
        buhin=valveMult.makeupBuhinByForm(valveMultForm);

        //keywordにより検索 結果リストを初期化
        List<Valve> lastResults=new ArrayList<Valve>();
        List<Valve> valveResults = new ArrayList<Valve>();
        List<Kiki> kikiResults=new ArrayList<Kiki>();
        List<Buhin> buhinResults=new ArrayList<Buhin>();

        valveResults=itemService.getItemByValve(valve);


        List<Integer> kikiIdList=new ArrayList<Integer>();
        List<Integer> buhinIdList=new ArrayList<Integer>();

        //機器　部品の検索条件あるかどうかFLG
        boolean kikiFlg=true;
        boolean buhinFlg=true;

        //機器項目に条件がない場合
        if(kiki.getKatasikiNo()=="" && kiki.getMakerRyaku()=="" && kiki.getMaker()=="" && kiki.getSyukan()==""){
            kikiFlg=false;
        }else{
            kikiResults=itemService.getItemByKiki(kiki);
        }
        //部品項目に条件がない場合
        if(buhin.getBuhinMei()=="" && buhin.getHyojunSiyou()=="" && buhin.getSunpou()=="" && buhin.getMakerRyaku()=="" && buhin.getMaker()==""){
            buhinFlg=false;
        }else{
            buhinResults=itemService.getItemByBuhin(buhin);
        }

        // kiki buhin リストから弁ID取得
        //kiki リストから弁ID取得
        for(int i=0;i<kikiResults.size();i++){
            kikiIdList.add(kikiResults.get(i).getKikisysidKiki());
        }
        //buhin　リストから弁ID取得
        for(int j=0;j<buhinResults.size();j++){
            buhinIdList.add(buhinResults.get(j).getKikisysidBuhin());
        }


        //kikisys ,kiki, buhinの三つのある場合は、空です。
        if(valveResults.size()>0 && kikiFlg && buhinFlg){
            //弁　機器　部品三つの広集を取得
            for(int x=0;x<valveResults.size();x++){
                Integer tmp=valveResults.get(x).getKikiSysId();
                if(kikiIdList.contains(tmp) && buhinIdList.contains(tmp)){
                    lastResults.add(valveResults.get(x));
                }
            }
        }else if(valveResults.size()>0 && kikiFlg){
            //弁　機器二つの取得
            for(int x=0;x<valveResults.size();x++){
                Integer tmp=valveResults.get(x).getKikiSysId();
                if(kikiIdList.contains(tmp)){
                    lastResults.add(valveResults.get(x));
                }
            }
        }else if(valveResults.size()>0 && buhinFlg){
            //弁　部品二つの取得
            for(int x=0;x<valveResults.size();x++){
                Integer tmp=valveResults.get(x).getKikiSysId();
                if(buhinIdList.contains(tmp)){
                    lastResults.add(valveResults.get(x));
                }
            }
        }else{
            lastResults=valveResults;
        }

        // 弁番号で 昇順ソート
        Collections.sort(lastResults,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return ( entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });


        session.setAttribute("valveMultResultsForKikisys",lastResults);
        session.setAttribute("valveMultSearchitemNum",lastResults.size());
        session.setAttribute("valveMultSearchForSeikak",valveMult);
        session.setAttribute("saveMultSelectedForValve","");
        return "list/valveMult";
    }

    /**
     * 弁検索ページ　弁Selected情報保存する
     * */

    @RequestMapping(value = "/saveSelectedForValve", method = RequestMethod.GET, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String saveSelectedForValve(@RequestParam("idList")String idList,@RequestParam("type")String type,HttpSession session) throws UnsupportedEncodingException {
        //文字化けを修正する
        idList = new String(idList.getBytes("ISO_8859_1"), "UTF-8");
        if("1".equals(type)){
            session.setAttribute("saveSelectedForValve",idList);
        }else if("2".equals(type)){
            session.setAttribute("saveSelectedForKiki",idList);
        }else if("3".equals(type)){
            session.setAttribute("saveSelectedForBuhin",idList);
        }else if("4".equals(type)) {
            session.setAttribute("saveMultSelectedForValve", idList);
        }

        return "true";
    }
    }
