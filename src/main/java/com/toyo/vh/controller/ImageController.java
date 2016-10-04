package com.toyo.vh.controller;

import com.google.gson.Gson;
import com.toyo.vh.dao.ReportImageMapper;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Lsr on 12/5/14.
 * 画像
 */

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ReportImageService reportImageService;

    @Autowired
    ReportimageKikiSystemService reportimageKikiSystemService;

    @Autowired
    ItemService itemService;

    @Autowired
    LuceneIndexService luceneIndexService;

    @Autowired
    ImageService imageService;

    @Autowired
    IcsService icsService;

    @Autowired
    KoujiService koujiService;
    @Resource
    ReportImageMapper reportImageMapper;

    /**
     * 画像をreportImageを取る
     *
     * @param imagename 画像名
     *
     * @return String
     * */

    @RequestMapping(value = "/getImageByImagename", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getImageByImagename(@RequestParam("imagename")String imagename,
                                      HttpSession session){
        ReportImage reportImage = new ReportImage();
        List<ReportImage> allReportImageList = (List<ReportImage>)session.getAttribute("reportImageList");

        for (int i = 0; i < allReportImageList.size(); i++) {
            if(allReportImageList.get(i).getImagename().equals(imagename)){
                reportImage = allReportImageList.get(i);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(reportImage);
    }
    /**
     * 画像をreportImageに保存する
     *
     * @param object 画像名
     * @param koujiId 工事ID
     *
     * @return String
     * */
    @RequestMapping(value = "/updateImageByImagePath", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateImageByImagePath(@RequestParam("object")String object,
                                       @RequestParam("koujiId")String koujiId,
                                       HttpSession session,
                                       ModelMap modelMap){
        ReportImage reportImage = new ReportImage();
        reportImage.setKoujiId(Integer.valueOf(koujiId));
        reportImage.setImagename(object);
        reportImage.setImagesyu("");
        reportImage.setPapersize("A4");
        reportImage.setImagesyu("");

        List<ReportImage> allReportImageList = (List<ReportImage>) session.getAttribute("reportImageList");
        allReportImageList.add(reportImage);
        session.setAttribute("reportImageList",allReportImageList);
        return "";
    }

    /**
     * 画像reportImageをデータベースに保存する
     *
     * @param object 画像名
     * @param koujiId 工事ID
     *
     * @return String
     * */
    @RequestMapping(value = "/saveImageByImagePath", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveImageByImagePath(@RequestParam("object")String object,
                                       @RequestParam("koujiId")String koujiId,
                                       HttpSession session,
                                       ModelMap modelMap){
        Kouji kouji = (Kouji)session.getAttribute("kouji");
        if(kouji == null){
            kouji = koujiService.getKoujiById(koujiId);
        }
        ReportImage reportImage = new ReportImage();
        reportImage.setKoujiId(Integer.valueOf(koujiId));
        reportImage.setImagename(object);
        reportImage.setImagesyu("");
        reportImage.setPapersize("A4");
        reportImage.setImagesyu("");
        reportImage.setBikou(kouji.getKjMeisyo());
        reportImageService.addReportImage(reportImage);

        //session更新
        List<ReportImage> allReportImageList = reportImageService.getReportImageByKoujiId(koujiId);
        session.setAttribute("reportImageList",allReportImageList);
        return "";
    }

    /**
     * 画像種別を更新する
     *
     * @param object 画像名
     * @param imagesyu 画像種別
     *
     * @return String
     * */
    @RequestMapping(value = "/updateSyuByImagename", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String updateSyuByImagename(@RequestParam("imagesyu")String imagesyu,
                                       @RequestParam("object")String object,
                                       HttpSession session){

//        ReportImage reportImage = reportImageService.getReportImageByImagename(object);
//        reportImage.setImagesyu(imagesyu);
//        reportImageService.updateReportImage(reportImage);

        List<ReportImage> allReportImageList = (List<ReportImage>) session.getAttribute("reportImageList");
        for (int i = 0; i < allReportImageList.size(); i++) {
            if(allReportImageList.get(i).getImagename().equals(object)){
                allReportImageList.get(i).setImagesyu(imagesyu);
            }
        }
        session.setAttribute("reportImageList",allReportImageList);
        return "";
    }
    /**
     * 画像種別を更新する
     *
     * @param koujiId 工事ID
     *
     * @return String
     * */
    @RequestMapping(value = "/changeImagePage", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String changeImagePage(@RequestParam("koujiId")String koujiId,
                                       @RequestParam("imagename")String imagename,@RequestParam("pageNew")String pageNew,@RequestParam("pageOld")String pageOld,
                                       HttpSession session){

        ReportImage reportImage=new ReportImage();

        List<ReportImage> allReportImageList = (List<ReportImage>) session.getAttribute("reportImageList");
        //index数字からpageを取得
        int newPageNum=allReportImageList.get(Integer.valueOf(pageNew)-1).getPage();
        int oldPageNum=allReportImageList.get(Integer.valueOf(pageOld)-1).getPage();
        //imagename により、update page　pageNew and pageOld　間のimageは対象です。
        if(newPageNum>oldPageNum){
            //小さいページ⇒大きいページに修正
            for (int i = 0; i < allReportImageList.size(); i++) {
                Integer tmpPage=allReportImageList.get(i).getPage();
                if(tmpPage<=newPageNum && tmpPage>oldPageNum){
                    allReportImageList.get(i).setPage(tmpPage-1);
                    //対象imageの後のpage 修正
                    ReportImage reportImageTmp =reportImageService.getReportImageByImagename(allReportImageList.get(i).getImagename());
                    reportImageTmp.setPage(tmpPage-1);
                    reportImageService.updateReportImage(reportImageTmp);
                }
            }
        }else{
            //大きいページ⇒小さいページに修正
            for (int i = 0; i < allReportImageList.size(); i++) {
                Integer tmpPage=allReportImageList.get(i).getPage();
                if(tmpPage>=newPageNum && tmpPage<oldPageNum){
                    allReportImageList.get(i).setPage(tmpPage+1);
                    //対象imageの後のpage 修正
                    ReportImage reportImageTmp =reportImageService.getReportImageByImagename(allReportImageList.get(i).getImagename());
                    reportImageTmp.setPage(tmpPage+1);
                    reportImageService.updateReportImage(reportImageTmp);
                }
            }
        }

        //対象データ修正
        reportImage = reportImageService.getReportImageByImagename(imagename);
        reportImage.setPage(newPageNum);
        reportImageService.updateReportImage(reportImage);


        //imagename により、update page　
        allReportImageList = reportImageService.getReportImageByKoujiId(koujiId);
        session.setAttribute("reportImageList",allReportImageList);
//        //対象ImageのIndex値を取得
//        Integer targerImageIndex=0;
//        for (int i = 0; i < allReportImageList.size(); i++) {
//            if(imagename.equals(allReportImageList.get(i).getImagename())){
//                targerImageIndex=i+1;
//            }
//        }

        return "";
    }
    /**
     * 画像種別をデータベースに保存
     *
     * @return String
     * */

    @RequestMapping(value = "/saveSyuByImagename",method = RequestMethod.POST)
    @ResponseBody
    public String saveSyuReportImageList(@RequestParam("imagesyu")String imagesyu,
                                         @RequestParam("object")String object,
                                         HttpSession session){

        List<ReportImage> allReportImageList = (List<ReportImage>) session.getAttribute("reportImageList");
        for (int i = 0; i < allReportImageList.size(); i++) {
            if(allReportImageList.get(i).getImagename().equals(object)){
                ReportImage reportImage = allReportImageList.get(i);
                reportImage.setImagesyu(imagesyu);
                reportImageService.updateReportImage(reportImage);
            }
        }
        //同じ工事を確認する
        session.setAttribute("reportImageList",allReportImageList);
        return "";
    }
    /**
     * sessionの画像を削除する
     *
     * @param object 画像名
     *
     * @return String 削除された画像名
     * */
    @RequestMapping(value = "/deleteByImagename", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String deleteByImagename(@RequestParam("object")String object,
                                    HttpSession session){
        List<ReportImage> allReportImageList = (List<ReportImage>) session.getAttribute("reportImageList");
        for (int i = 0; i < allReportImageList.size(); i++) {
            if(allReportImageList.get(i).getImagename().equals(object)){
                allReportImageList.remove(i);
            }
        }

        return object;
    }

    /**
     * データベースの画像を削除する
     *
     * @param object 画像名
     *
     * @return String 削除された画像名
     * */
    @RequestMapping(value = "/deleteDatabaseByImagename", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String deleteDatabaseByImagename(@RequestParam("object")String object,@RequestParam("koujiId")String koujiId,HttpSession session){
        //reportImageから削除
        ReportImage reportImage = reportImageService.getReportImageByImagename(object);
        reportImageService.deleteReportImage(reportImage);
        //reportimagekikisystemから削除
        reportimageKikiSystemService.deleteByKoujiIdAndImagename(reportImage.getKoujiId()+"",object);

        //図面のsessionを更新
        List<ReportImage> allReportImageList = reportImageService.getReportImageByKoujiId(koujiId);
        session.setAttribute("reportImageList",allReportImageList);
        return "";
    }

    /**
     * sessionの機器システムを更新
     *
     * @param koujiId 工事名
     * @param imagename  画像名
     * @param selectedIdList 機器システムlist
     *
     * @return String 削除された画像名
     * */
    @RequestMapping(value = "/updateImageKikisystem", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String updateImageKikisystem(@RequestParam("koujiId")String koujiId,
                                        @RequestParam("imagename")String imagename,
                                        @RequestParam("selectedIdList")String selectedIdList,
                                        HttpSession session){
        List<ReportimageKikiSystem> allRKList = (List<ReportimageKikiSystem>)session.getAttribute("reportimageKikiSystemList");

        List<ReportimageKikiSystem> newRKList = new ArrayList<ReportimageKikiSystem>();
        //delete old data
        for (int i = 0; i < allRKList.size(); i++) {
            if(allRKList.get(i).getImagename().equals(imagename)){
            } else {
                newRKList.add(allRKList.get(i));
            }
        }
        reportimageKikiSystemService.deleteByKoujiIdAndImagename(koujiId,imagename);

        if(selectedIdList.length() > 0) {
            String idArray[] = selectedIdList.split(",");
            List<String> idList = new ArrayList<String>();
            for (int i = 0; i < idArray.length; i++) {
                idList.add(idArray[i]);
                ReportimageKikiSystem reportimageKikiSystem = new ReportimageKikiSystem();
                reportimageKikiSystem.setKikiSysId(Integer.valueOf(idArray[i]));
                reportimageKikiSystem.setKoujiId(Integer.valueOf(koujiId));
                reportimageKikiSystem.setImagename(imagename);
                newRKList.add(reportimageKikiSystem);
                reportimageKikiSystemService.addReportimageKikisystemByReportimageKikiSystem(reportimageKikiSystem);
            }
            session.setAttribute("reportimageKikiSystemList",newRKList);
        }
        return "ok";
    }

    /**
     * databaseの機器システムを更新
     *
     * @param koujiId 工事名
     * @param imagename  画像名
     * @param selectedIdList 機器システムlist
     *
     * @return String 削除された画像名
     * */
    @RequestMapping(value = "/saveImageKikisystem", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String saveImageKikisystem(@RequestParam("koujiId")String koujiId,
                                        @RequestParam("imagename")String imagename,
                                        @RequestParam("selectedIdList")String selectedIdList){
        reportimageKikiSystemService.deleteByKoujiIdAndImagename(koujiId,imagename);
        if(selectedIdList.length() > 0) {
            String idArray[] = selectedIdList.split(",");
            List<String> idList = new ArrayList<String>();
            for (int i = 0; i < idArray.length; i++) {
                idList.add(idArray[i]);
                ReportimageKikiSystem reportimageKikiSystem = new ReportimageKikiSystem();
                reportimageKikiSystem.setKikiSysId(Integer.valueOf(idArray[i]));
                reportimageKikiSystem.setKoujiId(Integer.valueOf(koujiId));
                reportimageKikiSystem.setImagename(imagename);
            }

            reportimageKikiSystemService.addReportimageKikisystem(koujiId,imagename,idList);
        }
        return "";
    }

    /**
     * sessionからimagenameより、valveデータを取る
     *
     * @param koujiId 工事ID
     * @param imagename 画像名
     *
     * @return String valveList
     * */
    @RequestMapping(value = "/getReportimageKikisystemByKoujiAndImagename", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String getReportimageKikisystemByKoujiAndImagename(@RequestParam("koujiId")String koujiId,
                                                              @RequestParam("imagename")String imagename,
                                                              HttpSession session){

//        List<ReportimageKikiSystem> reportimageKikiSystemList = reportimageKikiSystemService.getListByReportimageKikiSystem(koujiId,imagename);
        List<ReportimageKikiSystem> allRKList = reportimageKikiSystemService.getListByReportimageKikiSystem(koujiId,imagename);
        List<Valve> allValveList = (List<Valve>)session.getAttribute("valveList");
        List<Valve> valveList = new ArrayList<Valve>();

        //select reportimageKikisystem record
//        for (int i = 0; i < allRKList.size(); i++) {
//            if(allRKList.get(i).getImagename().equals(imagename)){
//                reportimageKikiSystemList.add(allRKList.get(i));
//            }
//        }

        //select valve info
        for (int i = 0; i < allRKList.size(); i++) {
//            Valve valve = itemService.getKikisysByKikisysId(reportimageKikiSystemList.get(i).getKikiSysId() + "");
            for (int j = 0; j < allValveList.size(); j++) {
                if(allValveList.get(j).getKikiSysId() == allRKList.get(i).getKikiSysId()) {
                    valveList.add(allValveList.get(j));
                }
            }
        }
        Gson gson = new Gson();

        // 弁番号で 昇順ソート
        Collections.sort(valveList,
                new Comparator<Valve>() {
                    @Override
                    public int compare(Valve entry1,
                                       Valve entry2) {
                        return (entry1.getvNo())
                                .compareTo(entry2.getvNo());
                    }
                });

        return gson.toJson(valveList);
    }

    /**
     * sessionからセットナンバーより、画像リストを取る
     *
     * @param setNum セットナンバー
     *
     * @return String 画像List
     * */
    @RequestMapping(value = "/getReportimageBySet", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String getReportimageByPage(@RequestParam("setNum")String setNum,
                                       HttpSession session){
        List<ReportImage> allReportImageList = (List<ReportImage>) session.getAttribute("reportImageList");
//        List<ReportImage> reportImageList = new ArrayList<ReportImage>();
//        int num = 0;
//        for (int i = Integer.valueOf(setNum) * 6; i < allReportImageList.size(); i++, num++) {
//            if(num == 6){
//                break;
//            }
//            reportImageList.add(allReportImageList.get(i));
//        }

        Gson gson = new Gson();
        return gson.toJson(allReportImageList.get(Integer.valueOf(setNum)));

    }

    /**
     * 弁検索により、ページ遷移時の画像リストを取る
     *
     * @param setNum セットナンバー
     * @param type 1:ICS,2:GP,3:valve,4:kouji
     *
     * @return String 画像List
     * */
    @RequestMapping(value = "/getimageByValveID", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String getimageByValveID(@RequestParam("setNum")String setNum,@RequestParam("type")String type,
                                       HttpSession session){
        List<Image> GPImageList = (List<Image>) session.getAttribute("GPImageList");//GP
        List<Image> IcsImageList = (List<Image>) session.getAttribute("IcsImageList");//ICS
        List<Image> imageListKikisys = (List<Image>) session.getAttribute("imageListKikisys");//valve
        List<ReportImage> reportImageList = (List<ReportImage>) session.getAttribute("reportImageList");//kouji

        Gson gson = new Gson();
        if(type.equals("1")){
            return gson.toJson(IcsImageList.get(Integer.valueOf(setNum)));
        }else if(type.equals("2")){
            return gson.toJson(GPImageList.get(Integer.valueOf(setNum)));
        }else if(type.equals("3")){
            return gson.toJson(imageListKikisys.get(Integer.valueOf(setNum)));
        }else{
            return gson.toJson(reportImageList.get(Integer.valueOf(setNum)));
        }
    }

    @RequestMapping(value = "/saveSessionToDatabase", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String saveSessionToDatabase(){
        return "over";
    }

    /**
     * sessionからimagenameより、枚数と何枚目を取る
     *
     * @param imagename 画像名
     *
     * @return int currentPage
     * @return int totalPage
     * */
    @RequestMapping(value = "/updatePageNumber", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String getTotalpageNumber(@RequestParam("imagename")String imagename,
                                     HttpSession session){

        List<ReportImage> reportImageList = (List<ReportImage>)session.getAttribute("reportImageList");

        Map<String,Integer> pageMapper = new HashMap<String, Integer>();
        pageMapper.put("totalPage",reportImageList.size());
        if(imagename.length() > 0) {
            for (int i = 0; i < reportImageList.size(); i++) {
                if (reportImageList.get(i).getImagename().equals(imagename)) {
                    pageMapper.put("currentPage",i+1);
                    break;
                }
            }
        } else {
            pageMapper.put("currentPage",0);
        }
        Gson gson = new Gson();
        return gson.toJson(pageMapper);
    }

    /**
     * 弁IDにより、画像を取得
     * @param kikiSysId 弁ID
     * @param kikiOrBenFlg 検索種類フラグ
     *
     * @return String 画像画面パス
     *
     * */
    @RequestMapping(value = "/image/{kikiSysId}/{kikiOrBenFlg}/valve", method = RequestMethod.GET)
    public String image(@PathVariable String kikiSysId,@PathVariable String kikiOrBenFlg,
                        ModelMap modelMap,
                        HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        //lucene初期化
        Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");
        if(indexPath==null) {
            indexPath = luceneIndexService.generateLocalIndex();
        }
        session.setAttribute("indexPath",indexPath);

        Directory indexValveFile= FSDirectory.open(new File(indexPath.get("indexValveFile")));
        Directory indexKikiFile=FSDirectory.open(new File(indexPath.get("indexKikiFile")));
        Directory indexBuhinFile=FSDirectory.open(new File(indexPath.get("indexBuhinFile")));
        Directory indexKoujiFile=FSDirectory.open(new File(indexPath.get("indexKoujiFile")));
        Directory indexKoujirelationFile=FSDirectory.open(new File(indexPath.get("indexKoujirelationFile")));
        Directory indexKenan=FSDirectory.open(new File(indexPath.get("indexKenan")));

        Valve valve=new Valve();
        //valve検索
        List<SearchResultObject> resultsValve = luceneIndexService.selectRecord(indexValveFile, "VA"+kikiSysId+"End");
        if(!CollectionUtils.isEmpty(resultsValve)){
            Valve tmpValve=new Valve();
            valve=tmpValve.toValve(resultsValve.get(0).getBody());
        }


        //kouji関連の図面
        List<ReportImage> allReportImageList = reportImageService.getReportImageByKikisysId(kikiSysId);
        //kouji名を追加
        for (int i = 0; i < allReportImageList.size(); i++) {
            //工事取得
            Kouji kouji=new Kouji();
            Integer koujiId=allReportImageList.get(i).getKoujiId();
            if(koujiId>0){
                //kouji情報取得し、存在する場合、点検機器情報取得
                List<SearchResultObject> searchResultObjectsKJ = luceneIndexService.selectRecord(indexKoujiFile, "KJ" +koujiId + "End");
                if (searchResultObjectsKJ.size() > 0) {
                    Kouji tmpKouji = new Kouji();
                    kouji = tmpKouji.toKouji(searchResultObjectsKJ.get(0).getBody());
                }
            }
            allReportImageList.get(i).setPapersize(kouji.getKjMeisyo());
        }

        //Valveの基本情報の画像
        List<Image> allKikisysImageList = imageService.getImagesByKikisysId(kikiSysId);
        //ICS画像を取得
        List<Image> IcsImageList =new ArrayList<Image>();
        if(valve.getIcs()!=""){
            Ics ics=icsService.getIcsByIcsNum(valve.getIcs().trim());
            if(ics!=null&& ics.getImagepath().length()>0){
                //画面上で同じ形式で表示するために、imageに変更
                Image icsToImage=new Image();
                icsToImage.setBikou(ics.getBikou());
                icsToImage.setImagename(ics.getImagepath());
                icsToImage.setImagesyu("ICS");
                IcsImageList.add(icsToImage);
            }

        }

        //GP画像を取得
        List<Image> GPImageList =new ArrayList<Image>();
        for(Image tmpImage:allKikisysImageList){
            if(tmpImage.getImagesyu().contains("GP")){
                GPImageList.add(tmpImage);
            }
        }

        //first image get
        Image GPFirstImage=new Image();
        Image IcsFirstImage=new Image();
        Image ValveFirstImage=new Image();
        ReportImage KoujiFirstImage=new ReportImage();
        String GPFirstNum="0";
        String IcsFirstNum="0";
        String ValveFirstNum="0";
        String KoujiFirstNum="0";

        if(GPImageList.size()>0){
            GPFirstImage=GPImageList.get(0);
            GPFirstNum="1";
        }
        if(IcsImageList.size()>0){
//            IcsFirstImage=IcsImageList.get(0);
            IcsFirstImage=allKikisysImageList.get(0);
            IcsFirstNum="1";
        }
        if(allKikisysImageList.size()>0){
            ValveFirstImage=allKikisysImageList.get(0);
            ValveFirstNum="1";
        }
        if(allReportImageList.size()>0){
            KoujiFirstImage=allReportImageList.get(0);
            KoujiFirstNum="1";
        }

        //種別により、image取得  valve
        ArrayList<Image> allKikisysImageList01 =new ArrayList<Image>();
        ArrayList<Image> allKikisysImageList02 =new ArrayList<Image>();
        ArrayList<Image> allKikisysImageList03 =new ArrayList<Image>();
        ArrayList<Image> allKikisysImageList04 =new ArrayList<Image>();
        ArrayList<Image> allKikisysImageList05 =new ArrayList<Image>();
        ArrayList<Image> allKikisysImageList06 =new ArrayList<Image>();

        allKikisysImageList01=getImageListForType(allKikisysImageList,Config.ImageTypeValve01);
        allKikisysImageList02=getImageListForType(allKikisysImageList,Config.ImageTypeValve02);
        allKikisysImageList03=getImageListForType(allKikisysImageList,Config.ImageTypeValve03);
        allKikisysImageList04=getImageListForType(allKikisysImageList,Config.ImageTypeValve04);
        allKikisysImageList05=getImageListForType(allKikisysImageList,Config.ImageTypeValve05);
        allKikisysImageList06=getImageListForType(allKikisysImageList,Config.ImageTypeValve06);

        //type 図面　list
        session.setAttribute("allKikisysImageList01",allKikisysImageList01);
        session.setAttribute("allKikisysImageList02",allKikisysImageList02);
        session.setAttribute("allKikisysImageList03",allKikisysImageList03);
        session.setAttribute("allKikisysImageList04",allKikisysImageList04);
        session.setAttribute("allKikisysImageList05",allKikisysImageList05);
        session.setAttribute("allKikisysImageList06",allKikisysImageList06);
        session.setAttribute("allKikisysImageList",allKikisysImageList);

        //種別により、image取得  kouji
        ArrayList<ReportImage> allKikisysImageListKouji01 =new ArrayList<ReportImage>();
        ArrayList<ReportImage> allKikisysImageListKouji02 =new ArrayList<ReportImage>();
        ArrayList<ReportImage> allKikisysImageListKouji03 =new ArrayList<ReportImage>();
        ArrayList<ReportImage> allKikisysImageListKouji04 =new ArrayList<ReportImage>();
        ArrayList<ReportImage> allKikisysImageListKouji05 =new ArrayList<ReportImage>();
        ArrayList<ReportImage> allKikisysImageListKouji06 =new ArrayList<ReportImage>();

        allKikisysImageListKouji01=getReportImageListForType(allReportImageList,Config.ImageTypeValve01);
        allKikisysImageListKouji02=getReportImageListForType(allReportImageList,Config.ImageTypeValve02);
        allKikisysImageListKouji03=getReportImageListForType(allReportImageList,Config.ImageTypeValve03);
        allKikisysImageListKouji04=getReportImageListForType(allReportImageList,Config.ImageTypeValve04);
        allKikisysImageListKouji05=getReportImageListForType(allReportImageList,Config.ImageTypeValve05);
        allKikisysImageListKouji06=getReportImageListForType(allReportImageList,Config.ImageTypeValve06);

        //type 図面　list  kouji
        session.setAttribute("allKikisysImageListKouji01",allKikisysImageListKouji01);
        session.setAttribute("allKikisysImageListKouji02",allKikisysImageListKouji02);
        session.setAttribute("allKikisysImageListKouji03",allKikisysImageListKouji03);
        session.setAttribute("allKikisysImageListKouji04",allKikisysImageListKouji04);
        session.setAttribute("allKikisysImageListKouji05",allKikisysImageListKouji05);
        session.setAttribute("allKikisysImageListKouji06",allKikisysImageListKouji06);
        session.setAttribute("allKikisysImageListKouji",allReportImageList);

        //各種類のFirst図面
        session.setAttribute("GPFirstImage",GPFirstImage);
        session.setAttribute("IcsFirstImage",IcsFirstImage);
        session.setAttribute("ValveFirstImage",ValveFirstImage);
        session.setAttribute("KoujiFirstImage",KoujiFirstImage);
        //各種類のFirst Index num
        session.setAttribute("GPFirstNum",GPFirstNum);
        session.setAttribute("IcsFirstNum",IcsFirstNum);
        session.setAttribute("ValveFirstNum",ValveFirstNum);
        session.setAttribute("KoujiFirstNum",KoujiFirstNum);
        //各種類のimage size
        session.setAttribute("GPImageListSize",GPImageList.size());
        session.setAttribute("IcsImageListSize",IcsImageList.size());
        modelMap.addAttribute("imageListKikisysSize",allKikisysImageList.size());
        modelMap.addAttribute("imageListSize",allReportImageList.size());
        //各種類のimage
        session.setAttribute("IcsImageList",IcsImageList);
        session.setAttribute("GPImageList",GPImageList);
        session.setAttribute("imageListKikisys",allKikisysImageList);
        session.setAttribute("reportImageList",allReportImageList);


        session.setAttribute("valve",valve);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);

        modelMap.addAttribute("imageList",allReportImageList);//kouji側の画像
        modelMap.addAttribute("imageListKikisys",allKikisysImageList);//弁側の画像
        modelMap.addAttribute("IcsImageList",IcsImageList);
        modelMap.addAttribute("GPImageList",GPImageList);
        modelMap.addAttribute("valve",valve);



        //1場合は、弁検索へ戻る、２場合は、機器検索へ戻る、３場合は、部品検索へ戻る
        if(kikiOrBenFlg.equals("1")){
            return "valve/image";
        }else if(kikiOrBenFlg.equals("2")){
            return "kikiSearch/kikiimage";
        }else if(kikiOrBenFlg.equals("3")){
            return "buhinSearch/buhinimage";
        }else{
            return "valveMultSearch/valveMultimage";
        }
    }

    /**
     * 画像種別データをsession に設定
     *@param type  図面種別番号
     * @return String
     * */

    @RequestMapping(value = "/setImagesyuSession", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String setImagesyuSession(@RequestParam("type")String type,
                                         HttpSession session){
        List<Image> imageListKikisys =new ArrayList<Image>();
        if("1".equals(type)||type.equals(Config.ImageTypeValve01)){
            imageListKikisys = (List<Image>) session.getAttribute("allKikisysImageList01");
        }else if("2".equals(type)||type.equals(Config.ImageTypeValve02)){
            imageListKikisys = (List<Image>) session.getAttribute("allKikisysImageList02");
        }else if("3".equals(type)||type.equals(Config.ImageTypeValve03)){
            imageListKikisys = (List<Image>) session.getAttribute("allKikisysImageList03");
        }else if("4".equals(type)||type.equals(Config.ImageTypeValve04)){
            imageListKikisys = (List<Image>) session.getAttribute("allKikisysImageList04");
        }else if("5".equals(type)||type.equals(Config.ImageTypeValve05)){
            imageListKikisys = (List<Image>) session.getAttribute("allKikisysImageList05");
        }else if("6".equals(type)||type.equals(Config.ImageTypeValve06)){
            imageListKikisys = (List<Image>) session.getAttribute("allKikisysImageList06");
        }else {//全部画像
            imageListKikisys = (List<Image>) session.getAttribute("allKikisysImageList");
        }

        session.setAttribute("imageListKikisys",imageListKikisys);

        Gson gson = new Gson();
        return gson.toJson(imageListKikisys.get(0));
    }

    /**
     * 画像種別データをsession に設定 Kouji
     *@param type  図面種別番号
     * @return String
     * */

    @RequestMapping(value = "/setReportImagesyuSessionForKouji", method = RequestMethod.POST, produces = "html/text;charset=UTF-8")
    @ResponseBody
    public String setReportImagesyuSessionForKouji(@RequestParam("type")String type,
                                     HttpSession session){
        List<ReportImage> imageListKikisys =new ArrayList<ReportImage>();
        if("1".equals(type)||type.equals(Config.ImageTypeValve01)){
            imageListKikisys = (List<ReportImage>) session.getAttribute("allKikisysImageListKouji01");
        }else if("2".equals(type)||type.equals(Config.ImageTypeValve02)){
            imageListKikisys = (List<ReportImage>) session.getAttribute("allKikisysImageListKouji02");
        }else if("3".equals(type)||type.equals(Config.ImageTypeValve03)){
            imageListKikisys = (List<ReportImage>) session.getAttribute("allKikisysImageListKouji03");
        }else if("4".equals(type)||type.equals(Config.ImageTypeValve04)){
            imageListKikisys = (List<ReportImage>) session.getAttribute("allKikisysImageListKouji04");
        }else if("5".equals(type)||type.equals(Config.ImageTypeValve05)){
            imageListKikisys = (List<ReportImage>) session.getAttribute("allKikisysImageListKouji05");
        }else if("6".equals(type)||type.equals(Config.ImageTypeValve06)){
            imageListKikisys = (List<ReportImage>) session.getAttribute("allKikisysImageListKouji06");
        }else {//全部画像
            imageListKikisys = (List<ReportImage>) session.getAttribute("allKikisysImageListKouji");
        }

        //同じ工事を確認する
        session.setAttribute("reportImageList",imageListKikisys);

        Gson gson = new Gson();
        return gson.toJson(imageListKikisys.get(0));
    }

    /**
     * 画像種別により、図面リストを取得
     * @param type  図面種別番号
     * @return List
     * */
    public ArrayList<Image>  getImageListForType(List<Image> src,String type){
        ArrayList<Image> tmp =new ArrayList<Image>();
        for(int i=0;i<src.size();i++){
            if(src.get(i).getImagesyu().equals(type)){
                tmp.add(src.get(i));
            }
        }
        return  tmp;
    }

    /**
     * 画像種別により、図面リストを取得  Kouji
     * @param type  図面種別番号
     * @return List
     * */
    public ArrayList<ReportImage>  getReportImageListForType(List<ReportImage> src,String type){
        ArrayList<ReportImage> tmp =new ArrayList<ReportImage>();
        for(int i=0;i<src.size();i++){
            if(src.get(i).getImagesyu().equals(type)){
                tmp.add(src.get(i));
            }
        }
        return  tmp;
    }
}
