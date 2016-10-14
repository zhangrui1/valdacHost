package com.toyo.vh.controller;

import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.dao.ItemMapper;
import com.toyo.vh.entity.*;
import com.toyo.vh.service.LocationService;
import com.toyo.vh.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lsr on 12/4/14.
 */
@Controller
@RequestMapping("/list")
public class ListController {
    @Resource
    ItemMapper itemMapper;
    @Autowired
    LocationService locationService;
    @Autowired
    MasterService masterService;
    //工事検索結果画面へ遷移
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap,HttpSession session){
        User user = (User) session.getAttribute("user");
        List<Kouji> searchKoujiList = (List<Kouji>) session.getAttribute("searchKoujiList");
        List<TenkenRirekiUtil> tenkenRirekiList = (List<TenkenRirekiUtil>) session.getAttribute("tenkenRirekiHistory");
        modelMap.addAttribute("searchKoujiList",searchKoujiList);
        modelMap.addAttribute("tenkenRirekiHistory",tenkenRirekiList);
        modelMap.addAttribute("user",user);

        //get location
        List<Location> locationList =new ArrayList<Location>();
        if("admin".equals(user.getDepartment())){
            locationList = locationService.getAllLocation();
        }else{
            locationList = locationService.getLocationByUserDepartment(user.getDepartment());
        }

        List<String> nameList = new LinkedList<String>();
        for (int i = 0; i < locationList.size(); i++) {
            String tmpLocation= StringUtil.concatWithDelimit(" ",locationList.get(i).getkCodeL(),locationList.get(i).getkCodeM(),locationList.get(i).getkCodeS());
            if(!nameList.contains(locationList.get(i).getkCodeL())){
                nameList.add(locationList.get(i).getkCodeL());
            }
            if(!nameList.contains(tmpLocation)){
               nameList.add(tmpLocation);
            }
        }
        modelMap.addAttribute("nameList",nameList);
        session.setAttribute("locationList",locationList);
        modelMap.addAttribute("locationList", locationList);
        return "list/kouji";
    }


    //点検検索画面へ遷移
    @RequestMapping(value = "/tenken", method = RequestMethod.GET)
    public String tenken(){
        return "list/tenken";
    }

    //弁検索画面へ遷移
    @RequestMapping(value = "/valve", method = RequestMethod.GET)
    public String valve(ModelMap modelMap,HttpSession session){
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
            String tmpLocation= StringUtil.concatWithDelimit(" ",locationList.get(i).getkCodeL(),locationList.get(i).getkCodeM(),locationList.get(i).getkCodeS());
            if(!nameList.contains(locationList.get(i).getkCodeL())){
                nameList.add(locationList.get(i).getkCodeL());
            }
            if(!nameList.contains(tmpLocation)){
                nameList.add(tmpLocation);
            }
        }

        //会社名をsessionから取得
        String location=(String)session.getAttribute("locationNameSelectedForValve");
        if(StringUtil.isEmpty(location)){
            location="全部会社名";
        }
        //valve取得
        List<Valve> valveResults=(List<Valve>)session.getAttribute("valveResultsForKikisys");
        if(CollectionUtils.isEmpty(valveResults)){
            valveResults=new ArrayList<Valve>();
        }else{
            if(!valveResults.get(0).getLocationName().contains(location)){
                location="全部会社名";
                valveResults=new ArrayList<Valve>();
            }
        }
        //keyword取得
        String KikisysSearchKeyword=(String)session.getAttribute("KikisysSearchKeyword");
        if(KikisysSearchKeyword==null){
            KikisysSearchKeyword="";
        }
        //弁検索場合は１、機器検索場合は２、部品検索場合は３
        String kikiOrBenFlg="1";
        session.setAttribute("locationNameSelectedForValve",location);
        session.setAttribute("valveResultsForKikisys",valveResults);
        session.setAttribute("KikisysSearchKeyword",KikisysSearchKeyword);
        session.setAttribute("nameList",nameList);
        session.setAttribute("locationList",locationList);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
        session.setAttribute("valveSearchitemNum",valveResults.size());
        modelMap.addAttribute("KikisysSearchKeyword",KikisysSearchKeyword);
        modelMap.addAttribute("nameList",nameList);
        modelMap.addAttribute("valveResultsForKikisys",valveResults);
        modelMap.addAttribute("valveSearchitemNum",valveResults.size());

        return "list/valve";
    }
    //弁検索➡機器検索画面へ遷移
    @RequestMapping(value = "/kikiSearch", method = RequestMethod.GET)
    public String kikiSearch(ModelMap modelMap,HttpSession session){
        //get location
        List<String> nameList = (List<String>)session.getAttribute("nameList");
        if(CollectionUtils.isEmpty(nameList)){
            //get location
            User user=(User)session.getAttribute("user");
            List<Location> locationList =new ArrayList<Location>();
            if("admin".equals(user.getDepartment())){
                locationList = locationService.getAllLocation();
            }else{
                locationList = locationService.getLocationByUserDepartment(user.getDepartment());
            }

            for (int i = 0; i < locationList.size(); i++) {
                String tmpLocation= StringUtil.concatWithDelimit(" ",locationList.get(i).getkCodeL(),locationList.get(i).getkCodeM(),locationList.get(i).getkCodeS());
                if(!nameList.contains(locationList.get(i).getkCodeL())){
                    nameList.add(locationList.get(i).getkCodeL());
                }
                if(!nameList.contains(tmpLocation)){
                    nameList.add(tmpLocation);
                }
            }
        }
        //sessionにすでにある場合はsessionを使う
        String locationKikiSearchSelected=(String)session.getAttribute("locationKikiSearchSelected");
        if(locationKikiSearchSelected==null){
            locationKikiSearchSelected="全部会社名";
        }

        List<ValveForDL> itemResults=(List<ValveForDL>)session.getAttribute("kikiSearchitemResults");
        if(CollectionUtils.isEmpty(itemResults)){
            itemResults=new ArrayList<ValveForDL>();
        }else{
            if(!itemResults.get(0).getValve().getLocationName().contains(locationKikiSearchSelected)){
                locationKikiSearchSelected="全部会社名";
                itemResults=new ArrayList<ValveForDL>();
            }
        }
        String keywordMessage=(String)session.getAttribute("keywordMessage");
        if(keywordMessage==null){
            keywordMessage="";
        }
        //弁検索場合は１、機器検索場合は２、部品検索場合は３
        String kikiOrBenFlg="2";
        modelMap.addAttribute("kikiSearchitemResults",itemResults);
        modelMap.addAttribute("kikiSearchitemNum",itemResults.size());
        modelMap.addAttribute("nameList",nameList);
        modelMap.addAttribute("locationKikiSearchSelected",locationKikiSearchSelected);
        session.setAttribute("kikiSearchitemResults",itemResults);
        session.setAttribute("kikiSearchitemNum",itemResults.size());
        session.setAttribute("keywordMessage",keywordMessage);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
        session.setAttribute("nameList",nameList);
        session.setAttribute("locationKikiSearchSelected",locationKikiSearchSelected);
        return "list/valvekikiList";
    }
    //弁検索➡機器検索画面へ遷移
    @RequestMapping(value = "/buhinSearch", method = RequestMethod.GET)
    public String buhinSearch(ModelMap modelMap,HttpSession session){
        //get location
        List<String> nameList = (List<String>)session.getAttribute("nameList");
        if(CollectionUtils.isEmpty(nameList)){
            //get location
            User user=(User)session.getAttribute("user");
            List<Location> locationList =new ArrayList<Location>();
            if("admin".equals(user.getDepartment())){
                locationList = locationService.getAllLocation();
            }else{
                locationList = locationService.getLocationByUserDepartment(user.getDepartment());
            }

            for (int i = 0; i < locationList.size(); i++) {
                String tmpLocation= StringUtil.concatWithDelimit(" ",locationList.get(i).getkCodeL(),locationList.get(i).getkCodeM(),locationList.get(i).getkCodeS());
                if(!nameList.contains(locationList.get(i).getkCodeL())){
                    nameList.add(locationList.get(i).getkCodeL());
                }
                if(!nameList.contains(tmpLocation)){
                    nameList.add(tmpLocation);
                }
            }
        }
        //sessionにすでにある場合はsessionを使う
        String locationBuhinSearchSelected=(String)session.getAttribute("locationBuhinSearchSelected");
        if(locationBuhinSearchSelected==null){
            locationBuhinSearchSelected="全部会社名";
        }

        List<ValveForDL> itemResults=(List<ValveForDL>)session.getAttribute("buhinSearchitemResults");
        if(CollectionUtils.isEmpty(itemResults)){
            locationBuhinSearchSelected="全部会社名";
            itemResults=new ArrayList<ValveForDL>();
        }else{
            if(!itemResults.get(0).getValve().getLocationName().contains(locationBuhinSearchSelected)){
                locationBuhinSearchSelected="全部会社名";
                itemResults=new ArrayList<ValveForDL>();
            }
        }

        String buhinKeywordMessage=(String)session.getAttribute("buhinKeywordMessage");
        if(buhinKeywordMessage==null){
            buhinKeywordMessage="";
        }
        //弁検索場合は１、機器検索場合は２、部品検索場合は３
        String kikiOrBenFlg="3";
        modelMap.addAttribute("buhinSearchitemResults",itemResults);
        modelMap.addAttribute("buhinSearchitemNum",itemResults.size());
        modelMap.addAttribute("nameList",nameList);
        modelMap.addAttribute("locationBuhinSearchSelected",locationBuhinSearchSelected);
        session.setAttribute("buhinSearchitemResults",itemResults);
        session.setAttribute("buhinSearchitemNum",itemResults.size());
        session.setAttribute("buhinKeywordMessage",buhinKeywordMessage);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
        session.setAttribute("nameList",nameList);
        session.setAttribute("locationBuhinSearchSelected",locationBuhinSearchSelected);
        return "list/valvebuhinList";
    }

    //弁、機器、部品複数　検索画面へ遷移
    @RequestMapping(value = "/valveMult", method = RequestMethod.GET)
    public String valveMult(ModelMap modelMap,HttpSession session){
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
            String tmpLocation= StringUtil.concatWithDelimit(" ",locationList.get(i).getkCodeL(),locationList.get(i).getkCodeM(),locationList.get(i).getkCodeS());
            if(!nameList.contains(locationList.get(i).getkCodeL())){
                nameList.add(locationList.get(i).getkCodeL());
            }
            if(!nameList.contains(tmpLocation)){
                nameList.add(tmpLocation);
            }
        }


        //会社名をsessionから取得
        ValveMult  tmpValve=(ValveMult )session.getAttribute("valveMultSearchForSeikak");
        String location="全部会社名";
        if(tmpValve!=null){
            if(!StringUtil.isEmpty(tmpValve.locationName)){
                location=tmpValve.getLocationName();
            }
        }

        //valve取得
        List<Valve> valveMultResults=(List<Valve>)session.getAttribute("valveMultResultsForKikisys");
        if(CollectionUtils.isEmpty(valveMultResults)){
            location="全部会社名";
            valveMultResults=new ArrayList<Valve>();
            tmpValve=new ValveMult();
        }else{
            if(!"admin".equals(user.department)){
                //管理者ユーザ以外場合、弁リストの会社名とユーザの会社名が異なる場合、リセットする
                if(!valveMultResults.get(0).getLocationName().contains(user.department)){
                    location="全部会社名";
                    valveMultResults=new ArrayList<Valve>();
                    tmpValve=new ValveMult();
                }
            }
        }
        //get syukan
        List<Master> syukanList=new LinkedList<Master>();
        syukanList=masterService.getMasterByType("主管係");

        //弁検索場合は１、機器検索場合は２、部品検索場合は３,複数場合は4
        String kikiOrBenFlg="4";
        session.setAttribute("valveMultSearchForSeikak",tmpValve);
        session.setAttribute("locationNameSelectedForMultiValve",location);
        session.setAttribute("valveMultResultsForKikisys",valveMultResults);
        session.setAttribute("nameList",nameList);
        session.setAttribute("syukanList",syukanList);
        session.setAttribute("locationList",locationList);
        session.setAttribute("kikiOrBenFlg",kikiOrBenFlg);
        session.setAttribute("valveMultSearchitemNum",valveMultResults.size());


        return "list/valveMult";
    }

    //懸案検索画面へ遷移
    @RequestMapping(value = "/kenan", method = RequestMethod.GET)
    public String kenan(ModelMap modelMap,HttpSession session){
        User user = (User) session.getAttribute("user");
        String keyword=(String) session.getAttribute("keyword");

        if(keyword!=null){
            modelMap.addAttribute("keyword",keyword);
            session.setAttribute("keyword",keyword);
            return "redirect:/kenan/search";
        }else{
            return "list/kenan";
        }
    }

}
