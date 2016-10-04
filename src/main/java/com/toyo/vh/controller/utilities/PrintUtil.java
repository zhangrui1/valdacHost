package com.toyo.vh.controller.utilities;

import com.toyo.vh.controller.Config;
import com.toyo.vh.dto.SearchResultObject;
import com.toyo.vh.entity.*;
import com.toyo.vh.service.LuceneIndexService;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangrui on 2015/04/11.
 */
public class PrintUtil {

    /**
     * 弁IDから弁、機器、部品を取得
     * @param kikiSysId 弁ID
     * */
    public static ValveForDL getKikiByKikisysId(String kikiSysId,Map<String,String> indexPath,LuceneIndexService luceneIndexService) throws IOException {
        ValveForDL valveForDL=new ValveForDL();
        List<Kiki> kikiList=new ArrayList<Kiki>();
        List<Kiki> kikis=new ArrayList<Kiki>();//中間処理用
        Map<Kiki,List<Buhin>> kikibuhinMaps=new HashMap<Kiki, List<Buhin>>();

        //Lucenne directory
        Directory indexValveFile= FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));

        //valve検索
        List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiSysId+"End");
        if(resultsValve!=null) {
            Valve tmpValve = new Valve();
            Valve valve = tmpValve.toValve(resultsValve.get(0).getBody());
            valveForDL.setValve(valve);

            //Kiki検索----------------------------------------------------------------------
            List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"VA"+kikiSysId+"End");
            for(SearchResultObject tmpSearchResultObject :searchResultObjectsKiki){
                Kiki tmpKiki=new Kiki();
                Kiki kiki=tmpKiki.toKiki(tmpSearchResultObject.getBody());
                kikis.add(kiki);
            }

            //kikiListをソートする
            List<Kiki> kikilistA =new ArrayList<Kiki>();
            List<Kiki> kikilistB =new ArrayList<Kiki>();
            List<Kiki> kikilistC =new ArrayList<Kiki>();
            List<Kiki> kikilistD =new ArrayList<Kiki>();
            List<Kiki> kikilistE =new ArrayList<Kiki>();

            //Kikiソートする
            for(int i=0;i<kikis.size();i++){
                String kikibunrui=kikis.get(i).getKikiBunrui();
                if(Config.KikiBunRuiA.equals(kikibunrui)){
                    kikilistA.add(kikis.get(i));
                }else if(Config.KikiBunRuiB.equals(kikibunrui)){
                    kikilistB.add(kikis.get(i));
                }else if(Config.KikiBunRuiC.equals(kikibunrui)){
                    kikilistC.add(kikis.get(i));
                }else if(Config.KikiBunRuiD.equals(kikibunrui)){
                    kikilistD.add(kikis.get(i));
                }else{ //４種類以外の場合
                    kikilistE.add(kikis.get(i));
                }
            }
            //Listを結合する
            kikiList.addAll(kikilistA);
            kikiList.addAll(kikilistB);
            kikiList.addAll(kikilistC);
            kikiList.addAll(kikilistD);
            kikiList.addAll(kikilistE);


            //Buhin----------------------------------------------------------------------
            for(Kiki kiki:kikiList){
                List<Buhin> buhinList=new ArrayList<Buhin>();
                //Buhin検索
                List<SearchResultObject> resultsBuhin = luceneIndexService.selectRecord(indexBuhinFile, "KI"+kiki.getKikiId()+"End");
                if(resultsBuhin!=null){
                    for(SearchResultObject tmpSearchResultObject :resultsBuhin){
                        Buhin tmpBuhin=new Buhin();
                        Buhin buhin=tmpBuhin.toBuhin(tmpSearchResultObject.getBody());
                        buhinList.add(buhin);
                    }
                }
                kikibuhinMaps.put(kiki,buhinList);
            }

        }
        valveForDL.setKikiList(kikiList);
        valveForDL.setBuhinList(kikibuhinMaps);

        return valveForDL;
    }


    /**
     * 弁IDから懸案を取得
     * @param kikiSysId 弁ID
     * */
    public static ValveKenanDL getKenanByKikisysId(String kikiSysId,Map<String,String> indexPath,LuceneIndexService luceneIndexService) throws IOException {
        ValveKenanDL valveKenanDL=new ValveKenanDL();
        List<Kiki> kikiList=new ArrayList<Kiki>();
        //kikiのユニークを取得ため
        Map<String,Kiki> kikiMap=new HashMap<String, Kiki>();
        //kiki IDごとにkenanを取得
        Map<String,List<Kenan>> kenanList=new HashMap<String, List<Kenan>>();
        //kouji
        Map<String,Kouji> koujiMap=new HashMap<String, Kouji>();
        //kenan
        Map<String,Kenan> kenanMap=new HashMap<String, Kenan>();

        //Lucenne directory
        Directory indexValveFile= FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        //valve検索
        List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiSysId+"End");
        if(resultsValve!=null) {
            Valve tmpValve = new Valve();
            Valve valve = tmpValve.toValve(resultsValve.get(0).getBody());
            valveKenanDL.setValve(valve);
        }
        //Kenanを取得
        List<SearchResultObject> searchResultObjectsKenans=luceneIndexService.selectRecord(indexKenan,"VA"+kikiSysId+"End");
        if(searchResultObjectsKenans.size()>0) {
            for (SearchResultObject searchResultObject1 : searchResultObjectsKenans) {
                //kenan
                Kenan tmpKenan = new Kenan();
                Kenan kenan = tmpKenan.toKenan(searchResultObject1.getBody());
                kenanMap.put(kenan.getId()+"",kenan);
                //懸案から機器情報を取得する
                Kiki kiki=new Kiki();
                List<SearchResultObject> searchResultObjectsKiki=luceneIndexService.selectRecord(indexKikiFile,"KI"+kenan.getKikiId()+"End");
                if(searchResultObjectsKiki.size()>0){
                    //kiki
                    Kiki tmpKiki=new Kiki();
                    kiki=tmpKiki.toKiki(searchResultObjectsKiki.get(0).getBody());
                }

                //すでに追加したkenanを取得し、今回の懸案を追加して、もう一度保存する
                List<Kenan> tmpKenanList=new ArrayList<Kenan>();
                if(kenanList.containsKey(kiki.getKikiId()+"")){
                    tmpKenanList=kenanList.get(kiki.getKikiId()+"");
                }
                tmpKenanList.add(kenan);
                kenanList.put(kiki.getKikiId()+"",tmpKenanList);

                kikiMap.put(kenan.getKikiId()+"",kiki);

                //get kouji
                Kouji kouji = new Kouji();
                List<SearchResultObject> searchResultObjectsKouji=luceneIndexService.selectRecord(indexKoujiFile,"KJ"+kenan.getKoujiId()+"End");
                if(searchResultObjectsKouji.size()>0) {
                    Kouji tmpKouji = new Kouji();
                    kouji = tmpKouji.toKouji(searchResultObjectsKouji.get(0).getBody());
                }
                koujiMap.put(kouji.getId()+"",kouji);

            }
        }

        //Kikiリストを追加
        kikiList=transKikiMapToList(kikiMap);
        valveKenanDL.setKikiList(kikiList);
        //kikiとKenanリスト追加
        valveKenanDL.setKenanList(kenanList);
        valveKenanDL.setKoujiMap(koujiMap);

        return valveKenanDL;
    }

    public static List<Kiki> transKikiMapToList(Map<String,Kiki> kikiMap){

        List<Kiki> kikiList=new ArrayList<Kiki>();
        //kikiListをソートする
        List<Kiki> kikilistA =new ArrayList<Kiki>();
        List<Kiki> kikilistB =new ArrayList<Kiki>();
        List<Kiki> kikilistC =new ArrayList<Kiki>();
        List<Kiki> kikilistD =new ArrayList<Kiki>();
        List<Kiki> kikilistE =new ArrayList<Kiki>();

        //Kiki MapからListに変換する
        for(String key:kikiMap.keySet()){
            String kikibunrui=kikiMap.get(key).getKikiBunrui();
            if(Config.KikiBunRuiA.equals(kikibunrui)){
                kikilistA.add(kikiMap.get(key));
            }else if(Config.KikiBunRuiB.equals(kikibunrui)){
                kikilistB.add(kikiMap.get(key));
            }else if(Config.KikiBunRuiC.equals(kikibunrui)){
                kikilistC.add(kikiMap.get(key));
            }else if(Config.KikiBunRuiD.equals(kikibunrui)){
                kikilistD.add(kikiMap.get(key));
            }else{ //４種類以外の場合
                kikilistE.add(kikiMap.get(key));
            }
        }
        //Listを結合する
        kikiList.addAll(kikilistA);
        kikiList.addAll(kikilistB);
        kikiList.addAll(kikilistC);
        kikiList.addAll(kikilistD);
        kikiList.addAll(kikilistE);

        return  kikiList;
    }

}
