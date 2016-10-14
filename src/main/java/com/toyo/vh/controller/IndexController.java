package com.toyo.vh.controller;

import com.toyo.vh.controller.utilities.StringUtil;
import com.toyo.vh.dao.KoujiMapper;
import com.toyo.vh.entity.Kouji;
import com.toyo.vh.entity.Location;
import com.toyo.vh.entity.User;
import com.toyo.vh.service.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lsr on 11/14/14.
 */

@Controller
@RequestMapping("/")
public class IndexController {

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
    LocationService locationService;
    @Resource
    KoujiMapper koujiMapper;
    /**
     * ログイン後のTopページ
     *
     * @return  String 最新作成、更新した弁(１０個)
     *
     * */
    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpSession session, ModelMap modelMap) throws IOException {
        //
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "login";
        } else {
            //初期化
            Map<String,String> indexPath = (Map<String,String>)session.getAttribute("indexPath");

            if(indexPath==null) {
                //Luceneない場合、ログイン画面へ遷移する
               indexPath = luceneIndexService.generateLocalIndex();
                System.out.println("Luceneファイルが見つからない");
            }

            session.setAttribute("indexPath",indexPath);

            //get location
            List<Location> locationList =new ArrayList<Location>();
            if("admin".equals(user.getDepartment())){
                locationList = locationService.getAllLocation();
                //管理者場合、弁検索ページのセッションをリセット
                session.removeAttribute("valveMultResultsForKikisys");
                session.removeAttribute("valveMultSearchForSeikak");
                session.removeAttribute("locationNameSelectedForMultiValve");
            }else{
                locationList = locationService.getLocationByUserDepartment(user.getDepartment());
            }

            List<String> nameList = new LinkedList<String>();
            for (int i = 0; i < locationList.size(); i++) {
                String tmpLocation= StringUtil.concatWithDelimit(" ", locationList.get(i).getkCodeL(), locationList.get(i).getkCodeM(), locationList.get(i).getkCodeS());
                if(!nameList.contains(tmpLocation)){
                    nameList.add(tmpLocation);
                }
            }
            //ログインするたびに初期化
            String location="";
            List<Kouji> koujiresult=new ArrayList<Kouji>();
            if(StringUtil.isEmpty(location)){
                location=nameList.get(0);
                koujiresult=koujiMapper.findKoujiByLocation(location);
            }


            session.setAttribute("locationNameSelectedForKouji",location);
            session.setAttribute("locationKoujiSelectedForKouji",koujiresult);
            session.setAttribute("locationKoujiNum",koujiresult.size());

            modelMap.addAttribute("nameList",nameList);
            session.setAttribute("locationList",locationList);
            modelMap.addAttribute("locationList", locationList);
            return "index";
        }

    }


    /**
     * ログアウト
     * */
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logout(HttpSession session){
        User user=(User)session.getAttribute("user");
        System.out.println("user="+user.department);
        if("admin".equals(user.department)){
            session.removeAttribute("valveMultResultsForKikisys");
            session.removeAttribute("valveMultSearchForSeikak");
            session.removeAttribute("locationNameSelectedForMultiValve");
        }
        session.removeAttribute("user");

        return "login";
    }
}
