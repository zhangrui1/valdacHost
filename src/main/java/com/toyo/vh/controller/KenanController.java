package com.toyo.vh.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toyo.vh.dto.KenanForm;
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

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lsr on 12/4/14.
 * 懸案
 */

@Controller
@RequestMapping("/kenan")
public class KenanController {

    @Autowired
    KenanService kenanService;
    @Autowired
    TenkenRirekiService tenkenRirekiService;
    @Autowired
    ItemService itemService;
    @Autowired
    KoujirelationService koujirelationService;
    @Autowired
    KoujiService koujiService;
    @Autowired
    LuceneIndexService luceneIndexService;

    /**
     * 新規懸案への準備
     *
     * @param id 点検機器(tenkenRirekiテーブル)のID
     *
     * @return String koujiIDなどの情報を含むkenanForm
     * */
    @RequestMapping(value = "/addKenanByTenkenrireki", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addKenanByTenkenrireki(@RequestParam("id")String id){
        TenkenRireki tenkenRireki = tenkenRirekiService.getTenkenRirekiById(id);
        Koujirelation koujirelation = koujirelationService.getKoujirelationById(tenkenRireki.getKoujirelationId()+"");
        Valve valve = itemService.getKikisysByKikisysId(koujirelation.getKikisysid()+"");
        Kiki kiki = itemService.getKikiByKikiId(koujirelation.getKikiid()+"");
        Kouji kouji = koujiService.getKoujiById(koujirelation.getKoujiid()+"");
        KenanForm kenanForm = new KenanForm();
        kenanForm.setKoujirelationId(koujirelation.getId());
        kenanForm.setKoujiId(kouji.getId());
        kenanForm.setkikisysId(koujirelation.getKikisysid());
        kenanForm.setKikiId(kiki.getKikiId());
        kenanForm.setKiki(kiki);
        kenanForm.setValve(valve);
        kenanForm.setKouji(kouji);
        Gson gson = new Gson();

        return gson.toJson(kenanForm);
    }

    /**
     * 新規懸案 DBに追加
     *
     * @param kenanForm 懸案Form
     *
     * @return String
     * */
    @RequestMapping(value = "/saveKenanWithForm", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveKenanWithForm(@RequestParam("kenanForm")String kenanForm,ModelMap modelMap,HttpSession session) throws IOException {
        Gson gson = new Gson();
        List<String> kenanData = gson.fromJson(kenanForm, new TypeToken<List<String>>(){}.getType());
        Kenan kenan = new Kenan();
        kenan.setKoujiId(Integer.valueOf(kenanData.get(1)));
        kenan.setKoujirelationId(Integer.valueOf(kenanData.get(2)));
        kenan.setKikiId(Integer.valueOf(kenanData.get(3)));
        kenan.setkikisysId(Integer.valueOf(kenanData.get(4)));
        kenan.setTaiouFlg(kenanData.get(5));
        kenan.setHakkenDate(kenanData.get(6));
        kenan.setTaisakuDate(kenanData.get(7));
        kenan.setHakkenJyokyo(kenanData.get(8));
        kenan.setBuhin(kenanData.get(9));
        kenan.setGensyo(kenanData.get(10));
        kenan.setYouin(kenanData.get(11));
        kenan.setTaisaku(kenanData.get(12));
        kenan.setSyotiNaiyou(kenanData.get(13));



        Kenan resultKenan=kenanService.addKenan(kenan);

        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        luceneIndexService.insertRecord(indexKenan, "KEN" + resultKenan.getId() + "End", resultKenan.toText());

        //弁テーブルの懸案フラグ更新
        if(resultKenan.taiouFlg.equals(Config.KenanNoTaiyo)){//未対応場合のみ更新
            Valve valve = itemService.getKikisysByKikisysId(resultKenan.kikisysId+"");
            if(!valve.kenanFlg.equals(Config.KenanFlg)){
                //懸案フラグは１じゃない場合、"1"に更新
                String kenanFlg="1";
                valve=itemService.updateKenanFlgByKikisysId(resultKenan.kikisysId+"",kenanFlg);
                //lucene更新
                luceneIndexService.updateRecord(indexValveFile, "VA" + valve.getKikiSysId() + "End", valve.toText());
            }
        }

        return "";
    }

    /**
     * 懸案IDにより、懸案を取得
     *
     * @param id 懸案id
     *
     * @return String 懸案情報
     * */
    @RequestMapping(value = "/getKenanByIdInSession", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKenanByIdInSession(@RequestParam("id")String id, HttpSession session){
        List<KenanForm> kenanFormList = (List<KenanForm>) session.getAttribute("kenanFormList");
        Gson gson = new Gson();
        KenanForm kenanForm = new KenanForm();
        if(!CollectionUtils.isEmpty(kenanFormList)){
            for (int i = 0; i < kenanFormList.size(); i++) {
                if(kenanFormList.get(i).getId() == Integer.valueOf(id)){
                    kenanForm = kenanFormList.get(i);
                    break;
                }
            }
        }
        return gson.toJson(kenanForm);
    }

    /**
     * 懸案検索画面で、懸案IDにより、懸案を取得
     *
     * @param id 懸案id
     *
     * @return String 懸案情報
     * */
    @RequestMapping(value = "/getKenanByIdInSessionSearch", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKenanByIdInSessionSearch(@RequestParam("id")String id, HttpSession session){
        List<KenanForm> kenanFormListSearch = (List<KenanForm>) session.getAttribute("kenanFormListSearch");
        Gson gson = new Gson();
        KenanForm kenanForm=new KenanForm();//目的の懸案を保存する
        if(!CollectionUtils.isEmpty(kenanFormListSearch)){
            for(int i=0;i<kenanFormListSearch.size();i++){
                if((kenanFormListSearch.get(i).getId()+"").equals(id)){
                    kenanForm=kenanFormListSearch.get(i);
                    break;
                }
            }
        }
        //sessionから取得できない場合、DBから取得する
        if(kenanForm==null){
            Kenan kenan=kenanService.getKenanById(id);
            Valve valve=itemService.getKikisysByKikisysId(Integer.toString(kenan.getKikisysId()));
            Kouji kouji=koujiService.getKoujiById(Integer.toString(kenan.getKoujiId()));
            Kiki kiki=itemService.getKikiByKikiId(Integer.toString(kenan.getKikiId()));
            //kenanFormに設定
            kenanForm.setKouji(kouji);
            kenanForm.setValve(valve);
            kenanForm.setKiki(kiki);

            kenanForm.setId(kenan.getId());
            kenanForm.setKoujiId(kenan.getKoujiId());
            kenanForm.setkikisysId(kenan.getKikisysId());
            kenanForm.setKikiId(kenan.getKikiId());
            kenanForm.setGensyo(kenan.getGensyo());
            kenanForm.setYouin(kenan.getYouin());
            kenanForm.setSyotiNaiyou(kenan.getSyotiNaiyou());
            kenanForm.setHakkenJyokyo(kenan.getHakkenJyokyo());
            kenanForm.setBuhin(kenan.getBuhin());
            kenanForm.setId(kenan.getId());
            kenanForm.setJisyo(kenan.getJisyo());
            kenanForm.setTaisaku(kenan.getTaisaku());
            kenanForm.setTaiouFlg(kenan.getTaiouFlg());
            kenanForm.setHakkenDate(kenan.getHakkenDate());
            kenanForm.setTaisakuDate(kenan.getTaisakuDate());

        }

        return gson.toJson(kenanForm);
    }

    /**
     * 懸案を更新
     *
     * @param kenanForm 懸案Form
     *
     * @return String 懸案リスト画面へパス
     * */
    @RequestMapping(value = "/updateKenan", method = RequestMethod.POST)
    @ResponseBody
    public String updateKenanByForm(@RequestParam("kenanForm")String kenanForm,ModelMap modelMap,HttpSession session) throws IOException {

        Gson gson = new Gson();
        List<String> kenanData = gson.fromJson(kenanForm, new TypeToken<List<String>>(){}.getType());
        Kenan kenan = new Kenan();
        kenan.setId(Integer.valueOf(kenanData.get(0)));
        kenan.setKoujiId(Integer.valueOf(kenanData.get(1)));
        kenan.setKoujirelationId(Integer.valueOf(kenanData.get(2)));
        kenan.setKikiId(Integer.valueOf(kenanData.get(3)));
        kenan.setkikisysId(Integer.valueOf(kenanData.get(4)));
        kenan.setTaiouFlg(kenanData.get(5));
        kenan.setHakkenDate(kenanData.get(6));
        kenan.setTaisakuDate(kenanData.get(7));
        kenan.setHakkenJyokyo(kenanData.get(8));
        kenan.setBuhin(kenanData.get(9));
        kenan.setGensyo(kenanData.get(10));
        kenan.setYouin(kenanData.get(11));
        kenan.setTaisaku(kenanData.get(12));
        kenan.setSyotiNaiyou(kenanData.get(13));

        kenan=kenanService.updateKenan(kenan);
        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        luceneIndexService.updateRecord(indexKenan, "KEN" + kenan.getId() + "End", kenan.toText());

        //
        //弁テーブルの懸案フラグ更新
        List<Kenan> kenans=kenanService.getKenanByKikisysId(kenan.kikisysId+"");
        Valve valve = itemService.getKikisysByKikisysId(kenan.kikisysId+"");
        if(kenans.size()>0){//未対応がある
            if(!valve.kenanFlg.equals(Config.KenanFlg)){//懸案フラグは０場合は、更新
                //懸案フラグは１じゃない場合、"1"に更新
                String kenanFlg="1";
                valve=itemService.updateKenanFlgByKikisysId(kenan.kikisysId+"",kenanFlg);
                //lucene更新
                luceneIndexService.updateRecord(indexValveFile, "VA" + valve.getKikiSysId() + "End", valve.toText());
            }
        }else{
            if(valve.kenanFlg.equals(Config.KenanFlg)){//懸案フラグは０場合は、更新
                //懸案フラグは１じゃない場合、"1"に更新
                String kenanFlg="0";
                valve=itemService.updateKenanFlgByKikisysId(kenan.kikisysId+"",kenanFlg);
                //lucene更新
                luceneIndexService.updateRecord(indexValveFile, "VA" + valve.getKikiSysId() + "End", valve.toText());
            }
        }

        return "true";
    }


    /**
     * 懸案IDにより、懸案を削除
     *
     * @param kenanId 懸案id
     *
     * @return String 懸案リスト画面へパス
     * */
    @RequestMapping(value = "/deleteKenanByKenanId", method = RequestMethod.POST)
    @ResponseBody
    public String deleteKenanByKenanId(@RequestParam("kenanId")String kenanId,ModelMap modelMap,HttpSession session) throws IOException {
        //DB更新
        Kenan kenan = kenanService.getKenanById(kenanId);
        kenanService.deleteKenan(kenan);
        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));
        Directory indexValveFile=FSDirectory.open(new File(indexPath.get("indexValveFile")));
        luceneIndexService.deleteRecord(indexKenan, "KEN" + kenanId + "End");

        //懸案フラグ
        //弁テーブルの懸案フラグ更新
        List<Kenan> kenans=kenanService.getKenanByKikisysId(kenan.kikisysId+"");
        Valve valve = itemService.getKikisysByKikisysId(kenan.kikisysId+"");
        if(kenans.size()>0){//未対応がある
            if(!valve.kenanFlg.equals(Config.KenanFlg)){//懸案フラグは０場合は、更新
                //懸案フラグは１じゃない場合、"1"に更新
                String kenanFlg="1";
                valve=itemService.updateKenanFlgByKikisysId(kenan.kikisysId+"",kenanFlg);
                //lucene更新
                luceneIndexService.updateRecord(indexValveFile, "VA" + valve.getKikiSysId() + "End", valve.toText());
            }
        }else{
            if(valve.kenanFlg.equals(Config.KenanFlg)){//懸案フラグは０場合は、更新
                //懸案フラグは１じゃない場合、"1"に更新
                String kenanFlg="0";
                valve=itemService.updateKenanFlgByKikisysId(kenan.kikisysId+"",kenanFlg);
                //lucene更新
                luceneIndexService.updateRecord(indexValveFile, "VA" + valve.getKikiSysId() + "End", valve.toText());
            }
        }


        return "redirect:/kouji/"+kenan.getKoujiId()+"/kenan";
    }

    /**
     * kenanIDにより、懸案を取得
     * @param kenanId 懸案ID
     *
     * @return String 懸案リスト
     *
     * */
    @RequestMapping(value = "/getKenanByKenanId", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKenanByKenanId(@RequestParam("kenanId")String kenanId, ModelMap modelMap, HttpSession session) throws IOException {

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

        //kenan 追加
        Kenan kenan = new Kenan();
        KenanForm kenanForm = new KenanForm();

        List<SearchResultObject> searchResultObjectsKJ=luceneIndexService.selectRecord(indexKenan,"KEN"+kenanId+"End");
        if(searchResultObjectsKJ.size()>0) {
            Kenan tmpKenan = new Kenan();
            kenan = tmpKenan.toKenan(searchResultObjectsKJ.get(0).getBody());

            KenanForm TmpkenanForm = new KenanForm();
            kenanForm = TmpkenanForm.copyKenanForm(kenan);

            //kouji 追加
            List<SearchResultObject> searchResultObjectsKouji=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+kenan.getKoujiId()+"End");
            if(searchResultObjectsKouji.size()>0) {
                Kouji tmpKouji = new Kouji();
                Kouji kouji = tmpKouji.toKouji(searchResultObjectsKouji.get(0).getBody());
                kenanForm.setKouji(kouji);
            }else{
                Kouji kouji=new Kouji();
                kenanForm.setKouji(kouji);
            }
            //kiki追加
            List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+kenan.getKikiId()+"End");
            if(searchResultObjectsKiki.size()>0){
                Kiki TmpKiki=new Kiki();
                Kiki kiki=TmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                kenanForm.setKiki(kiki);
            }else{
                Kiki kiki=new Kiki();
                kenanForm.setKiki(kiki);
            }
            //valve追加
            List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,"VA"+kenan.getKikisysId()+"End");
            if(searchResultObjectsVA.size()>0){
                Valve tmpValve=new Valve();
                Valve valve=tmpValve.toValve(searchResultObjectsVA.get(0).getBody());
                kenanForm.setValve(valve);
            }else{
                Valve valve=new Valve();
                kenanForm.setValve(valve);
            }

        }



        Gson gson = new Gson();

        return gson.toJson(kenanForm);
    }



    /**
     * 懸案検索画面で、懸案を更新
     *
     * @param kenanForm 懸案Form
     *
     * @return String 懸案検索画面パス
     * */
    @RequestMapping(value = "/updateKenanSearch", method = RequestMethod.POST)
    public String updateKenanByFormSearch(@ModelAttribute("kenanForm")KenanForm kenanForm,ModelMap modelMap,HttpSession session) throws IOException {
        String keyword=(String) session.getAttribute("keyword");
        List<KenanForm> kenanFormListSearch=(List<KenanForm>) session.getAttribute("kenanFormListSearch");
        //DB更新
        Kenan kenan=kenanService.updateKenan(kenanForm);
        //session更新
        if(!CollectionUtils.isEmpty(kenanFormListSearch)){
            for(int i=0;i<kenanFormListSearch.size();i++){
                if(kenanForm.getId()==kenanFormListSearch.get(i).getId()){
                    KenanForm resultKenan=new KenanForm();
                    KenanForm tmpkenanForm=resultKenan.copyKenanForm(kenanForm);
                    tmpkenanForm.setValve(kenanFormListSearch.get(i).getValve());
                    tmpkenanForm.setKiki(kenanFormListSearch.get(i).getKiki());
                    tmpkenanForm.setKouji(kenanFormListSearch.get(i).getKouji());
                    kenanFormListSearch.remove(i);
                    kenanFormListSearch.add(tmpkenanForm);
                    break;
                }
            }
        }
        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        luceneIndexService.updateRecord(indexKenan, "KEN" + kenanForm.getId() + "End", kenanForm.toText());

        session.setAttribute("kenanFormListSearch",kenanFormListSearch);
        session.setAttribute("keyword",keyword);
        modelMap.addAttribute("kenanFormListSearch",kenanFormListSearch);
        modelMap.addAttribute("keyword",keyword);
        return "/list/kenan";
    }

    /**
     * 懸案IDにより、懸案を削除
     *
     * @param id 懸案id
     *
     * @return String 懸案リスト画面へパス
     * */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id")String id,ModelMap modelMap,HttpSession session) throws IOException {
        //DB更新
        Kenan kenan = kenanService.getKenanById(id);
        kenanService.deleteKenan(kenan);
        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));
        luceneIndexService.deleteRecord(indexKenan, "KEN" + id + "End");


        return "redirect:/kouji/"+kenan.getKoujiId()+"/kenan";
    }

    /**
     * 懸案検索画面で、懸案IDにより、懸案を削除
     *
     * @param id 懸案id
     *
     * @return String
     * */
    @RequestMapping(value = "/deleteSearch", method = RequestMethod.GET)
    @ResponseBody
    public String deleteByIdSearch(@ModelAttribute("id")String id,ModelMap modelMap,HttpSession session) throws IOException {
        //DB更新
        Kenan kenan = kenanService.getKenanById(id);
        kenanService.deleteKenan(kenan);

        //session更新
        List<KenanForm> kenanFormListSearch=(List<KenanForm>) session.getAttribute("kenanFormListSearch");
        if(!CollectionUtils.isEmpty(kenanFormListSearch)){
            for(KenanForm tmpkenanForm:kenanFormListSearch){
                if(tmpkenanForm.getId()==Integer.parseInt(id)){
                    kenanFormListSearch.remove(tmpkenanForm);
                    break;
                }
            }
        }
        //lucene更新
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));
        luceneIndexService.deleteRecord(indexKenan, "KEN" + id + "End");

        return "true";
    }

    /**
     * 懸案検索
     *
     * @param keyword 検索キーワード(弁情報で)
     *
     * @return String　弁に登録した全ての懸案事項
     * */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchKenanByKikisysId(@ModelAttribute("keyword")String keyword, ModelMap modelMap,HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        List<Valve> valves =new ArrayList<Valve>();
        List<KenanForm> kenanFormListSearch = new ArrayList<KenanForm>();
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

        //keywordが空の場合、sessionから取得
        if (keyword==null){
            keyword=(String)session.getAttribute("KenanKeyword");
        }
        //sessionのkeywordも空の場合は、検索完了
        if(keyword!=null){
            if(user == null){
                return "login";
            } else {
                //キーワードから弁リストを取得
                List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,keyword);
                System.out.println("valve 件数=" + searchResultObjectsVA.size());
                if(searchResultObjectsVA.size()>0){
                    for(SearchResultObject searchResultObject:searchResultObjectsVA){
                        if(searchResultObject.getId().contains("VA")){
                            Valve tmpValve=new Valve();
                            Valve valve=tmpValve.toValve(searchResultObject.getBody());
                            valves.add(valve);
                        }
                    }
                }
                Gson gson=new Gson();

                if(!CollectionUtils.isEmpty(valves)){
                    for (int i = 0; i < valves.size(); i++) {

//                    List<Koujirelation> koujirelations=koujirelationService.getKoujirelationBySystemId(Integer.toString(valves.get(i).getKikiSysId()));
                        List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"VA"+valves.get(i).getKikiSysId()+"End");
                        if(searchResultObjectsKR.size()>0){
                            for(SearchResultObject searchResultObject:searchResultObjectsKR){
                                //弁IDからkoujirelationを取得
                                Koujirelation Tmpkoujirelation=new Koujirelation();
                                Koujirelation koujirelation=Tmpkoujirelation.toKoujirelation(searchResultObject.getBody());


                                //koujirelation　IDから懸案取得
                                List<SearchResultObject> searchResultObjectsKenan=luceneIndexService.selectRecord(indexKenan,"KR"+koujirelation.getId()+"End");
                                if(searchResultObjectsKenan.size()>0){
                                    for(SearchResultObject searchResultObject1:searchResultObjectsKenan){
                                        Kenan tmpKenan =new Kenan();
                                        Kenan kenan=tmpKenan.toKenan(searchResultObject1.getBody());

                                        KenanForm kenanForm=new KenanForm();
                                        KenanForm TmpkenanForm=new KenanForm();
                                        kenanForm=TmpkenanForm.copyKenanForm(kenan);

                                        //kiki取得
                                        Kiki kiki=new Kiki();
                                        List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+koujirelation.getKikiid()+"End");
                                        if(searchResultObjectsKiki.size()>0){
                                            Kiki TmpKiki=new Kiki();
                                            kiki=TmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                                        }
                                        kenanForm.setKiki(kiki);

                                        //工事取得
                                        Kouji kouji=new Kouji();
                                        if(koujirelation.getKoujiid()!=0) {
                                            //kouji情報取得し、存在する場合、点検機器情報取得
                                            List<SearchResultObject> searchResultObjectsKJ = luceneIndexService.selectRecord(indexKoujiFile, "KJ" + koujirelation.getKoujiid() + "End");
                                            if (searchResultObjectsKJ.size() > 0) {
                                                Kouji tmpKouji = new Kouji();
                                                kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());

                                            }
                                        }
                                        kenanForm.setKouji(kouji);
                                        kenanForm.setValve(valves.get(i));
                                        kenanFormListSearch.add(kenanForm);
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
        //make cache
        session.setAttribute("kenanFormListSearch",kenanFormListSearch);
        session.setAttribute("KenanKeyword",keyword);
        modelMap.addAttribute("kenanFormListSearch",kenanFormListSearch);
        modelMap.addAttribute("KenanKeyword",keyword);
        return "/list/kenan";
    }

    /**
     * 弁IDにより、懸案を取得
     *
     * @param kikiSysId 弁ID
     * @param kikiOrBenFlg 検索種類フラグ
     *
     * @return String　弁に登録した全ての懸案事項
     * */
    @RequestMapping(value = "/kenan/{kikiSysId}/{kikiOrBenFlg}/valve", method = RequestMethod.GET)
    public String getKenanByKikisysId(@PathVariable String kikiSysId,@PathVariable String kikiOrBenFlg, ModelMap modelMap,HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
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

        Valve valve=new Valve();
        List<KenanForm> kenanFormListSearch = new ArrayList<KenanForm>();
        //valve検索
        List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiSysId+"End");
        if(!CollectionUtils.isEmpty(resultsValve)){
            Valve tmpValve=new Valve();
            valve=tmpValve.toValve(resultsValve.get(0).getBody());
        }
        List<Kenan> kenans=kenanService.getKenanByKikisysIdForAll(kikiSysId);
        if(!CollectionUtils.isEmpty(kenans)){
            for(int i=0;i<kenans.size();i++){

                KenanForm kenanForm = new KenanForm();
                KenanForm TmpkenanForm = new KenanForm();

                kenanForm = TmpkenanForm.copyKenanForm(kenans.get(i));

                //工事取得
                Kouji kouji=new Kouji();
                if(kenans.get(i).getKoujiId()!=0) {
                    //kouji情報取得し、存在する場合、点検機器情報取得
                    List<SearchResultObject> searchResultObjectsKJ = luceneIndexService.selectRecord(indexKoujiFile, "KJ" + kenans.get(i).getKoujiId() + "End");
                    if (searchResultObjectsKJ.size() > 0) {
                        Kouji tmpKouji = new Kouji();
                        kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
                    }
                }
                kenanForm.setKouji(kouji);

                //valve設定
                kenanForm.setValve(valve);
                kenanFormListSearch.add(kenanForm);
            }
        }


        //make cache
        session.setAttribute("kenanFormListSearch",kenanFormListSearch);
        session.setAttribute("valve",valve);
        modelMap.addAttribute("kenanFormListSearch",kenanFormListSearch);
        modelMap.addAttribute("valve",valve);

        //1場合は、弁検索へ戻る、２場合は、機器検索へ戻る、３場合は、部品検索へ戻る
        if(kikiOrBenFlg.equals("1")){
            return "valve/kenan";
        }else if(kikiOrBenFlg.equals("2")){
            return "kikiSearch/kikikenan";
        }else if(kikiOrBenFlg.equals("3")){
            return "buhinSearch/buhinkenan";
        }else{
            return "valveMultSearch/valveMultkenan";
        }
    }
}
