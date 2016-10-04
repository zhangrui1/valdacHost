<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 12/4/14
  Time: 11:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />
<style type="text/css">
    /* loading */
    #image-box-base #loading {
        display:none;
        position:absolute;
        top:0;left:0;
        width:100%;height:100%;
        background:url("/valdacHost/img/loading.gif") no-repeat center center;
        border-style: none;
        /*background-color:transparent;*/
    }
</style>
<html>
<head>
    <title>工事支援</title>
</head>
<body class="skin-black">
<!-- header logo: style can be found in header.less -->
<c:import url="../htmlframe/headerFrame.jsp"/>
<div class="container">
    <!-- Left side column. contains the logo and sidebar -->

    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <i class="glyphicon glyphicon-wrench"> ${kouji.kjMeisyo}</i>
            <c:choose>
                <c:when test="${kouji.status == '企画中'}">
                    <span class="label label-warning">${kouji.status}</span>
                </c:when>
                <c:when test="${kouji.status == '施工中'}">
                    <span class="label label-warning">${kouji.status}</span>
                </c:when>
                <c:when test="${kouji.status == '評価入力中'}">
                    <span class="label label-warning">${kouji.status}</span>
                </c:when>
                <c:when test="${kouji.status == '工事完了'}">
                    <span class="label label-success">${kouji.status}</span>
                </c:when>

            </c:choose>

        </h1>
        <ol class="breadcrumb" style="font-size:20pt;">
            <li><i class="fa fa-dashboard"></i> <a href="/valdacHost/">工事一覧へ</a></li>
        </ol>
    </section>
    <hr/>
    <input type="hidden" id="koujiId" value="${kouji.id}" />
    <section class="content">

        <div class="row">
            <div class="col-md-11">

                <!--                  -->
                <div class="box box-solid">
                    <div class="box-body">
                        <div class="form-group">

                            <%--<div class="row">--%>
                                <%--<div class="col-md-10">--%>
                                    <%--<div class="progress">--%>
                                        <%--<div id="totalProgressbar" class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="300" style="width: 0%">--%>
                                            <%--<span id="totalProgress"></span>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                    <%--<div class="progress xs progress-striped active">--%>
                                        <%--<div id="progressbar" class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%">--%>
                                            <%--<span id="currentProgress"></span>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--印刷ボタンなど--%>
                            <div class="row">
                                <div class="col-md-2">
                                    <button type="button" class="btn btn-default" onclick="printImage()" value=""/>印刷</button>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" id="firstButton" class="btn btn-default" onclick="turnToFirstPage()" value=""/>先頭へ</button>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" id="previousButton" class="btn btn-default" onclick="turnToPreviousPage()" value=""/>前ページ</button>
                                </div>
                                <div class="col-md-1">
                                    <span id="currentSet">${currentSet}</span>/<span id="totalSet">${totalSet}</span>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" id="NextButton" class="btn btn-default" onclick="turnToNextPage()" value=""/>  次ページ</button>
                                </div>
                                <div class="col-md-2">
                                    <button type="button" id="lastButton" class="btn btn-default" onclick="turnToLastPage()" value=""/>最終へ</button>
                                </div>
                                <div class="col-md-4">
                                    <div class="row">
                                        <div class="col-md-2">
                                            <input type="text" class="form-control input-sm" name="jump_page_num" id="jump_page_num" style="width: 50px;" value=""/>
                                        </div>
                                        <div class="col-md-2">
                                            <button type="button" class="btn btn-default" onclick="turnToNumPage()" value=""/>ページに遷移</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="previewDiv">
                                    <button type="button" class="btn btn-default" onclick="routeImageForKouji(1)" value=""/>右90度</button>
                                    <button type="button" class="btn btn-default" onclick="routeImageForKouji(0)" value=""/>左90度</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                                    <input type="text"   name="index_page_num" id="index_page_num" style="width: 50px;" value=""/>
                                    <button type="button" id="index_num" class="btn btn-default" onclick="changeNumPage()" value=""/>インデックス変更</button>
                                </div>

                            </div>

                            <div class="row previewDiv">
                                <!-- image row -->
                                <!-- image column  -->
                                <div class="col-md-8" id="image-box-base">
                                    <div  id="koujiImg">
                                        <img  id="previewImage" class="previewImage"  style="border:solid 1px silver" width="60%"  src="" width="600" />
                                    </div>
                                    <div id="loading"> </div>
                                    <div id="previewImageName" style="display: none"></div>
                                </div>

                                <!-- select data column -->
                                <div class="col-md-4" id="operate-panel">

                                    <div class="row form-group">

                                        <input type="hidden" id="currentImageObject" value="<c:if test="${not empty firstReportImage}">${firstReportImage.imagename}</c:if>">
                                    </div>

                                    <div class="form-group">
                                        <select class="form-control input-sm imagesyu kengen-operation-readonly" onchange="saveChangeToDatabase(this)" id="currentSyu"><option><c:if test="${not empty firstReportImage}">${firstReportImage.imagesyu}</c:if></option><option>図面仕様書</option><option>点検報告書</option><option>懸案事項一覧</option><option>完成図面</option><option>作業指示書</option><option>弁棒ねじ検索記録</option></select>
                                    </div>
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h3 class="panel-title">関連弁</h3>
                                        </div>
                                        <div class="panel-body">
                                            <%--<div class="form-group">--%>
                                            <div class="row">
                                                <div class="col-md-10">
                                                    <input type="text" name="table_search" id="table_search" class="input-sm pull-left" style="width: 150px;" placeholder="弁番号 Filter">
                                                    <%--<div class="input-group-btn pull-left col-md-5">--%>
                                                    <button class="btn btn-sm btn-default" id="table_search_btn" onclick="findBenByKeywords()"><i class="fa fa-search"></i></button>
                                                    <%--</div>--%>
                                                </div>
                                                <div class="col-md-2">
                                                    <%--<button class="btn btn-default btn-sm kengen-operation  pull-right" onclick="openKanrenben()">+</button>--%>
                                                </div>
                                            </div>
                                            <%--</div>--%>
                                            <div class="form-group">
                                                <div class="ben-table">
                                                    <table class="table table-hover" id="selected-ben-to-image">
                                                        <thead>
                                                        <tr>
                                                            <th>弁番号</th>
                                                            <th>弁名称</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody id="current-select-ben" style="text-align: left">
                                                        <c:forEach items="${firstValveImageList}" var="image">
                                                            <tr>
                                                                <td>${image.vNo}</td>
                                                                <td>${image.benMeisyo}</td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--                -->

            </div><!-- information tab -->
            <div class="col-md-1">
                <div class="row">
                    <div class="col-md-12">
                        <ul class="nav nav-pills nav-stacked bookmarkUl">
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/kenan"><i class="glyphicon glyphicon-floppy-save"> 懸案</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/history"><i class="glyphicon glyphicon-time"> 履歴</i></a></li>
                            <li role="presentation" class="currentBookmark"><a href="/valdacHost/kouji/${kouji.id}/image"><i class="glyphicon glyphicon-picture"> 図面</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/printhtml"><i class="glyphicon glyphicon-download"> DL</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/gpPrinthtml"><i class="glyphicon glyphicon-download"> GP&ICS</i></a></li>
                        </ul>
                    </div>
                </div>
            </div><!-- tab button tab -->
        </div>
    </section>
</div>

    <!-- 弁 リスト modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">関連弁追加</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="ben-table">
                                <div class="well well-sm">
                                <%--<input type="text" name="table_search_kanren" id="table_search_kanren" class="form-control input-sm" placeholder="弁番号 Filter" onkeyup="findBenByKeywordsInKanrenBen(this)" />--%>
                                    <div class="col-md-2 input-group">
                                        <input type="text" name="table_search_kanren" id="table_search_kanren" class="form-control input-sm pull-right" style="width: 150px;" placeholder="弁番号 Filter">
                                        <div class="input-group-btn">
                                            <button class="btn btn-sm btn-default" id="table_search_kanren_btn" onclick="findBenByKeywordsInKanrenBen()"><i class="fa fa-search"></i></button>
                                        </div>
                                    </div>
                            <table id="Allben" class="table table-hover">
                                <thead>
                                <tr>
                                    <th><input id="allCheckbox" type="checkbox" onmouseup="checkAll(this)" /></th>
                                    <th>弁番号</th>
                                    <th>弁名称</th>
                                </tr>
                                </thead>
                                <tbody id="ben-tbody">
                                <c:forEach items="${valveList}" var="valve">
                                    <tr class="valve-item">
                                        <td id="${valve.kikiSysId}"><input type="checkbox" class="select-ben" style="opacity: 10" value="${valve.kikiSysId}" id="select-${valve.kikiSysId}" /></td>
                                        <td>${valve.vNo}</td>
                                        <td>${valve.benMeisyo}</td>
                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                            </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="ben-table">
                                <table id="selected-table" class="table table-hover selected-table">
                                    <c:forEach items="${valveList}" var="valve">
                                        <tr id="selected-${valve.kikiSysId}" class="selected-ben" onclick="unselect(this)">
                                            <th scope="row"><button class="btn btn-xs">x</button></th>
                                            <td>${valve.vNo}</td>
                                            <td>${valve.benMeisyo}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">閉じる</button>
                    <%--<button type="button" class="btn btn-success" onclick="submitKanrenben()">確定</button>--%>
                </div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
//図面用
var BUCKET = 'valdac-construction-aisa'; //テストサーバ用
//var BUCKET = 'valdac-construction-asia'; //本番


    //図面回転用
    valueKouji=0;//kouji 用
    function routeImageForKouji(type){
        var type=type;
        var tmpAngle;

        if(type==1){//右回り
            valueKouji=(valueKouji+90)%360;
            tmpAngle=valueKouji-90;
        }else{//左回り
            valueKouji=(valueKouji-90)%360;
            tmpAngle=valueKouji+90;
        }
        //クリックして回す
        $("#koujiImg img").rotate({
            angle:tmpAngle,
            animateTo: valueKouji,
            easing: $.easing.easeInOutExpo
        });
    };

    function routeImage(value) {
        $("#koujiImg img").rotate({
            angle: 0,
            animateTo: value,
            easing: $.easing.easeInOutExpo
        });
    };

    $(document).ready(function(){
        //ユーザ権限
        var userKengen=$("#userKengen").val();
        if(userKengen=="6"){//管理者
            $(".kengen-operation").show();
        }else{
            $(".kengen-operation").hide();
            $(".kengen-operation-readonly").attr("disabled",true);
            //Index 修正ボタンを非表示
            document.getElementById('index_num').style.display = "none";
            document.getElementById('index_page_num').style.display = "none";
        }

        $("#left-menu-new").addClass("active");

        $(".box-panel").click(function(){
            $(this).next().toggle();
        });

        $(window).load(function () {
            //最初の図面を表示する
            loadImg("${firstReportImage.imagename}");
            document.getElementById('previewImageName').value="${firstReportImage.imagename}";
            //first ボタン設定
            firstAndLastButton(1,"${totalSet}","firstButton","previousButton","NextButton","lastButton");
        });

        var koujiId = document.getElementById("koujiId").value;

        //弁選択
        $('.valve-item').mouseup(function(obj){
            var tr = obj.currentTarget;
            var tdList=$(tr).find('td');
            var id=tdList[0].id;

            console.log("id="+id);
            console.log("checked="+$('#select-'+id).prop('checked'));
            if($('#select-'+id).prop('checked') == true){
                $(tr).removeClass("bg-light-blue-gradient");
                $('#selected-' + id).hide();
                $('#select-'+id).prop('checked', false);
            } else {
                $(tr).addClass("bg-light-blue-gradient");
                $('#selected-' + id).show();
                $('#select-'+id).prop('checked', true);
            }

            //全選択かどうか チェックする
            var booleanAllCheck=true;//全選択かどうか
            var allCheckboxTmp = document.getElementById( "allCheckbox" );
            var selectedList = $('.selected-ben');
            for(var i = 0;i<selectedList.length;i++){
                var tmp=selectedList[i].id;
                var element =document.getElementById(tmp);
                var style =element.style.display;
//                console.log("tmp="+tmp+"   ;style ="+style );
                if(style=="none"){
                    //選択がない場合
                    booleanAllCheck = false;
                }
            }
            if(booleanAllCheck){
                allCheckboxTmp.checked = true;
            }else{
                allCheckboxTmp.checked = false;
            }

        });
    });

    //画像名を取得
    function getImageName(){
        var imageUrl=document.getElementById("previewImageName").value;
        imageUrl=imageUrl.replace("http://storage.googleapis.com/"+BUCKET+"/","");
        return imageUrl;
    }

    function checkAll(obj){
        if(!obj.checked){
            //be selected
            $('.select-ben').prop('checked', true);
            $('.selected-ben').show();
            $('.valve-item').addClass("bg-light-blue-gradient");
        } else {
            $('.select-ben').prop('checked', false);
            $('.selected-ben').hide();
            $('.valve-item').removeClass("bg-light-blue-gradient");
        }
    }

    function printImage(obj){
        var imageUrl=document.getElementById("previewImage").src;
        //画像名を取得
        imageUrl=imageUrl.replace("http://storage.googleapis.com/","");
        if(imageUrl.length>20){
            window.open("https://valdac.power-science.com/print.html?"+imageUrl);
        }
    }



    function updatePage(imagename){
        $.post("/valdacHost/image/updatePageNumber",{"imagename":imagename},function(data){
            var items = JSON.parse(data);
            $("#totalPage").html(items.totalPage);
            $("#currentPage").html(items.currentPage);
        });
    }

    function turnToPreviousPage(){
        var setNum = parseInt(document.getElementById("currentSet").innerHTML);
        setNum = setNum - 1;
        if(setNum > 0) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum+"";
        }
    }

    function turnToFirstPage(){
        var setNum = parseInt(document.getElementById("totalSet").innerHTML);
        if(setNum > 0) {
            turnToSet(1);
            document.getElementById("currentSet").innerHTML = 1+"";
        }
    }

    function turnToLastPage(){
        var setNum = parseInt(document.getElementById("totalSet").innerHTML);
        if(setNum > 0) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum+"";
        }
    }

    function turnToNumPage(){
        var setNum = parseInt(document.getElementById("jump_page_num").value);
        console.log("setNum="+setNum);
        var tolNum = parseInt(document.getElementById("totalSet").innerHTML);
        if(setNum > 0 && setNum<=tolNum) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum+"";
        }else{
            alert("ページ数以内に半角数字を入力ください");
        }
    }

    function turnToNextPage(){
        var setNum = parseInt(document.getElementById("currentSet").innerHTML);
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        setNum = setNum + 1;
        if(setNum <= totalSet) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum + "";
        }
    }

    function turnToThisSet(){
        var setNum = parseInt(document.getElementById('setNum').value);
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        if(0 < setNum && setNum <= totalSet) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum + "";
        }
    }

    function updateCurrentSet(){
        var setNum = parseInt(document.getElementById("currentSet").innerHTML);
        var totalPage = parseInt(document.getElementById("totalPage").innerHTML);
        //update totalset
        if(totalPage>0){
            var totalSet = parseInt(totalPage/6);
            if(totalPage%6 != 0){
                totalSet = totalSet + 1;
            }
        }

        if(0 < setNum && setNum <= totalSet) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum + "";
            document.getElementById("totalSet").innerHTML = totalSet + "";
        }
    }

    function turnToSet(setNumStr){
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        //先頭、最終へボタン設定
        firstAndLastButton(0,0,"firstButton","previousButton","NextButton","lastButton");
        var setNum = parseInt(setNumStr);
        if(setNum >= 1 && setNum <= totalSet){
            setNum = setNum - 1;
            $.post("/valdacHost/image/getReportimageBySet", {"setNum": setNum}, function (data) {
                var items = JSON.parse(data);
                //図面描画
                var tmpImgUrl=items.imagename;
                document.getElementById('previewImageName').value=tmpImgUrl;
                loadImg(tmpImgUrl);
                console.log("over");
                //図面Index番号表示
                document.getElementById('index_page_num').value="";
                //図面種類表示
                $("#currentSyu").html("");
                var htmlitem="<option>"+items.imagesyu+"</option><option>図面仕様書</option><option>点検報告書</option><option>懸案事項一覧</option><option>完成図面</option><option>作業指示書</option><option>弁棒ねじ検索記録</option>";
                $("#currentSyu").html(htmlitem);
                var imagename=items.imagename;
                var koujiId = document.getElementById("koujiId").value;

                $.post("/valdacHost/image/getReportimageKikisystemByKoujiAndImagename",
                        {"koujiId":koujiId,
                            "imagename":imagename},
                        function(data){
                            var items = JSON.parse(data);
                            console.log("items size="+items.length);
                            //current-select-ben
                            var htmlContent = "";
                            for(var i = 0;i<items.length;i++){
                                htmlContent = htmlContent +
                                        "<tr>" +
                                        "<td>" + items[i].vNo + "</td>" +
                                        "<td>" + items[i].benMeisyo + "</td>" +
                                        "</tr>";
                            }
                            console.log("htmlContent="+htmlContent);
                            $("#current-select-ben").html(htmlContent);

                        });
            });
            //図面回転用
            valueKouji=0;//kouji 用
            routeImage(0);
        }
        //先頭、最終へボタン設定
        firstAndLastButton(setNumStr,totalSet,"firstButton","previousButton","NextButton","lastButton");
    }
    //先頭、最終へボタン設定
    function firstAndLastButton(firstNum,lastNum,firstButton,previousButton,NextButton,lastButton){
        var firstButtonName=document.getElementById(firstButton);
        var previousButtonName=document.getElementById(previousButton);
        var NextButtonName=document.getElementById(NextButton);
        var lastButtonName=document.getElementById(lastButton);
        console.log("firstNum="+firstNum+"      lastNum="+lastNum);
        if(firstNum<=1 && lastNum<=1){
            firstButtonName.disabled =true;
            previousButtonName.disabled =true;
            NextButtonName.disabled =true;
            lastButtonName.disabled =true;
        }else if((firstNum==1 && lastNum>1)){
            firstButtonName.disabled =true;
            previousButtonName.disabled =true;
            NextButtonName.disabled =false;
            lastButtonName.disabled =false;
        }else if(firstNum>1 && firstNum==lastNum){
            firstButtonName.disabled =false;
            previousButtonName.disabled =false;
            NextButtonName.disabled =true;
            lastButtonName.disabled =true;
        }else{
            firstButtonName.disabled =false;
            previousButtonName.disabled =false;
            NextButtonName.disabled =false;
            lastButtonName.disabled =false;
        }
    }

    function progressBarController(num){
        $("#progressbar").attr({"aria-valuenow":num});
        $("#progressbar").css({"width":num+"%"});
    }

    function totalProgressBarController(numStr,totalStr){
        var num = parseInt(numStr);
        var totalNum = parseInt(totalStr);
        var p = parseFloat(num/totalNum);
        p = p * 100;
        $("#totalProgressbar").attr({"aria-valuenow":p});
        $("#totalProgressbar").css({"width":p+"%"});

    }

    function saveChangeToDatabase(obj){
        var imagename = getImageName();
        console.log("saveChangeToDatabase imagename="+imagename+"    syurui="+$(obj).val());
        if(imagename.length < 1){
            return false;
        } else {
            //update session
            $.post("/valdacHost/image/updateSyuByImagename",
                    {"imagesyu": $(obj).val(), "object": imagename},
                    function (data) {
//                        $(obj).removeAttr("disabled");
                    });
            //update database
            $.post("/valdacHost/image/saveSyuByImagename",
                    {"imagesyu": $(obj).val(), "object": imagename},
                    function (data) {
//                        $(obj).removeAttr("disabled");
                    });
        }
    }


    function showDelete(obj){
        var content = $(obj).find(".thumbnail-content")[0];
        var btn = $(content).find(".thumbnail-delete-btn").css("opacity","1");
    }
    function hideDelete(obj){
        var content = $(obj).find(".thumbnail-content")[0];
        var btn = $(content).find(".thumbnail-delete-btn").css("opacity","0");
    }

    function submitBikou(){
        if(object.length > 0){
            $("#buhinzubikou").css("border-color","#ccc");
            $("#bikouSubmitButton").css("disabled","true");
            $.post("/valdacHost/image/submitBikouById",{"bikou":$("#buhinzubikou").val(),"object":object},function(data){
                $("#bikouSubmitButton").removeClass("disabled");
                $("#buhinzubikou").css("border-color","#00a65a");
            });
        }
    }

    $(window).unload(function(){
        $.post("/valdacHost/image/saveSessionToDatabase");
    });


    //弁検索
    function findBenByKeywords(){
            var keyword = $("#table_search").val();
            var trs=document.getElementById("selected-ben-to-image");
            var myTr=trs.getElementsByTagName("tr");
            for(var i = 1;i<myTr.length;i++){
                var myTd=myTr[i].getElementsByTagName("td");
                $(myTr[i]).hide();
                for(var j = 0;j<myTd.length;j++){
                    var temp=myTd[j].innerHTML;
                    if(temp.match(keyword)){
                        $(myTr[i]).show();
                        break;
                    }
                }
            }
    }


    //関連弁追加画面の検索
    function changeNumPage(){
        //arrayの中の番号　pageではない
        var setImageNumNew = parseInt(document.getElementById("index_page_num").value);
        var setImageNumOld=parseInt(document.getElementById("currentSet").innerHTML);
        //page番号を修正されたかどうか
        if(setImageNumNew==setImageNumOld){
            console.log("setImageNumNew="+setImageNumNew+"; setImageNumOld"+setImageNumOld);
            return false;
        }
        //page番号を有効値かどうか
        if(setImageNumNew>"${totalSet}" || setImageNumNew<1){
            console.log("範囲外　setImageNumNew="+setImageNumNew);
            return false;
        }


            var koujiId=document.getElementById("koujiId").value;
            var imagename= document.getElementById('previewImageName').value;

            //imagename と　koujiIdにより、page番号を修正する
            $.post("/valdacHost/image/changeImagePage",{"koujiId":koujiId,"imagename":imagename,"pageNew":setImageNumNew,"pageOld":setImageNumOld},function(data){
                console.log("data="+data);
                document.getElementById("currentSet").innerHTML=setImageNumNew;
                //first ボタン設定
                firstAndLastButton(setImageNumNew,"${totalSet}","firstButton","previousButton","NextButton","lastButton");
                alert("更新しました");
            });


    }

    //imgPreloaderを作成
    var imgPreloader=new Image();
    function loadImg(imgUrl) {
        document.getElementById('previewImage').src="/valdacHost/img/black.png";
        var OBJECT_NAME=imgUrl;
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        if(totalSet>0){
            //javaにて　signUrlを取得する
            $.post("/valdacHost/SingUrlTest/SignTest",{"OBJECT_NAME":OBJECT_NAME,"BUCKET_NAME":BUCKET},function(data){
                console.log("data="+data);
                //画像読み込みの直前にLoading画像を表示する
                $("#loading").css({'display':'block'});
                //onloadイベントハンドラ
                imgPreloader.onload=function() {
                    $("#loading").css({'display':'none'});
                    //img 設定
                    document.getElementById('previewImage').src=data;
                }
                imgPreloader.onerror=function(){
                    $("#loading").css({'display':'none'});
                    //エラー画像設定
                    document.getElementById('previewImage').src="/valdacHost/img/error.jpg";
                }
                //url-Set
                imgPreloader.src=data;
            });
        }else{
            document.getElementById('previewImage').src="";
        }

    };
</script>
</html>