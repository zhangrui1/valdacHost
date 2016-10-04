package com.toyo.vh.controller;

import com.google.gson.Gson;
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

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Lsr on 11/26/14.
 */

@Controller
@RequestMapping("/tenken")
public class TenkenController {

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
     * 点検ランクを一括更新
     * @param koujiId 工事ID
     * @param tenkenrank 点検ランク
     * @param tenkennaiyo 点検内容
     *
     * @return  点検ランク設定完了フラグ
     * */
    @RequestMapping(value = "/saveTenkenrankAllByKoujiID", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveTenkenrankAllByKoujiID(@RequestParam("id")String koujiId,
                                  @RequestParam("tenkenrank")String tenkenrank,
                                  @RequestParam("tenkennaiyo")String tenkennaiyo,
                                  HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //append update Date
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");

        TenkenRireki tenkenRireki=new TenkenRireki();

        //点検ランク設定 DB更新
        if(tenkenrank.length() < 1) {
            tenkenRireki=tenkenRirekiService.updateTenkenRankByKoujiId(koujiId, tenkenrank, tenkennaiyo,"未完成");
        } else {
            tenkenRireki=tenkenRirekiService.updateTenkenRankByKoujiId(koujiId, tenkenrank, tenkennaiyo,"完成");
        }
        //session更新 lucene更新
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenrank.length() < 1) {
                tenkenRirekiUtilList.get(i).setTenkenRank(tenkenrank);
                tenkenRirekiUtilList.get(i).setTenkennaiyo(tenkennaiyo);
                tenkenRirekiUtilList.get(i).setUpdDate(tenkenRireki.getUpdDateInt()+"");
                tenkenRirekiUtilList.get(i).setTenkenDate(tenkenRireki.getTenkenDate());
                tenkenRirekiUtilList.get(i).setTenkenNendo(tenkenRireki.getTenkenNendo());
                tenkenRirekiUtilList.get(i).setKanryoFlg("未完成");
            } else {
                tenkenRirekiUtilList.get(i).setTenkenRank(tenkenrank);
                tenkenRirekiUtilList.get(i).setTenkennaiyo(tenkennaiyo);
                tenkenRirekiUtilList.get(i).setUpdDate(tenkenRireki.getUpdDateInt()+"");
                tenkenRirekiUtilList.get(i).setTenkenDate(tenkenRireki.getTenkenDate());
                tenkenRirekiUtilList.get(i).setTenkenNendo(tenkenRireki.getTenkenNendo());
                tenkenRirekiUtilList.get(i).setKanryoFlg("完成");
                //lucene更新
                luceneIndexService.updateRecord(indexTenkenRireki,"TR"+tenkenRirekiUtilList.get(i).getId()+"End",tenkenRirekiUtilList.get(i).toText());
            }
        }

        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        return "";
    }

    /**
     * 点検ランクを更新
     *
     * @param rirekiId 点検機器履歴ID
     * @param tenkenrank 点検ランク
     *
     * @return  点検ランク設定完了フラグ
     * */
    @RequestMapping(value = "/saveTenkenrank", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveTenkenrank(@RequestParam("id")String rirekiId,
                                 @RequestParam("tenkenrank")String tenkenrank,
                                 @RequestParam("tenkennaiyo")String tenkennaiyo,
                                 HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //Lucene検索できない場合、データベースから取得
        List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"TR"+rirekiId+"End");
        TenkenRireki tenkenRireki=new TenkenRireki();
        if(searchResultObjectsTR.size()>0){
            TenkenRireki tmptenkenRireki=new TenkenRireki();
            tenkenRireki=tmptenkenRireki.toTenkenRireki(searchResultObjectsTR.get(0).getBody());
        }else{
            tenkenRireki = tenkenRirekiService.getTenkenRirekiById(rirekiId);
        }

        //点検ランク設定
        if(tenkenrank.length() < 1) {
            tenkenRireki.setKanryoFlg("未完成");
            tenkenRireki.setTenkenRank(tenkenrank);
            tenkenRireki.setTenkennaiyo(tenkennaiyo);
        } else {
            tenkenRireki.setTenkenRank(tenkenrank);
            tenkenRireki.setTenkennaiyo(tenkennaiyo);
            tenkenRireki.setKanryoFlg("完成");
        }
        //DB更新
        tenkenRireki=tenkenRirekiService.updateTenkenRireki(tenkenRireki);


        //session更新
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenRireki.getId() == tenkenRirekiUtilList.get(i).getId()){
                if(tenkenrank.length() < 1) {
                    tenkenRirekiUtilList.get(i).setKanryoFlg("未完成");
                    tenkenRirekiUtilList.get(i).setTenkenRank(tenkenrank);
                    tenkenRirekiUtilList.get(i).setTenkennaiyo(tenkennaiyo);
                } else {
                    tenkenRirekiUtilList.get(i).setTenkenRank(tenkenrank);
                    tenkenRirekiUtilList.get(i).setTenkennaiyo(tenkennaiyo);
                    tenkenRirekiUtilList.get(i).setTenkenNendo(tenkenRireki.getTenkenNendo());
                    tenkenRirekiUtilList.get(i).setTenkenDate(tenkenRireki.getTenkenDate());
                }
                break;
            }
        }


        //lucene更新
        luceneIndexService.updateRecord(indexTenkenRireki,"TR"+rirekiId+"End",tenkenRireki.toText());

        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        return tenkenRireki.getKanryoFlg();
    }

    /**
     * 点検履歴検索画面で、点検ランクを更新
     *
     * @param rirekiId 点検機器履歴ID
     * @param tenkenrank 点検ランク
     *
     * @return  点検ランク設定完了フラグ
     * */
    @RequestMapping(value = "/saveTenkenrankSearch", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveTenkenrankSearch(@RequestParam("id")String rirekiId,
                                 @RequestParam("tenkenrank")String tenkenrank,@RequestParam("tenkennaiyo")String tenkennaiyo,
                                 HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //Lucene検索できない場合、データベースから取得
        List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"TR"+rirekiId+"End");
        TenkenRireki tenkenRireki=new TenkenRireki();
        if(searchResultObjectsTR.size()>0){
            TenkenRireki tmptenkenRireki=new TenkenRireki();
            tenkenRireki=tmptenkenRireki.toTenkenRireki(searchResultObjectsTR.get(0).getBody());
        }else{
            tenkenRireki = tenkenRirekiService.getTenkenRirekiById(rirekiId);
        }

        //点検ランク設定
        if(tenkenrank.length() < 1) {
            tenkenRireki.setKanryoFlg("未完成");
            tenkenRireki.setTenkenRank(tenkenrank);
            tenkenRireki.setTenkennaiyo(tenkennaiyo);
        } else {
            tenkenRireki.setTenkenRank(tenkenrank);
            tenkenRireki.setTenkennaiyo(tenkennaiyo);
            tenkenRireki.setKanryoFlg("完成");
        }

        List<TenkenRirekiUtil> tenkenRirekiListSearch = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiListSearch");
        if(!CollectionUtils.isEmpty(tenkenRirekiListSearch)){
            for (int i = 0; i < tenkenRirekiListSearch.size(); i++) {
                if(tenkenRireki.getId() == tenkenRirekiListSearch.get(i).getId()){
                    if(tenkenrank.length() < 1) {
                        tenkenRirekiListSearch.get(i).setKanryoFlg("未完成");
                        tenkenRirekiListSearch.get(i).setTenkenRank(tenkenrank);
                        tenkenRirekiListSearch.get(i).setTenkennaiyo(tenkennaiyo);
                    } else {
                        tenkenRirekiListSearch.get(i).setKanryoFlg("完成");
                        tenkenRirekiListSearch.get(i).setTenkenRank(tenkenrank);
                        tenkenRirekiListSearch.get(i).setTenkennaiyo(tenkennaiyo);
                    }
                    break;
                }
            }
        }

        session.setAttribute("tenkenRirekiListSearch",tenkenRirekiListSearch);
        //DB更新
        tenkenRirekiService.updateTenkenRireki(tenkenRireki);

        //lucene更新
        luceneIndexService.updateRecord(indexTenkenRireki,"TR"+rirekiId+"End",tenkenRireki.toText());

        return tenkenRireki.getKanryoFlg();
    }

    /**
     * 点検備考を更新
     *
     * @param rirekiId 点検機器履歴ID
     * @param tenkenBikou 点検結果備考
     *
     * @return String
     * */
    @RequestMapping(value = "/saveTenkenBikou", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveTenkenBikou(@RequestParam("id")String rirekiId,
                                  @RequestParam("tenkenBikou")String tenkenBikou,
                                  HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //Lucene検索できない場合、データベースから取得
        List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"TR"+rirekiId+"End");
        TenkenRireki tenkenRireki=new TenkenRireki();
        if(searchResultObjectsTR.size()>0){
            TenkenRireki tmptenkenRireki=new TenkenRireki();
            tenkenRireki=tmptenkenRireki.toTenkenRireki(searchResultObjectsTR.get(0).getBody());
        }else{
            tenkenRireki = tenkenRirekiService.getTenkenRirekiById(rirekiId);
        }


        String result = "";
        if(tenkenRireki.getTenkenBikou() == null){
            tenkenRireki.setTenkenBikou(tenkenBikou);
            tenkenRirekiService.updateTenkenRireki(tenkenRireki);
            result = "1";
        } else {
            if (!tenkenRireki.getTenkenBikou().equals(tenkenBikou)) {
                tenkenRireki.setTenkenBikou(tenkenBikou);
                tenkenRirekiService.updateTenkenRireki(tenkenRireki);
                result = "1";
            } else {
                result = "0";
            }
        }

        //DB更新
        tenkenRireki=tenkenRirekiService.updateTenkenRireki(tenkenRireki);

        // update session cache
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenRireki.getId() == tenkenRirekiUtilList.get(i).getId()){
                tenkenRirekiUtilList.get(i).setTenkenBikou(tenkenBikou);
                tenkenRirekiUtilList.get(i).setTenkenNendo(tenkenRireki.getTenkenNendo());
                tenkenRirekiUtilList.get(i).setTenkenDate(tenkenRireki.getTenkenDate());
                break;
            }
        }
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);

        //lucene更新
        luceneIndexService.updateRecord(indexTenkenRireki, "TR" + rirekiId + "End", tenkenRireki.toText());

        return result;
    }

    /**
     * 点検結果を更新
     *
     * @param rirekiId 点検機器履歴ID
     * @param tenkenkekka 点検結果
     *
     * @return String
     * */
    @RequestMapping(value = "/saveTenkenkekka", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveTenkenkekka(@RequestParam("id")String rirekiId,
                                  @RequestParam("tenkenkekka")String tenkenkekka,
                                  HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //Lucene検索できない場合、データベースから取得
        List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"TR"+rirekiId+"End");
        TenkenRireki tenkenRireki=new TenkenRireki();
        if(searchResultObjectsTR.size()>0){
            TenkenRireki tmptenkenRireki=new TenkenRireki();
            tenkenRireki=tmptenkenRireki.toTenkenRireki(searchResultObjectsTR.get(0).getBody());
        }else{
            tenkenRireki = tenkenRirekiService.getTenkenRirekiById(rirekiId);
        }


        String result = "";
        if(tenkenRireki.getTenkenkekka() == null){
            tenkenRireki.setTenkenkekka(tenkenkekka);
            tenkenRirekiService.updateTenkenRireki(tenkenRireki);
            tenkenRireki.setKanryoFlg("完成");
            result = "1";
        } else {
            if (!tenkenRireki.getTenkenkekka().equals(tenkenkekka)) {
                tenkenRireki.setTenkenkekka(tenkenkekka);
                tenkenRirekiService.updateTenkenRireki(tenkenRireki);
                tenkenRireki.setKanryoFlg("完成");
                result = "1";
            } else {
                tenkenRireki.setKanryoFlg("未完成");
                result = "0";
            }
        }
        //DB更新
        tenkenRireki=tenkenRirekiService.updateTenkenRireki(tenkenRireki);

        // update session cache
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenRireki.getId() == tenkenRirekiUtilList.get(i).getId()){
                tenkenRirekiUtilList.get(i).setTenkenkekka(tenkenkekka);
                tenkenRirekiUtilList.get(i).setTenkenNendo(tenkenRireki.getTenkenNendo());
                tenkenRirekiUtilList.get(i).setTenkenDate(tenkenRireki.getTenkenDate());
                tenkenRirekiUtilList.get(i).setKanryoFlg("完成");
                break;
            }
        }
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);

        //lucene更新
        luceneIndexService.updateRecord(indexTenkenRireki, "TR" + rirekiId + "End", tenkenRireki.toText());

        return result;
    }

    /**
     * page数により、点検機器を取得
     *
     * @param currentPage page数
     *
     * @return String
     * */
    @RequestMapping(value = "/getTenkenrirekiByPage", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTenkenrirekiByPage(@RequestParam("currentPage")String currentPage,
                                        HttpSession session){

        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        //page control
        int countInOnePage = 10000;
        int pageCount = 0;
        int currentIndex = countInOnePage * Integer.valueOf(currentPage);
        List<TenkenRirekiUtil> tenkenRirekiUtils = new ArrayList<TenkenRirekiUtil>();
        pageCount = tenkenRirekiUtilList.size()/countInOnePage;
        if(tenkenRirekiUtilList.size()%countInOnePage != 0){
            pageCount++;
        }
        int addCount = currentIndex + countInOnePage;
        if(tenkenRirekiUtilList.size() < addCount){
            addCount = tenkenRirekiUtilList.size();
        }
        for (int i = currentIndex; i < addCount; i++) {
            tenkenRirekiUtils.add(tenkenRirekiUtilList.get(i));
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("pageCount", pageCount);
        result.put("currentPage", Integer.valueOf(currentPage));
        result.put("tenkenRirekiList", tenkenRirekiUtils);

        Gson gson = new Gson();
        return gson.toJson(result);
    }

    /**
     * page数により、点検機器を取得
     *
     * @param currentPage page数
     *
     * @return String
     * */
    @RequestMapping(value = "/getTenkenrirekiHitoryByPage", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTenkenrirekiHitoryByPage(@RequestParam("currentPage")String currentPage,
                                              HttpSession session){
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiHistory");
        //page control
        int countInOnePage = 10;
        int pageCount = 0;
        int currentIndex = countInOnePage * Integer.valueOf(currentPage);
        List<TenkenRirekiUtil> tenkenRirekiUtils = new ArrayList<TenkenRirekiUtil>();
        pageCount = tenkenRirekiUtilList.size()/countInOnePage;
        if(tenkenRirekiUtilList.size()%countInOnePage != 0){
            pageCount++;
        }
        int addCount = currentIndex + countInOnePage;
        if(tenkenRirekiUtilList.size() < addCount){
            addCount = tenkenRirekiUtilList.size();
        }
        for (int i = currentIndex; i < addCount; i++) {
            tenkenRirekiUtils.add(tenkenRirekiUtilList.get(i));
        }

        Map<String,Object> result = new HashMap<String, Object>();
        result.put("pageCount", pageCount);
        result.put("currentPage", Integer.valueOf(currentPage));
        result.put("tenkenRirekiList", tenkenRirekiUtils);

        Gson gson = new Gson();
        return gson.toJson(result);
    }

    @RequestMapping(value = "/getListNumber", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getListNumber(HttpSession session){
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        int total = tenkenRirekiUtilList.size();
        int complete = 0;
        int incomplete = 0;
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenRirekiUtilList.get(i).getTenkenRank().length() > 0){
                complete++;
            } else {
                incomplete++;
            }
        }

        Map<String, Integer> numbers = new HashMap<String, Integer>();
        numbers.put("total",total);
        numbers.put("complete",complete);
        numbers.put("incomplete",incomplete);
        Gson gson = new Gson();
        return gson.toJson(numbers);
    }

    @RequestMapping(value = "/getKekkaListNumber", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKekkaListNumber(HttpSession session){
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        int total = tenkenRirekiUtilList.size();
        int complete = 0;
        int incomplete = 0;
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenRirekiUtilList.get(i).getTenkenkekka().length() > 0){
                complete++;
            } else {
                incomplete++;
            }
        }

        Map<String, Integer> numbers = new HashMap<String, Integer>();
        numbers.put("total",total);
        numbers.put("complete",complete);
        numbers.put("incomplete",incomplete);
        Gson gson = new Gson();
        return gson.toJson(numbers);
    }

    /**
     * 点検機器を削除
     *
     * @param id 点検機器履歴ID
     *
     * @return String
     * */
    @RequestMapping(value = "/deleteTenkenRirekiByTenkenrireki", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteTenkenRirekiByTenkenrireki(@RequestParam("id")String id,HttpSession session) throws IOException {

        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        //DBから削除
        TenkenRireki tenkenrireki=new TenkenRireki();
        tenkenrireki.setId(Integer.valueOf(id));
        tenkenrireki=tenkenRirekiService.getTenkenRirekiById(id);
        //koujirelation削除
        Koujirelation koujirelation=new Koujirelation();
        if(tenkenrireki!=null){
            koujirelation.setId(tenkenrireki.getKoujirelationId());
            koujirelationService.deleteKoujirelation(koujirelation);
        }
        //tenkenKiki削除
        tenkenRirekiService.deleteTenkenRireki(tenkenrireki);


        //sessionからも削除する
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenRirekiUtilList.get(i).getId()==Integer.valueOf(id)){
                tenkenrireki.setKoujirelationId(tenkenRirekiUtilList.get(i).getKoujirelationId());
                tenkenRirekiUtilList.remove(i);
            }
        }
        //点検機器追加ページのsession
        List<ValveKikiSelectUtil> valveKikiSelectUtilList = (List<ValveKikiSelectUtil>) session.getAttribute(tenkenrireki.getKoujiId()+"");
        if(! CollectionUtils.isEmpty(valveKikiSelectUtilList)){
            //sessionから点検情報削除
            for (int i = 0; i < valveKikiSelectUtilList.size(); i++) {
                if((valveKikiSelectUtilList.get(i).getKiki().getKikiId())==(tenkenrireki.getKikiId())){
                    valveKikiSelectUtilList.get(i).setStatus("");
                }
            }
        }

        //luceneから削除
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));

        luceneIndexService.deleteRecord(indexTenkenRireki, "TR" + id + "End");
        luceneIndexService.deleteRecord(indexKoujirelationFile,"KR"+tenkenrireki.getKoujirelationId()+"End");

        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);
        session.setAttribute(tenkenrireki.getKoujiId()+"",valveKikiSelectUtilList);

        return  "true";
    }

    /**
     * 点検機器検索画面で、点検機器を削除
     *
     * @param id 点検機器履歴ID
     *
     * @return String
     * */
    @RequestMapping(value = "/deleteTenkenRirekiByTenkenrirekiSearch", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteTenkenRirekiByTenkenrirekiSearch(@RequestParam("id")String id,HttpSession session) throws IOException {

        List<TenkenRirekiUtil> tenkenRirekiListSearch = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiListSearch");
        //DB削除
        TenkenRireki tenkenrireki=new TenkenRireki();
        tenkenrireki.setId(Integer.valueOf(id));
        tenkenrireki=tenkenRirekiService.getTenkenRirekiById(id);
        //koujirelation削除
        Koujirelation koujirelation=new Koujirelation();
        if(tenkenrireki!=null){
            koujirelation.setId(tenkenrireki.getKoujirelationId());
            koujirelationService.deleteKoujirelation(koujirelation);
        }
        //tenkenKiki削除
        tenkenRirekiService.deleteTenkenRireki(tenkenrireki);

        //sessionからも削除する
        if(!CollectionUtils.isEmpty(tenkenRirekiListSearch)){
            for (int i = 0; i < tenkenRirekiListSearch.size(); i++) {
                if(tenkenRirekiListSearch.get(i).getId()==Integer.valueOf(id)){
                    tenkenRirekiListSearch.remove(i);
                    tenkenrireki.setKoujirelationId(tenkenRirekiListSearch.get(i).getKoujirelationId());
                }
            }
        }
        //luceneから削除
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));

        luceneIndexService.deleteRecord(indexTenkenRireki, "TR" + id + "End");
        luceneIndexService.deleteRecord(indexKoujirelationFile,"KR"+tenkenrireki.getKoujirelationId()+"End");


        session.setAttribute("tenkenRirekiListSearch",tenkenRirekiListSearch);
        return  "true";
    }

    /**
     * 点検内容を保存
     *
     * @param rirekiId 点検機器履歴ID
     * @param tenkennaiyo 点検内容
     *
     * @return String
     * */
    @RequestMapping(value = "/saveTenkennaiyo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
         @ResponseBody
         public String saveTenkennaiyo(@RequestParam("id")String rirekiId,
                                       @RequestParam("tenkennaiyo")String tenkennaiyo,
                                       HttpSession session) throws IOException {
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //Lucene検索できない場合、データベースから取得
        List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"TR"+rirekiId+"End");
        TenkenRireki tenkenRireki=new TenkenRireki();
        if(searchResultObjectsTR.size()>0){
            TenkenRireki tmptenkenRireki=new TenkenRireki();
            tenkenRireki=tmptenkenRireki.toTenkenRireki(searchResultObjectsTR.get(0).getBody());
        }else{
            tenkenRireki = tenkenRirekiService.getTenkenRirekiById(rirekiId);
        }

        //DB更新
        tenkenRireki.setTenkennaiyo(tenkennaiyo);
        tenkenRireki=tenkenRirekiService.updateTenkenRireki(tenkenRireki);


        //session更新
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        if(!CollectionUtils.isEmpty(tenkenRirekiUtilList)){
            for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
                if(tenkenRireki.getId() == tenkenRirekiUtilList.get(i).getId()){
                    tenkenRirekiUtilList.get(i).setTenkennaiyo(tenkennaiyo);
                    tenkenRirekiUtilList.get(i).setTenkenNendo(tenkenRireki.getTenkenNendo());
                    tenkenRirekiUtilList.get(i).setTenkenDate(tenkenRireki.getTenkenDate());
                    break;
                }
            }
        }
        session.setAttribute("tenkenRirekiUtilList",tenkenRirekiUtilList);


        //lucene更新
        luceneIndexService.updateRecord(indexTenkenRireki, "TR" + rirekiId + "End", tenkenRireki.toText());
        return "";

    }

    /**
     * 検索画面で,点検内容を保存
     *
     * @param rirekiId 点検機器履歴ID
     * @param tenkennaiyo 点検内容
     *
     * @return String
     * */
    @RequestMapping(value = "/saveTenkennaiyoSearch", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveTenkennaiyoSearch(@RequestParam("id")String rirekiId,
                                  @RequestParam("tenkennaiyo")String tenkennaiyo,
                                  HttpSession session) throws IOException {

        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        //Lucene検索できない場合、データベースから取得
        List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"TR"+rirekiId+"End");
        TenkenRireki tenkenRireki=new TenkenRireki();
        if(searchResultObjectsTR.size()>0){
            TenkenRireki tmptenkenRireki=new TenkenRireki();
            tenkenRireki=tmptenkenRireki.toTenkenRireki(searchResultObjectsTR.get(0).getBody());
        }else{
            tenkenRireki = tenkenRirekiService.getTenkenRirekiById(rirekiId);
        }

        //DB更新
        tenkenRireki.setTenkennaiyo(tenkennaiyo);
        tenkenRirekiService.updateTenkenRireki(tenkenRireki);

        //session更新
        List<TenkenRirekiUtil> tenkenRirekiListSearch = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiListSearch");
        if(!CollectionUtils.isEmpty(tenkenRirekiListSearch)){
            for (int i = 0; i < tenkenRirekiListSearch.size(); i++) {
                if(tenkenRireki.getId() == tenkenRirekiListSearch.get(i).getId()){
                    tenkenRirekiListSearch.get(i).setTenkennaiyo(tenkennaiyo);
                    break;
                }
            }
        }

        //lucene更新
        luceneIndexService.updateRecord(indexTenkenRireki, "TR" + rirekiId + "End", tenkenRireki.toText());

        session.setAttribute("tenkenRirekiListSearch",tenkenRirekiListSearch);

        return "";

    }

    @RequestMapping(value = "/getNaiyoNumbers", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getNaiyoNumbers(HttpSession session){
        List<TenkenRirekiUtil> tenkenRirekiUtilList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiUtilList");
        int total = tenkenRirekiUtilList.size();
        int complete = 0;
        int incomplete = 0;
        for (int i = 0; i < tenkenRirekiUtilList.size(); i++) {
            if(tenkenRirekiUtilList.get(i).getTenkennaiyo().length() > 0){
                complete++;
            } else {
                incomplete++;
            }
        }

        Map<String, Integer> numbers = new HashMap<String, Integer>();
        numbers.put("total",total);
        numbers.put("complete",complete);
        numbers.put("incomplete",incomplete);
        Gson gson = new Gson();
        return gson.toJson(numbers);
    }

    /**
     * 弁で検索し、弁に所属する機器の点検リストを取得
     *
     * @param keyword 検索キーワード
     *
     * @return String
     * */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchKenanByKikisysId(@ModelAttribute("keyword")String keyword, ModelMap modelMap,HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        List<Valve> valves =new ArrayList<Valve>();
        List<TenkenRirekiUtil> tenkenRirekiListSearch = new ArrayList<TenkenRirekiUtil>();
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


        if (keyword==null){
            keyword=(String)session.getAttribute("TenkenKeyword");
        }

        //sessionのsearchtextも空の場合は、検索完了
        if(keyword!=null){
            if(user == null){
                return "login";
            } else {
                //弁リストを取得
                List<SearchResultObject> searchResultObjectsVA=luceneIndexService.selectRecord(indexValveFile,keyword);
                if(searchResultObjectsVA.size()>0){
                    for(SearchResultObject searchResultObject:searchResultObjectsVA){
                        Valve tmpValve=new Valve();
                        Valve valve=tmpValve.toValve(searchResultObject.getBody());
                        valves.add(valve);
                    }
                }
                Gson gson=new Gson();

                if(valves!=null){
                    for (int i = 0; i < valves.size(); i++) {
                   //点検履歴関係を取得
                   List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"VA"+valves.get(i).getKikiSysId()+"End");
                    if(searchResultObjectsKR.size()>0){
                        for(SearchResultObject searchResultObject:searchResultObjectsKR){
                            Koujirelation Tmpkoujirelation=new Koujirelation();
                            Koujirelation koujirelation=Tmpkoujirelation.toKoujirelation(searchResultObject.getBody());

                            //点検履歴取得
                            List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"KR"+koujirelation.getId()+"End");
                            if(searchResultObjectsTR.size()>0){
                                TenkenRirekiUtil tmpTenkenRirekiUtil=new TenkenRirekiUtil();
                                TenkenRirekiUtil tenkenRirekiUtil=tmpTenkenRirekiUtil.toTenkenRirekiUtil(searchResultObjectsTR.get(0).getBody());

                                    //kiki取得
                                    Kiki kiki=new Kiki();
                                    List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+koujirelation.getKikiid()+"End");
                                    if(searchResultObjectsKiki.size()>0){
                                        Kiki TmpKiki=new Kiki();
                                        kiki=TmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                                        tenkenRirekiUtil.setKiki(kiki);
                                    }

                                    //工事取得
                                    Kouji kouji=new Kouji();
                                    if(koujirelation.getKoujiid()!=0) {
                                        //kouji情報取得し、存在する場合、点検機器情報取得
                                        List<SearchResultObject> searchResultObjectsKJ = luceneIndexService.selectRecord(indexKoujiFile, "KJ" + koujirelation.getKoujiid() + "End");
                                        if (searchResultObjectsKJ.size() > 0) {
                                            Kouji tmpKouji = new Kouji();
                                            kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
                                            tenkenRirekiUtil.setKouji(kouji);
                                        }
                                    }
                                tenkenRirekiUtil.setValve(valves.get(i));
                                tenkenRirekiUtil.setKoujirelation(koujirelation);
                                tenkenRirekiUtil.setKoujirelationId(koujirelation.getId());
                                tenkenRirekiListSearch.add(tenkenRirekiUtil);
                                }
                            }
                        }
                    }
                }
            }
        }
        modelMap.addAttribute("tenkenRirekiListSearch",tenkenRirekiListSearch);
        modelMap.addAttribute("TenkenKeyword",keyword);
        session.setAttribute("TenkenKeyword",keyword);
        session.setAttribute("tenkenRirekiListSearch",tenkenRirekiListSearch);
        return "/list/tenken";
    }

    /**
     * 弁IDにより、機器の点検リストを取得
     *
     * @param kikiSysId 弁ID
     * @param kikiOrBenFlg 検索種類フラグ
     *
     * @return String
     * */
    @RequestMapping(value = "/tenken/{kikiSysId}/{kikiOrBenFlg}/valve", method = RequestMethod.GET)
    public String getTenkenRirekiByKikisysId(@PathVariable String kikiSysId,@PathVariable String kikiOrBenFlg, ModelMap modelMap,HttpSession session) throws IOException {
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
        Directory indexTenkenRireki=FSDirectory.open(new File(indexPath.get("indexTenkenRireki")));

        Valve valve=new Valve();
        List<TenkenRirekiUtil> tenkenRirekiListSearch = new ArrayList<TenkenRirekiUtil>();
        //valve検索
        List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiSysId+"End");
        if(!CollectionUtils.isEmpty(resultsValve)){
            Valve tmpValve=new Valve();
            valve=tmpValve.toValve(resultsValve.get(0).getBody());
        }

        //点検履歴関係を取得
        List<SearchResultObject> searchResultObjectsKR=luceneIndexService.selectRecord(indexKoujirelationFile,"VA"+valve.getKikiSysId()+"End");
        if(searchResultObjectsKR.size()>0){
            for(SearchResultObject searchResultObject:searchResultObjectsKR){
                Koujirelation Tmpkoujirelation=new Koujirelation();
                Koujirelation koujirelation=Tmpkoujirelation.toKoujirelation(searchResultObject.getBody());

                //点検履歴取得
                List<SearchResultObject> searchResultObjectsTR=luceneIndexService.selectRecord(indexTenkenRireki,"KR"+koujirelation.getId()+"End");
                if(searchResultObjectsTR.size()>0){
                    TenkenRirekiUtil tmpTenkenRirekiUtil=new TenkenRirekiUtil();
                    TenkenRirekiUtil tenkenRirekiUtil=tmpTenkenRirekiUtil.toTenkenRirekiUtil(searchResultObjectsTR.get(0).getBody());

                    //kiki取得
                    Kiki kiki=new Kiki();
                    List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+koujirelation.getKikiid()+"End");
                    if(searchResultObjectsKiki.size()>0){
                        Kiki TmpKiki=new Kiki();
                        kiki=TmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                    }
                    tenkenRirekiUtil.setKiki(kiki);

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
                    tenkenRirekiUtil.setKouji(kouji);

                    tenkenRirekiUtil.setValve(valve);
                    tenkenRirekiUtil.setKoujirelation(koujirelation);
                    tenkenRirekiUtil.setKoujirelationId(koujirelation.getId());
                    tenkenRirekiListSearch.add(tenkenRirekiUtil);
                }
            }
        }

        modelMap.addAttribute("tenkenRirekiListSearch",tenkenRirekiListSearch);
        modelMap.addAttribute("valve",valve);
        session.setAttribute("tenkenRirekiListSearch",tenkenRirekiListSearch);
        session.setAttribute("valve",valve);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);

        //1場合は、弁検索へ戻る、２場合は、機器検索へ戻る、３場合は、部品検索へ戻る
        if(kikiOrBenFlg.equals("1")){
            return "valve/tenken";
        }else if(kikiOrBenFlg.equals("2")){
            return "kikiSearch/kikitenken";
        }else if(kikiOrBenFlg.equals("3")){
            return "buhinSearch/buhintenken";
        }else{
            return "valveMultSearch/valveMulttenken";
        }
    }
}
